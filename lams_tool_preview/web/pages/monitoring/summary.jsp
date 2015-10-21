<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<link type="text/css" href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}css/jquery.jqGrid.css" rel="stylesheet" />
<link type="text/css" href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
<link rel="stylesheet" href="<html:rewrite page='/includes/css/learning.css'/>">
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
	
	.subgrid-data .ui-jqgrid-hdiv {
		display:none !important;
	}
	
	.subgrid-data td[colspan]:not([colspan="1"]) {
		background: #F0F0F0;
	}
	
	.ui-jqgrid tr.jqgrow td {
		vertical-align: top;
	}
</style>

<script type="text/javascript">
	//var for jquery.jRating.js
	var pathToImageFolder = "${lams}images/css/";
		
	//vars for rating.js
	var MAX_RATES = 0,
	MIN_RATES = 0,
	COMMENTS_MIN_WORDS_LIMIT = 0,
	MAX_RATINGS_FOR_ITEM = 0,
	LAMS_URL = '',
	COUNT_RATED_ITEMS = 0,
	COMMENT_TEXTAREA_TIP_LABEL = '',
	WARN_COMMENTS_IS_BLANK_LABEL = '',
	WARN_MIN_NUMBER_WORDS_LABEL = '';
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script src="${lams}includes/javascript/jquery.jRating.js" type="text/javascript"></script>
<script src="${lams}includes/javascript/rating.js" type="text/javascript" ></script>
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
			   		{name:'rating', index:'rating', width:160, align:"center", sortable:false}
			   	],
			   	rowNum:10,
			   	rowList:[10,20,30,40,50,100],
			   	pager: '#pager${groupSummary.sessionId}',
			   	viewrecords:true,
				loadComplete: function(){
					initializeJRating();
				},
			   	caption: "${groupSummary.sessionName}",
				subGrid: true,
				subGridOptions: {
					reloadOnExpand : false 
				},
				subGridRowExpanded: function(subgrid_id, row_id) {
					var subgridTableId = subgrid_id+"_t";
					var sessionId = jQuery("#group${groupSummary.sessionId}").getRowData(row_id)["sessionId"];
					var userId = jQuery("#group${groupSummary.sessionId}").getRowData(row_id)["userId"];
					   
					jQuery("#"+subgrid_id).html("<table id='" + subgridTableId + "' class='scroll'></table>");
					   
					jQuery("#"+subgridTableId).jqGrid({
						datatype: "json",
						loadonce:true,
						rowNum: 10000,
						url: "<c:url value='/monitoring/getSubgridData.do'/>?sessionMapID=${sessionMapID}&userID=" + userId,
						height: "100%",
						autowidth:true,
						grouping:true,	
						groupingView : {
							groupField : ['criteriaId'],
							groupColumnShow : [false],
							groupText : ['<b>{0}</b>']
						},
						loadComplete: function(){
							//remove empty subgrids
					        var table_value = $('#'+subgridTableId).getGridParam('records');
					        if(table_value === 0){
					            $('#'+subgrid_id).parent().unbind('click').html('<fmt:message key="label.no.ratings.left" />');
					        }
						},
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
			}).jqGrid('navGrid','#pager${groupSummary.sessionId}',{add:false,del:false,edit:false,search:false});
			//$("#group${groupSummary.sessionId}").parents('div.ui-jqgrid-bdiv').css("max-height","1000px");
			
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
	<div id="pager${groupSummary.sessionId}"></div> 
	<br>
		
</c:forEach>
	
<c:if test="${sessionMap.peerreview.reflectOnActivity}">
	<%@ include file="reflections.jsp"%>
</c:if>

<%@ include file="advanceoptions.jsp"%>
