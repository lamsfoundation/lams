<%@ include file="/common/taglibs.jsp"%>
<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet"/>
<link type="text/css" href="${lams}css/free.ui.jqgrid.min.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet"/>
<style>
	/* remove jqGrid border radius */
	.ui-jqgrid.ui-jqgrid-bootstrap {
	    border-radius:0;
	    -moz-border-radius:0;
	    -webkit-border-radius:0;
	    -khtml-border-radius:0;
	}
</style>

<script type="text/javascript">
	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		submissionDateString: '${submissionDateString}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>?<csrf:token/>',
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};

	$(document).ready(function(){
		
		<c:forEach var="sessionEntry" items="${notebookDTO.sessions}">
			<c:set var="sessionId" value ="${sessionEntry.key}"/>
			<c:set var="sessionName" value ="${sessionEntry.value}"/>
			
			jQuery("#group${sessionId}").jqGrid({
				datatype: 'json',
				url: "<c:url value='/monitoring/getUsers.do?toolSessionID=${sessionId}'/>",
				autoencode:false,
				pager : '#group${sessionId}pager',
				pagerpos: "left",
				rowNum: 10,
				rowList:[10,20,30,40,50,100],
				height: 'auto',
				autowidth: true,
				shrinkToFit: false,
			   	multiselect: false,
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
			   	colNames:['#',
						'userUid',
 						"<fmt:message key="label.user.name" />",
 					    "<fmt:message key="label.lastModified" />",
					    "<fmt:message key="label.lastModified" />",
					    "<fmt:message key="label.lastModified" />",
	 				    'entry',
					    "<fmt:message key="label.comment" />",  // comment summary for sorting
					    'actualComment',
	 					'userId',
						'portraitId'
				],
			   	colModel:[
			   		{name:'id',index:'id', width:10, hidden: true, search: false},
			   		{name:'userUid',index:'userUid', width:0, hidden: true, search: false},
 			   		{name:'userName',index:'userName', width:200, formatter:userNameFormatter},
 			   		{name:'lastEdited',index:'lastEdited', hidden: true, width:0, search: false},		
			   		{name:'lastEditedTimeago',index:'lastEditedTimeago', hidden: true, width:0,  search: false},		
			   		{name:'lastEditedTimeagoOutput',index:'lastEditedTimeagoOutput', width:120, search: false},		
 			   		{name:'entry',index:'entry', hidden: true, width:0, search: false},	
			   		{name:'commentsort',index:'commentsort', width:40, search: false },
			   		{name:'comment',index:'comment', hidden: true, width:0, search: false},
 			   		{name:'userId',index:'userId', width:0, hidden: true, search: false},
			   		{name:'portraitId',index:'portraitId', width:0, hidden: true, search: false}
			   	],
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
  					initializePortraitPopover('${lams}');
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
 	                	var lastEdited = jQuery("#group${sessionId}").getCell(rowId, 'lastEdited');
 	                	var lastEditedTimeagoFormat = jQuery("#group${sessionId}").getCell(rowId, 'lastEditedTimeago');
 	                	if ( lastEditedTimeagoFormat && lastEditedTimeagoFormat.length > 0) {
 	                		jQuery("#group${sessionId}").setCell(rowId, 'lastEditedTimeagoOutput', 
 	                				'<time class="timeago" title="'+lastEdited+'" datetime="'+lastEditedTimeagoFormat+'"></time>'); 
 	                	}
 	                	var entry = jQuery("#group${sessionId}").getCell(rowId, 'entry');
 	                	if (entry && entry.length > 0) {
 	                		$("#group${sessionId}").expandSubGridRow(rowId);
 	                	}
 	                }); 
 	               $("time.timeago").timeago();
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
 						"data-checkbox-label": "<fmt:message key='label.notify.learner' />"
 					}).text(comment).appendTo(subgrid); 
  					
 					$(".editable${sessionId}_" + rowId + "_t").jinplace({
 					    url: "<c:url value='/monitoring/saveTeacherComment.do?userUid='/>" + userUid + "&<csrf:token/>",
 					    textOnly: true,
 					    placeholder: '<fmt:message key="label.click.to.edit" />',
 					    okButton: "<fmt:message key='button.ok' />",
 					    cancelButton: "<fmt:message key='button.cancel' />"
 					});
 				}
			}).jqGrid('filterToolbar',{searchOnEnter: false});
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

        function userNameFormatter (cellvalue, options, rowObject) {
    		return definePortraitPopover(rowObject.portraitId, rowObject.userId,  rowObject.userName);
	    }
	});

</script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<div class="panel">
	<h4>
	    <c:out value="${notebookDTO.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${notebookDTO.instructions}" escapeXml="false"/>
	</div>
	
		<c:if test="${empty notebookDTO.sessions}">
			<lams:Alert type="info" id="no-session-summary" close="false">
				<fmt:message key="message.monitoring.summary.no.session" />
			</lams:Alert>
		</c:if>
		
	</div>
	 
	<c:if test="${isGroupedActivity}">
		<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
	</c:if>

	<c:forEach var="session" items="${notebookDTO.sessions}" varStatus="status">

		<c:if test="${isGroupedActivity}">	
		    <div class="panel panel-default" >
		        <div class="panel-heading" id="heading${session.key}">
		        	<span class="panel-title collapsable-icon-left">
		        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${session.key}" 
							aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${session.key}" >
					<fmt:message key="message.session.name" />:	<c:out value="${session.value}" /></a>
					</span>
		        </div>
	        
	       		<div id="collapse${session.key}" class="panel-collapse collapse ${status.first ? 'in' : ''}"
	        	 role="tabpanel" aria-labelledby="heading${session.key}">
		</c:if>
	 
	 	<table id="group${session.key}" class="scroll" cellpadding="0" cellspacing="0" ></table>
		<div id="group${session.key}pager"></div>
 
		<c:if test="${isGroupedActivity}">
			</div> <!-- end collapse area  -->
			</div> <!-- end collapse panel  -->
		</c:if>
	${ !isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
	</c:forEach>
	
	<c:if test="${isGroupedActivity}">
		</div> <!--  end panel group -->
	</c:if> 

<%@include file="advanceOptions.jsp"%>

<%@include file="daterestriction.jsp"%>
