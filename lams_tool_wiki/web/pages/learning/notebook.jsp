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

<lams:Page type="learner" title="${wikiDTO.title}">
	<html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
		<html:hidden property="toolSessionID" styleId="toolSessionID" />
		<html:hidden property="mode" value="${mode}" />

		<div class="panel">
			<lams:out value="${wikiDTO.reflectInstructions}" escapeHtml="true" />
		</div>
		<div class="form-group">
			<html:textarea styleId="focused" rows="4" property="entryText" styleClass="form-control"></html:textarea>


			<html:hidden property="dispatch" value="submitReflection" />
			<html:link href="#nogo" styleClass="btn btn-primary voffset5 pull-right na" styleId="finishButton"
				onclick="submitForm('finish');return false">
				<c:choose>
					<c:when test="${activityPosition.last}">
						<fmt:message key="button.submit" />
					</c:when>
					<c:otherwise>
						<fmt:message key="button.finish" />
					</c:otherwise>
				</c:choose>
			</html:link>
		</div>
	</html:form>

</lams:Page>

	<script type="text/javascript">
		window.onload = function() {
			document.getElementById("focused").focus();
		}
	</script>


