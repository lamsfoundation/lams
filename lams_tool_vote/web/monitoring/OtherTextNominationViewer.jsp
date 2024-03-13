<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="title"><fmt:message key="label.openVotes" /></c:set>
<lams:PageMonitor title="${title}" hideHeader="true">
	<%@ include file="/common/monitorheader.jsp"%>
	<script type="text/javascript">
		function submitOpenVote(currentUid, actionMethod) {
		        var submitUid = currentUid;
		        $.ajax({
                    type: 'POST',
  					url: '<lams:WebAppURL/>monitoring/'+actionMethod+'.do?<csrf:token/>',
                    data: {
                        currentUid : submitUid
                    },
				}).done(function( data ) {
                    location.reload();
				});
		}
			
		function buildShowHideLink(currentUid, actionMethod) {
				var str = '<button type="button" onclick="submitOpenVote(\''+currentUid+'\', \''+actionMethod+'\')"  class="btn btn-light btn-sm">';
				if ( actionMethod == 'hideOpenVote' ) {
					str += 	'<i class="fa-regular fa-eye-slash me-1"></i>' +
							'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.hide"/></spring:escapeBody>';
				} else {
					str += '<i class="fa-regular fa-eye me-1"></i>' +
							'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.show"/></spring:escapeBody>';
				}
				str += '</button>';
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
	            <c:choose>
					<c:when test="${not empty param.sessionUid}">
						ajaxUrl : "<lams:WebAppURL/>monitoring/getOpenTextNominationsJSON.do?page={page}&size={size}&{sortList:column}&{filterList:fcol}&sessionUid=${param.sessionUid}&toolContentUID=${param.toolContentUID}",
					</c:when><c:otherwise>
						ajaxUrl : "<lams:WebAppURL/>monitoring/getOpenTextNominationsJSON.do?page={page}&size={size}&{sortList:column}&{filterList:fcol}&toolContentUID=${param.toolContentUID}",
					</c:otherwise>
				</c:choose>
				ajaxProcessing: function (data, table) {
					if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i];

							rows += '<tr>' +
										'<td>' +
											userData["userEntry"];	
																			 
							if ( userData["visible"] == false )
								rows += 	'<span class="badge text-bg-warning float-end">' + 
												'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.hidden"/></spring:escapeBody>' + 
											'</span>';
							rows += 	'</td>' +
							
										'<td>' +
											definePortraitPopover(userData["portraitId"], userData["userId"], userData["userName"], userData["userName"]) +
										'</td>' +

										'<td>' +
											'<time class="timeago" title="' +
													userData["attemptTime"] +
													'" datetime="' +
													userData["attemptTimeTimeago"] +
											'"></time>' +
										'</td>' +

										'<td><span id="link'+userData["userEntryUid"]+'">';
							if ( userData["visible"] ) {
								rows +=		buildShowHideLink(userData["userEntryUid"], 'hideOpenVote');
							} else {
								rows += 	buildShowHideLink(userData["userEntryUid"], 'showOpenVote');
							}
							rows += 	'</span></td>' +
									'</tr>';
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

	<div class="container-main">
		<h1 class="mb-4">
			${title}: ${nominationText}
		</h1>
				
		<c:choose>
			<c:when test="${not empty param.sessionUid}">
				<c:set var="dsi">data-session-id='${param.sessionUid}'</c:set>
			</c:when><c:otherwise>
				<c:set var="dsi">data-session-id='${param.toolContentUID}'</c:set>
			</c:otherwise>
		</c:choose>
	
		<div class="shadow rounded-5">
			<lams:TSTable5 numColumns="4" dataId="${dsi}">
				<th><fmt:message key="label.vote"/></th>
				<th> <fmt:message key="label.user"/></th>
				<th> <fmt:message key="label.attemptTime"/></th>
				<th style="width: 70px;"> <fmt:message key="label.visible"/></th>								  						 
		     </lams:TSTable5>
	     </div>
	
		<div class="activity-bottom-buttons">
			<button type="button" onclick="window.close()" class="btn btn-primary">
				<i class="fa-regular fa-circle-xmark me-1"></i>
				<fmt:message key="label.close" />
			</button>
		</div>
	</div>
</lams:PageMonitor>
