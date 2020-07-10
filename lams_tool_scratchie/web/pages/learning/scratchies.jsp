<%@ include file="/common/taglibs.jsp"%>

<c:if test="${isQuestionEtherpadEnabled and not empty allGroupUsers}">
	<%-- Prepare same content for each question Etherpad. Each group participant's first and last name --%>
	<c:set var="questionEtherpadContent">
		<c:forEach items="${allGroupUsers}" var="user"><c:out value="${user.firstName}" />&nbsp;<c:out value="${user.lastName}" />:<br />
	<br />
	<br /></c:forEach>
	</c:set>
</c:if>

<c:forEach var="item" items="${sessionMap.itemList}" varStatus="questionNumber">
	
	<div class="lead" id="questionTitle${questionNumber.count}">
        ${questionNumber.count})&nbsp;
        <c:if test="${not sessionMap.hideTitles}">
        	<c:out value="${item.qbQuestion.name}" escapeXml="true" />
       </c:if>
	</div>
	
	<div class="panel-body-sm">
		<c:out value="${item.qbQuestion.description}" escapeXml="false" />
	</div>
	
	<c:if test="${(sessionMap.userFinished && (mode == 'teacher')) || showResults}">
		<div class="panel-footer item-score">
			<fmt:message key="label.score" />&nbsp;${item.mark}
		</div>
	</c:if>

	<c:choose>
	<c:when test="${item.qbQuestion.type == 1}">
		<table class="table table-hover scratches">
			<c:forEach var="optionDto" items="${item.optionDtos}" varStatus="status">
				<tr id="tr${optionDto.qbOptionUid}">
					<td style="width: 40px;vertical-align: top;">
						<c:choose>
							<c:when test="${optionDto.scratched && optionDto.correct}">
								<img src="<lams:WebAppURL/>includes/images/scratchie-correct.png" class="scartchie-image"
									 id="image-${item.uid}-${optionDto.qbOptionUid}">
							</c:when>
							<c:when test="${optionDto.scratched && !optionDto.correct}">
								<img src="<lams:WebAppURL/>includes/images/scratchie-wrong.png" class="scartchie-image"
									 id="image-${item.uid}-${optionDto.qbOptionUid}">
							</c:when>
							<c:when test="${sessionMap.userFinished || item.unraveled || !isUserLeader || (mode == 'teacher') || showResults}">
								<img src="<lams:WebAppURL/>includes/images/answer-${status.index + 1}.png" class="scartchie-image"
									 id="image-${item.uid}-${optionDto.qbOptionUid}">
							</c:when>
							<c:otherwise>
								<a href="#nogo" onclick="scratchMcq(${item.uid}, ${optionDto.qbOptionUid}); return false;"
									id="imageLink-${item.uid}-${optionDto.qbOptionUid}"> <img
									src="<lams:WebAppURL/>includes/images/answer-${status.index + 1}.png" class="scartchie-image"
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
					</td>
	
					<td
						<c:if test="${fn:length(optionDto.confidenceLevelDtos) > 0}">class="answer-with-confidence-level-portrait"</c:if>
					>
						<div class="answer-description">
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
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:when>
	
	<c:otherwise>
		<table id="scratches-${item.uid}"  class="table table-hover scratches">
			<c:forEach var="optionDto" items="${item.optionDtos}" varStatus="status">
				<tr id="tr-${item.uid}-${optionDto.answerHash}">
				
					<td style="width: 40px; border: none;">
						<c:choose>
							<c:when test="${optionDto.scratched && optionDto.correct}">
								<img src="<lams:WebAppURL/>includes/images/scratchie-correct.png" class="scartchie-image"
									 id="image-${item.uid}-${optionDto.answerHash}">
							</c:when>
							<c:when test="${optionDto.scratched && !optionDto.correct}">
								<img src="<lams:WebAppURL/>includes/images/scratchie-wrong.png" class="scartchie-image"
									 id="image-${item.uid}-${optionDto.answerHash}">
							</c:when>
							<c:otherwise>
								<img src="<lams:WebAppURL/>includes/images/answer-${status.index + 1}.png" class="scartchie-image"
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
					</td>
	
					<td class="answer-with-confidence-level-portrait" style="vertical-align: top;">
						<div class="answer-description">
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
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<c:if test="${!sessionMap.userFinished && !item.unraveled && isUserLeader && (mode != 'teacher') && !showResults}">
			<div id="type-your-answer-${item.uid}" style="padding: 0 0 15px 100px; margin-top:-20px;">
				<div>
					<fmt:message key="label.type.your.group.answer" />
				</div>
							
				<div>
					<input type="text" id="input-${item.uid}" size="70" class="form-control input-sm voffset5 submit-user-answer-input
						<c:if test="${item.qbQuestion.autocompleteEnabled}">ui-autocomplete-input</c:if>"
						style="display: inline-block; width: 70%;" data-item-uid="${item.uid}"/>
							
					<button class="btn btn-default btn-sm submit-user-answer" data-item-uid="${item.uid}" >
						<fmt:message key="label.button.submit" />
					</button>
				</div>		
			</div>
		</c:if>
	</c:otherwise>
	</c:choose>
	
		
	<%--Display Etherpad for each question --%>
	<c:if test="${isQuestionEtherpadEnabled}">
		<div class="form-group question-etherpad-container">
			<a data-toggle="collapse" data-target="#question-etherpad-${item.uid}" href="#qe${item.uid}" class="collapsed">
				<span class="if-collapsed"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i></span>
  				<span class="if-not-collapsed"><i class="fa fa-xs fa-minus-square-o roffset5" aria-hidden="true"></i></span>
				<fmt:message key="label.etherpad.discussion" />
			</a>
			
			<div id="question-etherpad-${item.uid}" class="collapse">
				<div class="panel panel-default question-etherpad">
					<lams:Etherpad groupId="etherpad-scratchie-${toolSessionID}-question-${item.uid}" 
					   showControls="${mode eq 'teacher'}" showChat="false" heightAutoGrow="true"
					>${questionEtherpadContent}</lams:Etherpad>
				</div>
			</div>
		</div>
	</c:if>
	
	<%-- show burning questions --%>
	<c:if test="${!showResults && scratchie.burningQuestionsEnabled && (isUserLeader || (mode == 'teacher'))}">
		<div class="form-group burning-question-container">
			<!-- LDEV-4532: href is needed for the collapsing to work on an ipad but do not make it the same as the data-target here or the screen will jump around. -->
			<a data-toggle="collapse" data-target="#burning-question-item${item.uid}" href="#bqi${item.uid}"
					<c:if test="${empty item.burningQuestion}">class="collapsed"</c:if>>
				<span class="if-collapsed"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i></span>
  				<span class="if-not-collapsed"><i class="fa fa-xs fa-minus-square-o roffset5" aria-hidden="true"></i></span>
				<fmt:message key="label.burning.question" />
			</a>
			
			<div id="burning-question-item${item.uid}" class="collapse <c:if test="${not empty item.burningQuestion}">in</c:if>">
				<textarea rows="5" name="burningQuestion${item.uid}" class="form-control"
					<c:if test="${mode == 'teacher'}">disabled="disabled"</c:if>
				>${item.burningQuestion}</textarea>
			</div>
		</div>
	</c:if>
	
	<%-- show link to burning questions (only for results page) --%>
	<c:if test="${showResults && mode != 'teacher' && scratchie.burningQuestionsEnabled}">
		<div class="scroll-down-to-bq">
			<a href='#gbox_burningQuestions${item.uid}' data-item-uid="${item.uid}" class='pull-right' title="<fmt:message key="label.scroll.down.to.burning.question"/>">
				<i class="fa fa-xs fa-angle-double-down roffset5"></i>
			</a>
		</div>
	</c:if>

</c:forEach>
