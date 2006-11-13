<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ include file="/common/taglibs.jsp"%>

<table>

	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.onlineInstructions.col"></fmt:message>
			</div>
			<html:textarea property="onlineInstructions" rows="3" cols="75"></html:textarea>
		</td>
	</tr>

	<tr>
		<td>
			<ul>
				<c:forEach var="attachment"
					items="${qaGeneralAuthoringDTO.attachmentList}">
					<c:if test="${attachment.fileOnline == true }">
						<li>
							<bean:define id="view">/download/?uuid=<bean:write
									name="attachment" property="uuid" />&preferDownload=false</bean:define>
							<bean:define id="download">/download/?uuid=<bean:write
									name="attachment" property="uuid" />&preferDownload=true</bean:define>
							<bean:define id="uuid" name="attachment" property="uuid" />

							<bean:write name="attachment" property="fileName" />
							<a
								href='javascript:launchInstructionsPopup("<html:rewrite page='<%=view%>'/>")'>
								<fmt:message key="link.view" /> </a> &nbsp
							<html:link page="<%=download%>">
								<fmt:message key="link.download" />
							</html:link>
							&nbsp
							<html:link
								page="/authoring.do?dispatch=deleteFile&httpSessionID=${qaGeneralAuthoringDTO.httpSessionID}&toolContentID=${qaGeneralAuthoringDTO.toolContentID}&contentFolderID=${qaGeneralAuthoringDTO.contentFolderID}&activeModule=${qaGeneralAuthoringDTO.activeModule}&defaultContentIdStr=${qaGeneralAuthoringDTO.defaultContentIdStr}&synchInMonitor=${qaGeneralAuthoringDTO.synchInMonitor}&usernameVisible=${qaGeneralAuthoringDTO.usernameVisible}&showOtherAnswers=${qaGeneralAuthoringDTO.showOtherAnswers}&lockWhenFinished=${qaGeneralAuthoringDTO.lockWhenFinished}&questionsSequenced=${qaGeneralAuthoringDTO.questionsSequenced}&reflect=${qaGeneralAuthoringDTO.reflect}&reflectionSubject=${qaGeneralAuthoringDTO.reflectionSubject}"
								paramId="uuid" paramName="attachment" paramProperty="uuid"
								onclick="javascript:return confirm('Are you sure you want to delete this file?')"
								target="_self">
								<fmt:message key="link.delete" />
							</html:link>
						</li>
					</c:if>
				</c:forEach>
			</ul>
		</td>
	</tr>

	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.onlineFiles" />
			</div>

			<html:file property="theOnlineFile"></html:file>
			<html:submit property="submitOnlineFile" styleClass="button"
				onclick="submitMethod('addNewFile');">
				<fmt:message key="label.upload" />
			</html:submit>
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
				<fmt:message key="label.offlineInstructions.col"></fmt:message>
			</div>
			<html:textarea property="offlineInstructions" rows="3" cols="75"></html:textarea>
		</td>
	</tr>
	<tr>
		<td>
			<ul>
				<c:forEach var="attachment"
					items="${qaGeneralAuthoringDTO.attachmentList}">
					<c:if test="${attachment.fileOnline == false}">
						<li>
							<bean:define id="view">/download/?uuid=<bean:write
									name="attachment" property="uuid" />&preferDownload=false</bean:define>
							<bean:define id="download">/download/?uuid=<bean:write
									name="attachment" property="uuid" />&preferDownload=true</bean:define>
							<bean:define id="uuid" name="attachment" property="uuid" />


							<bean:write name="attachment" property="fileName" />

							<a
								href='javascript:launchInstructionsPopup("<html:rewrite page='<%=view%>'/>")'>
								<fmt:message key="link.view" /> </a> &nbsp&nbsp
							<html:link page="<%=download%>">
								<fmt:message key="link.download" />
							</html:link>
							&nbsp&nbsp
							<html:link
								page="/authoring.do?dispatch=deleteFile&httpSessionID=${qaGeneralAuthoringDTO.httpSessionID}&toolContentID=${qaGeneralAuthoringDTO.toolContentID}&contentFolderID=${qaGeneralAuthoringDTO.contentFolderID}&activeModule=${qaGeneralAuthoringDTO.activeModule}&defaultContentIdStr=${qaGeneralAuthoringDTO.defaultContentIdStr}&synchInMonitor=${qaGeneralAuthoringDTO.synchInMonitor}&usernameVisible=${qaGeneralAuthoringDTO.usernameVisible}&showOtherAnswers=${qaGeneralAuthoringDTO.showOtherAnswers}&questionsSequenced=${qaGeneralAuthoringDTO.questionsSequenced}&reflect=${qaGeneralAuthoringDTO.reflect}&reflectionSubject=${qaGeneralAuthoringDTO.reflectionSubject}"
								paramId="uuid" paramName="attachment" paramProperty="uuid"
								onclick="javascript:return confirm('Are you sure you want to delete this file?')"
								target="_self">
								<fmt:message key="link.delete" />
							</html:link>
						</li>
					</c:if>

				</c:forEach>
			</ul>
		</td>
	</tr>
	<tr>
		<td>
			<div class="field-name-alternative-color">
				<fmt:message key="label.offlineFiles" />
			</div>
			<html:file property="theOfflineFile"></html:file>
			<html:submit property="submitOfflineFile" styleClass="button"
				onclick="submitMethod('addNewFile');">
				<fmt:message key="label.upload" />
			</html:submit>
		</td>
	</tr>
</table>
