<%@ include file="/common/taglibs.jsp"%>

<div id="content">
<h1>
	${chatDTO.title}
</h1>

	<html:form action="/learning" method="post">
		<table>
			<tr>
				<td>
					<lams:out value="${chatDTO.reflectInstructions}"/>				
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
					<html:hidden property="chatUserUID" />
					<html:submit styleClass="button">
						<fmt:message>button.finish</fmt:message>
					</html:submit>
				</td>
			</tr>
		</table>
	</html:form>
</div>

