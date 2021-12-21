<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<lams:html>
	<lams:head>
	
		<%@ include file="/common/header.jsp"%>
		
		<title><fmt:message key="label.vsa.allocate.button" /></title>
		<style>
			body {
				padding: 10px;
			}
			
			.sortable-on {
				background: lightgoldenrodyellow;
    			height: 200px;
    			padding: 10px;
    			overflow-y: auto;
			}
			
			.tbl-correct-list {
				border: 3px solid #3c763d;
				border-radius: 10px;
				background-color: #3c763d10;
			}
			
			.tbl-incorrect-list {
				border: 3px solid #a94442;
				border-radius: 10px;
				background-color: #a9444210;
			}
			
			.answer-queue {
				border: 2px solid #ddd;
				border-radius: 10px;
				background: initial;
			}
			
			.filtered {
			    background-color: lightgrey;
			}
			
			.list-group-item {
				cursor: pointer;
			}
			
			#page-description, .question-description {
				margin-bottom: 20px;
			}
		</style>
		
	 	<script type="text/javascript" src="${lams}includes/javascript/portrait.js"></script>
	 	<script type="text/javascript" src="${lams}includes/javascript/Sortable.js"></script>

  	    <script>
	  	$(document).ready(function(){
		    //init options sorting feature
			$('.sortable-on').each(function() {
				let questionUid = $(this).data('question-uid');
				updateAnswerQueueSize(questionUid);
				
			    new Sortable(this, {
			    	group: 'question' + questionUid,
				    animation: 150,
				  	filter: '.filtered', // 'filtered' class is not draggable
				    //ghostClass: 'sortable-placeholder',
				    //direction: 'vertical',
					onStart: function (evt) {
						//stop answers' hover effect, once element dragging started
						//$("#option-table").removeClass("hover-active");
					},
					onEnd: function (evt) {
				        $.ajax({
				            url: '<c:url value="/monitoring/allocateUserAnswer.do"/>',
				            data: {
				            	sessionMapID: "${sessionMapID}",
				            	questionUid: questionUid,
				            	targetOptionUid: $(evt.to).data("option-uid"),
				            	previousOptionUid: $(evt.from).data("option-uid"),
				            	questionResultUid: $(evt.item).data("question-result-uid"),
								"<csrf:tokenname/>":"<csrf:tokenvalue/>"
						    },
				            method: 'post',
				          	dataType: "json",
  				        success: function (data) {
  				        	updateAnswerQueueSize(questionUid);
  				        	
  				            if (data.isAnswerDuplicated) {
	  				        	alert("<fmt:message key="label.someone.allocated.this.answer" />");
	  				        	$(evt.item).appendTo("#answer-group" + data.optionUid);
	  				        	$(evt.item).addClass("filtered");
		  				    }
  				        }
				            	
				       	});
					}
				});
			});
	  	});

	  	function updateAnswerQueueSize(questionUid) {
	  		 var answerQueueLength = $('.answer-queue[data-question-uid="' + questionUid + '"] .list-group-item').length;
			 $('#answer-queue-size' + questionUid).text(answerQueueLength ? ' (' + answerQueueLength + ')' : '');
		}
	  	
   		function refreshPage() { 
       		location.reload();
   		}

   		function closePage() { 
   			self.parent.tb_remove();
   		}
  		</script>
	</lams:head>
	
<body>
	<a href="#nogo" onclick="javascript:closePage()" class="btn btn-default pull-right loffset20">
		<i class="fa fa-close"></i>
		<fmt:message key="label.close" /> 
	</a>
	<a href="#nogo" onclick="javascript:refreshPage()" class="btn btn-primary pull-right">
		<i class="fa fa-refresh"></i>
		<fmt:message key="label.refresh" /> 
	</a>
		
	<h4 id="page-description"><fmt:message key="label.vsa.allocate.description" /></h4>

	
	<c:forEach var="questionSummary" items="${questionSummaries}">
		<c:set var="questionDto" value="${questionSummary.questionDto}"/>
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="panel-title">
					<c:out value="${questionDto.title}" escapeXml="true"/>
				</div>
			</div>
				
			<div class="panel-body">
				<lams:errors/>
		         
		        <c:if test="${not empty questionDto.question}">
			        <div class="question-description">
			        	<c:out value="${questionDto.question}" escapeXml="false"/>
					</div>
				</c:if>
				
		        <!--allocate responses-->
				<div class="row">
					<div class="col-sm-4 text-center">
						<c:set var="option0" value="${questionDto.optionDtos.toArray()[0]}"/>
						<h4>
							<c:choose>
								<c:when test="${questionSummary.tbl && option0.maxMark == 1}">
									<i class="fa fa-check fa-lg text-success"></i> <fmt:message key="label.correct" />
								</c:when>
								<c:when test="${questionSummary.tbl && option0.maxMark == 0}">
									<i class="fa fa-times fa-lg text-danger"></i>	<fmt:message key="label.incorrect" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.authoring.basic.option.grade"/>: ${option0.maxMark}
								</c:otherwise>
							</c:choose>
						</h4>
						
						<div class="list-group col sortable-on ${questionSummary.tbl ? 'tbl-correct-list' : ''}" 
							 data-question-uid="${questionDto.uid}"
							 data-option-uid="${option0.uid}" id="answer-group${option0.uid}"></div>
						
						<fmt:message key="label.answer.alternatives" />: 
						${fn:replace(option0.name, newLineChar, ', ')}
					</div>
					
					<div class="col-sm-4 text-center">
		            	<h4>
		            		<fmt:message key="label.answer.queue" />
		            		<span id="answer-queue-size${questionDto.uid}"></span>
		            	</h4>
		           		
		           		<div class="list-group col sortable-on answer-queue"
		           			 data-question-uid="${questionDto.uid}" 
		           			 data-option-uid="-1" id="answer-group-1">
		            		<c:forEach var="questionResult" items="${questionSummary.notAllocatedQuestionResults}">
		            			<div class="list-group-item" data-question-result-uid="${questionResult.uid}">
		            				<lams:Portrait userId="${questionResult.assessmentResult.user.userId}"/>&nbsp;
		            				${questionResult.answer}
		            			</div>
		            		</c:forEach>
		           		</div>		
					</div>
					
					<div class="col-sm-4 text-center">
						<c:set var="option1" value="${questionDto.optionDtos.toArray()[1]}"/>
						<h4>
							<c:choose>
								<c:when test="${questionSummary.tbl && option1.maxMark == 1}">
									<i class="fa fa-check fa-lg text-success"></i> <fmt:message key="label.correct" />
								</c:when>
								<c:when test="${questionSummary.tbl && option1.maxMark == 0}">
									<i class="fa fa-times fa-lg text-danger"></i>	<fmt:message key="label.incorrect" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.authoring.basic.option.grade"/>: ${option1.maxMark}
								</c:otherwise>
							</c:choose>
						</h4>
						
						<div class="list-group col sortable-on ${questionSummary.tbl ? 'tbl-incorrect-list' : ''}"
							 data-question-uid="${questionDto.uid}"
							 data-option-uid="${option1.uid}" id="answer-group${option1.uid}"></div>	
						
						<fmt:message key="label.answer.alternatives" />: 
						${fn:replace(option1.name, newLineChar, ', ')}
					</div>
				</div>
				
				<c:forEach var="optionDto" items="${questionDto.optionDtos}" begin="2" varStatus="status">
				
					<c:if test="${status.count % 3 == 0}">
						<div class="row">
					</c:if>
				
					<div class="col-sm-4 text-center">
						<h4>
							<fmt:message key="label.authoring.basic.option.grade"/>: ${optionDto.maxMark}
						</h4>
						
						<div class="list-group col sortable-on" data-question-uid="${questionDto.uid}"
							 data-option-uid="${optionDto.uid}" id="answer-group${optionDto.uid}"></div>	
						
						<fmt:message key="label.answer.alternatives" />: 
						${fn:replace(optionDto.name, newLineChar, ', ')}
					</div>
					
					<c:if test="${status.count % 3 == 0 || status.last}">
						</div>
					</c:if>
				</c:forEach>
			</div>
		</div>
	</c:forEach>		
	
	<div id="footer">
	</div>
</body>
</lams:html>