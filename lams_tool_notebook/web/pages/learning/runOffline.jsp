<%@ include file="/common/taglibs.jsp"%>

<div id="content">
<h1>
	${notebookDTO.title}
</h1>

	<c:if test="${mode == 'learner' || mode == 'author'}">
		<html:form action="/learning" method="post">

			<table>
				<tr>
					<td>
						${notebookDTO.instructions}
					</td>
				</tr>

				<tr>
					<td>
						<fmt:message key="message.runOfflineSet" />
					</td>
				</tr>

				<tr>
					<td class="right-buttons">
						<html:hidden property="dispatch" value="finishActivity" />
						<html:hidden property="toolSessionID" />
						<html:submit styleClass="button">
							<fmt:message>button.finish</fmt:message>
						</html:submit>
					</td>
				</tr>
			</table>
		</html:form>
	</c:if>
</div>

