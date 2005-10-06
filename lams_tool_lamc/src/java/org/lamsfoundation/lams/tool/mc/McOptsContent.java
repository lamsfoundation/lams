package org.lamsfoundation.lams.tool.mc;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class McOptsContent implements Serializable {
	
    /** identifier field */
    private Long uid;
    
    private Long mcQueOptionId;
    
    /** nullable persistent field */
    private boolean correctOption;

    /** nullable persistent field */
    private String mcQueOptionText;

    /** persistent field */
    private org.lamsfoundation.lams.tool.mc.McQueContent mcQueContent;
    
    /** persistent field */
    private Set mcUsrAttempts;

    
    /** full constructor */
    public McOptsContent(Long mcQueOptionId, boolean correctOption, String mcQueOptionText, org.lamsfoundation.lams.tool.mc.McQueContent mcQueContent) {
    	this.mcQueOptionId=mcQueOptionId;
        this.correctOption = correctOption;
        this.mcQueOptionText = mcQueOptionText;
        this.mcQueContent = mcQueContent;
    }
    
    public McOptsContent(Long mcQueOptionId, boolean correctOption, String mcQueOptionText, org.lamsfoundation.lams.tool.mc.McQueContent mcQueContent, Set mcUsrAttempts) {
    	this.mcQueOptionId=mcQueOptionId;
        this.correctOption = correctOption;
        this.mcQueOptionText = mcQueOptionText;
        this.mcQueContent = mcQueContent;
        this.mcUsrAttempts=mcUsrAttempts;
    }
    
    
    /** default constructor */
    public McOptsContent() {
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    
    public boolean isCorrectOption() {
        return this.correctOption;
    }

    public void setCorrectOption(boolean correctOption) {
        this.correctOption = correctOption;
    }

    public String getMcQueOptionText() {
        return this.mcQueOptionText;
    }

    public void setMcQueOptionText(String mcQueOptionText) {
        this.mcQueOptionText = mcQueOptionText;
    }

    public org.lamsfoundation.lams.tool.mc.McQueContent getMcQueContent() {
        return this.mcQueContent;
    }

    public void setMcQueContent(org.lamsfoundation.lams.tool.mc.McQueContent mcQueContent) {
        this.mcQueContent = mcQueContent;
    }

    
    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
    }

	/**
	 * @return Returns the mcQueOptionId.
	 */
	public Long getMcQueOptionId() {
		return mcQueOptionId;
	}
	/**
	 * @param mcQueOptionId The mcQueOptionId to set.
	 */
	public void setMcQueOptionId(Long mcQueOptionId) {
		this.mcQueOptionId = mcQueOptionId;
	}
	/**
	 * @return Returns the mcUsrAttempts.
	 */
	
	
	public Set getMcUsrAttempts() {
		if (this.mcUsrAttempts == null)
			setMcUsrAttempts(new HashSet());
	    return this.mcUsrAttempts;
	}
	/**
	 * @param mcUsrAttempts The mcUsrAttempts to set.
	 */
	public void setMcUsrAttempts(Set mcUsrAttempts) {
		this.mcUsrAttempts = mcUsrAttempts;
	}
}
