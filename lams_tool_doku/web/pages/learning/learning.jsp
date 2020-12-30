<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
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
<c:set var="isTimeLimitEnabled" value="${hasEditRight && assessment.getTimeLimit() != 0 && !finishedLock}" />
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
	
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.countdown.css" />
	<style media="screen,projection" type="text/css">
		#countdown {
			width: 150px; 
			font-size: 110%; 
			font-style: italic; 
			color:#47bc23;
			text-align: center;
		}
		#countdown-label {
			font-size: 170%; padding-top:5px; padding-bottom:5px; font-style: italic; color:#47bc23;
		}
		
		.lower-to-fit-countdown {
			margin-top: 70px;
		}
	</style>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/etherpad.js"></script>
	<script type="text/javascript">
	    // avoid name clash between bootstrap and jQuery UI
	    $.fn.bootstrapTooltip = $.fn.tooltip.noConflict();
	    
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
		            	height = Math.max(200, msg.data.height - (${hasEditRight} ? 0 : 64));
		           	iframe.height(height);
		        }
		    });
			
			$('#etherpad-container').pad({
				'padId':'${padId}',
				'host':'${etherpadServerUrl}',
				'lang':'${fn:toLowerCase(localeLanguage)}',
				'showControls':'${hasEditRight}',
				'showChat':'${dokumaran.showChat}',
				'showLineNumbers':'${dokumaran.showLineNumbers}',
				'height':'' + ($(window).height() - 200)
				<c:if test="${hasEditRight}">,'userName':'<lams:user property="firstName" />&nbsp;<lams:user property="lastName" />'</c:if>
			});
			
			//hide finish button for non-leaders until leader will finish activity 
			if (${!hasEditRight && !sessionMap.userFinished && !sessionMap.isLeaderResponseFinalized}) {
				$("#finish-button").hide();
			}
			
			if (${secondsLeft > 0}) {
				displayCountdown()
			}
			
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
		
		function displayCountdown() {
			$.blockUI({
				message: '<div id="countdown"></div>', 
				showOverlay: false,
				focusInput: false,
				css: { 
					top: '10px',
					left: '',
					right: '1%',
					width: '150px',
			        opacity: '.8',
			        cursor: 'default',
			        border: 'none'
		        }   
			});
			
			$('#countdown').countdown({
				until: '+${secondsLeft}S',
				format: 'hMS',
				compact: true,
				description: "<div id='countdown-label'><fmt:message key='label.time.left' /></div>",
				onTick: function(periods) {
					//check for 30 seconds
					if ((periods[4] == 0) && (periods[5] == 0) && (periods[6] <= 30)) {
						$('#countdown').css('color', '#FF3333');
					}					
				},
				onExpiry: function(periods) {
			        $.blockUI({ message: '<h1 id="timelimit-expired"><i class="fa fa-refresh fa-spin fa-1x fa-fw"></i> <fmt:message key="label.time.is.over" /></h1>' }); 
			        
			        setTimeout(function() { 
			        	location.reload();
			        }, 4000); 
				}
			});
		}
	</script>
	
	<c:if test="${isTimeLimitEnabled or dokumaran.galleryWalkEnabled}">
		<%@ include file="websocket.jsp"%>		
	</c:if>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${dokumaran.title}" style="">
	
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
		
		<p><c:out value="${dokumaran.description}" escapeXml="false" /></p>
		
		<div class='panel panel-default 
				<c:if test="${isTimeLimitEnabled}">lower-to-fit-countdown</c:if>'>			
			<div id="etherpad-container"></div>
			<div id="etherpad-containera"></div>
			<div id="etherpad-containerb"></div>
		</div>

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
					<c:when test="${dokumaran.galleryWalkEnabled}">
						<button class="btn btn-default voffset5 pull-right disabled"
								data-toggle="tooltip"  title="<fmt:message key='label.gallery.walk.wait.start' />">
							<fmt:message key="label.continue" />
						</button>
					</c:when>
					<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<button name="FinishButton" id="finish-button"
								onclick="return continueReflect()" class="btn btn-default voffset5 pull-right">
							<fmt:message key="label.continue" />
						</button>
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
