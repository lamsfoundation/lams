<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
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
	<%@ include file="/common/header.jsp"%>
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.countdown.css" />
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.jgrowl.css" />
	<link rel="stylesheet" type="text/css" href="<html:rewrite page='/includes/css/scratchie-learning.css'/>" />

	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jgrowl.js"></script>	
	<script type="text/javascript">

		function scratchItem(itemUid, answerUid){
			var id = '-' + itemUid + '-' + answerUid;
			
	        $.ajax({
	            url: '<c:url value="/learning/recordItemScratched.do"/>',
	            data: 'sessionMapID=${sessionMapID}&answerUid=' + answerUid,
	            dataType: 'json',
	            type: 'post',
	            success: function (json) {
	            	if (json == null) {
	            		return false;
	            	}
	            	
	            	if (json.answerCorrect) {
	            		//show animation
	            		$('#image' + id).attr("src", "<html:rewrite page='/includes/images/scratchie-correct-animation.gif'/>?reqID=" + (new Date()).getTime());
	            		
	            		//disable scratching
	            		$("[id^=imageLink-" + itemUid + "]").removeAttr('onclick'); 
	            		$("[id^=imageLink-" + itemUid + "]").css('cursor','default');
		            	$("a[id^=imageLink-" + itemUid + "]").not('#imageLink' + id).fadeTo(1300, 0.3);

	            	} else {
	            		
	            		//show animation, disable onclick
	            		$('#image' + id).attr("src", "<html:rewrite page='/includes/images/scratchie-wrong-animation.gif'/>?reqID=" + (new Date()).getTime());
	            		$('#imageLink' + id).removeAttr('onclick');
	            		$('#imageLink' + id).css('cursor','default');
	            	}
	            }
	       	});
        	
        	return false;
		}

		//time limit feature
		<c:if test="${isTimeLimitEnabled}">
			$(document).ready(function(){
			
				//show timelimit-start-dialog in order to start countdown
				if (${isTimeLimitNotLaunched}) {
				
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
				        $.blockUI({ message: '<h1 id="timelimit-expired"><img src="<html:rewrite page='/includes/images/indicator.gif'/>" border="0" > <fmt:message key="label.time.is.over" /></h1>' }); 
				        
				        setTimeout(function() { 
				        	finish(true);
				        }, 4000); 
					},
					description: "<div id='countdown-label'><fmt:message key='label.countdown.time.left' /></div>"
				});
			}
		</c:if>

		function finish(isTimelimitExpired) {
			var method = $("#method").val();
			
			var proceed = true;
			//ask for leave confirmation only if time limit is not expired
			if (!isTimelimitExpired) {
				var numberOfAvailableScratches = $("[id^=imageLink-][onclick]").length;
				proceed = (numberOfAvailableScratches > 0) ? confirm("<fmt:message key="label.one.or.more.questions.not.completed"></fmt:message>") : true;	
			}
			
			if (proceed) {
				document.getElementById("finishButton").disabled = true;
				document.location.href ='<c:url value="/learning/' + method + '.do?sessionMapID=${sessionMapID}"/>';	
			}
			
			return false;
		}
		
		var refreshIntervalId = null;
		if (${!isUserLeader && mode != "teacher"}) {
			refreshIntervalId = setInterval("refreshQuestionList();",3000);// Auto-Refresh every 3 seconds
		}
		
		function refreshQuestionList() {
			var url = "<c:url value="/learning/refreshQuestionList.do"/>",
				scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
			
			$("#questionListArea").load(
				url,
				{
					sessionMapID: "${sessionMapID}",
					reqId: (new Date()).getTime()
				},
				function(){
					$("html,body").scrollTop(scrollTop);
				}
			);
		}
		
    </script>
</lams:head>
<body class="stripes">

	<div id="content">
		<h1>
			<c:out value="${scratchie.title}" escapeXml="true"/>
		</h1>
		
		<h4>
			<fmt:message key="label.group.leader" >
				<fmt:param><c:out value="${sessionMap.groupLeaderName}" escapeXml="true"/></fmt:param>
			</fmt:message>
		</h4>

		<p style="font-style: italic;">
			<c:out value="${scratchie.instructions}" escapeXml="false"/>
		</p>
		
		<c:if test="${not empty sessionMap.submissionDeadline}">
			<div class="info">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div id="timelimit-start-dialog"> 
		        <h4>
		        	<fmt:message key='label.are.you.ready' />
		        </h4>
		        <html:button property="ok" styleId="timelimit-start-ok" styleClass="button">
					<fmt:message key='label.ok' />
				</html:button>
			</div>
		</c:if>

		<%@ include file="/common/messages.jsp"%>

		<div id="questionListArea">
			<%@ include file="questionlist.jsp"%>
		</div>

	</div>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
