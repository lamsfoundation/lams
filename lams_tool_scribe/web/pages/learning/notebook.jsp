<%@ include file="/common/taglibs.jsp"%>


<div id="content">

	<h1>
		${scribeDTO.title}
	</h1>

	<html:form action="/learning" method="post">
		<html:hidden property="dispatch" value="submitReflection" />
		<html:hidden property="scribeUserUID" />

		<p>
			${scribeDTO.reflectInstructions}
		</p>

		<html:textarea cols="60" rows="8" property="entryText"
			styleClass="text-area"></html:textarea>

		<div class="space-bottom-top align-right">
			<html:submit styleClass="button">
				<fmt:message>button.finish</fmt:message>
			</html:submit>
		</div>

	</html:form>
</div>

