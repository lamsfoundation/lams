package org.lamsfoundation.lams.tool.forum.persistence;

/**
 * @author conradb
 *
 * A Wrapper class for uploaded files. An Attachment cannot exist independently
 * and must belong to a Forum.
 * 
 *
 * @hibernate.joined-subclass table="ATTACHMENT"
 * @hibernate.joined-subclass-key column="id"
 *
 * @hibernate.query name="allAttachments" query="from Attachment attachment"
 * @hibernate.query name="getAttachmentbyType" query="from Attachment attachment where attachment.type = ?"
 */
public class Attachment extends GenericEntity {
    protected byte[] data;
    protected boolean type;
    protected String name;
    protected String contentType;
    public final static boolean TYPE_ONLINE = true;
    public final static boolean TYPE_OFFLINE = false;

    /**
     * @hibernate.property column="DATA"
     * 					   type="binary"
     */
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @hibernate.property column="TYPE"
     */
    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @hibernate.property column="NAME"
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
