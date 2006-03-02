<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams" %>

	<!---------------------------Instruction Tab Content ------------------------>
	<table class="forms">
		<!--hidden field contentID passed by flash-->
		<tr>
		</tr>
		<!-- Instructions Row -->
		<tr>
			<td class="formlabel"><fmt:message
				key="label.authoring.online.instruction" />:</td>
			<td NOWRAP width="700">
				<lams:SetEditor id="OnlineInstruction" text="${authoring.onlineInstruction}"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td class="formcontrol">
				<div id="onlinefile">
				<c:forEach var="file" items="${authoring.onlineFiles}">
					<li><c:out value="${file.name}"/>
					<c:set var="viewURL">
						<html:rewrite page="/download/?uuid=${file.uuID}&preferDownload=false"/>
					</c:set>
					<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
						<fmt:message key="label.view"/>
					</a>&nbsp;
					<c:set var="downloadURL">
							<html:rewrite page="/download/?uuid=${file.uuID}&versionID=${details.versionID}&preferDownload=true"/>
					</c:set>
					<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
						<fmt:message key="label.download"/>
					</a>&nbsp;
					<c:set var="deleteonline">
					<html:rewrite page="/deletefile.do?method=deleteOnlineFile&toolContentID=${authoring.contentID}&uuID=${file.uuID}&versionID=${file.versionID}"/>
					</c:set>
					<html:link href="javascript:loadDoc('${deleteonline}','onlinefile')">
						<fmt:message key="label.authoring.online.delete"/>
					</html:link>					
					</li>
				</c:forEach>
				</div>
			</td>
		</tr>
		<tr>
			<td class="formlabel">
				<fmt:message key="label.authoring.online.file" />:
			</td>
			<td class="formcontrol">
				<html:file property="onlineFile">
					<fmt:message key="label.authoring.choosefile.button" />
				</html:file>
				<html:link href="javascript:;" property="submit" onclick="doSubmit('uploadOnline')">
					<fmt:message key="label.authoring.upload.online.button" />
				</html:link>
			</td>
		</tr>	
		<!------------Offline Instructions ----------------------->
		<tr>
			<td class="formlabel"><fmt:message
				key="label.authoring.offline.instruction" />:</td>
			<td NOWRAP width="700">
			<!-- remove <FCK:editor id="offlineInstruction"
				basePath="/lams/fckeditor/" height="150" width="85%">
				<c:out value="${authoring.offlineInstruction}" escapeXml="false"/>
			</FCK:editor>-->
			<lams:SetEditor id="OfflineInstruction" text="${authoring.offlineInstruction}"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td class="formcontrol">
				<div id="offlinefile">
				<c:forEach var="file" items="${authoring.offlineFiles}">
					<li><c:out value="${file.name}"/>
					<html:link href="javascript:launchInstructionsPopup('download/?uuid=${file.uuID}&preferDownload=false')">
						<fmt:message key="label.view"/>
					</html:link>
					<html:link href="../download/?uuid=${file.uuID}&preferDownload=true">
						<fmt:message key="label.download"/>
					</html:link>
					<c:set var="deleteoffline">
					<html:rewrite page="/deletefile.do?method=deleteOfflineFile&toolContentID=${authoring.contentID}&uuID=${file.uuID}&versionID=${file.versionID}"/>
					</c:set>
					<html:link href="javascript:loadDoc('${deleteoffline}','offlinefile')">
						<fmt:message key="label.authoring.offline.delete"/>
					</html:link>					
					</li>
				</c:forEach>
				</div>
			</td>
		</tr>
		<tr>
			<td class="formlabel">
				<fmt:message key="label.authoring.offline.file" />:
			</td>
			<td class="formcontrol">
				<html:file property="offlineFile">
					<fmt:message key="label.authoring.choosefile.button" />
				</html:file>
				<html:link href="javascript:;" property="submit" onclick="doSubmit('uploadOffline')">
					<fmt:message key="label.authoring.upload.offline.button" />
				</html:link>
			</td>
		</tr>			
		<tr><td colspan="2"><html:errors/></td></tr>
		</table>