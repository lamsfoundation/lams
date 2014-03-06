<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>
<br>

<c:forEach var="sessionDto" items="${exportPortfolioDto.sessionDtos}">

	<c:if test="${isGroupedActivity && exportPortfolioDto.portfolioExportMode == 'teacher'}">
		<div >
			<B><fmt:message key="label.groupName" /></B> ${sessionDto.sessionName}
		</div>
	</c:if>
									
	<table class="alternative-color">
		<tr> 
			<th style="width: 35%;"> <fmt:message key="label.nomination"/>  </th>  
			<th style="width: 35%;"> <fmt:message key="label.user"/>  </th>  
			<th> <fmt:message key="label.attemptTime"/> </th>
		</tr>
									  			
		<c:forEach var="answer" items="${sessionDto.answers}">
			<c:if test="${fn:length(answer.questionAttempts) == 0}">
				<tr> 
					<td>   <c:out value="${answer.question}"/>   </td>  
					<td> - </td>  
					<td> - </td>
				</tr>			
			</c:if>
			
			<c:forEach var="questionAttemptMapItem" items="${answer.questionAttempts}">
				<c:set var="questionAttempt" scope="request" value="${questionAttemptMapItem.value}"/>
				<tr> 
					<td>   <c:out value="${answer.question}"/>   </td>  
					<td>   <c:out value="${questionAttempt.voteQueUsr.fullname}"/>   </td>  
					<td>  <lams:Date value="${questionAttempt.attemptTime}"/>  </td>
				</tr>
			</c:forEach>	  	
		</c:forEach>
		
		<c:forEach var="openVote" items="${sessionDto.openVotes}">
			
			<c:forEach var="questionAttempt" items="${openVote.questionAttempts}">
				<c:set var="userData" scope="request" value="${questionAttempt.value}"/>
				<tr> 
					<td>
						<c:out value="${openVote.question}"/> 
					</td>				
					<td>
						<c:out value="${userData.userName}"/> 
					</td>  
					<td >
						<lams:Date value="${userData.attemptTime}"/>
					</td>																		 
				</tr>		
			</c:forEach>
		</c:forEach>		  	
	</table>

</c:forEach>

<br>

<h2>
	<fmt:message key="label.reflection"/>
</h2>

<table class="alternative-color">

	<tr>
		<th style="width: 30%;">
			<fmt:message key="label.user"/>
		</th>
		<th >
			<fmt:message key="label.reflection"/>
		</th>
	</tr>
								
	<c:forEach var="currentDto" items="${reflectionsContainerDTO}">
		<c:set var="userName" scope="request" value="${currentDto.userName}"/>
		<c:set var="userId" scope="request" value="${currentDto.userId}"/>
		<c:set var="sessionId" scope="request" value="${currentDto.sessionId}"/>
		<c:set var="reflectionUid" scope="request" value="${currentDto.reflectionUid}"/>
		<c:set var="entry" scope="request" value="${currentDto.entry}"/>
					
		<tr>							
			<td>
				<c:out value="${userName}" escapeXml="false"/> 
			</td>

			<td>
				<c:out value="${entry}" escapeXml="false"/> 
			</td>
		</tr>	
	</c:forEach>		
</table>
