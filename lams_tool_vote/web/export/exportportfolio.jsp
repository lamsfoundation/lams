<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

	<lams:html>
	<lams:head>
	<title> <fmt:message key="label.export"/> </title>
		<lams:css localLinkPath="../"/>
	</lams:head>
	<body class="stripes">
	
    <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
	<html:hidden property="method"/>
	<html:hidden property="toolContentID"/>

		<div id="content">
	
		<h1>
		<c:if test="${(exportPortfolioDto.portfolioExportMode == 'learner')}"><fmt:message key="label.export.learner"/></c:if>			
		<c:if test="${(exportPortfolioDto.portfolioExportMode != 'learner')}"><fmt:message key="label.export.teacher"/> </h1></c:if>			
        </h1>
	
		<c:if test="${(exportPortfolioDto.userExceptionNoToolSessions == 'true')}"> 	
		<c:if test="${(exportPortfolioDto.portfolioExportMode != 'learner')}"> 	
				<table align="center">
					<tr> 
						<td NOWRAP valign=top align=center> 
							<b>  <fmt:message key="error.noLearnerActivity"/> </b>
						</td> 
					<tr>
				</table>
		</c:if>			
		</c:if>			


		<c:if test="${(exportPortfolioDto.userExceptionNoToolSessions != 'true') }"> 	
			<jsp:include page="/export/ExportContent.jsp" />
		</c:if>						
		
		</div>  <!--closes content-->
	
	
		<div id="footer">
		</div><!--closes footer-->
	
		</html:form>
</body>
</lams:html>



