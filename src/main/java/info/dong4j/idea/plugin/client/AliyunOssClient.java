/*
 * MIT License
 *
 * Copyright (c) 2020 dong4j <dong4j@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package info.dong4j.idea.plugin.client;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;

import info.dong4j.idea.plugin.enums.CloudEnum;
import info.dong4j.idea.plugin.settings.AliyunOssState;
import info.dong4j.idea.plugin.settings.MikPersistenComponent;
import info.dong4j.idea.plugin.settings.MikState;
import info.dong4j.idea.plugin.settings.OssState;
import info.dong4j.idea.plugin.util.DES;
import info.dong4j.idea.plugin.util.ImageUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Map;

import javax.swing.JPanel;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: no company</p>
 * <p>Description: 右键上传一次或者点击测试按钮时初始化一次</p>
 *
 * @author dong4j
 * @email dong4j@gmail.com
 * @since 2019 -03-18 09:57
 */
@Slf4j
@Client(CloudEnum.ALIYUN_CLOUD)
public class AliyunOssClient implements OssClient {
    /**
     * The constant URL_PROTOCOL_HTTPS.
     */
    public static final String URL_PROTOCOL_HTTPS = "https";
    private static final String URL_PROTOCOL_HTTP = "http";

    private static String bucketName;
    private static String filedir;
    private static OSS ossClient = null;

    static {
        init();
    }

    /**
     * 如果是第一次使用, ossClient == null
     */
    private static void init() {
        AliyunOssState aliyunOssState = MikPersistenComponent.getInstance().getState().getAliyunOssState();
        String accessKey = aliyunOssState.getAccessKey();
        String accessSecretKey = DES.decrypt(aliyunOssState.getAccessSecretKey(), MikState.ALIYUN);
        String endpoint = aliyunOssState.getEndpoint();

        bucketName = aliyunOssState.getBucketName();

        String tempFileDir = aliyunOssState.getFiledir();
        filedir = StringUtils.isBlank(tempFileDir) ? "" : tempFileDir + "/";

        try {
            ossClient = new OSSClientBuilder().build(endpoint, accessKey, accessSecretKey);
        } catch (Exception ignored) {
        }
    }

    /**
     * Set bucket name.
     *
     * @param newBucketName the new bucket name
     */
    private void setBucketName(String newBucketName) {
        bucketName = newBucketName;
    }

    private void setFiledir(String newFileddir){
        filedir = newFileddir;
    }

    /**
     * Set oss client.
     *
     * @param oss the oss
     */
    private void setOssClient(OSS oss) {
        ossClient = oss;
    }

    /**
     * 静态内部类实现单例
     * 为什么这样实现就是单例的？
     * 1. 因为这个类的实例化是靠静态内部类的静态常量实例化的;
     * 2. INSTANCE 是常量，因此只能赋值一次；它还是静态的，因此随着内部类一起加载;
     * 这样实现有什么好处？
     * 1. 我记得以前接触的懒汉式的代码好像有线程安全问题，需要加同步锁才能解决;
     * 2. 采用静态内部类实现的代码也是懒加载的，只有第一次使用这个单例的实例的时候才加载;
     * 3. 不会有线程安全问题;
     *
     * @return the instance
     */
    @Contract(pure = true)
    public static AliyunOssClient getInstance() {
        AliyunOssClient client = (AliyunOssClient)OssClient.INSTANCES.get(CloudEnum.ALIYUN_CLOUD);
        if(client == null){
            client = SingletonHandler.singleton;
            OssClient.INSTANCES.put(CloudEnum.ALIYUN_CLOUD, client);
        }
        return client;
    }

    /**
     * 使用缓存的 map 映射获取已初始化的 client, 避免创建多个实例
     */
    private static class SingletonHandler {
        private static AliyunOssClient singleton = new AliyunOssClient();
    }

    @Override
    public CloudEnum getCloudType() {
        return CloudEnum.ALIYUN_CLOUD;
    }

    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param inputStream 文件流
     * @param fileName    文件名称 包括后缀名
     * @return 出错返回 "" ,唯一MD5数字签名
     */
    @Override
    public String upload(InputStream inputStream, String fileName) {
        return upload(ossClient, inputStream, fileName);
    }

    /**
     * 直接从面板组件上获取最新配置, 不使用 state
     * {@link info.dong4j.idea.plugin.settings.ProjectSettingsPage#testAndHelpListener()}
     *
     * @param inputStream the input stream
     * @param fileName    the file name
     * @param jPanel      the j panel
     * @return the string
     */
    @Override
    public String upload(InputStream inputStream, String fileName, JPanel jPanel) {
        Map<String, String> map = getTestFieldText(jPanel);

        String bucketName = map.get("bucketName");
        String accessKey = map.get("accessKey");
        String accessSecretKey = map.get("secretKey");
        String endpoint = map.get("endpoint");
        String filedir = map.get("filedir");

        return upload(inputStream,
                      fileName,
                      bucketName,
                      accessKey,
                      accessSecretKey,
                      endpoint,
                      filedir);
    }

    /**
     * test 按钮点击事件后请求, 成功后保留 client, paste 或者 右键 上传时使用
     *
     * @param inputStream     the input stream
     * @param fileName        the file name
     * @param bucketName      the bucketName name
     * @param accessKey       the access key
     * @param accessSecretKey the access secret key
     * @param endpoint        the endpoint
     * @param filedir         the temp file dir
     * @return the string
     */
    private String upload(InputStream inputStream,
                          String fileName,
                          String bucketName,
                          String accessKey,
                          String accessSecretKey,
                          String endpoint,
                          String filedir) {

        filedir = org.apache.commons.lang.StringUtils.isBlank(filedir) ? "" : filedir + "/";

        // 重设 client 相关配置
        AliyunOssClient aliyunOssClient = AliyunOssClient.getInstance();

        aliyunOssClient.setBucketName(bucketName);
        aliyunOssClient.setFiledir(filedir);

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, accessSecretKey);

        String url = aliyunOssClient.upload(ossClient, inputStream, fileName);

        if (StringUtils.isNotBlank(url)) {
            int hashcode = bucketName.hashCode() +
                           accessKey.hashCode() +
                           accessSecretKey.hashCode() +
                           endpoint.hashCode();
            OssState.saveStatus(MikPersistenComponent.getInstance().getState().getAliyunOssState(),
                                hashcode,
                                MikState.OLD_HASH_KEY);
            aliyunOssClient.setOssClient(ossClient);
        }
        return url;
    }

    /**
     * Upload string.
     *
     * @param ossClient the ossClient client
     * @param instream  the instream
     * @param fileName  the file name
     * @return the string
     */
    public String upload(@NotNull OSS ossClient,
                         @NotNull InputStream instream,
                         @NotNull String fileName) {
        try {
            // 创建上传 Object 的 Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(ImageUtils.getImageType(fileName));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            ossClient.putObject(bucketName, filedir + fileName, instream, objectMetadata);
            return getUrl(ossClient, filedir, fileName);
        } catch (IOException | OSSException | ClientException e) {
            log.trace("", e);
        }
        return "";
    }

    /**
     * Gets url.
     *
     * @param ossClient the oss client
     * @param filedir   the filedir
     * @param fileName  the name
     * @return the url
     */
    private String getUrl(@NotNull OSS ossClient, String filedir, String fileName) {
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        URL url = ossClient.generatePresignedUrl(bucketName, filedir + fileName, expiration);
        if (url != null) {
            String[] split = url.toString().split("\\?");
            String uri = split[0];
            if (url.getProtocol().equals(URL_PROTOCOL_HTTP)) {
                uri = uri.replace(URL_PROTOCOL_HTTP, URL_PROTOCOL_HTTPS);
            }
            return uri;
        }
        return "";
    }


}
