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
package org.lamsfoundation.lams.lesson;

import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityOrderComparator;
import org.lamsfoundation.lams.lesson.dto.LearnerProgressDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * <p>Holds data that describes the Users progress through a lesson. It records
 * the exact position that a learner is in regarding a lesson.</p>
 * 
 * <p>It also helps lams to rebuild the learner page and progress bar whenever
 * an unexpected error condition is identified.</p>
 * 
 * @author Chris
 * @author Jacky Fang       
 * @version 1.1
 *
 */
public class LearnerProgress implements Serializable
{
    //---------------------------------------------------------------------
    // Class level constants
    //---------------------------------------------------------------------
    /** Indicates activity has been completed */
    public static final byte ACTIVITY_COMPLETED = 1;
    /** Indicates activity has been attempted but not completed */
    public static final byte ACTIVITY_ATTEMPTED = 2;
    /** Indicates activity has not been attempted yet */
    public static final byte ACTIVITY_NOT_ATTEMPTED = 3;
    //---------------------------------------------------------------------
    // attributes
    //---------------------------------------------------------------------
    /** Identifier field */
    private Long learnerProgressId;
    
    /** The User to whom this progress data belongs. */
    private User user;
    
    /** The Lesson this progress data is for*/
    private Lesson lesson;
    
    /** Set of attempted activities */
    private Set attemptedActivities;
    
    /**
     *  Set of completed activities that includes all completed activities
     *  before current activity.
     */
    private Set completedActivities;
    
    /**
     * The activity that user just completed. The purpose of this
     * activity is to allow lams to remove unecessary frame for 
     * next activity.
     */
    private Activity previousActivity;
    
    /** 
     * The current activity always present the activity with transition, which
     * means it won't be leaf node of a complex activity. To understand the
     * activity tree, please read relevant documentation and comment. The current
     * content could be the same as next activity if next activity is not the 
     * leaf node. The main purpose of current activity is to restore the 
     * progress states if the user exist without finishing the activity. 
     */
    private Activity currentActivity;


    /**
     * The activity that progress engine is about to progress to. It could be
     * next activity following the transition or leaf activity within a 
     * complex activity.
     */
    private Activity nextActivity;
    
    /**
     * Indicates is the User has completed this lesson.
     */
    private boolean lessonComplete;
    
    /**
     * Indicates the learner progress is in a incomplete parallel activity
     * or not.
     */
    private boolean parallelWaiting;
    
    /**
     * A list of completed activities ids before move on to next activity
     * following transition. This is created to help flash calculation
     * what has *just* been done.
     */
    private List currentCompletedActivitiesList;
    
    /**Indicate whether the learning progress is restarting or not*/
    private boolean restarting;
    
    private Date startDate;
    private Date finishDate;
    //---------------------------------------------------------------------
    // Constructors
    //---------------------------------------------------------------------
    /** default constructor */
    public LearnerProgress()
    {
    }
    /**
     * Chain constructor to create new learner progress with minimum data.
     * @param user the learner.
     * @param lesson the lesson that currently is running.
     */
    public LearnerProgress(User user,Lesson lesson)
    {
        this(null,user,lesson,new TreeSet( new ActivityOrderComparator()),new TreeSet( new ActivityOrderComparator()));
    }
    
    /** full constructor */
    public LearnerProgress(Long learnerProgressId, User user, Lesson lesson, Set attemptedActivities, Set completedActivities)
    {
        this.learnerProgressId = learnerProgressId;
        this.user = user;
        this.lesson = lesson;
        this.attemptedActivities = attemptedActivities;
        this.completedActivities = completedActivities;
    }
    //---------------------------------------------------------------------
    // Getters and Setters
    //---------------------------------------------------------------------
    /**
     *          
     *
     */
    public Long getLearnerProgressId()
    {
        return this.learnerProgressId;
    }
    
    public void setLearnerProgressId(Long learnerProgressId)
    {
        this.learnerProgressId = learnerProgressId;
    }
    
    /**
     *
     */
    public User getUser()
    {
        return this.user;
    }
    
    public void setUser(User user)
    {
        this.user = user;
    }
    
    /**
     *
     */
    public Lesson getLesson()
    {
        return this.lesson;
    }
    
    public void setLesson(Lesson lesson)
    {
        this.lesson = lesson;
    }
    
    /**
     *            
     *
     */
    public Set getAttemptedActivities()
    {
        return this.attemptedActivities;
    }
    
    public void setAttemptedActivities(java.util.Set attemptedActivities)
    {
        
        this.attemptedActivities = attemptedActivities;
    }
    
    /**
     *           
     *
     */
    public Set getCompletedActivities()
    {
        
        return this.completedActivities;
    }
    
    public void setCompletedActivities(java.util.Set completedActivities)
    {
        
        this.completedActivities = completedActivities;
    }
    
    public String toString()
    {
        return new ToStringBuilder(this)
        .append("learnerProgressId", getLearnerProgressId())
        .toString();
    }
    
    public boolean equals(Object other)
    {
        if ( (this == other ) ) return true;
        if ( !(other instanceof LearnerProgress) ) return false;
        LearnerProgress castOther = (LearnerProgress) other;
        return new EqualsBuilder()
        .append(this.getLearnerProgressId(), castOther.getLearnerProgressId())
        .isEquals();
    }
    
    public int hashCode()
    {
        return new HashCodeBuilder()
        .append(getLearnerProgressId())
        .toHashCode();
    }
    
    /**
     * Getter for property currentActivity.
     * @return Value of property currentActivity.
     */
    public Activity getCurrentActivity()
    {
        return this.currentActivity;
    }
    
    /**
     * Setter for property currentActivity.
     * @param currentActivity New value of property currentActivity.
     */
    public void setCurrentActivity(Activity currentActivity)
    {
        this.currentActivity =  currentActivity;
    }

    /**
     * Gives the progress state of the specific activity.
     * @param the activity whose progress state is required.
     * @return <code>ACTIVITY_COMPLETED</code>, <code>ACTIVITY_ATTEMPTED</code> or <code>ACTIVITY_NOT_ATTEMPTED</code>.
     */
    public byte getProgressState(Activity activity)
    {
        if (completedActivities.contains(activity))
        {
            return ACTIVITY_COMPLETED;
        }
        else if (attemptedActivities.contains(activity))
        {
            return ACTIVITY_ATTEMPTED;
        }
        else
        {
            return ACTIVITY_NOT_ATTEMPTED;
        }
    }
    
    /**
     * Sets the progress state for an activity.
     * @param activity whose progress is to be set
     * @param state one of <code>ACTIVITY_COMPLETED</code>, <code>ACTIVITY_ATTEMPTED</code> or <code>ACTIVITY_NOT_ATTEMPTED</code>.
     */
    public void setProgressState(Activity activity, byte state) {
    	// remove activity from current set
    	byte oldState = getProgressState(activity);
    	if (oldState == LearnerProgress.ACTIVITY_NOT_ATTEMPTED);
    	else if (oldState == LearnerProgress.ACTIVITY_ATTEMPTED) {
    		this.attemptedActivities.remove(activity);
    	}
    	else if (oldState == LearnerProgress.ACTIVITY_COMPLETED) {
    		this.completedActivities.remove(activity);
    	}
    	// add activity to new set
    	if (state == LearnerProgress.ACTIVITY_NOT_ATTEMPTED);
    	else if (state == LearnerProgress.ACTIVITY_ATTEMPTED) {
    		this.attemptedActivities.add(activity);
    	}
    	else if (state == LearnerProgress.ACTIVITY_COMPLETED) {
    		this.completedActivities.add(activity);
    	}
    }

    /**
     * Getter for property lessonComplete.
     * @return Value of property lessonComplete.
     */
    public boolean isLessonComplete()
    {

        return this.lessonComplete;
    }

    /**
     * Setter for property lessonComplete.
     * @param lessonComplete New value of property lessonComplete.
     */
    public void setLessonComplete(boolean lessonComplete)
    {

        this.lessonComplete = lessonComplete;
    }

    /**
     * Getter for property nextActivity.
     * @return Value of property nextActivity.
     */
    public Activity getNextActivity()
    {

        return this.nextActivity;
    }

    /**
     * Setter for property nextActivity.
     * @param nextActivity New value of property nextActivity.
     */
    public void setNextActivity(Activity nextActivity)
    {

        this.nextActivity = nextActivity;
    }

    
    
    /**
     * @return Returns the previousActivity.
     */
    public Activity getPreviousActivity()
    {
        return previousActivity;
    }
    /**
     * @param previousActivity The previousActivity to set.
     */
    public void setPreviousActivity(Activity previousActivity)
    {
        this.previousActivity = previousActivity;
    }
    /**
     * @return Returns the isParallelWaiting.
     */
    public boolean isParallelWaiting()
    {
        return parallelWaiting;
    }
    /**
     * @param isParallelWaiting The isParallelWaiting to set.
     */
    public void setParallelWaiting(boolean parallelWaiting)
    {
        this.parallelWaiting = parallelWaiting;
    }
    /**
     * @return Returns the currentCompletedActivitiesList.
     */
    public List getCurrentCompletedActivitiesList()
    {
        return currentCompletedActivitiesList;
    }
    /**
     * @param completedActivitiesList The currentCompletedActivitiesList to set.
     */
    public void setCurrentCompletedActivitiesList(List completedActivitiesList)
    {
        this.currentCompletedActivitiesList = new LinkedList();
        this.currentCompletedActivitiesList.addAll(completedActivitiesList);
    }
    /**
     * @return Returns the isRestarting.
     */
    public boolean isRestarting()
    {
        return restarting;
    }
    /**
     * @param isRestarting The isRestarting to set.
     */
    public void setRestarting(boolean restarting)
    {
        this.restarting = restarting;
    }
    //---------------------------------------------------------------------
    // Service methods
    //---------------------------------------------------------------------
    /**
     * Returns the learner progress data transfer object.
     */
    public LearnerProgressDTO getLearnerProgressData()
    {
        
        return new LearnerProgressDTO(this.lesson.getLessonId(),
                                      this.lesson.getLessonName(),
                                      this.user.getLogin(),
                                      this.user.getUserId(),
                                      this.currentActivity.getActivityId(),
                                      this.createIdArrayFrom(this.attemptedActivities),
                                      this.createIdArrayFrom(this.completedActivities));
    }
    
    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------
    /**
     * Extract the Id from activities and set them into an array.
     * @param activities the activities that is being used to create the 
     * 					 array.
     */
    private Long[] createIdArrayFrom(Set activities)
    {
        if(activities == null)
            throw new IllegalArgumentException("Fail to create id array" +
            		" from null activity set");
        
        ArrayList activitiesIds = new ArrayList();
        for(Iterator i= activities.iterator();i.hasNext();)
        {
            Activity activity = (Activity)i.next();
            activitiesIds.add(activity.getActivityId());
        }
        
        return (Long [])activitiesIds.toArray(new Long[activitiesIds.size()]);
    }
    
	public Date getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
