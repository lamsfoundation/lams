<%@ include file="/common/taglibs.jsp" %>
<%@ page import=" org.lamsfoundation.lams.contentrepository.client.IToolContentHandler"%>
<c:set var="CONS_OFFLINE" value="<%=IToolContentHandler.TYPE_OFFLINE %>"/>
<c:choose>
	<c:when test="${fileTypeFlag==CONS_OFFLINE}">
		<c:set var="targetFileType" value="<%=IToolContentHandler.TYPE_OFFLINE %>"/>
	</c:when>
	<c:otherwise>
		<c:set var="targetFileType" value="<%=IToolContentHandler.TYPE_ONLINE %>"/>
	</c:otherwise>
</c:choose>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="fileList" value="${sessionMap.attachmentList}"/>

<%-- check whehter has target file type --%>
<c:forEach var="file" items="${fileList}">
	<c:if test="${targetFileType == file.fileType}">
		<c:set var="hasFile" value="${true}"/>
	</c:if>
</c:forEach>

<%-- Display target file type --%>
<c:if test="${hasFile}">
	<table style="align:left;width:600px" border="0" >
		<c:forEach var="file" items="${fileList}">
			<c:if test="${targetFileType == file.fileType}">
			<tr>
					<td>
						<c:out value="${file.fileName}"/>
					</td>
					<td width="80px" align="center">
						<c:set var="viewURL">
							<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=false"/>
						</c:set>
						<html:link href="javascript:launchPopup('${viewURL}','offlinefile')" styleClass="button">
							<fmt:message key="label.view"/>
						</html:link>
					</td>
					<td width="100px" align="center">
						<c:set var="downloadURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true"/>
						</c:set>
						<html:link href="${downloadURL}"  styleClass="button">
							<fmt:message key="label.download"/>
						</html:link>
					</td>
					<td width="80px" align="center">
					<c:choose>
						<c:when test="${fileTypeFlag==CONS_OFFLINE}">
							<html:link href="#" onclick="deleteOfflineFile(${file.fileUuid},${file.fileVersionId})" styleClass="button">
								<fmt:message key="label.authoring.offline.delete"/>
							</html:link>
						</c:when>
						<c:otherwise>
							<html:link href="#" onclick="deleteOnlineFile(${file.fileUuid},${file.fileVersionId})" styleClass="button">
								<fmt:message key="label.authoring.online.delete"/>
							</html:link>
						</c:otherwise>
					</c:choose>
					</td>				
				</tr>			
			</c:if>
		</c:forEach>
	</table>
</c:if>