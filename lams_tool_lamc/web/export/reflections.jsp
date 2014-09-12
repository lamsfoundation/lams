<%@ include file="/common/taglibs.jsp"%>

<table  class="forms">
		
	<tr>			
		<td valign=top class="align-left">
			&nbsp
		</td>
	</tr>
		
	<tr>			
		<td valign=top class="align-left">
			<table class="align-left">
						
				<tr>			
					<td colspan=3 valign=top align=center>
						<b>  <fmt:message key="label.reflection"/>  </b> 
					</td>
				</tr>
						
				<c:forEach var="currentDto" items="${reflectionsContainerDTO}">
					<c:set var="userName" scope="request" value="${currentDto.userName}"/>
					<c:set var="userId" scope="request" value="${currentDto.userId}"/>
					<c:set var="sessionId" scope="request" value="${currentDto.sessionId}"/>
					<c:set var="reflectionUid" scope="request" value="${currentDto.reflectionUid}"/>
					<c:set var="entry" scope="request" value="${currentDto.entry}"/>
					
					<tr>			
						<td valign=top class="align-left">
							<b> <fmt:message key="label.user"/>:</b>
						</td>
								
						<td valign=top class="align-left">
							<c:out value="${userName}" escapeXml="false"/> 
						</td>

						<td valign=top class="align-left">
							<c:out value="${entry}" escapeXml="false"/> 
						</td>
					</tr>	
				</c:forEach>		
			</table>  	
		</td>
	</tr>	
</table>	