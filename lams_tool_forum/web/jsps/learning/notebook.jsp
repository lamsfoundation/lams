<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
	function submitForm(methodName){
        	var f = document.getElementById('messageForm');
	        f.submit();
        }
</script>

<html:form action="/learning/submitReflection" method="post"
	onsubmit="disableFinishButton();" styleId="messageForm">
	<html:hidden property="userID" />
	<html:hidden property="sessionMapID" />

	<lams:Page type="learner" title="${sessionMap.title}">

		<%@ include file="/common/messages.jsp"%>

		<p>
			<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
		</p>

		<html:textarea styleId="focused" rows="5" property="entryText" styleClass="form-control"></html:textarea>

		<div class="space-bottom-top align-right">
			<html:link  href="#nogo" styleClass="btn btn-primary voffset5 pull-right na" styleId="finishButton" onclick="submitForm('finish')">
				<span class="nextActivity">
					<c:choose>
	 					<c:when test="${sessionMap.activityPosition.last}">
	 						<fmt:message key="label.submit" />
	 					</c:when>
	 					<c:otherwise>
	 		 				<fmt:message key="label.finish" />
	 					</c:otherwise>
	 				</c:choose>
	 			</span>
			</html:link>
		</div>

	</lams:Page>
</html:form>

<script type="text/javascript">
	window.onload = function() {
		document.getElementById("focused").focus();
	}
</script>