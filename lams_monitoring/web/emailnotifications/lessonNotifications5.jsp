<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<c:set var="newLineChar" value="<%= \"\r\n\" %>" />

<lams:html>
<lams:head>

	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/free.ui.jqgrid.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme5.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/tempus-dominus.min.css">
	<style type="text/css">
		#activityDiv, #daysToDeadlineDiv {
			display: none;
		}
	</style>

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/popper.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/tempus-dominus.min.js"></script>
	
	<script type="text/javascript">
		jQuery(document).ready(function() {
			$.extend(true, $.jgrid.guiStyles.bootstrap4, {
				pager : {
					pagerSelect : 'form-control-select'
				},
				searchToolbar : {
					clearButton : 'btn btn-sm'
				},
				titleButton : "btn btn-xs"
			});
			
			//initialize user list 
			jQuery("#list3").jqGrid({
				guiStyle: "bootstrap4",
				iconSet: 'fontAwesomeSolid',
			   	url: "<c:url value='/emailNotifications/getUsers.do?'/>" + getSearchParams(),
				datatype: "json",
				colNames:['<spring:escapeBody javaScriptEscape='true'><fmt:message key="email.notifications.user.name"/></spring:escapeBody>'],
			   	colModel:[
			   		{name:'name',index:'name', width:260, firstsortorder:'desc', sorttype: 'text'}
			   	],
			    rowList:[4, 10,20,30,40,50,100],
			   	rowNum:4,
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
    		var datePicker = new tempusDominus.TempusDominus(document.getElementById('datePicker'), {
				restrictions: {
					minDate : new Date()
				},
				display : {
					components : {
						useTwentyfourHour : true
					},
					buttons : {
						today : true,
						close : true
					},
					sideBySide : true
				}
			});
			
			datePicker.dates.formatInput = function(date) {
				return date ? date.year + '-' + date.monthFormatted + '-' + date.dateFormatted + ' ' + date.hoursFormatted + ':' + date.minutesFormatted : '';
			};
    		
			$('#accordion .accordion-collapse').on('show.bs.collapse', function () {
				$("#emailButton").attr('value', 
						$(this).is("#nowDiv") ? '<spring:escapeBody javaScriptEscape='true'><fmt:message key="email.notifications.send"/></spring:escapeBody>'
											  : '<spring:escapeBody javaScriptEscape='true'><fmt:message key="email.notifications.button.schedule"/></spring:escapeBody>');
			});
			
    		$('#emailButton').click(function() {
    			
    			var isInstantEmailing = $('#accordion .accordion-collapse.show').is('#nowDiv');
    			var ids = jQuery("#list3").getGridParam('selarrrow');

    			var searchType = document.getElementById("searchType").value;
    			var params = "&searchType=" + searchType;
    		
    			if (isInstantEmailing && ids.length) {
    				for (var i=0;i<ids.length;i++) {
    			    	params += "&userId=" + ids[i];
    			    }
    			    params += '&lessonID=${lesson.lessonId}';
    			} else if (isInstantEmailing && !ids.length) {
    				return;
    				
    			//in case of scheduling
    			} else {
    				//get the timestamp in milliseconds since midnight Jan 1, 1970
    				var scheduleDate = datePicker.viewDate;
    				if (scheduleDate == null) {
    					return;
    				}
        			       			
    				params += "&scheduleDate=" + scheduleDate.getTime() + getSearchParams();
    			}
    			
    			var emailBody = encodeURIComponent(document.getElementById("emailBody").value);
    			
    	        $.ajax({
    	        	async: false,
    	            url: '<c:url value="/emailNotifications/emailUsers.do"/>?<csrf:token/>',
    	            data: "emailBody=" + emailBody + params,
    	            dataType: 'json',
    	            type: 'post',
    	            success: function (json) {
        	            let showToast = window.parent.showToast;
        	            
    		            if (json.isSuccessfullySent) {
    		            	showToast("<spring:escapeBody javaScriptEscape='true'><fmt:message key="email.notifications.emails.successfully.sent" /></spring:escapeBody>");
    					} else if (json.isSuccessfullyScheduled) {
    						showToast("<spring:escapeBody javaScriptEscape='true'><fmt:message key="email.notifications.emails.successfully.scheduled" /></spring:escapeBody>");
    					} else {
    						showToast("<spring:escapeBody javaScriptEscape='true'><fmt:message key="email.notifications.problems.sending.emails" /></spring:escapeBody>");
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
    
<body class="p-3">
	<div class="clearfix">
		<div class="float-end">
			<a href="<c:url value='/emailNotifications/showScheduledEmails.do'/>?newUI=true&lessonID=${lesson.lessonId}"
			   id="listEmailsHref" class="btn btn-secondary btn-sm">
				<i class="fa fa-calendar"></i> <fmt:message key="email.notifications.scheduled.messages.button" />
			</a>
			<a href="<c:url value='/emailNotifications/showArchivedEmails.do'/>?newUI=true&lessonID=${lesson.lessonId}"
			   id="archiveHref" class="btn btn-secondary btn-sm">
				<i class="fa fa-archive"></i> <fmt:message key="email.notifications.archived.messages.button" />
			</a>		
		</div>
	</div>
	<div class="row">
		<div class="col-sm-6">
			<h4><fmt:message key="email.notifications.notify.sudents.that"/></h4>
			
			<form:form action="emailUsers.do" method="post" modelAttribute="emailNotificationsForm" id="emailNotificationsForm" >	
			
			
			<!-- Dropdown menu for choosing a user search type -->
			<select id="searchType" onchange="getUsers();" class="form-select mb-2">
				<option selected="selected" value="0"><fmt:message key="email.notifications.user.search.property.0" /></option>
				<option value="1"><fmt:message key="email.notifications.user.search.property.1" /></option>
				<option value="2"><fmt:message key="email.notifications.user.search.property.2" /></option>
				<option value="3"><fmt:message key="email.notifications.user.search.property.3" /></option>
				<option value="4"><fmt:message key="email.notifications.user.search.property.4" /></option>
				<option value="5"><fmt:message key="email.notifications.user.search.property.5" /></option>
				<option value="6"><fmt:message key="email.notifications.user.search.property.6" /></option>
			</select>
			
			<div id="additionalParameters" class="mb-2">
				<div id="activityDiv">
					<h4>
						<fmt:message key="email.notifications.activity" />
					</h4>
					<select id="activityId" onchange="getUsers();" class="form-select mb-2">
						<c:forEach var="activity" items="${activities}" varStatus="i">
							<option <c:if test="${i.index==0}">selected="selected"</c:if> value="${activity.activityId}"><c:out value="${activity.title}"/></option>
						</c:forEach>
					</select>				
				</div>
				
				<div id="daysToDeadlineDiv">
					<h3>
						<fmt:message key="email.notifications.days.to.deadline" />
					</h3>
					<div class="row">
					  <div class="col">
					    <input name="daysToDeadline" type="number" min="1" max="999" id="daysToDeadline" value="3" size="3" class="form-control"/> 	
					  </div>
					  <div class="col">
					   	<input class="btn btn-secondary" type="button" value="<fmt:message key="button.ok"/>" onclick="getUsers();" />
					  </div>
					</div>
				</div>
			</div>
			
			<%@ include file="additionalSettings5.jsp"%>
			
			</form:form>			
		</div>
		<div class="col-sm-6">
			<div id="emailTextareaDiv">
				<h4><fmt:message key="email.notifications.message.header"/></h4>
				<c:set var="emailBody"><fmt:message key="email.notifications.lesson.email.body.header"/><br/><br/><fmt:message key="email.notifications.lesson.email.body.msg"/><br/><br/><br/><fmt:message key="email.notifications.lesson.email.body.footer" ><fmt:param>${lesson.lessonName}</fmt:param><fmt:param><lams:LAMSURL/>home/learner.do?lessonID=${lesson.lessonId}</fmt:param></fmt:message>
				</c:set>
				<textarea rows="17" name="emailBody" id="emailBody" width="100%" class="form-control">${fn:replace(emailBody, '<br/>', newLineChar)}</textarea>
				<br/>
				<input class="btn btn-primary mt-3 float-end" type="button" id="emailButton" value="<fmt:message key="email.notifications.send"/>" />
			</div>		
		</div>
	</div>
</body>
</lams:html>
