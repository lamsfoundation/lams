<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="kaltura" value="${sessionMap.kaltura}" />

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
	function submitForm(methodName) {
		var f = document.getElementById('messageForm');
		f.submit();
	}
	$(document).ready(function() {
		document.getElementById("focused").focus();
	});
</script>
	
<html:form action="/learning.do?dispatch=submitReflection" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
	<html:hidden property="userID" />
	<html:hidden property="sessionMapID" />

	<lams:Page type="learner" title="${kaltura.title}">

		<%@ include file="/common/messages.jsp"%>

		<div class="panel">
			<lams:out value="${kaltura.reflectInstructions}" escapeHtml="true"/>
		</div>

		<html:textarea styleId="focused" rows="5" property="entryText" styleClass="form-control" />

		<html:link href="#nogo" styleClass="btn btn-primary voffset10 pull-right" styleId="finishButton" onclick="submitForm('finish')">
			<span class="na">
				<c:choose>
	 				<c:when test="${sessionMap.activityPosition.last}">
	 					<fmt:message key="button.submit" />
	 				</c:when>
	 				<c:otherwise>
	 	 				<fmt:message key="button.finish" />
	 				</c:otherwise>
	 			</c:choose>
	 		</span>
		</html:link>
		
	</lams:Page>
</html:form>
	
