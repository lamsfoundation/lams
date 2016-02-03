<%@ include file="/common/taglibs.jsp"%>
<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<link type="text/css" href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">
<link type="text/css" href="${lams}css/jquery.jqGrid.css" rel="stylesheet"/>
<style media="screen,projection" type="text/css">
    .ui-jqgrid tr.jqgrow td, .ui-jqgrid .user-entry, .ui-search-input {
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
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/jinplace-1.0.1.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		<c:forEach var="sessionEntry" items="${notebookDTO.sessions}">
			<c:set var="sessionId" value ="${sessionEntry.key}"/>
			<c:set var="sessionName" value ="${sessionEntry.value}"/>
   			<c:choose>
				<c:when test="${isGroupedActivity}">
					<c:set var="gridCaption" value="${sessionName}"/>
				</c:when>
				<c:otherwise>
					<c:set var="gridCaption"><fmt:message key='label.notebook.entries' /></c:set>
				</c:otherwise>
			</c:choose>
			
			jQuery("#group${sessionId}").jqGrid({
			
				url: "<c:url value='/monitoring.do'/>?dispatch=getUsers&toolSessionID=${sessionId}",
				datatype: 'json',
				jsonReader : {
				     root: "rows",
				     page: "page",
				     total: "total",
				     records: "total_rows",
				     repeatitems: false,
				     id: "id",
				   },
				   
				loadError: function(xhr, status, error){ 
					alert("Error has occured "+error);
				},	
				
				subGrid: true,
			    subGridOptions: {
			        reloadOnExpand: false, // Prevent jqGrid from wiping out the subgrid data.
			    },

 				gridComplete: function() {	
					var dop = $("#group${sessionId}");
 			    	//expand subgrids that have available notebook entry, reformatting 
 			    	// the comment data for the "comment summary / comment sort" field.
 	                var rowIds = $("#group${sessionId}").getDataIDs();
 	                $.each(rowIds, function (index, rowId) {
 	                	var comment = jQuery("#group${sessionId}").getCell(rowId, 'comment');
 	                	if ( comment && comment.length > 0 ){
							jQuery("#group${sessionId}").setCell(rowId, 'commentsort','<fmt:message key="label.yes" />');
						} else {
							jQuery("#group${sessionId}").setCell(rowId, 'commentsort','<fmt:message key="label.no" />');
						}
 	                	var entry = jQuery("#group${sessionId}").getCell(rowId, 'entry');
 	                	if (entry && entry.length > 0) {
 	                		$("#group${sessionId}").expandSubGridRow(rowId);
 	                	}
 	                });
 	            },
 				subGridRowExpanded: function(subgridDivId, rowId) {
 					var subgrid = jQuery("#"+subgridDivId);
 					
 					//display "not available" sign if there is no entry
 					if (! (typeof(jQuery("#group${sessionId}").getCell) === "function")) {
 						subgrid.append("<fmt:message key='label.notAvailable' />");
 					}
 					
 					var entry = jQuery("#group${sessionId}").getCell(rowId, 'entry');
 					var comment = jQuery("#group${sessionId}").getCell(rowId, 'comment');
 					var userUid = jQuery("#group${sessionId}").getCell(rowId, 'userUid');

 					//display entry itself
 					$('<div/>', {
 						"class": ''<c:if test="${!notebookDTO.allowRichEditor}"> + 'user-entry'</c:if>,
 					    html: entry
 					}).appendTo(subgrid);
 					subgrid.append("<br>");
 					
 					subgrid.append('<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all ui-state-default ui-th-column ui-th-ltr" style="text-align:left; margin-bottom: 4px;padding: 3px;"><fmt:message key="label.comment" /></div>');
 					//ui-widget-content
 					//display teacher's comment
 					$('<div/>', {
 						"class": "user-entry editable${sessionId}_" + rowId + "_t comment-area",
 						"data-type": "textarea",
 						"data-checkbox-label": "<fmt:message key='label.notify.learner' />",
 					    html: comment
 					}).appendTo(subgrid);
 					
 					$(".editable${sessionId}_" + rowId + "_t").jinplace({
 					    url: "<c:url value='/monitoring.do'/>?dispatch=saveTeacherComment&userUid=" + userUid,
 					    textOnly: false,
 					    placeholder: '<fmt:message key="label.click.to.edit" />',
 					    okButton: "<fmt:message key='button.ok' />",
 					    cancelButton: "<fmt:message key='button.cancel' />"
 					});
 				},

				pager : '#group${sessionId}pager',
				pagerpos: "left",
				rowNum: 10,
				rowList:[10,20,30,40,50,100],
				
				height: 'auto',
				width: 780,
				
			   	colNames:['#',
						'userUid',
						"<fmt:message key="label.user.name" />",
					    "<fmt:message key="label.lastModified" />",
					    'entry',
					    "<fmt:message key="label.comment" />",  // comment summary for sorting
					    'actualComment'],
					    
			   	colModel:[
			   		{name:'id',index:'id', width:10, hidden: true, search: false},
			   		{name:'userUid',index:'userUid', width:0, hidden: true, search: false},
			   		{name:'userName',index:'userName', width:200},
			   		{name:'lastEdited',index:'lastEdited', width:120, search: false, sortable: false},		
			   		{name:'entry',index:'entry', hidden: true, width:0, search: false},	
			   		{name:'commentsort',index:'commentsort', width:40, search: false },
			   		{name:'comment',index:'comment', hidden: true, width:0, search: false }		
			   	],
			   	
			   	multiselect: false,
			   	caption: " ${gridCaption}"
			}).jqGrid('filterToolbar',{searchOnEnter: false});
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

<c:if test="${empty notebookDTO.sessions}">
	<div align="center">
		<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
	</div>
</c:if>

<div id="session-list">
	<c:forEach var="session" items="${notebookDTO.sessions}">
	
		<c:if test="${isGroupedActivity}">
			<h1>
				${session.value}
			</h1>
			<br>
		</c:if>
		
		<table id="group${session.key}" class="scroll" cellpadding="0" cellspacing="0" ></table>
		<div id="group${session.key}pager"></div> 
		<br><br>
		
	</c:forEach>
</div>

<%@include file="advanceOptions.jsp"%>

<%@include file="daterestriction.jsp"%>
