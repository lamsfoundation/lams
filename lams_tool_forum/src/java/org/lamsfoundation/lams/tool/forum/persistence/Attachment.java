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
    protected Long uuid;
    protected boolean type;
    protected String name;
    public final static boolean TYPE_ONLINE = true;
    public final static boolean TYPE_OFFLINE = false;

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
     * @hibernate.property column="TYPE"
     */
    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
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
