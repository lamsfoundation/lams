/*
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
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
	
	private User learner;
	private Lesson lesson;
	private LearnerProgress progress;
	
	public SessionBean(User learner,
	                   Lesson lesson,
	                   LearnerProgress progress)
	{
	    this.learner = learner;
	    this.lesson = lesson;
	    this.progress = progress;
	}
	
	
	public User getLearner() {
		return learner;
	}
	public void setLearner(User leaner) {
		this.learner = leaner;
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
