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
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ include file="/common/taglibs.jsp" %>

		<table class="forms">
          		<tr> 
					<td NOWRAP colspan=2 valign=top>
						<lams:SetEditor id="onlineInstructions" text="${QaAuthoringForm.onlineInstructions}" small="true" key="label.onlineInstructions.col"/>								
					</td> 
				</tr>
				
				<tr>
					<td NOWRAP colspan=2 valign=top>
				<table class="forms">
					<tr><td align=center>
					<logic:present name="attachmentList">
					<bean:size id="count" name="attachmentList" />
					<logic:notEqual name="count" value="0">
							<table  width="100%" align=center  border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										<table width="70%" align="left">

							            <logic:iterate name="attachmentList" id="attachment">
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
												                   		<bean:message key="link.view" />
												                    </a>
																	&nbsp&nbsp
													            	<html:link page="<%=download%>" styleClass="button">
													                	<bean:message key="link.download" />
													                </html:link>
																	&nbsp&nbsp
													            	<html:link page="/authoring.do?dispatch=deleteFile" 
													                         	paramId="uuid" paramName="attachment" paramProperty="uuid"
													                         	onclick="javascript:return confirm('Are you sure you want to delete this file?')"
													                         	target="_self" styleClass="button">
													                	<bean:message key="link.delete" />
													                </html:link> 
													            </td>
												           	</tr>
											            </table>
										           	</td>
									   	     	</tr>
										</c:if> 											   	     	
							    	    </logic:iterate>
										</table>
								 	</td>
								</tr>
							</table>
					 </logic:notEqual>
					 </logic:present>
							 	</td>
							</tr>
				</table>
					</td> 				
				</tr>
				
				
				<tr> 
					<td class="field-name"> <b>
	          				<bean:message key="label.onlineFiles" />
          				</b> 
          			</td>
          			<td NOWRAP> 
						<html:file  property="theOnlineFile"></html:file>
					 	<html:submit property="submitOnlineFile" 
                                     styleClass="linkbutton" 
                                     onclick="submitMethod('addNewFile');">
								<bean:message key="label.upload"/>
						</html:submit>
					</td> 
				
				</tr>
				<tr> 
					<td colspan=2 NOWRAP> 
						<lams:SetEditor id="offlineInstructions" text="${QaAuthoringForm.offlineInstructions}" small="true" key="label.offlineInstructions.col"/>								
					</td> 
				</tr>


				<tr>
					<td NOWRAP colspan=2 valign=top>
				<table class="forms">
					<tr><td align=center>
					<logic:present name="attachmentList">
					<bean:size id="count" name="attachmentList" />
					<logic:notEqual name="count" value="0">
							<table  width="100%" align=center  border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										<table width="70%" align="left">

							            <logic:iterate name="attachmentList" id="attachment">
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
												                   		<bean:message key="link.view" />
												                    </a>
																	&nbsp&nbsp
													            	<html:link page="<%=download%>" styleClass="button">
													                	<bean:message key="link.download" />
													                </html:link>
																	&nbsp&nbsp
													            	<html:link page="/authoring.do?dispatch=deleteFile" 
													                         	paramId="uuid" paramName="attachment" paramProperty="uuid"
													                         	onclick="javascript:return confirm('Are you sure you want to delete this file?')"
													                         	target="_self" styleClass="button">
													                	<bean:message key="link.delete" />
													                </html:link> 
													            </td>
												           	</tr>
											            </table>
										           	</td>
									   	     	</tr>
										</c:if> 											   	     	
							    	    </logic:iterate>
										</table>
								 	</td>
								</tr>
							</table>
					 </logic:notEqual>
					 </logic:present>
							 	</td>
							</tr>
				</table>
					</td> 				
				</tr>



				<tr> 
					<td class="field-name"> <b>
          				<bean:message key="label.offlineFiles" />
	          			</b>
          			</td>
          			<td NOWRAP> 
						<html:file  property="theOfflineFile"></html:file>
					 	<html:submit property="submitOfflineFile" 
                                     styleClass="linkbutton" 
                                     onclick="submitMethod('addNewFile');">
								<bean:message key="label.upload"/>
						</html:submit>
					</td> 
				</tr>          		
			</table>	  	
	


	
	




