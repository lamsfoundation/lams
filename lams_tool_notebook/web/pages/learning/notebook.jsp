<%@ include file="/common/taglibs.jsp"%>

<h1 class="no-tabs-below">
	<fmt:message key="activity.title" />
</h1>
<div id="header-no-tabs-learner"></div>
<div id="content-learner">
This is notebook content.
	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<html:form action="/learning" method="post">
			<div class="right-buttons">
				<html:hidden property="dispatch" value="finishActivity" />
				<html:hidden property="notebookUserUID" value="${USER_UID}" />
				<html:hidden property="toolSessionID" value="${SESSION_ID}" />
				<html:submit styleClass="button">
					<fmt:message>button.finish</fmt:message>
				</html:submit>
			</div>
		</html:form>
	</c:if>
	<div class="space-bottom"></div>
</div>
<div id="footer-learner"></div>