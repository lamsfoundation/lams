<%@ include file="/common/taglibs.jsp"%>

<h1>
	<fmt:message key="activity.title" />
</h1>

<div id="content">

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

