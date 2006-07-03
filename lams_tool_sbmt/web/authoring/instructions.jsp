<%@include file="/common/taglibs.jsp"%>

<!-- Instruction Tab Content -->
<table cellpadding="0">
	<!-- Instructions Row -->
	<tr>
		<td>
			<lams:SetEditor id="OnlineInstruction" text="${authoring.onlineInstruction}" key="label.authoring.online.instruction" small="true"/>
		</td>
	</tr>
	<tr>
		<td>
			<div id="onlinefile">
				<c:forEach var="file" items="${authoring.onlineFiles}">
					<li>
						<c:out value="${file.name}" />
						<c:set var="viewURL">
							<html:rewrite page="/download/?uuid=${file.uuID}&preferDownload=false" />
						</c:set>
						<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')"> <fmt:message key="label.view" /> </a>&nbsp;
						<c:set var="downloadURL">
							<html:rewrite page="/download/?uuid=${file.uuID}&versionID=${details.versionID}&preferDownload=true" />
						</c:set>
						<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <fmt:message key="label.download" /> </a>&nbsp;
						<c:set var="deleteonline">
							<html:rewrite page="/deletefile.do?method=deleteOnlineFile&toolContentID=${authoring.contentID}&uuID=${file.uuID}&versionID=${file.versionID}" />
						</c:set>
						<html:link href="javascript:loadDoc('${deleteonline}','onlinefile')">
							<fmt:message key="label.authoring.online.delete" />
						</html:link>
					</li>
				</c:forEach>
			</div>
		</td>
	</tr>
	<tr>
		<td class="field-name">
			<fmt:message key="label.authoring.online.file" />
			:
		</td>
	</tr>
	<tr>
		<td>
			<html:file property="onlineFile">
				<fmt:message key="label.authoring.choosefile.button" />
			</html:file>
			<html:button onclick="javascript:doSubmit('uploadOnline');" property="uploadOnlineButt" styleClass="button">
				<fmt:message key="label.authoring.upload.online.button" />
			</html:button>
		</td>
	</tr>
	<!-- Offline Instructions -->
</table>

<hr/>

<table cellpadding="0">	
	
	<tr>
		<td>
			<lams:SetEditor id="OfflineInstruction" text="${authoring.offlineInstruction}" key="label.authoring.offline.instruction" small="true" alt="true"/>
		</td>
	</tr>
	<tr>
		<td>
			<div id="offlinefile">
				<c:forEach var="file" items="${authoring.offlineFiles}">
					<li>
						<c:out value="${file.name}" />
						<html:link href="javascript:launchInstructionsPopup('download/?uuid=${file.uuID}&preferDownload=false')">
							<fmt:message key="label.view" />
						</html:link>
						<c:set var="downloadOfflineURL">
							<html:rewrite page="/download/?uuid=${file.uuID}&versionID=${details.versionID}&preferDownload=true" />
						</c:set>
						<html:link href="${downloadOfflineURL}">
							<fmt:message key="label.download" />
						</html:link>
						<c:set var="deleteoffline">
							<html:rewrite page="/deletefile.do?method=deleteOfflineFile&toolContentID=${authoring.contentID}&uuID=${file.uuID}&versionID=${file.versionID}" />
						</c:set>
						<html:link href="javascript:loadDoc('${deleteoffline}','offlinefile')">
							<fmt:message key="label.authoring.offline.delete" />
						</html:link>
					</li>
				</c:forEach>
			</div>
		</td>
	</tr>
	<tr>
		<td class="field-name-alternative-color">
			<fmt:message key="label.authoring.offline.file" />
			:
		</td>
	</tr>
	<tr>
		<td>
			<html:file property="offlineFile">
				<fmt:message key="label.authoring.choosefile.button" />
			</html:file>
			<html:button onclick="javascript:doSubmit('uploadOffline');" property="uploadOfflineButt" styleClass="button">
				<fmt:message key="label.authoring.upload.offline.button" />
			</html:button>
		</td>
	</tr>
</table>
