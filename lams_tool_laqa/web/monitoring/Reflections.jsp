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

				<c:if test="${notebookEntriesExist == 'true' }"> 			
					<table class="forms">
						
								<tr>			
									<td colspan=2 valign=top align=left>
										<b>  <bean:message key="label.reflection"/>  </b> 
									 </td>
								</tr>	
						
						
							<c:forEach var="currentDto" items="${reflectionsContainerDTO}">
					  	 		<c:set var="userName" scope="request" value="${currentDto.userName}"/>
					  	 		<c:set var="userId" scope="request" value="${currentDto.userId}"/>
					  	 		<c:set var="sessionId" scope="request" value="${currentDto.sessionId}"/>
					  	 		<c:set var="reflectionUid" scope="request" value="${currentDto.reflectionUid}"/>
								<tr>			
									<td valign=top align=left>
										 <c:out value="${userName}" escapeXml="false"/> 
									 </td>
	
									<td valign=top align=left>
										<c:url value="monitoring.do" var="openNotebook">
											<c:param name="dispatch" value="openNotebook" />
											<c:param name="uid" value="${reflectionUid}" />
											<c:param name="userId" value="${userId}" />
											<c:param name="userName" value="${userName}" />
											<c:param name="sessionId" value="${sessionId}" />													
											<c:param name="contentFolderID" value="${QaMonitoringForm.contentFolderID}" />																								
										</c:url>
		
										<html:link href="javascript:launchPopup('${fn:escapeXml(openNotebook)}')">
											<bean:message key="label.view" />
										</html:link>
	
									 </td>
								</tr>	
							</c:forEach>		
						</table>
				</c:if> 															
