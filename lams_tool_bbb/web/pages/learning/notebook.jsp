<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
</script>

<lams:Page type="learner" title="${contentDTO.title}">

	<html:form action="/learning" method="post" onsubmit="disableFinishButton();">

		<div class="panel">
			<lams:out value="${contentDTO.reflectInstructions}" escapeHtml="true" />
		</div>

		<html:textarea styleId="focused" rows="5" property="entryText" styleClass="form-control"></html:textarea>

		<html:hidden property="dispatch" value="submitReflection" />
		<html:hidden property="toolSessionID" />
		<html:submit styleClass="btn btn-primary pull-right voffset10 na" styleId="finishButton">
			<c:choose>
				<c:when test="${activityPosition.last}">
					<fmt:message key="button.submit" />
				</c:when>
				<c:otherwise>
					<fmt:message key="button.finish" />
				</c:otherwise>
			</c:choose>
		</html:submit>

	</html:form>
	
	<script type="text/javascript">
		window.onload = function() {
			document.getElementById("focused").focus();
		}
	</script>

</lams:Page>

