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

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

				<table class="forms">
				<tr> 
					<td NOWRAP colspan=2 valign=top>
						<b> <bean:message key="label.onlineInstructions.col"/> </b>
						<c:out value="${sessionScope.richTextOnlineInstructions}" escapeXml="false" />												
					</td> 
				</tr>
				

				<tr>
					<td colspan=2 NOWRAP align=left valign=top width="50%">
						<table width="50%" cellspacing="8" align="CENTER" class="forms">
									<c:forEach var='file' items='${sessionScope.listOnlineFilesMetadata}'>
											<tr>
												<td NOWRAP valign=top>
													 <c:out value="${file.filename}"/> 
												</td>
												<td NOWRAP valign=top>												
													<c:set var="viewURL">
														<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=false"/>
													</c:set>
													<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
														 	<bean:message key="label.view"/> 
													</a>

												</td>
												<td NOWRAP valign=top>													
													<c:set var="downloadURL">
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
					<td NOWRAP colspan=2 valign=top>
						<b> <bean:message key="label.offlineInstructions.col"/> </b>
						<c:out value="${richTextOfflineInstructions}" escapeXml="false" />																		
					</td> 
				</tr>

				<tr> 
					<td colspan=2 NOWRAP align=left valign=top width="50%">
						<table width="50%" cellspacing="8" align="CENTER" class="forms">
									<c:forEach var='file' items='${sessionScope.listOfflineFilesMetadata}'>
											<tr>
												<td NOWRAP valign=top>
													 <c:out value="${file.filename}"/> 
												</td>
												<td NOWRAP valign=top>												
													<c:set var="viewURL">
														<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=false"/>
													</c:set>
													<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
														 	<bean:message key="label.view"/> 
													</a>

												</td>
												<td NOWRAP valign=top>													
													<c:set var="downloadURL">
															<html:rewrite page="/download/?uuid=${file.uuid}&preferDownload=true"/>
													</c:set>
													<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
														<bean:message key="label.download"/> 
													</a>
												</td>
				         			</c:forEach>
	         			</table>
					</td> 
				</tr>

				<html:hidden property="fileItem"/>
				<html:hidden property="offlineFile"/>				
				<html:hidden property="uuid"/>				
				
			</table>	  	
