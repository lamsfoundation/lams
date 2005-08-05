<div id="datatablecontainer">
<h2><fmt:message key="titleHeading.summary" /> </h2>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><fmt:message key="basic.title"/></td>
			<td>
			<c:out value="${sessionScope.title}" escapeXml="false"/>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><fmt:message key="basic.content"/></td>
		<td>
			<c:out value="${sessionScope.content}" escapeXml="false"/>
		</td>
	</tr>
</table>
</div>
<br>
<hr>