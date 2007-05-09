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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $Id$ */

package org.lamsfoundation.lams.authoring.service;

import java.util.ArrayList;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignProcessor;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignProcessorException;

public class EditOnFlyProcessor extends LearningDesignProcessor {

    private static Logger log = Logger.getLogger(EditOnFlyProcessor.class);

    ArrayList<Activity> mainActivityList;
	ArrayList<Activity> currentActivityList;
	ArrayStack activityListStack;

	/** 
	 */
	public EditOnFlyProcessor(LearningDesign design, IActivityDAO activityDAO) {
		super(design, activityDAO);

		this.mainActivityList = new ArrayList<Activity>();
		this.activityListStack = new ArrayStack(5);
		this.currentActivityList = mainActivityList;
	}

	/** Prepares to process children */
	public void startComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException {
		// Create a new current activity list, putting the old current one on the stack. 
		activityListStack.push(currentActivityList);
		currentActivityList = new ArrayList<Activity>();
		
	}

	/** Creates an ActivityPortfolio and sets up the list of its children. Doesn't create an entry if there are no children. */
	public void endComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException {
		
		currentActivityList = (ArrayList<Activity>) activityListStack.pop();
		
		if(!activity.isActivityReadOnly())
			return;
		
		if ( activity != null ) {
			if(!currentActivityList.isEmpty()) currentActivityList.remove(currentActivityList.size()-1);
			currentActivityList.add(activity);
		}
	}

	public void startSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {
		// everything done by endSimpleActivity
	}

	/** Creates an ActivityPortfolio. */
	public void endSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {
		if(!activity.isActivityReadOnly())
			return;
		
		if(activity != null) {
			if(!currentActivityList.isEmpty()) currentActivityList.remove(currentActivityList.size()-1);
			currentActivityList.add(activity);
		}
		
	}
	
	/** Get the last activities in the sequence which are read-only, may contain more than one Activity due to branching paths */
	public ArrayList<Activity> getLastReadOnlyActivity() {
		return mainActivityList;
	}
}
