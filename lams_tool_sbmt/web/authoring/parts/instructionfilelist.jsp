<%@ include file="/common/taglibs.jsp"%>
<%@ page
	import=" org.lamsfoundation.lams.contentrepository.client.IToolContentHandler"%>
<c:set var="CONS_OFFLINE" value="<%=IToolContentHandler.TYPE_OFFLINE %>" />
<c:choose>
	<c:when test="${fileTypeFlag==CONS_OFFLINE}">
		<c:set var="targetFileType"
			value="<%=IToolContentHandler.TYPE_OFFLINE %>" />
	</c:when>
	<c:otherwise>
		<c:set var="targetFileType"
			value="<%=IToolContentHandler.TYPE_ONLINE %>" />
	</c:otherwise>
</c:choose>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="fileList" value="${sessionMap.attachmentList}" />

<%-- check whehter has target file type --%>
<c:forEach var="file" items="${fileList}">
	<c:if test="${targetFileType == file.type}">
		<c:set var="hasFile" value="${true}" />
	</c:if>
</c:forEach>

<%-- Display target file type --%>
<c:if test="${hasFile}">
	<ul>
		<c:forEach var="file" items="${fileList}">

			<c:if test="${targetFileType == file.type}">
				<li>
					<c:out value="${file.name}" />
					<c:set var="viewURL">
						<html:rewrite
							page="/download/?uuid=${file.uuID}&versionID=${file.versionID}&preferDownload=false" />
					</c:set>
					<html:link
						href="javascript:launchPopup('${viewURL}','offlinefile')">
						<fmt:message key="label.view" />
					</html:link>
					&nbsp;
					<c:set var="downloadURL">
						<html:rewrite
							page="/download/?uuid=${file.uuID}&versionID=${file.versionID}&preferDownload=true" />
					</c:set>
					<html:link href="${downloadURL}">
						<fmt:message key="label.download" />
					</html:link>
					&nbsp;
					<c:choose>
						<c:when test="${fileTypeFlag==CONS_OFFLINE}">
							<html:link href="#"
								onclick="deleteOfflineFile(${file.uuID},${file.versionID})">
								<fmt:message key="label.authoring.offline.delete" />
							</html:link>
						</c:when>
						<c:otherwise>
							<html:link href="#"
								onclick="deleteOnlineFile(${file.uuID},${file.versionID})">
								<fmt:message key="label.authoring.online.delete" />
							</html:link>
						</c:otherwise>
					</c:choose>
				</li>
			</c:if>

		</c:forEach>
	</ul>
</c:if>
