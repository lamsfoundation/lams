<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>
	
	<script type="text/javascript">
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
	    $(document).ready(function(){
	    	window.onload = function() {
	    		document.getElementById("focused").focus();
	    	}
	    });
	</script>
</lams:head>
<body class="stripes">
	<lams:Page type="learner" title="${sessionMap.title}">
	
		<html:form action="/learning/submitReflection" method="post" onsubmit="disableFinishButton();" styleId="reflectionForm">
			<html:hidden property="userID" />
			<html:hidden property="sessionMapID" />

			<%@ include file="/common/messages.jsp"%>

			<div class="panel">
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
			</div>

			<html:textarea rows="5" property="entryText" styleClass="form-control" styleId="focused" />

			<button type="submit" class="btn btn-primary voffset10 pull-right na" id="finishButton">
				<c:choose>
					<c:when test="${sessionMap.activityPosition.last}">
						<fmt:message key="label.submit" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.finished" />
					</c:otherwise>
				</c:choose>
			</button>
				
		</html:form>

	</lams:Page>
</body>
</lams:html>
