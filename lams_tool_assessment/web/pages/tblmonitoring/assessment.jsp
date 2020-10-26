<%@ include file="/common/taglibs.jsp"%>
<c:set var="method">
	<c:choose>
		<c:when test="${isIraAssessment}">
			iraAssessmentStudentChoices
		</c:when>
		<c:otherwise>
			aesStudentChoices
		</c:otherwise>
	</c:choose>
</c:set>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<script src="<lams:LAMSURL/>includes/javascript/common.js"></script> 
<script>
	$(document).ready(function(){
		// change attempted and all learners numbers
		$('.nav-tabs').bind('click', function (event) {
			
			var link = $(event.target);
			var attemptedLearnersNumber = link.data("attempted-learners-number");
			$("#attempted-learners-number").html(attemptedLearnersNumber);

			var toolContentId = link.data("tool-content-id");
			$("#selected-content-id").val(toolContentId);
		});

		//insert total learners number taken from the parent tblmonitor.jsp
		$("#total-learners-number").html(TOTAL_LESSON_LEARNERS_NUMBER);

		$('.results').each(function(){
			// load results div for the first time
			loadResultsPane($(this), false);
		});
		
	});
	
	function loadResultsPane(resultsPane, isRefresh) {
		// load an embedded results list
		resultsPane.load("<c:url value='/learning/showResultsForTeacher.do'/>?embedded=true&toolContentID=" 
					 + resultsPane.data('toolContentId'), function(){
			var assessmentPane = resultsPane.closest('.assessmentPane'),
				// are any correct/groups buttons clickable?
				discloseAllCorrectEnabled = false,
				discloseAllGroupsEnabled = false; 
			
			// disclose correct/group answers on click
			$('.disclose-button-group .btn', assessmentPane).not('[disabled]').each(function(){
				let button = $(this),
					isCorrectButton = button.hasClass('disclose-correct-button');
				if (isCorrectButton) {
					discloseAllCorrectEnabled = true;
				} else {
					discloseAllGroupsEnabled = true;
				}
				
				
				
				button.click(function(event) {	
					if (!confirm(isCorrectButton ? "<fmt:message key='message.disclose.correct.answers' />"
												: "<fmt:message key='message.disclose.groups.answers' />")) {
						return;
					}
					
					discloseAnswers(button, resultsPane);
				});
			});

			// if disclose all correct/groups answers buttons are clickable, add a click handler
			// and disable if not
			var allCorrectButton = $('.disclose-all-correct-button', assessmentPane);
			if (discloseAllCorrectEnabled) {
				// do not add a handler twice
				if (!isRefresh) {
					allCorrectButton.click(function(){
						if (!confirm("<fmt:message key='message.disclose.all.correct.answers' />")) {
							return;
						}
						$('.disclose-correct-button', assessmentPane).not('[disabled]').each(function() {
							discloseAnswers($(this), resultsPane);
						});
						disabledAndCheckButton(allCorrectButton);
					});
				}
			} else {
				disabledAndCheckButton(allCorrectButton);
			}

			var allGroupsButton = $('.disclose-all-groups-button', assessmentPane);
			if (discloseAllGroupsEnabled) {
				// do not add a handler twice
				if (!isRefresh) {
					allGroupsButton.click(function(){
						if (!confirm("<fmt:message key='message.disclose.all.groups.answers' />")) {
							return;
						}
						$('.disclose-groups-button', assessmentPane).not('[disabled]').each(function() {
							discloseAnswers($(this), resultsPane);
						});
						disabledAndCheckButton(allGroupsButton);
					});
				}
			} else {
				disabledAndCheckButton(allGroupsButton);
			}
		});
	}
	
	function discloseAnswers(button, resultsPane) {
		let isCorrectButton = button.hasClass('disclose-correct-button');
		
		$.ajax({
			'url'  : '<lams:WebAppURL />monitoring/' 
					  + (isCorrectButton ? 'discloseCorrectAnswers' : 'discloseGroupsAnswers')
					  + '.do',
			'type': 'POST',
			'data' : {
				'questionUid'   : button.closest('.disclose-button-group').attr('questionUid'),
				'toolContentID' : $('#selected-content-id').val(),
				'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
			}
		}).done(function(){
			// reload results after disclosing answers
			loadResultsPane(resultsPane, true);
		});
	}

	function disabledAndCheckButton(button){
		button.attr('disabled', true).html('<i class="fa fa-check text-success">&nbsp;</i>' + button.text());
	}
</script>

<!-- Header -->
<div class="row no-gutter">
	<div class="col-xs-12 col-md-12 col-lg-8">
		<h3>
			<c:choose>
				<c:when test="${isIraAssessment}">
					<fmt:message key="label.ira.questions.marks"/>
				</c:when>
				<c:otherwise>
					<fmt:message key="label.ae.questions.marks"/>
				</c:otherwise>
			</c:choose>
		</h3>
	</div>
</div>
<!-- End header -->    

<!-- Notifications -->  
<div class="row no-gutter">
	<div class="col-xs-6 col-md-4 col-lg-4 ">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<i class="fa fa-users" style="color:gray" ></i> 
					<fmt:message key="label.attendance"/>: <span id="attempted-learners-number">${assessmentDtos[0].attemptedLearnersNumber}</span>/<span id="total-learners-number"></span> 
				</h4> 
			</div>
		</div>
	</div>

	<c:if test="${method eq 'aesStudentChoices' or iraAttemptedByAnyLearners}">
		<div class="col-xs-6 col-md-4 col-md-offset-4 col-lg-4 col-lg-offset-2">
			<a href="#nogo" type="button" class="btn btn-sm btn-default buttons_column"
					onclick="javascript:loadTab('${method}', document.getElementById('selected-content-id').value); return false;">
				<i class="fa fa-file"></i>
				<fmt:message key="label.show.students.choices"/>
			</a>
		</div>      
	</c:if>                           
</div>

<div class="row no-gutter">
	<input type="hidden" value="${assessmentDtos[0].assessment.contentId}" id="selected-content-id">
	
	<c:if test="${!isIraAssessment}">
		<ul class="nav nav-tabs">
			<c:forEach var="assessmentDto" items="${assessmentDtos}" varStatus="i">
				<li <c:if test="${(i.index == 0 && toolContentID == null) || (toolContentID != null && assessmentDto.assessment.contentId == toolContentID)}">class="active"</c:if>>
					<a data-toggle="tab" href="#assessment-${assessmentDto.assessment.uid}" 
							data-attempted-learners-number="${assessmentDto.attemptedLearnersNumber}"
							data-tool-content-id="${assessmentDto.assessment.contentId}">
						<c:out value="${assessmentDto.activityTitle}" escapeXml="false"/>
					</a>
				</li>
			</c:forEach>
		</ul>
	</c:if>
		
</div>
<br>
<!-- End notifications -->

<!-- Tables -->
<div class="tab-content">
	<c:forEach var="assessmentDto" items="${assessmentDtos}" varStatus="k">
		<div id="assessment-${assessmentDto.assessment.uid}" class="assessmentPane tab-pane 
			<c:if test="${(k.index == 0 && toolContentID == null) || (toolContentID != null && assessmentDto.assessment.contentId == toolContentID)}">active</c:if>">
			
			<c:if test="${assessmentDto.assessment.allowDiscloseAnswers}">
				<%-- Release correct/groups answers for all questions in this assessment --%>
				<div class="row no-gutter">
					<div class="col-xs-12 col-md-12 col-lg-12">
						<div class="btn-group-sm pull-right disclose-all-button-group">
							<div class="btn btn-default disclose-all-correct-button">
								<fmt:message key="label.disclose.all.correct.answers"/>
							</div>
							<div class="btn btn-default disclose-all-groups-button">
								<fmt:message key="label.disclose.all.groups.answers"/>
							</div>
						</div>
						

					</div>
				</div>
			</c:if>
			
			<div class="results" data-tool-content-id="${assessmentDto.assessment.contentId}"></div>
		</div>
	</c:forEach>
</div>
<!-- End tables -->
