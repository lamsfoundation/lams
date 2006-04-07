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
-->
<%@ include file="/common/taglibs.jsp" %>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale = "true">
    <head>
      <html:base/>
      <meta http-equiv="pragma" content="no-cache">
      <meta http-equiv="cache-control" content="no-cache">
		<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
	    <link href="<html:rewrite page='/includes/css/aqua.css'/>" rel="stylesheet" type="text/css">
		<!-- this is the custom CSS for hte tool -->
		<link href="<html:rewrite page='/includes/css/tool_custom.css'/>" rel="stylesheet" type="text/css">
</head>
    
	<tiles:importAttribute/>
	<bean:define id="headerHeight" name="headerHeight" type="String"/>
	<bean:define id="header" name="header" type="String"/>
	<bean:define id="navigator" name="navigator" type="String"/>
	<bean:define id="content" name="content" type="String"/>
	<bean:define id="footerHeight" name="footerHeight" type="String"/>
	<bean:define id="footer" name="footer" type="String"/>

	<frameset rows="<%=headerHeight%>,*,<%=footerHeight%>" frameborder="no">
		<frame src="../<%=header%>" name=headerFrame" marginheight="0" scrolling="NO">
		<frameset cols="25%,75%" rows="*">
			<frame src="../<%=navigator%>" name="navigatorFrame" marginheight="0"  scrolling="YES">
			<frame src="../<%=content%>" name="contentFrame" marginheight="0" scrolling="YES">
		</frameset>
		<frame src="../<%=footer%>" name="footerFrame" marginheight="0" scrolling="NO">
	</frameset>
	
	<noframes><body>
		This tool requires the support of frames. Your browser does not support frames.
	</body></noframes>

</html:html>

