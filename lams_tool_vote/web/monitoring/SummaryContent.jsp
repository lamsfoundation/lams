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


<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

		<c:if test="${(voteGeneralMonitoringDTO.userExceptionNoToolSessions == 'true')}"> 	
				<table align="center">
					<tr> 
						<td NOWRAP valign=top align=center> 
							<b>  <bean:message key="error.noLearnerActivity"/> </b>
						</td> 
					<tr>
				</table>
		</c:if>			


		<c:if test="${(voteGeneralMonitoringDTO.userExceptionNoToolSessions != 'true') }"> 	
			<c:if test="${voteGeneralMonitoringDTO.currentMonitoredToolSession != 'All'}"> 							
					<jsp:include page="/monitoring/IndividualSessionSummary.jsp" />					
			</c:if> 	    	  

			<c:if test="${voteGeneralMonitoringDTO.currentMonitoredToolSession == 'All'}"> 							
					<jsp:include page="/monitoring/AllSessionsSummary.jsp" />								
			</c:if> 	    	  
		</c:if>								
		
			<table class="forms">
					<tr>			
							<td valign=top align=left>
								<table align=center>
								
										<tr>			
											<td colspan=2 valign=top align=left>
												<b>  <bean:message key="label.reflection"/> <bean:message key="label.include.runOffline"/> </b> 
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
												<c:url value="/monitoring.do" var="openNotebook">
													<c:param name="method" value="openNotebook" />
													<c:param name="uid" value="${reflectionUid}" />
													<c:param name="userId" value="${userId}" />
													<c:param name="userName" value="${userName}" />
													<c:param name="sessionId" value="${sessionId}" />													
												</c:url>
				
												<html:link href="${fn:escapeXml(openNotebook)}" target="_blank">
													<bean:message key="label.view" />
												</html:link>

											 </td>
										</tr>	
									</c:forEach>		
								</table>  	
							 </td>
						</tr>	
				</table>		  	 								
		

