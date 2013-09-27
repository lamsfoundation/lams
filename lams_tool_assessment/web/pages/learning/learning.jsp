<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>

	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="assessment" value="${sessionMap.assessment}" />
	<c:set var="pageNumber" value="${sessionMap.pageNumber}" />
	<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
	<c:set var="isResubmitAllowed" value="${sessionMap.isResubmitAllowed}" />
	<c:set var="result" value="${sessionMap.assessmentResult}" />
	<c:choose>
		<c:when test="${not empty param.secondsLeft}">
			<c:set var="secondsLeft" value="${param.secondsLeft - 1}" />		
		</c:when>
		<c:otherwise>
			<c:set var="secondsLeft" value="${assessment.timeLimit * 60}" />		
		</c:otherwise>
	</c:choose>	
	
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.countdown.css" />
	<style media="screen,projection" type="text/css">
		div.growlUI { background: url(check48.png) no-repeat 10px 10px }
		div.growlUI h1, div.growlUI h2 {
			color: white; padding: 5px 5px 5px 0px; text-align: center;
		}
	</style>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript">
	<!--	
		<c:if test="${not finishedLock && assessment.timeLimit > 0}">	
			$(document).ready(function(){
				if (${empty param.secondsLeft}) {
					$.blockUI({ message: $('#question'), css: { width: '325px', height: '85px'}, overlayCSS: { opacity: '.98'} }); 
			        $('#ok').click(function() {
			        	$.unblockUI();
			        	displayCountdown();
			        });					
				} else {
					displayCountdown();
				}
			});
			
			function displayCountdown(){
				var countdown = '<div id="countdown" style="width: 150px; position: absolute; font-size: 110%; font-style: italic; color:#47bc23;"></div>' 
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
						onTick: checkFor30Seconds,
						onExpiry: liftoffTime,
						description: "<div style='font-size: 170%; padding-top:5px; padding-bottom:5px; font-style: italic; color:#47bc23;' ><fmt:message key='label.learning.countdown.time.left' /></div>"
					});
			}	
			function checkFor30Seconds(periods) {
				if ((periods[4] == 0) && (periods[5] == 0) && (periods[6] <= 30)) {
					$('#countdown').css('color', '#FF3333');
				}
			}					
			function liftoffTime(){
		        $.blockUI({ message: '<h1 style="font-size: 145%;"><img src="<html:rewrite page='/includes/images/indicator.gif'/>" border="0" > <fmt:message key="label.learning.blockui.time.is.over" /></h1>' }); 
		        
		        setTimeout(function() { 
		            $.unblockUI({ 
		                onUnblock: submitAll 
		            }); 
		        }, 4000); 
			}	
		</c:if>
		
		//autosave feature
		<c:if test="${(!finishedLock) && (mode != 'teacher')}">
			var autosaveInterval = "30000"; // 30 seconds interval
			window.setInterval(
				function(){
					//copy value from CKEditor (only available in essay type of questions) to textarea before ajax submit
					$("span[id^=cke_question]").each(function() {
						var questionNumber = "" + this.id.substring("cke_question".length);
						
						$("textarea[id=question" + questionNumber + "]").each(function() {
							
							var ckeditorData = CKEDITOR.instances[this.name].getData();
							//skip out empty values
							this.value = ((ckeditorData == null) || (ckeditorData.replace(/&nbsp;| |<br \/>|\s|<p>|<\/p>|\xa0/g, "").length == 0)) ? "" : ckeditorData;
						});
					});
					
					//ajax form submit
					$('#answers').ajaxSubmit({
						url: "<c:url value='/learning/autoSaveAnswers.do'/>?sessionMapID=${sessionMapID}&date=" + new Date().getTime(),
		                success: function() {
		                	$.growlUI('<fmt:message key="label.learning.draft.autosaved" />');
		                }
					});
	        	}, autosaveInterval
	        );
		</c:if>
		
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		function nextPage(pageNumber){
			var secondsLeft = 0;
			if (${not finishedLock && assessment.timeLimit > 0}) {
				var times = $("#countdown").countdown('getTimes'); 
				secondsLeft = times[4]*3600 + times[5]*60 + times[6];
			}
        	var myForm = $("#answers");
        	myForm.attr("action", "<c:url value='/learning/nextPage.do?sessionMapID=${sessionMapID}&pageNumber='/>" + pageNumber + "&secondsLeft=" + secondsLeft);
        	myForm.submit();
		}
		function submitAll(){
        	var myForm = $("#answers");
        	myForm.attr("action", "<c:url value='/learning/submitAll.do?sessionMapID=${sessionMapID}'/>");
        	myForm.submit();
		}	
		function resubmit(){
			document.location.href ="<c:url value='/learning/resubmit.do?sessionMapID=${sessionMapID}'/>";
			return false;			
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
	-->        
    </script>
</lams:head>
<body class="stripes">

	<div id="content">
		<h1>
			${assessment.title}
		</h1>

		<c:if test="${not finishedLock}">
			<p>
				${assessment.instructions}
			</p>
		</c:if>
		
		<c:if test="${not empty sessionMap.submissionDeadline && (sessionMap.mode == 'author' || sessionMap.mode == 'learner')}">
			<div class="info">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
		</c:if>
		
		<c:if test="${sessionMap.isUserFailed}">
			<div class="warning">
				<fmt:message key="label.learning.havent.reached.passing.mark">
					<fmt:param>${assessment.passingMark}</fmt:param>
				</fmt:message>
			</div>
		</c:if>
		
		<div id="question" style="display:none; cursor: default"> 
	        <h1 style="padding: 30 10 50">
	        	<fmt:message key='label.learning.blockui.are.you.ready' />
	        </h1>
    	    <input type="button" id="ok" value="OK" />
    	    <br>  
		</div>
		
		<%@ include file="/common/messages.jsp"%>
		<br>
		
		<%@ include file="parts/attemptsummary.jsp"%>
		
		<c:if test="${!finishedLock || finishedLock && assessment.displaySummary}">
			<%@ include file="parts/allquestions.jsp"%>
		</c:if>
		
		<%-- Reflection entry --%>
		<c:if test="${sessionMap.reflectOn && sessionMap.userFinished && finishedLock}">
			<div class="small-space-top">
				<h2>
					${sessionMap.reflectInstructions}
				</h2>

				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em> <fmt:message key="message.no.reflection.available" />	</em>
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
						</p>
					</c:otherwise>
				</c:choose>

				<c:if test="${mode != 'teacher'}">
					<html:button property="FinishButton" onclick="return continueReflect()" styleClass="button">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right">
				<c:choose>
					<c:when	test="${not finishedLock}">					
						<html:button property="submitAll" onclick="return submitAll();" styleClass="button">
							<fmt:message key="label.learning.submit.all" />
						</html:button>	
					</c:when>
					<c:otherwise>
						<c:if test="${isResubmitAllowed}">
							<html:link href="javascript:;" property="resubmit" onclick="return resubmit()" styleClass="button">
								<fmt:message key="label.learning.resubmit" />
							</html:link>
						</c:if>	
						
						<c:if test="${! sessionMap.isUserFailed}">
						
							<c:choose>
								<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
									<html:button property="FinishButton" onclick="return continueReflect()" styleClass="button">
										<fmt:message key="label.continue" />
									</html:button>
								</c:when>
								<c:otherwise>
									<html:link href="#nogo" property="FinishButton" onclick="return finishSession()" styleClass="button">
										<span class="nextActivity">
											<c:choose>
							 					<c:when test="${sessionMap.activityPosition.last}">
							 						<fmt:message key="label.submit" />
							 					</c:when>
							 					<c:otherwise>
							 		 				<fmt:message key="label.finished" />
							 					</c:otherwise>
							 				</c:choose>
		 								</span>
									</html:link>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

	</div>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
