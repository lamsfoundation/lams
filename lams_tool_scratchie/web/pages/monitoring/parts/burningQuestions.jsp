<%@ include file="/common/taglibs.jsp"%>

<style>

	.discussion-sentiment-start-button-cell {
		width: 50px;
		text-align: right;
	}	
	
	.discussion-sentiment-start-button .fa-comments {
		color: black !important;
	}
	
	.discussion-sentiment-chart-row {
		display: none;
	}
</style>
<script>
	$(document).ready(function(){
		//handler for expand/collapse all button
		$("#toggle-burning-questions-button").click(function() {
			var isExpanded = eval($(this).data("expanded"));
				
			//fire the actual buttons so burning questions can be closed/expanded
			if (isExpanded) {
				$(".burning-question-title:not('.collapsed')").each(function() {
					this.click();
				});
								
			} else {
				$(".burning-question-title.collapsed").each(function() {
					this.click();
				});					
			}

			//change button label
			var newButtonLabel = isExpanded ? "<fmt:message key='label.expand.all' />" : "<fmt:message key='label.collapse.all' />";
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
				itemUid = link.data('itemUid');
			link.remove();
			$('#options-' + itemUid).show();
		});

		//use jqeury toggle instead of bootstrap collapse 
		$(".burning-question-title").on('click', function () {
			var div =  $("#collapse-" + $(this).data("itemuid"));
			div.toggleClass("in");
			$(this).toggleClass("collapsed");
		});
		
		<c:if test="${isTbl and discussionSentimentEnabled}">
			$.ajax({
				url : '<lams:LAMSURL />learning/discussionSentiment/checkMonitor.do',
				data : {
					toolContentId : ${toolContentID}
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


	<c:if test="${isTbl and discussionSentimentEnabled}">
		function startDiscussionSentiment(toolQuestionUid, burningQuestionUid, markAsActive) {
			var idSuffix = toolQuestionUid + '-' + burningQuestionUid,
				chartRow = $('#discussion-sentiment-chart-row-' + idSuffix).css('display', 'table-row');
			
			$('#discussion-sentiment-chart-panel-container-' + idSuffix, chartRow).load(
				'<lams:LAMSURL />learning/discussionSentiment/startMonitor.do',
				{
					toolQuestionUid    : toolQuestionUid,
					burningQuestionUid : burningQuestionUid,
					markAsActive       : markAsActive
				},
				function(){
					$('#discussion-sentiment-start-button-' + idSuffix).closest('td').remove();
				}
			)
		}
	</c:if>
</script>

<!-- Header -->
<div class="row no-gutter">
	<div class="col-xs-12">
        <button type="button" id="toggle-burning-questions-button" class="btn btn-sm btn-default pull-right ${isTbl ? 'voffset20' : ''}" data-expanded="false">
           	<i class="fa fa-plus-circle"></i> 
           	<span class="hidden-xs">
           		<fmt:message key='label.expand.all' />
           	</span>
        </button>
        <c:if test="${isTbl}">
			<h3>
				<fmt:message key="label.burning.questions"/>
			</h3>
		</c:if>
	</div>
</div>
<!-- End header -->              

<!-- Tables -->
<div class="row no-gutter voffset10">
<div class="col-xs-12 col-md-12 col-lg-12">

	<c:forEach var="burningQuestionItemDto" items="${burningQuestionItemDtos}" varStatus="i">
		<c:set var="burningQsCount" value="${fn:length(burningQuestionItemDto.burningQuestionDtos)}"/>
		<c:if test="${burningQsCount > 0}">
			<c:set var="item" value="${burningQuestionItemDto.scratchieItem}"/>
			<%-- toolQuestionUid, i.e. item UID, is required for discussion token.
				 It is missing for general burning questions as they are not bound to any question.
				 In this case we use first question's UID, as burning question UID will uniquely identify the discussion anyway
			 --%>
			<c:set var="generalBurningQuestionDiscussionItemUid"
				   value="${empty generalBurningQuestionDiscussionItemUid and not empty item.uid ? item.uid : generalBurningQuestionDiscussionItemUid}" />
			<c:set var="discussionItemUid" value="${empty item.uid ? generalBurningQuestionDiscussionItemUid : item.uid}" />

			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title panel-collapse">
						<a data-toggle="collapse" data-itemuid="${item.uid}" class="collapsed burning-question-title">
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
						</a>
					</h4>
				</div>
			
				<div id="collapse-${item.uid}" class="panel-collapse collapse">
				<div class="panel-body">
				
					<span class="burning-question-description">
						<c:out value="${item.qbQuestion.description}" escapeXml="false"/><br /> 
					</span>
					
					<c:if test="${not empty burningQuestionItemDto.scratchieItem.uid}">
						<a id="options-show-${item.uid}" href="#" class="options-show-link" 
						   data-item-uid="${item.uid}">
							<fmt:message key='label.options.show' />
						</a>
						<div  id="options-${item.uid}" class="voffset10 table-responsive" style="display: none">
							<table class="table table-striped table-hover">
								<c:forEach var="answer" items="${item.qbQuestion.qbOptions}" varStatus="j">
									<c:set var="cssClass"><c:if test='${answer.correct}'>bg-success</c:if></c:set>
									<tr>
										<td width="5px" class="${cssClass}">
											${ALPHABET[j.index]}.
										</td>
										<td class="${cssClass}">
											<c:out value="${answer.name}" escapeXml="false"/> 
										</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</c:if>
					
					<div class="table-responsive voffset10">
						<table class="table table-striped table-bordered table-hover">
							<tbody>
								<c:forEach var="burningQuestionDto" items="${burningQuestionItemDto.burningQuestionDtos}">
									<tr>
										<td class="text-nowrap">
											<c:out value="${burningQuestionDto.sessionName}" escapeXml="false"/>
										</td>
										<td>
											${burningQuestionDto.escapedBurningQuestion}
										</td>
										<td class="text-nowrap">
											<span class="badge">${burningQuestionDto.likeCount}</span> &nbsp; <i class="fa fa-thumbs-o-up" style="color:darkblue"></i>
										</td>
										<c:if test="${isTbl and discussionSentimentEnabled and not empty discussionItemUid}">
											<td class="discussion-sentiment-start-button-cell">
												<div id="discussion-sentiment-start-button-${discussionItemUid}-${burningQuestionDto.burningQuestion.uid}"
												     class="btn btn-xs btn-default discussion-sentiment-start-button"
												     onClick="javascript:startDiscussionSentiment(${discussionItemUid}, ${burningQuestionDto.burningQuestion.uid}, true)">
													<i class="fa fa-comments"></i><fmt:message key="label.monitoring.discussion.start"/>
												</div>
											</td>
										</c:if>
									</tr>
									<c:if test="${isTbl and discussionSentimentEnabled and not empty discussionItemUid}">
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
</div>
