<%@ include file="/common/taglibs.jsp"%>

<table class="alternative-color">
	<tr> 
		<th>
			<b> <fmt:message key="count.total.user" /> </b>
		</th>
		<td align="right">
			<c:out value="${qaStatsDTO.countAllUsers}"/>
		</td> 
	</tr>
						
	<tr> 
		<th>
			<b>  <fmt:message key="count.finished.session" /> </b>
		</th>
		<td align="right">
			<c:out value="${qaStatsDTO.countSessionComplete}"/>
		</td> 
	</tr>
</table>
