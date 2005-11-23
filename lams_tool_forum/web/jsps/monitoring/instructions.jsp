<%@ include file="/includes/taglibs.jsp" %>

	<h1><fmt:message key="label.authoring.heading.instructions" /></h1>
	<h2><fmt:message key="label.authoring.heading.instructions.desc" /></h2>
	<table class="forms">
		<!--hidden field contentID passed by flash-->
		<tr>
		</tr>
		<!-- Instructions Row -->
		<tr>
			<td class="formlabel"><fmt:message
				key="label.authoring.online.instruction" />:</td>
			<td class="formcontrol">
				<c:out value="${formBean.forum.onlineInstructions}" escapeXml="false"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td class="formcontrol">
				
				<div id="onlinefile">
				<c:forEach var="file" items="${formBean.onlineFileList}">
					<li><c:out value="${file.fileName}"/>
					<c:set var="viewURL">
						<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false"/>
					</c:set>
					<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
						<fmt:message key="label.view"/>
					</a>&nbsp;
					<c:set var="downloadURL">
							<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true"/>
					</c:set>
					<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
						<fmt:message key="label.download"/>
					</a>&nbsp;
					<c:set var="deleteonline">
					<html:rewrite page="/authoring/deleteOnline.do?toolContentID=${formBean.toolContentID}&uuID=${file.fileUuid}&versionID=${file.fileVersionId}"/>
					</c:set>
					<html:link href="javascript:loadDoc('${deleteonline}','onlinefile')">
						<fmt:message key="label.authoring.online.delete"/>
					</html:link>					
					</li>
				</c:forEach>
				</div>
			</td>
		</tr>
		<!------------Offline Instructions ----------------------->
		<tr>
			<td class="formlabel"><fmt:message
				key="label.authoring.offline.instruction" />:</td>
			<td class="formcontrol">
				<c:out value="${formBean.forum.offlineInstructions}" escapeXml="false"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td class="formcontrol">
				<div id="offlinefile">
				<c:forEach var="file" items="${formBean.offlineFileList}">
					<li><c:out value="${file.fileName}"/>
   				    <c:set var="viewURL">
						<html:rewrite page="/download/?uuid=${file.fileUuid}&preferDownload=false"/>
					</c:set>
					<html:link href="javascript:launchInstructionsPopup('${viewURL}')">
						<fmt:message key="label.view"/>
					</html:link>
					<c:set var="downloadURL">
							<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true"/>
					</c:set>
					<html:link href="${downloadURL}">
						<fmt:message key="label.download"/>
					</html:link>
					<c:set var="deleteoffline">
					<html:rewrite page="/authoring/deleteOffline.do?toolContentID=${formBean.toolContentID}&uuID=${file.fileUuid}&versionID=${file.fileVersionId}"/>
					</c:set>
					<html:link href="javascript:loadDoc('${deleteoffline}','offlinefile')">
						<fmt:message key="label.authoring.offline.delete"/>
					</html:link>					
					</li>
				</c:forEach>
				</div>
			</td>
		</tr>
	
		</tr>

	</table>
	
	