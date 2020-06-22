<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="dokumaran" value="${sessionMap.dokumaran}" />
<%@ page import="org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants"%>
<link rel="stylesheet" type="text/css" href="${lams}css/jquery.countdown.css" />
<link rel="stylesheet" type="text/css" href="${lams}css/jquery.jgrowl.css" />
<style media="screen,projection" type="text/css">
	#countdown {
		width: 150px; 
		font-size: 110%; 
		font-style: italic; 
		color:#47bc23; 
		margin-bottom: 10px;
		text-align: center;
		position: static;
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
	
	.panel {
		overflow: auto;
	}
	#time-limit-buttons {
		padding-bottom: 20px;
	}
</style>

<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jgrowl.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/portrait.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/etherpad.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript">
	var isCountdownStarted = ${not empty dokumaran.timeLimitLaunchedDate};
	
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
	            	height = Math.max(200, msg.data.height - 10);
	           	iframe.height(height);
	        }
	    });
		
		<c:set var="fullName"><lams:user property="firstName" />&nbsp;<lams:user property="lastName" /></c:set>
		<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
			$('#etherpad-container-${groupSummary.sessionId}').pad({
				'padId':'${groupSummary.padId}',
				'host':'${etherpadServerUrl}',
				//'lang':'',
				'showControls':'true',
				'showChat':'${dokumaran.showChat}',
				'showLineNumbers':'${dokumaran.showLineNumbers}',
				'height':'600',
				'userName':'<c:out value="${fullName}" />'
			});			
		</c:forEach>
		
		$(".fix-faulty-pad").click(function() {
			var toolSessionId = $(this).data("session-id");
			var button = $(this);
			
	    	//block #buttons
	    	$(this).block({
	    		message: '<h4 style="color:#fff";><fmt:message key="label.pad.started.fixing" /></h4>',
	    		baseZ: 1000000,
	    		fadeIn: 0,
	    		css: {
	    			border: 'none',
	    		    padding: "2px 7px", 
	    		    backgroundColor: '#000', 
	    		    '-webkit-border-radius': '10px', 
	    		    '-moz-border-radius': '10px', 
	    		    opacity: .98 ,
	    		    left: "0px",
	    		    width: "360px"
	    		},
	    		overlayCSS: {
	    			opacity: 0
	    		}
	    	});
	    	
	        $.ajax({
	        	async: true,
	            url: '<c:url value="/monitoring/fixFaultySession.do"/>',
	            data : 'toolSessionID=' + toolSessionId,
	            type: 'post',
	            success: function (response) {
	            	button.parent().html('<fmt:message key="label.pad.fixed" />');
	            	alert('<fmt:message key="label.pad.fixed" />');
	            },
	            error: function (request, status, error) {
	            	button.unblock();
	                alert(request.responseText);
	            }
	       	});
		});
		
		//display countdown 
		if (${dokumaran.timeLimit > 0}) {
			displayCountdown();
		}
		
		$("#start-activity").click(function() {
			var button = $(this);
			button.hide();
			
	        $.ajax({
	        	async: true,
	            url: '<c:url value="/monitoring/launchTimeLimit.do"/>',
	            data : 'toolContentID=${sessionMap.toolContentID}',
	            type: 'post',
	            success: function (response) {
                	$.jGrowl(
                    	"<i class='fa fa-lg fa-floppy-o'></i> <fmt:message key='label.started.activity' />",
                    	{ 
                    		life: 2000, 
                    		closeTemplate: ''
                    	}
                    );
                	
                	//unpause countdown
                	$('#countdown').countdown('resume');
                	isCountdownStarted = true;
	            },
	            error: function (request, status, error) {
	            	button.show();
	                alert(request.responseText);
	            }
	       	});
	        
	        return false;
		});
		
		$("#add-one-minute").click(function() {
			var button = $(this);
			
	        $.ajax({
	        	async: true,
	            url: '<c:url value="/monitoring/addOneMinute.do"/>',
	            data : 'toolContentID=${sessionMap.toolContentID}',
	            type: 'post',
	            success: function (response) {
	            	var times = $("#countdown").countdown('getTimes');
	            	var secondsLeft = times[4]*3600 + times[5]*60 + times[6] + 60;
	            	if (isCountdownStarted) {
		            	$('#countdown').countdown('option', { until: '+' + secondsLeft + 'S' });
		            
		            //fixing countdown bug of not updating it in case if being paused 
	            	} else {
	            		$('#countdown').countdown('resume');
		            	$('#countdown').countdown('option', { until: '+' + secondsLeft + 'S' });
		            	$('#countdown').countdown('pause');
	            	}
	            	
	            },
	            error: function (request, status, error) {
	                alert(request.responseText);
	            }
	       	});
	        
	        return false;
		});

	});
	
	function displayCountdown() {		
		$('#countdown').countdown({
			until: '+${sessionMap.secondsLeft}S',
			format: 'hMS',
			compact: true,
			description: "<div id='countdown-label'><fmt:message key='label.time.left' /></div>",
			onTick: function(periods) {
				//check for 30 seconds
				if ((periods[4] == 0) && (periods[5] == 0) && (periods[6] <= 30)) {
					$('#countdown').css('color', '#FF3333');
				} else {
					$('#countdown').css('color', '#47bc23');
				}					
			},
			onExpiry: function(periods) {
			}
		});
		
		//pause countdown in case it hasn't been yet started
		if (${empty dokumaran.timeLimitLaunchedDate}) {
			$('#countdown').countdown('pause');
		}
	}
</script>

<div class="panel">
	<h4>
	    <c:out value="${sessionMap.dokumaran.title}" escapeXml="true"/>
	</h4>
	
	<c:out value="${sessionMap.dokumaran.description}" escapeXml="false"/>
	 
	<c:if test="${empty summaryList}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			 <fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:if>
	
	<!--For release marks feature-->
	<i class="fa fa-spinner" style="display:none" id="message-area-busy"></i>
	<div id="message-area"></div>

	<c:if test="${dokumaran.timeLimit > 0}">
		
	
		<div class="pull-right" id="time-limit-buttons">
			<div id="countdown"></div>
		
			<c:if test="${empty dokumaran.timeLimitLaunchedDate}">
				<a href="#nogo" class="btn btn-default btn-xs" id="start-activity">
					<fmt:message key="label.start.activity" />
				</a>		
			</c:if>
			
			<a href="#nogo" class="btn btn-default btn-xs" id="add-one-minute">
				<fmt:message key="label.plus.one.minute" />
			</a>	
		</div>
		
		<br>
	</c:if>
</div>

<c:if test="${sessionMap.isGroupedActivity}">
	<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
	
	<c:if test="${sessionMap.isGroupedActivity}">	
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${groupSummary.sessionId}">
        	<span class="panel-title collapsable-icon-left">
        		<a class="collapsed" role="button" data-toggle="collapse" href="#collapse${groupSummary.sessionId}" 
						aria-expanded="false" aria-controls="collapse${groupSummary.sessionId}" >
					<fmt:message key="monitoring.label.group" />&nbsp;${groupSummary.sessionName}
				</a>
			</span>
        </div>
        
        <div id="collapse${groupSummary.sessionId}" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="heading${groupSummary.sessionId}">
	</c:if>
	
	<c:choose>
		<c:when test="${groupSummary.sessionFaulty}">
		
			<div class="faulty-pad-container">
				<fmt:message key="label.cant.display.faulty.pad" />
				
				<a href="#nogo" class="btn btn-default btn-xs fix-faulty-pad" data-session-id="${groupSummary.sessionId}">
					<fmt:message key="label.recreate.faulty.pad" />
				</a>
			</div>
									
		</c:when>
		<c:otherwise>
			<div class="btn-group btn-group-xs pull-right">
				<c:url  var="exportHtmlUrl" value="${etherpadServerUrl}/p/${groupSummary.padId}/export/html"/>
				<a href="#nogo" onclick="window.location = '${exportHtmlUrl}';" class="btn btn-default btn-sm " 
						title="<fmt:message key="label.export.pad.html" />">
					<i class="fa fa-lg fa-file-text-o"></i>
					<fmt:message key="label.export.pad.html" />
				</a>
			</div>	
					
			<div id="etherpad-container-${groupSummary.sessionId}"></div>		
		</c:otherwise>
	</c:choose>
	
	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
	
</c:forEach>

<c:if test="${sessionMap.isGroupedActivity}">
	</div> <!--  end accordianSessions --> 
</c:if>
	
<c:if test="${sessionMap.dokumaran.reflectOnActivity}">
	<%@ include file="reflections.jsp"%>
</c:if>

<%@ include file="advanceoptions.jsp"%>
