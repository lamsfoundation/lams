<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<table class="table table-condensed">
	<tr> 
		<th>
			<b> <fmt:message key="count.total.user" /> </b>
		</th>
		<td align="right">
			<c:out value="${mcGeneralMonitoringDTO.countAllUsers}"/>
		</td> 
	</tr>
						
	<tr> 
		<th>
			<b>  <fmt:message key="count.finished.session" /> </b>
		</th>
		<td align="right">
			<c:out value="${mcGeneralMonitoringDTO.countSessionComplete}"/>
		</td> 
	</tr>
</table>