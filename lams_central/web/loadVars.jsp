<%@ page language="java" contentType="text/javascript; charset=utf-8"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
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
		var sys_admin_size = "<lams:Configuration key='<%= ConfigurationKeys.ADMIN_SCREEN_SIZE %>'/>";
		if(!str_regex.test(sys_admin_size))
			sys_admin_size = "1024x768";			// default size
			
		var sys_admin_width = sys_admin_size.substring(0, sys_admin_size.lastIndexOf('x'));
		var sys_admin_height = sys_admin_size.substring(sys_admin_size.lastIndexOf('x')+1, sys_admin_size.length);
		
		var pedagogical_planner_width = 1280;
		var pedagogical_planner_height = authoring_height;
		
//-->
