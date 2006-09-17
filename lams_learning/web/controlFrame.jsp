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
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<html:html locale="true" xhtml="true">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<lams:css/>
		<title><fmt:message key="learner.title"/></title>
		<c:set var="randomID">
			<lams:generateID id="${param.lessonID}"/>
		</c:set>
		<script src="<lams:LAMSURL/>includes/javascript/AC_RunActiveContent.js" type="text/javascript"></script>
		<script language="JavaScript" type="text/JavaScript">
		<!--
			
			var thePopUp = null;
		
			function doAlert(arg){
				alert(unescape(arg));
			}
			
			function openPopUp(args, title, h, w, resize, status, scrollbar, menubar, toolbar){
	
				thePopUp = window.open(args,title,"HEIGHT="+h+",WIDTH="+w+",resizable="+resize+",scrollbars=yes,status="+status+",menubar="+menubar+", toolbar="+toolbar);
			}
		//-->
		</script>
	</head>
	<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
	
	<% 
	String clientVersion = Configuration.get(ConfigurationKeys.LEARNER_CLIENT_VERSION);
	String serverLanguage = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
	String languageDate = Configuration.get(ConfigurationKeys.DICTIONARY_DATE_CREATED);
	%>

	<c:set var="learnerurl_params">?userID=<lams:user property="userID"/>&serverURL=<lams:LAMSURL/>&build=<%=clientVersion%>&lang=<lams:user property="localeLanguage"/>&country=<lams:user property="localeCountry"/>&langDate=<%=languageDate%>&theme=<lams:user property="flashTheme"/>&lessonID=<c:out value="${param.lessonID}"/>&uniqueID=<c:out value="${randomID}"/><c:if test="${param.mode != null}">&mode=<c:out value="${param.mode}"/></c:if></c:set>
	<c:set var="learnerurl_js">lams_learner<c:out value="${learnerurl_params}"/></c:set>
	<c:set var="learnerurl_nojs">lams_learner.swf<c:out value="${learnerurl_params}"/></c:set>
	
	<script type="text/javascript">
		AC_FL_RunContent('classid', 'clsid:D27CDB6E-AE6D-11cf-96B8-444553540000', 'codebase','http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,47,0','width','100%','height','100%','align','left','src','<c:out value="${learnerurl_js}" escapeXml="false"/>','quality','high','scale','noscale','bgcolor','#B3B7C8','name','learning', 'id', 'learning', 'allowscriptaccess','sameDomain', 'swliveconnect', true, 'type', 'application/x-shockwave-flash', 'pluginspage','http://www.macromedia.com/go/getflashplayer','movie', '<c:out value="${learnerurl_js}" escapeXml="false"/>' );
	</script> 
	
	<noscript>
		<!-- URL's used in the movie-->
		<!-- text used in the movie-->
		<!--Library-->
		<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
		 codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,47,0" name="learning"
		 width="100%" height="100%" align="left" id="learning">
		  <param name="allowScriptAccess" value="sameDomain" />
	
		  <param name="movie" value="<c:out value="${learnerurl}" escapeXml="false"/>"/>
		  <param name="quality" value="high">
		  <param name="scale" value="noscale">
		  <param name="bgcolor" value="#B3B7C8">
		  <embed 	
			  src="<c:out value="${learnerurl}" escapeXml="false"/>"
			  quality="high" 
			  scale="noscale" 
			  bgcolor="#B3B7C8"  
			  width="100%" 
			  height="100%" 
			  swliveconnect=true 
			  id="learning" 
			  name="learning" 
			  align=""
			  type="application/x-shockwave-flash" 
			  pluginspage="http://www.macromedia.com/go/getflashplayer" />
		</object>
	</noscript>
	</body>
</html:html>
