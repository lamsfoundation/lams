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
package org.lamsfoundation.testharness;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import org.lamsfoundation.testharness.admin.AdminTest;
import org.lamsfoundation.testharness.admin.MockAdmin;
import org.lamsfoundation.testharness.author.AuthorTest;
import org.lamsfoundation.testharness.author.MockAuthor;
import org.lamsfoundation.testharness.learner.LearnerTest;
import org.lamsfoundation.testharness.learner.MockLearner;
import org.lamsfoundation.testharness.monitor.MockMonitor;
import org.lamsfoundation.testharness.monitor.MonitorTest;

import org.lamsfoundation.testharness.Call.CallType;
import static org.lamsfoundation.testharness.Call.CallType.*;

/**
 * @version
 *
 * <p>
 * <a href="PropertyFileTestManager.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class PropertyFileTestManager extends AbstractTestManager {

    private static final Logger log = Logger.getLogger(PropertyFileTestManager.class);
    
    // property keys of the master property file 
    private static final String REPORT_FILE_NAME = "ReportFileName";
    private static final String REPORT_FILE_TEMPLATE = "ReportFileTemplate";
    private static final String INDEX_PAGE_URL = "IndexPageURL";
    private static final String NUMBER_OF_TEST_SUITES = "NumberOfTestSuites";
    private static final String TARGET_SERVER = "TargetServer";
    private static final String CONTEXT_ROOT = "ContextRoot";
    private static final String RMI_REGISTRY_SERVICE_PORT = "RMIRegistryServicePort";
    private static final String HTTP_PORT = "HttpPort";
    private static final String ADMIN_PROPERTY_FILE = "AdminTestPropertyFile";
    private static final String AUTHOR_PROPERTY_FILE = "AuthorTestPropertyFile";
    private static final String MONITOR_PROPERTY_FILE = "MonitorTestPropertyFile";
    private static final String LEARNER_PROPERTY_FILE = "LearnerTestPropertyFile";
    
    //common property keys of all the single tests(admin test, author test, monitor test, learner test)
    private static final String CALL_TYPE = "CallType";
    private static final String WEB_SERVICE_ADDRESS = "WebServiceAddress";
    private static final String RMI_REGISTRY_NAME = "RMIRegistryName";
    private static final String MIN_DELAY = "MinDelay";
    private static final String MAX_DELAY = "MaxDelay";
    
    //property keys of admin test
    private static final String CREATE_COURSE_URL = "CreateCourseURL";
    private static final String CREATE_USER_URL = "CreateUserURL";
    private static final String COURSE_NAME = "CourseName";
    private static final String COURSE_ID = "CourseId";
    private static final String USER_CREATED = "UserCreated";
    private static final String SYSADMIN_USERNAME = "SysadminUsername";
    private static final String SYSADMIN_PASSWORD = "SysadminPassword";

    //property keys of author test
    private static final String LEARNING_DESIGN_UPLOAD_URL = "LearningDesignUploadURL";
    private static final String LEARNING_DESIGN_FILE = "LearningDesignFile";
    private static final String LEARNING_DESIGN_ID = "LearningDesignId";
    private static final String BASE_AUTHOR_NAME = "BaseAuthorName";

    //property keys of monitor test
    private static final String INIT_LESSON_URL = "InitLessonURL";
    private static final String CREATE_LESSON_CLASS_URL = "CreateLessonClassURL";
    private static final String START_LESSON_URL = "StartLessonURL";
    private static final String LESSON_ID = "LessonId";
    private static final String LESSON_NAME = "LessonName";
    private static final String USER_ID = "UserId";
    private static final String BASE_MONITOR_NAME = "BaseMonitorName";
    private static final String GET_LESSON_DETAILS_URL = "GetLessonDetailsURL";
    private static final String GET_CA_URL = "GetContributeActivitiesURL";
    private static final String GET_LD_DETAILS_URL =	"GetLearningDesignDetailsURL";
    private static final String GET_ALL_PROGRESS_URL = "GetAllLearnersProgressURL";
    
    //property keys of learner test
    private static final String NUMBER_LEARNERS = "NumberOfLearners";
    private static final String LEARNER_OFFSET = "LearnerOffset";
    private static final String BASE_LEARNER_NAME = "BaseLearnerName";
    private static final String GET_LESSON_URL = "GetLessonURL";
    private static final String GET_LD_URL = "GetLearningDesignURL";
    private static final String JOIN_LESSON_URL = "JoinLessonURL";
    private static final String GET_PROGRESS_URL = "GetFlashProgressDataURL";
    private static final String LESSON_ENTRY_URL = "LessonEntryURL";
    private static final String FILES_TO_UPLOAD = "FilesToUpload";
    private static final String USER_ID_OFFSET = "UserIdOffset";

    private String testPropertyFileName;

    public PropertyFileTestManager(String name) {
        this.testPropertyFileName = name;
    }
    
    /**
     *  
     * @see org.lamsfoundation.testharness.AbstractTestManager#init()
     */
    protected void init(){
        log.info("Initializing...");
        Properties testProperties = PropertyUtil.loadProperties(testPropertyFileName);
        TestReporter.setFileName(PropertyUtil.getStringProperty(testPropertyFileName,testProperties,REPORT_FILE_NAME,false));
        MockUser.setIndexPage(PropertyUtil.getStringProperty(testPropertyFileName,testProperties,INDEX_PAGE_URL,false));
        String fileTemplate = PropertyUtil.getStringProperty(testPropertyFileName,testProperties,REPORT_FILE_TEMPLATE,false);
        TestReporter.setFileTemplate(fileTemplate);
        int numberOfTestSuites = PropertyUtil.getIntegerProperty(testPropertyFileName,
                testProperties, NUMBER_OF_TEST_SUITES, false);
        for (int i = 1; i < numberOfTestSuites+1; i++) {
            addTestSuite(createTestSuite(testProperties, i));
        }
        log.info("Finished initialization");
    }
    /**
     * @return Returns the propertyFileName.
     */
    public String getTestPropertyFileName() {
        return testPropertyFileName;
    }
    
    
    private String buildPropertyKey(String basePropertyKey, int suiteIndex){
        return basePropertyKey+"."+suiteIndex;
    }
    
    private String extractTestName(String propertyFileName){
        return propertyFileName.substring(0,propertyFileName.indexOf(".properties"));
    }
    
    private TestSuite createTestSuite(Properties testProperties, int suiteIndex){
        log.info("Creating test suite " + suiteIndex+ "...");
        String targetServer = PropertyUtil.getStringProperty(testPropertyFileName,testProperties,buildPropertyKey(TARGET_SERVER,suiteIndex),true);
        String contextRoot = PropertyUtil.getStringProperty(testPropertyFileName,testProperties,buildPropertyKey(CONTEXT_ROOT,suiteIndex),true);
        Integer rmiRegistryServicePort = PropertyUtil.getIntegerProperty(testPropertyFileName,testProperties,buildPropertyKey(RMI_REGISTRY_SERVICE_PORT,suiteIndex),true);
        Integer httpPort = PropertyUtil.getIntegerProperty(testPropertyFileName,testProperties,buildPropertyKey(HTTP_PORT,suiteIndex),true);
        String adminTestPropertyFileName = PropertyUtil.getStringProperty(testPropertyFileName,testProperties,buildPropertyKey(ADMIN_PROPERTY_FILE,suiteIndex),true);
        AdminTest adminTest = adminTestPropertyFileName==null? null : createAdminTest(adminTestPropertyFileName);
        String authorTestPropertyFileName = PropertyUtil.getStringProperty(testPropertyFileName,testProperties,buildPropertyKey(AUTHOR_PROPERTY_FILE,suiteIndex),true);
        AuthorTest authorTest = authorTestPropertyFileName==null? null : createAuthorTest(authorTestPropertyFileName);
        String monitorTestPropertyFileName = PropertyUtil.getStringProperty(testPropertyFileName,testProperties,buildPropertyKey(MONITOR_PROPERTY_FILE,suiteIndex),true);
        MonitorTest monitorTest = monitorTestPropertyFileName==null? null : createMonitorTest(monitorTestPropertyFileName);
        String learnerTestPropertyFileName = PropertyUtil.getStringProperty(testPropertyFileName,testProperties,buildPropertyKey(LEARNER_PROPERTY_FILE,suiteIndex),true);
        LearnerTest learnerTest = learnerTestPropertyFileName==null? null : createLearnerTest(learnerTestPropertyFileName);
        TestSuite suite = new TestSuite(this, suiteIndex,targetServer,contextRoot,rmiRegistryServicePort,httpPort,adminTest,authorTest,monitorTest,learnerTest);
        log.info("Finished creating test suite " + suite.toString());
        return suite;
    }

    private LearnerTest createLearnerTest(String learnerTestPropertyFileName){
        String testName = extractTestName(learnerTestPropertyFileName);
        log.info("Creating learner test:"+testName+"...");
        Properties learnerTestProperties = PropertyUtil
                .loadProperties(learnerTestPropertyFileName);
        CallType callType = get(PropertyUtil.getStringProperty(learnerTestPropertyFileName,
                learnerTestProperties, CALL_TYPE, false));
        String learnerRMIRegistryName = PropertyUtil.getStringProperty(
                learnerTestPropertyFileName, learnerTestProperties, RMI_REGISTRY_NAME,
                !callType.equals(Call.CallType.RMI));
        String webServiceAddress = PropertyUtil.getStringProperty(
                learnerTestPropertyFileName, learnerTestProperties, WEB_SERVICE_ADDRESS,
                !callType.equals(Call.CallType.WS));
        Integer minDelay = PropertyUtil.getIntegerProperty(
                learnerTestPropertyFileName, learnerTestProperties, MIN_DELAY, true);
        Integer maxDelay = PropertyUtil.getIntegerProperty(
                learnerTestPropertyFileName, learnerTestProperties, MAX_DELAY,true);
        Integer numberOfLearners = PropertyUtil.getIntegerProperty(
                learnerTestPropertyFileName, learnerTestProperties, NUMBER_LEARNERS,true);
        Integer learnerOffset = PropertyUtil.getIntegerProperty(
                learnerTestPropertyFileName, learnerTestProperties, LEARNER_OFFSET,true);
        String baseLearnerName = PropertyUtil.getStringProperty(
        		learnerTestPropertyFileName, learnerTestProperties, BASE_LEARNER_NAME, true);
        String getLessonURL = PropertyUtil.getStringProperty(
        		learnerTestPropertyFileName, learnerTestProperties, GET_LESSON_URL, !callType.equals(Call.CallType.WEB));
        String getLearningDesignURL = PropertyUtil.getStringProperty(
        		learnerTestPropertyFileName, learnerTestProperties, GET_LD_URL, !callType.equals(Call.CallType.WEB));
        String joinLessonURL = PropertyUtil.getStringProperty(
        		learnerTestPropertyFileName, learnerTestProperties, JOIN_LESSON_URL, !callType.equals(Call.CallType.WEB));
        String getProgressURL = PropertyUtil.getStringProperty(
        		learnerTestPropertyFileName, learnerTestProperties, GET_PROGRESS_URL, !callType.equals(Call.CallType.WEB));
        String lessonEntryURL = PropertyUtil.getStringProperty(
        		learnerTestPropertyFileName, learnerTestProperties, LESSON_ENTRY_URL, !callType.equals(Call.CallType.WEB));
        String filesToUpload = PropertyUtil.getStringProperty(
        		learnerTestPropertyFileName, learnerTestProperties, FILES_TO_UPLOAD, !callType.equals(Call.CallType.WEB));
        Integer userIdOffset = PropertyUtil.getIntegerProperty(
                learnerTestPropertyFileName, learnerTestProperties, USER_ID_OFFSET, true);
        LearnerTest test = new LearnerTest(testName,callType,learnerRMIRegistryName,webServiceAddress,minDelay,maxDelay, getLessonURL,getLearningDesignURL,joinLessonURL,getProgressURL,lessonEntryURL, filesToUpload==null? null : filesToUpload.split(";"));
        numberOfLearners = numberOfLearners == null? 1 : numberOfLearners;
        learnerOffset = learnerOffset==null? 1 : learnerOffset;
        MockLearner[] learners = new MockLearner[numberOfLearners];
        if(userIdOffset!=null){
        	userIdOffset --;
        }
        for(int i=0; i<numberOfLearners; i++){
        	baseLearnerName = baseLearnerName==null? MockLearner.DEFAULT_NAME : baseLearnerName; 
        	String username = TestUtil.buildName(testName, baseLearnerName + (learnerOffset+i), MAX_USERNAME_LENGTH);
        	String userId = userIdOffset==null? null : (++userIdOffset).toString();
        	learners[i] = new MockLearner(test, username, username, userId);
        }
        test.setUsers(learners);
        log.info("Finished creating learner test "+testName);
        return test;
    }

 	/**
     * @param monitorTestPropertyFileName
     * @return
     */
    private MonitorTest createMonitorTest(String monitorTestPropertyFileName){
        String testName = extractTestName(monitorTestPropertyFileName);
        log.info("Creating monitor test "+testName+"...");
        Properties monitorTestProperties = PropertyUtil.loadProperties(monitorTestPropertyFileName);
        Integer lsId = PropertyUtil.getIntegerProperty(
        		monitorTestPropertyFileName, monitorTestProperties, LESSON_ID, true);
        CallType callType = get(PropertyUtil.getStringProperty(monitorTestPropertyFileName,
                monitorTestProperties, CALL_TYPE, lsId!=null));
        String monitorRMIRegistryName = PropertyUtil.getStringProperty(
                monitorTestPropertyFileName, monitorTestProperties, RMI_REGISTRY_NAME,
                lsId!=null || !callType.equals(Call.CallType.RMI));
        String webServiceAddress = PropertyUtil.getStringProperty(
                monitorTestPropertyFileName, monitorTestProperties, WEB_SERVICE_ADDRESS,
                lsId!=null || !callType.equals(Call.CallType.WS));
        Integer minDelay = PropertyUtil.getIntegerProperty(
        		monitorTestPropertyFileName, monitorTestProperties, MIN_DELAY, true);
        Integer maxDelay = PropertyUtil.getIntegerProperty(
        		monitorTestPropertyFileName, monitorTestProperties, MAX_DELAY, true);
        String initLessonURL = PropertyUtil.getStringProperty(
                monitorTestPropertyFileName, monitorTestProperties, INIT_LESSON_URL, 
                lsId!=null || !callType.equals(Call.CallType.WEB));
        String createLessonClassURL = PropertyUtil.getStringProperty(
        		monitorTestPropertyFileName, monitorTestProperties, CREATE_LESSON_CLASS_URL, 
        		lsId!=null || !callType.equals(Call.CallType.WEB));
        String startLessonURL = PropertyUtil.getStringProperty(
                monitorTestPropertyFileName, monitorTestProperties, START_LESSON_URL, 
                lsId!=null || !callType.equals(Call.CallType.WEB));
        String getLessonDetailsURL = PropertyUtil.getStringProperty(
                monitorTestPropertyFileName, monitorTestProperties, GET_LESSON_DETAILS_URL, 
                lsId!=null || !callType.equals(Call.CallType.WEB));
        String getContributeActivitiesURL = PropertyUtil.getStringProperty(
                monitorTestPropertyFileName, monitorTestProperties, GET_CA_URL, 
                lsId!=null || !callType.equals(Call.CallType.WEB));
        String getLearningDesignDetailsURL = PropertyUtil.getStringProperty(
                monitorTestPropertyFileName, monitorTestProperties, GET_LD_DETAILS_URL, 
                lsId!=null || !callType.equals(Call.CallType.WEB));
        String getAllLearnersProgressURL = PropertyUtil.getStringProperty(
                monitorTestPropertyFileName, monitorTestProperties, GET_ALL_PROGRESS_URL, 
                lsId!=null || !callType.equals(Call.CallType.WEB));
        String lsName = PropertyUtil.getStringProperty(
        		monitorTestPropertyFileName, monitorTestProperties, LESSON_NAME, true);
        String baseMonitorName = PropertyUtil.getStringProperty(
        		monitorTestPropertyFileName, monitorTestProperties, BASE_MONITOR_NAME, true);
        Integer userId = PropertyUtil.getIntegerProperty(
        		monitorTestPropertyFileName, monitorTestProperties, USER_ID, true);
        MonitorTest test = new MonitorTest(testName,callType,monitorRMIRegistryName,webServiceAddress,minDelay,maxDelay,initLessonURL,createLessonClassURL,startLessonURL, getLessonDetailsURL, getContributeActivitiesURL, getLearningDesignDetailsURL, getAllLearnersProgressURL, lsName, lsId==null? null : lsId.toString());
        baseMonitorName = baseMonitorName==null? MockMonitor.DEFAULT_NAME : baseMonitorName;
        String username = TestUtil.buildName(testName, baseMonitorName, MAX_USERNAME_LENGTH);
        test.setUsers(new MockMonitor[]{new MockMonitor(test, username, username, userId==null? null : userId.toString())});
        log.info("Finished creating monitor test "+testName);
        return test;
    }

    /**
     * @param authorTestPropertyFileName
     * @return
     */
    private AuthorTest createAuthorTest(String authorTestPropertyFileName){
        String testName = extractTestName(authorTestPropertyFileName);
        log.info("Creating author test "+testName+"...");
        Properties authorTestProperties = PropertyUtil
                .loadProperties(authorTestPropertyFileName);
        Integer ldId = PropertyUtil.getIntegerProperty(
        		authorTestPropertyFileName, authorTestProperties, LEARNING_DESIGN_ID, true);
        CallType callType = get(PropertyUtil.getStringProperty(authorTestPropertyFileName,
                authorTestProperties, CALL_TYPE, ldId!=null));
        String authorRMIRegistryName = PropertyUtil.getStringProperty(
                authorTestPropertyFileName, authorTestProperties, RMI_REGISTRY_NAME,
                ldId!=null || !callType.equals(Call.CallType.RMI));
        String webServiceAddress = PropertyUtil.getStringProperty(
                authorTestPropertyFileName, authorTestProperties, WEB_SERVICE_ADDRESS,
                ldId!=null || !callType.equals(Call.CallType.WS));
        Integer minDelay = PropertyUtil.getIntegerProperty(
        		authorTestPropertyFileName, authorTestProperties, MIN_DELAY, true);
        Integer maxDelay = PropertyUtil.getIntegerProperty(
        		authorTestPropertyFileName, authorTestProperties, MAX_DELAY, true);
        String learningDesignUploadURL = PropertyUtil.getStringProperty(
                authorTestPropertyFileName, authorTestProperties, LEARNING_DESIGN_UPLOAD_URL, 
                ldId!=null || !callType.equals(Call.CallType.WEB));
        String learningDesignFile = PropertyUtil.getStringProperty(
                authorTestPropertyFileName, authorTestProperties, LEARNING_DESIGN_FILE, 
                ldId!=null || !callType.equals(Call.CallType.WEB));
        String baseAuthorName = PropertyUtil.getStringProperty(
                authorTestPropertyFileName, authorTestProperties, BASE_AUTHOR_NAME, true);
        AuthorTest test = new AuthorTest(testName,callType,authorRMIRegistryName,webServiceAddress,minDelay,maxDelay,learningDesignUploadURL,learningDesignFile, ldId==null? null: ldId.toString());
        baseAuthorName = baseAuthorName==null? MockAuthor.DEFAULT_NAME : baseAuthorName;
        String username = TestUtil.buildName(testName, baseAuthorName, MAX_USERNAME_LENGTH);
        test.setUsers(new MockAuthor[]{new MockAuthor(test, username, username, null)});
        log.info("Finished creating author test "+testName);
        return test;
    }

    /**
     * @param adminTestPropertyFileName
     */
    private AdminTest createAdminTest(String adminTestPropertyFileName){
        String testName = extractTestName(adminTestPropertyFileName);
        log.info("Creating admin test "+testName+"...");
        Properties adminTestProperties = PropertyUtil
                .loadProperties(adminTestPropertyFileName);
        Integer courseId = PropertyUtil.getIntegerProperty(adminTestPropertyFileName,
                adminTestProperties, COURSE_ID, true);
        Boolean userCreated = PropertyUtil.getBooleanProperty(adminTestPropertyFileName,
                adminTestProperties, USER_CREATED, true);
        CallType callType = get(PropertyUtil.getStringProperty(adminTestPropertyFileName,
                adminTestProperties, CALL_TYPE, courseId!=null && userCreated));
        String adminRMIRegistryName = PropertyUtil.getStringProperty(
                adminTestPropertyFileName, adminTestProperties, RMI_REGISTRY_NAME,
                courseId!=null && userCreated || !callType.equals(Call.CallType.RMI));
        String webServiceAddress = PropertyUtil.getStringProperty(
                adminTestPropertyFileName, adminTestProperties, WEB_SERVICE_ADDRESS,
                courseId!=null && userCreated || !callType.equals(Call.CallType.WS));
        Integer minDelay = PropertyUtil.getIntegerProperty(
        		adminTestPropertyFileName, adminTestProperties, MIN_DELAY, true);
        Integer maxDelay = PropertyUtil.getIntegerProperty(
        		adminTestPropertyFileName, adminTestProperties, MAX_DELAY, true);
        String createCourseURL = PropertyUtil.getStringProperty(
        		adminTestPropertyFileName, adminTestProperties, CREATE_COURSE_URL,
        		courseId!=null || !callType.equals(Call.CallType.WEB));
        String createUserURL = PropertyUtil.getStringProperty(
        		adminTestPropertyFileName, adminTestProperties, CREATE_USER_URL,
        		userCreated || !callType.equals(Call.CallType.WEB));
        String courseName = PropertyUtil.getStringProperty(adminTestPropertyFileName,
                adminTestProperties, COURSE_NAME, true);
        String sysadminUsername = PropertyUtil.getStringProperty(adminTestPropertyFileName,
        		adminTestProperties, SYSADMIN_USERNAME, false);
        String sysadminPassword = PropertyUtil.getStringProperty(adminTestPropertyFileName,
        		adminTestProperties, SYSADMIN_PASSWORD, false);
        AdminTest test = new AdminTest(testName,callType,adminRMIRegistryName,webServiceAddress, minDelay, maxDelay, createCourseURL, createUserURL, courseId==null? null: courseId.toString(), userCreated, courseName);
        test.setUsers(new MockAdmin[]{new MockAdmin(test,sysadminUsername,sysadminPassword, null)});
        log.info("Finished creating admin test "+testName);
        return test;
    }
    
    private static class PropertyUtil {
    	
    	private static final Logger log = Logger.getLogger(PropertyUtil.class);
        
        static Properties loadProperties(String propertyFileName){
            try{
                InputStream propFile = new FileInputStream(propertyFileName);
                Properties props = new Properties();
                props.load(propFile);
                return props;
            }catch(FileNotFoundException e){
            	String errorMsg = "File:"+propertyFileName+" was not found";
            	log.error(errorMsg);
                throw new TestHarnessException(errorMsg,e);
            }catch(IOException e){
            	String errorMsg = "IO error occured during loading file "+propertyFileName;
            	log.error(errorMsg);
                throw new TestHarnessException(errorMsg, e);
            }
        }
        
		static String getStringProperty(String properyFileName, Properties properties, String key, boolean nullable){
            String value = properties.getProperty(key);
            if(value!=null) value = value.trim();
            if(!nullable){
                if((value==null)||(value.length()==0)){
                	String errorMsg = key+" is not specified in file:"+properyFileName;
                	log.error(errorMsg);
                    throw new TestHarnessException(errorMsg);
                }
            }
            return (value!=null)&&(value.length()==0)? null : value;
        }

        static Integer getIntegerProperty(String propertyFileName, Properties properties, String key, boolean nullable){
            String value = getStringProperty(propertyFileName,properties,key,nullable);
            try{
                if(value==null)
                	return null;
                else{
                	Integer number = new Integer(value);
                	if(number<=0){
                		throw new TestHarnessException(key+" is not specified as a positive number in file:"+propertyFileName);
                	}
                	return number;
                }
            }catch(NumberFormatException e){
                throw new TestHarnessException(key+" is not specified as a number in file:"+propertyFileName, e);
            }
        }
        
        static Boolean getBooleanProperty(String propertyFileName, Properties properties, String key, boolean nullable) {
        	String value = getStringProperty(propertyFileName,properties,key,nullable);
			return new Boolean(value);
		}

    }

}
