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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.monitoring.service;

import java.util.Vector;

import org.apache.commons.collections.ArrayStack;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignProcessor;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignProcessorException;
import org.lamsfoundation.lams.monitoring.dto.ContributeActivityDTO;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;

/**
 * Generate a hierachy of contribute activities, that can then be serialised and sent to Monitoring. This is used to
 * build the todo list.
 *
 * Works by creating a series of activity list. We start out with the
 * activity list which will contain all the top level activities. This starts off as the "current activity list".
 *
 * When a complex activity is encountered, put the "current activity list" on the "activity lists stack" and create a
 * new "current activity list". When a complex activity is ended, assign the "current activity list" to complex
 * activity's DTO (as this is its children) and set the "current activity list" to the top list on the stack (which
 * will be either the main list or the list for the parent of the complex activity).
 *
 * When a simple activity is encountered, it is converted to a DTO and put in the "current activity list". So the
 * activity list doesn't have to care where it is in the heirarchy - the "current activity list" will either be for its
 * parent activity or the main list for the overall sequence.
 *
 * Once the design has been parsed, the overall tree of activities can be accessed via getMainActivityList(), which
 * returns the list created right at the start of the process.
 *
 * Note: The collections used by this class are not synchronised.
 *
 * @author Fiona Malikoff, Marcin Cieslak
 */
public class ContributeActivitiesProcessor extends LearningDesignProcessor {

    Vector<ContributeActivityDTO> mainActivityList;
    ArrayStack activityListStack;
    Vector<ContributeActivityDTO> currentActivityList;
    ILamsCoreToolService toolService;
    Long lessonID;

    public ContributeActivitiesProcessor(LearningDesign design, Long lessonID, IActivityDAO activityDAO,
	    ILamsCoreToolService toolService) {
	super(design, activityDAO);
	this.lessonID = lessonID;
	this.toolService = toolService;
	this.mainActivityList = new Vector<ContributeActivityDTO>();
	this.activityListStack = new ArrayStack(5);
	this.currentActivityList = mainActivityList;
    }

    public Vector<ContributeActivityDTO> getMainActivityList() {
	return mainActivityList;
    }

    @Override
    public boolean startComplexActivity(ComplexActivity activity) {

	// Create a new current activity list, putting the old current one on the stack.
	activityListStack.push(currentActivityList);
	currentActivityList = new Vector<ContributeActivityDTO>();
	return true;
    }

    @Override
    public void endComplexActivity(ComplexActivity activity) {

	ContributeActivityDTO dto = null;
	// always attempt to get a dto, as some branching activities require contribution.
	dto = ContributeDTOFactory.getContributeActivityDTO(lessonID, activity, toolService, currentActivityList);
	Vector<ContributeActivityDTO> topActivityList = (Vector<ContributeActivityDTO>) activityListStack.pop();
	topActivityList.addAll(currentActivityList);
	currentActivityList = topActivityList;
	if (dto != null) {
	    currentActivityList.add(dto);
	}
    }

    @Override
    public void startSimpleActivity(SimpleActivity activity) {
	// nothing to do - everything done by the end
    }

    @Override
    public void endSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {
	try {
	    ContributeActivityDTO dto = ContributeDTOFactory.getContributeActivityDTO(lessonID, activity, toolService);
	    if (dto != null) {
		currentActivityList.add(dto);
	    }
	} catch (LamsToolServiceException e) {
	    throw new LearningDesignProcessorException(e);
	}
    }
}