<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<script type="text/javascript">
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
		function submitForm(methodName){
			var f = document.getElementById('messageForm');
			f.submit();
		}
	</script>
</lams:head>
<body class="stripes">
	<lams:Page type="learner" title="${sessionMap.title}">
		
		<html:form action="/learning/submitReflection" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
			<html:hidden property="userID" />
			<html:hidden property="sessionMapID" />

			<%@ include file="/common/messages.jsp"%>

			<p>
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
			</p>

			<html:textarea styleId="focused" rows="5" property="entryText" styleClass="form-control" />

			<html:link href="#nogo" styleClass="btn btn-primary voffset5 pull-right" styleId="finishButton" onclick="submitForm('finish')">
				<div class="na"> 
					<c:choose>
						<c:when test="${sessionMap.activityPosition.last}">
							<fmt:message key="label.submit" />
						</c:when>
						<c:otherwise>
			 				<fmt:message key="label.finished" />
						</c:otherwise>
					</c:choose>
				</div>
			</html:link>
				
		</html:form>
	
		<div id="footer">
		</div>
		<!--closes footer-->
	
	</lams:Page>

	<script type="text/javascript">
		window.onload = function() {
			document.getElementById("focused").focus();
		}
	</script>
</body>
</lams:html>
