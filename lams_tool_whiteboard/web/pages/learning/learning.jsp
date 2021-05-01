<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="whiteboard" value="${sessionMap.whiteboard}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:set var="hasEditRight" value="${sessionMap.hasEditRight}"/>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
	
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
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
				
		#whiteboard-frame {
			width: 100%;
			height: 700px;
			margin-bottom: 20px;
			border: 1px solid #c1c1c1;
		}
	</style>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript">
	    // avoid name clash between bootstrap and jQuery UI
	    $.fn.bootstrapTooltip = $.fn.tooltip.noConflict();
	    
		$(document).ready(function(){
			$('[data-toggle="tooltip"]').bootstrapTooltip();
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
	            		$("#finish-button").show();
	            	}
	            }
	       	});
		}
	
		function finishSession(){
			document.getElementById("finish-button").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}


		
		// TIME LIMIT
		var whiteboardWebsocketInitTime = Date.now(),
			whiteboardWebsocket = new WebSocket('<lams:WebAppURL />'.replace('http', 'ws') 
						+ 'learningWebsocket?toolContentID=' + ${sessionMap.toolContentID}),
			whiteboardWebsocketPingTimeout = null,
			whiteboardWebsocketPingFunc = null;
		
		whiteboardWebsocket.onclose = function(){
			// react only on abnormal close
			if (e.code === 1006 &&
				Date.now() - whiteboardWebsocketInitTime > 1000) {
				location.reload();
			}
		};
		
		whiteboardWebsocketPingFunc = function(skipPing){
			if (whiteboardWebsocket.readyState == whiteboardWebsocket.CLOSING 
					|| whiteboardWebsocket.readyState == whiteboardWebsocket.CLOSED){
				return;
			}
			
			// check and ping every 3 minutes
			whiteboardWebsocketPingTimeout = setTimeout(whiteboardWebsocketPingFunc, 3*60*1000);
			// initial set up does not send ping
			if (!skipPing) {
				whiteboardWebsocket.send("ping");
			}
		};
		
		// set up timer for the first time
		whiteboardWebsocketPingFunc(true);

		var timeLimitExceeded = ${timeLimitExceeded};
		// run when the server pushes new reports and vote statistics
		whiteboardWebsocket.onmessage = function(e) {
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
					    // teacher gave extra time, reload to writable Whiteboard
						location.reload();
						return;
					}
				} else {
					// initialise the timer
					displayCountdown(secondsLeft);
				}
			}
			
			// reset ping timer
			clearTimeout(whiteboardWebsocketPingTimeout);
			whiteboardWebsocketPingFunc(true);
		};

		function displayCountdown(secondsLeft){
			var countdown = '<div id="countdown"></div>';
			
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
				},
				onExpiry: function(periods) {
			        $.blockUI({ message: '<h1 id="timelimit-expired"><i class="fa fa-refresh fa-spin fa-1x fa-fw"></i> <fmt:message key="label.time.is.over" /></h1>' }); 
			        
			        setTimeout(function() { 
			        	location.reload();
			        }, 4000); 
				},
				description: "<div id='countdown-label'><fmt:message key='label.time.left' /></div>"
			});
		}
	</script>
	
	<%@ include file="websocket.jsp"%>		
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${whiteboard.title}" style="">
	
		<!--  Warnings -->
		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
			<lams:Alert type="danger" id="warn-lock" close="false">
				<c:choose>
					<c:when test="${sessionMap.userFinished}">
						<fmt:message key="message.activityLocked" /> 
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" />
					</c:otherwise>
				</c:choose>
			</lams:Alert>
		</c:if>

		<lams:errors/>
		
		<p><c:out value="${whiteboard.instructions}" escapeXml="false" /></p>
		

		<iframe id="whiteboard-frame"
		        src='${whiteboardServerUrl}?whiteboardid=${wid}&username=${whiteboardAuthorName}${empty whiteboardAccessToken ? "" : "&accesstoken=".concat(whiteboardAccessToken)}${empty whiteboard.sourceWid ? "" : "&copyfromwid=".concat(whiteboard.sourceWid).concat("&copyaccesstoken=").concat(whiteboardCopyAccessToken)}'>
		</iframe>

		<!-- Reflection -->
		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="panel panel-default">
				<div class="panel-heading panel-title">
					<fmt:message key="title.reflection" />
				</div>
				<div class="panel-body">
					<div class="reflectionInstructions">
						<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
					</div>

					<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em> 
								<fmt:message key="message.no.reflection.available" />
							</em>
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
						</p>
					</c:otherwise>
					</c:choose>

					<c:if test="${mode != 'teacher'}">
						<button name="ContinueButton" onclick="return continueReflect()" class="btn btn-sm btn-default voffset5">
						<fmt:message key="label.edit" />
						</button>
					</c:if>
				</div>
			</div>
		</c:if>
		<!-- End Reflection -->

		<c:if test="${mode != 'teacher'}">
			<div>
				<c:choose>
					<c:when test="${whiteboard.galleryWalkEnabled}">
						<button data-toggle="tooltip" 
								class="btn btn-default voffset5 pull-right ${mode == 'author' ? '' : 'disabled'}"
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
						<button name="FinishButton" id="finish-button"
								onclick="return continueReflect()" class="btn btn-default voffset5 pull-right">
							<fmt:message key="label.continue" />
						</button>
					</c:when>
					<c:when test="${!hasEditRight && !sessionMap.userFinished && !sessionMap.isLeaderResponseFinalized}">
						<%-- show no button for non-leaders until leader will finish activity  --%>
					</c:when>
					<c:otherwise>
						<a href="#nogo" name="FinishButton" id="finish-button"
								onclick="return finishSession()" class="btn btn-primary voffset5 pull-right na">
							<span class="nextActivity">
								<c:choose>
				 					<c:when test="${sessionMap.isLastActivity}">
				 						<fmt:message key="label.submit" />
				 					</c:when>
				 					<c:otherwise>
				 		 				<fmt:message key="label.finished" />
				 					</c:otherwise>
				 				</c:choose>
							</span>
						</a>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->

	</lams:Page>
</body>
</lams:html>
