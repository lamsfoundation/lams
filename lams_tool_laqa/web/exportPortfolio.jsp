<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>

<c:set var="lams"><lams:LAMSURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  	<head>    
	    <title><bean:message key="label.exportPortfolio"/></title>
		<lams:css localLink="true" />
	</head>  
  	<body>
   		<h1><bean:message key="label.exportPortfolio.simple"/></h1>
		<h2><bean:message key="label.tool.shortname"/></h2>
		<br>
		<div id="datatablecontainer">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center">
					<c:out value="${McExportForm.title}" escapeXml="false"/>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td align="left">
						<c:out value="${McExportForm.content}" escapeXml="false"/>
					</td>
				</tr>
			</table>
		</div>

	</body>
</html:html>