<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = "disabled";
	}
</script>

<div id="content">
	<h1>
		${notebookDTO.title}
	</h1>

	<p>
		${notebookDTO.instructions}
	</p>

	<p>
		<fmt:message key="message.runOfflineSet" />
	</p>

	<c:if test="${mode == 'learner' || mode == 'author'}">
		<html:form action="/learning" method="post" onsubmit="disableFinishButton();">
			<html:hidden property="dispatch" value="finishActivity" />
			<html:hidden property="toolSessionID" />

			<div align="right" class="space-bottom-top">
				<html:submit styleClass="button" styleId="finishButton">
					<fmt:message>button.finish</fmt:message>
				</html:submit>
			</div>
		</html:form>
	</c:if>
</div>

