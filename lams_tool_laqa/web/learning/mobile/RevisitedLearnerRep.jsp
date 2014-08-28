
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
	
	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	<link rel="stylesheet" href="${lams}css/jquery.jRating.css" />
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme-blue.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	<style media="screen,projection" type="text/css">
		.rating-stars-div {margin-top: 8px;}
		.user-answer {padding: 7px 2px;}
		tr.odd:hover .jStar {background-image: url(${lams}images/css/jquery.jRating-stars-grey.png)!important;}
		tr.even:hover .jStar {background-image: url(${lams}images/css/jquery.jRating-stars-light-grey.png)!important;}
		tr.odd .jStar {background-image: url(${lams}images/css/jquery.jRating-stars-light-blue.png)!important;}
		.tablesorter-blue {margin-bottom: 5px;}
		.pager {padding-bottom: 20px; width: 150px;}
	</style>

	<script type="text/javascript"> 
		//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/";
	</script>
	<script src="${lams}includes/javascript/jquery.js" type="text/javascript" ></script>
	<script src="${lams}includes/javascript/jquery.jRating.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter.js"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-pager.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js" type="text/javascript" ></script>	>	
	<script type="text/javascript">
	  	$(document).bind('pageinit', function(){
	  		
			$(".tablesorter").tablesorter({
				theme: 'blue',
			    widthFixed: true,
			    widgets: ['zebra'],
			    headers : { 1 : { sorter: false } }
			});
			
			$(".tablesorter").each(function() {
				$(this).tablesorterPager({
					// set to false otherwise it remembers setting from other jsFiddle demos
					savePages: false,
				      // use this format: "http:/mydatabase.com?page={page}&size={size}&{sortList:col}"
				      // where {page} is replaced by the page number (or use {page+1} to get a one-based index),
				      // {size} is replaced by the number of records to show,
				      // {sortList:col} adds the sortList to the url into a "col" array, and {filterList:fcol} adds
				      // the filterList to the url into an "fcol" array.
				      // So a sortList = [[2,0],[3,0]] becomes "&col[2]=0&col[3]=0" in the url
				      // and a filterList = [[2,Blue],[3,13]] becomes "&fcol[2]=Blue&fcol[3]=13" in the url
					ajaxUrl : "<c:url value='/learning.do'/>?method=getResponses&page={page}&size={size}&{sortList:column}&qaSessionId=" + $("#toolSessionID").val() + "&questionUid=" + $(this).attr('data-question-uid') + "&userId=" + $("#userID").val(),

				      // modify the url after all processing has been applied
				   //   customAjaxUrl: function(table, url) {
				          // manipulate the url string as you desire
				          // url += '&cPage=' + window.location.pathname;
				          // trigger my custom event
				          //$(table).trigger('changingUrl', url);
				          // send the server the current page
				       //   return url;
				     // },
					ajaxProcessing: function (data) {
				    	if (data && data.hasOwnProperty('rows')) {
				    		var rows = [],
				            json = {};
				    		
							for (i = 0; i < data.rows.length; i++){
								var userData = data.rows[i];
								
								rows += '<tr>';
								rows += '<td>';
								
								if (${generalLearnerFlowDTO.userNameVisible == 'true'}) {
									rows += '<div>';
									rows += 	'<span class="field-name">';
									rows += 		userData["userName"];
									rows += 	'</span> ';
									rows += 	userData["attemptTime"];
									rows += '</div>';
								}
								
								rows += 	'<div class="user-answer">';
								if (userData["visible"] == 'true') {
									rows += 	userData["answer"];
								} else {
									rows += 	'<i><fmt:message key="label.hidden"/></i>';
								}
								rows += 	'</div>';
								
								rows += '</td>';
								
								if (${generalLearnerFlowDTO.allowRateAnswers == 'true'}) {
									rows += '<td style="width:50px;">';
									
									if (userData["visible"] == 'true') {
										var responseUid = userData["responseUid"];
										
										rows += '<div class="rating-stars-div">';
										rows += 	'<div class="rating-stars" data-average="' + userData["averageRating"] +'" data-id="' + responseUid + '"></div>';
										rows += 	'<div class="rating-stars-caption">';
										rows += 		'<span id="averageRating' + responseUid + '">' + userData["averageRating"] +'</span> / ';
										rows += 		'<span id="numberOfVotes' + responseUid + '">' + userData["numberOfVotes"] +'</span> ';
										rows += 		'<fmt:message key="label.votes" />';
										rows += 	'</div>';
										rows += '</div>';
										rows += '<div style="clear: both;"></div>';
									}									
									
									rows += '</td>';
								}	
								
								rows += '</tr>';
							}
				            
							json.total = data.total_rows; // only allow 100 rows in total
							//json.filteredRows = 100; // no filtering
							json.rows = $(rows);
							return json;
				            
				    	}
					},
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
				})
				
				// bind to pager events
				.bind('pagerInitialized pagerComplete', function(event, options){
				    $(".rating-stars").jRating({
				    	phpPath : "<c:url value='/learning.do'/>?method=rateResponse&toolSessionID=" + $("#toolSessionID").val(),
				    	rateMax : 5,
				    	decimalLength : 1,
					  	onSuccess : function(data, responseUid){
					    	$("#averageRating" + responseUid).html(data.averageRating);
					    	$("#numberOfVotes" + responseUid).html(data.numberOfVotes);
					    	$("#averageRating" + responseUid).parents(".tablesorter").trigger("update");
						},
					  	onError : function(){
					    	jError('Error : please retry');
					  	}
					});
				});
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
		
		<c:if test="${isLeadershipEnabled}">
			<h4>
				<fmt:message key="label.group.leader" >
					<fmt:param>${sessionMap.groupLeader.fullname}</fmt:param>
				</fmt:message>
			</h4>
		</c:if>

		<h2 class="space-top">
			<fmt:message key="label.learnerReport" />
		</h2>

		<ul data-role="listview" data-theme="c" id="qaAnswers" >
			<c:forEach var="currentDto" items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}" varStatus="status">
				<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}" />

				<li>
					<p class="space-top">
						<strong> <fmt:message key="label.question" /> ${status.count}: </strong>
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
										<p class="space-bottom user-answer">
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
			<c:if test="${(generalLearnerFlowDTO.lockWhenFinished != 'true') && hasEditRight}">
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
				<c:forEach var="question" items="${generalLearnerFlowDTO.questions}" varStatus="status">			
			
					<li>
						<p class="space-top"> 
							<strong> <fmt:message key="label.question" /> ${status.count}: </strong>
							<c:out value="${question.question}" escapeXml="false" />
						</p>
						
						<table class="tablesorter" data-question-uid="${question.uid}">
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
								
					</li>
				</c:forEach>
			</ul>
		</c:if>				


		<c:if test="${generalLearnerFlowDTO.reflection == 'true' }">					
			<h2>
				<lams:out value="${generalLearnerFlowDTO.reflectionSubject}" escapeHtml="true" />
			</h2>

			<p><lams:out value="${QaLearningForm.entryText}" escapeHtml="true" /></p>

			<c:if test="${hasEditRight}">
				<span class="button-inside">
					<html:button property="forwardtoReflection"	onclick="submitMethod('forwardtoReflection');"> 
						<fmt:message key="label.edit" />
					</html:button>
				</span>	
			</c:if>
		</c:if>		

		<html:form action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">
			<html:hidden property="method" />
			<html:hidden property="toolSessionID" styleId="toolSessionID"/>
			<html:hidden property="userID" styleId="userID"/>
			<html:hidden property="httpSessionID" />
			<html:hidden property="totalQuestionCount" />	
		</html:form>
		
	</div>
	
	<div data-role="footer" data-theme="b" class="ui-bar">
		<c:if test="${generalLearnerFlowDTO.teacherViewOnly != 'true' }">
			<span class="ui-finishbtn-right">
				<button name="endLearning" id="finishButton" onclick="javascript:submitMethod('endLearning');" data-icon="arrow-r" data-theme="b">
					<c:choose>
	 					<c:when test="${sessionMap.activityPosition.last}">
	 						<fmt:message key="button.submit" />
	 					</c:when>
	 					<c:otherwise>
	 		 				<fmt:message key="button.endLearning" />
	 					</c:otherwise>
	 				</c:choose>
				</button>
			</span>
		</c:if>
	</div><!-- /footer -->

</div>
</body>
</lams:html>
