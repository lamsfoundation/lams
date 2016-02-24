<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
	function submitForm(methodName) {
		var f = document.getElementById('messageForm');
		f.submit();
	}
</script>

<lams:Page type="learner" title="${chatDTO.title}">

	<html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="messageForm">

		<div class="panel">
			<lams:out value="${chatDTO.reflectInstructions}" escapeHtml="true" />
		</div>

		<html:textarea rows="5" property="entryText" styleClass="form-control" styleId="focused"></html:textarea>

		<html:hidden property="dispatch" value="submitReflection" />
		<html:hidden property="chatUserUID" />
		<html:link href="#nogo" styleClass="btn btn-primary pull-right voffset10 na" styleId="finishButton"
			onclick="submitForm('finish')">
			<c:choose>
				<c:when test="${activityPosition.last}">
					<fmt:message key="button.submit" />
				</c:when>
				<c:otherwise>
					<fmt:message key="button.finish" />
				</c:otherwise>
			</c:choose>
		</html:link>
	</html:form>
</lams:Page>


<script type="text/javascript">
	window.onload = function() {
		document.getElementById("focused").focus();
	}
</script>


