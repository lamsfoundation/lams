<%@ taglib uri="tags-core" prefix="c" %>

<hr>
<BR>
<div id="datatablecontainer">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="center">
			<c:out value="${NbExportForm.title}" escapeXml="false"/>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td align="left">
				<c:out value="${NbExportForm.content}" escapeXml="false"/>
			</td>
		</tr>
	</table>
</div>
<BR>
<hr>