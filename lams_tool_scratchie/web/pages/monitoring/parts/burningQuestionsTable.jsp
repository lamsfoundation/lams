<%@ include file="/common/taglibs.jsp"%>

<c:set var="burningQuestionsAvailable" value="false" />
<c:forEach var="burningQuestionItemDto" items="${burningQuestionItemDtos}">
	<c:if test="${not empty burningQuestionItemDto.burningQuestionDtos}">
		<c:set var="burningQuestionsAvailable" value="true" />
	</c:if>
</c:forEach>
		
<script>
	$(document).ready(function(){
		//handler for expand/collapse all button
		$("#toggle-burning-questions-button").click(function() {
			var isExpanded = eval($(this).data("expanded"));
				
			//fire the actual buttons so burning questions can be closed/expanded
			if (isExpanded) {
				$("#burning-questions-accordion .accordion-button:not('.collapsed')").each(function() {
					this.click();
				});
								
			} else {
				$("#burning-questions-accordion .accordion-button").each(function() {
					this.click();
				});					
			}

			//change button label
			var newButtonLabel = isExpanded ? "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.expand.all' /></spring:escapeBody>" : "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.collapse.all' /></spring:escapeBody>";
			$(".hidden-xs", $(this)).text(newButtonLabel);

			//change button icon
			if (isExpanded) {
				$(".fa", $(this)).removeClass("fa-minus-circle").addClass("fa-plus-circle");
			} else {
				$(".fa", $(this)).removeClass("fa-plus-circle").addClass("fa-minus-circle");
			}

			//change button's data-expanded attribute
			$(this).data("expanded", !isExpanded);
		});

		$('.options-show-link').click(function(e){
			// so browser does not go to the top of page
			e.preventDefault();
			
			// hide link, show answers
			let link = $(this),
				itemUid = link.closest('.collapse').data('itemUid');
			link.remove();
			$('#options-' + itemUid).show();
		});

		<c:if test="${discussionSentimentEnabled}">
			$.ajax({
				url : '<lams:LAMSURL />learning/discussionSentiment/checkMonitor.do',
				data : {
					toolContentId : traToolContentId
				},
				dataType : 'json',
				success : function(result){
					result.forEach(function(discussion){
						startDiscussionSentiment(discussion.toolQuestionUid, discussion.burningQuestionUid, false);
					});
				}
			});
		</c:if>
	});
</script>
	
<div class="row">
	<div class="col-10 offset-1 pb-2">
		<c:if test="${burningQuestionsAvailable}">
			<div class="float-end">
				<button type="button" id="toggle-burning-questions-button" class="btn btn-secondary" data-expanded="false">
					<i class="fa fa-plus-circle"></i>
					<span class="hidden-xs">
						<fmt:message key='label.expand.all' />
					</span>
				</button>
			</div>
		</c:if>
		<h3>
			<fmt:message key="label.burning.questions"/>
		</h3>
	</div>
</div>

<div class="row">
	<div class="col-10 offset-1">
		<c:choose>
			<c:when test="${burningQuestionsAvailable}">
				<div class="accordion" id="burning-questions-accordion"> 	
					<c:forEach var="burningQuestionItemDto" items="${burningQuestionItemDtos}" varStatus="i">
						<c:if test="${not empty burningQuestionItemDto.burningQuestionDtos}">
							<c:set var="item" value="${burningQuestionItemDto.scratchieItem}"/>
							<%-- toolQuestionUid, i.e. item UID, is required for discussion token.
								 It is missing for general burning questions as they are not bound to any question.
								 In this case we use first question's UID, as burning question UID will uniquely identify the discussion anyway
							 --%>
							<c:set var="generalBurningQuestionDiscussionItemUid"
								   value="${empty generalBurningQuestionDiscussionItemUid and not empty item.uid ? item.uid : generalBurningQuestionDiscussionItemUid}" />
							<c:set var="discussionItemUid" value="${empty item.uid ? generalBurningQuestionDiscussionItemUid : item.uid}" />
				 			<div class="accordion-item" tabindex="0">
						    	<h4 class="accordion-header" id="heading${item.uid}">
						    	  	<button class="accordion-button collapsed" type="button"
							    			data-bs-toggle="collapse" data-bs-target="#collapse${item.uid}"
							    			aria-expanded="false">
							    		<!-- Don't display number prior to general burning question -->
										<c:choose>
											<c:when test="${empty item.uid}">
												<c:out value="${item.qbQuestion.name}" escapeXml="false"/>
											</c:when>
											<c:otherwise>
												Q${i.index+1}) 
												<c:if test="${not hideTitles}">
													<c:out value="${item.qbQuestion.name}" escapeXml="false"/>
												</c:if> 
											</c:otherwise>
										</c:choose>
							    	</button>
							    </h4>
							    <div id="collapse${item.uid}" data-item-uid="${item.uid}"
							    	 class="accordion-collapse collapse" aria-labelledby="heading${item.uid}">	
									<div class="accordion-body">
									
										<span class="burning-question-description">
											<c:out value="${item.qbQuestion.description}" escapeXml="false"/><br /> 
										</span>
										
										<c:if test="${not empty burningQuestionItemDto.scratchieItem.uid}">
											<a id="options-show-${item.uid}" href="#" class="options-show-link">
												<fmt:message key='label.options.show' />
											</a>
											<div  id="options-${item.uid}" class="table-responsive" style="display: none">
												<table class="table table-hover">
													<c:forEach var="answer" items="${item.qbQuestion.qbOptions}" varStatus="j">
														<c:set var="cssClass"><c:if test='${answer.correct}'>bg-success</c:if></c:set>
														<tr>
															<td width="5px" class="${cssClass} align-middle">
																${ALPHABET[j.index]}.
															</td>
															<td class="${cssClass} align-middle">
																<c:out value="${answer.name}" escapeXml="false"/> 
															</td>
														</tr>
													</c:forEach>
												</table>
											</div>
										</c:if>
										
										<div class="table-responsive mt-3">
											<table class="table table-bordered">
												<tbody>
													<c:forEach var="burningQuestionDto" items="${burningQuestionItemDto.burningQuestionDtos}">
														<tr>
															<td class="text-nowrap align-middle fw-bold">
																<c:out value="${burningQuestionDto.sessionName}" escapeXml="false"/>
															</td>
															<td>
																${burningQuestionDto.escapedBurningQuestion}
															</td>
															<td class="text-nowrap burning-questions-likes align-middle">
																<span class="badge rounded-pill text-bg-secondary">${burningQuestionDto.likeCount}</span>
																<i class="fa fa-thumbs-up text-primary ms-2"></i>
															</td>
															<c:if test="${discussionSentimentEnabled and not empty discussionItemUid}">
																<td class="discussion-sentiment-start-button-cell align-middle">
																	<div id="discussion-sentiment-start-button-${discussionItemUid}-${burningQuestionDto.burningQuestion.uid}"
																	     class="btn btn-sm btn-secondary discussion-sentiment-start-button"
																	     onClick="javascript:startDiscussionSentiment(${discussionItemUid}, ${burningQuestionDto.burningQuestion.uid}, true)">
																		<i class="fa fa-comments me-1"></i><fmt:message key="label.monitoring.discussion.start"/>
																	</div>
																</td>
															</c:if>
														</tr>
														<c:if test="${discussionSentimentEnabled and not empty discussionItemUid}">
															<tr id="discussion-sentiment-chart-row-${discussionItemUid}-${burningQuestionDto.burningQuestion.uid}"
															    class="discussion-sentiment-chart-row">
																<td colspan="3">
																	<div id="discussion-sentiment-chart-panel-container-${discussionItemUid}-${burningQuestionDto.burningQuestion.uid}"></div>
																</td>
															</tr>
														</c:if>
													</c:forEach>
												</tbody>
											</table>
										</div>
									</div>
								</div>
								
							</div>
						</c:if>
					</c:forEach>    
				</div>
			</c:when>
			<c:otherwise>
				<div class="card">
					<div class="card-body text-center">
						<fmt:message key="label.burning.questions.none"/>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		
	</div>
</div>
