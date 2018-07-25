<%@ include file="/common/taglibs.jsp"%>

<table class="table table-condensed table-no-border">
	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="count.total.user" />
		</td>
		<td>
			<c:out value="${qaStatsDTO.countAllUsers}" />
		</td>
		
	</tr>
	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="count.finished.session" />
		</td>
		
		<td>
			<c:out value="${qaStatsDTO.countSessionComplete}" />
		</td>
	</tr>
</table>