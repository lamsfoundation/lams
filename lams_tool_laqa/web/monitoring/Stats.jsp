<%@ include file="/common/taglibs.jsp"%>

<table>
	<tr> 
		<td NOWRAP valign=top>
			<b> <fmt:message key="count.total.user" /> </b>
		</td>
		<td NOWRAP valign=top>
			<c:out value="${qaStatsDTO.countAllUsers}"/>
		</td> 
	</tr>
						
	<tr> 
		<td NOWRAP valign=top>
			<b>  <fmt:message key="count.finished.session" /> </b>
		</td>
		<td NOWRAP valign=top>
			<c:out value="${qaStatsDTO.countSessionComplete}"/>
		</td> 
	</tr>
</table>