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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.testharness.admin;

import org.lamsfoundation.testharness.AbstractTest;
import org.lamsfoundation.testharness.TestUtil;
import org.lamsfoundation.testharness.Call.CallType;

/**
 * @version
 *
 * <p>
 * <a href="AdminTest.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class AdminTest extends AbstractTest {
    
	private static final String DEFAULT_COURSE_NAME = "Course";
    
	private String createCourseURL;
	private String createUserURL;
	private Boolean userCreated;
	private String courseName;
    private String courseId;

	/**
     * @param testName
     * @param callType
     * @param adminRMIRegistryName
     * @param webServiceAddress
     * @param userCreated
     * @param courseName
     * @param courseId
     */
    public AdminTest(String testName, CallType callType, String adminRMIRegistryName, String webServiceAddress, Integer minDelay, Integer maxDelay, String createCourseURL, String createUserURL, String courseId, Boolean userCreated, String courseName) {
    	
        super(testName,callType,adminRMIRegistryName, webServiceAddress, minDelay, maxDelay);
        this.createCourseURL = createCourseURL;
        this.createUserURL = createUserURL;
        this.courseName = courseName==null? TestUtil.buildName(testName,DEFAULT_COURSE_NAME):TestUtil.buildName(testName,courseName);
        this.courseId = courseId;
        this.userCreated = userCreated;
    }

	/**
	 * let MockAdmin do what he should do 
	 * login, create course and then create users
	 * 
	 * @exception TestHarnessExcepton
	 * @exception RuntimeException
	 */
    protected void startWEB(){
    	if((courseId!=null)&&userCreated)
    		return;
    	MockAdmin admin = (MockAdmin) users[0];
    	admin.login();
    	if(courseId == null){
			setCourseId(admin.createCourse(createCourseURL, courseName));
    	}
		admin.createUsers(createUserURL, courseId);
	}

	@Override
	protected void startWS(){
		//TODO implement me
	}

	@Override
	protected void startRMI(){
		//TODO implement me
	}

	public final void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	public final String getCourseName() {
		return courseName;
	}


	public final String getCreateCourseURL() {
		return createCourseURL;
	}


	public final String getCreateUserURL() {
		return createUserURL;
	}

    public final String getCourseId() {
		return courseId;
	}

	public final void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public final Boolean getUserCreated() {
		return userCreated;
	}

	public final void setUserCreated(Boolean userCreated) {
		this.userCreated = userCreated;
	}

}
