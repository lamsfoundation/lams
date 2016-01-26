<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="monitoringSummary" value="${sessionMap.monitoringSummary}" />
<c:set var="anyRecordsAvailable" value="false" />
<c:url var="refreshSummaryUrl" value="/monitoring/summary.do?sessionMapID=${sessionMapID}"/>
<c:url var="statisticsUrl" value="/monitoring/statistic.do?sessionMapID=${sessionMapID}"/>
<c:set var="daco" value="${sessionMap.daco}"/>
				
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js"></script>
<script type="text/javascript">
	var gStatisticsUrl = "${statisticsUrl}"; // used by the tab functions
	
	function exportSummary(){
		location.href = "<c:url value='/monitoring/exportToSpreadsheet.do'/>?sessionMapID=${sessionMapID}&reqID=" + (new Date()).getTime();
	};
	
  	$(document).ready(function(){
	    
		$(".tablesorter").tablesorter({
			theme: 'blue',
		    sortInitialOrder: 'desc',
            sortList: [[0]],
            widgets: [ "resizable", "filter" ],
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
								rows +=	'&nbsp;&nbsp;<a href="#" onclick="javascript:launchPopup(\''+url+'\',\''+popUpTitle+'\');">';
								rows += 'View Records'
								url = '<c:url value="/monitoring/getQuestionSummaries.do"/>?sessionMapID=${sessionMapID}&toolSessionID='+$(table).attr('data-session-id')+'&userId='+userData["userId"];
								popUpTitle = '<fmt:message key="tab.monitoring.statistics" />';
								rows += '</a>&nbsp;&nbsp;<a href="#" onclick="javascript:launchPopup(\''+url+'\',\''+popUpTitle+'\');">';
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
<h1>
  <c:out value="${daco.title}" escapeXml="true"/>
</h1>
<div class="instructions space-bottom-top">
  <c:out value="${daco.instructions}" escapeXml="false"/>
</div>

<c:choose>
	<c:when test="${empty monitoringSummary}">
		<div align="center" style="font-weight: bold;">
			<fmt:message key="message.monitoring.summary.no.session" />
		</div>
	</c:when>
	<c:otherwise>
		<c:forEach var="sessionSummary" items="${monitoringSummary}">
			<c:url var="viewRecordList" value="/monitoring/listRecords.do?sessionMapID=${sessionMapID}&toolSessionID=${sessionSummary.sessionId}&sort=1" />
			<div id="${sessionSummary.sessionId}" class="space-bottom"/>
			<c:if test="${sessionMap.isGroupedActivity}">
				<h1 style="display: inline">
					<fmt:message key="label.learning.tableheader.summary.group" />: <c:out value="${sessionSummary.sessionName}"/>
				</h1>
				<a href="#nogo" onclick="javascript:launchPopup('${viewRecordList}','RecordList')" class="button float-right">
					<fmt:message key="label.monitoring.viewrecords.all" />
				</a>					
			</c:if>
		
			<div class="tablesorter-holder">
			<table class="tablesorter" data-session-id="${sessionSummary.sessionId}">
				<thead>
					<tr>
						<th><fmt:message key="label.monitoring.fullname" /></th>
						<c:choose>
						<c:when test="${daco.reflectOnActivity}">
						<th align="center"  width="8%"><fmt:message key="label.monitoring.recordcount" /></th>
						<th align="center"  width="25%"></th>
						<th align="center"  width="35%"><fmt:message key="label.monitoring.notebook" /></th>
						</c:when>
						<c:otherwise>
						<th align="center"  width="8%"><fmt:message key="label.monitoring.recordcount" /></th>
						<th align="center"  width="25%"></th>
						</c:otherwise>
						</c:choose>
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

			</div> <!-- end tablesorter-holder -->
			</div>

		</c:forEach>
	</c:otherwise>
</c:choose>
<p>
	<a href="#nogo"  class="button" onclick="javascript:document.location.href='${refreshSummaryUrl}';">
		<fmt:message key="label.common.summary.refresh" />
	</a>
	<c:if test="${! empty viewRecordList}">
		<c:if test="${! sessionMap.isGroupedActivity }">
		<a href="#nogo" onclick="javascript:launchPopup('${viewRecordList}','RecordList')" class="button space-left">
			<fmt:message key="label.monitoring.viewrecords.all" />
		</a>					
		</c:if>
	<html:link href="javascript:exportSummary();" styleClass="button space-left">
		<fmt:message key="button.export" />
	</html:link>
	</c:if>
</p>

<h2>
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" 
		onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="label.monitoring.advancedsettings" />
	</a>
</h2>
<br />

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
	<table class="alternative-color">
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
</div>
