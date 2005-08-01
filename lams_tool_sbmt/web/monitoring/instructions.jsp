
<%@ page language="java"%>
<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    <html:base />
    
    <title>Monitoring Instructions</title>    
  	<link href="<html:rewrite page='/includes/css/aqua.css'/>" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/common.js'/>"></script>
  </head>
  	<h1><fmt:message key="label.monitoring.heading.instructions" /></h1>
	<h2><fmt:message key="label.monitoring.heading.instructions.desc" /></h2>
	<table class="forms">
		<!--hidden field contentID passed by flash-->
		<tr>
		</tr>
		<!-- Instructions Row -->
		<tr>
			<td class="formlabel"><fmt:message
				key="label.authoring.online.instruction" />:</td>
			<td class="formcontrol">
				<c:out value="${authoring.onlineInstruction}" escapeXml="false"/>
			</td>
		</tr>
		<tr>
			<td class="formlabel"><fmt:message key="label.authoring.online.filelist"/>:</td>
			<td class="formcontrol">
				<c:forEach var="file" items="${authoring.onlineFiles}">
					<li><c:out value="${file.name}"/>
					<html:link href="javascript:launchInstructionsPopup('download/?uuid=${file.uuID}&preferDownload=false')">
						<fmt:message key="label.view"/>
					</html:link>
					<html:link href="../download/?uuid=${file.uuID}&preferDownload=true">
						<fmt:message key="label.download"/>
					</html:link>
					</li>
				</c:forEach>
			</td>
		</tr>
		<tr><td colspan="2"><hr></td></tr>
		<tr>
			<td class="formlabel">
			<fmt:message
				key="label.authoring.offline.instruction" />:</td>
			<td class="formcontrol">
				<c:out value="${authoring.offlineInstruction}" escapeXml="false"/>
			</td>
		</tr>
		<tr>
			<td class="formlabel"><fmt:message key="label.authoring.offline.filelist"/>:</td>
			<td class="formcontrol">
				<c:forEach var="file" items="${authoring.offlineFiles}">
					<li><c:out value="${file.name}"/></li>
					<html:link href="javascript:launchInstructionsPopup('download/?uuid=${file.uuID}&preferDownload=false')">
						<fmt:message key="label.view"/>
					</html:link>
					<html:link href="../download/?uuid=${file.uuID}&preferDownload=true">
						<fmt:message key="label.download"/>
					</html:link>
				</c:forEach>
			</td>
		</tr>
		</tr>
	</table>
  <body>

  </body>
</html:html>
