
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.httpSessionID]}" />

<lams:html>
<lams:head>
	<html:base />
	<title><fmt:message key="activity.title" /></title>

	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	<link rel="stylesheet" href="${lams}css/jquery.jRating.css" />

	<script type="text/javascript"> 
		//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/";
	</script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.mobile.js"></script>	
	<script type="text/javascript">
	  	$(document).bind('pageinit', function(){
		    $(".rating-stars").jRating({
		    	phpPath : "<c:url value='/learning.do'/>?method=rateResponse&toolSessionID=" + $("#toolSessionID").val(),
		    	rateMax : 5,
		    	decimalLength : 1,
			  	onSuccess : function(data, responseUid){
			    	$("#averageRating" + responseUid).html(data.averageRating);
			    	$("#numberOfVotes" + responseUid).html(data.numberOfVotes);
				},
			  	onError : function(){
			    	jError('Error : please retry');
			  	}
			});
		    $(".rating-stars-disabled").jRating({
		    	rateMax : 5,
		    	isDisabled : true
		    });
		 });
	
		function submitLearningMethod(actionMethod) {
			if (actionMethod == 'endLearning') {
				$("#finishButton").attr("disabled", true);
			}
			document.QaLearningForm.method.value=actionMethod; 
			document.QaLearningForm.submit();
		}
		
		function submitMethod(actionMethod) {
			submitLearningMethod(actionMethod);
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
	
		<c:if test="${not empty sessionMap.submissionDeadline}">
			<div class="info">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
		</c:if>			

		<c:if test="${generalLearnerFlowDTO.requestLearningReportProgress != 'true'}">

			<h2>This is revisited
				<fmt:message key="label.other.answers" />
			</h2>

			<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">
				<html:hidden property="method" />
				<html:hidden property="toolSessionID" styleId="toolSessionID"/>
				<html:hidden property="userID" />
				<html:hidden property="httpSessionID" />
				<html:hidden property="totalQuestionCount" />

				<ul data-role="listview" data-theme="c" id="qaAnswers" >
				<c:forEach var="currentDto"	items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
					<c:set var="currentQuestionId" scope="request"	value="${currentDto.questionUid}" />

					<li>
						<p class="space-top">
							<strong> <fmt:message key="label.question" /> : </strong>
							<c:out value="${currentDto.question}" escapeXml="false" />
						</p>

						<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
							<c:forEach var="sData" items="${questionAttemptData.value}">
								<c:set var="userData" scope="request" value="${sData.value}" />
								<c:set var="responseUid" scope="request" value="${userData.uid}" />

								<c:if test="${generalLearnerFlowDTO.userUid == userData.queUsrId}">
										<c:if test="${currentQuestionId == userData.questionUid}">
											<p class="small-space-top">
												<span class="field-name"> <c:out
														value="${userData.userName}" /> </span> -
												<lams:Date value="${userData.attemptTime}" style="short"/>
											</p>
											<p class="space-bottom">
												<c:out value="${userData.responsePresentable}"
													escapeXml="false" />
											</p>
											<jsp:include page="parts/ratingStarsDisabled.jsp" />
										</c:if>
								</c:if>								
							</c:forEach>
						</c:forEach>
					</li>
				</c:forEach>
				</ul>
				
				<c:if test="${generalLearnerFlowDTO.existMultipleUserResponses == 'true'}">				
					<h2>
						<fmt:message key="label.other.answers" />
					</h2>
	
					<ul data-role="listview" data-theme="c" id="qaAnswers" >
						<c:forEach var="currentDto"	items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
							<c:set var="currentQuestionId" scope="request"	value="${currentDto.questionUid}" />
			
							<li>
								<p class="space-top">
									<strong> <fmt:message key="label.question" /> : Here -revisited </strong>
									<c:out value="${currentDto.question}" escapeXml="false" />
								</p>
			
									<c:forEach var="questionAttemptData"
										items="${currentDto.questionAttempts}">
										<c:forEach var="sData" items="${questionAttemptData.value}">
											<c:set var="userData" scope="request" value="${sData.value}" />
											<c:set var="responseUid" scope="request" value="${userData.uid}" />
		
										<c:if test="${generalLearnerFlowDTO.userUid != userData.queUsrId}">	
											<c:if test="${currentQuestionId == userData.questionUid}">
												<p class="small-space-top">
													<span class="field-name"> <c:out
															value="${userData.userName}" /> </span>
															
													<c:if test="${generalLearnerFlowDTO.userNameVisible == 'true'}">																			
																 -
														<lams:Date value="${userData.attemptTime}" style="short"/>
													</c:if>																						
												</p>
												<p class="small-space-bottom">
																									<c:choose>
													<c:when test="${userData.visible == 'true'}">
														<c:out value="${userData.responsePresentable}"
															escapeXml="false" />
														<jsp:include page="parts/ratingStars.jsp" />
													</c:when>
													<c:otherwise>
														<i><fmt:message key="label.hidden"/></i>
													</c:otherwise>
													</c:choose>
													<hr size=1> 												
												</p>
											</c:if>
										</c:if>									
										</c:forEach>
									</c:forEach>
							</li>
						</c:forEach>
					</ul>
				</c:if>				



			<c:if test="${generalLearnerFlowDTO.reflection == 'true' }">					
					<h2>
							<c:out value="${generalLearnerFlowDTO.reflectionSubject}" escapeXml="false" />												
					</h2>

					<c:out value="${QaLearningForm.entryText}" escapeXml="false" />
					
					<c:if test="${generalLearnerFlowDTO.lockWhenFinished == 'true' }">					
						<br>
						<span class="button-inside">
								<html:button property="forwardtoReflection" onclick="submitMethod('forwardtoReflection');"> 
									<fmt:message key="label.edit" />
								</html:button>
						</span>
					</c:if>													
			</c:if>																			


				<c:if test="${generalLearnerFlowDTO.requestLearningReportViewOnly != 'true' }">
					<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' }">
						<c:if test="${generalLearnerFlowDTO.lockWhenFinished != 'true'}">
							<br>
							<span class="button-inside">					
								<html:button property="redoQuestions" onclick="submitMethod('redoQuestions');">
									<fmt:message key="label.redo" />
								</html:button>
							</span>
						</c:if>						
					</c:if>
				</c:if>
			</html:form>
		</c:if>


		<c:if test="${generalLearnerFlowDTO.requestLearningReportProgress == 'true'}">

			<h2 class="space-top">
				<fmt:message key="label.learnerReport" />
			</h2>

			<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">
				<html:hidden property="method" />
				<html:hidden property="toolSessionID" styleId="toolSessionID"/>
				<html:hidden property="userID" />
				<html:hidden property="httpSessionID" />
				<html:hidden property="totalQuestionCount" />

				<ul data-role="listview" data-theme="c" id="qaAnswers" >
				<c:forEach var="currentDto" items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
					<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}" />

					<li>
						<p class="space-top">
							<strong> <fmt:message key="label.question" /> : </strong>
							<c:out value="${currentDto.question}" escapeXml="false" />
						</p>

						<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
							<c:forEach var="sData" items="${questionAttemptData.value}">
								<c:set var="userData" scope="request" value="${sData.value}" />
								<c:set var="responseUid" scope="request" value="${userData.uid}" />

								<c:if test="${generalLearnerFlowDTO.userUid == userData.queUsrId}">
										<c:if test="${currentQuestionId == userData.questionUid}">
											<p class="small-space-top">
												<span class="field-name"> <c:out value="${userData.userName}" /> </span> -
												<lams:Date value="${userData.attemptTime}" style="short"/>
											</p>
											<p class="space-bottom">
												<c:out value="${userData.responsePresentable}" escapeXml="false" />
											</p>
											<jsp:include page="parts/ratingStarsDisabled.jsp" />
										</c:if>
								</c:if>								
							</c:forEach>
						</c:forEach>
					</li>
				</c:forEach>
				</ul>
				
				<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' }">
					<c:if test="${generalLearnerFlowDTO.lockWhenFinished != 'true'}">
						<br>
						<span class="button-inside">
							<html:button property="redoQuestions" styleClass="button" onclick="submitMethod('redoQuestions');">
								<fmt:message key="label.redo" />
							</html:button>
						</span>
					</c:if>						
				</c:if>
				
				<c:if test="${generalLearnerFlowDTO.existMultipleUserResponses == 'true'}">				
					<h2>
						<fmt:message key="label.other.answers" />
					</h2>
					
					<ul data-role="listview" data-theme="c" id="qaAnswers" >
					<c:forEach var="currentDto"	items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
						<c:set var="currentQuestionId" scope="request"	value="${currentDto.questionUid}" />
			
						<li>
									<p class="space-top"> 
										<strong> <fmt:message key="label.question" /> : </strong>
										<c:out value="${currentDto.question}" escapeXml="false" />
									</p>
			
									<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
										<c:forEach var="sData" items="${questionAttemptData.value}">
											<c:set var="userData" scope="request" value="${sData.value}" />
											<c:set var="responseUid" scope="request" value="${userData.uid}" />
		
										<c:if test="${generalLearnerFlowDTO.userUid != userData.queUsrId}">	
											<c:if test="${currentQuestionId == userData.questionUid}">
												<p class="small-space-top">
													<span class="field-name"> <c:out value="${userData.userName}" /> </span>
															
													<c:if test="${generalLearnerFlowDTO.userNameVisible == 'true'}">																			
																 -
														<lams:Date value="${userData.attemptTime}" style="short"/>
													</c:if>																						
												</p>
												<p class="small-space-bottom">
													<c:choose>
													<c:when test="${userData.visible == 'true'}">
														<c:out value="${userData.responsePresentable}"
															escapeXml="false" />
														<jsp:include page="parts/ratingStars.jsp" />
													</c:when>
													<c:otherwise>
														<i><fmt:message key="label.hidden"/></i>
													</c:otherwise>
													</c:choose>												
												</p>
											</c:if>
										</c:if>									
										</c:forEach>
									</c:forEach>
							</li>
						</c:forEach>
					</ul>
				</c:if>				


			<c:if test="${generalLearnerFlowDTO.reflection == 'true' }">					
					<h2>
						<c:out value="${generalLearnerFlowDTO.reflectionSubject}" escapeXml="false" />						
					</h2>

					<p><c:out value="${QaLearningForm.entryText}" escapeXml="false" /></p>

					<span class="button-inside">
						<html:button property="forwardtoReflection"	onclick="submitMethod('forwardtoReflection');"> 
							<fmt:message key="label.edit" />
						</html:button>
					</span>	
			</c:if>													
						
			</html:form>
		</c:if>
	</div>
	
	<div data-role="footer" data-theme="b" class="ui-bar">
		<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' }">
			<span class="ui-finishbtn-right">
				<button name="endLearning" id="finishButton" onclick="javascript:submitMethod('endLearning');" data-icon="arrow-r" data-theme="b">
					<fmt:message key="button.endLearning" />
				</button>
			</span>
		</c:if>
	</div><!-- /footer -->

</div>
</body>
</lams:html>
