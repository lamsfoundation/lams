<%@ include file="/common/taglibs.jsp" %>
<table style="align:left;width:600px" border="0" >
	<c:forEach var="file" items="${onlineFileList}">
	<tr>
		<td>
			<c:out value="${file.name}"/>
		</td>
		<td width="80px" align="center">
			<c:set var="viewURL">
				<html:rewrite page="/download/?uuid=${file.uuID}&versionID=${file.versionID}&preferDownload=false"/>
			</c:set>
			<html:link href="javascript:launchPopup('${viewURL}','onlinefile')" styleClass="button">
				<fmt:message key="label.view"/>
			</html:link>
		</td>
		<td width="100px" align="center">
			<c:set var="downloadURL">
					<html:rewrite page="/download/?uuid=${file.uuID}&versionID=${file.versionID}&preferDownload=true"/>
			</c:set>
			<html:link href="${downloadURL}"  styleClass="button">
				<fmt:message key="label.download"/>
			</html:link>
		</td>
		<td width="80px" align="center">
			<html:link href="#" onclick="deleteOnlineFile(${file.uuID},${file.versionID})" styleClass="button">
				<fmt:message key="label.authoring.online.delete"/>
			</html:link>
		</td>				
	</tr>
	</c:forEach>
</table>