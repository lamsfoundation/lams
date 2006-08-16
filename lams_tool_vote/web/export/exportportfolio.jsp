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
	<title> <bean:message key="label.export"/> </title>
		<lams:css localLinkPath="../"/>
	</head>
	<body>
	
    <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
	<html:hidden property="method"/>
	<html:hidden property="toolContentID"/>

	<div id="page-learner"><!--main box 'page'-->

		<h1 class="no-tabs-below">
		<c:if test="${(exportPortfolioDto.userExceptionNoToolSessions != 'true') }"> 	
			<c:if test="${(exportPortfolioDto.portfolioExportMode == 'learner')}"><bean:message key="label.export.learner"/></c:if>			
			<c:if test="${(exportPortfolioDto.portfolioExportMode != 'learner')}"><bean:message key="label.export.teacher"/> </h1></c:if>			
		</c:if>
        </h1>
		<div id="header-no-tabs-learner">
	
		</div><!--closes header-->
	
		<div id="content-learner">
	
		<c:if test="${(exportPortfolioDto.userExceptionNoToolSessions == 'true')}"> 	
				<table align="center">
					<tr> 
						<td NOWRAP valign=top align=center> 
							<b>  <bean:message key="error.noLearnerActivity"/> </b>
						</td> 
					<tr>
				</table>
		</c:if>			


		<c:if test="${(exportPortfolioDto.userExceptionNoToolSessions != 'true') }"> 	
			<jsp:include page="/export/ExportContent.jsp" />
		</c:if>						
		
		</div>  <!--closes content-->
	
	
		<div id="footer-learner">
		</div><!--closes footer-->
	
	</div><!--closes page-->

		</html:form>
</body>
</html:html>



