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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ include file="/common/taglibs.jsp"%>


		<table class="forms">
          		<tr> 
					<td NOWRAP colspan=2 valign=top>
                   		<b> <fmt:message key="label.onlineInstructions.col" /> </b> 
                   		<c:out value="${mcGeneralMonitoringDTO.onlineInstructions}" escapeXml="false" />						
					</td> 
				</tr>
				
				
				
				<tr>
					<td NOWRAP colspan=2 valign=top>
				<table class="forms">
					<tr><td align=center>

							<table  width="100%" align=center  border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										<table width="70%" class="align-left">

       									<c:forEach var="attachment" items="${mcGeneralMonitoringDTO.attachmentList}">
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
					<td colspan=2 NOWRAP> 
                   		<b> <fmt:message key="label.offlineInstructions.col" /> </b>	<c:out value="${mcGeneralMonitoringDTO.offlineInstructions}" escapeXml="false" />												
					</td> 
				</tr>


				<tr>
					<td NOWRAP colspan=2 valign=top>
				<table class="forms">
					<tr><td align=center>

							<table  width="100%" align=center  border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										<table width="70%" class="align-left">

       									<c:forEach var="attachment" items="${mcGeneralMonitoringDTO.attachmentList}">
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

			</table>	  	
