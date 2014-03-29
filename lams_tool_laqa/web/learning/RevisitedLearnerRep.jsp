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
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="isLeadershipEnabled" value="${sessionMap.content.useSelectLeaderToolOuput}" />
<c:set var="hasEditRight" value="${!isLeadershipEnabled || isLeadershipEnabled && isUserLeader}" />

<lams:html>
<lams:head>
	<html:base />
	<title><fmt:message key="activity.title" /></title>
	
	<lams:css />
	<link rel="stylesheet" href="${lams}css/jquery.jRating.css"/>
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme-blue.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
	<style media="screen,projection" type="text/css">
		.rating-stars-div {margin-top: 8px;}
		.user-answer {padding: 7px 2px;}
		tr.odd:hover .jStar {background-image: url(${lams}images/css/jquery.jRating-stars-grey.png)!important;}
		tr.even:hover .jStar {background-image: url(${lams}images/css/jquery.jRating-stars-light-grey.png)!important;}
		tr.odd .jStar {background-image: url(${lams}images/css/jquery.jRating-stars-light-blue.png)!important;}
		.tablesorter-blue {margin-bottom: 5px;}
		.pager {padding-bottom: 20px;}
	</style>

	<script type="text/javascript"> 
		//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/"; 
	</script>
	<script src="${lams}includes/javascript/jquery.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.jRating.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-pager.js" type="text/javascript"></script>
	<script type="text/javascript">
	
	  	$(document).ready(function(){
	  		
			$(".tablesorter").tablesorter({
				theme: 'blue',
			    widthFixed: true,
			    widgets: ['zebra']
			});
			
			$(".tablesorter").each(function() {
				$(this).tablesorterPager({
				    container: $(this).next(".pager"),
				    output: '{startRow} to {endRow} ({totalRows})',// possible variables: {page}, {totalPages}, {filteredPages}, {startRow}, {endRow}, {filteredRows} and {totalRows}
				    // if true, the table will remain the same height no matter how many records are displayed. The space is made up by an empty
				    // table row set to a height to compensate; default is false
				    fixedHeight: true,
				    // remove rows from the table to speed up the sort of large tables.
				    // setting this to false, only hides the non-visible rows; needed if you plan to add/remove rows with the pager enabled.
				    removeRows: false,
				    // css class names of pager arrows
				    cssNext: '.tablesorter-next', // next page arrow
					cssPrev: '.tablesorter-prev', // previous page arrow
					cssFirst: '.tablesorter-first', // go to first page arrow
					cssLast: '.tablesorter-last', // go to last page arrow
					cssGoto: '.gotoPage', // select dropdown to allow choosing a page
					cssPageDisplay: '.pagedisplay', // location of where the "output" is displayed
					cssPageSize: '.pagesize', // page size selector - select dropdown that sets the "size" option
					// class added to arrows when at the extremes (i.e. prev/first arrows are "disabled" when on the first page)
					cssDisabled: 'disabled' // Note there is no period "." in front of this class name
				});
			});
	  		
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

<body class="stripes">

	<div id="content">
		<h1>
			<c:out value="${generalLearnerFlowDTO.activityTitle}" escapeXml="true" />
		</h1>
		
		<c:if test="${not empty sessionMap.submissionDeadline}">
			<div class="info">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
				</fmt:message>
			</div>
		</c:if>	
		
		<c:if test="${isLeadershipEnabled}">
			<h4>
				<fmt:message key="label.group.leader" >
					<fmt:param>${sessionMap.groupLeader.fullname}</fmt:param>
				</fmt:message>
			</h4>
		</c:if>		

		<h2>
			<fmt:message key="label.learnerReport" />
		</h2>

		<c:forEach var="currentDto" items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
			<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}" />

			<div class="shading-bg">
				<p>
					<strong> <fmt:message key="label.question" /> : </strong>
					<c:out value="${currentDto.question}" escapeXml="false" />
				</p>

				<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
					<c:forEach var="sData" items="${questionAttemptData.value}">
						<c:set var="userData" scope="request" value="${sData.value}" />
						<c:set var="responseUid" scope="request" value="${userData.uid}" />

						<c:if test="${generalLearnerFlowDTO.userUid == userData.queUsrId}">
		
							<c:if test="${currentQuestionId == userData.questionUid}">
								<p>
									<span class="field-name"> 
										<c:out value="${userData.userName}" /> 
									</span> -
									<lams:Date value="${userData.attemptTime}" />
								</p>
								<p class="user-answer">
									<c:out value="${userData.responsePresentable}" escapeXml="false" />
								</p>
								<jsp:include page="parts/ratingStarsDisabled.jsp" />
							</c:if>
						</c:if>								
					</c:forEach>
				</c:forEach>
			</div>
		</c:forEach>
				
		<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' }">
			<c:if test="${(generalLearnerFlowDTO.lockWhenFinished != 'true') && hasEditRight}">
				<br>
				<html:button property="redoQuestions" styleClass="button" onclick="submitMethod('redoQuestions');">
					<fmt:message key="label.redo" />
				</html:button>
			</c:if>						
		</c:if>
				
		<c:if test="${generalLearnerFlowDTO.existMultipleUserResponses == 'true'}">				
			<h2>
				<fmt:message key="label.other.answers" />
			</h2>
	
			<c:forEach var="currentDto" items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
				<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}" />
			
				<p>
					<strong> <fmt:message key="label.question" /> : </strong>
					<c:out value="${currentDto.question}" escapeXml="false" />
				</p>
						
				<table class="tablesorter">
					<thead>
						<tr>
							<th title="<fmt:message key='label.sort.by.answer'/>" >
								<fmt:message key="label.learning.answer" />
							</th>
							<c:if test="${generalLearnerFlowDTO.allowRateAnswers == 'true'}">
								<th title="<fmt:message key='label.sort.by.rating'/>">
									<fmt:message key="label.learning.rating" />
								</th>
							</c:if>
						</tr>
					</thead>
					<tbody>
			
						<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
							<c:forEach var="sData" items="${questionAttemptData.value}">
								<c:set var="userData" scope="request" value="${sData.value}" />
								<c:set var="responseUid" scope="request" value="${userData.uid}" />
		
								<c:if test="${generalLearnerFlowDTO.userUid != userData.queUsrId}">	
									<c:if test="${currentQuestionId == userData.questionUid}">
										<tr>
											<td>									
												<div>
													<span class="field-name"> <c:out value="${userData.userName}" /> </span>
															
													<c:if test="${generalLearnerFlowDTO.userNameVisible == 'true'}">																			
																 -
														<lams:Date value="${userData.attemptTime}" />
													</c:if>																						
												</div>
												<div class="user-answer">
													<c:choose>
													<c:when test="${userData.visible == 'true'}">
														<c:out value="${userData.responsePresentable}" escapeXml="false" />
													</c:when>
													<c:otherwise>
														<i><fmt:message key="label.hidden"/></i>
													</c:otherwise>
													</c:choose>												
												</div>
											</td>
											
											<c:if test="${generalLearnerFlowDTO.allowRateAnswers == 'true'}">
												<td style="width:50px;">
													<c:if test="${userData.visible == 'true'}">	
														<jsp:include page="parts/ratingStars.jsp" />
													</c:if>	
												</td>
											</c:if>
										</tr>
									</c:if>
								</c:if>									
							</c:forEach>
						</c:forEach>
					</tbody>
				</table>
				
				<!-- pager -->
				<div class="pager">
					<form>
					   	<img class="tablesorter-first"/>
				    	<img class="tablesorter-prev"/>
				    	<span class="pagedisplay"></span> <!-- this can be any element, including an input -->
				   		<img class="tablesorter-next"/>
				    	<img class="tablesorter-last"/>
				    	<select class="pagesize">
				      		<option selected="selected" value="10">10</option>
				      		<option value="20">20</option>
				      		<option value="30">30</option>
				      		<option value="40">40</option>
				      		<option value="50">50</option>
				      		<option value="100">100</option>
				    	</select>
				  </form>
				</div>
				
			</c:forEach>
		</c:if>

		<c:if test="${generalLearnerFlowDTO.reflection == 'true' }">					
			<h2>
				<lams:out value="${generalLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />
			</h2>

			<p><c:out value="${QaLearningForm.entryText}" escapeXml="true" /></p>
			
			<c:if test="${hasEditRight}">
				<html:button property="forwardtoReflection" styleClass="button" onclick="submitMethod('forwardtoReflection');"> 
					<fmt:message key="label.edit" />
				</html:button>
			</c:if>
		</c:if>													
				
		<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' }">
			<div class="space-bottom-top align-right">
				<html:button property="endLearning" styleId="finishButton" onclick="javascript:submitMethod('endLearning');" styleClass="button">
					<c:choose>
	 					<c:when test="${sessionMap.activityPosition.last}">
	 						<fmt:message key="button.submit" />
	 					</c:when>
	 					<c:otherwise>
	 		 				<fmt:message key="button.endLearning" />
	 					</c:otherwise>
	 				</c:choose>
				</html:button>
			</div>
		</c:if>
			
		<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">
			<html:hidden property="method" />
			<html:hidden property="toolSessionID" styleId="toolSessionID"/>
			<html:hidden property="userID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="totalQuestionCount" />						
		</html:form>
		
	</div>
	<div id="footer"></div>
</body>
</lams:html>
