/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.gradebook;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author lfoxton
 * 
 * This class maps to one activity mark for a learner
 * 
 * @hibernate.class table="lams_gradebook_user_activity"
 */
public class GradeBookUserActivity {

    private long uid;
    private ToolActivity activity;
    private User learner;
    private Double mark;
    private String feedback;

    public GradeBookUserActivity() {
    }
    
    public GradeBookUserActivity(ToolActivity activity, User learner){
	this.activity = activity;
	this.learner = learner;
    }

    /**
     * @hibernate.id column="uid" generator-class="native" type="java.lang.Long"
     */
    public long getUid() {
	return uid;
    }

    public void setUid(long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="activity_id"
     */
    public ToolActivity getActivity() {
	return activity;
    }

    public void setActivity(ToolActivity activity) {
	this.activity = activity;
    }

    /** 
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="user_id"      
     */
    public User getLearner() {
	return learner;
    }

    public void setLearner(User learner) {
	this.learner = learner;
    }

    /**
     * @hibernate.property column="mark"
     */
    public Double getMark() {
	return mark;
    }

    public void setMark(Double mark) {
	this.mark = mark;
    }

    /**
     * @hibernate.property column="feedback" length="65535"
     */
    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
