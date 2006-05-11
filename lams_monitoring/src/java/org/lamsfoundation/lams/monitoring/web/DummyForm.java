/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.monitoring.web;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * See DummyMonitoringAction
 * @struts:form name="DummyForm"
 */
public class DummyForm extends ActionForm {

	private Logger log = Logger.getLogger(DummyForm.class);
	private String title;
	private String desc;
	private Long learningDesignId;
	private Integer startDay;
	private Integer startMonth;
	private Integer startYear;
	private Integer startHour;
	private Integer startMinute;
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		
	}
	

	/**
	 * @return Returns the desc.
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc The desc to set.
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return Returns the learningDesignId.
	 */
	public Long getLearningDesignId() {
		return learningDesignId;
	}
	/**
	 * @param learningDesignId The learningDesignId to set.
	 */
	public void setLearningDesignId(Long learningDesignId) {
		this.learningDesignId = learningDesignId;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	public Integer getStartDay() {
		return startDay;
	}


	public void setStartDay(Integer startDay) {
		this.startDay = startDay;
	}


	public Integer getStartHour() {
		return startHour;
	}


	public void setStartHour(Integer startHour) {
		this.startHour = startHour;
	}


	public Integer getStartMinute() {
		return startMinute;
	}


	public void setStartMinute(Integer startMinute) {
		this.startMinute = startMinute;
	}


	public Integer getStartMonth() {
		return startMonth;
	}


	public void setStartMonth(Integer startMonth) {
		this.startMonth = startMonth;
	}


	public Integer getStartYear() {
		return startYear;
	}


	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}
	
	public Calendar getStartDate() {
		if ( getStartDay() != null && getStartDay().intValue() > 0 ) {
			Calendar cal = new GregorianCalendar();
			cal.set(Calendar.DAY_OF_MONTH, getStartDay().intValue());
			cal.set(Calendar.MONTH, getStartMonth() != null ? getStartMonth().intValue()-1 : 1);
			cal.set(Calendar.YEAR, getStartYear() != null ? getStartYear().intValue() : 0);
			cal.set(Calendar.HOUR_OF_DAY, getStartHour() != null ? getStartHour().intValue() : 0);
			cal.set(Calendar.MINUTE, getStartMinute() != null ? getStartMinute().intValue() : 0);
			return cal;
		}
		return null;
	}
}