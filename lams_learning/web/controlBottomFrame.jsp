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
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="<lams:LAMSURL/>/css/default.css" rel="stylesheet" type="text/css"/>
		<title<fmt:message key="learner.title"/></title>
	</head>
	<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

	<% 
	String clientVersion = Configuration.get(ConfigurationKeys.LEARNER_CLIENT_VERSION);
	String serverLanguage = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
	String languageDate = Configuration.getDictionaryDateForLanguage(serverLanguage);
	%>

	<c:set var="learnerurl">lams_learning.swf?userID=<lams:user property="userID"/>&serverURL=<lams:LAMSURL/>&build=<%=clientVersion%>&lang=<%=serverLanguage%>&date=<%=languageDate%>&theme=<lams:user property="flashTheme"/></c:set>

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
		  id="authoring" 
		  name="authoring" 
		  align=""
		  type="application/x-shockwave-flash" 
		  pluginspage="http://www.macromedia.com/go/getflashplayer" />
	</object>

	</body>
</html:html>
