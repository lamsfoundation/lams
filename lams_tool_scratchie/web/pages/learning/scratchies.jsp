<%@ include file="/common/taglibs.jsp"%>

<c:if test="${isQuestionEtherpadEnabled and not empty allGroupUsers}">
	<%-- Prepare same content for each question Etherpad. Each group participant's first and last name --%>
	<c:set var="questionEtherpadContent">
		<c:forEach items="${allGroupUsers}" var="user">
			<c:out value="${user.firstName}" />&nbsp;<c:out value="${user.lastName}" />:
			<br />
			<br />
			<br />
		</c:forEach>
	</c:set>
</c:if>

<c:forEach var="item" items="${sessionMap.itemList}" varStatus="questionNumber">
	<div class="card lcard lcard-no-borders shadow my-4" id="question${questionNumber.count}" aria-label="question">
		
		<div class="card-header lcard-header lcard-header-button-border lead" id="questionTitle${questionNumber.count}">
	        ${questionNumber.count})&nbsp;
	        <c:if test="${not sessionMap.hideTitles}">
	        	<c:out value="${item.qbQuestion.name}" escapeXml="true" />
	       </c:if>
		</div>
		
		<div class="card-body">
			<div class="question-description">
				<c:out value="${item.qbQuestion.description}" escapeXml="false" />
			</div>
		
			<c:if test="${(sessionMap.userFinished && (mode == 'teacher')) || showResults}">
				<div class="panel-footer item-score">
					<fmt:message key="label.score" />&nbsp;${item.mark}
				</div>
			</c:if>
	
			<c:choose>
			<c:when test="${item.qbQuestion.type == 1 or item.qbQuestion.type == 8}">
				<div class="table div-hover scratches">
					<c:forEach var="optionDto" items="${item.optionDtos}" varStatus="status">
						<div id="tr${optionDto.qbOptionUid}" class="row mx-2">
							<div class="scartchie-image-col">
								<c:choose>
									<c:when test="${optionDto.scratched && optionDto.correct}">
										<img src="<lams:WebAppURL/>includes/images/scratchie-correct.png" class="scartchie-image" alt="<fmt:message key='label.correct'/>"
											 id="image-${item.uid}-${optionDto.qbOptionUid}">
									</c:when>
									<c:when test="${optionDto.scratched && !optionDto.correct}">
										<img src="<lams:WebAppURL/>includes/images/scratchie-wrong.png" class="scartchie-image" alt="<fmt:message key='label.incorrect'/>"
											 id="image-${item.uid}-${optionDto.qbOptionUid}">
									</c:when>
									<c:when test="${sessionMap.userFinished || item.unraveled || !isUserLeader || (mode == 'teacher') || showResults}">
										<img src="<lams:WebAppURL/>includes/images/answer-${status.index + 1}.png" class="scartchie-image" alt="<fmt:message key='label.monitoring.summary.answer'/> ${status.index + 1}"
											 id="image-${item.uid}-${optionDto.qbOptionUid}">
									</c:when>
									<c:otherwise>
										<a href="#" role="button" 
											id="imageLink-${item.uid}-${optionDto.qbOptionUid}" class="scratchie-link"
											data-item-uid="${item.uid}"
											data-option-uid="${optionDto.qbOptionUid}"
											aria-labelledby="answer-description-${optionDto.qbOptionUid}"
											aria-controls="image-${item.uid}-${optionDto.qbOptionUid}"
											<c:choose>
										    	<c:when test="${scratchie.revealOnDoubleClick}">
										    		onDblClick=
										    	</c:when>
										    	<c:otherwise>
										    		onClick=
										    	</c:otherwise>
										    </c:choose>
										    <%-- call this function either on click or double click --%>
										    "scratchMcq(${item.uid}, ${optionDto.qbOptionUid}); return false;"> 
												<img aria-live="polite"
													src="<lams:WebAppURL/>includes/images/answer-${status.index + 1}.png"
													alt="<fmt:message key='label.monitoring.summary.answer'/> ${status.index + 1}"
													class="scartchie-image"
													id="image-${item.uid}-${optionDto.qbOptionUid}" />
										</a>
									</c:otherwise>
								</c:choose> 
								
								<c:if test="${(showResults || mode == 'teacher') && (optionDto.attemptOrder != -1)}">
									<div style="text-align: center; margin-top: 2px;">
										<fmt:message key="label.choice.number">
											<fmt:param>${optionDto.attemptOrder}</fmt:param>
										</fmt:message>
									</div>
								</c:if>
							</div>
			
							<div class="col <c:if test="${fn:length(optionDto.confidenceLevelDtos) > 0}">answer-with-confidence-level-portrait</c:if>">
								<div id="answer-description-${optionDto.qbOptionUid}">
									<span class="visually-hidden">
										<fmt:message key='label.monitoring.summary.answer'/> ${status.index + 1}: 
									</span>
									<c:out value="${optionDto.answer}" escapeXml="false" />
								</div>
								
								<c:if test="${fn:length(optionDto.confidenceLevelDtos) > 0}">
									<hr class="hr-confidence-level" />
								
									<div>
										<c:set var="confidenceLevelsAnonymous" value="${mode != 'teacher' && scratchie.confidenceLevelsAnonymous}" />
										<c:forEach var="confidenceLevelDto" items="${optionDto.confidenceLevelDtos}" varStatus="status">
											<%@ include file="parts/confidenceLevelPortrait.jsp"%>
										</c:forEach>
									</div>
								</c:if>
							</div>
						</div>
					</c:forEach>
				</div>
			</c:when>
			
			<c:otherwise>
				<div id="scratches-${item.uid}" class="scratches table">
					<c:forEach var="optionDto" items="${item.optionDtos}" varStatus="status">
						<div id="tr-${item.uid}-${optionDto.answerHash}" class="row mx-2">
						
							<div class="scartchie-image-col">
								<c:choose>
									<c:when test="${optionDto.scratched && optionDto.correct}">
										<img src="<lams:WebAppURL/>includes/images/scratchie-correct.png" class="scartchie-image" alt="<fmt:message key='label.correct'/>"
											 id="image-${item.uid}-${optionDto.answerHash}">
									</c:when>
									<c:when test="${optionDto.scratched && !optionDto.correct}">
										<img src="<lams:WebAppURL/>includes/images/scratchie-wrong.png" class="scartchie-image" alt="<fmt:message key='label.incorrect'/>"
											 id="image-${item.uid}-${optionDto.answerHash}">
									</c:when>
									<c:otherwise>
										<img src="<lams:WebAppURL/>includes/images/answer-${status.index + 1}.png" class="scartchie-image" alt="<fmt:message key='label.monitoring.summary.answer'/> ${status.index + 1}"
											id="image-${item.uid}-${optionDto.answerHash}" 
											<c:if test="${!sessionMap.userFinished && !item.unraveled && (mode != 'teacher') && !showResults}">style="visibility: hidden;"</c:if>
											/>
									</c:otherwise>
								</c:choose>
								
								<c:if test="${(showResults || mode == 'teacher') && (optionDto.attemptOrder != -1)}">
									<div style="text-align: center; margin-top: 2px;">
										<fmt:message key="label.choice.number">
											<fmt:param>${optionDto.attemptOrder}</fmt:param>
										</fmt:message>
									</div>
								</c:if>
							</div>
			
							<div class="col answer-with-confidence-level-portrait" style="vertical-align: top;">
								<div>
									<c:out value="${optionDto.answer}" escapeXml="true" />
								</div>
								
								<c:if test="${fn:length(optionDto.confidenceLevelDtos) > 0}">
									<hr class="hr-confidence-level" />
									
									<div>
										<c:forEach var="confidenceLevelDto" items="${optionDto.confidenceLevelDtos}" varStatus="status">
											<%@ include file="parts/confidenceLevelPortrait.jsp"%>
										</c:forEach>
									</div>
								</c:if>
							</div>
						</div>
					</c:forEach>
				</div>
				
				<c:if test="${!sessionMap.userFinished && !item.unraveled && isUserLeader && (mode != 'teacher') && !showResults}">
					<div id="type-your-answer-${item.uid}" style="padding: 0 0 15px 100px; margin-top:-10px;">
						<div id="type-your-answer-descr-${item.uid}">
							<fmt:message key="label.type.your.group.answer" />
						</div>
									
						<div>
							<input type="text" id="input-${item.uid}" size="70" class="form-control input-sm submit-user-answer-input
								<c:if test="${item.qbQuestion.autocompleteEnabled}">ui-autocomplete-input</c:if>"
								style="display: inline-block; width: 70%;" data-item-uid="${item.uid}" aria-labelledby="type-your-answer-descr-${item.uid}"/>
									
							<button class="btn btn-secondary btn-sm submit-user-answer" data-item-uid="${item.uid}" >
								<fmt:message key="label.button.submit" />
							</button>
						</div>		
					</div>
				</c:if>
			</c:otherwise>
			</c:choose>
				
			<%--Display Etherpad for each question --%>
			<c:if test="${isQuestionEtherpadEnabled}">
				<div class="question-etherpad-container mb-3">
					<a data-bs-toggle="collapse" data-bs-target="#question-etherpad-${item.uid}" href="#" class="collapsed" role="button" >
						<span class="if-collapsed"><i class="fa fa-xs fa-regular fa-square-plus" aria-hidden="true"></i></span>
		  				<span class="if-not-collapsed"><i class="fa fa-xs fa-regular fa-square-minus" aria-hidden="true"></i></span>
						<fmt:message key="label.etherpad.discussion" />
					</a>
					
					<div id="question-etherpad-${item.uid}" class="question-etherpad-collapse collapse">
						<div class="card question-etherpad">
							<lams:Etherpad groupId="etherpad-scratchie-${toolSessionID}-question-${item.uid}" 
							   showControls="${mode eq 'teacher'}" showChat="false" heightAutoGrow="true" showOnDemand="true"
							>${questionEtherpadContent}</lams:Etherpad>
						</div>
					</div>
				</div>
			</c:if>
			
			<%-- show burning questions --%>
			<c:if test="${!showResults && scratchie.burningQuestionsEnabled && (isUserLeader || (mode == 'teacher'))}">
				<div class="burning-question-container mb-3">
					<a id="bq${item.uid}" data-bs-toggle="collapse" data-bs-target="#burning-question-item${item.uid}" href="#" role="button"
							<c:if test="${empty item.burningQuestion}">class="collapsed"</c:if>>
						<span class="if-collapsed"><i class="fa fa-xs fa-regular fa-square-plus" aria-hidden="true"></i></span>
		  				<span class="if-not-collapsed"><i class="fa fa-xs fa-regular fa-square-minus" aria-hidden="true"></i></span>
						<fmt:message key="label.burning.question" />
					</a>
					
					<div id="burning-question-item${item.uid}" class="collapse <c:if test="${not empty item.burningQuestion}">show</c:if> p-2">
						<textarea rows="5" name="burningQuestion${item.uid}" class="form-control" aria-labelledby="questionTitle${questionNumber.count} bq${item.uid}" 
							<c:if test="${mode == 'teacher'}">disabled="disabled"</c:if>
						>${item.burningQuestion}</textarea>
					</div>
				</div>
			</c:if>
			
			<%-- show link to burning questions (only for results page) --%>
			<c:if test="${showResults && mode != 'teacher' && scratchie.burningQuestionsEnabled}">
				<div class="scroll-down-to-bq">
					<a href='#gbox_burningQuestions${item.uid}' data-item-uid="${item.uid}" class='float-end' title="<fmt:message key="label.scroll.down.to.burning.question"/>">
						<i class="fa fa-xs fa-solid fa-angles-down me-1"></i>
					</a>
				</div>
			</c:if>
		</div>
	</div>
</c:forEach>
