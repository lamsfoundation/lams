/*
 * ProgressEngine.java
 *
 * Created on 07 December 2004, 10:42
 */

package org.lamsfoundation.lams.learning.progress;


import com.lamsinternational.lams.learningdesign.*;
import com.lamsinternational.lams.lesson.*;
import com.lamsinternational.lams.usermanagement.User;

/**
 * The Progress Engine controls how a learner progresses
 * through a sequence.
 *
 * @author  chris
 */
public class ProgressEngine
{
    
    /**
     * Creates a new instance of ProgressEngine.
     */
    public ProgressEngine()
    {
    }
    
    /**
     * Method determines next step for a learner based on the activity
     * they have just completed.
     * @param learner The <CODE>User</CODE> who is progressing through the <CODE>Lesson</CODE>.
     * @param completedActivity The <CODE>Activity</CODE> the learner has just completed.
     * @param lesson The <CODE>Lesson</CODE> the learner needs progress for.
     * @return Progress The VO that contains the data needed to send 
     * the learner to the next step.
     * @throws ProgressException if progress cannot be calculated successfully.
     */
    public Progress calculateProgress(User learner, Lesson lesson, Activity completedActivity) throws ProgressException
    {
        //mark activity as complete for user
        
        //if activity has transition
        
            //follow transition to get next activity
        
            //mark next activit as current act for user
        
            //fill in VO with right data for next act.
        
        //else if activity has a parent activity
        
            //get parent
        
            //notify parent activity that child is complete
        
            //if parent is now complete
        
                //recurse
        
        //else (if no parent and no transtions)
            
            //mark learner as having finished sequence
        
            //fill in VO with right data for the end of sequence
    	
    	return null;
    }
    
    /**
     * Method determines the start point for a learner when they begin a Lesson.
     * @param learner the <CODE>User</CODE> who is starting the <CODE>Lesson</CODE>.
     * @param lesson the <CODE>Lesson</CODE> the learner is starting.
     * @return Progress - the VO that contains the data needed to send
     * @throws ProgressException if the start point cannot be calculated successfully.
     */
    public Progress getStartPoint(User learner, Lesson lesson) throws ProgressException
    {
        
        //get lD from lesson
        
        //get first activity from ld
        
        //fill in VO based on first activity
    
    	return null;
    }
    
}
