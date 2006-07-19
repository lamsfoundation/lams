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


<!-- Instructions Tab Content  -->
<table>
	<tr>
		<td>
			<lams:SetEditor id="onlineInstructions" text="${NbAuthoringForm.onlineInstructions}" key="instructions.onlineInstructions" small="true" />
		</td>
	</tr>

	<c:if test="${onlineExist == 'true'}">
		<tr>
			<td>
				<c:if test="${not empty requestScope.sessionMap.attachmentList}">
					<ul>
						<c:forEach var="attachment" items="${requestScope.sessionMap.attachmentList}">
							<c:if test="${attachment.onlineFile}">
								<li>
									${attachment.filename}
									<c:set var="viewURL">
										<html:rewrite page="/download/?uuid=${attachment.uuid}&amp;preferDownload=false" />
									</c:set>
									<a href="javascript:launchInstructionsPopup('${viewURL}');"> <fmt:message key="link.view" /> </a> &nbsp;

									<c:set var="downloadURL">
										<html:rewrite page="/download/?uuid=${attachment.uuid}&amp;preferDownload=true" />
									</c:set>
									<a href="${downloadURL}"> <fmt:message key="link.download" /> </a> &nbsp;

									<c:set var="deleteURL">
										<html:rewrite page="/authoring.do?method=Delete&amp;uuid=${attachment.uuid}" />
									</c:set>
									<a href="${deleteURL}" onclick="javascript:return confirm('Are you sure you want to delete this file?')" target="_self"> <fmt:message key="link.delete" /> </a>
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</c:if>
			</td>
		</tr>
	</c:if>

	<tr>
		<td class="field-name">
			<fmt:message key="instructions.uploadOnlineInstr" />
		</td>
	</tr>
	<tr>
		<td>
			<html:file property="onlineFile" />
			<html:submit property="method" styleClass="button">
				<fmt:message key="button.upload" />
			</html:submit>
		</td>
	</tr>
</table>

<hr />

<table>
	<tr>
		<td>
			<lams:SetEditor id="offlineInstructions" text="${NbAuthoringForm.offlineInstructions}" key="instructions.offlineInstructions" alt="true" small="true" />
		</td>
	</tr>  

	<c:if test="${offlineExist == 'true'}">
		<tr>
			<td>
				<c:if test="${not empty requestScope.sessionMap.attachmentList}">
					<ul>
						<c:forEach var="attachment" items="${requestScope.sessionMap.attachmentList}">
							<c:if test="${not attachment.onlineFile}">
								<li>
									${attachment.filename}
									<c:set var="viewURL">
										<html:rewrite page="/download/?uuid=${attachment.uuid}&amp;preferDownload=false" />
									</c:set>
									<a href="javascript:launchInstructionsPopup('${viewURL}');"> <fmt:message key="link.view" /> </a> &nbsp;

									<c:set var="downloadURL">
										<html:rewrite page="/download/?uuid=${attachment.uuid}&amp;preferDownload=true" />
									</c:set>
									<a href="${downloadURL}"> <fmt:message key="link.download" /> </a> &nbsp;

									<c:set var="deleteURL">
										<html:rewrite page="/authoring.do?method=Delete&amp;uuid=${attachment.uuid}" />
									</c:set>
									<a href="${deleteURL}" onclick="javascript:return confirm('Are you sure you want to delete this file?')" target="_self"> <fmt:message key="link.delete" /> </a>
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</c:if>
			</td>
		</tr>
	</c:if>

	<tr>
		<td class="field-name-alternative-color">
			<fmt:message key="instructions.uploadOfflineInstr" />
		</td>
	</tr>
	<tr>
		<td>
			<html:file property="offlineFile" />
			<html:submit property="method" styleClass="button">
				<fmt:message key="button.upload" />
			</html:submit>
		</td>
	</tr>
</table>
