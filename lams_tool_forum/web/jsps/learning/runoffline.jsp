<%@ include file="/includes/taglibs.jsp"%>

<h1 class="no-tabs-below">
	<fmt:message key="activity.title" />
</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">
	<table>
		<tr>
			<td>
				<fmt:message key="run.offline.message" />
			</td>
		</tr>
	</table>

	<div class="right-buttons">
		<c:set var="finish">
			<html:rewrite page="/learning/finish.do?toolSessionID=${param.toolSessionID}" />
		</c:set>

		<div class="buttons-right">
			<html:button property="finish" onclick="javascript:location.href='${finish}';" disabled="${finishedLock}" styleClass="button">
				<fmt:message key="label.finish" />
			</html:button>
		</div>
	</div>

	<div class="space-bottom"></div>
</div>

<div id="footer-learner"></div>
