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
<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>


<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
	<html:html locale="true">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title> <bean:message key="label.exportPortfolio"/> </title>
	
	 <lams:css localLink="true" />
	<!-- depending on user / site preference this will get changed probably use passed in variable from flash to select which one to use-->

 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
    <script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
    <script type="text/javascript" src="${lams}includes/javascript/fckcontroller.js"></script>
    <link href="${lams}css/fckeditor_style.css" rel="stylesheet" type="text/css">

	<script language="JavaScript" type="text/JavaScript">
    	var imgRoot="${lams}images/";
	    var themeName="aqua";
	</script>
	
	<script type="text/javascript" src="<c:out value="${lams}"/>includes/javascript/tabcontroller.js"></script>    
	<script type="text/javascript" src="<c:out value="${lams}"/>includes/javascript/common.js"></script>
	
	</head>
	<body>
	    <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<html:hidden property="method"/>
		<html:hidden property="toolContentID"/>
	
	
			<c:if test="${(userExceptionNoToolSessions == 'true')}"> 	
					<table align="center">
						<tr> 
							<td NOWRAP valign=top align=center> 
								<b> <font size=2> <bean:message key="error.noLearnerActivity"/> </font></b>
							</td> 
						<tr>
					</table>
			</c:if>			
	
	
			<c:if test="${(userExceptionNoToolSessions != 'true') }"> 	
			
				<table align="center">
					<c:if test="${(portfolioExportMode == 'learner')}">
						<tr> 
							<td NOWRAP valign=top align=center> 
								<h1> <bean:message key="label.export.learner"/>  </h1>
							</td> 
						<tr>
					</c:if>			
					<c:if test="${(portfolioExportMode != 'learner')}">
						<tr> 
							<td NOWRAP valign=top align=center> 
								<h1> <bean:message key="label.export.teacher"/>  </h1>
							</td> 
						<tr>
					</c:if>			
				</table>
			
				<jsp:include page="/export/ExportContent.jsp" />
			</c:if>						
	
		</html:form>
	
	</body>
</html:html>



