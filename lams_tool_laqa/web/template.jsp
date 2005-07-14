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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale = "true">
    <head>
       <html:base target="../"/>
	  <fmt:setBundle basename = "org.lamsfoundation.lams.tool.qa.QaResources" />
      <title><tiles:getAsString name="title"/></title>
      <meta http-equiv="pragma" content="no-cache">
      <meta http-equiv="cache-control" content="no-cache">
	  <link href="../learner.css" rel="stylesheet" type="text/css">
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
      <table width="900" border="0" cellspacing="0" cellpadding="0" align="center"> 
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
