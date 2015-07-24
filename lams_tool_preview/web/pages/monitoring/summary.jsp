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
			   	url: "<c:url value='/monitoring/getUsers.do'/>?toolContentId=${sessionMap.toolContentID}&toolSessionId=${groupSummary.sessionId}",
				datatype: "json",
				height: 'auto',
				autowidth: true,
				shrinkToFit: false,
			   	colNames:[
						'sessionId',
						'userId',
						"<fmt:message key="label.user.name" />",
						"<fmt:message key="label.rating" />"
				],
			   	colModel:[
			   		{name:'sessionId', index:'sessionId', width:0, hidden: true},
			   		{name:'userId', index:'userId', width:0, hidden: true},
			   		{name:'userName', index:'userName', width:260},
			   		{name:'rating', index:'rating', width:160, align:"center"}
			   	],
			   	rowNum:10,
			   	rowList:[10,20,30,40,50,100],
			   	pager: '#pager${groupSummary.sessionId}',
			    loadonce: true,
			    pagerpos:'left',
			   	caption: "${groupSummary.sessionName}",
				subGrid: true,
				subGridRowExpanded: function(subgrid_id, row_id) {
					var subgridTableId = subgrid_id+"_t";
					var sessionId = jQuery("#group${groupSummary.sessionId}").getRowData(row_id)["sessionId"];
					var userId = jQuery("#group${groupSummary.sessionId}").getRowData(row_id)["userId"];
					   
					jQuery("#"+subgrid_id).html("<table id='" + subgridTableId + "' class='scroll'></table>");
					   
					jQuery("#"+subgridTableId).jqGrid({
						datatype: "json",
						loadonce: true,
						url: "<c:url value='/monitoring/getSubgridData.do'/>?sessionMapID=${sessionMapID}&userID=" + userId,
						height: "100%",
						rowNum: 10000,
						autowidth:true,
						grouping:true,	
						groupingView : { 
							groupField : ['criteriaId'],
							groupColumnShow : [false],
							groupText : ['<b>{0}</b>']
						},
						colNames: [
						   '',
						   "<fmt:message key="monitoring.label.user.name"/>",
						   "<fmt:message key="label.rating"/>",
						   ''
						],
						colModel:[
						   {name:'id', index:'id', hidden:true},
						   {name:'userName',index:'userName'},
						   {name:'rating', index:'rating', width:140, align:"center"},
						   {name:'criteriaId', hidden:true}
						],
						loadError: function(xhr,st,err) {
					    	jQuery("#"+subgridTableId).clearGridData();
					    	info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
					    }
					})
				}
			}).jqGrid('navGrid','#pager${groupSummary.sessionId}',{add:false,del:false,edit:false,position:'right'});
			
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
		}).trigger('resize');
		setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);
		
		
	});

</script>

<h1>
	<c:out value="${sessionMap.peerreview.title}" escapeXml="true"/>
</h1>

<div class="instructions">
	<c:out value="${sessionMap.peerreview.instructions}" escapeXml="false"/>
</div>

<br/>

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
	
<c:if test="${sessionMap.peerreview.reflectOnActivity}">
	<%@ include file="reflections.jsp"%>
</c:if>

<%@ include file="advanceoptions.jsp"%>
