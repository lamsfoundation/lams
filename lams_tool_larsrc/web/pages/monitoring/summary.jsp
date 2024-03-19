<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<link href="${lams}css/rating.css" rel="stylesheet" type="text/css">
<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme5.css" rel="stylesheet"/>
<link type="text/css" href="${lams}css/free.ui.jqgrid.custom.css" rel="stylesheet">
<link type="text/css" href="<lams:WebAppURL/>css/monitor.css" rel="stylesheet" />

<script type="text/javascript">
	var LAMS_URL = '${lams}',
		MAX_RATES = MAX_RATINGS_FOR_ITEM = MIN_RATES = COUNT_RATED_ITEMS = 0, // no restrictions
		COMMENTS_MIN_WORDS_LIMIT = 0, // comments not used,
		COMMENT_TEXTAREA_TIP_LABEL = WARN_COMMENTS_IS_BLANK_LABEL = WARN_MIN_NUMBER_WORDS_LABEL = '',
		ALLOW_RERATE = false; 
</script>
<lams:JSImport src="includes/javascript/rating.js" />
<lams:JSImport src="includes/javascript/rsrccommon.js" relative="true" />
<lams:JSImport src="includes/javascript/rsrcresourceitem.js" relative="true"/>
<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
<lams:JSImport src="includes/javascript/monitorToolSummaryAdvanced.js" />
<lams:JSImport src="includes/javascript/portrait5.js" />
<script type="text/javascript">
	$(document).ready(function(){
		doStatistic();
		initializePortraitPopover("<lams:LAMSURL />");

		<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
			<%-- if no sessions exist the basic authored data is sent with a sessionId of 0. Do not use subgrid and modify how rating/comments are displayed. --%>
			<c:set var="sessionsExist">${groupSummary.sessionId gt 0}</c:set>
		
			jQuery("#group${groupSummary.sessionId}").jqGrid({
				datatype: "local",
				autoencode:false,
				rowNum: 10000,
				height: 'auto',
				autowidth: true,
				shrinkToFit: false,
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
			   	colNames:['#',
						'itemUid',
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key='monitoring.label.title' /></spring:escapeBody>",
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key='monitoring.label.type' /></spring:escapeBody>",
					    "<spring:escapeBody javaScriptEscape='true'><fmt:message key='monitoring.label.suggest' /></spring:escapeBody>",
					    "<spring:escapeBody javaScriptEscape='true'><fmt:message key='monitoring.label.views' /></spring:escapeBody>",
						<c:if test="${groupSummary.allowRating}">
					    	"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.rating' /></spring:escapeBody>",
					   	</c:if>
						<c:if test="${groupSummary.allowComments}">
					   		"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.comments' /></spring:escapeBody>",
					   	</c:if>
					    "<spring:escapeBody javaScriptEscape='true'><fmt:message key='monitoring.label.actions' /></spring:escapeBody>" 
				],
			   	colModel:[
			   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
			   		{name:'itemUid', index:'itemUid', width:0, hidden: true},
			   		{name:'title', index:'title', width:260},
			   		{name:'type', index:'type', width:90, align:"center"},
			   		{name:'suggest', index:'suggest', width:160, align:"center"},
			   		{name:'viewNumber', index:'viewNumber', width:100, align:"center", sorttype:"int"},
					<c:if test="${groupSummary.allowRating}">
						{name:'rating', index:'rating', width:200, align:"center"},
				   	</c:if>
					<c:if test="${groupSummary.allowComments}">
						{name:'comments', index:'comments', width:200, align:"center"},
				   	</c:if>
			   		{name:'actions', index:'actions', width:120, align:"center"}		
			   	],
			   	<c:if test="${sessionsExist}">
				subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
					var subgridTableId = subgrid_id+"_t";
					var itemUid = jQuery("#group${groupSummary.sessionId}").getRowData(row_id)["itemUid"];
					   
					jQuery("#"+subgrid_id).html("<table id='" + subgridTableId + "' class='scroll'></table>" + 
							"<div id='pager-" + subgridTableId + "' class='scroll'></div>")
					   
					jQuery("#"+subgridTableId).jqGrid({
						datatype: "json",
						url: "<c:url value='/monitoring/getSubgridData.do'/>?itemUid=" + itemUid + '&toolSessionID=${groupSummary.sessionId}',
						height: "100%",
						autowidth:true,
						pager: 'pager-' + subgridTableId,
						rowList:[10,20,30,40,50,100],
						rowNum:10,
						viewrecords:true,
						guiStyle: "bootstrap",
						iconSet: 'fontAwesome',
						colNames: [
						   '',
						   "<spring:escapeBody javaScriptEscape='true'><fmt:message key='monitoring.label.user.name'/></spring:escapeBody>",
						   "<spring:escapeBody javaScriptEscape='true'><fmt:message key='monitoring.label.access.time'/></spring:escapeBody>", 
						   "<spring:escapeBody javaScriptEscape='true'><fmt:message key='monitoring.label.complete.time'/></spring:escapeBody>", 
						   "<spring:escapeBody javaScriptEscape='true'><fmt:message key='monitoring.label.time.taken'/></spring:escapeBody>",
						   'portraitId'
						],
						colModel:[
						   {name:'id', index:'id', hidden:true},
						   {name:'userName',index:'userName', searchoptions: { clearSearch: false }, formatter:userNameFormatter},
						   {name:'startTime', index:'startTime', width:140, align:"center", search:false},
						   {name:'completeTime', index:'completeTime', width:140, align:"center", search:false},
						   {name:'timeTaken',index:'timeTaken', width:70, align:"center", search:false},
						   {name:'portraidId',index:'portraidId', hidden:true}
						],
						loadError: function(xhr,st,err) {
						    jQuery("#"+subgridTableId).clearGridData();
						    jQuery.jgrid.info_dialog("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.error'/></spring:escapeBody>", "<spring:escapeBody javaScriptEscape='true'><fmt:message key='error.loaderror'/></spring:escapeBody>", "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.ok'/></spring:escapeBody>");
					    },
						loadComplete: function () {
					   	 	initializePortraitPopover('<lams:LAMSURL/>');
					    }
					})
					.jqGrid('filterToolbar', { 
						searchOnEnter: false
					})
					.navGrid("#pager-" + subgridTableId, {edit:false,add:false,del:false,search:false});
				}
			   	</c:if>
			})
   	        <c:forEach var="item" items="${groupSummary.items}" varStatus="i">
				<c:choose>
					<c:when test="${item.itemHide}">
						<c:set var="changeItemVisibility">
							"<button type='button' onclick='changeItemVisibility(this, ${item.itemUid}, ${groupSummary.sessionId}, false)'" +
							"			class='btn btn-sm btn-light'>" +
							"	<i class='fa-regular fa-eye me-1'></i>" +
							"	<fmt:message key='monitoring.label.show' />" +
							"</button>"
						</c:set>
					</c:when>
					<c:otherwise>
						<c:set var="changeItemVisibility">
							"<button type='button' onclick='changeItemVisibility(this, ${item.itemUid}, ${groupSummary.sessionId}, true)'" +
							"		class='btn btn-sm btn-light'>" +
							"	<i class='fa-regular fa-eye-slash me-1'></i>" +
							"	<fmt:message key='monitoring.label.hide' />" +
							"</button>"
						</c:set>
					</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${item.itemType == 1}">
						<c:set var="itemTypeLabel"><fmt:message key="label.authoring.basic.resource.url" /></c:set>
					</c:when>
					<c:when test="${item.itemType == 2}">
						<c:set var="itemTypeLabel"><fmt:message key="label.authoring.basic.resource.file" /></c:set>
					</c:when>
					<c:when test="${item.itemType == 3}">
						<c:set var="itemTypeLabel"><fmt:message key="label.authoring.basic.resource.website" /></c:set>
					</c:when>
				</c:choose>
   	        
				<c:set var="itemTitle">
					<c:out value="${item.itemTitle}" escapeXml="true"/>
				</c:set>
				
				<c:if test="${groupSummary.allowRating}">
					<c:set var="ratingHTML">
						<div class="starability-holder">
					</c:set>
					<c:choose>
						<c:when test="${sessionsExist}">
							<c:forEach var="criteriaDto" items="${item.ratingDTO.criteriaDtos}">
								<c:set var="dataRating">
									<fmt:formatNumber value="${criteriaDto.averageRating-(criteriaDto.averageRating%1)}" pattern="#"></fmt:formatNumber>
									${(criteriaDto.averageRating%1) >= 0.5 ? '.5' : ''}
								</c:set>
							
								<c:set var="ratingHTML">
									${ratingHTML}<div class="starability starability-result" data-rating="${dataRating}"></div><div class="starability-caption"><fmt:message key="label.average.rating"><fmt:param>${criteriaDto.averageRating}</fmt:param><fmt:param>${criteriaDto.numberOfVotes}</fmt:param></fmt:message></div>
								</c:set>
							</c:forEach>
						</c:when>
						<c:when test="${not sessionsExist and item.allowRating}">
							<c:set var="ratingHTML">
								${ratingHTML}<i class="fa fa-check"></i>
							</c:set>
						</c:when>
					</c:choose>
					<c:set var="ratingHTML">
						${ratingHTML}</div>
					</c:set>
				</c:if>
					
				<c:if test="${groupSummary.allowComments}">
					<c:set var="commentButtonText"><fmt:message key="label.view.comments"/></c:set>
					<c:choose>
					<c:when test="${item.allowComments and sessionsExist}">
						<c:set var="commentHTML"><button type="button" onclick="viewComments(${item.itemUid}, ${groupSummary.sessionId})" class="btn btn-sm btn-light"><i class="fa-solid fa-binoculars me-1"></i>${commentButtonText}</button></c:set>
					</c:when>
					<c:when test="${item.allowComments and not sessionsExist}">
						<c:set var="commentHTML"><i class="fa fa-check"></i></c:set>
					</c:when>
					<c:otherwise>
						<c:set var="commentHTML">&nbsp;</c:set>
					</c:otherwise>
					</c:choose>
				</c:if>
				
   	     		jQuery("#group${groupSummary.sessionId}").addRowData(${i.index + 1}, {
   	   	     		id:		"${i.index + 1}",
   	   	     		itemUid:	"${item.itemUid}",
   	   	     		title:	"${itemTitle} <button type='button' onclick='viewItem(${item.itemUid})' class='btn btn-sm btn-light ms-2'>" +
   	   	     					"<i class='fa-solid fa-up-right-from-square'></i>" +
   	   	     				"</button>",
   	   	     		type:	"${itemTypeLabel}",
   	   	     		suggest:	"${item.username}",
   	   	     		viewNumber:"	${item.viewNumber}",
					<c:if test="${groupSummary.allowRating}">
						rating: '${ratingHTML}',
				   	</c:if>
					<c:if test="${groupSummary.allowComments}">
						comments: '${commentHTML}',
					</c:if>
   	   	     		actions:	${changeItemVisibility}
   	   	   	    });
	        </c:forEach>
			
		</c:forEach>

		initializeStarability();

		//jqgrid autowidth (http://stackoverflow.com/a/1610197)
		$(window).bind('resize', function() {
			resizeJqgrid(jQuery(".ui-jqgrid-btable"));
		});
		setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);

		//resize jqGrid on openning of bootstrap collapsible
		$('div[id^="collapse"]').on('shown.bs.collapse', function () {
			resizeJqgrid(jQuery(".ui-jqgrid-btable", this));
		})
	});

	function resizeJqgrid(jqgrids) {
		jqgrids.each(function(index) {
			var gridId = $(this).attr('id');
			var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
			jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
		});
	};
	
	function userNameFormatter (cellvalue, options, rowObject) {
		return definePortraitPopover(rowObject[5], rowObject[0],  rowObject[1]);
	}
	
	function changeItemVisibility(linkObject, itemUid, toolSessionId, isHideItem) {
        $.ajax({
            url: '<c:url value="/monitoring/changeItemVisibility.do"/>?<csrf:token/>',
            data: 'sessionMapID=${sessionMapID}&toolSessionID='+toolSessionId+'&itemUid=' + itemUid + '&isHideItem=' + isHideItem + '&toolContentID=' + ${sessionMap.toolContentID},
            type: 'post',
            success: function () {
            	if (isHideItem) {
            		linkObject.innerHTML = '<i class="fa-regular fa-eye me-1"></i> <spring:escapeBody javaScriptEscape="true"><fmt:message key="monitoring.label.show" /></spring:escapeBody>' ;
            		linkObject.onclick = function (){
            			changeItemVisibility(this, itemUid, toolSessionId, false); 
            			return false;
            		}
            	} else {
            		linkObject.innerHTML = '<i class="fa-regular fa-eye-slash me-1"></i> <spring:escapeBody javaScriptEscape="true"><fmt:message key="monitoring.label.hide" /></spring:escapeBody>' ;
            		linkObject.onclick = function (){
            			changeItemVisibility(this, itemUid, toolSessionId, true); 
            			return false;
            		}
            	}
            }
       	});
	}

	function viewItem(itemUid){
		var myUrl = "<c:url value="/reviewItem.do"/>?mode=teacher&itemUid=" + itemUid + "&sessionMapID=${sessionMapID}";
		launchPopup(myUrl,"MonitoringReview");
	}
	
	function viewComments(itemUid, toolSessionID){
		var myUrl = "<c:url value='/monitoring/viewComments.do'/>?mode=teacher&itemUid=" + itemUid + "&toolSessionID=" + toolSessionID + "&sessionMapID=${sessionMapID}";
		launchPopup(myUrl,"MonitoringReview");
	}
</script>
	
<c:if test="${empty summaryList}">
	<lams:Alert5 type="info" id="no-session-summary">
		 <fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert5>
</c:if>

<h1>
    <c:out value="${sessionMap.resource.title}" escapeXml="true"/>
</h1>

<div class="instructions">
    <c:out value="${sessionMap.resource.instructions}" escapeXml="false"/>
</div>
	
<!--For release marks feature-->
<lams:WaitingSpinner id="message-area-busy"/>
<div id="message-area"></div>

<c:if test="${sessionMap.isGroupedActivity}">
	<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
	<c:if test="${sessionMap.isGroupedActivity}">	
	    <div class="lcard" >
        <div class="card-header" id="heading${groupSummary.sessionId}">
        	<span class="card-title collapsable-icon-left">
        		<button class="btn btn-secondary-darker no-shadow ${status.first ? '' : 'collapsed'}" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${groupSummary.sessionId}" 
						aria-expanded="${status.first}" aria-controls="collapse${groupSummary.sessionId}" >
					<c:choose>
						<c:when test="${sessionsExist}">
							<fmt:message key="monitoring.label.group" />&nbsp;${groupSummary.sessionName}
						</c:when>
						<c:otherwise>
							&nbsp;
						</c:otherwise>
					</c:choose>
				</button>
			</span>
        </div>
        
        <div id="collapse${groupSummary.sessionId}" class="card-collapse collapse ${status.first ? 'show' : ''}">
	</c:if>

	<table id="group${groupSummary.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
	
	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>	
</c:forEach>

<c:if test="${sessionMap.isGroupedActivity}">
	</div> <!--  end accordianSessions --> 
</c:if>

<h2 class="card-subheader fs-4 mt-3" id="header-statistics">
	<fmt:message key="monitoring.tab.statistics" />
</h2>
<%@ include file="statistic.jsp"%>

<h2 class="card-subheader fs-4" id="header-settings">
	Settings
</h2>
<%@ include file="editactivity.jsp"%>
