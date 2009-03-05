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

	<script type="text/javascript">
	<!--
		function finishSession(){
			//document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function nextPage(pageNumber){
        	var myForm = $("answers");
        	myForm.action = "<c:url value='/learning/nextPage.do?sessionMapID=${sessionMapID}&pageNumber='/>" + pageNumber;
        	myForm.submit();
		}		
		function finishTest(){
        	var myForm = $("answers");
        	myForm.action = "<c:url value='/learning/finishTest.do?sessionMapID=${sessionMapID}'/>";
        	myForm.submit();
		}
		function submitAll(){
        	var myForm = $("answers");
        	myForm.action = "<c:url value='/learning/submitAll.do?sessionMapID=${sessionMapID}'/>";
        	myForm.submit();
		}	
		function resubmit(){
			document.location.href ="<c:url value='/learning/resubmit.do?sessionMapID=${sessionMapID}'/>";
			return false;			
		}		
		var orderingTargetDiv = "orderingArea";
		function upOption(questionUid, idx){
			var url = "<c:url value="/learning/upOption.do"/>";
			var param = "sessionMapID=${sessionMapID}&optionIndex=" + idx + "&questionUid=" + questionUid;
		    var myAjax = new Ajax.Updater(
		    		orderingTargetDiv,
			    	url,
			    	{
			    		method:'get',
			    		parameters:param,
			    		evalScripts:false
			    	}
		    );
		}
		function downOption(questionUid, idx){
			var url = "<c:url value="/learning/downOption.do"/>";
			var param = "sessionMapID=${sessionMapID}&optionIndex=" + idx + "&questionUid=" + questionUid;
		    var myAjax = new Ajax.Updater(
		    		orderingTargetDiv,
			    	url,
			    	{
			    		method:'get',
			    		parameters:param,
			    		evalScripts:false
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

		<p>
			${assessment.instructions}
		</p>

		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
				<div class="info">
					<c:choose>
						<c:when test="${sessionMap.userFinished}">
							<fmt:message key="message.activityLocked" />
						</c:when>
						<c:otherwise>
							<fmt:message key="message.warnLockOnFinish" />
						</c:otherwise>
					</c:choose>
				</div>
		</c:if>

		<%@ include file="/common/messages.jsp"%>
		<br><br>
		
		<c:if test="${finishedLock}">
			<table class="forum" style="background:none; border: 1px solid #cacdd1; margin-bottom:60px; padding-top:0px; margin-bottom: 10px;" cellspacing="0">
				<tr>
					<th style="width: 130px; border-left: none; padding-top:0px; " >
						<fmt:message key="label.learning.summary.started.on" />
					</th>
					<td >
						<lams:Date value="${result.startDate}"/>
					</td>
				</tr>
				
				<tr>
					<th style="width: 130px;" >
						<fmt:message key="label.learning.summary.completed.on" />
					</th>
					<td>
						<lams:Date value="${result.finishDate}" />
					</td>
				</tr>
				<tr>
					<th style="width: 130px;" >
						<fmt:message key="label.learning.summary.time.taken" />
					</th>
					<td>
						<fmt:formatDate value="${result.timeTaken}" pattern="H" timeZone="GMT" /> <fmt:message key="label.learning.summary.hours" />
						<fmt:formatDate value="${result.timeTaken}" pattern="m" timeZone="GMT" /> <fmt:message key="label.learning.summary.minutes" />
					</td>
				</tr>
				<c:if test="${assessment.allowGradesAfterAttempt}">
					<tr>
						<th style="width: 130px;" >
							<fmt:message key="label.learning.summary.grade" />
						</th>
						<td>
							<fmt:formatNumber value="${result.grade}" maxFractionDigits="3"/>
							<fmt:message key="label.learning.summary.out.of.maximum" />
							${result.maximumGrade} (<fmt:formatNumber value="${result.grade * 100 / result.maximumGrade}" maxFractionDigits="2"/>%)
						</td>
					</tr>
				</c:if>
			</table>
		</c:if>			
		
		<form id="answers" name="answers" method="post" >
			<table cellspacing="0" class="alternative-color">
				<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}" varStatus="status">
					<tr>
						<td style="padding-left: 15px; vertical-align: middle; width: 10px;" >
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
							<a href="javascript:;" onclick="return nextPage(${status.index + 1})" style="margin-left: 10px; font-size: 130%; color: red;">
						</c:when>
						<c:otherwise>
							<a href="javascript:;" onclick="return nextPage(${status.index + 1})" style="margin-left: 10px; font-size: 130%;">
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
					<c:when	test="${(not finishedLock)}">
						<html:button property="finishTest" onclick="return finishTest();" styleClass="button">
							<fmt:message key="label.learning.finish.test" />
						</html:button>					
						<html:button property="submitAll" onclick="return submitAll();" styleClass="button">
							<fmt:message key="label.learning.submit.all" />
						</html:button>	
					</c:when>
					<c:otherwise>
						<c:if test="${isResubmitAllowed}">
							<html:button property="resubmit" onclick="return resubmit()" styleClass="button">
								<fmt:message key="label.learning.resubmit" />
							</html:button>				
						</c:if>					
						<html:button property="FinishButton" onclick="return finishSession()" styleClass="button">
							<fmt:message key="label.learning.next.activity" />
						</html:button>
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