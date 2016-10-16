<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-function" prefix="fn"%>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<html:base/>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<title><fmt:message key="${fn:escapeXml(title)}"/></title>
	
	<lams:css/>
	<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="<lams:LAMSURL/>/css/jquery.jqGrid.css" />
	<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui.timepicker.css" rel="stylesheet">
	<style type="text/css">
		#additionalParameters {min-height: 50px;}
		#activityDiv, #daysToDeadlineDiv {display: none;}
		#pager3_right table{float:right !important; }
		#datePickerDiv {margin: 10px 0 10px;}
		
		/* Accordion */
		.ui-state-active a, .ui-state-active a:link, .ui-state-active a:visited, .ui-state-active a:hover  {
		    background-image: none;
		    background-color: transparent ;
		}
		#nowDiv{padding-left: 10px;}
		.ui-jqgrid-hbox {padding-left: 0;}
		#accordion {width: 100%;}
		#accordion h3 a {border-bottom: 0;}
		#accordion p {text-align: center; padding-bottom: 0px; margin-bottom: 0xp; font-size:12px;}
	</style>	
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/monitorToolSummaryAdvanced.js "></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.jqGrid.locale-en.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.jqGrid.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.jstepper.min.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			
			//initialize user list 
			jQuery("#list3").jqGrid({
			   	url: "<c:url value='/emailNotifications.do'/>?method=getUsers" + getSearchParams(),
				datatype: "json",
			   	colNames:['<fmt:message key="email.notifications.user.name"/>'],
			   	colModel:[
			   		{name:'name',index:'name', width:260, firstsortorder:'desc', sorttype: 'text'}
			   	],
			   	rowNum:50,
			   	rowList:[50,75,100,150,200],
			   	pager: '#pager3',
			   	sortname: 'name',
			   	multiselect: true,
			    sortorder: "asc",
			    loadonce: true,
			    height:'100%',
			    pagerpos:'left',
			    ignoreCase: true
			});
			jQuery("#list3").jqGrid('navGrid','#pager3',{add:false,del:false,edit:false,position:'right'});
			jQuery("#list3").jqGrid('filterToolbar',{stringResult: true, searchOnEnter: true, defaultSearch: 'cn'});
			$("#list3").parents('div.ui-jqgrid-bdiv').css("max-height","1000px");

			//initialize jStepper for additional parameters
			$("#daysToDeadline").jStepper({minValue:0, maxValue:999, defaultValue:3, allowDecimals:false});
			
    		//initialize datetimepicker
    		$("#datePicker").datetimepicker();
    		
    		//initialize accordion
    		$( "#accordion" ).accordion({
    			create: function(event, ui) {
    				// accordion probably sets its height based on empty grid
    				// once it is loaded, it needs to be adjusted
    				$('div.ui-accordion-content').css('height', '100%');
    			},
    			activate: function(event, ui) {
    				if ($('#accordion').accordion('option', 'active') == 0) {
    					$("#emailButton").attr('value', '<fmt:message key="email.notifications.send"/>');
    				} else {
    					$("#emailButton").attr('value', '<fmt:message key="email.notifications.button.schedule"/>');
    				}
    			}
    		});
			
    		$('#emailButton').click(function() {
    			
    			var isInstantEmailing = ($('#accordion').accordion('option', 'active') == 0);
    			var ids = jQuery("#list3").getGridParam('selarrrow');
    			
    			var params = "";
    			if (isInstantEmailing && ids.length) {
    				for (var i=0;i<ids.length;i++) {
    			    	params += "&userId=" + ids[i];
    			    }
    			} else if (isInstantEmailing && !ids.length) {
    				return;
    				
    			//in case of scheduling
    			} else {
    				//get the timestamp in milliseconds since midnight Jan 1, 1970
    				var scheduleDate = $("#datePicker").datetimepicker('getDate');
    				if (scheduleDate == null) {
    					return;
    				}
        			       			
    				var searchType = document.getElementById("searchType").value;
    				params = "&searchType=" + searchType + "&scheduleDate=" + scheduleDate.getTime() + getSearchParams();
    			}
    			
    			var emailBody = encodeURIComponent(document.getElementById("emailBody").value);
    			
    	        $.ajax({
    	        	async: false,
    	            url: '<c:url value="/emailNotifications.do"/>',
    	            data: "method=emailUsers&emailBody=" + emailBody + params,
    	            dataType: 'json',
    	            type: 'post',
    	            success: function (json) {
    		            if (json.isSuccessfullySent) {
    		            	alert("<fmt:message key="email.notifications.emails.successfully.sent" />");
    					} else if (json.isSuccessfullyScheduled) {
    		            	alert("<fmt:message key="email.notifications.emails.successfully.scheduled" />");
    					} else {
    						alert("<fmt:message key="email.notifications.problems.sending.emails" />");
    					}
    	            }
    	       	});
    		});
		});
		
		function getUsers(){
			var searchParams = getSearchParams();
			var url = "<c:url value='/emailNotifications.do'/>?method=getUsers" + searchParams;
			if (searchParams.length) {
				$("#list3").setGridParam({datatype:'json', page:1, url: url}).trigger('reloadGrid');	
			} else {
				$("#list3").setGridParam({page:1}).clearGridData();
			}
			return false;
		}
		
		function getSearchParams() {
			
			var searchType = document.getElementById("searchType").value;
			
			//process in case of "Haven't reached particular activity"
			var activityId;
			if (searchType == 5) {
				$("#activityDiv").show();
				activityId = document.getElementById("activityId").value;
			} else {
				$("#activityDiv").hide();
			}
			
			//process in case of "Have X days to deadline"
			var daysToDeadline;
			if (searchType == 6) {
				$("#daysToDeadlineDiv").show();
				daysToDeadline = document.getElementById("daysToDeadline").value;
				if (daysToDeadline == "") {
					return "";
				}
			} else {
				$("#daysToDeadlineDiv").hide();
			}
			
			//return search params
			return "&lessonID=${lesson.lessonId}&searchType=" + searchType + "&activityID=" + activityId + "&daysToDeadline=" + daysToDeadline;
		}
	</script>
	
</lams:head>
    
<body class="stripes">

	<lams:Page title="${title}" type="admin">
		<div class="row">
			<div class="col-sm-6">
				<h4><fmt:message key="email.notifications.notify.sudents.that"/></h4>
				
				<form action="<c:url value="/emailNotifications.do"/>?method=emailUsers" method="post" id="emailNotificationsForm" >	
				
				
				<!-- Dropdown menu for choosing a user search type -->
				<div>
					<select id="searchType" onchange="getUsers();">
						<option selected="selected" value="0"><fmt:message key="email.notifications.user.search.property.0" /></option>
						<option value="1"><fmt:message key="email.notifications.user.search.property.1" /></option>
						<option value="2"><fmt:message key="email.notifications.user.search.property.2" /></option>
						<option value="3"><fmt:message key="email.notifications.user.search.property.3" /></option>
						<option value="4"><fmt:message key="email.notifications.user.search.property.4" /></option>
						<option value="5"><fmt:message key="email.notifications.user.search.property.5" /></option>
						<option value="6"><fmt:message key="email.notifications.user.search.property.6" /></option>
					</select>
				</div>
				
				<div id="additionalParameters">
					<div id="activityDiv">
						<h3>
							<fmt:message key="email.notifications.activity" />
						</h3>
						<select id="activityId" onchange="getUsers();">
							<c:forEach var="activity" items="${activities}" varStatus="i">
								<option <c:if test="${i.index==0}">selected="selected"</c:if> value="${activity.activityId}"><c:out value="${activity.title}"/></option>
							</c:forEach>
						</select>				
					</div>
					
					<div id="daysToDeadlineDiv">
						<h3>
							<fmt:message key="email.notifications.days.to.deadline" />
						</h3>
						
						<input name="daysToDeadline" type="text" id="daysToDeadline" value="3" size="3"/> 	
						<input class="btn btn-default btn-sm" type="button" value="<fmt:message key="button.ok"/>" onclick="getUsers();" />			
					</div>
				</div>
				
				<%@ include file="additionalSettings.jsp"%>
				
				</form>			
			</div>
			<div class="col-sm-6">
				<div id="emailTextareaDiv">
					<h4>Message:</h4>
					<c:set var="emailBody"><fmt:message key="email.notifications.lesson.email.body.header"/><br/><br/><fmt:message key="email.notifications.lesson.email.body.msg"/><br/><br/><br/><fmt:message key="email.notifications.lesson.email.body.footer" ><fmt:param>${lesson.lessonName}</fmt:param><fmt:param><lams:LAMSURL/>home.do?method=learner&lessonID=${lesson.lessonId}</fmt:param></fmt:message>
					</c:set>
					<textarea rows="8" name="emailBody" id="emailBody" width="100%" class="form-control">${fn:replace(emailBody, '<br/>', newLineChar)}</textarea>
					<br/>
					<input class="btn btn-primary btn-sm voffset10 pull-right" type="button" id="emailButton" value="<fmt:message key="email.notifications.send"/>" />
				</div>		
			</div>
		</div>

	</lams:Page>
</body>
</lams:html>
