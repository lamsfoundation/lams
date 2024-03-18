 <%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="monitoringSummary" value="${sessionMap.monitoringSummary}" />
<c:set var="anyRecordsAvailable" value="false" />	
<c:set var="daco" value="${sessionMap.daco}" scope="request"/>

<%@ include file="/common/jqueryheader.jsp" %>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js"></script>
<script type="text/javascript" src="${lams}/includes/javascript/portrait5.js" ></script>
<script type="text/javascript">
	MONITORING_STATISTIC_URL = "<c:url value="/monitoring/statistic.do?sessionMapID=${sessionMapID}"/>" + "&reqID=" + (new Date()).getTime();;

	function exportSummary(){
		location.href = "<c:url value='/monitoring/exportToSpreadsheet.do'/>?sessionMapID=${sessionMapID}&reqID=" + (new Date()).getTime();
	};
	
  	$(document).ready(function(){
  		doStatistic();
  		
		$(".tablesorter").tablesorter({
			theme: 'bootstrap',
			headerTemplate : '{content} {icon}',
		    sortInitialOrder: 'desc',
            sortList: [[0]],
            widgets: [ "uitheme", "resizable", "filter" ],
            headers: { 1: { filter: false}, 2: { filter: false, sorter: false }, 3: { filter: false, sorter: false }, 4: { filter: false, sorter: false }  }, 
            widgetOptions: {
            	resizable: true,
            	// include column filters 
                filter_columnFilters: true, 
                filter_placeholder: { search : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.search"/></spring:escapeBody>' }, 
                filter_searchDelay: 700 
            }
		});
		
		$(".tablesorter").each(function() {
			$(this).tablesorterPager({
				savePages: false,
                container: $(this).find(".ts-pager"),
                output: '{startRow} to {endRow} ({totalRows})',
                cssPageDisplay: '.pagedisplay',
                cssPageSize: '.pagesize',
                cssDisabled: 'disabled',
				ajaxUrl : "<c:url value='/monitoring/getUsers.do'/>?sessionMapID=${sessionMapID}&page={page}&size={size}&{sortList:column}&{filterList:fcol}&toolSessionID=" + $(this).attr('data-session-id'),
				ajaxProcessing: function (data, table) {
			    	if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i],
								userId = userData["userId"],
								fullName = userData["userFullName"];;
							
							rows += '<tr>' +
										'<td>' + definePortraitPopover(userData["portraitId"], userId, fullName, fullName) + '</td>' +
							
										'<td align="center">' +
											userData["recordCount"] +
										'</td>' +
										
										'<td align="center">';
							if ( userData["recordCount"] > 0) {
								var url = '<c:url value="/monitoring/listRecords.do"/>?sessionMapID=${sessionMapID}&toolSessionID='+$(table).attr('data-session-id')+'&sort=1&userId='+userData["userId"];
								var popUpTitle = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="title.monitoring.recordlist" /></spring:escapeBody>';
								rows +=		'<button type="button" onclick="launchPopup(\''+url+'\',\''+popUpTitle+'\')" class="btn btn-light btn-sm ms-2">' +
												'<i class="fa-solid fa-eye me-1"></i>' +
												'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.view.records" /></spring:escapeBody>' +
											'</button>'; 
								url = '<c:url value="/monitoring/getQuestionSummaries.do"/>?sessionMapID=${sessionMapID}&toolSessionID='+$(table).attr('data-session-id')+'&userId='+userData["userId"];
								popUpTitle = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.common.summary" /></spring:escapeBody>';
								rows += 	'<button type="button" onclick="launchPopup(\''+url+'\',\''+popUpTitle+'\')" class="btn btn-light btn-sm ms-2">' +
												'<i class="fa-solid fa-eye me-1"></i>' +
												'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.view.summary" /></spring:escapeBody>' +
											'</button>';
							} 
							rows += 	'</td>' +
									'</tr>';
						}
			            
						json.total = data.total_rows;
						json.rows = $(rows);
						return json;
			            
			    	}				
				}}).bind('pagerInitialized pagerComplete', function(event, options){
					initializePortraitPopover('${lams}');
	            })
			});
	  	})
</script>

<h1>
	<c:out value="${daco.title}" escapeXml="true"/>
</h1>

<div class="instructions">
	<c:out value="${daco.instructions}" escapeXml="false"/>
</div>

<c:choose>
	<c:when test="${empty monitoringSummary}">
		<lams:Alert5 type="info" id="no-session-summary">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert5>
	</c:when>
	
	<c:otherwise>
		<c:if test="${sessionMap.isGroupedActivity}">
			<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
		</c:if>

		<c:forEach var="sessionSummary" items="${monitoringSummary}" varStatus="status">
			<c:url var="viewRecordList" value="/monitoring/listRecords.do?sessionMapID=${sessionMapID}&toolSessionID=${sessionSummary.sessionId}&sort=1" />
			
 			<c:if test="${sessionMap.isGroupedActivity}">
			    <div class="lcard" >
			        <div class="card-header" id="heading${sessionSummary.sessionId}">
						<span class="card-title collapsable-icon-left">
				        	<button class="btn btn-secondary-darker no-shadow ${status.first ? '' : 'collapsed'}" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${sessionSummary.sessionId}" 
									aria-expanded="${status.first}" aria-controls="collapse${sessionSummary.sessionId}" >
								<fmt:message key="label.learning.tableheader.summary.group" />: <c:out value="${sessionSummary.sessionName}"/>
							</button>
						</span>
						
 						<button type="button" onclick="launchPopup('${viewRecordList}','RecordList')" class="btn btn-light btn-sm no-shadow float-end">
 							<i class="fa-solid fa-eye me-1"></i>
							<fmt:message key="label.monitoring.viewrecords.all" />
						</button>				
			        </div>
			        
			        <div id="collapse${sessionSummary.sessionId}" class="card-collapse collapse ${status.first ? 'show' : ''}">
			</c:if>
		
			<!-- Details for the group / whole of class -->
			<lams:TSTable5 numColumns="3" dataId="data-session-id='${sessionSummary.sessionId}'">
				<th><fmt:message key="label.monitoring.fullname" /></th>
				<th align="center" width="8%"><fmt:message key="label.monitoring.recordcount" /></th>
				<th align="center" width="35%"></th>
			</lams:TSTable5>

			<c:if test="${sessionMap.isGroupedActivity}">
					</div> <!-- end collapse area  -->
				</div> <!-- end collapse panel  -->
			</c:if>
		</c:forEach>
		
		<c:if test="${sessionMap.isGroupedActivity}">
			</div> <!-- end panel-group for accordian -->
		</c:if>

		<div class="clearfix">
			<c:if test="${not sessionMap.isGroupedActivity }">
				<c:forEach var="sessionSummary" items="${monitoringSummary}" varStatus="status">
					<c:url var="viewRecordList" value="/monitoring/listRecords.do?sessionMapID=${sessionMapID}&toolSessionID=${sessionSummary.sessionId}&sort=1" />
				</c:forEach>
					
				<button type="button" onclick="launchPopup('${viewRecordList}','RecordList')" class="btn btn-light btn-sm float-end ms-2">
					<i class="fa-solid fa-eye me-1"></i>
					<fmt:message key="label.monitoring.viewrecords.all" />
				</button>					
			</c:if>
			
			<button type="button" onclick="exportSummary()" class="btn btn-light btn-sm float-end">
				<i class="fa fa-download me-1" aria-hidden="true"></i>
				<fmt:message key="button.export" />
			</button>
		</div>
	</c:otherwise>
</c:choose>

<h2 class="card-subheader fs-4" id="header-statistics">
	<fmt:message key="tab.monitoring.statistics" />
</h2>
<%@ include file="statistics.jsp"%>

 <h2 class="card-subheader fs-4" id="header-settings">
	Settings
</h2>
<%@ include file="editactivity.jsp" %>
