<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

	<lams:html>
	<lams:head>
	<title> <fmt:message key="label.exportPortfolio"/> </title>
	<lams:css localLinkPath="../"/>
	</lams:head>

	<body class="stripes">
	
	    <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<html:hidden property="method"/>
		<html:hidden property="toolContentID"/>
	
			<div id="content">
		
			<h1>
				<c:if test="${(portfolioExportMode == 'learner')}"><fmt:message key="label.export.learner"/></c:if>			
				<c:if test="${(portfolioExportMode != 'learner')}"><fmt:message key="label.export.teacher"/></c:if>			
	        </h1>

				<c:if test="${(userExceptionNoToolSessions == 'true') && (portfolioExportMode != 'learner')}"> 
					<h2> <fmt:message key="error.noLearnerActivity"/> </h2>
				</c:if>			
		
				<c:if test="${(userExceptionNoToolSessions != 'true') }"> 	
					<c:choose> 
						<c:when test="${(portfolioExportMode != 'learner')}" > 
							<jsp:include page="/export/class.jsp" /> 
					  	</c:when> 
					  	<c:otherwise>
							<c:if test="${not empty  listMonitoredAnswersContainerDto}"> 
								<jsp:include page="/export/learner.jsp" /> 
							</c:if>
					  	</c:otherwise>
					</c:choose> 

			  	  	<jsp:include page="/export/reflections.jsp" />  
				</c:if>						
		
			</div>  <!--closes content-->		
		
			<div id="footer"></div><!--closes footer-->
		
		</html:form>	

	</body>
</lams:html>
