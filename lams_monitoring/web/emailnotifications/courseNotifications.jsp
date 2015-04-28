<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-function" prefix="fn"%>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
<lams:head>
	<html:base/>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<title><fmt:message key="${title}"/></title>
	
	<lams:css style="learner"/>
	<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="<lams:LAMSURL/>/css/jquery.jqGrid.css" />	
	<style media="screen,projection" type="text/css">
		div#content {min-height: 250px; }
		#emailTextareaDiv {float: right;}
		#emailButton {text-align: right; float: right;}
		#additionalParameters {min-height: 50px;}
		#lessonsDiv {display: none;}
		#lessonDiv { padding-bottom: 20px;}
		#pager3_right table{float:right !important; }
		#datePickerDiv {margin: 10px 0 50px;}
		
		/* Accordion */
		.ui-state-active a, .ui-state-active a:link, .ui-state-active a:visited, .ui-state-active a:hover  {
		    color: #47bc23 !important;
		    background-image: none;
		    background-color: transparent ;
		}
		#nowDiv{padding-left: 10px;}
		.ui-jqgrid-hbox {padding-left: 0;}
		#accordion {width: 325px;}
		#accordion h3 a {border-bottom: 0;}
		#accordion p {text-align: center; padding-bottom: 0px; margin-bottom: 0xp;}
		#listEmailsHref {border-bottom: 1px dotted #47BC23;color: #47BC23;text-decoration: none;}
	</style>	
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/monitorToolSummaryAdvanced.js "></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.jqGrid.locale-en.js"></script>
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.jqGrid.js"></script>
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.jstepper.min.js"></script>
	<script language="JavaScript" type="text/javascript">
	<!--
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
			    height:'auto',
			    pagerpos:'left',
			    ignoreCase: true
			});
			jQuery("#list3").jqGrid('navGrid','#pager3',{add:false,del:false,edit:false,position:'right'});
			$("#list3").parents('div.ui-jqgrid-bdiv').css("max-height","1000px");
			jQuery("#list3").jqGrid('filterToolbar',{stringResult: true, searchOnEnter: true, defaultSearch: 'cn'});
			
    		//initialize datePicker
    		$( "#datePicker" ).datepicker({ 
    			dateFormat: 'dd/mm/yy',
    			minDate: 1
    		});
    		
    		//initialize accordion
    		$( "#accordion" ).accordion({
    			autoHeight: false,
    			create: function(event, ui) {
    				
    			},
    			change: function(event, ui) {
    				if ($('#accordion').accordion('option', 'active') == 0) {
    					$("#emailButton").attr('value', '<fmt:message key="email.notifications.send"/>');
    				} else {
    					$("#emailButton").attr('value', '<fmt:message key="email.notifications.button.schedule"/>');
    				}
    			}
    		});
			
			jQuery("#lessonsTable").jqGrid({
				datatype: "local",
			   	colNames:['Id', '<fmt:message key="email.notifications.lesson.name"/>'],
			   	colModel:[
					{name:'id2',index:'id2', width:35, sorttype:"int"},
			   		{name:'name',index:'name', width:225, firstsortorder:'desc', sorttype: 'text'}
			   	],
			   	sortname: 'id2',
			   	multiselect: true,
			    sortorder: "asc",
			    height:'auto',
			    ignoreCase: true
			});
			jQuery("#lessonsTable").jqGrid('filterToolbar',{stringResult: true, searchOnEnter: true, defaultSearch: 'cn'});
			
			<c:forEach var="lesson" items="${lessons}" varStatus="i">
				jQuery("#lessonsTable").jqGrid('addRowData',${lesson.lessonId}, {id2:'${lesson.lessonId}',name:'${fn:escapeXml(lesson.lessonName)}'});
			</c:forEach>
			
			$("#lessonsTable").parents('div.ui-jqgrid-bdiv').css("max-height","10000px");
			
			//initialize jStepper for additional parameters
			$("#daysToDeadline").jStepper({minValue:0, maxValue:999, defaultValue:3, allowDecimals:false});
			
    		$('#emailButton').click(function() {
    			
    			var isInstantEmailing = ($('#accordion').accordion('option', 'active') == 0);
    			var ids = jQuery("#list3").getGridParam('selarrrow');
    			var params = "";
    			if(isInstantEmailing && ids.length) {
    			    for (var i=0;i<ids.length;i++) {
    			    	params += "&userId=" + ids[i];
    			    }
    			} else if (isInstantEmailing && !ids.length) {
    				return;
    				
    			} else { //in case of scheduling
    				var scheduleDate = $("#datePicker").datepicker( "getDate" );
    				if (scheduleDate == null) {
    					return;
    				} else {
    					scheduleDate = scheduleDate.getTime();
    				}
        			       			
    				var searchType = document.getElementById("searchType").value;
    				params = "&searchType=" + searchType + "&scheduleDate=" + scheduleDate + getSearchParams(); 
    			}
    			
    			var emailBody = encodeURIComponent(document.getElementById("emailBody").value);
    			var scheduleDate = $("#datePicker").datepicker( "getDate" );
    			scheduleDate = (scheduleDate == null) ? "" : scheduleDate.getTime();
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
			
			//process in case of "Have finished particular lesson" | "Haven't started particular lesson"
			var lessonId = "";
			if ((searchType == 7) || (searchType == 8)) {
				$("#lessonDiv").show();
				lessonId = "&lessonID=" + document.getElementById("lessonId").value;
				if (document.getElementById("lessonId").value == "") {
					return "";
				}
			} else {
				$("#lessonDiv").hide();
			}
			
			//process in case of "Have finished these lessons"
			var lessonIds = "";
			if ((searchType == 10) || (searchType == 11)) {
				$("#lessonsDiv").show();

    			var ids = jQuery("#lessonsTable").getGridParam('selarrrow');
    			if(ids.length) {
    			    for (var i=0;i<ids.length;i++) {
    			    	lessonIds += "&lessonID=" + ids[i];
    			    }
    			} else {
    				return "";
    			}
			} else {
				$("#lessonsDiv").hide();
			}
			
			//return search params
			return "&organisationID=${org.organisationId}&searchType=" + searchType + lessonId + lessonIds;
		}
	//-->
	</script>
	
</lams:head>
    
<body class="stripes">
<div id="page">
	<div id="content">

		<h2><fmt:message key="email.notifications.notify.sudents.that"/></h2>
		
		<form action="<c:url value="/emailNotifications.do"/>?method=emailUsers" method="post" id="emailNotificationsForm" >	
		
		<div id="emailTextareaDiv">
		
			<c:set var="emailBody"><fmt:message key="email.notifications.course.email.body.header"/><br/><br/><fmt:message key="email.notifications.course.email.body.msg"/><br/><br/><br/>
			</c:set>
			<textarea rows="8" name="emailBody" id="emailBody" cols="43" >${fn:replace(emailBody, '<br/>', newLineChar)}</textarea>
			<br/><br/>
			<input class="button" type="button" id="emailButton" value="<fmt:message key="email.notifications.send"/>" />
			
		</div>
		
		<!-- Dropdown menu for choosing a user search type -->
		<div>
			<select id="searchType" onchange="getUsers();">
				<option selected="selected" value="7"><fmt:message key="email.notifications.user.search.property.7" /></option>
				<option value="8"><fmt:message key="email.notifications.user.search.property.8" /></option>
				<option value="9"><fmt:message key="email.notifications.user.search.property.9" /></option>
				<option value="10"><fmt:message key="email.notifications.user.search.property.10" /></option>
				<option value="11"><fmt:message key="email.notifications.user.search.property.11" /></option>
			</select>
		</div>
		
		<div id="additionalParameters">
			<div id="lessonDiv">
				<h3>
					<fmt:message key="email.notifications.lesson" />
				</h3>
				<select id="lessonId" onchange="getUsers();">
					<c:forEach var="lesson" items="${lessons}">
						<option <c:if test="${lesson.lessonId==firstLesson.lessonId}">selected="selected"</c:if> value="${lesson.lessonId}">${fn:escapeXml(lesson.lessonName)}</option>
					</c:forEach>
				</select>				
			</div>
			
			<div id="lessonsDiv">
				<h3>
					<fmt:message key="email.notifications.lessons" />
				</h3>
				
				<table id="lessonsTable"></table>
				<br/>
				<input style="margin-left: 230px;" class="button small-space-bottom" type="button" value="<fmt:message key="button.ok"/>" onclick="getUsers();"  />			
			</div>
		</div>

		<%@ include file="additionalSettings.jsp"%>

		</form>
	</div>
</div>
</body>
</lams:html>
