/*
 * Created on 14/01/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.web.bean;

import org.lamsfoundation.lams.usermanagement.*;
import org.lamsfoundation.lams.lesson.*;

/**
 * @author kevin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SessionBean implements java.io.Serializable {
	
	public static String NAME = "lams.learning.session";
	
	private User leaner;
	private Lesson lesson;
	private LearnerProgress progress;
	
	public User getLeaner() {
		return leaner;
	}
	public void setLeaner(User leaner) {
		this.leaner = leaner;
	}
	public Lesson getLesson() {
		return lesson;
	}
	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}
	public LearnerProgress getLearnerProgress() {
		return progress;
	}
	public void setLearnerProgress(LearnerProgress progress) {
		this.progress = progress;
	}
}
