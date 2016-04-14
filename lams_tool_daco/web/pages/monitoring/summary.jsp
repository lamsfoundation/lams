 <%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="monitoringSummary" value="${sessionMap.monitoringSummary}" />
<c:set var="anyRecordsAvailable" value="false" />	
<c:set var="daco" value="${sessionMap.daco}"/>
				
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js"></script>
<script type="text/javascript">
	function exportSummary(){
		location.href = "<c:url value='/monitoring/exportToSpreadsheet.do'/>?sessionMapID=${sessionMapID}&reqID=" + (new Date()).getTime();
	};
	
  	$(document).ready(function(){
	    
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
                filter_placeholder: { search : '<fmt:message key="label.search"/>' }, 
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
							var userData = data.rows[i];
							
							rows += '<tr>';
							
							rows += '<td>';
							rows += 	userData["userFullName"];
							rows += '</td>';
							
							rows += '<td align="center">';
							rows += userData["recordCount"];
							rows += '</td>';
							rows += '<td align="center">';
							if ( userData["recordCount"] > 0) {
								var url = '<c:url value="/monitoring/listRecords.do"/>?sessionMapID=${sessionMapID}&toolSessionID='+$(table).attr('data-session-id')+'&sort=1&userId='+userData["userId"];
								var popUpTitle = '<fmt:message key="title.monitoring.recordlist" />';
								rows +=	'&nbsp;&nbsp;<a href="#" onclick="javascript:launchPopup(\''+url+'\',\''+popUpTitle+'\');" class="btn btn-default btn-xs">';
								rows += 'View Records'
								url = '<c:url value="/monitoring/getQuestionSummaries.do"/>?sessionMapID=${sessionMapID}&toolSessionID='+$(table).attr('data-session-id')+'&userId='+userData["userId"];
								popUpTitle = '<fmt:message key="tab.monitoring.statistics" />';
								rows += '</a>&nbsp;&nbsp;<a href="#" onclick="javascript:launchPopup(\''+url+'\',\''+popUpTitle+'\');" class="btn btn-default btn-xs">';
								rows += 'View Summary'
								rows += '</a>';
							} 
							rows += '</td>';

							
							if (${daco.reflectOnActivity}) {
								rows += '<td style="text-align: left">';
								rows += (userData["notebookEntry"]) ? userData["notebookEntry"] : '<fmt:message key="label.monitoring.notebook.none" />' ;
								rows += '</td>';
							}
							
							rows += '</tr>';
						}
			            
						json.total = data.total_rows;
						json.rows = $(rows);
						return json;
			            
			    	}				
				}})
			// bind to pager events
            .bind('pagerInitialized pagerComplete', function(event, options){
                initializeJRating();
            });
		});
  	})
</script>

<div class="panel">
<div class="pull-right">
	<c:if test="${not sessionMap.isGroupedActivity }">
		<c:forEach var="sessionSummary" items="${monitoringSummary}" varStatus="status">
			<c:url var="viewRecordList" value="/monitoring/listRecords.do?sessionMapID=${sessionMapID}&toolSessionID=${sessionSummary.sessionId}&sort=1" />
		</c:forEach>	
		<a href="#nogo" onclick="javascript:launchPopup('${viewRecordList}','RecordList')" class="btn btn-default btn-xs">
			<fmt:message key="label.monitoring.viewrecords.all" />
		</a>					
	</c:if>
	<a href="javascript:exportSummary();" class="btn btn-default btn-xs"><fmt:message key="button.export" /></a>
</div>

<h4>
  <c:out value="${daco.title}" escapeXml="true"/>
</h4>


<div class="instructions voffset5">
  <c:out value="${daco.instructions}" escapeXml="false"/>
</div>

</div>

<c:choose>
	<c:when test="${empty monitoringSummary}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:when>
	<c:otherwise>
		<c:if test="${sessionMap.isGroupedActivity}">
			<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
		</c:if>

		<c:forEach var="sessionSummary" items="${monitoringSummary}" varStatus="status">
			<c:url var="viewRecordList" value="/monitoring/listRecords.do?sessionMapID=${sessionMapID}&toolSessionID=${sessionSummary.sessionId}&sort=1" />
 		<c:if test="${sessionMap.isGroupedActivity}">
			    <div class="panel panel-default" >
			        <div class="panel-heading" id="heading${sessionSummary.sessionId}">
						<span class="panel-title collapsable-icon-left">
			        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${sessionSummary.sessionId}" 
							aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${sessionSummary.sessionId}" >
						<fmt:message key="label.learning.tableheader.summary.group" />: <c:out value="${sessionSummary.sessionName}"/></a>
						</span>
 						<a href="#nogo" onclick="javascript:launchPopup('${viewRecordList}','RecordList')" class="btn btn-default btn-xs pull-right">
						<fmt:message key="label.monitoring.viewrecords.all" />
						</a>					
			        </div>
			        <div id="collapse${sessionSummary.sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${sessionSummary.sessionId}">
		</c:if>
		
					<!-- Details for the group / whole of class -->
					<c:set var="numColumns" value="3"/>
					<c:if test="${daco.reflectOnActivity}">
						<c:set var="numColumns" value="4"/>
					</c:if>
				
					<lams:TSTable numColumns="${numColumns}" dataId="data-session-id='${sessionSummary.sessionId}'">
						<th><fmt:message key="label.monitoring.fullname" /></th>
						<th align="center"  width="8%"><fmt:message key="label.monitoring.recordcount" /></th>
						<th align="center"  width="25%"></th>
						<c:if test="${daco.reflectOnActivity}">
							<th align="center"  width="35%"><fmt:message key="label.monitoring.notebook" /></th>
						</c:if>
					</lams:TSTable>

		<c:if test="${sessionMap.isGroupedActivity}">
					</div> <!-- end collapse area  -->
				</div> <!-- end collapse panel  -->
				${status.last ? '' : '<div class="voffset5">&nbsp;</div>'}
		</c:if>

		</c:forEach>
		<c:if test="${sessionMap.isGroupedActivity}">
			</div> <!-- end panel-group for accordian -->
		</c:if>

	</c:otherwise>
</c:choose>

<c:set var="adTitle"><fmt:message key="label.monitoring.advancedsettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
          	
<table class="table table-striped table-condensed">
	<tr>
		<td>
			<fmt:message key="label.common.min" />
		</td>
		<td>
			<c:choose>
				<c:when test="${daco.minRecords==0}">
					<fmt:message key="label.authoring.advanced.record.nolimit" />
				</c:when>
				<c:otherwise>
					${daco.minRecords}
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="label.common.max" />
		</td>
		<td>
			<c:choose>
				<c:when test="${daco.maxRecords==0}">
					<fmt:message key="label.authoring.advanced.record.nolimit" />
				</c:when>
				<c:otherwise>
					${daco.maxRecords}
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="label.authoring.advanced.notify.onlearnerentry" />
		</td>
		<td>
			<c:choose>
				<c:when test="${daco.notifyTeachersOnLearnerEntry}">
					<fmt:message key="label.monitoring.advancedsettings.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.monitoring.advancedsettings.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="label.authoring.advanced.notify.onrecordsubmit" />
		</td>
		<td>
			<c:choose>
				<c:when test="${daco.notifyTeachersOnRecordSumbit}">
					<fmt:message key="label.monitoring.advancedsettings.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.monitoring.advancedsettings.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.authoring.advanced.lock.on.finished" />
		</td>
		<td>
			<c:choose>
				<c:when test="${daco.lockOnFinished}">
					<fmt:message key="label.monitoring.advancedsettings.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.monitoring.advancedsettings.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="label.monitoring.advancedsettings.addNotebook" />
		</td>
		<td>
			<c:choose>
				<c:when test="${daco.reflectOnActivity}">
					<fmt:message key="label.monitoring.advancedsettings.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.monitoring.advancedsettings.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	<c:if test="${daco.reflectOnActivity}">
		<tr>
			<td>
				<fmt:message key="label.monitoring.advancedsettings.notebookinstructions" />
			</td>
			<td>
				<lams:out value="${daco.reflectInstructions}" escapeHtml="true"/>
			</td>
		</tr>
	</c:if>
</table>
</lams:AdvancedAccordian>
