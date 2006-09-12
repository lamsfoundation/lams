/****************************************************************
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
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.survey.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.NodeKey;

/**
 * Survey
 * @author Dapeng Ni
 *
 * @hibernate.class  table="tl_lasurv11_question"
 *
 */
public class SurveyQuestion  implements Cloneable{
	private static final Logger log = Logger.getLogger(SurveyQuestion.class);
	
	private Long uid;
	//Survey Type:1=Single Choice,2=Multiple Choice,3=Test Entry
	private short type;
	
	private String description;
	
	private boolean appendText;
	private boolean compulsory;
	private int maxAnswsers;
	
	private Set<SurveyOption> options;
	
	private Date createDate;
	private SurveyUser createBy;
	
	//***********************************************
	//DTO fields:
	private boolean complete;
	
    public Object clone(){
    	SurveyQuestion obj = null;
		try {
			obj = (SurveyQuestion) super.clone();
//			clone attachment
  			if(options != null){
  				Iterator iter = options.iterator();
  				Set set = new HashSet();
  				while(iter.hasNext()){
  					SurveyOption instruct = (SurveyOption)iter.next(); 
  					SurveyOption newInsruct = (SurveyOption) instruct.clone();
					set.add(newInsruct);
  				}
  				obj.options = set;
  			}
			((SurveyQuestion)obj).setUid(null);
  			//clone ReourceUser as well
  			if(this.createBy != null)
  				((SurveyQuestion)obj).setCreateBy((SurveyUser) this.createBy.clone());
  			
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + SurveyQuestion.class + " failed");
		}
		
		return obj;
	}	
//    **********************************************************
	  	//		Get/Set methods
//	  **********************************************************
		/**
		 * @hibernate.id generator-class="identity" type="java.lang.Long" column="uid"
		 * @return Returns the uid.
		 */
		public Long getUid() {
			return uid;
		}
		/**
		 * @param uid The uid to set.
		 */
		public void setUid(Long userID) {
			this.uid = userID;
		}
		
		/**
		 * @hibernate.property column="description"
		 * @return
		 */
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
		/**
	     * @hibernate.set   lazy="false"
	     * 					cascade="all-delete-orphan"
	     * 					inverse="false"
	     * 					order-by="sequence_id asc"
	     * @hibernate.collection-key column="option_uid"
	     * @hibernate.collection-one-to-many
	     * 			class="org.lamsfoundation.lams.tool.survey.model.SurveyOption"
	     * @return
		 */
		public Set<SurveyOption> getOptions() {
			return options;
		}
		public void setOptions(Set<SurveyOption> itemInstructions) {
			this.options = itemInstructions;
		}
		
		/**
	     * @hibernate.many-to-one
	     *     cascade="none"
	     * 		column="create_by"
		 * 
		 * @return
		 */
		public SurveyUser getCreateBy() {
			return createBy;
		}
		public void setCreateBy(SurveyUser createBy) {
			this.createBy = createBy;
		}
		/**
		 * @hibernate.property column="create_date" 
		 * @return
		 */
		public Date getCreateDate() {
			return createDate;
		}
		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}

		/**
		 * @hibernate.property column="question_type" 
		 * @return
		 */
		public short getType() {
			return type;
		}
		public void setType(short type) {
			this.type = type;
		}
	    /**
	     * @hibernate.property  column="append_text" 
	     * @return
	     */
	    public boolean isAppendText() {
	    	return appendText;
	    }
	    public void setAppendText(boolean appendText) {
	    	this.appendText = appendText;
	    }
	    
	    /**
	     * @hibernate.property  column="compulsory" 
	     * @return
	     */
		public boolean isCompulsory() {
			return compulsory;
		}
		public void setCompulsory(boolean compulsory) {
			this.compulsory = compulsory;
		}
	    
	    /**
	     * @hibernate.property  column="max_answers" 
	     * @return
	     */		
		public int getMaxAnswsers() {
			return maxAnswsers;
		}
		public void setMaxAnswsers(int maxAnswsers) {
			this.maxAnswsers = maxAnswsers;
		}
}
