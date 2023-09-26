<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title> <fmt:message key="label.learnersVoted"/> </title>

		<%@ include file="/common/monitorheader.jsp"%>

		<script type="text/javascript">
	
			$(document).ready(function(){
	    
			$(".tablesorter").tablesorter({
				theme: 'bootstrap',
			    sortInitialOrder: 'desc',
	            sortList: [[0]],
	            headerTemplate : '{content} {icon}',
	            widgets: [ "uitheme", "resizable", "filter" ],
	            headers: { 1: { filter: false} }, 
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
				ajaxUrl : "<lams:WebAppURL/>monitoring/getVoteNominationsJSON.do?page={page}&size={size}&{sortList:column}&{filterList:fcol}&questionUid=${questionUid}&sessionUid=" + $(this).attr('data-session-id'),
				ajaxProcessing: function (data, table) {
					if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i];

							rows += '<tr>';
							rows += '<td>';
							rows += definePortraitPopover(userData["portraitId"], userData["userId"], userData["userName"], userData["userName"]);
							rows += '</td>';

							rows += '<td>';
							rows += '<time class="timeago" title="';
							rows += userData["attemptTime"];
							rows += '" datetime="';
							rows += userData["attemptTimeTimeago"];
							rows += '"></time>';
							rows += '</td>';

							rows += '</tr>';
						}
			            
						json.total = data.total_rows;
						json.rows = $(rows);
						return json;
			            
			    	}
				}				
			})
			
			// bind to pager events
			.bind('pagerInitialized pagerComplete', function(event, options){
				$("time.timeago").timeago();
				initializePortraitPopover('<lams:LAMSURL />');
			})
		});
  	})

</script>	
</lams:head>
<body class="stripes">
	<c:set var="title"><fmt:message key="label.learnersVoted"/></c:set>
	<lams:Page type="learner" title="${title}">
		
		<h4><fmt:message key="label.learnersVoted"/>: ${nominationText}</h4>

		<lams:TSTable numColumns="2" dataId="data-session-id='${sessionUid}'">
	       <th><fmt:message key="label.user"/></td>
	       <th><fmt:message key="label.attemptTime"/></td>
	     </lams:TSTable>
		
	<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>
