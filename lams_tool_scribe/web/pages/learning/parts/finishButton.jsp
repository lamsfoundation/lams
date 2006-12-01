<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = "disabled";
	}
</script>

<c:if
	test="${scribeUserDTO.finishedActivity and scribeDTO.reflectOnActivity}">
	<div>
		<h4>
			${scribeDTO.reflectInstructions}
		</h4>
		<p>
			${scribeUserDTO.notebookEntry}
		</p>
	</div>
</c:if>

<div class="space-bottom">
	<html:form action="/learning" method="post" onsubmit="disableFinishButton()">
		<html:hidden property="scribeUserUID" value="${scribeUserDTO.uid}" />
		<c:choose>
			<c:when
				test="${!scribeUserDTO.finishedActivity and scribeDTO.reflectOnActivity}">
				<html:hidden property="dispatch" value="openNotebook" />

				<html:submit styleClass="button right-buttons">
					<fmt:message key="button.continue" />
				</html:submit>

			</c:when>
			<c:otherwise>
				<html:hidden property="dispatch" value="finishActivity" />
				<html:submit styleClass="button right-buttons" styleId="finishButton">
					<fmt:message key="button.finish" />
				</html:submit>
			</c:otherwise>
		</c:choose>
	</html:form>
</div>
