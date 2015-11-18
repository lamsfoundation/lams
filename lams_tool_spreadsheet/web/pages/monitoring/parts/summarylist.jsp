<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>

<div id="summaryList2">

<link type="text/css" href="${lams}css/jquery.tablesorter.theme-blue.css" rel="stylesheet">
<link type="text/css" href="${lams}css/jquery.tablesorter.pager.css" rel="stylesheet">

<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>

<script type="text/javascript">
	// use noConflict() otherwise jQuery conflicts with prototype.js
	jQuery.noConflict();
	jQuery(document).ready(function($){
	    
		$(".tablesorter").tablesorter({
			theme: 'blue',
		    sortInitialOrder: 'desc',
            sortList: [[0]],
            widgets: [ "resizable", "filter" ],
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
								rows += '<a href="javascript:launchPopup(\'' + reviewURL + '\')" style="margin-left: 7px;" styleClass="button">' + userData["userName"] + '</a>';
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
								rows += '<a href="javascript:editMark(\''+userData["userUid"]+'\')" style="margin-left: 7px;"><fmt:message key="label.monitoring.summary.mark.button" /></a>';
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

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="spreadsheet" value="${sessionMap.spreadsheet}"/>

<c:if test="${empty summaryList}">
	<div align="center">
		<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
	</div>
</c:if>

	<c:forEach var="summary" items="${summaryList}">
		<c:if test="${sessionMap.isGroupedActivity}">
			<h1>
				<fmt:message key="monitoring.label.group" /> ${summary.sessionName}	
			</h1>
		</c:if>
		
		<h2 style="color:black; margin-left: 20px;"><fmt:message key="label.monitoring.summary.overall.summary" />	</h2>
		
		<div class="tablesorter-holder">
		<table class="tablesorter" data-session-id="${summary.sessionId}">
			<thead>
				<tr>
					<th align="left">
						<fmt:message key="label.monitoring.summary.learner" />
					</th>
					<c:if test="${spreadsheet.markingEnabled}">			
						<th width="60px" align="center">
							<fmt:message key="label.monitoring.summary.marked" />
						</th>
					</c:if>
					<c:if test="${spreadsheet.reflectOnActivity}">			
						<th width="50%" align="center">
							<fmt:message key="label.monitoring.summary.reflection" />
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
	
		
		<c:if test="${spreadsheet.markingEnabled}">	
			<div class="space-bottom-top" style="position:relative; left:30px; ">
				<html:link href="javascript:viewAllMarks(${summary.sessionId});"
					property="viewAllMarks" styleClass="button">
					<fmt:message key="label.monitoring.summary.viewAllMarks.button" />
				</html:link>
				<html:link href="javascript:releaseMarks(${summary.sessionId});"
					property="releaseMarks" styleClass="button">
					<fmt:message key="label.monitoring.summary.releaseMarks.button" />
				</html:link>
 				<html:link href="javascript:downloadMarks(${summary.sessionId});"
					property="downloadMarks" styleClass="button">
					<fmt:message key="label.monitoring.summary.downloadMarks.button" />
				</html:link>
			</div>
		</c:if>
	</c:forEach>		
</div>