package org.lamsfoundation.lams.lesson;

import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityOrderComparator;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Holds data that describes the Users progress through a lesson.       
 *
 */
public class LearnerProgress implements Serializable
{
    /** Indicates activity has been completed */
    public static final byte ACTIVITY_COMPLETED = 1;

    /** Indicates activity has been attempted but not completed */
    public static final byte ACTIVITY_ATTEMPTED = 2;

    /** Indicates activity has not been attempted yet */
    public static final byte ACTIVITY_NOT_ATTEMPTED = 3;
    
    /** Identifier field */
    private Long learnerProgressId;
    
    /** The User to whom this progress data belongs. */
    private User user;
    
    /** The Lesson this progress data is for*/
    private Lesson lesson;
    
    /** Set of attempted activities */
    private Set attemptedActivities;
    
    /** Set of completed activities */
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
    
    
    /** default constructor */
    public LearnerProgress()
    {
    }
    
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
}
