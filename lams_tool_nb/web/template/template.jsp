<!-- 
  Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

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

<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>

<%
String protocol = request.getProtocol();
if(protocol.startsWith("HTTPS")){
	protocol = "https://";
}else{
	protocol = "http://";
}
String pathToRoot = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
String pathToShare = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/../..";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale = "true">
    <head>
      <html:base/>
	  <fmt:setBundle basename = "org.lamsfoundation.lams.tool.noticeboard.web.ApplicationResources" />
      <title><tiles:getAsString name="title"/></title>
      <script src="<%=pathToShare%>/includes/javascript/common.js"></script>
      <meta http-equiv="pragma" content="no-cache">
      <meta http-equiv="cache-control" content="no-cache">
	   <link href="<%=pathToShare%>/css/aqua.css" rel="stylesheet" type="text/css">
	
	 
    </head>
    
    <body summary="This table is being used for layout purposes only">
      
        <!-- header -->
        <c:set var="pageheader" scope="session"><tiles:getAsString name="pageHeader"/></c:set>
		
	
		<h1>
		<c:out value="${sessionScope.pageheader}"/>
		</h1>
        <!-- end of header -->
        
        <!-- main content -->
		
        <tiles:insert attribute="content" />
	
        <!--end of main content-->
        
       
    </body>
</html:html>
