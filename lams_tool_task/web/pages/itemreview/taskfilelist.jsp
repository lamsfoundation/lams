<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.contentrepository.client.IToolContentHandler"%>

<c:set var="targetFileType"	value="<%=IToolContentHandler.TYPE_ONLINE%>" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="fileList" value="${sessionMap.instructionAttachmentList}" />

<%-- check whehter has target file type --%>
<c:forEach var="file" items="${fileList}">
	<c:if test="${targetFileType == file.fileType}">
		<c:set var="hasFile" value="${true}" />
	</c:if>
</c:forEach>

<%-- Display target file type --%>
<c:if test="${hasFile}">
	<ul>
		<c:forEach var="file" items="${fileList}">
			<c:if test="${targetFileType == file.fileType}">
				<li>
					<c:out value="${file.fileName}" />
					<c:set var="viewURL">
						<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=false" />
					</c:set>
					<html:link href="javascript:launchPopup('${viewURL}','instructionfile')">
						<fmt:message key="label.view"/>
					</html:link>
					
					<c:set var="downloadURL">
						<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
					</c:set>
					<html:link href="${downloadURL}">
						<fmt:message key="label.download" />
					</html:link>
					<html:link href="#"	onclick="deleteOnlineFile(${file.fileUuid},${file.fileVersionId})">
						<fmt:message key="label.authoring.online.delete" />
					</html:link>
				</li>
			</c:if>
		</c:forEach>
	</ul>
</c:if>
