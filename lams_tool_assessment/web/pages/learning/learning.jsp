<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
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
	
	<link rel="stylesheet" type="text/css" href="<html:rewrite page='/includes/css/jquery.countdown.css'/>" />

	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery-1.2.6.pack.js'/>"></script> 
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.countdown.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.blockUI.js'/>"></script>
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
		
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
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
		function finishTest(){
        	var myForm = $("#answers");
        	myForm.attr("action", "<c:url value='/learning/finishTest.do?sessionMapID=${sessionMapID}'/>");
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

		<form id="answers" name="answers" method="post" >
			<table cellspacing="0" class="alternative-color">
				<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}" varStatus="status">
					<tr>
						<td style="padding: 15px 15px 15px; width: 10px; font-weight: bold;" >
							${status.index + sessionMap.questionNumberingOffset} 
						</td>
						<td style="padding-left: 0px;">
							<input type="hidden" name="questionUid${status.index}" id="questionUid${status.index}" value="${question.uid}" />						
							
							<div class="field-name" style="padding: 10px 15px 15px;">
								${question.question}
							</div>
							
							<c:choose>
								<c:when test="${question.type == 1}">
									<%@ include file="parts/multiplechoice.jsp"%>
								</c:when>
								<c:when test="${question.type == 2}">
									<%@ include file="parts/matchingpairs.jsp"%>
								</c:when>
								<c:when test="${question.type == 3}">
									<%@ include file="parts/shortanswer.jsp"%>
								</c:when>
								<c:when test="${question.type == 4}">
									<%@ include file="parts/numerical.jsp"%>
								</c:when>
								<c:when test="${question.type == 5}">
									<%@ include file="parts/truefalse.jsp"%>
								</c:when>
								<c:when test="${question.type == 6}">
									<%@ include file="parts/essay.jsp"%>
								</c:when>
								<c:when test="${question.type == 7}">
									<%@ include file="parts/ordering.jsp"%>
								</c:when>
							</c:choose>
							
							<%@ include file="parts/questionsummary.jsp"%>
						</td>
					</tr>
				</c:forEach>
			</table>
		</form>
		
		<!--Paging-->
		<c:if test="${fn:length(sessionMap.pagedQuestions) > 1}">
			<div style="text-align: center; padding-top: 60px;">
				<fmt:message key="label.learning.page" />
				<c:forEach var="questions" items="${sessionMap.pagedQuestions}" varStatus="status">
					<c:choose>
						<c:when	test="${(status.index+1) == pageNumber}">
							<a href="#" onclick="return nextPage(${status.index + 1})" style="margin-left: 10px; font-size: 130%; color: red;">
						</c:when>
						<c:otherwise>
							<a href="#" onclick="return nextPage(${status.index + 1})" style="margin-left: 10px; font-size: 130%;">
						</c:otherwise>
					</c:choose>				
						${status.index + 1} 
					</a>
				</c:forEach>		
			</div>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="space-bottom-top align-right">
				<c:choose>
					<c:when	test="${not finishedLock}">
						<html:button property="finishTest" onclick="return finishTest();" styleClass="button">
							<fmt:message key="label.learning.finish.test" />
						</html:button>					
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
						<html:link href="#" property="FinishButton" onclick="return finishSession()" styleClass="button">
							<span class="nextActivity"><fmt:message key="label.learning.next.activity" /></span>
						</html:link>
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
