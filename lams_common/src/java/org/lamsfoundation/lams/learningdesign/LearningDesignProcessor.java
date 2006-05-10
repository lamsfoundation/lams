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

/* $Id$ */
package org.lamsfoundation.lams.learningdesign;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignProcessorException;

/** Run through a learning design, in the order of the activities. Process the learning design
 * to produce somethings else.
 * 
 * This is an abstract class that implements stepping through the learning design. It is used
 * by getAllContributeActivities to build up the list of "todo" activities and can also be used
 * by export portfolio and export learning design. The implementing classes will need to implement
 * the startBlah and endBlah methods. 
 * 
 * @author Fiona Malikoff
 *
 */
public abstract class LearningDesignProcessor {

	private static Logger log = Logger.getLogger(LearningDesignProcessor.class);
	 
	private LearningDesign design;
	private IActivityDAO activityDAO;
	
	public LearningDesignProcessor(LearningDesign design, IActivityDAO activityDAO) {
		this.design = design;
		this.activityDAO = activityDAO;
	}

	/** A complex activity has been found. Do any processing needed at the start of the activity */
	public abstract void startComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException ;
	
	/** Do any processing needed at the end of a complex activity */
	public abstract void endComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException ;

	/** A simple activity has been found. Do any processing needed at the start of the activity */
	public abstract void startSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException ;

	/** Do any processing needed at the end of a complex activity */
	public abstract void endSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException;
	
	public void parseLearningDesign() throws LearningDesignProcessorException {
		if ( getDesign() != null ) {
			handleActivity(getDesign().getFirstActivity());
		}
	}
	
	protected void handleActivity( Activity activity ) throws LearningDesignProcessorException {
		if ( activity == null ) {
			log.warn("Parsing activity method handleActivity got a null activity. Learning design was "+getDesign());
		} else {
			if ( log.isDebugEnabled() ) {
				log.debug("Processing activity "+activity.getActivityId()+" "+activity.getTitle());
			}
			if ( activity.isComplexActivity() ) {
				handleComplexActivity( activity );
			} else { 
				handleSimpleActivity( activity );
			}
		
			// if there is a transition to the next one then go to it, 
			// otherwise just return as we must be either at the end or inside another activity.
			if ( activity.getTransitionFrom() != null ) {
				handleActivity(activity.getTransitionFrom().getToActivity());
			} 
		}
	}
	
	protected void handleComplexActivity( Activity activity ) throws LearningDesignProcessorException {
		// ensure it is a real activity not a CGLIB proxy
		ComplexActivity complex = (ComplexActivity) activityDAO.getActivityByActivityId(activity.getActivityId(),SimpleActivity.class);
		startComplexActivity(complex);

		// work through all the child activities for this activity, in order id.
		Set children = new TreeSet(new ActivityOrderComparator());
        children.addAll(complex.getActivities());
        Iterator i=children.iterator();
        while ( i.hasNext() ) {
			handleActivity((Activity)i.next());
        }
        
		endComplexActivity(complex);

	}
	

	protected void handleSimpleActivity( Activity activity ) throws LearningDesignProcessorException {
		// ensure it is a real activity not a CGLIB proxy
		SimpleActivity simple = (SimpleActivity) activityDAO.getActivityByActivityId(activity.getActivityId(),SimpleActivity.class);
		startSimpleActivity(simple);
		endSimpleActivity(simple);
	}

	public LearningDesign getDesign() {
		return design;
	}
}

