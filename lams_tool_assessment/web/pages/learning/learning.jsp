<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="assessment" value="${sessionMap.assessment}" />
	<c:set var="pageNumber" value="${sessionMap.pageNumber}" />
	<c:set var="isResubmitAllowed" value="${sessionMap.isResubmitAllowed}" />
	<c:set var="hasEditRight" value="${sessionMap.hasEditRight}"/>
	<c:set var="isTimeLimitEnabled" value="${hasEditRight && assessment.getTimeLimit() != 0}" />
	<c:set var="result" value="${sessionMap.assessmentResult}" />
	<c:set var="isUserLeader" value="${sessionMap.isUserLeader}"/>
	<c:set var="isLeadershipEnabled" value="${assessment.useSelectLeaderToolOuput}"/>
		
	<!-- hasEditRight=${hasEditRight} -->
	
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.countdown.css" />
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.jgrowl.css" />
	<style media="screen,projection" type="text/css">
		#timelimit-start-dialog {
			display:none; cursor: default; padding: 10px;
		}
		#timelimit-start-dialog h1{
			padding-bottom: 10px;
		}
		#countdown {
			width: 150px; position: absolute; font-size: 110%; font-style: italic; color:#47bc23;
		}
		#countdown-label {
			font-size: 170%; padding-top:5px; padding-bottom:5px; font-style: italic; color:#47bc23;
		}
		#timelimit-expired {
			font-size: 145%; padding: 15px;
		}
		
		.jGrowl {
		  	font-size: 22px;
		  	font-family: arial, helvetica, sans-serif;
		  	margin-left: 120px;
		}
		.jGrowl-notification {
			opacity: .6;
			border-radius: 10px;
			width: 260px;
			padding: 10px 20px;
  			margin: 10px 20px;
		}
		.jGrowl-message {
			padding-left: 10px;
			padding-top: 5px;
		}
	</style>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jgrowl.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			
			//if isLeadershipEnabled - enable/disable submit buttons for hedging marks type of questions
			if (${isLeadershipEnabled}) {
				$(".mark-hedging-select").on('change keydown keypress keyup paste', function() {
					
					//get questionIndex based on whether element is select or textbox
					var questionIndex = $(this).is("select") ? $(this).data("question-index") : $(this).attr("name").substring(8, $(this).attr("name").indexOf("__"));
					
					var selects = $("select[name^=question" + questionIndex + "_]");
					var grade = selects.length == 0 ? 0 : eval(selects.first().find('option:last-child').val())
					var totalSelected = countHedgeQuestionSelectTotal(questionIndex);
					

					var isButtonEnabled = (totalSelected == grade);
					
					//check if hedging justification is enabled
					var justificationTextarea = $("#question" + questionIndex + "__lamstextarea");
					if( justificationTextarea.length) {
						isButtonEnabled = isButtonEnabled && $.trim(justificationTextarea.val());
					}
					
					//if totalSelected equals to question's grade - show button
					if (isButtonEnabled) {
						$("[type=button][name=submit-hedging-question" + questionIndex + "]").prop("disabled", "").removeClass("button-disabled");
					} else {
						$("[type=button][name=submit-hedging-question" + questionIndex + "]").prop("disabled", "true").addClass("button-disabled");
					}
				}).trigger("change");
				
			}

		});
		
		function countHedgeQuestionSelectTotal(questionIndex) {
			var totalSelected = 0;
			$("select[name^=question" + questionIndex + "_]").each(function() {
				totalSelected += eval(this.value);
			});
			return totalSelected;
		}
	
		//boolean to indicate whether ok dialog is still ON so that autosave can't be run
		var isWaitingForConfirmation = ${isTimeLimitEnabled && sessionMap.isTimeLimitNotLaunched};
	
		//timelimit feature
		<c:if test="${isTimeLimitEnabled}">
			$(document).ready(function(){
				//show timelimit-start-dialog in order to start countdown
				if (${sessionMap.isTimeLimitNotLaunched}) {
					
					$.blockUI({ 
						message: $('#timelimit-start-dialog'), 
						css: { width: '325px', height: '120px'}, 
						overlayCSS: { opacity: '.98'} 
					});
					
					//once OK button pressed start countdown
			        $('#timelimit-start-ok').click(function() {
			        	
			        	//store date when user has started activity with time limit
				        $.ajax({
				        	async: true,
				            url: '<c:url value="/learning/launchTimeLimit.do"/>',
				            data: 'sessionMapID=${sessionMapID}',
				            type: 'post'
				       	});
			        	
			        	$.unblockUI();
			        	displayCountdown();
			        	isWaitingForConfirmation = false;
			        });
					
				} else {
					displayCountdown();
				}
			});
			
			function displayCountdown(){
				var countdown = '<div id="countdown"></div>' 
				$.blockUI({
					message: countdown, 
					showOverlay: false,
					focusInput: false,
					css: { 
						top: '40px',
						left: '',
						right: '0%',
				        opacity: '.8', 
				        width: '230px',
				        cursor: 'default',
				        border: 'none'
			        }   
				});
				
				$('#countdown').countdown({
					until: '+${secondsLeft}S',
					format: 'hMS',
					compact: true,
					onTick: function(periods) {
						//check for 30 seconds
						if ((periods[4] == 0) && (periods[5] == 0) && (periods[6] <= 30)) {
							$('#countdown').css('color', '#FF3333');
						}					
					},
					onExpiry: function(periods) {
				        $.blockUI({ message: '<h1 id="timelimit-expired"><i class="fa fa-refresh fa-spin fa-1x fa-fw"></i> <fmt:message key="label.learning.blockui.time.is.over" /></h1>' }); 
				        
				        setTimeout(function() { 
				        	submitAll(true);
				        }, 4000); 
					},
					description: "<div id='countdown-label'><fmt:message key='label.learning.countdown.time.left' /></div>"
				});
			}
		</c:if>
		
		//autosave feature
		<c:if test="${hasEditRight && (mode != 'teacher')}">
			
			var autosaveInterval = "30000"; // 30 seconds interval
			window.setInterval(
				function(){
					if (isWaitingForConfirmation) return;
					
					//copy value from CKEditor (only available in essay type of questions) to textarea before ajax submit
					$("textarea[id^='question']:not([id$='__lamstextarea'])").each(function()  {
						var ckeditorData = CKEDITOR.instances[this.name].getData();
						//skip out empty values
						this.value = ((ckeditorData == null) || (ckeditorData.replace(/&nbsp;| |<br \/>|\s|<p>|<\/p>|\xa0/g, "").length == 0)) ? "" : ckeditorData;						
					});
					
					//fire onchange event for lams:textarea
					$("[id$=__lamstextarea]").change();
					
					//ajax form submit
					$('#answers').ajaxSubmit({
						url: "<c:url value='/learning/autoSaveAnswers.do'/>?sessionMapID=${sessionMapID}&date=" + new Date().getTime(),
		                success: function() {
		                	$.jGrowl(
		                		"<i class='fa fa-lg fa-floppy-o'></i> <fmt:message key="label.learning.draft.autosaved" />",
		                		{ life: 2000, closeTemplate: '' }
		                	);
		                }
					});
	        	}, autosaveInterval
	        );
		</c:if>
		
		//check if we came back due to failed answers' validation (missing required question's answer or min words limit not reached)
		$(document).ready(function(){
			if (${param.isAnswersValidationFailed == true}) {
				validateAnswers();
			}
		});
		
		function disableButtons() {
			$('.btn').prop('disabled',true);
		}
		
		function nextPage(pageNumber){
			if (!validateAnswers()) {
				return;
			}
			disableButtons();
        	var myForm = $("#answers");
        	myForm.attr("action", "<c:url value='/learning/nextPage.do?sessionMapID=${sessionMapID}&pageNumber='/>" + pageNumber);
        	myForm.submit();
		}
		
		function submitAll(isTimelimitExpired){
			
			//only if time limit is not expired
			if (!isTimelimitExpired) {
				if (!validateAnswers()) {
					return;
				}
			}
			disableButtons();
        	var myForm = $("#answers");
        	myForm.attr("action", "<c:url value='/learning/submitAll.do?sessionMapID=${sessionMapID}'/>&isTimelimitExpired=" + isTimelimitExpired);
        	myForm.submit();
		}
		
		function submitSingleMarkHedgingQuestion(singleMarkHedgingQuestionUid, questionIndex){
			
			//only if time limit is not expired
			if ((typeof isTimelimitExpired !== 'undefined') && isTimelimitExpired) {
				return;
			}
			
			//ajax form submit
			$('#answers').ajaxSubmit({
				url: "<c:url value='/learning/submitSingleMarkHedgingQuestion.do'/>?sessionMapID=${sessionMapID}&singleMarkHedgingQuestionUid=" + singleMarkHedgingQuestionUid +"&questionIndex="+ questionIndex +"&date=" + new Date().getTime(),
				target: '#mark-hedging-question-' + singleMarkHedgingQuestionUid,
                success: function() {
                	$('#question-area-' + questionIndex).removeClass('bg-warning');
                }
			});
		}

		function upOption(questionUid, idx){
			var orderingArea = "#orderingArea" + questionUid;
			var url = "<c:url value="/learning/upOption.do"/>";
			$(orderingArea).load(
					url,
					{
						questionUid: questionUid,
						optionIndex: idx, 
						sessionMapID: "${sessionMapID}"
					}
			);
		}
		function downOption(questionUid, idx){
			var orderingArea = "#orderingArea" + questionUid;
			var url = "<c:url value="/learning/downOption.do"/>";
			$(orderingArea).load(
				url,
				{
					questionUid: questionUid,
					optionIndex: idx, 
					sessionMapID: "${sessionMapID}"
				}
			);		    
		}		
		
		if (${!hasEditRight && mode != "teacher"}) {
			setInterval("checkLeaderProgress();", 15000);// Auto-Refresh every 15 seconds for non-leaders
		}
		
		function checkLeaderProgress() {
	        $.ajax({
	        	async: false,
	            url: '<c:url value="/learning/checkLeaderProgress.do"/>',
	            data: 'toolSessionID=${toolSessionID}',
	            dataType: 'json',
	            type: 'post',
	            success: function (json) {
	            	if (json.isPageRefreshRequested) {
	            		location.reload();
	            	}
	            }
	       	});
		}
		
		function validateAnswers() {

			if (${!hasEditRight}) {
				return true;
			}
			
			var missingRequiredQuestions = [];
			var minWordsLimitNotReachedQuestions = [];
			var maxWordsLimitExceededQuestions = [];
			var markHedgingWrongTotalQuestions = [];
			var markHedgingWrongJustification = [];
			
			<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}" varStatus="status">
				<c:if test="${question.answerRequired}">
					<c:choose>
						<c:when test="${question.type == 1}">
							//multiplechoice
							if ($("input[name^=question${status.index}]:checked").length == 0) {
								missingRequiredQuestions.push("${status.index}");
							}
						</c:when>
						
						<c:when test="${question.type == 2}">
							//matchingpairs
							
	    					var eachSelectHasBeenAnswered = true;
	    					$("select[name^=question${status.index}_]").each(function() {
	    						eachSelectHasBeenAnswered &= this.value != -1;
	    					});
	    					
							if (!eachSelectHasBeenAnswered) {
								missingRequiredQuestions.push("${status.index}");
							}

						</c:when>
						
						<c:when test="${(question.type == 3) || (question.type == 4) || (question.type == 6 && !question.allowRichEditor)}">
							//shortanswer or numerical or essay without ckeditor
							var inputText = $("input[name=question${status.index}]")[0];
							if($.trim(inputText.value).length == 0) {
								missingRequiredQuestions.push("${status.index}");
							}
						</c:when>
							
						<c:when test="${question.type == 5}">
							//truefalse
							if ($("input[name=question${status.index}]:checked").length == 0) {
								missingRequiredQuestions.push("${status.index}");
							}
						</c:when>
							
						<c:when test="${question.type == 6 && question.allowRichEditor}">
							//essay with ckeditor
							var ckeditorData = CKEDITOR.instances["question${status.index}"].getData();
							//can't be null and empty value
							if((ckeditorData == null) || (ckeditorData.replace(/&nbsp;| |<br \/>|\s|<p>|<\/p>|\xa0/g, "").length == 0)) {
								missingRequiredQuestions.push("${status.index}");
							}
						</c:when>
							
						<c:when test="${question.type == 7}">
							//ordering - do nothing
						</c:when>
							
						<c:when test="${question.type == 8}">
							//mark hedging - processed below
						</c:when>
					</c:choose>
				</c:if>
				
				//essay question which has max words limit
				<c:if test="${question.type == 6 && question.maxWordsLimit != 0}">
					var wordCount${status.index} = $('#word-count${status.index}').text();
					//max words limit is exceeded
					if(wordCount${status.index} > ${question.maxWordsLimit}) {
						maxWordsLimitExceededQuestions.push("${status.index}");
					}
				</c:if>
				
				//essay question which has min words limit
				<c:if test="${question.type == 6 && question.minWordsLimit != 0}">
					var wordCount${status.index} = $('#word-count${status.index}').text();
					//min words limit is not reached
					if(wordCount${status.index} < ${question.minWordsLimit}) {
						minWordsLimitNotReachedQuestions.push("${status.index}");
					}
				</c:if>
				
				//mark hedging question and leader aware feature is ON => question is required to answer
				<c:if test="${question.type == 8}">
				
					var questionIndex = ${status.index};
					
					if (${isLeadershipEnabled}) {
						if ($("[type=button][name=submit-hedging-question"+ questionIndex +"]").length > 0) {
							markHedgingWrongTotalQuestions.push("${status.index}");
						}
						
					} else {
						var grade = ${question.grade};
						var totalSelected = countHedgeQuestionSelectTotal(questionIndex);
				
						//if totalSelected not equals to question's grade OR textarea is empty or contains only white-space - show warning
						if (totalSelected != grade) {
							markHedgingWrongTotalQuestions.push("${status.index}");
						}
						if(${question.hedgingJustificationEnabled} && !$.trim($("#question" + questionIndex + "__lamstextarea").val())){
							markHedgingWrongJustification.push("${status.index}");
						}
					}
					
				</c:if>
				
			</c:forEach>
			
			//return true in case all required questions were answered, false otherwise
			if (missingRequiredQuestions.length == 0 && minWordsLimitNotReachedQuestions.length == 0 
					&& maxWordsLimitExceededQuestions.length == 0 && markHedgingWrongTotalQuestions.length == 0 && markHedgingWrongJustification.length ==0) {
				return true;
				
			} else {
				//remove .bg-warning from all questions
				$('[id^=question-area-]').removeClass('bg-warning');
				$('#warning-answers-required').hide();
				$('#warning-words-limit').hide();
				$('#warning-mark-hedging-wrong-total').hide();
				$('#warning-mark-hedging-wrong-justification').hide();
				
				if (missingRequiredQuestions.length != 0) {
					//add .bg-warning class to those needs to be filled
					for (i = 0; i < missingRequiredQuestions.length; i++) {
					    $("#question-area-" + missingRequiredQuestions[i]).addClass('bg-warning');
					}
					//show alert message as well
					$("#warning-answers-required").show();
					
				}
				
				if (maxWordsLimitExceededQuestions.length != 0) {
					//add .bg-warning class to those needs to be filled
					for (i = 0; i < maxWordsLimitExceededQuestions.length; i++) {
					    $("#question-area-" + maxWordsLimitExceededQuestions[i]).addClass('bg-warning');
					}
					//show alert message as well
					$("#warning-words-limit").show();		
				}
				
				if (minWordsLimitNotReachedQuestions.length != 0) {
					//add .bg-warning class to those needs to be filled
					for (i = 0; i < minWordsLimitNotReachedQuestions.length; i++) {
					    $("#question-area-" + minWordsLimitNotReachedQuestions[i]).addClass('bg-warning');
					}
					//show alert message as well
					$("#warning-words-limit").show();		
				}
				
				if (markHedgingWrongTotalQuestions.length != 0) {
					//add .bg-warning class to those needs to be filled
					for (i = 0; i < markHedgingWrongTotalQuestions.length; i++) {
					    $("#question-area-" + markHedgingWrongTotalQuestions[i]).addClass('bg-warning');
					}
					//show alert message as well
					$("#warning-mark-hedging-wrong-total").show();		
				}
				if (markHedgingWrongJustification.length != 0) {
					//add .bg-warning class to those needs to be filled
					for (i = 0; i < markHedgingWrongJustification.length; i++) {
					    $("#question-area-" + markHedgingWrongJustification[i]).addClass('bg-warning');
					}
					//show alert message as well
					$("#warning-mark-hedging-wrong-justification").show();
				}
				
				$("html, body").animate({ scrollTop: 0 }, "slow");//window.scrollTo(0, 0);
				
				return false;
			}
		}

		function getNumberOfWords(value, isRemoveHtmlTags) {
			
		    //HTML tags stripping 
			if (isRemoveHtmlTags) {
				value = value.replace(/&nbsp;/g, '').replace(/<\/?[a-z][^>]*>/gi, '');
			}
			value = value.trim();
			
		    var wordCount = value ? (value.replace(/['";:,.?\-!]+/g, '').match(/\S+/g) || []).length : 0;
		    return wordCount;
		}
    </script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${assessment.title}">
		
		<c:if test="${not empty sessionMap.submissionDeadline && (sessionMap.mode == 'author' || sessionMap.mode == 'learner')}">
			<lams:Alert id="submission-deadline" type="info" close="true">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</lams:Alert>
		</c:if>
		
		<c:if test="${isLeadershipEnabled}">
			<h4>
				<fmt:message key="label.group.leader" >
					<fmt:param>${sessionMap.groupLeader.firstName} ${sessionMap.groupLeader.lastName}</fmt:param>
				</fmt:message>
			</h4>
		</c:if>

		<div class="panel">
			<c:out value="${assessment.instructions}" escapeXml="false"/>
		</div>
		
		<lams:Alert id="warning-answers-required" type="warning" close="true">
			<fmt:message key="warn.answers.required" />
		</lams:Alert>
		
		<lams:Alert id="warning-words-limit" type="warning" close="true">
			<fmt:message key="warn.answers.word.requirements.limit" />
		</lams:Alert>
		
		<lams:Alert id="warning-mark-hedging-wrong-total" type="warning" close="true">
			<fmt:message key="warn.mark.hedging.wrong.total" />
		</lams:Alert>
		
		<lams:Alert id="warning-mark-hedging-wrong-justification" type="warning" close="true">
			<fmt:message key="warn.mark.hedging.wrong.justification" />
		</lams:Alert>
		
		<div id="timelimit-start-dialog"> 
	        <h4>
	        	<fmt:message key='label.learning.blockui.are.you.ready' />
	        </h4>
	        <html:button property="ok" styleId="timelimit-start-ok" styleClass="button">
				OK
			</html:button>
		</div>
		
		<%@ include file="/common/messages.jsp"%>
		<br>
		
		<div class="form-group">
			<%@ include file="parts/allquestions.jsp"%>
			
			<%@ include file="parts/paging.jsp"%>
		</div>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right">
				<c:if test="${hasEditRight}">					
					<html:button property="submitAll"
							onclick="return submitAll(false);" 
							styleClass="btn btn-primary voffset10 pull-right na">
						<fmt:message key="label.learning.submit.all" />
					</html:button>
				</c:if>
			</div>
		</c:if>

	</lams:Page>

</body>
</lams:html>