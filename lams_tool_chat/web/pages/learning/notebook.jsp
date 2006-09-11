<%@ include file="/common/taglibs.jsp"%>

<h1 class="no-tabs-below">
	${chatDTO.title}
</h1>
<div id="header-no-tabs-learner"></div>
<div id="content-learner">
	<html:form action="/learning" method="post">
		<table>
			<tr>
				<td>
					<lams:out value="${chatDTO.reflectInstructions}"/>				
				</td>
			</tr>

			<tr>
				<td>
					<html:textarea cols="66" rows="8" property="entryText"></html:textarea>
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
<div id="footer-learner"></div>
