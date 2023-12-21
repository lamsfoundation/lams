<!DOCTYPE html>
	

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:css/>
		
		<lams:JSImport src="includes/javascript/common.js" />
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	
	</lams:head>
	<body class="stripes">
		<lams:Page type="learner" title="${scribeDTO.title}">
		
			<c:set var="appointedScribe">
				<c:out value="${scribeSessionDTO.appointedScribe}" escapeXml="true" />
			</c:set>
		
			<c:if test="${role == 'scribe'}">
				<div class="panel">
					<p>
						<fmt:message key="message.scribeInstructions">
							<fmt:param value="<strong><mark>${appointedScribe}</mark></strong>"></fmt:param>
						</fmt:message>
					</p>
		
					<p>
						<fmt:message key="message.scribeInstructions2" />
					</p>
		
					<p>
						<fmt:message key="message.scribeInstructions3" />
					</p>
		
					<p>
						<fmt:message key="message.scribeInstructions4" />
					</p>
				</div>
			</c:if>
		
			<c:if test="${role == 'learner'}">
				<div class="panel">
					<p>
						<fmt:message key="message.learnerInstructions"/>
					</p>
					<p>
						<fmt:message key="message.learnerInstructions2">
							<fmt:param value="<strong><mark>${appointedScribe}</mark></strong>"></fmt:param>
						</fmt:message>
					</p>
					<p>
						<fmt:message key="message.learnerInstructions3" />
					</p>
					<p>
						<fmt:message key="message.learnerInstructions4" />
					</p>
				</div>
		
		
		
			</c:if>
		
			<div class="activity-bottom-buttons">
				<form:form action="learning/startActivity.do" modelAttribute="learningForm">
					<form:hidden path="toolSessionID" />
					<form:hidden path="mode" value="${MODE}" />
					<button class="btn btn-primary">
						<fmt:message key="button.continue" />
					</button>
				</form:form>
			</div>
		
		</lams:Page>
	</body>
</lams:html>