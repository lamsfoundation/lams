/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.pojos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;


/**
 * <p>Persistent  object/bean that defines the content for the MCQ tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_lamc11_options_content
 * </p>
 * 
 * @author Ozgur Demirtas
 */ 
public class McOptsContent implements Serializable, Comparable {
	static Logger logger = Logger.getLogger(McOptsContent.class.getName());
	
    /** identifier field */
    private Long uid;
    
    private Long mcQueOptionId;
    
    /** nullable persistent field */
    private boolean correctOption;

    /** nullable persistent field */
    private String mcQueOptionText;

    /** non persistent field */
    private Long mcQueContentId;
	
    /** persistent field */
    private org.lamsfoundation.lams.tool.mc.pojos.McQueContent mcQueContent;
    
    /** persistent field */
    private Set mcUsrAttempts;

    
    public McOptsContent(Long mcQueOptionId, boolean correctOption, String mcQueOptionText, org.lamsfoundation.lams.tool.mc.pojos.McQueContent mcQueContent, Set mcUsrAttempts) {
    	this.mcQueOptionId=mcQueOptionId;
        this.correctOption = correctOption;
        this.mcQueOptionText = mcQueOptionText;
        this.mcQueContent = mcQueContent;
        this.mcUsrAttempts=mcUsrAttempts;
    }
    
    
    public McOptsContent(boolean correctOption, String mcQueOptionText, org.lamsfoundation.lams.tool.mc.pojos.McQueContent mcQueContent, Set mcUsrAttempts) {
    	this.correctOption = correctOption;
        this.mcQueOptionText = mcQueOptionText;
        this.mcQueContent = mcQueContent;
        this.mcUsrAttempts=mcUsrAttempts;
    }
    
    public static McOptsContent newInstance(McOptsContent mcOptsContent,
											McQueContent newMcQueContent)
											
	{
    	McOptsContent newMcOptsContent = new McOptsContent(mcOptsContent.isCorrectOption(),
							    						  mcOptsContent.getMcQueOptionText(),
							    						  newMcQueContent,
														  new TreeSet());
    	return newMcOptsContent;
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

    public org.lamsfoundation.lams.tool.mc.pojos.McQueContent getMcQueContent() {
        return this.mcQueContent;
    }

    public void setMcQueContent(org.lamsfoundation.lams.tool.mc.pojos.McQueContent mcQueContent) {
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
	
	/**
	 * @return Returns the mcQueContentId.
	 */
	public Long getMcQueContentId() {
		return mcQueContentId;
	}
	/**
	 * @param mcQueContentId The mcQueContentId to set.
	 */
	public void setMcQueContentId(Long mcQueContentId) {
		this.mcQueContentId = mcQueContentId;
	}
	
	public int compareTo(Object o)
    {
		McOptsContent optContent = (McOptsContent) o;
        //if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
        if (mcQueOptionId == null)
        	return 1;
		else
			return (int) (mcQueOptionId.longValue() - optContent.mcQueOptionId.longValue());
    }
}
