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
<c:set var="isScratchingFinished" value="${sessionMap.isScratchingFinished}" />
<c:set var="isWaitingForLeaderToSubmitNotebook" value="${sessionMap.isWaitingForLeaderToSubmitNotebook}" />
<c:set var="hideFinishButton" value="${!isUserLeader && (!isScratchingFinished || isWaitingForLeaderToSubmitNotebook)}" />

<fmt:message key="label.learning.title" var="titlr"/>
<lams:PageLearner title="${title}" toolSessionID="${toolSessionID}">

	<!-- ********************  CSS ********************** -->
	
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" />
	<link rel="stylesheet" type="text/css" href="<lams:WebAppURL/>includes/css/scratchie.css">
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.countdown.css" />
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.jgrowl.css" />
	<link rel="stylesheet" type="text/css" href="${lams}css/circle.css" />
	<link rel="stylesheet" type="text/css" href="<lams:WebAppURL/>includes/css/scratchie-learning.css" />

	<!-- ********************  javascript ********************** -->
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script>
		//Resolve name collision between jQuery UI and Twitter Bootstrap
		$.widget.bridge('uitooltip', $.ui.tooltip);
	</script>
	<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>    
	<script type="text/javascript" src="${lams}includes/javascript/jquery.timeago.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jgrowl.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
	<script type="text/javascript">
		var isScratching = false,
			requireAllAnswers = <c:out value="${scratchie.requireAllAnswers}" />;
		
		$(document).ready(function(){
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
			
			// show etherpads only on Discussion expand
			$('.question-etherpad-collapse').on('show.bs.collapse', function(){
				var etherpad = $('.etherpad-container', this);
				if (!etherpad.hasClass('initialised')) {
					var id = etherpad.attr('id'),
						groupId = id.substring('etherpad-container-'.length);
					etherpadInitMethods[groupId]();
				}
			});
			
			<c:if test="${scratchie.revealOnDoubleClick}">
				$('.scratchie-link').on('touchend', function(){
					if (!this.hasAttribute('onDblClick')) {
						return;
					}
					
					// allow single touch scratching on iPads even if double click scratching is enabled
					var itemUid = $(this).data('itemUid'),
						optionUid = $(this).data('optionUid');
					scratchMcq(itemUid, optionUid);
				});
			</c:if>


			
			// hide Finish button for non-leaders until leader finishes
			if (${hideFinishButton}) {
				$("#finishButton").hide();
			} else if (requireAllAnswers) {
				checkAllCorrectMcqAnswersFound();
			}

			<%-- Connect to command websocket only if it is learner UI --%>
			<c:if test="${mode == 'learner'}">
				// command websocket stuff for refreshing
				// trigger is an unique ID of page and action that command websocket code in Page.tag recognises
				commandWebsocketHookTrigger = 'scratchie-leader-change-refresh-${toolSessionID}';
				// if the trigger is recognised, the following action occurs
				commandWebsocketHook = function() {
					location.reload();
				};
			</c:if>

			//initialize tooltips showing user names next to confidence levels
			$('[data-toggle="tooltip"]').tooltip();
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
			
			image.on('load', function(){
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
			if (isScratching) {
				// do not allow parallel scratching
				return;
			}
			
			isScratching = true;
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
		            	$("[id^=imageLink-" + itemUid + "]").removeAttr('onClick').removeAttr('onDblClick'); 
		            	$("[id^=imageLink-" + itemUid + "]").css('cursor','default');
		            	$("[id^=image-" + itemUid + "]").not("img[src*='scratchie-correct-animation.gif']").not("img[src*='scratchie-correct.gif']").fadeTo(1300, 0.3);

		            	checkAllCorrectMcqAnswersFound();
		            } else {
		            	var id = '-' + itemUid + '-' + optionUid;
		            	$('#imageLink' + id).removeAttr('onclick');
		            	$('#imageLink' + id).css('cursor','default');
		            }

	            },
	            complete : function(){
    				// enable scratching again
    				isScratching = false;
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
			
			if (isScratching) {
				// do not allow parallel scratching
				return;
			}
			
			isScratching = true;

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

			    	var loggedAnswerHash = json.loggedAnswerHash,
			    		isAnswerUnique = loggedAnswerHash == -1,
			    		answerHashToScratch = isAnswerUnique ? hashCode(answer) : loggedAnswerHash
					    trId = "#tr-" + itemUid + "-" + answerHashToScratch;

			    	//if answer was not provided yet, add it to the list
			    	if (isAnswerUnique) {
						paintNewVsaAnswer(itemUid, answer);
					}

			    	var isScrathed = $(trId + ' img[src*="scratchie-correct.png"], ' + trId + ' img[src*="scratchie-wrong.png"]', "#scratches-" + itemUid).length > 0;
			    	//highlight already scratched answer
			    	if (isScrathed) {
						var tableRowTohighlight = $(trId, "#scratches-" + itemUid);
						$([document.documentElement, document.body]).animate(
							{
						       	scrollTop: tableRowTohighlight.offset().top
						   	}, 
						   	1000, 
						   	function() {
						   		tableRowTohighlight.effect("highlight", 1500);
						   	}
						);

			    	//scratch it otherwise
		        	} else {
				        scratchImage(itemUid, answerHashToScratch, json.isAnswerCorrect);
				            	
				        if (json.isAnswerCorrect) {
				           	//disable further answering
				           	$("[id^=image-" + itemUid + "]").not("img[src*='scratchie-correct-animation.gif']").not("img[src*='scratchie-correct.gif']").fadeTo(1300, 0.3);
					            
					        //disable submit button
				           	$("#type-your-answer-" + itemUid).hide();
				        }
					}
		        },
	            complete : function(){
    				// enable scratching again
    				isScratching = false;
	            }
	       	});

	        //blank input field
			input.val("");
		}

		//add new VSA answer to the table (required in case user entered answer not present in the previous answers)
		function paintNewVsaAnswer(itemUid, answer) {
			var optionsLength = $("#scratches #scratches-" + itemUid + " .row").length;
			var idSuffix = '-' + itemUid + '-' + hashCode(answer);
			
			var trElem = 
				'<div id="tr' + idSuffix + '" class="row mx-2">' +
					'<div class="scartchie-image-col">' +
						'<img src="<lams:WebAppURL/>includes/images/answer-' + optionsLength + '.png" class="scartchie-image" id="image' + idSuffix + '" />' +
					'</div>' +

					'<div class="col answer-with-confidence-level-portrait">' +
						'<div class="answer-description">' +
							xmlEscape(answer) +
						'</div>' +
						'<hr class="hr-confidence-level" />' +
						'<div style="padding-bottom: 10px;">' +
							'<lams:Portrait userId="${sessionMap.groupLeaderUserId}"/>' +
						'</div>' +
					'</div>' +
				'</div>';
			$(".table#scratches-" + itemUid).append(trElem);
		}

		function checkAllCorrectMcqAnswersFound() {
            var numberOfAvailableScratches = $("[id^=imageLink-][onclick]").length;
			if (numberOfAvailableScratches > 0) {
	            $('#finishButton').prop('disabled', true).css('pointer-events', 'none')
	            				  .parent().attr('data-title', '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.learning.require.all.answers" /></spring:escapeBody>')
				  				  		   .tooltip();
			} else {
				$('#finishButton').prop('disabled', false)
								  .css('pointer-events', 'auto')
								  .parent().tooltip('dispose');
			}
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
			var hash = 0;
		    if (str.length == 0) {
		        return hash;
		    }
		    for (var i = 0; i < str.length; i++) {
		        var char = str.charCodeAt(i);
		        hash = ((hash<<5)-hash)+char;
		        hash = hash & hash; // Convert to 32bit integer
		    }
		    return hash;
		}

		<c:if test="${mode != 'teacher'}">
			// time limit feature
			
			function displayCountdown(secondsLeft){
				var countdown = '<div id="countdown" role="timer"></div>' 
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
					until: '+' + secondsLeft +'S',
					format: 'hMS',
					compact: true,
					alwaysExpire : true,
					onTick: function(periods) {
						// check for 30 seconds or less and display timer in red
						var secondsLeft = $.countdown.periodsToSeconds(periods);
						if (secondsLeft <= 30) {
							$(this).addClass('countdown-timeout');
						} else {
							$(this).removeClass('countdown-timeout');
						}
					},
					onExpiry: function(periods) {
				        $.blockUI({ message: '<h1 id="timelimit-expired" role="alert"><i class="fa fa-refresh fa-spin fa-1x fa-fw"></i> <spring:escapeBody javaScriptEscape="true"><fmt:message key="label.time.is.over" /></spring:escapeBody></h1>' }); 
				        
				        setTimeout(function() { 
					        if (${isUserLeader}) {
						    	finish(true);
					        } else {
					        	location.reload();
						    }
				        }, 4000);
					},
					description: "<div id='countdown-label'><spring:escapeBody javaScriptEscape='true'><fmt:message key='label.countdown.time.left' /></spring:escapeBody></div>"
				});
			}
	
			//init the connection with server using server URL but with different protocol
			var scratchieWebsocketInitTime = Date.now(),
				scratchieWebsocket = new WebSocket('<lams:WebAppURL />'.replace('http', 'ws') 
							+ 'learningWebsocket?toolSessionID=' + ${toolSessionID} + '&toolContentID=' + ${scratchie.contentId}),
				scratchieWebsocketPingTimeout = null,
				scratchieWebsocketPingFunc = null;
			
			scratchieWebsocket.onclose = function(e) {
				// react only on abnormal close
				if (e.code === 1006 &&
					Date.now() - scratchieWebsocketInitTime > 1000) {
					location.reload();		
				}
			};
			
			scratchieWebsocketPingFunc = function(skipPing){
				if (scratchieWebsocket.readyState == scratchieWebsocket.CLOSING 
						|| scratchieWebsocket.readyState == scratchieWebsocket.CLOSED){
					return;
				}
				
				// check and ping every 3 minutes
				scratchieWebsocketPingTimeout = setTimeout(scratchieWebsocketPingFunc, 3*60*1000);
				// initial set up does not send ping
				if (!skipPing) {
					scratchieWebsocket.send("ping");
				}
			};
			
			// set up timer for the first time
			scratchieWebsocketPingFunc(true);
			
			// run when the server pushes new reports and vote statistics
			scratchieWebsocket.onmessage = function(e) {
				// create JSON object
				var input = JSON.parse(e.data);

				if (input.pageRefresh) {
					location.reload();
					return;
				}
			
				if (input.clearTimer == true) {
					// teacher stopped the timer, destroy it
					$('#countdown').countdown('destroy').remove();
				} else if (typeof input.secondsLeft != 'undefined'){
					// teacher updated the timer
					var secondsLeft = +input.secondsLeft,
						counterInitialised = $('#countdown').length > 0;
						
					if (counterInitialised) {
						// just set the new time
						$('#countdown').countdown('option', 'until', secondsLeft + 'S');
					} else {
						// initialise the timer
						displayCountdown(secondsLeft);
					}
				} else if (${not isUserLeader}){
					// reflect the leader's choices
					$.each(input, function(itemUid, options) {
						$.each(options, function(optionUid, optionProperties){
							
							if (optionProperties.isVSA) {
								var answer = optionUid;
								optionUid = hashCode(optionUid);
	
								//check if such image exists, create it otherwise
								if ($('#image-' + itemUid + '-' + optionUid).length == 0) {
									paintNewVsaAnswer(eval(itemUid), answer);
								}
							}
							
							scratchImage(itemUid, optionUid, optionProperties.isCorrect);
						});
					});
				}
				
				// reset ping timer
				clearTimeout(scratchieWebsocketPingTimeout);
				scratchieWebsocketPingFunc(true);
			};
		</c:if>

		//autosave feature
		<c:if test="${isUserLeader && (mode != 'teacher')}">
			var autosaveInterval = "60000"; // 60 seconds interval
			window.setInterval(learnerAutosave,	autosaveInterval);
			
			function learnerAutosave(isCommand){
                // isCommand means that the autosave was triggered by force complete or another command websocket message
			    // in this case do not check multiple tabs open, just autosave
			    if (!isCommand) {
				  let shouldAutosave = preventLearnerAutosaveFromMultipleTabs(autosaveInterval);
				  if (!shouldAutosave) {
					return;
				  }
			    }
			    
				//ajax form submit
				$('#burning-questions').ajaxSubmit({
					url: "<lams:WebAppURL/>learning/autosaveBurningQuestions.do?sessionMapID=${sessionMapID}&date=" + new Date().getTime(),
	                success: function() {
		                $.jGrowl(
		                	"<span aria-live='polite'><i class='fa fa-lg fa-floppy-o'></i> <spring:escapeBody javaScriptEscape='true'><fmt:message key='label.burning.questions.autosaved' /></spring:escapeBody><span>",
		                	{ life: 2000, closeTemplate: '' }
		                );
	                }
				});
			}
		</c:if>

		function finish(isTimelimitExpired) {
			var method = $("#method").val();
			
			var proceed = true;
			// ask for leave confirmation only if time limit is not expired
			if (!isTimelimitExpired) {
				var numberOfAvailableScratches = $("[id^=imageLink-][onclick], [id^=type-your-answer-]:visible").length;
				proceed = numberOfAvailableScratches == 0 || 
						  confirm("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.one.or.more.questions.not.completed'/></spring:escapeBody>");	
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

	<div class="container-lg">
		<lams:LeaderDisplay username="${sessionMap.groupLeaderName}" userId="${sessionMap.groupLeaderUserId}"/>

		<c:if test="${sessionMap.userFinished && (mode == 'teacher')}">
			<div class="mt-3">
				<lams:Alert5 id="score" type="info" close="false">
					<fmt:message key="label.you.ve.got">
						<fmt:param>${score}</fmt:param>
						<fmt:param>${scorePercentage}</fmt:param>
					</fmt:message>
				</lams:Alert5>
			</div>
		</c:if>
		
		<c:if test="${isUserLeader and scratchie.revealOnDoubleClick}">
			<lams:Alert5 type="warning" id="reveal-double-click-warning" close="false">
				<i class="fa fa-info-circle fa-lg" aria-hidden="true"></i> <fmt:message key="label.learning.reveal.double.click" />
			</lams:Alert5>			
		</c:if>

		<div id="instructions" class="instructions">
			<c:out value="${scratchie.instructions}" escapeXml="false" />
		</div>

		<c:if test="${not empty sessionMap.submissionDeadline}">
			<lams:Alert5 id="submissionDeadline" close="true" type="info">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${sessionMap.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert5>
		</c:if>
		
		<c:if test="${mode != 'teacher'}">
			<div id="timelimit-start-dialog" role="alertdialog" aria-labelledby="are-you-ready"> 
		        <h4 id="are-you-ready">
		        	<fmt:message key='label.are.you.ready' />
		        </h4>
		        <button name="ok" id="timelimit-start-ok" class="button">
					<fmt:message key='label.ok' />
				</button>
			</div>
		</c:if>

		<lams:errors5/>

		<div id="questionListArea">
			<%@ include file="questionlist.jsp"%>
		</div>
		
	</div>
</lams:PageLearner>
