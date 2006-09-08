<%@ include file="/common/taglibs.jsp"%>

<h1 class="no-tabs-below">
	<fmt:message key="activity.title" />
</h1>
<div id="header-no-tabs-learner"></div>
<div id="content-learner">

	<table>
		<tr>
			<td>
				<fmt:message key="message.runOfflineSet" />
			</td>
		</tr>
	</table>

	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>
	<div class="space-bottom"></div>
</div>
<div id="footer-learner"></div>
