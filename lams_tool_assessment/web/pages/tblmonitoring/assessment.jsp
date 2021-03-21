<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<script>
	$(document).ready(function(){
		var assessmentPane = $('#assessment-pane-${toolContentID}');
		//insert total learners number taken from the parent tblmonitor.jsp
		$(".total-learners-number", assessmentPane).text(TOTAL_LESSON_LEARNERS_NUMBER);
		
		loadResultsPane($('.results', assessmentPane), false);
	});
	
	function loadResultsPane(resultsPane, isRefresh) {
		var toolContentId = resultsPane.data('toolContentId');
		// load an embedded results list
		resultsPane.load("<c:url value='/learning/showResultsForTeacher.do'/>?embedded=true&toolContentID=" + toolContentId, function(){
			var assessmentPane = $(this).closest('.tab-pane'),
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
		let isCorrectButton = button.hasClass('disclose-correct-button'),
			toolContentId = resultsPane.data('toolContentId');
		
		$.ajax({
			'url'  : '<lams:WebAppURL />monitoring/' 
					  + (isCorrectButton ? 'discloseCorrectAnswers' : 'discloseGroupsAnswers')
					  + '.do',
			'type': 'POST',
			'data' : {
				'questionUid'   : button.closest('.disclose-button-group').attr('questionUid'),
				'toolContentID' : toolContentId,
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

	function showStudentChoices() {
		// these methods come from tblmonitor.jsp and aes.jsp in lams_monitoring
		if (${not empty isIraAssessment and isIraAssessment}) {
			loadTab('iraAssessmentStudentChoices', ${toolContentID});
			return;
		}
		loadAePane(${toolContentID}, 'studentChoices');
	}
</script>

<%-- For AEs tab the panes are defined in TBL monitor, for IRA we need to define it here --%>
<div ${isIraAssessment ? 'id="assessment-pane-' += toolContentID += '"' : '' }>
	<c:if test="${isIraAssessment}">
		<div class="row">
			<div class="col-xs-12">
				<h3>
					<fmt:message key="label.ira.questions.marks"/>
				</h3>
			</div>
		</div>
	</c:if>
	
	<!-- Notifications -->  
	<div class="row">
		<div class="col-xs-6 col-sm-4">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<i class="fa fa-users" style="color:gray" ></i> 
						<fmt:message key="label.attendance"/>: <span>${attemptedLearnersNumber}</span>/<span class="total-learners-number"></span> 
					</h4> 
				</div>
			</div>
		</div>
		<div class="col-xs-6 col-sm-8">
			<div class="btn btn-sm btn-default pull-right"
				 onclick="javascript:showStudentChoices()">
				<i class="fa fa-file"></i>
				<fmt:message key="label.show.students.choices"/>
			</div>   
		</div>                 
	</div>
		
	<c:if test="${allowDiscloseAnswers}">
		<%-- Release correct/groups answers for all questions in this assessment --%>
		<div class="row">
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

	<div class="results" data-tool-content-id="${toolContentID}"></div>
</div>