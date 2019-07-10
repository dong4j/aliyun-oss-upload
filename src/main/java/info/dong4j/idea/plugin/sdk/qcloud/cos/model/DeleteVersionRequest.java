package info.dong4j.idea.plugin.sdk.qcloud.cos.model;

import info.dong4j.idea.plugin.sdk.qcloud.cos.internal.CosServiceRequest;

import java.io.*;

public class DeleteVersionRequest extends CosServiceRequest implements Serializable {

    /**
     * The name of the bucket containing the version to delete.
     */
    private String bucketName;

    /**
     * The key of the object version to delete.
     */
    private String key;

    /**
     * The version ID uniquely identifying which version of the object to
     * delete.
     */
    private String versionId;

    /**
     * Constructs a new {@link DeleteVersionRequest} object,
     * ready to be executed to
     * delete the version identified by the specified version ID, in the
     * specified bucket and key.
     *
     * @param bucketName
     *            The name of the bucket containing the version to delete.
     * @param key
     *            The key of the object version to delete.
     * @param versionId
     *            The version ID identifying the version to delete.
     *
     */
    public DeleteVersionRequest(String bucketName, String key, String versionId) {
        this.bucketName = bucketName;
        this.key = key;
        this.versionId = versionId;
    }


    /**
     * Gets the name of the Qcloud COS bucket containing the object to delete.
     *
     * @return The name of the Qcloud COS bucket containing the object to delete.
     *
     * @see DeleteVersionRequest#setBucketName(String)
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * Sets the name of the Qcloud COS bucket containing the object to delete.
     *
     * @param bucketName The name of the Qcloud COS bucket containing the object to delete.
     * @see DeleteVersionRequest#getBucketName()
     */
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * Sets the name of the Qcloud COS bucket containing the object to delete and returns this
     * object, enabling additional method calls to be chained together.
     *
     * @param bucketName The name of the Qcloud COS bucket containing the object to delete.
     *
     * @return The updated {@link DeleteVersionRequest} object, enabling additional method calls to
     *         be chained together.
     */
    public DeleteVersionRequest withBucketName(String bucketName) {
        setBucketName(bucketName);
        return this;
    }

    /**
     * Gets the key of the object to delete.
     *
     * @return The key of the object to delete.
     *
     * @see DeleteVersionRequest#setKey(String)
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the key of the object to delete.
     *
     * @param key The key of the object to delete.
     *
     * @see DeleteVersionRequest#getKey()
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Sets the key of the object to delete and returns this object, enabling additional method
     * calls to be chained together.
     *
     * @param key The key of the object to delete.
     *
     * @return The updated {@link DeleteVersionRequest} object, enabling additional method calls to
     *         chained together.
     */
    public DeleteVersionRequest withKey(String key) {
        setKey(key);
        return this;
    }

    /**
     * Gets the version ID uniquely identifying which version of the object
     * to delete.
     *
     * @return The version ID uniquely identifying which version of the object
     *         to delete.
     *
     * @see DeleteVersionRequest#setVersionId(String)
     * @see DeleteVersionRequest#withVersionId(String)
     */
    public String getVersionId() {
        return versionId;
    }

    /**
     * Sets the version ID uniquely identifying which version of the
     * object to delete.
     *
     * @param versionId
     *            The version ID uniquely identifying which version of
     *            the object to delete.
     *
     * @see DeleteVersionRequest#getVersionId()
     * @see DeleteVersionRequest#withVersionId(String)
     */
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    /**
     * Sets the version ID uniquely identifying which version of the object to
     * delete
     * Returns this {@link DeleteVersionRequest}, enabling additional method
     * calls to be chained together.
     *
     * @param versionId
     *            The version ID uniquely identifying which version of the
     *            object to delete.
     *
     * @return This {@link DeleteVersionRequest}, enabling additional method
     *         calls to be chained together.
     *
     * @see DeleteVersionRequest#getVersionId()
     * @see DeleteVersionRequest#setVersionId(String)
     */
    public DeleteVersionRequest withVersionId(String versionId) {
        setVersionId(versionId);
        return this;
    }

}
