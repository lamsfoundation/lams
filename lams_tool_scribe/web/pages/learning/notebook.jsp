<%@ include file="/common/taglibs.jsp"%>

<h1>
	${scribeDTO.title}
</h1>

<div id="content">
	<html:form action="/learning" method="post">
		<table>
			<tr>
				<td>
					${scribeDTO.reflectInstructions}
				</td>
			</tr>

			<tr>
				<td>
					<html:textarea cols="60" rows="8" property="entryText"></html:textarea>
				</td>
			</tr>

			<tr>
				<td class="right-buttons">
					<html:hidden property="dispatch" value="submitReflection" />
					<html:hidden property="scribeUserUID" />
					<html:submit styleClass="button">
						<fmt:message>button.finish</fmt:message>
					</html:submit>
				</td>
			</tr>
		</table>
	</html:form>
</div>

