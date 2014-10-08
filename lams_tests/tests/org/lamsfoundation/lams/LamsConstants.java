package org.lamsfoundation.lams;

public class LamsConstants {
    // system-wide constants
	
	public static final String TEST_SERVER = "http://192.168.1.10";
	public static final String TEST_SERVER_PORT = "8080";
	
	
	// Default users
	// Sysadmin
	public static final String SYSADMIN_USER = "sysadmin";
	public static final String SYSADMIN_PASSWD = "sysadmin";
	
	// Author only
	public static final String AUTHOR_USER = "test1";
	public static final String AUTHOR_PASSWD = "test1";

	// Monitor only
	public static final String MONITOR_USER = "test2";
	public static final String MONITOR_PASSWD = "test2";
	
	// Learner only
	public static final String LEARNER_USER = "test3";
	public static final String LEARNER_PASSWD = "test3";
	
	
	// Resources path - put all your referenced files in here
	
	public static final String RESOURCES_PATH = "/tmp/lamsqa/resources/";
	
	
	
	// You shouldn't need to change anything below
	// URLs
	
	public static final String TEST_SERVER_URL = TEST_SERVER +":"+ TEST_SERVER_PORT + "/lams/";

	// Admin URLS
	public static final String ADMIN_MENU_URL = TEST_SERVER_URL + "admin/sysadminstart.do";
	public static final String ADMIN_MANAGE_COURSES_URL = TEST_SERVER_URL + "admin/orgmanage.do?org=1";
	public static final String ADMIN_CREATE_USER_URL = TEST_SERVER_URL + "admin/user.do?method=edit";
	public static final String ADMIN_SEARCH_USER_URL = TEST_SERVER_URL + "admin/usersearch.do";
	public static final String ADMIN_TOOL_MANAGEMENT_MENU_URL = TEST_SERVER_URL + "admin/toolcontentlist.do";
	
	// Public signup URL
	public static final String SIGNUP_URL = TEST_SERVER_URL + "signup/";
	
	// Integration URLS:
	public static final String INTEGRATION_LEARNING_DESIGN_REPO_URL = TEST_SERVER_URL + "services/xml/LearningDesignRepository";
	public static final String INTEGRATION_LESSON_MANAGER_URL = TEST_SERVER_URL + "services/xml/LessonManager";
	public static final String INTEGRATION_LOGINREQUEST_URL = TEST_SERVER_URL + "LoginRequest";
	public static final String INTEGRATION_LEARNING_DESIGN_SVG = TEST_SERVER_URL + "services/LearningDesignSVG";
		
	// Authoring URLs
	public static final String AUTHOR_FLASHLESS_URL = TEST_SERVER_URL + "authoring/author.do?method=openAuthoring";
	
	// Logout
	public static final String LOGOUT_URL = TEST_SERVER_URL + "home.do?method=logout";
	
	
}

