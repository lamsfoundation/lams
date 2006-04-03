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
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;


/**
 * <p>Persistent  object/bean that defines the question content for the MCQ tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_lamc11_que_content
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McQueContent implements Serializable, Comparable {
	static Logger logger = Logger.getLogger(McQueContent.class.getName());

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long mcQueContentId;

    /** nullable persistent field */
    private String question;

    /** nullable persistent field */
    private Integer displayOrder;
    
    /** nullable persistent field */
    private Integer weight;
    
    /** persistent field */
    private boolean disabled;
    
    private String feedbackCorrect;
    
    private String feedbackIncorrect;
    
    
    /** non persistent field */
    private Long mcContentId;
    
    /** persistent field */
    private org.lamsfoundation.lams.tool.mc.pojos.McContent mcContent;
    
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
    
    public McQueContent(String question, Integer displayOrder,  McContent mcContent, Set mcUsrAttempts, Set mcOptionsContents) {
        this.question = question;
        this.displayOrder = displayOrder;
        this.mcContent=mcContent;
        this.mcUsrAttempts = mcUsrAttempts;
        this.mcOptionsContents = mcOptionsContents;
    }
    
    public McQueContent(String question, Integer displayOrder, Integer weight,  boolean disabled, McContent mcContent, Set mcUsrAttempts, Set mcOptionsContents) {
        this.question = question;
        this.displayOrder = displayOrder;
        this.weight = weight;
        this.disabled = disabled;
        this.mcContent=mcContent;
        this.mcUsrAttempts = mcUsrAttempts;
        this.mcOptionsContents = mcOptionsContents;
    }
    
    public McQueContent(String question, Integer displayOrder, Integer weight,  boolean disabled, String feedbackIncorrect, String feedbackCorrect, McContent mcContent, Set mcUsrAttempts, Set mcOptionsContents) {
        this.question = question;
        this.displayOrder = displayOrder;
        this.weight = weight;
        this.disabled = disabled;
        this.feedbackIncorrect = feedbackIncorrect;
        this.feedbackCorrect = feedbackCorrect;
        this.mcContent=mcContent;
        this.mcUsrAttempts = mcUsrAttempts;
        this.mcOptionsContents = mcOptionsContents;
    }
    
    
    public McQueContent(String question, Integer displayOrder, Integer weight, String feedbackCorrect, String feedbackIncorrect, boolean disabled, McContent mcContent, Set mcUsrAttempts, Set mcOptionsContents) {
        this.question = question;
        this.displayOrder = displayOrder;
        this.weight = weight;
        this.disabled = disabled;
        this.feedbackCorrect=feedbackCorrect;
        this.feedbackIncorrect=feedbackIncorrect;
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
    
    public McQueContent(Long mcQueContentId, String question, Integer displayOrder, Integer weight, Set mcUsrAttempts, Set mcOptionsContents) {
        this.mcQueContentId = mcQueContentId;
        this.question = question;
        this.displayOrder = displayOrder;
        this.weight=weight;
        this.mcUsrAttempts = mcUsrAttempts;
        this.mcOptionsContents = mcOptionsContents;
    }
    
    
    
    public McQueContent(String question, Integer displayOrder,  Set mcUsrAttempts, Set mcOptionsContents) {
        this.question = question;
        this.displayOrder = displayOrder;
        this.mcUsrAttempts = mcUsrAttempts;
        this.mcOptionsContents = mcOptionsContents;
    }
    
    

    /** default constructor */
    public McQueContent() {
    }

    /** minimal constructor */
    public McQueContent(Long mcQueContentId, org.lamsfoundation.lams.tool.mc.pojos.McContent mcContent, Set mcUsrAttempts, Set mcOptionsContents) {
        this.mcQueContentId = mcQueContentId;
        this.mcContent = mcContent;
        this.mcUsrAttempts = mcUsrAttempts;
        this.mcOptionsContents = mcOptionsContents;
    }
    
    
    /**
     *  gets called by copyToolContent
     * 
     * Copy constructor
     * @param queContent the original qa question content
     * @return the new qa question content object
     */
    public static McQueContent newInstance(McQueContent queContent,
    										McContent newMcContent)
    										
    {
    	logger.debug("deep copying queContent: " + queContent.getFeedbackIncorrect());
    	McQueContent newQueContent = new McQueContent(queContent.getQuestion(),
													  queContent.getDisplayOrder(),
													  queContent.getWeight(),
													  queContent.isDisabled(),
													  queContent.getFeedbackIncorrect(),
													  queContent.getFeedbackCorrect(),
													  newMcContent,
                                                      new TreeSet(),
                                                      new TreeSet());
    	
    	newQueContent.setMcOptionsContents(queContent.deepCopyMcOptionsContent(newQueContent));
    	return newQueContent;
    }
    
    
    public Set deepCopyMcOptionsContent(McQueContent newQueContent)
    {
    	Set newMcOptionsContent = new TreeSet();
        for (Iterator i = this.getMcOptionsContents().iterator(); i.hasNext();)
        {
            McOptsContent mcOptsContent = (McOptsContent) i.next();
            McOptsContent mcNewOptsContent= McOptsContent.newInstance(mcOptsContent,newQueContent);
            
            if (mcNewOptsContent.getMcQueContent() != null)
            {
            	newMcOptionsContent.add(mcNewOptsContent);
            }
        }
        return newMcOptionsContent;
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

    public org.lamsfoundation.lams.tool.mc.pojos.McContent getMcContent() {
        return this.mcContent;
    }

    public void setMcContent(org.lamsfoundation.lams.tool.mc.pojos.McContent mcContent) {
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
	
	/**
	 * @return Returns the mcContentId.
	 */
	public Long getMcContentId() {
		return mcContentId;
	}
	/**
	 * @param mcContentId The mcContentId to set.
	 */
	public void setMcContentId(Long mcContentId) {
		this.mcContentId = mcContentId;
	}
	/**
	 * @return Returns the disabled.
	 */
	public boolean isDisabled() {
		return disabled;
	}
	/**
	 * @param disabled The disabled to set.
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	/**
	 * @return Returns the weight.
	 */
	public Integer getWeight() {
		return weight;
	}
	/**
	 * @param weight The weight to set.
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	/**
	 * @return Returns the feedbackCorrect.
	 */
	public String getFeedbackCorrect() {
		return feedbackCorrect;
	}
	/**
	 * @param feedbackCorrect The feedbackCorrect to set.
	 */
	public void setFeedbackCorrect(String feedbackCorrect) {
		this.feedbackCorrect = feedbackCorrect;
	}
	/**
	 * @return Returns the feedbackIncorrect.
	 */
	public String getFeedbackIncorrect() {
		return feedbackIncorrect;
	}
	/**
	 * @param feedbackIncorrect The feedbackIncorrect to set.
	 */
	public void setFeedbackIncorrect(String feedbackIncorrect) {
		this.feedbackIncorrect = feedbackIncorrect;
	}
	
	public int compareTo(Object o)
    {
        McQueContent queContent = (McQueContent) o;
        //if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
        if (mcQueContentId == null)
        	return 1;
		else
			return (int) (mcQueContentId.longValue() - queContent.mcQueContentId.longValue());
    }
}
