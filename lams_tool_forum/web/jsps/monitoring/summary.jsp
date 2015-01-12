<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="forum" value="${sessionMap.forum}" />

<%-- If you change this file, remember to update the copy made for CNG-12 --%>

<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">
<link type="text/css" href="${lams}css/jquery.tablesorter.theme-blue.css" rel="stylesheet">
<link type="text/css" href="${lams}css/jquery.tablesorter.pager.css" rel="stylesheet">

<style media="screen,projection" type="text/css">
	#message-area {
		margin-bottom: 20px;
		display: none;
	}
	
	.collapsed-headers {
		padding-bottom: 20px;
	}
	
	#buttons {
		margin-bottom: 80px;
		margin-top: 20px;
	}
	
	.tablesorter, .pager {
		width: 97%;
		margin-left: 10px;
	}
</style>

<script type="text/javascript">
	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${sessionMap.submissionDeadline}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>',
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>  
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>

<script type="text/javascript">

	function releaseMarks(sessionId){
		var url = "<c:url value="/monitoring/releaseMark.do"/>";
		
		$("#message-area-busy").show();
		$("#message-area").load(
			url,
			{
				toolSessionID: sessionId, 
				reqID: (new Date()).getTime()
			},
			function() {
				$("#message-area").show("slow").effect("highlight", {}, 2000);;
				$("#message-area-busy").hide();
			}
		);
	}
	
  	$(document).ready(function(){
	    
		$(".tablesorter").tablesorter({
			theme: 'blue',
		    widthFixed: true,
		    sortInitialOrder: 'desc',
            sortList: [[0]] 
		});
		
		$(".tablesorter").each(function() {
			$(this).tablesorterPager({
				savePages: false,
				ajaxUrl : "<c:url value='/monitoring/getUsers.do'/>?sessionMapID=${sessionMapID}&page={page}&size={size}&{sortList:column}&sessionId=" + $(this).attr('data-session-id'),
				ajaxProcessing: function (data, table) {
			    	if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i];
							
							rows += '<tr>';
							
							rows += '<td>';
							rows += 	userData["userName"];
							rows += '</td>';
							
							rows += '<td>';
							if ( userData["lastMessageDate"] != null) {
								rows += userData["lastMessageDate"];
							}
							rows += '</td>';
							
							rows += '<td  align="right">';
							rows += 	userData["numberOfPosts"];
							rows += '</td>';

							//anyPostsMarked column
							rows += '<td align="center">';
							if (userData["numberOfPosts"] == 0) {
								rows += "-";
								
							} else {
								var anyPostsMarked = (userData["anyPostsMarked"]) ? '<fmt:message key="label.yes"/>' : '<fmt:message key="label.no"/>';
								rows += anyPostsMarked;	
								
								var viewUserMarkUrl = '<c:url value="/monitoring/viewUserMark.do"/>?sessionMapID=${sessionMapID}&userUid=' + userData["userUid"] + "&toolSessionID=" + $(table).attr('data-session-id');
								rows += 	'<a href="javascript:launchPopup(\'' + viewUserMarkUrl + '\')" style="margin-left: 7px;" styleClass="button">';
								rows += 		'<fmt:message key="lable.topic.title.mark" />';
								rows += 	'</a>';
							}
							rows += '</td>';
							
							if (${forum.reflectOnActivity}) {
								rows += '<td>';
								rows += 	(userData["notebookEntry"] == 'null') ? '-' : userData["notebookEntry"];
								rows += '</td>';
							}
							
							rows += '</tr>';
						}
			            
						json.total = data.total_rows;
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
		});
  	})
</script>

<h1>
    <c:out value="${title}" escapeXml="true"/>
</h1>
<div class="instructions space-top">
    <c:out value="${instruction}" escapeXml="false"/>
</div>
<br/>

<c:if test="${empty sessionMap.sessionDtos}">
	<p>
		<fmt:message key="message.monitoring.summary.no.session" />
	</p>
</c:if>

<c:forEach var="sessionDto" items="${sessionMap.sessionDtos}">
	
	<!--For release marks feature-->
	<img src="${tool}/images/indicator.gif" style="display:none" id="message-area-busy" />
	<div id="message-area"></div>

	<c:if test="${sessionMap.isGroupedActivity}">	
		<h2>
			<fmt:message key="message.session.name" />:	<c:out value="${sessionDto.sessionName}" />
		</h2>
	</c:if>
	
	<table class="tablesorter" data-session-id="${sessionDto.sessionID}">
		<thead>
			<tr>
				<th width="20%">
					<fmt:message key="monitoring.user.fullname"/>
				</th>
				<th <c:if test="${forum.reflectOnActivity}">width="30%"</c:if>>
					<fmt:message key="label.latest.posting.date"/>
				</th>
				<th width="100px">
					<fmt:message key="label.number.of.posts"/>
				</th>
				<th width="130px" align="center" class="sorter-false">
					<fmt:message key="monitoring.marked.question"/>
				</th>
				<c:if test="${forum.reflectOnActivity}">
					<th width="30%" align="center" class="sorter-false">
						<fmt:message key="monitoring.user.reflection"/>
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

	<div id="buttons">
		<div style="float:left;padding:5px;margin-left:5px">
			<html:form action="/learning/viewForum.do" target="_blank">
				<html:hidden property="mode" value="teacher"/>
				<html:hidden property="toolSessionID" value="${sessionDto.sessionID}" />
				<html:hidden property="hideReflection" value="true"/>
				<html:submit property="viewForum" styleClass="button">
					<fmt:message key="label.monitoring.summary.view.forum" />
				</html:submit>
			</html:form>
		</div>
		<div style="float:left;padding:5px;margin-left:5px">
			<html:button property="releaseMarks" onclick="releaseMarks(${sessionDto.sessionID})" styleClass="button">
				<fmt:message key="button.release.mark" />
			</html:button>
		</div>
		<div style="float:left;padding:5px;margin-left:5px">
			<html:form action="/monitoring/downloadMarks">
				<html:hidden property="toolSessionID" value="${sessionDto.sessionID}" />
				<html:submit property="downloadMarks" styleClass="button">
					<fmt:message key="message.download.marks" />
				</html:submit>
			</html:form>
		</div>
		<div style="float:left;padding:9px">
			<c:url value="/monitoring.do" var="refreshMonitoring">
				<c:param name="contentFolderID" value="${contentFolderID}"/>
				<c:param name="toolContentID" value="${toolContentID}" />
			</c:url>
			<html:link href="${refreshMonitoring}" styleClass="button">
					<fmt:message key="label.refresh" />
			</html:link>
		</div>
	</div>
</c:forEach>

<%@include file="parts/advanceOptions.jsp"%>

<%@include file="parts/daterestriction.jsp"%>
