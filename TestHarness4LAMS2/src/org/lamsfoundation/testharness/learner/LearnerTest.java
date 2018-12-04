/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.testharness.learner;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.lamsfoundation.testharness.AbstractTest;

/**
 * <p>
 * <a href="LearnerTest.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class LearnerTest extends AbstractTest {
    private static final Logger log = Logger.getLogger(LearnerTest.class);

    protected String joinLessonURL;
    protected String lessonEntryURL;
    protected String[] filesToUpload;
    protected CountDownLatch allDoneSignal;

    /**
     * LearnerTest Constructor
     */
    public LearnerTest(String testName, Integer minDelay, Integer maxDelay, String joinLessonURL, String lessonEntryURL,
	    String[] filesToUpload) {
	super(testName, minDelay, maxDelay);
	this.joinLessonURL = joinLessonURL;
	this.lessonEntryURL = lessonEntryURL;
	this.filesToUpload = filesToUpload;
    }

    @Override
    protected void startTest() {
	log.info(users.length + (users.length == 1 ? " learner begins studying..." : " learners begin studying..."));
	allDoneSignal = new CountDownLatch(users.length);
	for (int i = 0; i < users.length; i++) {
	    MockLearner learner = (MockLearner) users[i];
	    new Thread(learner, learner.getUsername()).start();
	}
	try {
	    allDoneSignal.await();
	    log.info(composeEndInfo());
	} catch (InterruptedException e) {
	    log.debug(e.getMessage(), e);
	    // what to do?
	}
    }

    private String composeEndInfo() {
	int abortedCounter = countAborted();
	int finishedCounter = countFinished();
	if (finishedCounter == 0) {
	    return composeSubject(users.length) + " aborted on the lesson";
	}
	if (abortedCounter == 0) {
	    return composeSubject(users.length) + " finished the lesson";
	}
	return finishedCounter
		+ (finishedCounter == 1 ? " learner finished the lesson while "
			: " learners finished the lesson while ")
		+ abortedCounter
		+ (abortedCounter == 1 ? "learner aborted on the lesson" : "learners aborted on the lesson");

    }

    private String composeSubject(int length) {
	switch (length) {
	    case 1:
		return "The only learner";
	    case 2:
		return "Both the learners";
	    default:
		return "All the " + length + " learners";
	}
    }

    public int countAborted() {
	int amount = 0;
	for (MockLearner learner : (MockLearner[]) users) {
	    if (!learner.isFinished()) {
		amount++;
	    }
	}
	return amount;
    }

    public int countFinished() {
	return users.length - countAborted();
    }

    public int countLearners() {
	return users == null ? 0 : users.length;
    }

    public final String[] getFilesToUpload() {
	return filesToUpload;
    }
}