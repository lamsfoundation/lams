<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL/></c:set>
<%@ page import="org.lamsfoundation.lams.tool.survey.SurveyConstants"%>
<lams:html>
<lams:head>
	    <%@ include file="/common/header.jsp" %>
	    <link type="text/css" href="${lams}css/jquery.tablesorter.theme.bootstrap.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/jquery.tablesorter.pager.css" rel="stylesheet">

		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
		<lams:JSImport src="includes/javascript/portrait.js" />
</lams:head>
<script type="text/javascript">

	$(document).ready(function(){
	    
		$(".tablesorter").tablesorter({
			theme: 'bootstrap',
			headerTemplate : '{content} {icon}',
		    sortInitialOrder: 'desc',
            sortList: [[0]],
            widgets: [ "uitheme", "resizable", "filter" ],
            headers: { 1: { sorter: false, filter: false} }, 
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
				ajaxUrl : "<c:url value='/monitoring/getReflectionsJSON.do'/>?page={page}&size={size}&{sortList:column}&{filterList:fcol}&toolSessionID=" + $(this).attr('data-session-id'),
				ajaxProcessing: function (data, table) {
					if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i];
							
							rows += '<tr>';
							rows += '<td>';
							rows += definePortraitPopover(userData["portraitId"], userData["userId"],  userData["userName"],  userData["userName"]);
							rows += '</td>';

							rows += '<td>';
							if ( userData["notebookEntry"] ) {
								rows += userData["notebookEntry"];
							} else {
								rows += '-';
							}
							rows += '</td>';
													
							rows += '</tr>';
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
<body class="stripes">

	<c:set var="title"><fmt:message key="title.reflection"/></c:set>
	<lams:Page type="monitor" title="${title}">

		<lams:TSTable numColumns="2" dataId='data-session-id="${param.toolSessionID}"'> 
			<th><fmt:message key="monitoring.user.fullname"/></th>
			<th><fmt:message key="monitoring.user.reflection"/></th>
		</lams:TSTable>
				
		<a href="javascript:window.close();" class="btn btn-default btn-sm">
		<fmt:message key="button.close"/>
		</a>

	</lams:Page>
</body>
</lams:html>