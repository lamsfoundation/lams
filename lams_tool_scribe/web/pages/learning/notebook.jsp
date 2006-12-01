<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = "disabled";
	}
</script>

<div id="content">

	<h1>
		${scribeDTO.title}
	</h1>

	<html:form action="/learning" method="post" onsubmit="disableFinishButton();">
		<html:hidden property="dispatch" value="submitReflection" />
		<html:hidden property="scribeUserUID" />

		<p>
			${scribeDTO.reflectInstructions}
		</p>

		<html:textarea cols="60" rows="8" property="entryText"
			styleClass="text-area"></html:textarea>

		<div class="space-bottom-top align-right">
			<html:submit styleClass="button" styleId="finishButton">
				<fmt:message>button.finish</fmt:message>
			</html:submit>
		</div>

	</html:form>
</div>

