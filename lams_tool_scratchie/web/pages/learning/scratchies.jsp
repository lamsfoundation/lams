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
	<div class="card lcard mt-4" id="question${questionNumber.count}">
		<div class="card-header" id="questionTitle${questionNumber.count}">
	        ${questionNumber.count})&nbsp;
	        <c:if test="${not sessionMap.hideTitles}">
	        	<c:out value="${item.qbQuestion.name}" escapeXml="true" />
	       </c:if>
		
			<c:if test="${(sessionMap.userFinished && (mode == 'teacher')) || showResults}">
				<div class="badge bg-success float-end item-score p-2 px-4">
					<fmt:message key="label.score" />&nbsp;${item.mark}
				</div>
			</c:if>
		</div>
		
		<div class="card-body">
			<div class="question-description">
				<c:out value="${item.qbQuestion.description}" escapeXml="false" />
			</div>
	
			<c:choose>
			<%-- 1:TYPE_MULTIPLE_CHOICE, 8:TYPE_MARK_HEDGING --%>
			<c:when test="${item.qbQuestion.type == 1 or item.qbQuestion.type == 8}">
				<div class="table div-hover scratches mt-4">
					<c:forEach var="optionDto" items="${item.optionDtos}" varStatus="status">
						<c:set var="svgId" value="${item.uid}-${optionDto.qbOptionUid}"/>
						
						<div id="tr${optionDto.qbOptionUid}" class="row">
							<div class="scartchie-image-col">
								<c:choose>
									<c:when test="${optionDto.scratched && optionDto.correct}">
										<jsp:include page='parts/scratchieSvg.jsp'>
											<jsp:param name="type" value="correct"/>
											<jsp:param name="svgId" value="${svgId}"/>
										</jsp:include>
									</c:when>
									
									<c:when test="${optionDto.scratched && !optionDto.correct}">
										<jsp:include page='parts/scratchieSvg.jsp'>
											<jsp:param name="type" value="incorrect"/>
											<jsp:param name="svgId" value="${svgId}"/>
										</jsp:include>
									</c:when>
									
									<c:when test="${sessionMap.userFinished || item.unraveled || !isUserLeader || (mode == 'teacher') || showResults}">
										<jsp:include page='parts/scratchieSvg.jsp'>
											<jsp:param name="type" value="letter"/>
											<jsp:param name="svgId" value="${svgId}"/>
											<jsp:param name="letter" value="${status.index}"/>
										</jsp:include>
									</c:when>
									
									<c:otherwise>
										<a href="#" role="button"
												id="imageLink-${svgId}" class="scratchie-link"
												data-item-uid="${item.uid}"
												data-option-uid="${optionDto.qbOptionUid}"
												aria-labelledby="answer-description-${optionDto.qbOptionUid}"
												aria-controls="svg-${svgId}"
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
											<jsp:include page='parts/scratchieSvg.jsp'>
												<jsp:param name="type" value="full"/>
												<jsp:param name="svgId" value="${svgId}"/>
												<jsp:param name="letter" value="${status.index}"/>
											    <jsp:param name="isHidden" value="false"/>
											</jsp:include>
										</a>
									</c:otherwise>
								</c:choose> 
								
								<c:if test="${(showResults || mode == 'teacher') && (optionDto.attemptOrder != -1)}">
									<div class="text-center badge text-bg-success bg-opacity-75 m-1 mx-2 px-3">
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
			
			<%-- 3:TYPE_VERY_SHORT_ANSWERS --%>
			<c:otherwise>
				<div id="vsa-${item.uid}" class="div-hover scratches my-4">
					<c:forEach var="optionDto" items="${item.optionDtos}" varStatus="status">
						<c:set var="svgId" value="${item.uid}-${optionDto.answerHash}"/>
						
						<div id="tr-${item.uid}-${optionDto.answerHash}" class="row">
							<div class="scartchie-image-col">
								<c:choose>
									<c:when test="${optionDto.scratched && optionDto.correct}">
										<jsp:include page='parts/scratchieSvg.jsp'>
											<jsp:param name="type" value="correct"/>
											<jsp:param name="svgId" value="${svgId}"/>
										</jsp:include>
									</c:when>
									
									<c:when test="${optionDto.scratched && !optionDto.correct}">
										<jsp:include page='parts/scratchieSvg.jsp'>
											<jsp:param name="type" value="incorrect"/>
											<jsp:param name="svgId" value="${svgId}"/>
										</jsp:include>
									</c:when>
									
									<c:otherwise>
										<jsp:include page='parts/scratchieSvg.jsp'>
											<jsp:param name="type" value="full"/>
											<jsp:param name="svgId" value="${svgId}"/>
											<jsp:param name="letter" value="${status.index}"/>
										    <jsp:param name="isHidden" value="${!sessionMap.userFinished && !item.unraveled && (mode != 'teacher') && !showResults}"/>
										</jsp:include>
									</c:otherwise>
								</c:choose>
								
								<c:if test="${(showResults || mode == 'teacher') && (optionDto.attemptOrder != -1)}">
									<div class="text-center badge text-bg-success bg-opacity-75 m-1 mx-2 px-3">
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
							<input type="text" id="input-${item.uid}" size="70" class="form-control form-control-sm submit-user-answer-input
								<c:if test="${item.qbQuestion.autocompleteEnabled}">ui-autocomplete-input</c:if>"
								style="display: inline-block; width: 70%;" data-item-uid="${item.uid}" aria-labelledby="type-your-answer-descr-${item.uid}"/>
									
							<button type="button" class="btn btn-secondary btn-sm submit-user-answer ms-1" data-item-uid="${item.uid}" >
								<i class="fa-solid fa-circle-play me-1"></i>
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
					<button type="button" data-bs-toggle="collapse" data-bs-target="#question-etherpad-${item.uid}" class="collapsed">
						<span class="if-collapsed"><i class="fa fa-xs fa-regular fa-square-plus" aria-hidden="true"></i></span>
		  				<span class="if-not-collapsed"><i class="fa fa-xs fa-regular fa-square-minus" aria-hidden="true"></i></span>
						<fmt:message key="label.etherpad.discussion" />
					</button>
					
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
				<div class="burning-question-container mb-2 mt-4">
					<button type="button" id="bq${item.uid}" data-bs-toggle="collapse" data-bs-target="#burning-question-item${item.uid}"
							class="btn btn-light card-subheader <c:if test="${empty item.burningQuestion}">collapsed</c:if>">
						<span class="if-collapsed"><i class="fa fa-xs fa-regular fa-square-plus" aria-hidden="true"></i></span>
		  				<span class="if-not-collapsed"><i class="fa fa-xs fa-regular fa-square-minus" aria-hidden="true"></i></span>
						<fmt:message key="label.burning.question" />
					</button>
					
					<div id="burning-question-item${item.uid}" class="collapse <c:if test="${not empty item.burningQuestion}">show</c:if> pt-3">
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
