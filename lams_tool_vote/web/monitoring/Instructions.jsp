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


<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

	
				<table>
				<tr> 
					<td NOWRAP colspan=4 valign=top>
							<b> <bean:message key="label.onlineInstructions.col" />  </b>
							<c:out value="${voteGeneralMonitoringDTO.richTextOnlineInstructions}" escapeXml="false" />	
					</td> 
				</tr>
				

				<tr> 
					<td NOWRAP colspan=4 valign=top>
    	      				<b> <bean:message key="label.onlineFiles" />  </b>		
					</td> 
				</tr>

				<tr>
					<td NOWRAP colspan=4 align=left valign=top width="100%">
						<table align="left">
									<c:forEach var='file' items='${voteGeneralMonitoringDTO.listOnlineFilesMetadata}'>
											<tr>
												<td NOWRAP valign=top>
													 <c:out value="${file.filename}"/> 
													</td>
													<td NOWRAP valign=top>												
													<c:set scope="request" var="viewURL">
														<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=false"/>
													</c:set>
													<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
														 	<bean:message key="label.view"/> 
													</a>
													</td>
													<td NOWRAP valign=top>													

													<c:set scope="request" var="downloadURL">
															<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=true"/>
													</c:set>
													<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
														 <bean:message key="label.download"/> 
													</a>
													</td>
											</tr>
				         			</c:forEach>
	         			</table>
					</td> 
				</tr>

				
				<tr> 
					<td NOWRAP colspan=4 valign=top>
						<b> <bean:message key="label.offlineInstructions.col" /> </b>
					    <c:out value="${voteGeneralMonitoringDTO.richTextOfflineInstructions}" escapeXml="false" />	
					</td> 
				</tr>
				
				<tr> 
					<td NOWRAP colspan=4 valign=top>
    	      				<b> <bean:message key="label.offlineFiles" />  </b>		
					</td> 
				</tr>
				
				<tr> 
					<td NOWRAP colspan=4 align=left valign=top width="100%">
						<table align="left">
									<c:forEach var='file' items='${voteGeneralMonitoringDTO.listOfflineFilesMetadata}'>
											<tr>
												<td NOWRAP valign=top>
													 <c:out value="${file.filename}"/> 
													</td>
													<td NOWRAP valign=top>												
													<c:set scope="request" var="viewURL">
														<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=false"/>
													</c:set>
													<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
														 	<bean:message key="label.view"/> 
													</a>
													</td>
													<td NOWRAP valign=top>													

													<c:set scope="request" var="downloadURL">
															<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=true"/>
													</c:set>
													<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
														 <bean:message key="label.download"/> 
													</a>
													</td>
											</tr>
				         			</c:forEach>
	         			</table>
					</td> 
				</tr>


				<html:hidden property="fileItem"/>
				<html:hidden property="offlineFile"/>				
				<html:hidden property="uuid"/>				
				
			</table>	  	
		