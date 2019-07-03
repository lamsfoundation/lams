<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
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
	<link href="<lams:WebAppURL/>includes/css/scratchie.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.countdown.css" />
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.jgrowl.css" />
	<link rel="stylesheet" type="text/css" href="${lams}css/circle.css" />
	<link rel="stylesheet" type="text/css" href="<lams:WebAppURL/>includes/css/scratchie-learning.css" />
	<style type="text/css">

	</style>

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

		$(document).ready(function(){
			//initialize tooltips showing user names next to confidence levels
			$('[data-toggle="tooltip"]').tooltip();
		});
	
		function scratchImage(itemUid, optionUid, isCorrect) {
			// first show animation, then put static image
			var imageSuffix = isCorrect ? 'correct' : 'wrong';
	    		$('#image-' + itemUid + '-' + optionUid).load(function(){
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

		function scratchItem(itemUid, optionUid){
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
				var numberOfAvailableScratches = $("[id^=imageLink-][onclick]").length;
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
