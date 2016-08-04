<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
	<lams:head>
		<title> <fmt:message key="label.view.reflection"/> </title>
		
		<lams:css />
		<link type="text/css" href="${lams}css/jquery-ui-smoothness-theme.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/jquery-ui.timepicker.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
		<link rel="stylesheet" type="text/css" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${lams}css/jquery.tablesorter.pager.css">
		 
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script> 
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	
		<script type="text/javascript">
	
			$(document).ready(function(){
				$(".tablesorter").tablesorter({
					theme: 'blue',
				    sortInitialOrder: 'desc',
		            sortList: [[0]],
		            widgets: [ "resizable", "filter" ],
		            headers: { 1: { filter: false, sorter: false} }, 
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
						ajaxUrl : "<lams:WebAppURL/>monitoring.do?dispatch=getReflectionsJSON&page={page}&size={size}&{sortList:column}&{filterList:fcol}&toolSessionID=" + $(this).attr('data-session-id'),
						ajaxProcessing: function (data, table) {
							if (data && data.hasOwnProperty('rows')) {
					    		var rows = [],
					            json = {};
					    		
								for (i = 0; i < data.rows.length; i++){
									var userData = data.rows[i];
		
									rows += '<tr>';
									rows += '<td>';
									rows += userData["username"];
									rows += '</td>';
		
									rows += '<td>';
									if ( userData["notebook"] ) {
										rows += userData["notebook"];
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
		  	});
		</script>	
	</lams:head>
<body class="stripes">

	<c:set var="title"><fmt:message key="label.notebook.entries" /></c:set>
	<lams:Page title="${title}" type="monitoring" >
		<lams:TSTable numColumns="2" dataId="data-session-id='${param.toolSessionID}'">
			<th><fmt:message key="label.user"/></th>
			<th><fmt:message key="label.reflection" /></th>
		</lams:TSTable>
		<div id="footer"></div>
	</lams:Page>

</body>
</lams:html>