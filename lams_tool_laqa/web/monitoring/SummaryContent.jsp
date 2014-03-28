<%@ include file="/common/taglibs.jsp"%>

<div class="instructions">
	${content.title}
</div>

<div class="instructions">
	${content.instructions}
</div>

<c:if test="${empty listAllGroupsDTO}">
	<div align="center">
		<b> <fmt:message key="error.noLearnerActivity"/> </b>
	</div>
</c:if>

<c:if test="${content.useSelectLeaderToolOuput && not empty listAllGroupsDTO}">
	<div class="info">
		<fmt:message key="label.info.use.select.leader.outputs" />
	</div>
</c:if>

<c:forEach var="groupDto" items="${listAllGroupsDTO}">
	<c:set var="sessionId" scope="request" value="${groupDto.sessionId}"/>
	<c:set var="sessionName" scope="request" value="${groupDto.sessionName}"/>
	<c:set var="groupData" scope="request" value="${groupDto.groupData}"/>
			  	 		
	<c:if test="${isGroupedActivity}">
		<h1 class="group-name-title">
			<fmt:message key="group.label" />: <c:out value="${sessionName}"/>
		</h1>
	</c:if>

	<c:forEach var="currentDto" items="${groupData}">
		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
		
		<div class="tablesorter-container">
		<div class="question-title">
			<c:out value="${currentDto.question}" escapeXml="false"/>
		</div>	
		
		<table class="tablesorter">	
		
			<thead>
				<tr>
					<th title="<fmt:message key='label.sort.by.answer'/>" >
						<fmt:message key="label.learning.answer" />
					</th>
					
					<c:if test="${content.allowRateAnswers}">
						<th title="<fmt:message key='label.sort.by.rating'/>" width="19%">
							<fmt:message key="label.learning.rating" />
						</th>
					</c:if>
				</tr>
			</thead>
			
			<tbody>

		  		<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
					<c:forEach var="sData" items="${questionAttemptData.value}">
						<c:set var="userData" scope="request" value="${sData.value}"/>
						<c:set var="responseUid" scope="request" value="${userData.uid}"/>
						<c:set var="userSessionId" scope="request" value="${userData.sessionId}"/>
								  	 		
	  	 				<c:if test="${(sessionId == userSessionId) && (currentQuestionId == userData.questionUid)}"> 											  	 		

							<tr> 
								<td id="td-response-${responseUid}" valign=top <c:if test="${userData.visible != 'true' }">class="hidden"</c:if>>  
									<c:out value="${userData.userName}"/> <lams:Date value="${userData.attemptTime}"/>
	
				   					<div class="float-right" valign=top>
										<a href="#nogo" title="<fmt:message key='label.tooltip.edit'/>" class="image-link"
												onclick="javascript:editResponse(${userData.uid});">
									    	<img src="<c:out value="${tool}"/>images/edit.gif" border="0">
										</a>
				   						
				   						<c:choose>
				   							<c:when test="${userData.visible == 'true'}">
												<a href='#nogo' title="<fmt:message key='label.hide'/>" class="image-link"
														onclick="changeResponseVisibility(this, ${responseUid}, true);">						                                             
													<img src="<c:out value="${tool}"/>images/display-eye.png" border="0">
												</a>				   							
				   							</c:when>
				   							<c:otherwise>
												<a href='#nogo' title="<fmt:message key='label.show'/>" 
														onclick="changeResponseVisibility(this, ${responseUid}, false);">						                                             
													<img src="<c:out value="${tool}"/>images/hidden-eye.png" border="0">
												 </a>				   							
				   							</c:otherwise>
				   						</c:choose>																
									</div>
									 
								 	<br>
										
									<span id="response-${responseUid}"><c:out value="${userData.response}" escapeXml="false"/></span>
								</td>
								
								<c:if test="${content.allowRateAnswers}">
									<td>
										<jsp:include page="../learning/parts/ratingStarsDisabled.jsp" />
									</td>
								</c:if>								
							</tr>
						</c:if>										  					 									  													  														

	 				</c:forEach>		  	
				</c:forEach>
			</tbody>
		</table>
		
		<!-- pager -->
		<div class="pager">
			<form>
				<img class="tablesorter-first"/>
				<img class="tablesorter-prev"/>
				<span class="pagedisplay"></span> <!-- this can be any element, including an input -->
				<img class="tablesorter-next"/>
				<img class="tablesorter-last"/>
				<select class="pagesize">
					<option selected="selected" value="10">10</option>
					<option value="20">20</option>
					<option value="30">30</option>
					<option value="40">40</option>
					<option value="50">50</option>
					<option value="100">100</option>
				</select>
			</form>
		</div>
		</div>
		
	</c:forEach>
			  	
</c:forEach>
		
<c:if test="${content.reflect && not empty reflectionsContainerDTO}"> 							
	<jsp:include page="/monitoring/Reflections.jsp" />
</c:if>

<%@include file="AdvanceOptions.jsp"%>

<%@include file="dateRestriction.jsp"%>