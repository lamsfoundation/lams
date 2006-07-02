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
	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

	<html:html locale="true">
	<head>
	<title> <bean:message key="label.exportPortfolio"/> </title>
	<lams:csss localLink="true"/>
	</head>
	<body>
	
	    <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<html:hidden property="method"/>
		<html:hidden property="toolContentID"/>
	
	
			<c:if test="${(userExceptionNoToolSessions == 'true')}"> 	
					<table align="center">
						<tr> 
							<td NOWRAP valign=top align=center> 
								<b>  <bean:message key="error.noLearnerActivity"/> </b>
							</td> 
						<tr>
					</table>
			</c:if>			
	
	
			<c:if test="${(userExceptionNoToolSessions != 'true') }"> 	
			
				<table align="center">
					<c:if test="${(portfolioExportMode == 'learner')}">
						<tr> 
							<td NOWRAP valign=top align=center> 
								<h1> <bean:message key="label.export.learner"/>   </h1>
							</td> 
						<tr>
					</c:if>			
					<c:if test="${(portfolioExportMode != 'learner')}">
						<tr> 
							<td NOWRAP valign=top align=center> 
								<h1> <bean:message key="label.export.teacher"/> </h1>
							</td> 
						<tr>
					</c:if>			
				</table>
			
				<jsp:include page="/export/ExportContent.jsp" />
			</c:if>						
	
		</html:form>


	</body>
</html:html>



