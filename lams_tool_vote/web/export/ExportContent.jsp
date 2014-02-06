<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>
<br>
					 			
<c:if test="${(exportPortfolioDto.portfolioExportMode == 'learner')}">
	<h2>  
		<fmt:message key="label.individual.learnerVotes"/>  
	</h2>
</c:if>											
	
<c:if test="${(exportPortfolioDto.portfolioExportMode != 'learner')}">
	<h2>  
		<fmt:message key="label.all.learnerVotes"/>  
	</h2>
</c:if>											

<c:forEach var="currentDto" items="${exportPortfolioDto.listMonitoredAnswersContainerDto}">
	<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
	
	<h4>
		<fmt:message key="label.nomination"/>: <c:out value="${currentDto.question}" escapeXml="false"/>
	</h4>
									
	<table class="alternative-color">
		<tr> 
			<th style="width: 30%;"> <fmt:message key="label.user"/>  </th>  
			<th> <fmt:message key="label.attemptTime"/> </th>
		</tr>
									  			
		<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
			<c:forEach var="sData" items="${questionAttemptData.value}">
				<c:set var="userData" scope="request" value="${sData.value}"/>
				<c:set var="responseUid" scope="request" value="${userData.uid}"/>
			
				<c:if test="${currentQuestionId == userData.questionUid}">
					<tr> 
						<td>   <c:out value="${userData.userName}"/>   </td>  
						<td>  <lams:Date value="${userData.attemptTime}"/>  </td>
					</tr>		
				</c:if>														  					 
			</c:forEach>		  	
		</c:forEach>		  	
	</table>
</c:forEach>		  
									
<c:forEach var="currentDto" items="${exportPortfolioDto.listUserEntries}">
	<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
		
	<h4>
		<fmt:message key="label.nomination"/>: <c:out value="${currentDto.question}" escapeXml="false"/>
	</h4>	
		
	<table class="alternative-color">
		<tr> 
			<th style="width: 30%;">
				<fmt:message key="label.user"/>
			</th>  
			<th>
				<fmt:message key="label.attemptTime"/>
			</th>
		</tr>				 
			
		<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
			<c:set var="userData" scope="request" value="${questionAttemptData.value}"/>
			<tr> 
				<td>
					<c:out value="${userData.userName}"/> 
				</td>  
				<td >
					<lams:Date value="${userData.attemptTime}"/>
				</td>																		 
			</tr>		
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
