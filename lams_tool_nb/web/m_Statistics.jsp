<div id="datatablecontainer">
<h2><fmt:message key="titleHeading.summary" /> </h2>
<table width="60%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<c:set var="groupCounter" scope="request" value="0"/>
	<c:forEach var="group" items="${requestScope.groupStatsMap}">
		<c:set var="groupCounter" scope="request" value="${groupCounter+1}"/>
	<tr>
		<td>
			<fmt:message key="heading.group">
				<fmt:param value="${groupCounter}" />
			</fmt:message>
			&nbsp;&nbsp;
			<fmt:message key="heading.totalLearners"/>
			<c:out value="${group.value}"/>
			
		</td>
	<!-- 	<td align="left"></td> -->
	</tr>
	</c:forEach>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="heading.totalLearnersInGroup" />&nbsp;&nbsp;<c:out value="${requestScope.totalLearners}"/>
		</td>
	</tr>
</table>
</div>
<hr>