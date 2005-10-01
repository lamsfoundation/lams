package org.lamsfoundation.lams.tool.mc;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class McQueContent implements Serializable {

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long qaQueContentId;

    /** nullable persistent field */
    private String question;

    /** nullable persistent field */
    private Integer displayOrder;

    /** persistent field */
    private org.lamsfoundation.lams.tool.mc.McContent mcContent;

    /** full constructor */
    public McQueContent(Long qaQueContentId, String question, Integer displayOrder, org.lamsfoundation.lams.tool.mc.McContent mcContent) {
        this.qaQueContentId = qaQueContentId;
        this.question = question;
        this.displayOrder = displayOrder;
        this.mcContent = mcContent;
    }

    /** default constructor */
    public McQueContent() {
    }

    /** minimal constructor */
    public McQueContent(Long qaQueContentId, org.lamsfoundation.lams.tool.mc.McContent mcContent) {
        this.qaQueContentId = qaQueContentId;
        this.mcContent = mcContent;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getQaQueContentId() {
        return this.qaQueContentId;
    }

    public void setQaQueContentId(Long qaQueContentId) {
        this.qaQueContentId = qaQueContentId;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public org.lamsfoundation.lams.tool.mc.McContent getMcContent() {
        return this.mcContent;
    }

    public void setMcContent(org.lamsfoundation.lams.tool.mc.McContent mcContent) {
        this.mcContent = mcContent;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
    }

}
