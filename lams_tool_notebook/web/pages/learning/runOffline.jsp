<%@ include file="/common/taglibs.jsp"%>

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
		<html:form action="/learning" method="post">
			<html:hidden property="dispatch" value="finishActivity" />
			<html:hidden property="toolSessionID" />

			<div align="right" class="space-bottom-top">
				<html:submit styleClass="button">
					<fmt:message>button.finish</fmt:message>
				</html:submit>
			</div>
		</html:form>
	</c:if>
</div>

