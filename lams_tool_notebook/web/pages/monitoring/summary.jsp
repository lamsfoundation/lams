<%@ include file="/common/taglibs.jsp"%>
<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<link type="text/css" href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}css/jquery.jqGrid.css" rel="stylesheet"/>
<style media="screen,projection" type="text/css">
    .ui-jqgrid tr.jqgrow td, .ui-jqgrid .user-entry {
        word-wrap: break-word; /* IE 5.5+ and CSS3 */
        white-space: pre-wrap; /* CSS3 */
        white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
        white-space: -pre-wrap; /* Opera 4-6 */
        white-space: -o-pre-wrap; /* Opera 7 */
        overflow: hidden;
        height: auto;
        vertical-align: middle;
        padding-top: 2px;
        padding-bottom: 2px
    }
	
	#session-list {
		padding: 15px 20px 10px;
	}
	
	#session-list > h1 {
		margin-left: -10px;
	}
	
	.comment-area {
		background-color: whitesmoke;
	}
	
	.comment-area input.jip-ok-button {
		margin: 7px 7px 5px 20px;
	}
	
	.comment-area input.jip-cancel-button {
		margin-right: 5px;
	}
	
	.comment-area #notify-learner {
		margin-right: 5px;
		vertical-align: middle;
	}
	
	.jip-button {
		padding: 4px;
	}
</style>

<script type="text/javascript">
	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring.do?dispatch=setSubmissionDeadline"/>',
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/jinplace-1.0.1.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		<c:forEach var="sessionDto" items="${notebookDTO.sessionDTOs}" varStatus="status">
			<c:set var="sessionId" value ="${sessionDto.sessionID}"/>
   			<c:choose>
				<c:when test="${isGroupedActivity}">
					<c:set var="gridCaption" value="${sessionDto.sessionName}"/>
				</c:when>
				<c:otherwise>
					<c:set var="gridCaption"><fmt:message key='label.group.summary' /></c:set>
				</c:otherwise>
			</c:choose>
			
			//fill up data array for subgrid
			var entryData${sessionId} = [];
			var teachersCommentData${sessionId} = [];
   	        <c:forEach var="userDto" items="${sessionDto.userDTOs}" varStatus="i">
   	    		entryData${sessionId}['${userDto.uid}'] = "<c:if test='${not empty userDto.entryDTO}'>${userDto.entryDTO.entryEscaped}</c:if>";
   	    		
   	    		teachersCommentData${sessionId}['${userDto.uid}'] = "${userDto.teachersComment}";
	        </c:forEach>
		
			jQuery("#group${sessionId}").jqGrid({
				datatype: "local",
				height: 'auto',
				width: 780,
				
			   	colNames:['#',
						'userUid',
						"<fmt:message key="label.user.name" />",
					    "<fmt:message key="label.last.edited" />"],
					    
			   	colModel:[
			   		{name:'id',index:'id', width:10, hidden: true},
			   		{name:'userUid',index:'userUid', width:0, hidden: true},
			   		{name:'userName',index:'userName', width:200},
			   		{name:'lastEdited',index:'lastEdited', width:120, sorttype:"date"}		
			   	],
			   	
			   	multiselect: false,
			   	caption: " ${gridCaption}",
			    subGrid: true,			    
			    subGridOptions: {
			        reloadOnExpand: false, // Prevent jqGrid from wiping out the subgrid data.
			    },
			    gridComplete: function() {
			    	
			    	//expand subgrids that have available notebook entry
	                var rowIds = $("#group${sessionId}").getDataIDs();
	                $.each(rowIds, function (index, rowId) {
	                	
	                	var userUid = jQuery("#group${sessionId}").getCell(rowId, 'userUid');
	                	if (entryData${sessionId}[userUid]) {
	                		$("#group${sessionId}").expandSubGridRow(rowId);
	                	}
	                });
	            },
				subGridRowExpanded: function(subgridDivId, rowId) {
					var subgrid = jQuery("#"+subgridDivId);
					
					//display "not available" sign if there is no entry
					if (! (typeof(jQuery("#group${sessionId}").getCell) === "function")) {
						subgrid.append("<fmt:message key='label.notAvailable' />");
						return;
					}
					
					var userUid = jQuery("#group${sessionId}").getCell(rowId, 'userUid');
					
					//display entry itself
					$('<div/>', {
						"class": ''<c:if test="${!notebookDTO.allowRichEditor}"> + 'user-entry'</c:if>,
					    html: entryData${sessionId}[userUid]
					}).appendTo(subgrid);
					subgrid.append("<br>");
					
					subgrid.append('<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all ui-state-default ui-th-column ui-th-ltr" style="text-align:left; margin-bottom: 4px;padding: 3px;"><fmt:message key="label.comment" /></div>');
					//ui-widget-content
					//display teacher's comment
					$('<div/>', {
						"class": "user-entry editable${sessionId}_" + rowId + "_t comment-area",
						"data-type": "textarea",
						"data-checkbox-label": "<fmt:message key='label.notify.learner' />",
					    html: teachersCommentData${sessionId}[userUid]
					}).appendTo(subgrid);
					
					$(".editable${sessionId}_" + rowId + "_t").jinplace({
					    url: "<c:url value='/monitoring.do'/>?dispatch=saveTeacherComment&userUid=" + userUid,
					    textOnly: false,
					    placeholder: '<fmt:message key="label.click.to.edit" />',
					    okButton: "<fmt:message key='button.ok' />",
					    cancelButton: "<fmt:message key='button.cancel' />"
					});

					
					//<c:url value='/monitoring.do'/>?dispatch=saveTeacherComment&userUid=" + userUid, <fmt:message key="label.comment" />

				}
			});
			
   	        <c:forEach var="userDto" items="${sessionDto.userDTOs}" varStatus="i">
	   			<c:choose>
					<c:when test="${empty userDto.entryDTO}">
						<c:set var="lastModified" value ="-"/>
					</c:when>
					<c:otherwise>
						<c:set var="lastModified"><lams:Date value='${userDto.entryDTO.lastModified}'/></c:set>
					</c:otherwise>
				</c:choose>
   	        
   	     		jQuery("#group${sessionId}").addRowData(${i.index + 1}, {
   	   	     		id:"${i.index + 1}",
   	   	     		userUid:"${userDto.uid}",
   	   	     		userName:"${fn:escapeXml(userDto.firstName)} ${fn:escapeXml(userDto.lastName)}",
   	   	     		lastEdited:"${lastModified}"
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

</script>

<h1>
	<c:out value="${notebookDTO.title}" escapeXml="true"/>
</h1>

<div class="instructions space-top">
	<c:out value="${notebookDTO.instructions}" escapeXml="false"/>
</div>

<c:if test="${empty notebookDTO.sessionDTOs}">
	<div align="center">
		<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
	</div>
</c:if>

<div id="session-list">
	<c:forEach var="sessionDto" items="${notebookDTO.sessionDTOs}" varStatus="firstGroup">
	
		<c:if test="${isGroupedActivity}">
			<h1>
				${sessionDto.sessionName}
			</h1>
			<br>
		</c:if>
		
		<table id="group${sessionDto.sessionID}" class="scroll" cellpadding="0" cellspacing="0" ></table>
		<br><br>
		
	</c:forEach>
</div>

<%@include file="advanceOptions.jsp"%>

<%@include file="daterestriction.jsp"%>
