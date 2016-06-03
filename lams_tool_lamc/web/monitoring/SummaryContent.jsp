<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>

<link type="text/css" href="${lams}css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">
<link href="${lams}css/jquery.jqGrid.css" rel="stylesheet" type="text/css"/>

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
<script type="text/javascript"> 
	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring.do?dispatch=setSubmissionDeadline"/>',
		toolContentID: '${toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};	
</script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript">
	$(document).ready(function(){
		<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
		
			jQuery("#group${sessionDto.sessionId}").jqGrid({
				datatype: "json",
				url: "<c:url value='/monitoring.do'/>?dispatch=getPagedUsers&toolSessionID=${sessionDto.sessionId}",
				height: 'auto',
				autowidth: true,
				shrinkToFit: true,
				pager: 'pager-${sessionDto.sessionId}',
				rowList:[10,20,30,40,50,100],
				rowNum:10,
				viewrecords:true,
			   	/* caption: "${sessionDto.sessionName}", */
			   	colNames:[
						'userUid',
						"<fmt:message key="label.monitoring.summary.user.name" />",
					    "<fmt:message key="label.monitoring.summary.total" />"],
			   	colModel:[
			   		{name:'userUid',index:'userUid', width:0, hidden: true},
			   		{name:'userName',index:'userName', width:200, searchoptions: { clearSearch: false }},
			   		{name:'total',index:'total', width:50,align:"right",sorttype:"int", search:false}		
			   	],
			   	loadError: function(xhr,st,err) {
			   		jQuery("#group${sessionDto.sessionId}").clearGridData();
			   		info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
			   	},
			  	onSelectRow: function(rowid) { 
			  	    if(rowid == null) { 
			  	    	rowid=0; 
			  	    } 
			   		var userUid = jQuery("#group${sessionDto.sessionId}").getCell(rowid, 'userUid');
					var userMasterDetailUrl = '<c:url value="/monitoring.do"/>';
		  	        jQuery("#userSummary${sessionDto.sessionId}").clearGridData().setGridParam({gridstate: "visible"}).trigger("reloadGrid");
		  	        $("#masterDetailArea").load(
		  	        	userMasterDetailUrl,
		  	        	{
		  	        		dispatch: "userMasterDetail",
		  	        		userUid: userUid
		  	       		}
		  	       	);
	  	  		} 
			})
			.jqGrid('filterToolbar', { 
 	 			searchOnEnter: false
 	 		})
 	 		.navGrid("#pager-${sessionDto.sessionId}", {edit:false,add:false,del:false,search:false});
	        
	        var oldValue = 0;
			jQuery("#userSummary${sessionDto.sessionId}").jqGrid({
				datatype: "local",
				rowNum: 10000,
				gridstate:"hidden",
				//hiddengrid:true,
				height: 110,
				autowidth: true,
				shrinkToFit: true,
				caption: "<fmt:message key="label.monitoring.summary.learner.summary" />",
			   	colNames:['#',
						'userAttemptUid',
  						'Question',
  						"<fmt:message key="label.monitoring.user.summary.response" />",
  						"<fmt:message key="label.monitoring.user.summary.grade" />"],
					    
			   	colModel:[
	  			   		{name:'id', index:'id', width:20, sorttype:"int"},
	  			   		{name:'userAttemptUid', index:'userAttemptUid', width:0, hidden: true},
	  			   		{name:'title', index:'title', width: 200},
	  			   		{name:'response', index:'response', width:443, sortable:false},
	  			   		{name:'grade', index:'grade', width:80, sorttype:"int", editable:true, editoptions: {size:4, maxlength: 4}, align:"right" }
			   	],
			   	multiselect: false,

				cellurl: '<c:url value="/monitoring.do?dispatch=saveUserMark"/>',
  				cellEdit: true,
  				afterEditCell: function (rowid,name,val,iRow,iCol){
  					oldValue = eval(val);
				},
				beforeSaveCell : function(rowid, name, val, iRow, iCol) {
					var intRegex = /^\d+$/;
					if (!intRegex.test(val)) {
  						return null;
  					}
					
					// get maxGrade attribute which was set in masterDetailLoadUp.jsp
					var maxGrade = jQuery("table#userSummary${sessionDto.sessionId} tr#" + iRow 
							              + " td[aria-describedby$='_" + name + "']").attr("maxGrade");
					if (+val > +maxGrade) {
						return maxGrade;
					}
				},
  				afterSaveCell : function (rowid,name,val,iRow,iCol){
  					var intRegex = /^\d+$/;
  					if (!intRegex.test(val)) {
  						jQuery("#userSummary${sessionDto.sessionId}").restoreCell(iRow,iCol); 
  					} else {
   						var parentSelectedRowId = jQuery("#group${sessionDto.sessionId}").getGridParam("selrow");
  						var previousTotal =  eval(jQuery("#group${sessionDto.sessionId}").getCell(parentSelectedRowId, 'total'));
  						jQuery("#group${sessionDto.sessionId}").setCell(parentSelectedRowId, 'total', previousTotal - oldValue + eval(val), {}, {});
  					}
				},	  		
  				beforeSubmitCell : function (rowid,name,val,iRow,iCol){
  					var intRegex = /^\d+$/;
  					if (!intRegex.test(val)) {
  						return {nan:true};
  					} else {
  						var userAttemptUid = jQuery("#userSummary${sessionDto.sessionId}").getCell(rowid, 'userAttemptUid');
  						return {userAttemptUid:userAttemptUid};		  				  		
  				  	}
  				}
			});
			
		</c:forEach>	
		
        //jqgrid autowidth (http://stackoverflow.com/a/1610197)
        $(window).bind('resize', function() {
            resizeJqgrid(jQuery(".ui-jqgrid-btable:visible"));
        });

        //resize jqGrid on openning of bootstrap collapsible
        $('div[id^="collapse"]').on('shown.bs.collapse', function () {
            resizeJqgrid(jQuery(".ui-jqgrid-btable:visible", this));
        })

        function resizeJqgrid(jqgrids) {
            jqgrids.each(function(index) {
                var gridId = $(this).attr('id');
                var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
                jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
            });
        };
        setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);
	});

</script>

<div class="panel">
	<h4>
	    <c:out value="${mcGeneralMonitoringDTO.activityTitle}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${mcGeneralMonitoringDTO.activityInstructions}" escapeXml="false"/>
	</div>
	
	<c:if test="${useSelectLeaderToolOuput}">
		<lams:Alert type="info" id="use-leader" close="false">
			<fmt:message key="label.info.use.select.leader.outputs" />
		</lams:Alert>
	</c:if>
	
	<c:if test="${(mcGeneralMonitoringDTO.userExceptionNoToolSessions == 'true')}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="error.noLearnerActivity"/>
		</lams:Alert>
	</c:if>
	
	<!--For release marks feature-->
	<i class="fa fa-spinner" style="display:none" id="message-area-busy"></i>
	<div id="message-area"></div>

</div>

<%@ include file="parts/advanceQuestions.jsp"%>


<c:if test="${mcGeneralMonitoringDTO.userExceptionNoToolSessions != 'true'}">
	<html:link href="#" onclick="javascript:submitMonitoringMethod('downloadMarks');" styleClass="btn btn-default btn-xs pull-right">
		<fmt:message key="label.monitoring.downloadMarks.button" />
	</html:link>

	<h4>    
		<fmt:message key="label.studentMarks"/>
	</h4>

	<div id="masterDetailArea">
		<%@ include file="masterDetailLoadUp.jsp"%>
	</div>

	<c:if test="${isGroupedActivity}">
	<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
	</c:if>
	
	<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">

		<c:if test="${isGroupedActivity}">
			<div class="panel panel-default" >
	        <div class="panel-heading" id="heading${sessionDto.sessionId}">
    	    	<span class="panel-title collapsable-icon-left">
        		<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${sessionDto.sessionId}" 
					aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${sessionDto.sessionId}" >
				<fmt:message key="group.label" />&nbsp;${sessionDto.sessionName}</a>
				</span>
        	</div>
        
	        <div id="collapse${sessionDto.sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${sessionDto.sessionId}">
		</c:if>
					
			<table id="group${sessionDto.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
			<div id="pager-${sessionDto.sessionId}" class="scroll"></div>
			
			<div style="margin-top: 10px; width:99%;">
				<table id="userSummary${sessionDto.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
			</div>
			
		<c:if test="${isGroupedActivity}">
			</div> <!-- end collapse area  -->
			</div> <!-- end collapse panel  -->
		</c:if>
		${ !isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
		
	</c:forEach>

	<c:if test="${isGroupedActivity}">
	</div>
	</c:if>

	<jsp:include page="/monitoring/Reflections.jsp" />
				
</c:if>

<c:if test="${noSessionsNotebookEntriesExist == 'true'}"> 							
	<jsp:include page="/monitoring/Reflections.jsp" />
</c:if>

<%@ include file="parts/advanceOptions.jsp"%>
<%@ include file="parts/dateRestriction.jsp"%>
