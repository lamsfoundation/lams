/*
 * Created on 14/01/2005
 *
 */
package org.lamsfoundation.lams.learning.web.bean;

import org.lamsfoundation.lams.usermanagement.*;
import org.lamsfoundation.lams.lesson.*;

/**
 * @author daveg
 *
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
