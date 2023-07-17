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
		<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/jquery-ui.timepicker.css" rel="stylesheet">
		<lams:css suffix="jquery.jRating"/>
		<link rel="stylesheet" type="text/css" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${lams}css/jquery.tablesorter.pager.css">
		 
		<lams:JSImport src="includes/javascript/common.js" />
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script> 
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	
		<script type="text/javascript">
	
			$(document).ready(function(){
				$(".tablesorter").tablesorter({
					theme: 'bootstrap',
				    widthFixed: true,
					widgets: ["uitheme", "zebra", "filter", "resizable"],
					headerTemplate : '{content} {icon}',
					headers: { 1: { filter: false, sorter: false} },
				    widgetOptions : {
		            	resizable: true,
				        // include column filters
				        filter_columnFilters: true,
			    	    filter_placeholder: { search : '<fmt:message key="label.search"/>' },
			    	    filter_searchDelay: 700
			    	    
			      	},
				    sortInitialOrder: 'desc',
		            sortList: [[0]]
				});
				
				$(".tablesorter").each(function() {
					$(this).tablesorterPager({
						// set to false otherwise it remembers setting from other jsFiddle demos
						savePages: false,
						container: $(this).find(".ts-pager"),
		                output: '{startRow} to {endRow} ({totalRows})',
		                cssPageDisplay: '.pagedisplay',
		                cssPageSize: '.pagesize',
		                cssDisabled: 'disabled',
						ajaxUrl : "<lams:WebAppURL/>monitoring/getReflectionsJSON.do?page={page}&size={size}&{sortList:column}&{filterList:fcol}&toolSessionID=" + $(this).attr('data-session-id'),
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