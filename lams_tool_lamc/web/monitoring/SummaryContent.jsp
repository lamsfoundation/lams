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
		<c:forEach var="sessionMarkDto" items="${listMonitoredMarksContainerDto}" varStatus="status">
		
			jQuery("#group${sessionMarkDto.sessionId}").jqGrid({
				datatype: "local",
				height: 'auto',
				width: 630,
				shrinkToFit: ${isShrinkToFit},
				
			   	colNames:['#',
						'userUid',
						"<fmt:message key="label.monitoring.summary.user.name" />",
					    "<fmt:message key="label.monitoring.summary.total" />"],
					    
			   	colModel:[
			   		{name:'id',index:'id', width:20, sorttype:"int"},
			   		{name:'userUid',index:'userUid', width:0, hidden: true},
			   		{name:'userName',index:'userName', width:200},
			   		{name:'total',index:'total', width:50,align:"right",sorttype:"int"}		
			   	],
			   	
			   	multiselect: false,
			   	caption: "${sessionMarkDto.sessionName}",
			  	onSelectRow: function(rowid) { 
			  	    if(rowid == null) { 
			  	    	rowid=0; 
			  	    } 
			   		var userUid = jQuery("#group${sessionMarkDto.sessionId}").getCell(rowid, 'userUid');
					var userMasterDetailUrl = '<c:url value="/monitoring.do"/>';
		  	        jQuery("#userSummary${sessionMarkDto.sessionId}").clearGridData().setGridParam({gridstate: "visible"}).trigger("reloadGrid");
		  	        $("#masterDetailArea").load(
		  	        	userMasterDetailUrl,
		  	        	{
		  	        		dispatch: "userMasterDetail",
		  	        		userUid: userUid
		  	       		}
		  	       	);
	  	  		} 
			}).hideCol("userId").hideCol("sessionId");
			
   	        <c:forEach var="userMarkEntity" items="${sessionMarkDto.userMarks}" varStatus="i">
   	     		<c:set var="mcSessionMarkDTO" scope="request" value="${userMarkEntity.value}"/>
   	     		jQuery("#group${sessionMarkDto.sessionId}").addRowData(${i.index + 1}, {
   	   	     		id:"${i.index + 1}",
   	   	     		userUid:"${mcSessionMarkDTO.queUsrId}",
   	   	     		userName:"${mcSessionMarkDTO.fullName}",
   	   	     		total:"${mcSessionMarkDTO.totalMark}"
   	   	   	    });
	        </c:forEach>
	        
	        var oldValue = 0;
			jQuery("#userSummary${sessionMarkDto.sessionId}").jqGrid({
				datatype: "local",
				gridstate:"hidden",
				//hiddengrid:true,
				height: 110,
				width: 780,
				shrinkToFit: false,
				scrollOffset: 0,
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
  				afterSaveCell : function (rowid,name,val,iRow,iCol){
  					var intRegex = /^\d+$/;
  					if (!intRegex.test(val)) {
  						jQuery("#userSummary${sessionMarkDto.sessionId}").restoreCell(iRow,iCol); 
  					} else {
  						var parentSelectedRowId = jQuery("#group${sessionMarkDto.sessionId}").getGridParam("selrow");
  						var previousTotal =  eval(jQuery("#group${sessionMarkDto.sessionId}").getCell(parentSelectedRowId, 'total'));
  						jQuery("#group${sessionMarkDto.sessionId}").setCell(parentSelectedRowId, 'total', previousTotal - oldValue + eval(val), {}, {});
  					}
				},	  		
  				beforeSubmitCell : function (rowid,name,val,iRow,iCol){
  					var intRegex = /^\d+$/;
  					if (!intRegex.test(val)) {
  						return {nan:true};
  					} else {
  						var userAttemptUid = jQuery("#userSummary${sessionMarkDto.sessionId}").getCell(rowid, 'userAttemptUid');
  						return {userAttemptUid:userAttemptUid};		  				  		
  				  	}
  				}
			});
			
		</c:forEach>
	});

</script>

<%@ include file="parts/advanceOptions.jsp"%>
<%@ include file="parts/dateRestriction.jsp"%>

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

	<c:choose>
		<c:when test="${mcGeneralMonitoringDTO.displayAnswers == 'true'}">
			<p>
				<fmt:message key="label.monitoring.yesDisplayAnswers"/>
			</p>
		</c:when>
		<c:when test="${mcGeneralMonitoringDTO.displayAnswers == 'false'}">
			<p>
				<fmt:message key="label.monitoring.noDisplayAnswers1"/><br>
				<quote>
				<fmt:message key="label.monitoring.noDisplayAnswers2"/>
				<input onclick="javascript:submitChangeDisplayAnswers(this.value, 'displayAnswers');" class="button" name="displayAnswers" class="button" value="<fmt:message key='button.monitoring.noDisplayAnswers'/>" type="button">	
				</quote>
			</p>
		</c:when>
	</c:choose>
		
		<c:if test="${mcGeneralMonitoringDTO.userExceptionNoToolSessions != 'true'}">

			<h2>    
				<fmt:message key="label.studentMarks"/>
			</h2>

			<div id="masterDetailArea">
				<%@ include file="masterDetailLoadUp.jsp"%>
			</div>
		
			<c:forEach var="sessionMarkDto" items="${listMonitoredMarksContainerDto}" varStatus="status">
			
				<div style="padding-left: 30px; <c:if test='${! status.last}'>padding-bottom: 30px;</c:if><c:if test='${ status.last}'>padding-bottom: 15px;</c:if> ">
					<c:if test="${isGroupedActivity}">
						<div style="padding-bottom: 5px; font-size: small;">
							<B><fmt:message key="group.label" /></B> ${sessionMarkDto.sessionName}
						</div>
					</c:if>
					
					<table id="group${sessionMarkDto.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
					<div style="margin-top: 10px;">
						<table id="userSummary${sessionMarkDto.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
					</div>
				</div>
				
			</c:forEach>

			<jsp:include page="/monitoring/Reflections.jsp" />
				
			<html:link href="#" onclick="javascript:submitMonitoringMethod('downloadMarks');" styleClass="button float-right">
				<fmt:message key="label.monitoring.downloadMarks.button" />
			</html:link>					
		</c:if>						
		
		
		<c:if test="${noSessionsNotebookEntriesExist == 'true'}"> 							
			<jsp:include page="/monitoring/Reflections.jsp" />
		</c:if>
