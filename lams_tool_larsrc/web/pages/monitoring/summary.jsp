<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<link type="text/css" href="${lams}css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}css/jquery.jqGrid.css" rel="stylesheet" />
<style media="screen,projection" type="text/css">
	 .ui-jqgrid {
		border-left-style: none !important;
		border-right-style: none !important;
		border-bottom-style: none !important;
	}
	
	.ui-jqgrid tr {
		border-left-style: none !important;
	}
	
	.ui-jqgrid td {
		border-style: none !important;
	}
</style>

<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript">
	$(document).ready(function(){
		
		<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
		
			jQuery("#group${groupSummary.sessionId}").jqGrid({
				datatype: "local",
				rowNum: 10000,
				height: 'auto',
				autowidth: true,
				shrinkToFit: false,
			   	colNames:['#',
						'itemUid',
						"<fmt:message key="monitoring.label.title" />",
						"<fmt:message key="monitoring.label.type" />",
					    "<fmt:message key="monitoring.label.suggest" />",
					    "<fmt:message key="monitoring.label.views" />",
					    "<fmt:message key="monitoring.label.actions" />" 
				],
			   	colModel:[
			   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
			   		{name:'itemUid', index:'itemUid', width:0, hidden: true},
			   		{name:'title', index:'title', width:260},
			   		{name:'type', index:'type', width:90, align:"center"},
			   		{name:'suggest', index:'suggest', width:160, align:"center"},
			   		{name:'viewNumber', index:'viewNumber', width:100, align:"center", sorttype:"int"},
			   		{name:'actions', index:'actions', width:120, align:"center"}		
			   	],
			   	// caption: "${groupSummary.sessionName}",
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
						colNames: [
						   '',
						   "<fmt:message key="monitoring.label.user.name"/>",
						   "<fmt:message key="monitoring.label.access.time"/>", 
						   "<fmt:message key="monitoring.label.complete.time"/>", 
						   "<fmt:message key="monitoring.label.time.taken"/>"
						],
						colModel:[
						   {name:'id', index:'id', hidden:true},
						   {name:'userName',index:'userName', searchoptions: { clearSearch: false }},
						   {name:'startTime', index:'startTime', width:140, align:"center", search:false},
						   {name:'completeTime', index:'completeTime', width:140, align:"center", search:false},
						   {name:'timeTaken',index:'timeTaken', width:70, align:"center", search:false}
						],
						loadError: function(xhr,st,err) {
					    	jQuery("#"+subgridTableId).clearGridData();
					    	info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
					    }
					})
					.jqGrid('filterToolbar', { 
						searchOnEnter: false
					})
					.navGrid("#pager-" + subgridTableId, {edit:false,add:false,del:false,search:false});
				}
			});
			
   	        <c:forEach var="item" items="${groupSummary.items}" varStatus="i">
				<c:choose>
					<c:when test="${item.itemHide}">
						<c:set var="changeItemVisibility"><a href='#nogo' onclick='javascript:changeItemVisibility(this, ${item.itemUid}, false); return false;'> <fmt:message key='monitoring.label.show' /> </a></c:set>
					</c:when>
					<c:otherwise>
						<c:set var="changeItemVisibility"><a href='#nogo' onclick='javascript:changeItemVisibility(this, ${item.itemUid}, true); return false;'> <fmt:message key='monitoring.label.hide' /> </a></c:set>
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
					<c:when test="${item.itemType == 4}">
						<c:set var="itemTypeLabel"><fmt:message key="label.authoring.basic.resource.learning.object" /></c:set>
					</c:when>
				</c:choose>
   	        
				<c:set var="itemTitle">
					<c:out value="${item.itemTitle}" escapeXml="true"/>
				</c:set>
				
   	     		jQuery("#group${groupSummary.sessionId}").addRowData(${i.index + 1}, {
   	   	     		id:		"${i.index + 1}",
   	   	     		itemUid:	"${item.itemUid}",
   	   	     		title:	"<a href='#nogo' onclick='javascript:viewItem(${item.itemUid}); return false;'>${itemTitle}</a>",
   	   	     		type:	"${itemTypeLabel}",
   	   	     		suggest:	"${item.username}",
   	   	     		viewNumber:"	${item.viewNumber}",
   	   	     		actions:	"${changeItemVisibility}"
   	   	   	    });
	        </c:forEach>
			
		</c:forEach>
        
		//jqgrid autowidth (http://stackoverflow.com/a/1610197)
		$(window).bind('resize', function() {
			var grid;
		    if (grid = jQuery(".ui-jqgrid-btable:visible")) {
		    	grid.each(function(index) {
		        	var gridId = $(this).attr('id');
		        	var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
		        	jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
		    	});
		    }
		});
		setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);
		
	});
	
	function changeItemVisibility(linkObject, itemUid, isHideItem) {
		<c:set var="hideShowLink"><a href='<c:url value='/monitoring/changeItemVisibility.do'/>?sessionMapID=${sessionMapID}&itemUid=${item.itemUid}' class='button'> <fmt:message key='monitoring.label.show' /> </a></c:set>
        $.ajax({
            url: '<c:url value="/monitoring/changeItemVisibility.do"/>',
            data: 'sessionMapID=${sessionMapID}&itemUid=' + itemUid + '&isHideItem=' + isHideItem,
            dataType: 'json',
            type: 'post',
            success: function (json) {
            	if (isHideItem) {
            		linkObject.innerHTML = '<fmt:message key='monitoring.label.show' />' ;
            		linkObject.onclick = function (){
            			changeItemVisibility(this, itemUid, false); 
            			return false;
            		}
            	} else {
            		linkObject.innerHTML = '<fmt:message key='monitoring.label.hide' />' ;
            		linkObject.onclick = function (){
            			changeItemVisibility(this, itemUid, true); 
            			return false;
            		}
            	}
            }
       	});
	}

</script>

<div class="panel">
	<h4>
	    <c:out value="${sessionMap.resource.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${sessionMap.resource.instructions}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty summaryList}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			 <fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:if>
	
	<!--For release marks feature-->
	<lams:WaitingSpinner id="message-area-busy"/>
	<div id="message-area"></div>

</div>


<c:if test="${sessionMap.isGroupedActivity}">
<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
	
	<c:if test="${sessionMap.isGroupedActivity}">	
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${groupSummary.sessionId}">
        	<span class="panel-title collapsable-icon-left">
        	<a class="collapsed" role="button" data-toggle="collapse" href="#collapse${groupSummary.sessionId}" 
					aria-expanded="false" aria-controls="collapse${groupSummary.sessionId}" >
			<fmt:message key="monitoring.label.group" />&nbsp;${groupSummary.sessionName}</a>
			</span>
        </div>
        
        <div id="collapse${groupSummary.sessionId}" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="heading${groupSummary.sessionId}">
	</c:if>

		<table id="group${groupSummary.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
	
	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
	
</c:forEach>

<c:if test="${sessionMap.isGroupedActivity}">
</div> <!--  end accordianSessions --> 
</c:if>
	
<c:if test="${sessionMap.resource.reflectOnActivity}">
	<%@ include file="reflections.jsp"%>
</c:if>

<%@ include file="advanceoptions.jsp"%>
