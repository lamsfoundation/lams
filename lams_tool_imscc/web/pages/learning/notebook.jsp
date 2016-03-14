<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>       

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<script type="text/javascript">
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
		
		function submitForm(methodName) {
			var f = document.getElementById('reflectionForm');
			f.submit();
		}

		$(document).ready(function() {
			document.getElementById("focused").focus();
		});
	</script>
</lams:head>
<body class="stripes">

	<c:set var="sessionMapID" value="${param.sessionMapID}" />
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	
	<html:form action="/learning/submitReflection" method="post" onsubmit="disableFinishButton();" styleId="reflectionForm">
		<html:hidden property="userID" />
		<html:hidden property="sessionMapID" />

		<lams:Page type="learner" title="${sessionMap.title}">

			<%@ include file="/common/messages.jsp"%>

			<div class="panel">
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
			</div>

			<html:textarea styleId="focused" rows="5" property="entryText" styleClass="form-control" />

			<div class="voffset10 pull-right">
				<html:link href="#nogo" styleClass="btn btn-primary " styleId="finishButton" onclick="submitForm('finish')">
					<span class="na">
						<c:choose>
		 					<c:when test="${sessionMap.activityPosition.last}">
		 						<fmt:message key="label.submit" />
		 					</c:when>
		 					<c:otherwise>
		 		 				<fmt:message key="label.finished" />
		 					</c:otherwise>
		 				</c:choose>
		 			</span>
				</html:link>
			</div>
		</lams:Page>
	</html:form>

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
