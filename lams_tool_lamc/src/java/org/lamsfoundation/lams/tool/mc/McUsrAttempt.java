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

    /** persistent field */
    private org.lamsfoundation.lams.tool.mc.McQueContent mcQueContent;

    /** persistent field */
    private org.lamsfoundation.lams.tool.mc.McQueUsr mcQueUsr;

    /** persistent field */
    private org.lamsfoundation.lams.tool.mc.McOptsContent mcOptionsContent;

    /** full constructor */
    public McUsrAttempt(Long attemptId, Date attemptTime, String timeZone, org.lamsfoundation.lams.tool.mc.McQueContent mcQueContent, 
    		org.lamsfoundation.lams.tool.mc.McQueUsr mcQueUsr, org.lamsfoundation.lams.tool.mc.McOptsContent mcOptionsContent) {
        this.attemptId = attemptId;
        this.attemptTime = attemptTime;
        this.timeZone = timeZone;
        this.mcQueContent = mcQueContent;
        this.mcQueUsr = mcQueUsr;
        this.mcOptionsContent = mcOptionsContent;
    }

    public McUsrAttempt(Date attemptTime, String timeZone, org.lamsfoundation.lams.tool.mc.McQueContent mcQueContent, 
    		org.lamsfoundation.lams.tool.mc.McQueUsr mcQueUsr, org.lamsfoundation.lams.tool.mc.McOptsContent mcOptionsContent) {
        this.attemptTime = attemptTime;
        this.timeZone = timeZone;
        this.mcQueContent = mcQueContent;
        this.mcQueUsr = mcQueUsr;
        this.mcOptionsContent = mcOptionsContent;
    }
    
    
    /** default constructor */
    public McUsrAttempt() {
    }

    /** minimal constructor */
    public McUsrAttempt(Long attemptId, org.lamsfoundation.lams.tool.mc.McQueContent mcQueContent, org.lamsfoundation.lams.tool.mc.McQueUsr mcQueUsr, org.lamsfoundation.lams.tool.mc.McOptsContent mcOptionsContent) {
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

    public org.lamsfoundation.lams.tool.mc.McQueContent getMcQueContent() {
        return this.mcQueContent;
    }

    public void setMcQueContent(org.lamsfoundation.lams.tool.mc.McQueContent mcQueContent) {
        this.mcQueContent = mcQueContent;
    }

    public org.lamsfoundation.lams.tool.mc.McQueUsr getMcQueUsr() {
        return this.mcQueUsr;
    }

    public void setMcQueUsr(org.lamsfoundation.lams.tool.mc.McQueUsr mcQueUsr) {
        this.mcQueUsr = mcQueUsr;
    }

    public org.lamsfoundation.lams.tool.mc.McOptsContent getMcOptionsContent() {
        return this.mcOptionsContent;
    }

    public void setMcOptionsContent(org.lamsfoundation.lams.tool.mc.McOptsContent mcOptionsContent) {
        this.mcOptionsContent = mcOptionsContent;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
    }

}
