
	<!---------------------------Instruction Tab Content ------------------------>
	<div id='content_i'  class="tabbody content_i">
	<script type="text/javascript"	src="<html:rewrite page='/includes/javascript/xmlrequest.js'/>"></script>
	<h2><fmt:message key="label.authoring.heading.instructions.desc" /></h2>
	<table class="forms">
		<!--hidden field contentID passed by flash-->
		<tr>
		</tr>
		<!-- Instructions Row -->
		<tr>
			<td class="formlabel"><fmt:message
				key="label.authoring.online.instruction" />:</td>
			<td class="formcontrol"><FCK:editor id="forum.onlineInstructions"
				basePath="/lams/fckeditor/" height="150" width="85%">
				<c:out value="${formBean.forum.onlineInstructions}" escapeXml="false"/>
			</FCK:editor></td>
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
		<tr>
			<td class="formlabel">
				<fmt:message key="label.authoring.online.file" />:
			</td>
			<td class="formcontrol">
				<html:file property="onlineFile">
					<fmt:message key="label.authoring.choosefile.button" />
				</html:file>
				<c:set var="uploadonline">
					<html:rewrite page="/authoring/uploadOnline.do"/>
				</c:set>
				<html:button property="onlineUpload" onclick="submitForm('${uploadonline}')">
					<fmt:message key="label.authoring.upload.online.button" />
				</html:button>
			</td>
		</tr>	
		<!------------Offline Instructions ----------------------->
		<tr>
			<td class="formlabel"><fmt:message
				key="label.authoring.offline.instruction" />:</td>
			<td class="formcontrol"><FCK:editor id="forum.offlineInstructions"
				basePath="/lams/fckeditor/" height="150" width="85%">
				<c:out value="${formBean.forum.offlineInstructions}" escapeXml="false"/>
			</FCK:editor></td>
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
		<tr>
			<td class="formlabel">
				<fmt:message key="label.authoring.offline.file" />:
			</td>
			<td class="formcontrol">
				<html:file property="offlineFile">
					<fmt:message key="label.authoring.choosefile.button" />
				</html:file>
				<c:set var="uploadoffline">
					<html:rewrite page="/authoring/uploadOffline.do"/>
				</c:set>
				<html:button property="offlineUpload" onclick="submitForm('${uploadoffline}')">
					<fmt:message key="label.authoring.upload.offline.button" />
				</html:button>
			</td>
		</tr>			
		</tr>
		<tr><td colspan="2"><html:errors/></td></tr>
		</table>
		<!-- Button Row -->
		<HR>
		<p align="right">
			<html:submit property="save" styleClass="a.button">
				<fmt:message key="label.authoring.save.button" />
			</html:submit>
			<html:button property="cancel"
				onclick="window.close()" styleClass="a.button">
				<fmt:message key="label.authoring.cancel.button" />
			</html:button>
		</p>
	</div>