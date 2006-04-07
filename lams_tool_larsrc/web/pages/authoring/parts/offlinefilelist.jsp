<%@ include file="/common/taglibs.jsp" %>
<table border="0">
	<c:forEach var="file" items="${offlineFileList}">
	<tr>
		<td>
			<c:out value="${file.fileName}"/>
		</td>
		<td>
			<c:set var="viewURL">
				<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=false"/>
			</c:set>
			<html:link href="javascript:launchPopup('${viewURL}','offlinefile')" styleClass="button">
				<fmt:message key="label.view"/>
			</html:link>
		</td>
		<td>
			<c:set var="downloadURL">
					<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true"/>
			</c:set>
			<html:link href="${downloadURL}"  styleClass="button">
				<fmt:message key="label.download"/>
			</html:link>
		</td>
		<td>
			<html:link href="#" onclick="deleteOfflineFile(${file.fileUuid},${file.fileVersionId})" styleClass="button">
				<fmt:message key="label.authoring.offline.delete"/>
			</html:link>
		</td>				
	</tr>
	</c:forEach>
</table>