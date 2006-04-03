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
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * <p>Persistent  object/bean that defines the user attempt for the MCQ tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_lamc11_usr_attempt
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McUsrAttempt implements Serializable {

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long attemptId;

    /** nullable persistent field */
    private Date attemptTime;

    /** nullable persistent field */
    private String timeZone;
    
    private Integer mark;
    
    private boolean attemptCorrect;
    
    private boolean passed;
    
    private Integer attemptOrder;
    
    private Long queUsrId;
    
    private Long mcQueContentId;

    /** persistent field */
    private org.lamsfoundation.lams.tool.mc.pojos.McQueContent mcQueContent;

    /** persistent field */
    private org.lamsfoundation.lams.tool.mc.pojos.McQueUsr mcQueUsr;

    /** persistent field */
    private org.lamsfoundation.lams.tool.mc.pojos.McOptsContent mcOptionsContent;

    /** full constructor */
    public McUsrAttempt(Long attemptId, Date attemptTime, String timeZone, org.lamsfoundation.lams.tool.mc.pojos.McQueContent mcQueContent, 
    		org.lamsfoundation.lams.tool.mc.pojos.McQueUsr mcQueUsr, org.lamsfoundation.lams.tool.mc.pojos.McOptsContent mcOptionsContent) {
        this.attemptId = attemptId;
        this.attemptTime = attemptTime;
        this.timeZone = timeZone;
        this.mcQueContent = mcQueContent;
        this.mcQueUsr = mcQueUsr;
        this.mcOptionsContent = mcOptionsContent;
    }

    public McUsrAttempt(Date attemptTime, String timeZone, org.lamsfoundation.lams.tool.mc.pojos.McQueContent mcQueContent, 
    		org.lamsfoundation.lams.tool.mc.pojos.McQueUsr mcQueUsr, org.lamsfoundation.lams.tool.mc.pojos.McOptsContent mcOptionsContent) {
        this.attemptTime = attemptTime;
        this.timeZone = timeZone;
        this.mcQueContent = mcQueContent;
        this.mcQueUsr = mcQueUsr;
        this.mcOptionsContent = mcOptionsContent;
    }
    
    public McUsrAttempt(Date attemptTime, String timeZone, org.lamsfoundation.lams.tool.mc.pojos.McQueContent mcQueContent, 
    		org.lamsfoundation.lams.tool.mc.pojos.McQueUsr mcQueUsr, org.lamsfoundation.lams.tool.mc.pojos.McOptsContent mcOptionsContent, Integer mark, boolean passed) {
        this.attemptTime = attemptTime;
        this.timeZone = timeZone;
        this.mcQueContent = mcQueContent;
        this.mcQueUsr = mcQueUsr;
        this.mcOptionsContent = mcOptionsContent;
        this.mark = mark;
        this.passed = passed;
    }
    
    public McUsrAttempt(Date attemptTime, String timeZone, org.lamsfoundation.lams.tool.mc.pojos.McQueContent mcQueContent, 
    		org.lamsfoundation.lams.tool.mc.pojos.McQueUsr mcQueUsr, org.lamsfoundation.lams.tool.mc.pojos.McOptsContent mcOptionsContent, Integer mark, boolean passed, Integer attemptOrder) {
        this.attemptTime = attemptTime;
        this.timeZone = timeZone;
        this.mcQueContent = mcQueContent;
        this.mcQueUsr = mcQueUsr;
        this.mcOptionsContent = mcOptionsContent;
        this.mark = mark;
        this.passed = passed;
        this.attemptOrder=attemptOrder;
    }
    
    public McUsrAttempt(Date attemptTime, String timeZone, org.lamsfoundation.lams.tool.mc.pojos.McQueContent mcQueContent, 
    		org.lamsfoundation.lams.tool.mc.pojos.McQueUsr mcQueUsr, org.lamsfoundation.lams.tool.mc.pojos.McOptsContent mcOptionsContent, Integer mark, boolean passed, Integer attemptOrder, boolean attemptCorrect) {
        this.attemptTime = attemptTime;
        this.timeZone = timeZone;
        this.mcQueContent = mcQueContent;
        this.mcQueUsr = mcQueUsr;
        this.mcOptionsContent = mcOptionsContent;
        this.mark = mark;
        this.passed = passed;
        this.attemptOrder=attemptOrder;
        this.attemptCorrect=attemptCorrect;
    }
      
    
    /** default constructor */
    public McUsrAttempt() {
    }

    /** minimal constructor */
    public McUsrAttempt(Long attemptId, org.lamsfoundation.lams.tool.mc.pojos.McQueContent mcQueContent, org.lamsfoundation.lams.tool.mc.pojos.McQueUsr mcQueUsr, org.lamsfoundation.lams.tool.mc.pojos.McOptsContent mcOptionsContent) {
        this.attemptId = attemptId;
        this.mcQueContent = mcQueContent;
        this.mcQueUsr = mcQueUsr;
        this.mcOptionsContent = mcOptionsContent;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getAttemptId() {
        return this.attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public Date getAttemptTime() {
        return this.attemptTime;
    }

    public void setAttemptTime(Date attemptTime) {
        this.attemptTime = attemptTime;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public org.lamsfoundation.lams.tool.mc.pojos.McQueContent getMcQueContent() {
        return this.mcQueContent;
    }

    public void setMcQueContent(org.lamsfoundation.lams.tool.mc.pojos.McQueContent mcQueContent) {
        this.mcQueContent = mcQueContent;
    }

    public org.lamsfoundation.lams.tool.mc.pojos.McQueUsr getMcQueUsr() {
        return this.mcQueUsr;
    }

    public void setMcQueUsr(org.lamsfoundation.lams.tool.mc.pojos.McQueUsr mcQueUsr) {
        this.mcQueUsr = mcQueUsr;
    }

    public org.lamsfoundation.lams.tool.mc.pojos.McOptsContent getMcOptionsContent() {
        return this.mcOptionsContent;
    }

    public void setMcOptionsContent(org.lamsfoundation.lams.tool.mc.pojos.McOptsContent mcOptionsContent) {
        this.mcOptionsContent = mcOptionsContent;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
    }

	/**
	 * @return Returns the mark.
	 */
	public Integer getMark() {
		return mark;
	}
	/**
	 * @param mark The mark to set.
	 */
	public void setMark(Integer mark) {
		this.mark = mark;
	}
	/**
	 * @return Returns the passed.
	 */
	public boolean isPassed() {
		return passed;
	}
	/**
	 * @param passed The passed to set.
	 */
	public void setPassed(boolean passed) {
		this.passed = passed;
	}
	/**
	 * @return Returns the queUsrId.
	 */
	public Long getQueUsrId() {
		return queUsrId;
	}
	/**
	 * @param queUsrId The queUsrId to set.
	 */
	public void setQueUsrId(Long queUsrId) {
		this.queUsrId = queUsrId;
	}
	/**
	 * @return Returns the attemptOrder.
	 */
	public Integer getAttemptOrder() {
		return attemptOrder;
	}
	/**
	 * @param attemptOrder The attemptOrder to set.
	 */
	public void setAttemptOrder(Integer attemptOrder) {
		this.attemptOrder = attemptOrder;
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
	
	/**
	 * @return Returns the attemptCorrect.
	 */
	public boolean isAttemptCorrect() {
		return attemptCorrect;
	}
	/**
	 * @param attemptCorrect The attemptCorrect to set.
	 */
	public void setAttemptCorrect(boolean attemptCorrect) {
		this.attemptCorrect = attemptCorrect;
	}
}
