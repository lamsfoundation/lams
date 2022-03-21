<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" contentType="text/javascript;charset=UTF-8"%>
<!--
		// Setup screen sizes for Authoring
		var str_regex = new RegExp("\\d{3,4}(x)\\d{3,4}");
		var authoring_size = "<lams:Configuration key='<%= ConfigurationKeys.AUTHORING_SCREEN_SIZE %>'/>";
		
		if(!str_regex.test(authoring_size))
			authoring_size = "1024x768";			// default size
			
		var authoring_width = authoring_size.substring(0, authoring_size.lastIndexOf('x'));
		var authoring_height = authoring_size.substring(authoring_size.lastIndexOf('x')+1, authoring_size.length);
		
		// Setup screen sizes for Monitor
		var monitor_size = "<lams:Configuration key='<%= ConfigurationKeys.MONITOR_SCREEN_SIZE %>'/>";
		if(!str_regex.test(monitor_size))
			monitor_size = "1024x768";			// default size
			
		var monitor_width = monitor_size.substring(0, monitor_size.lastIndexOf('x'));
		if(isMac) monitor_width -= 17;			// mac adjustment
		
		var monitor_height = monitor_size.substring(monitor_size.lastIndexOf('x')+1, monitor_size.length);
		
		// Setup screen sizes for Learner
		var learner_size = "<lams:Configuration key='<%= ConfigurationKeys.LEARNER_SCREEN_SIZE %>'/>";
		if(!str_regex.test(learner_size))
			learner_size = "1024x768";
		var learner_width = learner_size.substring(0, learner_size.lastIndexOf('x'));
		var learner_height = learner_size.substring(learner_size.lastIndexOf('x')+1, learner_size.length);
		
		// Setup screen sizes for Admin
		var app_admin_size = "<lams:Configuration key='<%= ConfigurationKeys.ADMIN_SCREEN_SIZE %>'/>";
		if(!str_regex.test(app_admin_size))
			app_admin_size = "1024x768";			// default size
			
		var app_admin_width = app_admin_size.substring(0, app_admin_size.lastIndexOf('x'));
		var app_admin_height = app_admin_size.substring(app_admin_size.lastIndexOf('x')+1, app_admin_size.length);
		
//-->
