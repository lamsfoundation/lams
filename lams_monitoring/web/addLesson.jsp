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

<html:html locale="true" xhtml="true">

<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<lams:css style="core"/>
	<title><fmt:message key="monitor.title"/></title>
	<script src="<lams:LAMSURL/>includes/javascript/AC_RunActiveContent.js" type="text/javascript"></script>
	<script type="text/javascript">
	<!--
		function closeWizard() {
			window.opener.location.reload();
			window.close();
		}
	//-->
	</script>
	</head>

<body class="stripes" bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<% 
String clientVersion = Configuration.get(ConfigurationKeys.MONITOR_CLIENT_VERSION);
String serverLanguage = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
String languageDate = Configuration.get(ConfigurationKeys.DICTIONARY_DATE_CREATED);
%>

	<%-- courseID and classID are passed in as request parameters by addLesson.jsp in lams_central. --%>
	<c:set var="wizardurl_params">?loadFile=monitoring/lams_wizard.swf&loadLibrary=monitoring/lams_wizard_library.swf&userID=<lams:user property="userID"/>&serverURL=<lams:LAMSURL/>&build=<%=clientVersion%>&lang=<lams:user property="localeLanguage"/>&country=<lams:user property="localeCountry"/>&langDate=<%=languageDate%>&theme=<lams:user property="flashTheme"/>&courseID=<c:out value="${param.courseID}"/>&classID=<c:out value="${param.classID}"/></c:set>
	<c:set var="wizardurl_nojs">../lams_preloader_wizard.swf<c:out value="${wizardurl_params}"/></c:set>
	<c:set var="wizardurl_js">../lams_preloader_wizard<c:out value="${wizardurl_params}"/></c:set>

	<script type="text/javascript">
		AC_FL_RunContent('classid', 'clsid:D27CDB6E-AE6D-11cf-96B8-444553540000', 'codebase','http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,47,0','width','100%','height','100%','align','left','src','<c:out value="${wizardurl_js}" escapeXml="false"/>','quality','high','scale','noscale','bgcolor','#FFFFFF','name','wizard', 'id', 'wizard', 'allowscriptaccess','sameDomain', 'swliveconnect', true, 'type', 'application/x-shockwave-flash', 'pluginspage','http://www.macromedia.com/go/getflashplayer','movie', '<c:out value="${wizardurl_js}" escapeXml="false"/>' );
	</script>

	<noscript>
		<!-- URL's used in the movie-->
		<!-- text used in the movie-->
		<!--Library-->
		<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
		 codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,47,0" name="wizard"
		 width="100%" height="100%" align="left" id="wizard">
		  <param name="allowScriptAccess" value="sameDomain" />
		
		  <param name="movie" value="<c:out value="${wizardurl_nojs}" escapeXml="false"/>"/>
		  <param name="quality" value="high">
		  <param name="scale" value="noscale">
		  <param name="bgcolor" value="#FFFFFF">
		  <embed 	
		   	  src="<c:out value="${wizardurl_nojs}" escapeXml="false"/>"
			  quality="high" 
			  scale="noscale" 
			  bgcolor="#FFFFF"  
			  width="100%" 
			  height="100%" 
			  swliveconnect=true 
			  id="wizard" 
			  name="wizard" 
			  align=""
			  type="application/x-shockwave-flash" 
			  pluginspage="http://www.macromedia.com/go/getflashplayer" />
		</object>
	</noscript>
	
</BODY>
</html:html>