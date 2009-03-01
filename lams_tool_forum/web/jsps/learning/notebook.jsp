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

	<div id="content">
		<h1>
			${sessionMap.title}
		</h1>

		<%@ include file="/common/messages.jsp"%>

		<p>
			<lams:out value="${sessionMap.reflectInstructions}" />
		</p>

		<html:textarea cols="52" rows="6" property="entryText"
			styleClass="text-area" />

		<div class="space-bottom-top align-right">
			<html:link  href="javascript:;" styleClass="button" styleId="finishButton" onclick="submitForm('finish')">
				<span class="nextActivity"><fmt:message key="label.finish" /></span>
			</html:link>
		</div>

	</div>
</html:form>
