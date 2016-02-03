<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>
<c:set var="taskList" value="${sessionMap.taskList}"/>

<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>
<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">
<link type="text/css" href="${lams}css/jquery.jqGrid.css" rel="stylesheet"/>
<style media="screen,projection" type="text/css">
	.ui-jqgrid tr.jqgrow td {
	    white-space: normal !important;
	    height:auto;
	    vertical-align:text-top;
	    padding-top:2px;
	}
	.ui-jqgrid .ui-jqgrid-bdiv {
		overflow: auto;
	}
</style>

<script type="text/javascript">
	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${sessionMap.submissionDeadline}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>',
		toolContentID: '${toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		<c:forEach var="sessionDto" items="${sessionDtos}">
		
			jQuery("#group${sessionDto.sessionId}").jqGrid({
				datatype: "json",
				url: "<c:url value='/monitoring/getPagedUsers.do'/>?sessionMapID=${sessionMapID}&toolSessionID=${sessionDto.sessionId}",
				height: 'auto',
				shrinkToFit: false,
				width: '800',
				pager: 'pager-${sessionDto.sessionId}',
				rowList:[10,20,30,40,50,100],
				rowNum:10,
				viewrecords:true,
			   	caption: "${sessionDto.sessionName}",
			   	colNames:[
						'userUid'
						,"<fmt:message key="label.monitoring.summary.user" />"
						<c:forEach var="item" items="${sessionDto.taskListItems}">
							,'<a href="javascript:;" onclick="return summaryTask(${item.uid})"><c:out value="${item.title}" escapeXml="true"/></a>'
						</c:forEach>
						<c:if test="${sessionMap.monitorVerificationRequired}">
							,'<fmt:message key="label.monitoring.summary.confirm.completion" />'
						</c:if>
						],
			   	colModel:[
			   		{name:'userUid',index:'userUid', width:0, hidden: true}
			   		,{name:'userName',index:'userName', width:200, searchoptions: { clearSearch: false }}
			   		<c:forEach var="item" items="${sessionDto.taskListItems}" varStatus="status">
			   			,{name:'item${status.index}',index:'total${status.index}', width:100,sorttype:"int", search:false, align:"center"}
			   		</c:forEach>
			   		<c:if test="${sessionMap.monitorVerificationRequired}">
			   			,{name:'monitorVerificationRequired',index:'monitorVerificationRequired', width:130, sortable:false, search:false, align:"center"}
			   		</c:if>
			   	],
			   	loadError: function(xhr,st,err) {
			   		jQuery("#group${sessionDto.sessionId}").clearGridData();
			   		info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
			   	}
			})
			.jqGrid('filterToolbar', { 
 	 			searchOnEnter: false
 	 		})
 	 		.navGrid("#pager-${sessionDto.sessionId}", {edit:false,add:false,del:false,search:false});
	        			
		</c:forEach>

		//jqgrid autowidth
		function resizeFunc(){
			var grid;
		    if (grid = jQuery(".ui-jqgrid-btable:visible")) {
		    	grid.each(function(index) {
			        var gridId = $(this).attr('id');
			        
				    var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
				    jQuery('#' + gridId).setGridWidth(gridParentWidth, false);
		        });
		    }
		}
		resizeFunc();
		$(window).bind('resize', resizeFunc);
	});

	function summaryTask(taskUid){
		var myUrl = "<c:url value="/monitoring/itemSummary.do"/>?toolContentID=${toolContentID}&taskListItemUid=" + taskUid;
		launchPopup(myUrl,"LearnerView");
	}
	
	function setVerifiedByMonitor(link, userUid) {

		$.ajax({
			type: "POST",
			url: '<c:url value="/monitoring/setVerifiedByMonitor.do"/>',
			data: { userUid: userUid },
			success: function(response) {
				$("#verif-"+response).html('<img src="<html:rewrite page='/includes/images/tick.gif'/>" >');
			}
		});
		
		return false;
	}

</script>

<h1>
	<c:out value="${taskList.title}" escapeXml="true"/>
</h1>
<div class="instructions space-top space-bottom">
	<c:out value="${taskList.instructions}" escapeXml="false"/>
</div>

<%-- Summary list  --%>
<c:if test="${empty sessionDtos}">
	<div align="center">
		<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
	</div>
</c:if>

<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
			
	<div id="jqgrid-parent" style="padding-left: 30px; <c:if test='${! status.last}'>padding-bottom: 30px;</c:if><c:if test='${ status.last}'>padding-bottom: 15px;</c:if> ">
		<c:if test="${sessionMap.isGroupedActivity}">
			<div style="padding-bottom: 5px; font-size: small;">
				<B><fmt:message key="monitoring.label.group" /></B> ${sessionDto.sessionName}
			</div>
		</c:if>
		
		<table id="group${sessionDto.sessionId}"></table>
		<div id="pager-${sessionDto.sessionId}"></div>
	</div>
				
</c:forEach>	
				
<br/>
<c:if test="${taskList.reflectOnActivity && not empty sessionMap.reflectList}">
	<%@ include file="parts/reflections.jsp"%>
</c:if>

<%@include file="parts/advanceoptions.jsp"%>
<%@include file="daterestriction.jsp"%>
				