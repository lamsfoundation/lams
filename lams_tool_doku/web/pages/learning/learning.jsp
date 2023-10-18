<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="dokumaran" value="${sessionMap.dokumaran}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:set var="hasEditRight" value="${sessionMap.hasEditRight}"/>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
	
<lams:PageLearner title="${dokumaran.title}" toolSessionID="${toolSessionID}">
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.countdown.css" />
	<style media="screen,projection" type="text/css">
  		.countdown-timeout {
  			color: #FF3333 !important;
  		}		
  		
  		#countdown {
			width: 150px; 
			font-size: 110%; 
			font-style: italic; 
			color:#47bc23;
			text-align: center;
		}
		
		.lower-to-fit-countdown {
			margin-top: 55px;
		}
	</style>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<lams:JSImport src="includes/javascript/etherpad.js" />
	<script type="text/javascript">
		checkNextGateActivity('finish-button', '${toolSessionID}', '', finishSession);
		$(document).ready(function(){
			// Resize Etherpad iframe when its content grows.
			// It does not support shrinking, only growing.
			// This feature requires ep_resize plugin installed in Etherpad and customised with code in Doku tool
			$(window).on('message onmessage', function (e) {
				var msg = e.originalEvent.data;
		        if (msg.name === 'ep_resize') {
		        	var src = msg.data.location.substring(0, msg.data.location.indexOf('?')),
		        		iframe = $('iframe[src^="' + src + '"]'),
		            	// height should be no less than 200 px
		            	height = Math.max(200, msg.data.height + (${hasEditRight and not timeLimitExceeded} ? 10 : 29));
		           	iframe.height(height);
		        }
		    });
			
			$('#etherpad-container').pad({
				'padId':'${padId}',
				'host':'${etherpadServerUrl}',
				'lang':'${fn:toLowerCase(localeLanguage)}',
				'showControls':'${hasEditRight and not timeLimitExceeded}',
				'showChat':'${dokumaran.showChat}',
				'showLineNumbers':'${dokumaran.showLineNumbers}',
				'height':'' + ($(window).height() - 200)
				<c:if test="${hasEditRight}">
					<c:set var="fullName"><lams:user property="firstName" />&nbsp;<lams:user property="lastName" /></c:set>
					,'userName': encodeURIComponent("<c:out value='${fullName}' />")
				</c:if>
			});
			
			$('[data-bs-toggle="tooltip"]').each((i, el) => {
				new bootstrap.Tooltip($(el))
			});
		});
		
		if (${!hasEditRight && mode != "teacher" && !finishedLock}) {
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
	            	if (json.isLeaderResponseFinalized) {
	            		$("#finish-button, #continue-button").show();
	            	}
	            }
	       	});
		}
	
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
		}
		
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
		// TIME LIMIT
		var dokuWebsocketInitTime = Date.now(),
			dokuWebsocket = new WebSocket('<lams:WebAppURL />'.replace('http', 'ws') 
						+ 'learningWebsocket?toolContentID=' + ${sessionMap.toolContentID}),
			dokuWebsocketPingTimeout = null,
			dokuWebsocketPingFunc = null;
		
		dokuWebsocket.onclose = function(e){
			// react only on abnormal close
			if (e.code === 1006 &&
				Date.now() - dokuWebsocketInitTime > 1000) {
				location.reload();
			}
		};
		
		dokuWebsocketPingFunc = function(skipPing){
			if (dokuWebsocket.readyState == dokuWebsocket.CLOSING 
					|| dokuWebsocket.readyState == dokuWebsocket.CLOSED){
				return;
			}
			
			// check and ping every 3 minutes
			dokuWebsocketPingTimeout = setTimeout(dokuWebsocketPingFunc, 3*60*1000);
			// initial set up does not send ping
			if (!skipPing) {
				dokuWebsocket.send("ping");
			}
		};
		
		// set up timer for the first time
		dokuWebsocketPingFunc(true);

		var timeLimitExceeded = ${timeLimitExceeded};
		// run when the server pushes new reports and vote statistics
		dokuWebsocket.onmessage = function(e) {
			// create JSON object
			var input = JSON.parse(e.data);
	
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
				} else if (timeLimitExceeded){
					if (secondsLeft > 0) {
					    // teacher gave extra time, reload to writable Etherpad
						location.reload();
						return;
					}
				} else {
					// initialise the timer
					displayCountdown(secondsLeft);
				}
			}
			
			// reset ping timer
			clearTimeout(dokuWebsocketPingTimeout);
			dokuWebsocketPingFunc(true);
		};

		// time limit feature
		function displayCountdown(secondsLeft){
			$('#etherpad-panel').addClass('lower-to-fit-countdown');

			var countdown = '<div id="countdown" role="timer"></div>' + 
		    				'<div id="screenreader-countdown" aria-live="polite" class="visually-hidden" aria-atomic="true"></div>'; 
			
			$.blockUI({
				message: countdown, 
				showOverlay: false,
				focusInput: false,
				css: { 
					top: '40px',
					left: '',
					right: '0%',
			        opacity: '1', 
			        width: '150px',
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

					//handle screenreaders
					var screenCountdown = $("#screenreader-countdown");
					var hours = $("#countdown").countdown('getTimes')[4];
					var minutes = $("#countdown").countdown('getTimes')[5];
					if (screenCountdown.data("hours") != hours || screenCountdown.data("minutes") != minutes) {
						var timeLeftText = "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.countdown.time.left' /></spring:escapeBody> ";
						if (hours > 0) {
							timeLeftText += hours + " <spring:escapeBody javaScriptEscape='true'><fmt:message key='label.hours' /></spring:escapeBody> ";
						}
						timeLeftText += minutes + " <spring:escapeBody javaScriptEscape='true'><fmt:message key='label.minutes' /></spring:escapeBody> ";
						screenCountdown.html(timeLeftText);
						
						screenCountdown.data("hours", hours);
						screenCountdown.data("minutes", minutes);
					}
				},
				onExpiry: function(periods) {
			        $.blockUI({  
				        message: '<h1 id="timelimit-expired" role="alert">' +
	        						'<i class="fa fa-refresh fa-spin fa-1x fa-fw"></i> ' +
	        						'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.time.is.over" /></spring:escapeBody>' + 
	        		 			 '</h1>'
			        });
			        setTimeout(function() { 
			        	location.reload();
			        }, 4000); 
				},
				description: "<div id='countdown-label'><fmt:message key='label.time.left' /></div>"
			});
		}
	</script>
	<%@ include file="websocket.jsp"%>		

	<div id="container-main">	

		<!--  Warnings -->
		<c:if test="${not empty sessionMap.submissionDeadline && (sessionMap.mode == 'author' || sessionMap.mode == 'learner')}">
			<lams:Alert5 id="submission-deadline" type="info" close="true">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</lams:Alert5>
		</c:if>
	
		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
			<lams:Alert5 type="danger" id="warn-lock" close="false">
				<c:choose>
					<c:when test="${sessionMap.userFinished}">
						<fmt:message key="message.activityLocked" /> 
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" />
					</c:otherwise>
				</c:choose>
			</lams:Alert5>
		</c:if>

		<lams:errors/>
		
		<div id="instructions" class="instructions">
			<c:out value="${dokumaran.description}" escapeXml="false" />
		</div>
		
		<div id="etherpad-panel" class='card lcard'>			
			<div id="etherpad-container"></div>
			<div id="etherpad-containera"></div>
			<div id="etherpad-containerb"></div>
		</div>

		<!-- Reflection -->
		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<lams:NotebookReedit
				reflectInstructions="${sessionMap.reflectInstructions}"
				reflectEntry="${sessionMap.reflectEntry}"
				isEditButtonEnabled="${mode != 'teacher'}"
				notebookHeaderLabelKey="title.reflection"/>
		</c:if>
		<!-- End Reflection -->

		<c:if test="${mode != 'teacher'}">
			<div class="activity-bottom-buttons">
				<c:choose>
					<c:when test="${dokumaran.galleryWalkEnabled}">
						<button type="button" data-bs-toggle="tooltip" id="continue-button"
								class="btn btn-primary na ${mode == 'author' ? '' : 'disabled'}"
								<c:choose>
									<c:when test="${mode == 'author'}">
										title="<fmt:message key='label.gallery.walk.wait.start.preview' />"
										onClick="javascript:location.href = location.href + '&galleryWalk=forceStart'"
									</c:when>
									<c:otherwise>
										title="<fmt:message key='label.gallery.walk.wait.start' />"
									</c:otherwise>
								</c:choose>
							>
							<fmt:message key="label.continue" />
						</button>
					</c:when>
					
					<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<button type="button" name="FinishButton" id="continue-button"
								onclick="return continueReflect()" class="btn btn-primary na">
							<fmt:message key="label.continue" />
						</button>
					</c:when>
					
					<c:when test="${!hasEditRight && !sessionMap.userFinished && !sessionMap.isLeaderResponseFinalized}">
						<%-- show no button for non-leaders until leader will finish activity  --%>
					</c:when>
					
					<c:otherwise>
						<button type="button" name="FinishButton" id="finish-button" class="btn btn-primary na">
							<c:choose>
				 				<c:when test="${sessionMap.isLastActivity}">
				 					<fmt:message key="label.submit" />
				 				</c:when>
				 				<c:otherwise>
				 		 			<fmt:message key="label.finished" />
				 				</c:otherwise>
				 			</c:choose>
						</button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

	</div>
</lams:PageLearner>