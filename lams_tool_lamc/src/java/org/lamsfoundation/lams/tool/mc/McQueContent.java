package org.lamsfoundation.lams.tool.mc;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class McQueContent implements Serializable {

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long mcQueContentId;

    /** nullable persistent field */
    private String question;

    /** nullable persistent field */
    private Integer displayOrder;

    /** persistent field */
    private org.lamsfoundation.lams.tool.mc.McContent mcContent;
    
        
    /** persistent field */
    private Set mcUsrAttempts;

    /** persistent field */
    private Set mcOptionsContents;

    /** full constructor */
    public McQueContent(Long mcQueContentId, String question, Integer displayOrder,  McContent mcContent, Set mcUsrAttempts, Set mcOptionsContents) {
        this.mcQueContentId = mcQueContentId;
        this.question = question;
        this.displayOrder = displayOrder;
        this.mcContent=mcContent;
        this.mcUsrAttempts = mcUsrAttempts;
        this.mcOptionsContents = mcOptionsContents;
    }
    
    public McQueContent(Long mcQueContentId, String question, Integer displayOrder,  Set mcUsrAttempts, Set mcOptionsContents) {
        this.mcQueContentId = mcQueContentId;
        this.question = question;
        this.displayOrder = displayOrder;
        this.mcUsrAttempts = mcUsrAttempts;
        this.mcOptionsContents = mcOptionsContents;
    }
    
    

    /** default constructor */
    public McQueContent() {
    }

    /** minimal constructor */
    public McQueContent(Long mcQueContentId, org.lamsfoundation.lams.tool.mc.McContent mcContent, Set mcUsrAttempts, Set mcOptionsContents) {
        this.mcQueContentId = mcQueContentId;
        this.mcContent = mcContent;
        this.mcUsrAttempts = mcUsrAttempts;
        this.mcOptionsContents = mcOptionsContents;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getMcQueContentId() {
        return this.mcQueContentId;
    }

    public void setMcQueContentId(Long mcQueContentId) {
        this.mcQueContentId = mcQueContentId;
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

    public Set getMcUsrAttempts() {
    	if (this.mcUsrAttempts == null)
        	setMcUsrAttempts(new HashSet());
        return this.mcUsrAttempts;
    }

    
    public void setMcUsrAttempts(Set mcUsrAttempts) {
        this.mcUsrAttempts = mcUsrAttempts;
    }

    
    public Set getMcOptionsContents() {
    	if (this.mcOptionsContents == null)
        	setMcOptionsContents(new HashSet());
        return this.mcOptionsContents;
    }

    public void setMcOptionsContents(Set mcOptionsContents) {
        this.mcOptionsContents = mcOptionsContents;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
    }
	
}
