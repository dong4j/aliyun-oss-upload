package info.dong4j.idea.plugin.client;

import info.dong4j.idea.plugin.enums.CloudEnum;
import info.dong4j.idea.plugin.settings.MikPersistenComponent;
import info.dong4j.idea.plugin.settings.MikState;
import info.dong4j.idea.plugin.settings.OssState;
import info.dong4j.idea.plugin.settings.TencentOssState;
import info.dong4j.idea.plugin.util.DES;
import info.dong4j.idea.plugin.util.QcloudCosUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Map;

import javax.swing.JPanel;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: no company</p>
 * <p>Description: oss client 实现步骤:
 * 1. 初始化配置: 从持久化配置中初始化 client
 * 2. 静态内部类获取 client 单例
 * 3. 实现 OssClient 接口
 * 4. 自定义 upload 逻辑</p>
 *
 * @author dong4j
 * @version x.x.x
 * @email dong4j @gmail.com
 * @date 2020.04.22 01:17
 * @since 2019 -07-08 16:39
 */
@Slf4j
@Client(CloudEnum.TENCENT_CLOUD)
public class TencentOssClient implements OssClient {
    /** isAuth */
    private static boolean isAuth = false;
    /** bucketName */
    private static String bucketName;
    /** regionName */
    private static String regionName;
    private static String accessKey;
    private static String accessSecretKey;

    static {
        init();
    }

    /**
     * 如果是第一次使用, ossClient == null, 使用持久化配置初始化
     * 1. 如果是第一次设置, 获取的持久化配置为 null, 则初始化 ossClient 失败
     */
    private static void init() {
        TencentOssState tencentOssState = MikPersistenComponent.getInstance().getState().getTencentOssState();
        bucketName = tencentOssState.getBucketName();
        accessKey = tencentOssState.getAccessKey();
        accessSecretKey = DES.decrypt(tencentOssState.getSecretKey(), MikState.TENCENT);
        regionName = tencentOssState.getRegionName();

    }

    /**
     * Sets bucket name *
     *
     * @param newBucketName new bucket name
     */
    private void setBucketName(String newBucketName) {
        bucketName = newBucketName;
    }

    /**
     * Sets region name *
     *
     * @param newRegionName new region name
     */
    private void setRegionName(String newRegionName) {
        regionName = newRegionName;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    @Contract(pure = true)
    public static TencentOssClient getInstance() {
        TencentOssClient client = (TencentOssClient) OssClient.INSTANCES.get(CloudEnum.TENCENT_CLOUD);
        if (client == null) {
            client = SingletonHandler.SINGLETON;
            OssClient.INSTANCES.put(CloudEnum.TENCENT_CLOUD, client);
        }
        return client;
    }

    /**
     * 使用缓存的 map 映射获取已初始化的 client, 避免创建多个实例
     *
     * @author dong4j
     * @version x.x.x
     * @email "mailto:dongshijie@fkhwl.com"
     * @date 2020.04.22 01:17
     */
    private static class SingletonHandler {
        /** SINGLETON */
        private static final TencentOssClient SINGLETON = new TencentOssClient();
    }

    /**
     * 实现接口, 获取当前 client type
     *
     * @return the cloud typed
     */
    @Override
    public CloudEnum getCloudType() {
        return CloudEnum.TENCENT_CLOUD;
    }

    /**
     * 通过文件流上传文件
     *
     * @param inputStream the input stream
     * @param fileName    the file name
     * @return the string
     */
    @Override
    public String upload(InputStream inputStream, String fileName) {
        // 拼接 url = <BucketName-APPID>.cos.region_name.myqcloud.com/key
        return QcloudCosUtils.putObject(inputStream,
                                        fileName,
                                        accessKey,
                                        accessSecretKey,
                                        bucketName,
                                        regionName);
    }

    /**
     * 在设置界面点击 'Test' 按钮上传时调用, 通过 JPanel 获取当前配置
     * {@link info.dong4j.idea.plugin.settings.ProjectSettingsPage#testAndHelpListener()}
     *
     * @param inputStream the input stream
     * @param fileName    the file name
     * @param jPanel      the j panel
     * @return the string
     */
    @Override
    public String upload(InputStream inputStream, String fileName, JPanel jPanel) {
        Map<String, String> map = this.getTestFieldText(jPanel);
        String bucketName = map.get("bucketName");
        String accessKey = map.get("accessKey");
        String secretKey = map.get("secretKey");
        String regionName = map.get("regionName");

        return this.upload(inputStream,
                           fileName,
                           bucketName,
                           accessKey,
                           secretKey,
                           regionName);
    }

    /**
     * test 按钮点击事件后请求, 成功后保留 client, paste 或者 右键 上传时使用
     *
     * @param inputStream the input stream
     * @param fileName    the file name
     * @param bucketName  the bucketName name
     * @param accessKey   the access key
     * @param secretKey   the secret key
     * @param regionName  the region name
     * @return the string
     */
    @NotNull
    @Contract(pure = true)
    public String upload(InputStream inputStream,
                         String fileName,
                         String bucketName,
                         String accessKey,
                         String secretKey,
                         String regionName) {

        TencentOssClient tencentOssClient = TencentOssClient.getInstance();

        this.setBucketName(bucketName);
        this.setRegionName(regionName);

        String url = tencentOssClient.upload(inputStream, fileName);

        if (StringUtils.isNotBlank(url)) {
            int hashcode = bucketName.hashCode() +
                           secretKey.hashCode() +
                           accessKey.hashCode() +
                           regionName.hashCode();
            // 更新可用状态
            OssState.saveStatus(MikPersistenComponent.getInstance().getState().getTencentOssState(),
                                hashcode,
                                MikState.OLD_HASH_KEY);
            isAuth = true;
        }
        return url;
    }
}
