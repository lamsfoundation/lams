/******************************************************************************
 * LamstwoBean.java - created by Sakai App Builder -AZ
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.lamsfoundation.lams.integrations.sakai.tool.jsf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic;
import org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem;

/**
 * This is a backing bean for the JSF app which handles the events and
 * sends the information from the logic layer to the UI
 * @author Sakai App Builder -AZ
 */
public class LamstwoBean {

	private static Log log = LogFactory.getLog(LamstwoBean.class);

	private static final String TEXT_DEFAULT = "";
	private static final String INTRODUCTION_DEFAULT = "";
	private static final Long SEQUENCE_ID_DEFAULT = 0L;
	private static final Long LESSON_ID_DEFAULT = 0L;
	private static final Boolean HIDDEN_DEFAULT = Boolean.FALSE;
	private static final Boolean SCHEDULE_DEFAULT = Boolean.FALSE;
	private static final Date START_DATE_DEFAULT = new Date(0); // TODO do we need this ?
	
	private static final long ONE_WEEK = 604800000; // Number of milliseconds in a week.
	
	private static final Integer AUTHORING_MODE = new Integer(1);
	private static final Integer MONITORING_MODE = new Integer(2);
	
	private DataModel itemsModel;
	private LamstwoItemWrapper currentItem = null;

	private LamstwoLogic logic;
	public void setLogic(LamstwoLogic logic) {
		this.logic = logic;
	}

	private String itemText = TEXT_DEFAULT;
	public String getItemText() {
		return itemText;
	}
	public void setItemText(String itemText) {
		this.itemText = itemText;
	}

	private String itemIntroduction = INTRODUCTION_DEFAULT;
	public String getItemIntroduction() {
		return itemIntroduction;
	}
	public void setItemIntroduction(String itemIntroduction) {
		this.itemIntroduction = itemIntroduction;
	}
	
	private Long itemSequenceId = SEQUENCE_ID_DEFAULT;
	public Long getItemSequenceId() {
		return itemSequenceId;
	}
	public void setItemSequenceId(Long itemSequenceId) {
		this.itemSequenceId = itemSequenceId;
	}
	
	private Long itemLessonId = LESSON_ID_DEFAULT;
	public Long getItemLessonId() {
		return itemLessonId;
	}
	public void setItemLessonId(Long itemLessonId) {
		this.itemLessonId = itemLessonId;
	}
	
	private Boolean itemHidden = HIDDEN_DEFAULT;
	public Boolean getItemHidden() {
		return itemHidden;
	}
	public void setItemHidden(Boolean itemHidden) {
		this.itemHidden = itemHidden;
	}
	
	private Boolean itemSchedule = SCHEDULE_DEFAULT;
	public Boolean getItemSchedule() {
		return itemSchedule;
	}
	public void setItemSchedule(Boolean itemSchedule) {
		this.itemSchedule = itemSchedule;
	}
	
	private Date itemStartDate = START_DATE_DEFAULT;
	public Date getItemStartDate() {
		return itemStartDate;
	}
	public void setItemStartDate(Date itemStartDate) {
		this.itemStartDate = itemStartDate;
	}
	
	private String learningDesigns;
	public String getLearningDesigns() {
		return learningDesigns;
	}
	public void setLearningDesigns(String learningDesigns) {
		this.learningDesigns = learningDesigns;
	}
	
	private Boolean lessonCreated;
	public Boolean getLessonCreated() {
		return lessonCreated;		
	}
	public void setLessonCreated(Boolean lessonCreated) {
		this.lessonCreated = lessonCreated;		
	}

	public String getCurrentUserDisplayName() {
		return logic.getCurrentUserDisplayName();
	}
	
	public String getAuthorURL() {
		return logic.getAuthorURL();
	}
	
	public String getMonitorURL() {
		String url = logic.getMonitorURL();
		url = url + "&lsid=" + itemLessonId;
		return url;
	}
	
	public String getLearnerURL() {
		String url = logic.getLearnerURL();
		url = url + "&lsid=" + itemLessonId;
		return url;
	}


	public LamstwoBean() {
	}

	public DataModel getAllItems() {
		log.debug("wrapping items for JSF datatable...");
		List wrappedItems = new ArrayList();

		List items = logic.getAllVisibleItems();
		for (Iterator iter = items.iterator(); iter.hasNext(); ) {
			LamstwoItemWrapper wrapper = 
				new LamstwoItemWrapper((LamstwoItem) iter.next());
			// Mark the item if the current user owns it and can delete it
			if( logic.canWriteItem(wrapper.getItem()) ) {
				wrapper.setCanDelete(true);
			} else {
				wrapper.setCanDelete(false);
			}
			wrappedItems.add(wrapper);
		}
		itemsModel = new ListDataModel(wrappedItems);
		return itemsModel;
	}
	
	private static final String LAMS_DATE_FORMAT = "dd/M/yyyy h:mm a";
	private static String convertToLamsDateFormat(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(LAMS_DATE_FORMAT);
		return formatter.format(date);		
	}
	
	public String processActionAdd() {
		log.debug("in process action add...");
		FacesContext fc = FacesContext.getCurrentInstance();
		// Test for empty items and don't add them
		if (itemText != null && !itemText.equals("")) {
			String message;
			LamstwoItem item;
			if (currentItem == null) {
				item = new LamstwoItem();
				// ownerId, siteId, and dateCreated are set in the logic.saveItem
				message = "Added new item:" + itemText;
			} else {
				item = currentItem.getItem();
				message = "Updated item:" + itemText;
			}
			item.setTitle(itemText);
			item.setIntroduction(itemIntroduction);
			item.setSequenceId(itemSequenceId);
			item.setSchedule(itemSchedule);
			item.setStartDate(itemStartDate);
			
			if (itemHidden == null) { itemHidden = Boolean.FALSE; }
			item.setHidden(itemHidden);
			
			String startDate = convertToLamsDateFormat(itemStartDate);
			log.debug(startDate);
			Long lessonId = null;
			if (item.getDateCreated() == null) {
				// We are creating a new item
				if (itemSchedule) {
					lessonId = logic.scheduleLesson(itemSequenceId, itemText, itemIntroduction, startDate);
				} else {
					lessonId = logic.startLesson(itemSequenceId, itemText, itemIntroduction);
				}
				
				if (lessonId == null) {
					log.error("LAMS did not return a lessonId, Unable to save lesson");
					// TODO need to send something more meaningful to the user than a stack trace.
					throw new RuntimeException();  
				}
				
				item.setLessonId(lessonId);
			}
			
			logic.saveItem(item);

			fc.addMessage("items", new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));

			// Reset text
			itemText = "";
			itemIntroduction = "";
			itemSequenceId = 0L; // TODO is this right ???
			itemLessonId = 0L;
		} else {
			String message = "Could not add item without a title";
			fc.addMessage("items", new FacesMessage(FacesMessage.SEVERITY_WARN, message, message));
		}
		// TODO need to check fields
		
		return "addedItem";
	}

	public String processActionDelete() {
		log.debug("in process action delete...");
		FacesContext fc = FacesContext.getCurrentInstance();
		List items = (List) itemsModel.getWrappedData();
		int itemsRemoved = 0;
		for (Iterator iter = items.iterator(); iter.hasNext(); ) {
			LamstwoItemWrapper wrapper = 
				(LamstwoItemWrapper) iter.next();
			if(wrapper.isSelected()) {
				logic.deleteLesson(wrapper.getItem().getLessonId());
				logic.removeItem(wrapper.getItem());
				itemsRemoved++;
			}
		}
		String message = "Removed " + itemsRemoved + " items";
		fc.addMessage("items", new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
		return "deleteItems";
	}


	public String processActionNew() {
		log.debug("in process action new...");
		currentItem = null;
		// set the values to the new defaults
		itemText = TEXT_DEFAULT;
		itemIntroduction = INTRODUCTION_DEFAULT;
		itemSequenceId = SEQUENCE_ID_DEFAULT;
		itemStartDate = new Date(System.currentTimeMillis() + ONE_WEEK);
		itemHidden = HIDDEN_DEFAULT;
		learningDesigns = logic.getLearningDesigns(MONITORING_MODE);
		lessonCreated = Boolean.FALSE;
		return "newItem";
	}

	public String processActionUpdate() {
		log.debug("in process action update...");
		currentItem = (LamstwoItemWrapper) itemsModel.getRowData(); // gets the user selected item
		// set the values to those of the selected item
		itemText = currentItem.getItem().getTitle();
		itemIntroduction = currentItem.getItem().getIntroduction();
		itemSequenceId = currentItem.getItem().getSequenceId();  //TODO temp just for now, we shouldn't be able to change this.
		// NB: itemStartDate can not be updated once a lesson has been scheduled or started.
		itemHidden = currentItem.getItem().getHidden();
		lessonCreated = Boolean.TRUE;
		return "updateItem";
	}

	public String processActionList() {
		log.debug("in process action list...");
		return "listItems";
	}
		
	public String processActionLearner() {
		log.debug("in process action learner...");
		currentItem = (LamstwoItemWrapper) itemsModel.getRowData(); // gets the user selected item
		itemLessonId = currentItem.getItem().getLessonId();
		return "openLearner";
	}
	
	public String processActionMonitor() {
		log.debug("in process action monitor...");
		currentItem = (LamstwoItemWrapper) itemsModel.getRowData(); // gets the user selected item
		itemLessonId = currentItem.getItem().getLessonId();
		return "openMonitor";
	}
	
	public Boolean getCanCreateItem() {
		return logic.canCreateItem();
	}
}
