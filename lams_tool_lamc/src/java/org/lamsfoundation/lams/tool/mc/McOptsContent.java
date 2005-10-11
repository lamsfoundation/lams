/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

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

    
    public McOptsContent(Long mcQueOptionId, boolean correctOption, String mcQueOptionText, org.lamsfoundation.lams.tool.mc.McQueContent mcQueContent, Set mcUsrAttempts) {
    	this.mcQueOptionId=mcQueOptionId;
        this.correctOption = correctOption;
        this.mcQueOptionText = mcQueOptionText;
        this.mcQueContent = mcQueContent;
        this.mcUsrAttempts=mcUsrAttempts;
    }
    
    
    public McOptsContent(boolean correctOption, String mcQueOptionText, org.lamsfoundation.lams.tool.mc.McQueContent mcQueContent, Set mcUsrAttempts) {
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
