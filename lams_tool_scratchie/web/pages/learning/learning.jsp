<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="scratchie" value="${sessionMap.scratchie}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />	

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>

	<!-- ********************  CSS ********************** -->
	<lams:css />
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" />
	<link href="<lams:WebAppURL/>includes/css/scratchie.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.countdown.css" />
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.jgrowl.css" />
	<link rel="stylesheet" type="text/css" href="${lams}css/circle.css" />
	<link rel="stylesheet" type="text/css" href="<lams:WebAppURL/>includes/css/scratchie-learning.css" />

	<!-- ********************  javascript ********************** -->
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script>
		//Resolve name collision between jQuery UI and Twitter Bootstrap
		$.widget.bridge('uitooltip', $.ui.tooltip);
	</script>
	<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>    
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.timeago.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jgrowl.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
	<script type="text/javascript">
		var assessmentAnswers = new Map([
			<c:forEach var="item" items="${sessionMap.itemList}">
				[${item.uid}, [
				<c:forEach var="optionDto" items="${item.optionDtos}" varStatus="status">
					 <c:if test="${!optionDto.mcqType && not empty optionDto.answer}">"<c:out value='${optionDto.answer}' escapeXml='true' />",</c:if>
				</c:forEach>
				]],
			</c:forEach>
		]);

		$(document).ready(function(){
			//initialize tooltips showing user names next to confidence levels
			$('[data-toggle="tooltip"]').tooltip();

			//handler for VSA input fields
			$('.submit-user-answer-input').keypress(function(event){
				var keycode = (event.keyCode ? event.keyCode : event.which);
				if(keycode == '13') {
					var itemUid = $(this).data("item-uid");
					scratchVsa(itemUid);
					return false;
				}
			});

			//handler for VSA submit buttons
			$(".submit-user-answer").on('click', function(){
				var itemUid = $(this).data("item-uid");
				scratchVsa(itemUid);
				return false;
			});

			//autocomplete for VSA
			$('.ui-autocomplete-input').each(function(){
				$(this).autocomplete({
					'source' : '<c:url value="/learning/vsaAutocomplete.do"/>?itemUid=' + $(this).data("item-uid"),
					'delay'  : 500,
					'minLength' : 3
				});
			});
		});

		//scratch image (used by both scratchMcq() and scratchVsa())
		function scratchImage(itemUid, optionUid, isCorrect) {
			// first show animation, then put static image
			var imageSuffix = isCorrect ? 'correct' : 'wrong';
			var image = $('#image-' + itemUid + '-' + optionUid);

			//show VSA question image 
			if (image.css('visibility') != 'visible') {
				image.css('visibility', 'visible');
			}
			
			image.load(function(){
	    		var image = $(this).off("load");
	    		// show static image after animation
	    		setTimeout(
	    			function(){
	    				image.attr("src", "<lams:WebAppURL/>includes/images/scratchie-" + imageSuffix + ".png");
	    			}, 
	    			1300
	    		);
	    	}).attr("src", "<lams:WebAppURL/>includes/images/scratchie-" + imageSuffix + "-animation.gif");
		}

		//scratch MCQ answer
		function scratchMcq(itemUid, optionUid){
	        $.ajax({
	            url: '<c:url value="/learning/recordItemScratched.do"/>',
	            data: 'sessionMapID=${sessionMapID}&optionUid=' + optionUid + '&itemUid=' + itemUid,
	            dataType: 'json',
	            type: 'post',
	            success: function (json) {
		            if (json == null) {
		            	return false;
		            }
		            	
		            scratchImage(itemUid, optionUid, json.optionCorrect);
		            	
		            if (json.optionCorrect) {
		            	//disable scratching
		            	$("[id^=imageLink-" + itemUid + "]").removeAttr('onclick'); 
		            	$("[id^=imageLink-" + itemUid + "]").css('cursor','default');
		            	$("[id^=image-" + itemUid + "]").not("img[src*='scratchie-correct-animation.gif']").not("img[src*='scratchie-correct.gif']").fadeTo(1300, 0.3);

		            } else {
		            	var id = '-' + itemUid + '-' + optionUid;
		            	$('#imageLink' + id).removeAttr('onclick');
		            	$('#imageLink' + id).css('cursor','default');
		            }
	            }
	       	});
		}

		//scratch VSA answer
		function scratchVsa(itemUid) {
			var input = $("#input-" + itemUid),
				answer = input.val();

			if (answer == "") {
				return;
			}

			//determine if such answer is available in the list
			var answerRowIndex = -1;
			assessmentAnswers.get(itemUid).forEach(function(assessmentAnswer, index) {
				if (assessmentAnswer == xmlEscape(answer)) {//comparing with escaped answer as assessmentAnswers' answers are escaped
					answerRowIndex = index;
				}
			});

			var isRecordVsaAnswerRequired = true;
			if (answerRowIndex != -1) {
				var isScrathed = $('tr#tr-' + answerRowIndex + ' img[src*="scratchie-correct.png"], tr#tr-' + answerRowIndex + ' img[src*="scratchie-wrong.png"]', "#scratches-" + itemUid).length > 0;
				//highlight already scratched answer
				if (isScrathed) {
					var tableRowTohighlight = $("tr#tr-" + answerRowIndex, "#scratches-" + itemUid);
					$([document.documentElement, document.body]).animate(
						{
				        	scrollTop: tableRowTohighlight.offset().top
				    	}, 
				    	1000, 
				    	function() {
				    		tableRowTohighlight.effect("highlight", 1500);
				    	}
				    );
						
					isRecordVsaAnswerRequired = false;
				}

			//if answer is not available in the list, add it to the list
			} else {
				paintNewVsaAnswer(itemUid, answer);
			}

			if (isRecordVsaAnswerRequired) {
				$.ajax({
			    	url: '<c:url value="/learning/recordVsaAnswer.do"/>',
			        data: {
			           	sessionMapID: "${sessionMapID}", 
			           	itemUid: itemUid,
			           	answer: answer 
					},
			        dataType: 'json',
			        type: 'post',
			        success: function (json) {
				    	if (json == null) {
				           	return false;
				        }

				        scratchImage(itemUid, hashCode(answer), json.isAnswerCorrect);
				            	
				        if (json.isAnswerCorrect) {
				           	//disable further answering
				           	$("[id^=image-" + itemUid + "]").not("img[src*='scratchie-correct-animation.gif']").not("img[src*='scratchie-correct.gif']").fadeTo(1300, 0.3);
					            
					        //disable submit button
				           	$("#type-your-answer-" + itemUid).hide();
				        }
			        }
		       	});
			}

	        //blank input field
			input.val("");
		}

		//add new VSA answer to the table (required in case user entered answer not present in the previous answers)
		function paintNewVsaAnswer(itemUid, answer) {
			var answerRowIndex = assessmentAnswers.get(itemUid).length;
			
			var trElem = 
				'<tr id="tr-' + answerRowIndex + '">' +
					'<td style="width: 40px; border: none;">' +
						'<img src="<lams:WebAppURL/>includes/images/answer-' + answerRowIndex + '.png" class="scartchie-image" id="image-' + itemUid + '-' + hashCode(answer) + '" />' +
					'</td>' +

					'<td class="answer-with-confidence-level-portrait">' +
						'<div class="answer-description">' +
							xmlEscape(answer) +
						'</div>' +
						'<hr class="hr-confidence-level" />' +
						'<div style="padding-bottom: 10px;">' +
							'<lams:Portrait userId="${sessionMap.groupLeaderUserId}"/>' +
						'</div>' +
					'</td>' +
				'</tr>';
			$("table#scratches-" + itemUid).append(trElem);
			assessmentAnswers.get(itemUid).push(xmlEscape(answer));
		}

		function xmlEscape(value) {
			return value.replace(/&/g, '&amp;')
            	.replace(/</g, '&lt;')
            	.replace(/>/g, '&gt;')
            	.replace(/"/g, '&#034;')
            	.replace(/'/g, '&#039;');
		}

		//a direct replacement for Java's String.hashCode() method 
		function hashCode(str) {
			return str.split('').reduce((prevHash, currVal) =>
		    	(((prevHash << 5) - prevHash) + currVal.charCodeAt(0))|0, 0);
		}

		//boolean to indicate whether ok dialog is still ON so that autosave can't be run
		var isWaitingForConfirmation = ${isTimeLimitEnabled && isTimeLimitNotLaunched};

		//time limit feature
		<c:if test="${isTimeLimitEnabled}">
			$(document).ready(function(){
				
				//show timelimit-start-dialog in order to start countdown
				if (${isTimeLimitNotLaunched}) {
					
					//show confirmation dialog
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
				        $.blockUI({ message: '<h1 id="timelimit-expired"><i class="fa fa-refresh fa-spin fa-fw"></i> <fmt:message key="label.time.is.over" /></h1>' }); 
				        
				        setTimeout(
							function() {
				        			finish(true);
				        		}, 
				        		4000
				        ); 
					},
					description: "<div id='countdown-label'><fmt:message key='label.countdown.time.left' /></div>"
				});
			}
		</c:if>

		//autosave feature
		<c:if test="${isUserLeader && (mode != 'teacher')}">
			var autosaveInterval = "60000"; // 60 seconds interval
			window.setInterval(
				function(){
					if (isWaitingForConfirmation) return;
					
					//ajax form submit
					$('#burning-questions').ajaxSubmit({
						url: "<lams:WebAppURL/>learning/autosaveBurningQuestions.do?sessionMapID=${sessionMapID}&date=" + new Date().getTime(),
		                success: function() {
			                	$.jGrowl(
			                		"<i class='fa fa-lg fa-floppy-o'></i> <fmt:message key="label.burning.questions.autosaved" />",
			                		{ life: 2000, closeTemplate: '' }
			                	);
		                }
					});
	        	}, 
	        	autosaveInterval
	        );
		</c:if>

		function finish(isTimelimitExpired) {
			var method = $("#method").val();
			
			var proceed = true;
			//ask for leave confirmation only if time limit is not expired
			if (!isTimelimitExpired) {
				var numberOfAvailableScratches = $("[id^=imageLink-][onclick], [id^=type-your-answer-][class=item-required]:visible").length;
				proceed = (numberOfAvailableScratches > 0) ? confirm("<fmt:message key="label.one.or.more.questions.not.completed"></fmt:message>") : true;	
			}
			
			if (proceed) {
				document.getElementById("finishButton").disabled = true;

		        var myForm = $('#burning-questions');
		        myForm.attr("action", '<lams:WebAppURL />learning/' + method + '.do?sessionMapID=${sessionMapID}&date=' + new Date().getTime());
		        myForm.submit();
			}
			
			return false;
		}
    </script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${scratchie.title}">

		<lams:LeaderDisplay username="${sessionMap.groupLeaderName}" userId="${sessionMap.groupLeaderUserId}"/>

		<div class="panel">
			<c:out value="${scratchie.instructions}" escapeXml="false" />
		</div>

		<c:if test="${not empty sessionMap.submissionDeadline}">
			<lams:Alert id="submissionDeadline" close="true" type="info">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${sessionMap.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>
		</c:if>
		
		<c:if test="${mode != 'teacher'}">
			<div id="timelimit-start-dialog"> 
		        <h4>
		        	<fmt:message key='label.are.you.ready' />
		        </h4>
		        <button name="ok" id="timelimit-start-ok" class="button">
					<fmt:message key='label.ok' />
				</button>
			</div>
		</c:if>

		<lams:errors/>

		<div id="questionListArea">
			<%@ include file="questionlist.jsp"%>
		</div>

		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>
