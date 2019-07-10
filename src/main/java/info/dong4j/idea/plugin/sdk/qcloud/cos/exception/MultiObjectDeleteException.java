package info.dong4j.idea.plugin.sdk.qcloud.cos.exception;

import info.dong4j.idea.plugin.sdk.qcloud.cos.model.DeleteObjectsResult.DeletedObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultiObjectDeleteException extends CosServiceException implements Serializable {

    private static final long serialVersionUID = -1453532693692585751L;

    private final List<DeleteError> errors = new ArrayList<DeleteError>();
    private final List<DeletedObject> deletedObjects = new ArrayList<DeletedObject>();

    public MultiObjectDeleteException(Collection<DeleteError> errors, Collection<DeletedObject> deletedObjects) {
        super("One or more objects could not be deleted");
        this.deletedObjects.addAll(deletedObjects);
        this.errors.addAll(errors);
    }

    /**
     * Always returns {@code null} since this exception represents a
     * "successful" response from the service with no top-level error code. Use
     * {@link #getErrors()} to retrieve a list of objects whose deletion failed,
     * along with the error code and message for each individual failure.
     */
    @Override
    public String getErrorCode() {
        return super.getErrorCode();
    }

    /**
     * Returns the list of successfully deleted objects from this request. If
     * {@link DeleteObjectsRequest#getQuiet()} is true, only error responses
     * will be returned from cos.
     */
    public List<DeletedObject> getDeletedObjects() {
        return deletedObjects;
    }

    /**
     * Returns the list of errors from the attempted delete operation.
     */
    public List<DeleteError> getErrors() {
        return errors;
    }

    /**
     * An error that occurred when deleting an object.
     */
    public static class DeleteError implements Serializable {

        private String key;
        private String versionId;
        private String code;
        private String message;

        /**
         * Returns the key of the object that couldn't be deleted.
         */
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        /**
         * Returns the versionId of the object that couldn't be deleted.
         */
        public String getVersionId() {
            return versionId;
        }

        public void setVersionId(String versionId) {
            this.versionId = versionId;
        }

        /**
         * Returns the status code for the failed delete.
         */
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        /**
         * Returns a description of the failure.
         */
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
