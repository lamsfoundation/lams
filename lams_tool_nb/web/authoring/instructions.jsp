<%@ include file="/includes/taglibs.jsp"%>

<c:set var="onlineExist" value="false" />
<c:set var="offlineExist" value="false" />
<c:forEach var="att" items="${requestScope.sessionMap.attachmentList}">
	<c:if test="${att.onlineFile}">
		<c:set var="onlineExist" value="true" />
	</c:if>
	<c:if test="${not att.onlineFile}">
		<c:set var="offlineExist" value="true" />
	</c:if>
</c:forEach>
<html:hidden property="deleteFileUuid" />

<!-- Instructions Tab Content  -->
<table>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="instructions.onlineInstructions"></fmt:message>
			</div>
			<html:textarea property="onlineInstructions" rows="3" cols="75"></html:textarea>
		</td>
	</tr>

	<c:if test="${onlineExist == 'true'}">
		<tr>
			<td>
				<c:if test="${not empty requestScope.sessionMap.attachmentList}">
					<ul>
						<c:forEach var="attachment"
							items="${requestScope.sessionMap.attachmentList}">
							<c:if test="${attachment.onlineFile}">
								<li>
									${attachment.filename}
									<c:set var="viewURL">
										<html:rewrite
											page="/download/?uuid=${attachment.uuid}&amp;preferDownload=false" />
									</c:set>
									<a href="javascript:launchInstructionsPopup('${viewURL}');">
										<fmt:message key="link.view" /> </a> &nbsp;

									<c:set var="downloadURL">
										<html:rewrite
											page="/download/?uuid=${attachment.uuid}&amp;preferDownload=true" />
									</c:set>
									<a href="${downloadURL}"> <fmt:message key="link.download" />
									</a> &nbsp;

									<html:link
										href="javascript:deleteAttachment('${attachment.uuid}')">
										<fmt:message key="link.delete" />
									</html:link>
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</c:if>
			</td>
		</tr>
	</c:if>

	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="instructions.uploadOnlineInstr" />
			</div>

			<html:file property="onlineFile" />
			<html:link href="javascript:doUpload();" property="submit"
				styleClass="button">
				<fmt:message key="button.upload" />
			</html:link>
		</td>
	</tr>

	<tr>
		<td>
			<hr />
		</td>
	</tr>

	<tr>
		<td>
			<div class="field-name-alternative-color">
				<fmt:message key="instructions.offlineInstructions"></fmt:message>
			</div>
			<html:textarea property="offlineInstructions" rows="3" cols="75"></html:textarea>
		</td>
	</tr>

	<c:if test="${offlineExist == 'true'}">
		<tr>
			<td>
				<c:if test="${not empty requestScope.sessionMap.attachmentList}">
					<ul>
						<c:forEach var="attachment"
							items="${requestScope.sessionMap.attachmentList}">
							<c:if test="${not attachment.onlineFile}">
								<li>
									${attachment.filename}
									<c:set var="viewURL">
										<html:rewrite
											page="/download/?uuid=${attachment.uuid}&amp;preferDownload=false" />
									</c:set>
									<a href="javascript:launchInstructionsPopup('${viewURL}');">
										<fmt:message key="link.view" /> </a> &nbsp;

									<c:set var="downloadURL">
										<html:rewrite
											page="/download/?uuid=${attachment.uuid}&amp;preferDownload=true" />
									</c:set>
									<a href="${downloadURL}"> <fmt:message key="link.download" />
									</a> &nbsp;

									<html:link
										href="javascript:deleteAttachment('${attachment.uuid}')">
										<fmt:message key="link.delete" />
									</html:link>
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</c:if>
			</td>
		</tr>
	</c:if>

	<tr>
		<td>
			<div class="field-name-alternative-color">
				<fmt:message key="instructions.uploadOfflineInstr" />
			</div>

			<html:file property="offlineFile" />
			<html:link href="javascript:doUpload();" property="submit"
				styleClass="button">
				<fmt:message key="button.upload" />
			</html:link>
		</td>
	</tr>
</table>
