package info.dong4j.idea.plugin.sdk.qcloud.cos.model;

import info.dong4j.idea.plugin.sdk.qcloud.cos.utils.Base64;

import java.io.*;

import javax.crypto.SecretKey;


/**
 * Represents a customer provided key for use with Qcloud COS server-side
 * encryption.
 *
 * Server-side encryption is about data encryption at rest, that is, Qcloud COS
 * encrypts your data as it writes it to disks in its data centers and decrypts
 * it for you when you access it. Qcloud COS manages encryption and decryption
 * for you.
 *
 * This class allows you to specify your own encryption key for Qcloud COS to use
 * when encrypting your data on the server-side, instead of allowing Qcloud to
 * automatically generate an encryption key for you.
 *
 */
public class SSECustomerKey implements Serializable {
    private final String base64EncodedKey;
    private String base64EncodedMd5;
    private String algorithm;


    /**
     * Constructs a new customer provided server-side encryption key using the specified
     * base64-encoded key material.
     *
     * By default, this is assumed to be an AES-256 key, but the key algorithm
     * can be set through the {@link #setAlgorithm(String)} method.
     *
     * Currently, Qcloud COS only supports AES-256 encryption keys.
     *
     * @param base64EncodedKey
     *            The base 64 encoded encryption key material.
     */
    public SSECustomerKey(String base64EncodedKey) {
        if (base64EncodedKey == null || base64EncodedKey.length() == 0)
            throw new IllegalArgumentException("Encryption key must be specified");

        // Default to AES-256 encryption
        this.algorithm = SSEAlgorithm.AES256.getAlgorithm();
        this.base64EncodedKey = base64EncodedKey;
    }

    /**
     * Constructs a new customer provided server-side encryption key using the
     * specified raw key material.
     *
     * By default, this is assumed to be an AES-256 key, but the key algorithm
     * can be set through the {@link #setAlgorithm(String)} method.
     *
     * Currently, Qcloud COS only supports AES-256 encryption keys.
     *
     * @param rawKeyMaterial
     *            The raw bytes of the customer provided encryption key.
     */
    public SSECustomerKey(byte[] rawKeyMaterial) {
        if (rawKeyMaterial == null || rawKeyMaterial.length == 0)
            throw new IllegalArgumentException("Encryption key must be specified");

        // Default to AES-256 encryption
        this.algorithm = SSEAlgorithm.AES256.getAlgorithm();
        this.base64EncodedKey = Base64.encodeAsString(rawKeyMaterial);
    }

    /**
     * Constructs a new customer provided server-side encryption key using the
     * specified SecretKey.
     *
     * By default, this is assumed to be an AES-256 key, but the key algorithm
     * can be set through the {@link #setAlgorithm(String)} method.
     *
     * Currently, Qcloud COS only supports AES-256 encryption keys.
     *
     * @param key
     *            The customer provided server-side encryption key.
     */
    public SSECustomerKey(SecretKey key) {
        if (key == null)
            throw new IllegalArgumentException("Encryption key must be specified");

        // Default to AES-256 encryption
        this.algorithm = SSEAlgorithm.AES256.getAlgorithm();
        this.base64EncodedKey = Base64.encodeAsString(key.getEncoded());
    }

    /**
     * Constructs a new SSECustomerKey instance with key as null.
     */
    private SSECustomerKey() {
        this.base64EncodedKey = null;
    }


    /**
     * Returns the base64-encoded server-side encryption key that was provided
     * in this object's constructor.
     *
     * @return The base64-encoded server-side encryption key.
     */
    public String getKey() {
        return base64EncodedKey;
    }

    /**
     * Returns the encryption algorithm to use with this customer-provided
     * server-side encryption key.
     *
     * Currently, "AES256" is the only supported algorithm.
     *
     * @return The encryption algorithm to use with this customer-provided
     *         server-side encryption key.
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Sets the encryption algorithm to use with this customer-provided
     * server-side encryption key.
     *
     * Currently, "AES256" is the only supported algorithm.
     *
     * @see SSEAlgorithm#AES256
     *
     * @param algorithm
     *            The server-side encryption algorithm to use with this
     *            customer-provided server-side encryption key.
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Sets the encryption algorithm to use with this customer-provided
     * server-side encryption key, and returns this object so that method calls
     * can be chained together.
     *
     * Currently, "AES256" is the only supported algorithm.
     *
     * @see SSEAlgorithm#AES256
     *
     * @param algorithm
     *            The server-side encryption algorithm to use with this
     *            customer-provided server-side encryption key.
     *
     * @return The updated ServerSideEncryptionKey object, so that method calls
     *         may be chained together.
     */
    public SSECustomerKey withAlgorithm(String algorithm) {
        setAlgorithm(algorithm);
        return this;
    }

    /**
     * Returns the optional base64-encoded MD5 digest of the encryption key to
     * use when validating the integrity of the transmitted server-side
     * encryption key.
     *
     * If a MD5 digest is not explicitly specified, then it will be
     * automatically calculated.
     *
     * @return The base64-encoded MD5 digest of this customer-provided
     *         server-side encryption key.
     */
    public String getMd5() {
        return base64EncodedMd5;
    }

    /**
     * Sets the optional MD5 digest (base64-encoded) of the encryption key to use when
     * encrypting the object. This will be used as a message integrity check
     * that the key was transmitted without error. If not set, the SDK will fill
     * in this value by calculating the MD5 digest of the secret key, before
     * sending the request.
     *
     * @param md5Digest
     *            The MD5 digest (base64-encoded) of the encryption key to use
     *            when encrypting the object.
     */
    public void setMd5(String md5Digest) {
        this.base64EncodedMd5 = md5Digest;
    }

    /**
     * Sets the optional MD5 digest (base64-encoded) of the encryption key to
     * use when encrypting the object, and returns the updated object so that
     * additional method calls can be chained together. This will be used as a
     * message integrity check that the key was transmitted without error. If
     * not set, the SDK will fill in this value by calculating the MD5 digest of
     * the secret key, before sending the request.
     *
     * @param md5Digest
     *            The MD5 digest (base64-encoded) of the encryption key to use
     *            when encrypting the object.
     *
     * @return The updated object, so that additional method calls can be
     *         chained together.
     */
    public SSECustomerKey withMd5(String md5Digest) {
        setMd5(md5Digest);
        return this;
    }

    /**
     * Constructs a new SSECustomerKey that can be used for generating the
     * presigned URL's.
     *
     * Currently, "AES256" is the only supported algorithm.
     *
     * @see SSEAlgorithm#AES256
     *
     * @param algorithm
     *            The server-side encryption algorithm to use with this
     *            customer-provided server-side encryption key; must not be
     *            null.
     *
     * @throws IllegalArgumentException
     *             if the input parameter is null.
     */
    public static SSECustomerKey generateSSECustomerKeyForPresignUrl(
            String algorithm) {
        if (algorithm == null)
            throw new IllegalArgumentException();
        return new SSECustomerKey().withAlgorithm(algorithm);
    }
}
