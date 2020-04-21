package info.dong4j.idea.plugin.client;

import info.dong4j.idea.plugin.enums.CloudEnum;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Map;

import javax.swing.JPanel;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: no company</p>
 * <p>Description: 百度云</p>
 *
 * @author dong4j
 * @email dong4j@gmail.com
 * @since 2019 -07-08 17:07
 */
@Slf4j
@Client(CloudEnum.BAIDU_CLOUD)
public class BaiduOssClient implements OssClient {

    private static OssClient ossClient = null;

    static{
        init();
    }

    /**
     * 如果是第一次使用, ossClient == null, 使用持久化配置初始化 SDK client
     */
    @Contract(pure = true)
    private static void init() {

    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    @Contract(pure = true)
    public static BaiduOssClient getInstance() {
        BaiduOssClient client = (BaiduOssClient)OssClient.INSTANCES.get(CloudEnum.BAIDU_CLOUD);
        if(client == null){
            client = BaiduOssClient.SingletonHandler.SINGLETON;
            OssClient.INSTANCES.put(CloudEnum.BAIDU_CLOUD, client);
        }
        return client;
    }

    /**
     * 使用缓存的 map 映射获取已初始化的 client, 避免创建多个实例
     */
    private static class SingletonHandler {
        private static final BaiduOssClient SINGLETON = new BaiduOssClient();
    }

    /**
     * Set oss client.
     *
     * @param oss the oss
     */
    private void setOssClient(OssClient oss) {
        ossClient = oss;
    }

    /**
     * 实现接口, 获取当前 client type
     *
     * @return the cloud typed
     */
    @Override
    public CloudEnum getCloudType() {
        return CloudEnum.BAIDU_CLOUD;
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
        return this.upload(ossClient, inputStream, fileName);
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

        return this.upload(inputStream,
                           fileName,
                           bucketName,
                           accessKey,
                           secretKey);
    }

    /**
     * test 按钮点击事件后请求, 成功后保留 client, paste 或者 右键 上传时使用
     *
     * @param inputStream the input stream
     * @param fileName    the file name
     * @param bucketName  the bucketName name
     * @param accessKey   the access key
     * @param secretKey   the secret key
     * @return the string
     */
    @NotNull
    @Contract(pure = true)
    public String upload(InputStream inputStream,
                         String fileName,
                         String bucketName,
                         String accessKey,
                         String secretKey) {

        // 1. 使用 SDK 生成 client
        // 2. 调用 SDK 上传文件
        // 3. 计算 hashcode
        // 4. 保存有效的 client
        return "url";
    }

    /**
     * 调用 SDK 上传文件
     *
     * @param ossClient   the oss client
     * @param inputStream the input stream
     * @param fileName    the file name
     * @return the string
     */
    public String upload(@NotNull OssClient ossClient, InputStream inputStream, String fileName) {
        return "";
    }
}
