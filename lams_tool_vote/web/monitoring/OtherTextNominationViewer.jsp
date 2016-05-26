<!DOCTYPE html>

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
				theme: 'bootstrap',
			    sortInitialOrder: 'desc',
	            sortList: [[0]],
	            headerTemplate : '{content} {icon}',
	            widgets: [ "uitheme", "resizable", "filter" ],
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
	            container: $(this).find(".ts-pager"),
	            output: '{startRow} to {endRow} ({totalRows})',
	            cssPageDisplay: '.pagedisplay',
	            cssPageSize: '.pagesize',
	            cssDisabled: 'disabled',
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
				}					
			})
		});
  	})
</script>	
	</lams:head>
<body class="stripes">

	<c:set var="title"><fmt:message key="label.openVotes"/></c:set>
	<lams:Page type="learner" title="${title}">
		
		<c:choose>
		<c:when test="${not empty param.sessionUid}">
			<c:set var="dsi">data-session-id='${param.sessionUid}'</c:set>
		</c:when><c:otherwise>
			<c:set var="dsi">data-session-id='${param.toolContentUID}'</c:set>
		</c:otherwise>
		</c:choose>
	
		<lams:TSTable numColumns="4" dataId="${dsi}">
			<th><fmt:message key="label.vote"/></th>
			<th> <fmt:message key="label.user"/></th>
			<th> <fmt:message key="label.attemptTime"/></th>
			<th style="width: 70px;"> <fmt:message key="label.visible"/></th>								  						 
	     </lams:TSTable>
		
	<div id="footer"></div>
	</lams:Page>

</body>
</lams:html>
