<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.lamsfoundation.lams.tool.qa.QaAppConstants"%>

<c:set var="sessionMap" value="${sessionScope[generalLearnerFlowDTO.httpSessionID]}" />
<c:set var="qaContent" value="${content}" />

    <% 
		Set tabs = new LinkedHashSet();
		tabs.add("label.summary");
		tabs.add("label.editActivity");
		tabs.add("label.stats");
		pageContext.setAttribute("tabs", tabs);
	%>

	<lams:html>
	<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
		
	<!-- ********************  CSS ********************** -->
	<c:choose>
		<c:when test="${not empty localLinkPath}">
			<lams:css localLinkPath="${localLinkPath}" style="tabbed"/>
		</c:when>
		<c:otherwise>
			<lams:css  style="tabbed"/>
		</c:otherwise>
	</c:choose>

	<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">
	<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">
	<link type="text/css" href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme-blue.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
	<link rel="stylesheet" href="<html:rewrite page='/includes/qalearning.css'/>">
	<style media="screen,projection" type="text/css">
		.tablesorter-blue {
			margin: 5px 0 5px;
		}
		.question-title {
			font-weight: bold;
		}
		.tablesorter-container {
			width: 94%;
			margin: 20px 20px 0;
		}
		.rating-stars-div {
			padding-top: 12px;
		}
		.instructions {
			padding-left: 20px;
		}
		#reflections {
			padding: 70px 0 0px;
		}
		.hidden {
			color: red !important;
			font-style: italic;
		}
		.dialog {
			display: none;
		}
		.group-name-title {
			padding: 40px 10px 0;
		}
		a.image-link {
			border-bottom: none !important;
		}
		
		.tablesorter tbody > tr:nth-child(odd) > td, .tablesorter tbody > tr:nth-child(odd) > th {
		    background-color: #EBF2FA;
		}
		.tablesorter tbody > tr:nth-child(odd):hover > td, .tablesorter tbody > tr:nth-child(odd):hover > th {
		    background-color: #bfbfbf;
		}
		tr:nth-child(odd):hover .jStar {background-image: url(${lams}images/css/jquery.jRating-stars-grey.png)!important;}
		tr:nth-child(even):hover .jStar {background-image: url(${lams}images/css/jquery.jRating-stars-light-grey.png)!important;}
		tr:nth-child(odd) .jStar {background-image: url(${lams}images/css/jquery.jRating-stars-light-blue.png)!important;}
		
		h4 {
			color:#0087e5; 
			font-size:11px;
			margin-top:10px;
			margin-bottom:10px;
			padding:0;
		}
		
	</style>
	
	<!-- ********************  javascript ********************** -->
	<script type="text/javascript"> 
		//pass settings to monitorToolSummaryAdvanced.js
		var submissionDeadlineSettings = {
			lams: '${lams}',
			submissionDeadline: '${submissionDeadline}',
			setSubmissionDeadlineUrl: '<c:url value="/monitoring.do?dispatch=setSubmissionDeadline"/>',
			toolContentID: '${content.qaContentId}',
			messageNotification: '<fmt:message key="monitor.summary.notification" />',
			messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
			messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
		};	
	
		//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/";
		
		//vars for rating.js
		var AVG_RATING_LABEL = '<fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message>',
		YOUR_RATING_LABEL = '<fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message>',
		IS_DISABLED =  true,
		COMMENTS_MIN_WORDS_LIMIT = 0,
		MAX_RATES = 0,
		MIN_RATES = 0,
		LAMS_URL = '${lams}',
		COUNT_RATED_ITEMS = 0,
		COMMENT_TEXTAREA_TIP_LABEL = '<fmt:message key="label.comment.textarea.tip"/>',
		WARN_COMMENTS_IS_BLANK_LABEL = '<fmt:message key="${warnCommentIsBlankLabel}"/>',
		WARN_MIN_NUMBER_WORDS_LABEL = '<fmt:message key="${warnMinNumberWordsLabel}"><fmt:param value="${itemRatingDto.commentsMinWordsLimit}"/></fmt:message>';

	</script>
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script> 
	<script type="text/javascript" src="${lams}includes/javascript/rating.js"></script> 

 	<!-- ******************** FCK Editor related javascript & HTML ********************** -->
	<script language="JavaScript" type="text/JavaScript">
	
		var POSTED_BY_LABEL = '<fmt:message key="label.posted.by"><fmt:param>{0}</fmt:param><fmt:param>{1}</fmt:param></fmt:message>';
	
	  	$(document).ready(function(){
		    
			$(".tablesorter").tablesorter({
				theme: 'blue',
			    widthFixed: true,
				widgets: ["zebra", "filter"],
				headers: { 1: { filter: false }, 2: { filter: false, sorter: false } },
			    widgetOptions : {
			        // include column filters
			        filter_columnFilters: true,
		    	    filter_placeholder: { search : '<fmt:message key="label.search"/>' },
		    	    filter_searchDelay: 700
		      	}
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
					ajaxUrl : "<c:url value='/learning.do'/>?method=getResponses&page={page}&size={size}&{sortList:column}&{filterList:fcol}&isMonitoring=true&isAllowRateAnswers=${qaContent.allowRateAnswers}&isAllowRichEditor=${qaContent.allowRichEditor}&isOnlyLeadersIncluded=${qaContent.useSelectLeaderToolOuput}&qaContentId=${qaContent.qaContentId}&qaSessionId=" + $(this).attr('data-session-id') + "&questionUid=" + $(this).attr('data-question-uid') + "&userId=" + $("#userID").val() + "&reqID=" + (new Date()).getTime(),
					ajaxProcessing: function (data) {
				    	if (data && data.hasOwnProperty('rows')) {
				    		var rows = [],
				            json = {},
							countRatedItems = data.countRatedItems;
				    		
							for (i = 0; i < data.rows.length; i++){
								var userData = data.rows[i];
								var itemId = userData["responseUid"];
								
								rows += '<tr>';
								rows += '<td>';
								
									rows += '<div>';
									rows += 	'<span class="field-name">';
									rows += 		userData["userName"];
									rows += 	'</span> ';
									rows += 	userData["attemptTime"];
									rows += '</div>';
								
								rows += 	'<div class="user-answer">';
								if (userData["visible"] == 'true') {
									rows += 	userData["answer"];
								} else {
									rows += 	'<i><fmt:message key="label.hidden"/></i>';
								}
								rows += 	'</div>';
								
								rows += '</td>';
								
								if (${isRatingsEnabled}) {
									rows += '<td style="width:150px;">';
									
									if (userData["visible"] == 'true') {
										rows += '<div class="rating-stars-holder">';
										
										for (j = 0; j < userData.criteriaDtos.length; j++){
											var criteriaDto = userData.criteriaDtos[j];
											var objectId = criteriaDto["ratingCriteriaId"] + "-" + itemId;
											var averageRating = criteriaDto.averageRating;
											var numberOfVotes = criteriaDto.numberOfVotes;
											var userRating = criteriaDto.userRating;
											var isCriteriaNotRatedByUser = userRating == "";
											var averageRatingDisplayed = averageRating;
											var ratingStarsClass = "rating-stars-disabled";
								
											rows += '<h4>';
											rows += 	 criteriaDto.title;
											rows += '</h4>';
											
											rows += '<div class="'+ ratingStarsClass +' rating-stars-new" data-average="'+ averageRatingDisplayed +'" data-id="'+ objectId +'">';
											rows += '</div>';
											
												rows += '<div class="rating-stars-caption">';
												rows += 	AVG_RATING_LABEL.replace("@1@", averageRating).replace("@2@", numberOfVotes);
												rows += '</div>';
										}
										
										rows += '</div>';
									}
									
									rows += '</td>';
								}
								
								if (${isCommentsEnabled}) {
									rows += '<td style="width:30%; min-width: 250px;" id="comments-area-' + itemId + '">';
										
										//show all comments needs to be shown
										if (userData.comments) {
											for (j = 0; j < userData.comments.length; j++){
												var comment = userData.comments[j];
												
												var postedBy = POSTED_BY_LABEL.replace("{0}", comment.userFullName).replace("{1}", comment.postedDate);
												
												rows += '<div class="rating-comment">';
												rows += 	comment.comment;
												rows += 	'<div class="rating-comment-posted-by">';
												rows += 		postedBy;
												rows += 	'</div>';
												rows += '</div>';
											}
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
				    output: '{startRow} to {endRow} ({totalRows})',
				    //removeRows: false,
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
					initializeJRating();
				})
			});
			
			$("#edit-response-dialog").dialog({
				bgiframe: true,
				autoOpen: false,
				closeOnEscape: false,
				dialogClass: 'alert',
				height: 400,
				width: 700,
				modal: true,
				position: 'center',
				open: function( event, ui ) {
					//move CKEditor inside dialog. We can't place it inside from the beginning as there is a problem and jquery dialog creates 2 instances of CKEDitor
					if ($("#updated-response-editor").parent().attr('id') != "edit-response-dialog") {
						$("#updated-response-editor").appendTo("#edit-response-dialog").show();	
					}

					var responseUid = $('#edit-response-dialog').dialog( 'option' , 'responseUid');
					var response = $("#response-" + responseUid).html();
					
					if (${content.allowRichEditor}) {
						CKEDITOR.instances["updated-response"].setData(response);
					} else {
						$('#updated-response').val(response);
					}
				},
				buttons: {
					'<fmt:message key="label.save"/>': function() {
						var responseUid = $('#edit-response-dialog').dialog( 'option' , 'responseUid');
						
						var updatedResponse;
						if (${content.allowRichEditor}) {
							//prepareOptionEditorForAjaxSubmit
							var ckeditorData = CKEDITOR.instances["updated-response"].getData();
							//skip out empty values
							updatedResponse = ((ckeditorData == null) || (ckeditorData.replace(/&nbsp;| |<br \/>|\s|<p>|<\/p>|\xa0/g, "").length == 0)) ? "" : ckeditorData;
						} else {
							updatedResponse = $("#updated-response").val();
						}
						
				        $.ajax({
				        	async: false,
				            url: '<c:url value="/monitoring.do"/>',
				            data: {
				            	dispatch: "updateResponse",
				            	responseUid: responseUid,
				            	updatedResponse: updatedResponse
				            },
				            type: 'post',
				            success: function () {
				            	$("#response-" + responseUid).html( updatedResponse );
				            }
				       	});
						$(this).dialog('close');
					},
					'<fmt:message key="label.cancel"/>': function() {
						$(this).dialog('close');
					}
				}
			});

		 });
	  	
		function changeResponseVisibility(linkObject, responseUid, isHideItem) {
	        $.ajax({
	            url: '<c:url value="/monitoring.do"/>',
	            data: 'dispatch=updateResponseVisibility&responseUid=' + responseUid + '&isHideItem=' + isHideItem,
	            dataType: 'json',
	            type: 'post',
	            success: function (json) {
	            	
	            	if (isHideItem) {
	            		linkObject.innerHTML = '<img src="<c:out value="${tool}"/>images/hidden-eye.png" border="0">' ;
	            		linkObject.title = "<fmt:message key='label.show'/>";
	            		linkObject.onclick = function (){
	            			changeResponseVisibility(this, responseUid, false); 
	            			return false;
	            		}
	            		$("#td-response-" + responseUid).addClass( "hidden" );
	            		
	            	} else {
	            		linkObject.innerHTML = '<img src="<c:out value="${tool}"/>images/display-eye.png" border="0">' ;
	            		linkObject.title = "<fmt:message key='label.hide'/>";
	            		linkObject.onclick = function (){
	            			changeResponseVisibility(this, responseUid, true); 
	            			return false;
	            		}
	            		$("#td-response-" + responseUid).removeClass( "hidden" );
	            	}
	            }
	       	});
		}
		
		function editResponse(responseUid) {
			$('#edit-response-dialog').dialog( 'option' , 'responseUid' , responseUid );
			$('#edit-response-dialog').dialog('open');
		}
	  	
		function submitMonitoringMethod(actionMethod) {
			document.QaMonitoringForm.dispatch.value=actionMethod; 
			document.QaMonitoringForm.submit();
		}
		
		function submitMethod(actionMethod) {
			submitMonitoringMethod(actionMethod);
		}

		function submitModifyMonitoringQuestion(questionIndexValue, actionMethod) {
			document.QaMonitoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}

        function init(){
            var tag = document.getElementById("currentTab");
	    	if(tag.value != "")
	    		selectTab(tag.value);
            else
                selectTab(1); //select the default tab;
        }     
        
        function doSelectTab(tabId) {
        	// start optional tab controller stuff
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	// end optional tab controller stuff
	    	selectTab(tabId);
        } 
        
        function doSubmit(method) {
        	document.QaMonitoringForm.dispatch.value=method;
        	document.QaMonitoringForm.submit();
        }
	
	</script>
	
</lams:head>
<body class="stripes" onLoad="init();">

<div id="page">
	<h1> 
		<fmt:message key="label.monitoring"/> 
	</h1>

	<div id="header">
		<lams:Tabs collection="${tabs}" useKey="true" control="true"/>	
	</div>	
	
	<div id="content">		
	    <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<html:hidden property="dispatch"/>
		<html:hidden property="currentUid"/>
		<html:hidden property="toolContentID"/>
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="httpSessionID"/>					
		<html:hidden property="contentFolderID"/>						
		
		<lams:help toolSignature="<%= QaAppConstants.MY_SIGNATURE %>" module="monitoring"/>

		<lams:TabBody id="1" titleKey="label.summary" page="SummaryContent.jsp"/>
		<lams:TabBody id="2" titleKey="label.editActivity" page="Edit.jsp" />
		<lams:TabBody id="3" titleKey="label.stats" page="Stats.jsp" />
		</html:form>
	</div>
	
	<div id="footer"></div>
	
	<div id="edit-response-dialog" title="<fmt:message key='label.modify.users.response' />" class="dialog">
		<br>
	</div>
	
	<div id="updated-response-editor" class="dialog">
		<c:choose>
			<c:when test="${content.allowRichEditor}">
				<lams:CKEditor id="updated-response" value="" toolbarSet="DefaultMonitor"/>
			</c:when>
			<c:otherwise>
				<lams:textarea name="updated-response" id="updated-response" rows="16" cols="108"> </lams:textarea>
			</c:otherwise>
		</c:choose>
	</div>

	</div>
	
</body>
</lams:html>
