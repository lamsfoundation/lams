<%@ include file="/common/taglibs.jsp"%>

<table class="alternative-color">
	<tr> 
		<td class="field-name">
			<b> <fmt:message key="count.total.user" /> </b>
		</td>
		<td align="right">
			<c:out value="${qaStatsDTO.countAllUsers}"/>
		</td> 
	</tr>
						
	<tr> 
		<td class="field-name">
			<b>  <fmt:message key="count.finished.session" /> </b>
		</td>
		<td align="right">
			<c:out value="${qaStatsDTO.countSessionComplete}"/>
		</td> 
	</tr>
</table>
