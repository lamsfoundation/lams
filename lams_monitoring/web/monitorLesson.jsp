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
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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

<html:html locale="true" xhtml="true">

<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<lams:css/>
	<title><fmt:message key="monitor.title"/></title>
	<script src="<lams:LAMSURL/>includes/javascript/AC_RunActiveContent.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/JavaScript">
	<!--
		function openPopUp(args, title, h, w, resize, status, scrollbar, menubar, toolbar){
	// refocus code commented out as we want to replace contents due to tool's session issues. Code will be 
	// wanted again the future.
	//if(thePopUp && thePopUp.open && !thePopUp.closed){
	//		thePopUp.focus();
			
	//}else{
		thePopUp = window.open(args,title,"HEIGHT="+h+",WIDTH="+w+",resizable="+resize+",scrollbars=yes,status="+status+",menubar="+menubar+", toolbar="+toolbar);
	//}
		}
		
	//-->
	</script>
</head>

<BODY bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<% 
String clientVersion = Configuration.get(ConfigurationKeys.MONITOR_CLIENT_VERSION);
String serverLanguage = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
String languageDate = Configuration.getDictionaryDateForLanguage(serverLanguage);
%>

	<%-- lessonID is passed in as request parameters by addLesson.jsp in lams_central. --%>
	<c:set var="monitorurl_params">?loadFile=monitoring/lams_monitoring.swf&loadLibrary=monitoring/lams_monitoring_library.swf&userID=<lams:user property="userID"/>&serverURL=<lams:LAMSURL/>&build=<%=clientVersion%>&lang=<lams:user property="localeLanguage"/>&country=<lams:user property="localeCountry"/>&theme=<lams:user property="flashTheme"/>&lessonID=<c:out value="${param.lessonID}"/></c:set>
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
</html:html>