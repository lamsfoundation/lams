<%@ include file="/common/taglibs.jsp"%>

				<c:if test="${notebookEntriesExist == 'true' }"> 			
					<table class="alternative-color">
						
								<tr>			
									<th colspan=2 class="align-left">
										<b>  <fmt:message key="label.reflection"/>  </b> 
									 </th>
								</tr>	
						
						
							<c:forEach var="currentDto" items="${reflectionsContainerDTO}">
					  	 		<c:set var="userName" scope="request" value="${currentDto.userName}"/>
					  	 		<c:set var="userId" scope="request" value="${currentDto.userId}"/>
					  	 		<c:set var="sessionId" scope="request" value="${currentDto.sessionId}"/>
					  	 		<c:set var="reflectionUid" scope="request" value="${currentDto.reflectionUid}"/>
								<tr>			
									<td valign=top class="align-left">
										 <c:out value="${userName}" escapeXml="false"/> 
									 </td>
	
									<td valign=top class="align-right">
										<c:url value="monitoring.do" var="openNotebook">
											<c:param name="dispatch" value="openNotebook" />
											<c:param name="uid" value="${reflectionUid}" />
											<c:param name="userId" value="${userId}" />
											<c:param name="userName" value="${userName}" />
											<c:param name="sessionId" value="${sessionId}" />													
											<c:param name="contentFolderID" value="${McMonitoringForm.contentFolderID}" />																								
										</c:url>
		
										<html:link href="javascript:launchPopup('${fn:escapeXml(openNotebook)}');" styleClass="button">
											<fmt:message key="label.view" />
										</html:link>
	
									 </td>
								</tr>	
							</c:forEach>		
						</table>
				</c:if> 															
