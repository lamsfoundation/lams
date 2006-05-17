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

<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale = "true">
    <head>
       <meta http-equiv="content-type" content="text/html; charset=UTF-8">
       <html:base/>
	   <lams:css/>
	  <fmt:setBundle basename = "org.lamsfoundation.lams.monitoring.MonitoringResources" />
      <title><tiles:getAsString name="title"/></title>
      <meta http-equiv="pragma" content="no-cache">
      <meta http-equiv="cache-control" content="no-cache">
	  <script language="JavaScript" type="text/JavaScript">
	        <!--
	        function pviiClassNew(obj, new_style) { //v2.7 by PVII
	            obj.className=new_style;
	        }
	        //-->
	  </script>
	  <NOSCRIPT><!--This browser doesn't supports scripting--></NOSCRIPT>
    </head>
    
    <body bgcolor="#9DC5EC" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
      <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
        <!-- header -->
        <c:set var="pageheader" scope="session"><tiles:getAsString name="pageHeader"/></c:set>
		<tiles:insert attribute="header" />
        <!-- end of header -->
        <!-- main content -->
        <tiles:insert attribute="content" />
        <!--end of main content-->
        <!--footer-->
        <tiles:insert attribute="footer" />
        <!-- end of footer -->
      </table>
    </body>
</html:html>
