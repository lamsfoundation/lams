<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="isShrinkToFit" value="${(145 + fn:length(assessment.questions)*80) < 630}"/>

<link type="text/css" href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">
<link href="${lams}css/jquery.jqGrid.css" rel="stylesheet" type="text/css"/>
<style media="screen,projection" type="text/css">
	.ui-jqgrid tr.jqgrow td {
	    white-space: normal !important;
	    height:auto;
	    vertical-align:text-top;
	    padding-top:2px;
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
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
		
			jQuery("#group${sessionDto.sessionId}").jqGrid({
				datatype: "json",
				url: "<c:url value='/monitoring.do'/>?dispatch=getPagedUsers&toolSessionID=${sessionDto.sessionId}",
				height: 'auto',
				width: 630,
				shrinkToFit: ${isShrinkToFit},
				pager: 'pager-${sessionDto.sessionId}',
				rowList:[10,20,30,40,50,100],
				rowNum:10,
				viewrecords:true,
			   	caption: "${sessionDto.sessionName}",
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
				shrinkToFit: false,
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
			var grid;
		    if (grid = jQuery(".ui-jqgrid-btable:visible")) {
		    	grid.each(function(index) {
			        var gridId = $(this).attr('id');
			        
			        //resize only user summary grids
			        if (gridId.match("^userSummary")) {
				        var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
				        jQuery('#' + gridId).setGridWidth(gridParentWidth, true);			        	
			        }
		        });
		    }
		});
	});

</script>

<h1>
	<c:out value="${mcGeneralMonitoringDTO.activityTitle}" escapeXml="true"/>
</h1>

<div class="instructions space-top">
	<c:out value="${mcGeneralMonitoringDTO.activityInstructions}" escapeXml="false"/>
</div>
<%@ include file="parts/advanceQuestions.jsp"%>

<c:if test="${useSelectLeaderToolOuput}">
	<div class="info space-top">
		<fmt:message key="label.info.use.select.leader.outputs" />
	</div>
	<br>
</c:if>

<c:if test="${(mcGeneralMonitoringDTO.userExceptionNoToolSessions == 'true')}"> 	
	<c:if test="${notebookEntriesExist != 'true' }"> 			
		<table align="center">
			<tr> 
				<td NOWRAP valign=top align=center> 
					<b>  <fmt:message key="error.noLearnerActivity"/> </b>
				</td> 
			<tr>
		</table>
	</c:if>
</c:if>
			
<c:if test="${mcGeneralMonitoringDTO.userExceptionNoToolSessions != 'true'}">
	<br/>
	<h2 style="font-size: 15px; margin-left: 30px;">    
		<fmt:message key="label.studentMarks"/>
	</h2>

	<div id="masterDetailArea">
		<%@ include file="masterDetailLoadUp.jsp"%>
	</div>
		
	<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
			
		<div style="padding-left: 30px; <c:if test='${! status.last}'>padding-bottom: 30px;</c:if><c:if test='${ status.last}'>padding-bottom: 15px;</c:if> ">
			<c:if test="${isGroupedActivity}">
				<div style="padding-bottom: 5px; font-size: small;">
					<B><fmt:message key="group.label" /></B> ${sessionDto.sessionName}
				</div>
			</c:if>
					
			<table id="group${sessionDto.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
			<div id="pager-${sessionDto.sessionId}" class="scroll"></div>
			
			<div style="margin-top: 10px; width:99%;">
				<table id="userSummary${sessionDto.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
			</div>
		</div>
				
	</c:forEach>

	<jsp:include page="/monitoring/Reflections.jsp" />
				
	<html:link href="#" onclick="javascript:submitMonitoringMethod('downloadMarks');" styleClass="button float-right">
		<fmt:message key="label.monitoring.downloadMarks.button" />
	</html:link>
	<br><br>			
</c:if>

<c:if test="${noSessionsNotebookEntriesExist == 'true'}"> 							
	<jsp:include page="/monitoring/Reflections.jsp" />
</c:if>

<%@ include file="parts/advanceOptions.jsp"%>
<%@ include file="parts/dateRestriction.jsp"%>
