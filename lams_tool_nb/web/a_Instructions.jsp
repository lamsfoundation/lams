<div id="formtablecontainer">
<table class="forms">
	<tr>
		<td>
			<table width="65%" align="center">
				<tr>
					<td class="formlabel"><fmt:message key="instructions.onlineInstructions" /></td>
					<td class="formcontrol">
							<FCK:editor id="richTextOnlineInstructions" basePath="/lams/fckeditor/"
								height="200"
								width="85%">
								<c:out value="${NbAuthoringForm.onlineInstructions}" escapeXml="false" />
							</FCK:editor>
					</td>
				</tr>
				
				<tr>
					<td class="formlabel">
						<fmt:message key="instructions.uploadOnlineInstr" />
					</td>
					<td class="formcontrol">
						<html:file property="onlineFile" /><html:submit property="method"><fmt:message key="button.upload" /></html:submit>
											
					</td>						
				</tr>
				<tr>
					<td class="formlabel"><fmt:message key="instructions.offlineInstructions" /></td>
					<td class="formcontrol">
						<FCK:editor id="richTextOfflineInstructions" basePath="/lams/fckeditor/"
								width="85%"
								height="200">
							<c:out value="${NbAuthoringForm.offlineInstructions}" escapeXml="false" />
						</FCK:editor>
					</td>
				</tr>
				<tr>
					<td class="formlabel">
						<fmt:message key="instructions.uploadOfflineInstr" />
					</td>
					<td class="formcontrol">
						<html:file property="offlineFile" /><html:submit property="method"><fmt:message key="button.upload" /></html:submit>
					</td>						
				</tr>
			
			</table>
		</td>
	</tr>
</table>
</div>
<logic:present name="attachmentList">
<bean:size id="count" name="attachmentList" />
<logic:notEqual name="count" value="0">

	<h2><fmt:message key="label.attachments" /></h2>
	<div id="datatablecontainer">
		<table width="100%"  border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<table width="70%" align="left">
		            <tr>
		                <td><fmt:message key="label.filename" /></td>
		                <td><fmt:message key="label.type" /></td>
		            	<td>&nbsp;</td>
		            </tr>
		            <logic:iterate name="attachmentList" id="attachment">
		            	<bean:define id="view">/download/?uuid=<bean:write name="attachment" property="uuid"/>&preferDownload=false</bean:define>
						<bean:define id="download">/download/?uuid=<bean:write name="attachment" property="uuid"/>&preferDownload=true</bean:define>
                        <bean:define id="uuid" name="attachment" property="uuid" />
                        
                        <tr>
			                         	
			            	<td><bean:write name="attachment" property="filename"/></td>
			                <td>
			                	<c:choose>
					            	<c:when test="${attachment.onlineFile}" >
					                	<fmt:message key="instructions.type.online" />
					               	</c:when>
					                <c:otherwise>
					                	<fmt:message key="instructions.type.offline" />
					                </c:otherwise>
				                </c:choose>
				            </td>
				            <td>
					        	<table>
						        	<tr>
						            	<td>
						                	<a href='javascript:launchInstructionsPopup("<html:rewrite page='<%=view%>'/>")' class="button">
						                   		<fmt:message key="link.view" />
						                    </a>
						               	</td>
						                <td>
							            	<html:link page="<%=download%>" styleClass="button">
							                	<fmt:message key="link.download" />
							                </html:link>
						                </td>
						                <td>
							            	<html:link page="/authoring.do?method=Delete" 
							                         	paramId="uuid" paramName="attachment" paramProperty="uuid"
							                         	onclick="javascript:return confirm('Are you sure you want to delete this file?')"
							                         	target="_self" styleClass="button">
							                	<fmt:message key="link.delete" />
							                </html:link> 
							            </td>
						           	</tr>
					            </table>
				           	</td>
			   	     	</tr>
		    	    </logic:iterate>
					</table>
			 	</td>
			</tr>
		</table>
	</div>
	<hr>
 </logic:notEqual>
 </logic:present>
		      

<div id="datatablecontainer">
<table width="100%" align="center">
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td align="center"><html:submit property="method"><fmt:message key="button.done"/></html:submit>
		</td>
	</tr>
</table>
</div>