<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>

<h2><fmt:message key="titleHeading.exportPortfolio"/></h2>
<div id="datatablecontainer">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><fmt:message key="basic.title"/></td>
			<td>
				<c:out value="${NbExportForm.title}" escapeXml="false"/>
			</td>
		</tr>
		
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		
		<tr>
			<td><fmt:message key="basic.content"/></td>
			<td>
				<c:out value="${NbExportForm.content}" escapeXml="false"/>
			</td>
		</tr>			
	</table>
</div>