<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.httpSessionID]}" scope="request"/>
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" scope="request"/>
<c:set var="mode" value="${sessionMap.mode}" scope="request"/>
<c:set var="isLeadershipEnabled" value="${sessionMap.content.useSelectLeaderToolOuput}" scope="request"/>
<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" scope="request"/>

<lams:html>
<lams:head>
	<html:base />
	<title><fmt:message key="activity.title" /></title>
	
	<style media="screen,projection" type="text/css">
		div.growlUI { background: url(check48.png) no-repeat 10px 10px }
		div.growlUI h1, div.growlUI h2 {
			color: white; padding: 5px 5px 5px 0px; text-align: center;
		}
	</style>
	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	
	<script src="${lams}includes/javascript/jquery.js"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js"></script>	
 	<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
 	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>	
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script language="JavaScript" type="text/JavaScript">

		
		//autoSaveAnswers if hasEditRight
		if (${hasEditRight}) {
				
			var interval = "3000"; // = 30 seconds
			window.setInterval(
				function(){
			               	$.growlUI('<fmt:message key="label.learning.draft.autosaved" />');
		       	}, interval
		   );
		}
		
	</script>
</lams:head>

<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${generalLearnerFlowDTO.activityTitle}" escapeXml="false" />
		</h1>
	</div><!-- /header -->

	<div data-role="content">	
		

		<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self"  styleId="learningForm">
			<c:choose>
				<c:when test="${generalLearnerFlowDTO.questionListingMode == 'questionListingModeSequential'}">
					<html:hidden property="method" value="getNextQuestion"/>
				</c:when>
				<c:otherwise>
					<html:hidden property="method" value="submitAnswersContent"/>
				</c:otherwise>
			</c:choose>
				
			<html:hidden property="toolSessionID" styleId="tool-session-id" />
			<html:hidden property="userID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="questionIndex" />
			<html:hidden property="totalQuestionCount" />
			
			<p>
				<c:out value="${generalLearnerFlowDTO.activityInstructions}" escapeXml="false" />
			</p>
			
			<ul data-role="listview"  >
			<c:forEach var="dto" varStatus="status"	items="${generalLearnerFlowDTO.mapAnswers}">
			
				<li>
							<p class="space-top">
							 	asdasdasdasd
							</p>
								
							<p class="small-space-top">
								<strong><fmt:message key="label.answer" /></strong>
							</p>
								
							<div class="small-space-bottom">
										<lams:textarea name="answeraaa" rows="5" cols="60" class="text-area" ><c:out value='aaaa' escapeXml='false' /></lams:textarea>
			
			
							</div>
				</li>
			</c:forEach>
			</ul>
		</html:form>
			
			</div><!-- /page div -->
			
			<div data-role="footer" data-theme="b">
				<h2>&nbsp;</h2>
			</div><!-- /footer -->


</div>
</body>
</lams:html>
