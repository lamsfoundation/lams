<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	final String LAMS_SERVER_NAME = "LAMS 2 Demo Server";
	final String LAMS_SERVER_URL = "http://172.20.100.188:8080/lams";
	final String LAMS_SERVER_ID ="demolamsserver";
	final String LAMS_SERVER_KEY = "ixybitzy";
	final boolean ADD_TO_GROUP = true;
	final String GROUP_ID = "Playpen";
	final String GROUP_NAME = "Playpen";
	final String GROUP_COUNTRY_ISO_CODE = "AU";
	final String GROUP_LANG_ISO_CODE = "en";
	final String[] TRUSTED_OPENID_PROVIDERS = {
			"myopenid.com", 
			"pip.verisignlabs.com", 
			"myvidoop.com", 
			"yahoo.com", 
			"google.com"
	};
%>