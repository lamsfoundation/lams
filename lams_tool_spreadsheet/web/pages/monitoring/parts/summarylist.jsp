<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>

<div id="summaryList2">

<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">

<script type="text/javascript">

	$(document).ready(function(){

		$(".tablesorter").tablesorter({
			theme: 'bootstrap',
			headerTemplate : '{content} {icon}',
		    sortInitialOrder: 'desc',
            sortList: [[0]],
            widgets: [ "uitheme", "resizable", "filter" ],
            <c:choose>
			<c:when test="${spreadsheet.markingEnabled}">
            headers: { 1: { filter: false}, 2: { sorter: false, filter: false} }, 
            </c:when>
			<c:otherwise>
            headers: { 1: { sorter: false, filter: false} }, 
            </c:otherwise>
            </c:choose>
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
                ajaxUrl : "<c:url value='/monitoring/getUsers.do'/>?sessionMapID=${sessionMapID}&toolContentID=${param.toolContentID}&page={page}&size={size}&{sortList:column}&{filterList:fcol}&toolSessionID=" + $(this).attr('data-session-id'),
				ajaxProcessing: function (data, table) {
					if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i];

							rows += '<tr>';
							rows += '<td>';
							if ( ${spreadsheet.learnerAllowedToSave} ) {
								var reviewURL = '<c:url value="/reviewItem.do"/>?userUid='+userData["userUid"];
								rows += '<a href="javascript:launchPopup(\'' + reviewURL + '\')">' + userData["userName"] + '</a>';
							} else {
								rows += userData["userName"];
							}
							rows += '</td>';

							<c:if test="${spreadsheet.markingEnabled}">
							rows += '<td align="right"><span id="mark'+userData["userUid"]+'">'; 
							if ( userData["userModifiedSpreadsheet"] && userData["mark"] ) {
								rows += userData["mark"];
							} else {
								rows += '-';
							}
							rows += '</span>';
							if ( userData["userModifiedSpreadsheet"] ) {
								var editUrl = '<c:url value="/monitoring/editMark.do"/>?userUid=' + userData["userUid"] +'&toolContentID=${param.toolContentID}&sessionMapID=${sessionMapID}';
								rows += '<a href="javascript:launchPopup(\''+ editUrl +'\')" class="btn btn-default btn-xs loffset5"><fmt:message key="label.monitoring.summary.mark.button" /></a>';
							}
							rows += '</td>';
							</c:if>
							
							<c:if test="${spreadsheet.reflectOnActivity}">
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
			})
		});
  	})
</script>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="spreadsheet" value="${sessionMap.spreadsheet}"/>

<c:if test="${empty summaryList}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert>
</c:if>

<c:if test="${sessionMap.isGroupedActivity}">
<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

	<c:forEach var="summary" items="${summaryList}" varStatus="status">
	
		<c:if test="${sessionMap.isGroupedActivity}">
			<div class="panel panel-default" >
	        <div class="panel-heading" id="heading${summary.sessionId}">
	        	<span class="panel-title collapsable-icon-left">
	        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${summary.sessionId}" 
						aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse$${summary.sessionId}" >
				<fmt:message key="monitoring.label.group" />&nbsp;${summary.sessionName}</a>
				</span>
	        </div>
		        
		    <div id="collapse${summary.sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${summary.sessionId}">
		</c:if>
		
			<c:set var="numColumns" value="1"/>
			<c:if test="${spreadsheet.markingEnabled}"><c:set var="numColumns" value="${numColumns+1}"/></c:if>
			<c:if test="${spreadsheet.reflectOnActivity}"><c:set var="numColumns" value="${numColumns+1}"/></c:if>
			<lams:TSTable numColumns="${numColumns}" dataId='data-session-id="${summary.sessionId}"' test="fred"> 
				<th align="left">
					<fmt:message key="label.monitoring.summary.learner" />
				</th>
				<c:if test="${spreadsheet.markingEnabled}">			
					<th width="20%" align="center">
						<fmt:message key="label.monitoring.summary.marked" />
					</th>
				</c:if>
				<c:if test="${spreadsheet.reflectOnActivity}">			
					<th width="50%" align="center">
						<fmt:message key="label.monitoring.summary.reflection" />
					</th>
				</c:if>
			</lams:TSTable>
			
			<c:if test="${spreadsheet.markingEnabled}">	
				<p>
					<html:link href="javascript:viewAllMarks(${summary.sessionId});"
						property="viewAllMarks" styleClass="btn btn-default voffset10 loffset5">
						<fmt:message key="label.monitoring.summary.viewAllMarks.button" />
					</html:link>
					<html:link href="javascript:releaseMarks(${summary.sessionId});"
						property="releaseMarks" styleClass="btn btn-default voffset10 loffset5">
						<fmt:message key="label.monitoring.summary.releaseMarks.button" />
					</html:link>
	 				<html:link href="javascript:downloadMarks(${summary.sessionId});"
						property="downloadMarks" styleClass="btn btn-default voffset10 loffset5">
						<fmt:message key="label.monitoring.summary.downloadMarks.button" />
					</html:link>
					<c:url value="/monitoring/summary.do" var="refreshMonitoring">
						<c:param name="contentFolderID" value="${sessionMap.contentFolderID}"/>
						<c:param name="toolContentID" value="${sessionMap.toolContentID}" />
					</c:url>
					<html:link href="${refreshMonitoring}" styleClass="btn btn-default loffset5 voffset10" >
						<fmt:message key="label.refresh" />
					</html:link>
				</p>
			</c:if>
		
		<c:if test="${sessionMap.isGroupedActivity}">
			</div> <!-- end collapse area  -->
			</div> <!-- end collapse panel  -->
		</c:if>
	${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}

</c:forEach>

<c:if test="${sessionMap.isGroupedActivity}">
	</div> <!--  end panel group -->
</c:if>

</div>