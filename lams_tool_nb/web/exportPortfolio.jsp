<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  	<head>    
	    <title>Noticeboard Export</title>
	  	
		<lams:css localLink="true" />
	</head>  
  	<body>
   		<h1>Export Portfolio</h1>
		<h2>Noticeboard</h2>
		<br>
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

	</body>
</html:html>