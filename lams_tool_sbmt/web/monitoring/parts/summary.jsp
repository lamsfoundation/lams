<%@include file="/common/taglibs.jsp"%>

<%-- If you change this file, remember to update the copy made for CNG-12 --%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery.tablesorter.theme-blue.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery.tablesorter.pager.css" rel="stylesheet">

<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>	
<script type="text/javascript">
	// pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring.do?method=setSubmissionDeadline"/>',
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};
</script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
	    
		$(".tablesorter").tablesorter({
			theme: 'blue',
		    sortInitialOrder: 'desc',
            sortList: [[0]],
            widgets: [ "resizable", "filter" ],
            headers: { 1: { filter: false}, 2: { filter: false }, 3: { sorter: false, filter: false} }, 
            widgetOptions: {
            	resizable: true,
            	// include column filters 
                filter_columnFilters: true, 
                filter_placeholder: { search : '<fmt:message key="label.search"/>' }, 
                filter_searchDelay: 700 
            }
		});
		
		$(".tablesorter").each(function() {
			$(this).tablesorterPager({
				savePages: false,
				ajaxUrl : "<c:url value='/monitoring.do'/>?method=getUsers&sessionMapID=${sessionMapID}&toolContentID=${param.toolContentID}&page={page}&size={size}&{sortList:column}&{filterList:fcol}&toolSessionID=" + $(this).attr('data-session-id'),
				ajaxProcessing: function (data, table) {
					if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i];

							rows += '<tr>';
							rows += '<td>'
							rows += userData["fullName"];
							rows += '</td>';
							rows += '<td align="center">'
							rows += userData["numFiles"];
							rows += '</td>';
							
							rows += '<td align="center">';
							if ( userData["marked"] ) {
								rows += '<fmt:message key="label.yes"/>';
							} else {
								rows += '<fmt:message key="label.no"/>';
							}
							if ( userData["numFiles"] > 0 ) {
								rows += ' [<a href=\"javascript:viewMark('+userData["userID"]+','+$(table).attr("data-session-id")+');\"><fmt:message key="label.monitoring.Mark.button" /></a>]';
							} 
							rows += '</td>';

							<c:if test="${reflectOn}">
							rows += '<td>';
							if ( userData["reflection"] ) {
								rows += userData["reflection"];
							} else {
								rows += '-';
							}
							rows += '</td>';
							</c:if>
							
							rows += '</tr>';
						}
			            
						json.total = data.total_rows;
						json.rows = $(rows);
						return json;
			            
			    	}
				},					
			    container: $(this).next(".pager"),
			    output: '{startRow} to {endRow} ({totalRows})',
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


<script type="text/javascript">
	function launchPopup(url, title) {
		var wd = null;
		if (wd && wd.open && !wd.closed) {
			wd.close();
		}
		wd = window.open(url, title,
				'resizable,width=796,height=570,scrollbars');
		wd.window.focus();
	}

	function viewMark(userId, sessionId) {
		var act = "<c:url value="/monitoring.do"/>";
		launchPopup(act + "?method=listMark&userID=" + userId
				+ "&toolSessionID=" + sessionId, "mark");
	}
	function viewAllMarks(sessionId) {
		var act = "<c:url value="/monitoring.do"/>";
		launchPopup(act + "?method=listAllMarks&toolSessionID=" + sessionId,
				"mark");
	}

	function releaseMarks(sessionId) {
		var url = "<c:url value="/monitoring.do"/>";
		
		$("#messageArea_Busy").show();
		$("#messageArea").load(
			url,
			{
				method: "releaseMarks",
				toolSessionID: sessionId, 
				reqID: (new Date()).getTime()
			},
			function() {
				$("#messageArea_Busy").hide();
			}
		);
	}

	function downloadMarks(sessionId) {
		var url = "<c:url value="/monitoring.do"/>";
		var reqIDVar = new Date();
		var param = "?method=downloadMarks&toolSessionID=" + sessionId
				+ "&reqID=" + reqIDVar.getTime();
		url = url + param;
		location.href = url;
	}
</script>

<h1>
	<c:out value="${authoring.title}" escapeXml="true" />
</h1>
<div class="instructions space-top">
	<c:out value="${authoring.instruction}" escapeXml="false" />
</div>

<table cellpadding="0">
<tr><td>
	<img src="${tool}/images/indicator.gif" style="display:none" id="messageArea_Busy" />
	<span id="messageArea"></span>
</td></tr>
</table>
<c:if test="${empty sessions}">
	<fmt:message key="label.no.user.available" />
</c:if>

<c:forEach var="sessionDto" items="${sessions}">

		<c:if test="${isGroupedActivity}">
			<h1>
				<fmt:message key="label.session.name" />: 
				<c:out value="${sessionDto.sessionName}" />
			</h1>
		</c:if>
		<br/>
		
		<div class="tablesorter-holder">
		<table class="tablesorter" data-session-id="${sessionDto.sessionID}">
			<thead>
				<tr>
					<th>
						<fmt:message key="monitoring.user.fullname"/>
					</th>
					<th  width="15%" align="center">
						<fmt:message key="monitoring.user.submittedFiles"/>
					</th>
					<th width="15%" align="center">
						<fmt:message key="monitoring.marked.question"/>
					</th>
					<c:if test="${reflectOn}">			
						<th align="center">
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
		</div>
		
		<table>
		  <tr>
			<td align="center"><html:link href="javascript:viewAllMarks(${sessionDto.sessionID});"
				property="viewAllMarks" styleClass="button">
				<fmt:message key="label.monitoring.viewAllMarks.button" />
			</html:link></td>
			<td align="center"><html:link href="javascript:releaseMarks(${sessionDto.sessionID});"
				property="releaseMarks" styleClass="button">
				<fmt:message key="label.monitoring.releaseMarks.button" />
			</html:link></td>
			<td align="center" style="border-bottom: 12px;border-bottom-color: gray;"><html:link href="javascript:downloadMarks(${sessionDto.sessionID});"
				property="downloadMarks" styleClass="button">
				<fmt:message key="label.monitoring.downloadMarks.button" />
			</html:link></td>
		</tr>
	</table>
	<br/>
</c:forEach>

<br/>

<h1 style="padding-bottom: 10px;">
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
<table class="alternative-color">

	<tr>
		<td>
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${authoring.lockOnFinished}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.limit.number.upload" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${authoring.limitUpload}">
					<fmt:message key="label.on" />, ${authoring.limitUploadNumber}
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.authoring.advanced.notify.mark.release" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${authoring.notifyLearnersOnMarkRelease}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="label.authoring.advanced.notify.onfilesubmit" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${authoring.notifyTeachersOnFileSubmit}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="monitor.summary.td.addNotebook" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${authoring.reflectOnActivity}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${authoring.reflectOnActivity}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>
					<lams:out value="${authoring.reflectInstructions}" escapeHtml="true"/>	
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>
</div>

<%@include file="daterestriction.jsp"%>