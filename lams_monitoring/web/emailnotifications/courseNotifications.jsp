<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<lams:html>
<lams:head>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<title><fmt:message key="${title}"/></title>
	
	<lams:css/>
 	<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
 	<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui.timepicker.css" rel="stylesheet">
 	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet">

	<style type="text/css">
		#additionalParameters {min-height: 50px;}
		#lessonsDiv {display: none;}
		#lessonDiv { padding-bottom: 20px;}
		#datePickerDiv {margin: 10px 0 10px;}
		
		/* Accordion */
		.ui-state-active a, .ui-state-active a:link, .ui-state-active a:visited, .ui-state-active a:hover  {
		    background-image: none;
		    background-color: transparent ;
		}
		#nowDiv{padding: 0px;}
		#nowDiv .ui-jqgrid-bootstrap{border-radius: 0px;}
		.ui-jqgrid-hbox {padding-left: 0;}
		#accordion h3 a {border-bottom: 0;}
		#accordion h4 {margin-bottom: 0;border-bottom: 1px;}
		#accordion p {text-align: center; padding-bottom: 0px; margin-bottom: 0xp; font-size:12px;}
	</style>	
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/monitorToolSummaryAdvanced.js "></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.jstepper.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {

			//initialize user list 
			jQuery("#list3").jqGrid({
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
			   	url: "<c:url value='/emailNotifications/getUsers.do?'/>" + getSearchParams(),
				datatype: "json",
			   	colNames:['<fmt:message key="email.notifications.user.name"/>'],
			   	colModel:[
			   		{name:'name',index:'name', width:260, firstsortorder:'desc', sorttype: 'text'}
			   	],
			    rowList:[10,20,30,40,50,100],
			   	rowNum:10,
			   	pager: '#pager3',
			   	sortname: 'name',
			   	multiselect: true,
				multiPageSelection : true,
			    sortorder: "asc",
			    loadonce: true,
			    height:'100%',
				autowidth:true,
			    ignoreCase: true
			});
			jQuery("#list3").jqGrid('navGrid','#pager3',{add:false,del:false,edit:false,search:false});
			jQuery("#list3").jqGrid('filterToolbar',{stringResult: true, searchOnEnter: true, defaultSearch: 'cn'});
			$("#list3").parents('div.ui-jqgrid-bdiv').css("max-height","1000px");
			$('#pager3_right').css('display','inline');
			
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
			
			jQuery("#lessonsTable").jqGrid({
				datatype: "local",
				rowNum: 10000,
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
				jQuery("#lessonsTable").jqGrid('addRowData',${lesson.id}, {id2:'${lesson.id}',name:'${fn:escapeXml(lesson.name)}'});
			</c:forEach>
			
			$("#lessonsTable").parents('div.ui-jqgrid-bdiv').css("max-height","10000px");
			
			//initialize jStepper for additional parameters
			$("#daysToDeadline").jStepper({minValue:0, maxValue:999, defaultValue:3, allowDecimals:false});
			
    		$('#emailButton').click(function() {
    			
    			var isInstantEmailing = ($('#accordion').accordion('option', 'active') == 0);
    			var ids = jQuery("#list3").getGridParam('selarrrow');

    			var searchType = document.getElementById("searchType").value;
    			var params =  "&searchType=" + searchType;
    			
    			if(isInstantEmailing && ids.length) {
    			    for (var i=0;i<ids.length;i++) {
    			    	params += "&userId=" + ids[i];
    			    }
    			    params += "&organisationID=${org.organisationId}";
    			} else if (isInstantEmailing && !ids.length) {
    				return;

    			//in case of scheduling
    			} else {
       				//get the timestamp in milliseconds since midnight Jan 1, 1970
    				var scheduleDate = $("#datePicker").datetimepicker('getDate');
    				if (scheduleDate == null) {
    					return;
    				}
        			       			
    				
    				params += "&scheduleDate=" + scheduleDate.getTime() + getSearchParams(); 
    			}
    			
    			var emailBody = encodeURIComponent(document.getElementById("emailBody").value);
    			var scheduleDate = $("#datePicker").datepicker( "getDate" );
    			scheduleDate = (scheduleDate == null) ? "" : scheduleDate.getTime();
    	        $.ajax({
    	        	async: false,
    	            url: '<c:url value="/emailNotifications/emailUsers.do"/>?<csrf:token/>',
    	            data: "emailBody=" + emailBody + params,
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
			var url = "<c:url value='/emailNotifications/getUsers.do?'/>" + searchParams;
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
	</script>
	
</lams:head>
    
<body class="stripes">

	<lams:Page title="${title}" type="admin" formID="emailNotificationsForm">
		<div class="row">
			<div class="col-sm-12">
				<div class="btn-group pull-right">
					<a href="<c:url value='/emailNotifications/showScheduledEmails.do'/>?organisationID=${org.organisationId}"
					   id="listEmailsHref" class="btn btn-default btn-sm">
						<i class="fa fa-calendar"></i> <fmt:message key="email.notifications.scheduled.messages.button" />
					</a>
					<a href="<c:url value='/emailNotifications/showArchivedEmails.do'/>?organisationID=${org.organisationId}"
					   id="archiveHref" class="btn btn-default btn-sm">
						<i class="fa fa-archive"></i> <fmt:message key="email.notifications.archived.messages.button" />
					</a>		
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<h4><fmt:message key="email.notifications.notify.sudents.that"/></h4>
				
				<form:form action="emailUsers.do" method="post" modelAttribute="emailNotificationsForm" id="emailNotificationsForm" >	
				
				<!-- Dropdown menu for choosing a user search type -->
				<div>
					<c:choose>
					<c:when test="${empty lessons}">
						<!--  only valid type is not started lessons -->
						<select id="searchType" onchange="getUsers();">
							<option selected="selected" value="9"><fmt:message key="email.notifications.user.search.property.9" /></option>
						</select>
					</c:when>
					<c:otherwise>
					<select id="searchType" onchange="getUsers();">
						<option selected="selected" value="7"><fmt:message key="email.notifications.user.search.property.7" /></option>
						<option value="8"><fmt:message key="email.notifications.user.search.property.8" /></option>
						<option value="9"><fmt:message key="email.notifications.user.search.property.9" /></option>
						<option value="10"><fmt:message key="email.notifications.user.search.property.10" /></option>
						<option value="11"><fmt:message key="email.notifications.user.search.property.11" /></option>
					</select>
					</c:otherwise>
					</c:choose>
				</div>
				
				<div id="additionalParameters">
					<div id="lessonDiv">
						<h4>
							<fmt:message key="email.notifications.lesson" />
						</h4>
						<select id="lessonId" onchange="getUsers();">
							<c:forEach var="lesson" items="${lessons}">
								<option <c:if test="${lesson.id==firstLesson.id}">selected="selected"</c:if> value="${lesson.id}">${fn:escapeXml(lesson.name)}</option>
							</c:forEach>
						</select>				
					</div>
					
					<div id="lessonsDiv">
						<h4>
							<fmt:message key="email.notifications.lessons" />
						</h4>
						
						<table id="lessonsTable"></table>
						<input style="margin-left: 230px;" class="btn btn-default btn-sm" type="button" value="<fmt:message key="button.ok"/>" onclick="getUsers();"  />			
					</div>
				</div>
		
				<%@ include file="additionalSettings.jsp"%>
		
				</form:form>
			
			</div>
			<div class="col-sm-6">
				<div id="emailTextareaDiv">
					<h4><fmt:message key="email.notifications.message.header"/></h4>
					<c:set var="emailBody"><fmt:message key="email.notifications.course.email.body.header"/><br/><br/><fmt:message key="email.notifications.course.email.body.msg"/><br/><br/><br/>
					</c:set>
					<textarea rows="8" name="emailBody" id="emailBody" width="100%"  class="form-control" >${fn:replace(emailBody, '<br/>', newLineChar)}</textarea>
					<br/>
					<input class="btn btn-primary btn-sm voffset10 pull-right" type="button" id="emailButton" value="<fmt:message key="email.notifications.send"/>" />
					
				</div>
			</div>
		</div>
	</lams:Page>
</body>
</lams:html>
