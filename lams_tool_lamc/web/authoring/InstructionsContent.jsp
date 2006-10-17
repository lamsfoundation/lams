
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

<%@ include file="/common/taglibs.jsp" %>

		<table class="forms">

				<tr>
					<td NOWRAP colspan=2 valign=top>
						<div class="field-name" style="text-align: left;">
							<fmt:message key="label.onlineInstructions.col"></fmt:message>
						</div>
						<html:textarea property="onlineInstructions" rows="3" cols="80"></html:textarea>
					</td>
				</tr>
				
				
				<tr>
				<td NOWRAP colspan=2 valign=top>
				<table class="forms">
					<tr><td align=center>

							<table  width="100%" align=center  border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										<table width="70%" align="left">

									<c:forEach var="attachment" items="${mcGeneralAuthoringDTO.attachmentList}">
											<c:if test="${attachment.fileOnline == true }"> 			
								            	<bean:define id="view">/download/?uuid=<bean:write name="attachment" property="uuid"/>&preferDownload=false</bean:define>
												<bean:define id="download">/download/?uuid=<bean:write name="attachment" property="uuid"/>&preferDownload=true</bean:define>
						                        <bean:define id="uuid" name="attachment" property="uuid" />
						                        
						                        <tr>
									            	<td> <bean:write name="attachment" property="fileName"/>  </td>
										            <td>
											        	<table>
												        	<tr>
												            	<td>
												                	<a href='javascript:launchInstructionsPopup("<html:rewrite page='<%=view%>'/>")' class="button">
												                   		<fmt:message key="label.view" />
												                    </a>
																	&nbsp&nbsp
													            	<html:link page="<%=download%>" styleClass="button">
													                	<fmt:message key="label.download" />
													                </html:link>
																	&nbsp&nbsp   
													            	<html:link page="/authoring.do?dispatch=deleteFile&httpSessionID=${mcGeneralAuthoringDTO.httpSessionID}&toolContentID=${mcGeneralAuthoringDTO.toolContentID}&contentFolderID=${mcGeneralAuthoringDTO.contentFolderID}&activeModule=${mcGeneralAuthoringDTO.activeModule}&defaultContentIdStr=${mcGeneralAuthoringDTO.defaultContentIdStr}&sln=${mcGeneralAuthoringDTO.sln}&questionsSequenced=${mcGeneralAuthoringDTO.questionsSequenced}&retries=${mcGeneralAuthoringDTO.retries}&reflect=${mcGeneralAuthoringDTO.reflect}&reflectionSubject=${mcGeneralAuthoringDTO.reflectionSubject}&passmark=${mcGeneralAuthoringDTO.passMarkValue}"
													                         	paramId="uuid" paramName="attachment" paramProperty="uuid"
													                         	onclick="javascript:return confirm('Are you sure you want to delete this file?')"
													                         	target="_self" styleClass="button">
													                	<fmt:message key="label.delete" />
													                </html:link> 
													            </td>
												           	</tr>
											            </table>
										           	</td>
									   	     	</tr>
											</c:if> 											   	     	
										</c:forEach>
										</table>
								 	</td>
								</tr>
							</table>



							 	</td>
							</tr>
				</table>
					</td> 				
				</tr>
				
				
				<tr> 
					<td class="field-name"> 
	          				<fmt:message key="label.onlineFiles" />
          			</td>
          			<td NOWRAP> 
						<html:file  property="theOnlineFile"></html:file>
					 	<html:submit property="submitOnlineFile" 
                                     styleClass="linkbutton" 
                                     onclick="submitMethod('addNewFile');">
								<fmt:message key="label.upload"/>
						</html:submit>
					</td> 
				
				</tr>


				<tr>
					<td NOWRAP colspan=2 valign=top>
						<div class="field-name" style="text-align: left;">
							<fmt:message key="label.offlineInstructions.col"></fmt:message>
						</div>
						<html:textarea property="offlineInstructions" rows="3" cols="80"></html:textarea>
					</td>
				</tr>


				<tr>
					<td NOWRAP colspan=2 valign=top>
				<table class="forms">
					<tr><td align=center>
							<table  width="100%" align=center  border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										<table width="70%" align="left">

									<c:forEach var="attachment" items="${mcGeneralAuthoringDTO.attachmentList}">
		
											<c:if test="${attachment.fileOnline == false}"> 			
								            	<bean:define id="view">/download/?uuid=<bean:write name="attachment" property="uuid"/>&preferDownload=false</bean:define>
												<bean:define id="download">/download/?uuid=<bean:write name="attachment" property="uuid"/>&preferDownload=true</bean:define>
						                        <bean:define id="uuid" name="attachment" property="uuid" />
						                        
						                        <tr>
									            	<td> <bean:write name="attachment" property="fileName"/>  </td>
										            <td>
											        	<table>
												        	<tr>
												            	<td>
												                	<a href='javascript:launchInstructionsPopup("<html:rewrite page='<%=view%>'/>")' class="button">
												                   		<fmt:message key="label.view" />
												                    </a>
																	&nbsp&nbsp
													            	<html:link page="<%=download%>" styleClass="button">
													                	<fmt:message key="label.download" />
													                </html:link>
																	&nbsp&nbsp
													            	<html:link page="/authoring.do?dispatch=deleteFile&httpSessionID=${mcGeneralAuthoringDTO.httpSessionID}&toolContentID=${mcGeneralAuthoringDTO.toolContentID}&contentFolderID=${mcGeneralAuthoringDTO.contentFolderID}&activeModule=${mcGeneralAuthoringDTO.activeModule}&defaultContentIdStr=${mcGeneralAuthoringDTO.defaultContentIdStr}&sln=${mcGeneralAuthoringDTO.sln}&questionsSequenced=${mcGeneralAuthoringDTO.questionsSequenced}&retries=${mcGeneralAuthoringDTO.retries}&reflect=${mcGeneralAuthoringDTO.reflect}&reflectionSubject=${mcGeneralAuthoringDTO.reflectionSubject}&passmark=${mcGeneralAuthoringDTO.passMarkValue}"
													            	paramId="uuid" paramName="attachment" paramProperty="uuid"
													                         	onclick="javascript:return confirm('Are you sure you want to delete this file?')"
													                         	target="_self" styleClass="button">
													                	<fmt:message key="label.delete" />
													                </html:link> 
													            </td>
												           	</tr>
											            </table>
										           	</td>
									   	     	</tr>
										</c:if> 											   	     	
											</c:forEach>
										</table>
								 	</td>
								</tr>
							</table>

							 	</td>
							</tr>
				</table>
					</td> 				
				</tr>



				<tr> 
					<td class="field-name"> 
          				<fmt:message key="label.offlineFiles" />
          			</td>
          			<td NOWRAP> 
						<html:file  property="theOfflineFile"></html:file>
					 	<html:submit property="submitOfflineFile" 
                                     styleClass="linkbutton" 
                                     onclick="submitMethod('addNewFile');">
								<fmt:message key="label.upload"/>
						</html:submit>
					</td> 
				</tr>          		
			</table>	  	
	


	
	




