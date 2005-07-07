package org.lamsfoundation.lams.tool.forum.persistence;

import java.io.InputStream;

/**
 * @author conradb
 *
 * A Wrapper class for uploaded files. An Attachment cannot exist independently
 * and must belong to a Forum.
 * 
 *
 * @hibernate.joined-subclass table="tl_lafrum11_attachment"
 * @hibernate.joined-subclass-key column="id"
 *
 * @hibernate.query name="allAttachments" query="from Attachment attachment"
 * @hibernate.query name="getAttachmentbyType" query="from Attachment attachment where attachment.type = ?"
 */
public class Attachment extends GenericEntity {
    protected Long uuid;
    protected Long version;
    protected String type;
    protected String name;
    protected InputStream inputStream;
    protected String contentType;
    public final static String TYPE_ONLINE = "ONLINE";
    public final static String TYPE_OFFLINE = "OFFLINE";

    /**
     * @hibernate.property column="UUID"
     *
     */
    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    /**
     * @hibernate.property column="VERSION"
     *
     */
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
