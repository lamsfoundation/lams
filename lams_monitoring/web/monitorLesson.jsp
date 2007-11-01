<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ page import="org.lamsfoundation.lams.themes.dto.CSSThemeBriefDTO" %>

<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<lams:html xhtml="true">

<lams:head>
	<lams:css style="core"/>
	<title><fmt:message key="monitor.title"/></title>
	
	<script src="<lams:LAMSURL/>includes/javascript/openUrls.js" type="text/javascript"></script>
	<script src="<lams:LAMSURL/>includes/javascript/AC_RunActiveContent.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/JavaScript">
	<!--
		
		// Use for handling popup windows
  	 	var pWins = new Array();
  	 	var iWins = 0;
	
		var isInternetExplorer = navigator.appName.indexOf("Microsoft") != -1;
		
		function monitor_DoFSCommand(command, args) {
			//alert("command:"+command+","+args);
			if (command == "openPopUp"){
				openPopUpFS(args);
			} else if(command == "closeWindow") {
				closeWindow();
			}else if(command == "openURL"){
				openURL(args);
			}else if(command == "closeWindowRefresh") {
				closeWindowRefresh();
			}else if(command == "openAuthorForEditOnFly") {
				openAuthorForEditOnFly(args);
			}else if(command == "reloadWindow") {
				reloadWindow(); 
			}
			
		}
				
		// Hook for Internet Explorer.
		if (navigator.appName && navigator.appName.indexOf("Microsoft") != -1 && navigator.userAgent.indexOf("Windows") != -1 && navigator.userAgent.indexOf("Windows 3.1") == -1) {
			document.write('<script language=\"VBScript\"\>\n');
			document.write('On Error Resume Next\n');
			document.write('Sub monitor_FSCommand(ByVal command, ByVal args)\n');
			document.write('	Call monitor_DoFSCommand(command, args)\n');
			document.write('End Sub\n');
			document.write('</script\>\n');
		}	
		
		function openPopUpFS(args){
			var params = args.split(",");		
			var urlparams = args.split("&");
			
			var activityID = urlparams[1];

			// assigned the args
			var url = params[0];
			var title = params[1] + "_" + activityID;
			var h = params[2];
			var w = params[3];
			var resize = params[4];
			var status = params[5];
			var scrollbar = params[6];
			var menubar = params[7];
			var toolbar = params[8];
			
			openPopUp(url, title, h, w, resize, status, scrollbar, menubar, toolbar);
		}
		
		function reloadWindow() {
			window.location.reload(true);
		}
	
		function openPopUp(args, title, h, w, resize, status, scrollbar, menubar, toolbar){
	// refocus code commented out as we want to replace contents due to tool's session issues. Code will be 
	// wanted again the future.
	//if(thePopUp && thePopUp.open && !thePopUp.closed){
	//		thePopUp.focus();
			
	//}else{
	//	thePopUp = window.open(args,title,"HEIGHT="+h+",WIDTH="+w+",resizable="+resize+",scrollbars=yes,status="+status+",menubar="+menubar+", toolbar="+toolbar);
	//}		
			var found = false;
			
			for(var i=0; i<pWins.length; i++) {
				if(pWins[i] != null && !pWins[i].closed && pWins[i].open) {
					if(pWins[i].name == title) {
					 	pWins[i].focus();
						found = true;	
					}
				}
			}
			
			if(!found) {
				pWins[iWins] = window.open(args,title,"HEIGHT="+h+",WIDTH="+w+",resizable="+resize+",scrollbars=yes,status="+status+",menubar="+menubar+", toolbar="+toolbar);
				iWins++;
			}
		}
		
		function closeWindow() {
			if(isInternetExplorer) {
				this.focus();
				window.opener = this;
				window.close();
			} else {
				window.location.href = "javascript: window.close()";
			}
			// close all open windows
  	 		if(window.closed) {
  	 			for(var i=0; i<pWins.length; i++) {
  	 		    	if(pWins[i] != null && !pWins[i].closed) {
  	 		        	pWins[i].close();
  	 		        }
  	 		    }
  	 		}
		}
		
		function closeWindowRefresh() {
			window.opener.location.reload();
			closeWindow();
		}
		
		function openAuthorForEditOnFly(learningDesignID) {
			try{
				window.opener.openAuthorForEditOnFly(learningDesignID);
			}
			catch(e)
			{
				openAuthorForEditOnFlyIntegrated(learningDesignID);
			}
		}
		
		function openURL(args){
			window.open(args, "_blank");
		}
		
	//-->
	</script>
</lams:head>

<body class="stripes" bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<% 
String clientVersion = Configuration.get(ConfigurationKeys.MONITOR_CLIENT_VERSION);
String serverLanguage = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
String languageDate = Configuration.get(ConfigurationKeys.DICTIONARY_DATE_CREATED);
String actColour = Configuration.get(ConfigurationKeys.AUTHORING_ACTS_COLOUR);
String allowDirectLessonLaunch = Configuration.get(ConfigurationKeys.ALLOW_DIRECT_LESSON_LAUNCH);
String allowEditOnFly = Configuration.get(ConfigurationKeys.ALLOW_EDIT_ON_FLY);
String version = Configuration.get(ConfigurationKeys.VERSION);
%>

	<%-- lessonID is passed in as request parameters by addLesson.jsp in lams_central. --%>
	<c:set var="monitorurl_params">?loadFile=monitoring/lams_monitoring.swf&loadLibrary=monitoring/lams_monitoring_library.swf&userID=<lams:user property="userID"/>&serverURL=<lams:LAMSURL/>&build=<%=clientVersion%>&version=<%=version%>&lang=<lams:user property="localeLanguage"/>&country=<lams:user property="localeCountry"/>&langDate=<%=languageDate%>&theme=<lams:user property="flashTheme"/>&lessonID=<c:out value="${param.lessonID}"/>&actColour=<%= actColour %>&lessonLaunch=<%=allowDirectLessonLaunch%>&editOnFly=<%=allowEditOnFly%></c:set>
	<c:set var="monitorurl_js">../lams_preloader<c:out value="${monitorurl_params}"/></c:set>
	<c:set var="monitorurl_nojs">../lams_preloader.swf<c:out value="${monitorurl_params}"/></c:set>

	<script type="text/javascript">
		AC_FL_RunContent('classid', 'clsid:D27CDB6E-AE6D-11cf-96B8-444553540000', 'codebase','http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,47,0','width','100%','height','100%','align','left','src','<c:out value="${monitorurl_js}" escapeXml="false"/>','quality','high','scale','noscale','bgcolor','#FFFFFF','name','monitor', 'id', 'monitor', 'allowscriptaccess','sameDomain', 'swliveconnect', true, 'type', 'application/x-shockwave-flash', 'pluginspage','http://www.macromedia.com/go/getflashplayer','movie', '<c:out value="${monitorurl_js}" escapeXml="false"/>' );
	</script>

	<noscript>
		<!-- URL's used in the movie-->
		<!-- text used in the movie-->
		<!--Library-->
		<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
		 codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,47,0" name="monitoring"
		 width="100%" height="100%" align="left" id="monitoring">
		  <param name="allowScriptAccess" value="sameDomain" />
		
		  <param name="movie" value="<c:out value="${monitorurl_nojs}" escapeXml="false"/>"/>
		  <param name="quality" value="high">
		  <param name="scale" value="noscale">
		  <param name="bgcolor" value="#FFFFFF">
		  <embed 	
		   	  src="<c:out value="${monitorurl_nojs}" escapeXml="false"/>"
			  quality="high" 
			  scale="noscale" 
			  bgcolor="#FFFFFF"  
			  width="100%" 
			  height="100%" 
			  swliveconnect=true 
			  id="monitor" 
			  name="monitor" 
			  align=""
			  type="application/x-shockwave-flash" 
			  pluginspage="http://www.macromedia.com/go/getflashplayer" />
		</object>
	</noscript>
</BODY>
</lams:html>