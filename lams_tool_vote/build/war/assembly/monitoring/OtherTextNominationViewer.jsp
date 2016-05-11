<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title> <fmt:message key="label.learnersVoted"/> </title>

		<%@ include file="/common/monitorheader.jsp"%>

		<script type="text/javascript">
	
			function submitOpenVote(currentUid, actionMethod) {

		        var submitUid = currentUid;
		        $.ajax({
  					url: '<lams:WebAppURL/>/monitoring.do?dispatch='+actionMethod+'&currentUid='+submitUid,
				}).done(function( data ) {
					if ( data.currentUid == submitUid ) {
						$('#entryTable').trigger('pagerUpdate');
						return;
						
// 						var nextActionMethod = data.nextActionMethod;
// 						if ( nextActionMethod ) {
// 						$('table').trigger('pagerUpdate');
// 							var newLink = buildShowHideLink(currentUid, nextActionMethod);
// 							$('#link'+data.currentUid).html(newLink);
//						}
					}
					alert("An error has occurred. Please reload screen! [currentUid = "+data.currentUid+" nextActionMethod="+data.nextActionMethod+"]");
				});
			}
			
			function buildShowHideLink(currentUid, actionMethod) {
				var str = '<a href="#" onclick="javascript:submitOpenVote(\''+currentUid+'\', \''+actionMethod+'\');"  class="linkbutton">';
				if ( actionMethod == 'hideOpenVote' ) 
					str += '<fmt:message key="label.hide"/></a>';
				else
					str += '<fmt:message key="label.show"/></a>';
				return str;
			}

			$(document).ready(function(){
	    
			$(".tablesorter").tablesorter({
				theme: 'blue',
			    sortInitialOrder: 'desc',
	            sortList: [[0]],
	            widgets: [ "resizable", "filter" ],
	            headers: { 2: { filter: false}, 3: { filter: false} }, 
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
				<c:choose>
				<c:when test="${not empty param.sessionUid}">
				ajaxUrl : "<lams:WebAppURL/>monitoring.do?dispatch=getOpenTextNominationsJSON&page={page}&size={size}&{sortList:column}&{filterList:fcol}&sessionUid=${param.sessionUid}&toolContentUID=${param.toolContentUID}",
				</c:when><c:otherwise>
				ajaxUrl : "<lams:WebAppURL/>monitoring.do?dispatch=getOpenTextNominationsJSON&page={page}&size={size}&{sortList:column}&{filterList:fcol}&toolContentUID=${param.toolContentUID}",
				</c:otherwise>
				</c:choose>
				ajaxProcessing: function (data, table) {
					if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i];

							rows += '<tr>';
							rows += '<td>';
							rows += userData["userEntry"]; 
							if ( userData["visible"] == false )
								rows += ' <em><fmt:message key="label.hidden"/></em>';							
							rows += '</td>';

							rows += '<td>';
							rows += userData["userName"];
							rows += '</td>';

							rows += '<td>';
							rows += userData["attemptTime"];
							rows += '</td>';

							rows += '<td><span id="link'+userData["userEntryUid"]+'">';
							if ( userData["visible"] ) {
								rows += buildShowHideLink(userData["userEntryUid"], 'hideOpenVote')
							} else {
								rows += buildShowHideLink(userData["userEntryUid"], 'showOpenVote')
							}
							rows += '</span></td>';

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
	</lams:head>
<body class="stripes">

	<div id="content">
	
		<h2><fmt:message key="label.learnersVoted"/>: <fmt:message key="label.openVotes"/></h2>
		    
   		<div class="tablesorter-holder">
   		
   		<c:choose>
		<c:when test="${not empty param.sessionUid}">
		<table id="entryTable" class="tablesorter" data-session-id="${param.sessionUid}">
		</c:when><c:otherwise>
		<table id="entryTable" class="tablesorter" data-session-id="${param.toolContentUID}">
		</c:otherwise>
		</c:choose>
			<thead>
				<tr>
					<th><fmt:message key="label.vote"/></th>
					<th> <fmt:message key="label.user"/></th>
					<th> <fmt:message key="label.attemptTime"/></th>
					<th style="width: 70px;"> <fmt:message key="label.visible"/></th>								  						 
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
		
	</div>
	<div id="footer"></div>
</body>
</lams:html>
