<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<link type="text/css" href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}css/jquery.jqGrid.css" rel="stylesheet" />
<style media="screen,projection" type="text/css">
	#user-dropdown-div {padding-left: 30px; margin-top: -5px; margin-bottom: 50px;}
	.bottom-buttons {margin: 20px 20px 0px; padding-bottom: 20px;}
	.section-header {padding-left: 20px; margin-bottom: 15px; margin-top: 60px;}
	.ui-jqgrid tr.jqgrow td {
	    white-space: normal !important;
	    height:auto;
	    vertical-align:text-top;
	    padding-top:2px;
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
				height: 'auto',
				autowidth: true,
				shrinkToFit: false,
			   	colNames:['#',
						'sessionId',
						'itemUid',
						"<fmt:message key="monitoring.label.title" />",
						"<fmt:message key="monitoring.label.type" />",
					    "<fmt:message key="monitoring.label.suggest" />",
					    "<fmt:message key="monitoring.label.views" />",
					    "<fmt:message key="monitoring.label.actions" />" 
				],
			   	colModel:[
			   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
			   		{name:'sessionId', index:'sessionId', width:0, hidden: true},
			   		{name:'itemUid', index:'itemUid', width:0, hidden: true},
			   		{name:'title', index:'title', width:260},
			   		{name:'type', index:'type', width:90},
			   		{name:'suggest', index:'suggest', width:160},
			   		{name:'viewNumber', index:'viewNumber', width:100, align:"right", sorttype:"int"},
			   		{name:'actions', index:'actions', width:120, align:"center"}		
			   	],
			   	caption: "${groupSummary.sessionName}",
				subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
					var subgridTableId = subgrid_id+"_t";
					var sessionId = jQuery("#group${groupSummary.sessionId}").getRowData(row_id)["sessionId"];
					var itemUid = jQuery("#group${groupSummary.sessionId}").getRowData(row_id)["itemUid"];
					   
					jQuery("#"+subgrid_id).html("<table id='" + subgridTableId + "' class='scroll'></table>");
					   
					jQuery("#"+subgridTableId).jqGrid({
						datatype: "json",
						loadonce: true,
						url: "<c:url value='/monitoring/getSubgridData.do'/>?itemUid=" + itemUid + '&toolSessionID=' + sessionId,
						height: "100%",
						rowNum: 10000,
						autowidth:true,
						colNames: [
						   '',
						   "<fmt:message key="monitoring.label.user.name"/>",
						   "<fmt:message key="monitoring.label.access.time"/>", 
						   "<fmt:message key="monitoring.label.complete.time"/>", 
						   "<fmt:message key="monitoring.label.time.taken"/>"
						],
						colModel:[
						   {name:'id', index:'id', hidden:true},
						   {name:'userName',index:'userName'},
						   {name:'startTime', index:'startTime', width:140, align:"center"},
						   {name:'completeTime', index:'completeTime', width:140, align:"center"},
						   {name:'timeTaken',index:'timeTaken', width:70, align:"center"}
						],
						loadError: function(xhr,st,err) {
					    	jQuery("#"+subgridTableId).clearGridData();
					    	info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
					    }
					})
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
   	        
   	     		jQuery("#group${groupSummary.sessionId}").addRowData(${i.index + 1}, {
   	   	     		id:		"${i.index + 1}",
   	   	     		sessionId:	"${groupSummary.sessionId}",
   	   	     		itemUid:	"${item.itemUid}",
   	   	     		title:	"<a href='#nogo' onclick='javascript:viewItem(${item.itemUid}); return false;'>${item.itemTitle}</a>",
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

<h2>
	${sessionMap.resource.title}
</h2>

	${sessionMap.resource.instructions}

<c:if test="${empty summaryList}">
	<div align="center">
		<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
	</div>
</c:if>

<c:forEach var="groupSummary" items="${summaryList}">
	
	<c:if test="${sessionMap.isGroupedActivity}">
		<h1>
			<fmt:message key="monitoring.label.group" /> ${groupSummary.sessionName}
		</h1>
		<br>
	</c:if>
	
	<table id="group${groupSummary.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
	<br>
		
</c:forEach>
	
<c:if test="${sessionMap.resource.reflectOnActivity}">
	<%@ include file="reflections.jsp"%>
</c:if>

<%@ include file="advanceoptions.jsp"%>
