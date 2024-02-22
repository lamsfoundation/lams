<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="scratchie" value="${sessionMap.scratchie}"/>

<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme5.css" rel="stylesheet">
<link type="text/css" href="${lams}css/thickbox.css" rel="stylesheet"  media="screen">
<link href="${lams}css/jquery-ui.timepicker.css" rel="stylesheet" type="text/css" >
<link href="${lams}css/free.ui.jqgrid.custom.css" rel="stylesheet" type="text/css" >
<lams:css suffix="chart"/>
<style type="text/css">
	/* remove jqGrid borders */
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
	
	.ui-jqgrid tr.jqgrow td {
	    padding-top:2px;
	}
	.ui-jqgrid tr.jqgrow td {
		vertical-align:middle !important
	}
	
	/* remove jqGrid border radius */
	.ui-jqgrid.ui-jqgrid-bootstrap {
	    border-radius:0;
	    -moz-border-radius:0;
	    -webkit-border-radius:0;
	    -khtml-border-radius:0;
	}
	
	#collapseBurning .panel {
		margin-bottom: 20px;
	}
	
	#collapseBurning .panel-heading a:after {
	  font-family: FontAwesome;
	  content: "\f139";
	  float: right;
	  color: grey;
	}
	
	#collapseBurning .panel-heading a.collapsed:after {
	  content: "\f13a";
	 }
	
	#collapseBurning .burning-question-title {
	  cursor: pointer;
	}
</style>

<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/thickbox.js"></script> 
<lams:JSImport src="includes/javascript/portrait5.js" />
<!--  File Download -->
<script type="text/javascript" src="${lams}includes/javascript/jquery.cookie.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/download.js"></script>
 <!--  Marks Chart -->
 <script type="text/javascript" src="${lams}includes/javascript/d3.js"></script>
 <script type="text/javascript" src="${lams}includes/javascript/chart.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		 $('[data-bs-toggle="tooltip"]').tooltip();
		 
		var oldValue = 0;
		
		<c:forEach var="summary" items="${summaryList}" varStatus="status">
			jQuery("#list${summary.sessionId}").jqGrid({
				datatype: "local",
				rowNum: 10000,
				height: 'auto',
				autowidth: true,
				shrinkToFit: false,
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
			   	colNames:[
				   	'#',
					'userId',
					'sessionId',
					"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.summary.user.name' /></spring:escapeBody>",
					"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.summary.attempts' /></spring:escapeBody>",
					"<spring:escapeBody javaScriptEscape='true'><xage key='label.monitoring.summary.mark' /></spring:escapeBody>",
					'portraitId',
					'isLeader',
					'reachedActivity'
				],
			   	colModel:[
			   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
			   		{name:'userId', index:'userId', width:0, hidden: true},
			   		{name:'sessionId', index:'sessionId', width:0, hidden: true},
			   		{name:'userName', index:'userName', width:570, formatter:userNameFormatter,	cellattr: leaderRowFormatter},
			   		{name:'totalAttempts', index:'totalAttempts', width:100, align:"right", sortable: false, cellattr: leaderRowFormatter},
			   		{name:'mark', index:'mark', width:100, align:"right", sortable: false, editoptions: {size:4, maxlength: 4}, cellattr: leaderRowFormatter,
				   	       editable: function (options) {
				               var row = $(this).jqGrid("getLocalRow", options.rowid);
				               return row.isLeader == 'true';
				           }
			   		},
			   		{name:'portraitId', index:'portraitId', width:0, hidden: true},
			   		{name:'isLeader', index:'isLeader', width:0, hidden: true},
			   		{name:'reachedActivity', index:'reachedActivity', width:0, hidden: true},
			   	],
			   	ondblClickRow: function(rowid) {
			   		var jqGrid = $("#list${summary.sessionId}");
			   		if (jqGrid.getCell(rowid, 'isLeader') != 'true') {
			   			return;
			   		}
			   		var userId = jqGrid.getCell(rowid, 'userId');
			   		var toolSessionId = jqGrid.getCell(rowid, 'sessionId');

			   		var userSummaryUrl = "<c:url value='/learning/start.do'/>?userID=" + userId + "&toolSessionID=" + toolSessionId + "&mode=teacher&reqId=" + (new Date()).getTime();
					launchPopup(userSummaryUrl, "MonitoringReview");		
			  	},
			   	// caption: "${summary.sessionName}",
				cellurl: '<c:url value="/monitoring/saveUserMark.do"/>?<csrf:token/>',
  				cellEdit: true,
  				afterEditCell: function (rowid,name,val,iRow,iCol){
  					oldValue = eval(val);
				},
  				afterSaveCell : function (rowid,name,val,iRow,iCol){
  					var number = new Number(val);
  					if (Number.isNaN(number)) {
  						jQuery("#list${summary.sessionId}").restoreCell(iRow,iCol); 
  					}
				},	  		
  				beforeSubmitCell : function (rowid,name,val,iRow,iCol){
  					var number = new Number(val);
  					if (Number.isNaN(number)) {
  						return {nan:true};
  					} else {
  						var userId = jQuery("#list${summary.sessionId}").getCell(rowid, 'userId');
  						var sessionId = jQuery("#list${summary.sessionId}").getCell(rowid, 'sessionId');
  						return {userId:userId, sessionId:sessionId};		  				  		
  				  	}
  				}
			});
			
   	        <c:forEach var="user" items="${summary.users}" varStatus="i">
   	     		jQuery("#list${summary.sessionId}").addRowData(${i.index + 1}, {
   	   	     		id:"${i.index + 1}",
   	   	     		userId:"${user.userId}",
   	   	     		sessionId:"${empty user.session ? '' : user.session.sessionId}",
   	   	     		userName:"${user.lastName}, ${user.firstName}",
   	   				totalAttempts:"${summary.leaderUid eq user.uid ? summary.totalAttempts : ''}",
   	   				mark:"${summary.leaderUid eq user.uid ? (summary.totalAttempts == 0 ? '-' : summary.mark) : ''}",
   	   				portraitId:"${user.portraitId}",
   	   				isLeader : "${not empty summary.leaderUid and summary.leaderUid eq user.uid}",
   	   				reachedActivity : "${summary.getUsersWhoReachedActivity().contains(user.userId)}"
   	   	   	    });
	        </c:forEach>

		</c:forEach>

		initializePortraitPopover('<lams:LAMSURL/>');

		<!-- Display burningQuestionItemDtos -->
		<c:forEach var="burningQuestionItemDto" items="${sessionMap.burningQuestionItemDtos}" varStatus="i">
			<c:set var="scratchieItem" value="${burningQuestionItemDto.scratchieItem}"/>
			
			jQuery("#burningQuestions${scratchieItem.uid}").jqGrid({
				datatype: "local",
				rowNum: 10000,
				height: 'auto',
				autowidth: true,
				shrinkToFit: false,
				autoencode: false,
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
			   	colNames:['#',
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.team' /></spring:escapeBody>",
					    "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.burning.questions' /></spring:escapeBody>",
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.count' /></spring:escapeBody>"
				],
			   	colModel:[
			   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
			   		{name:'groupName', index:'groupName', width:150},
			   		{name:'feedback', index:'feedback', width:520},
			   		{name:'count', index:'count', align:"right", width:70}
			   	],
			   	// caption: "${scratchieItem.qbQuestion.name}"
			});
			
			<c:forEach var="burningQuestionDto" items="${burningQuestionItemDto.burningQuestionDtos}" varStatus="i">
		    	jQuery("#burningQuestions${scratchieItem.uid}").addRowData(${i.index + 1}, {
		   			id:"${i.index + 1}",
		   	     	groupName:"${burningQuestionDto.sessionName}",
		   	    	feedback:"${burningQuestionDto.escapedBurningQuestion}",
		   	  		count:"${burningQuestionDto.likeCount}"
		   	   	});
	        </c:forEach>
	        
	     	jQuery("#burningQuestions${scratchieItem.uid}").jqGrid('sortGrid','groupName', false, 'asc');
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
    		var name = definePortraitPopover(rowObject.portraitId, rowObject.userId,  rowObject.userName);
    		var icon = '';
    		
    		if (rowObject.isLeader == 'true') {
    			icon = '&nbsp;<i title="<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.monitoring.team.leader"/></spring:escapeBody>" class="text-primary fa fa-star"></i>';
    		} else if (rowObject.reachedActivity == 'true') {
    			icon = '&nbsp;<i class="text-primary fa fa-check"></i>';
    		}
    		
    		if (icon != '') {
    			if (rowObject.portraitId == '') {
    				name += icon;
    			} else {
    				name = name.replace('</a>', icon + '</a>');
    			}
    		}
    		
    		return name;
    	}
        
        function leaderRowFormatter (rowID, val, rawObject, cm, rdata) {
			if (rdata.isLeader == 'true') {
				return 'class="info"';
			} else if (rdata.reachedActivity == 'true') {
				return 'title="<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.summary.reached.activity"/></spring:escapeBody>"';
			}
		}
    	
		$("#item-uid").change(function() {
			var itemUid = $(this).val();
			if (itemUid != -1) {
				var itemSummaryUrl = '<c:url value="/monitoring/itemSummary.do?sessionMapID=${sessionMapID}"/>';
				var itemSummaryHref = itemSummaryUrl + "&itemUid=" + itemUid + "&KeepThis=true&TB_iframe=true";
				$("#item-summary-href").attr("href", itemSummaryHref);	

				//return;
				$("#item-summary-href").click(); 		 
			}
	    });
		
		$("#userid-dropdown").change(function() {
			var userId = $(this).val();
			
			if (userId != -1) {
				var toolSessionId = $(this).find('option:selected').attr("alt");
				var userSummaryUrl = "<c:url value='/learning/start.do'/>?userID=" + userId + "&toolSessionID=" + toolSessionId + "&mode=teacher";
	
				launchPopup(userSummaryUrl, "MonitoringReview");
			}
	    });
	});
	
	function exportExcel(){
		var url = "<c:url value='/monitoring/exportExcel.do'/>?<csrf:token/>&sessionMapID=${sessionMapID}&reqID=" + (new Date()).getTime();
		return downloadFile(url, 'messageArea_Busy', '<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.summary.downloaded"/></spring:escapeBody>', 'messageArea', 'btn-disable-on-submit');
	};

	function showChangeLeaderModal(toolSessionId) {
		let modalContainer = $('#change-leader-modals');
		modalContainer.empty().load(
			'<c:url value="/monitoring/displayChangeLeaderForGroupDialogFromActivity.do" />',
			{
				toolSessionID : toolSessionId
			}, 
			function(){
				modalContainer.children('.modal').modal('show');
			}
		);
	}

	function onChangeLeaderCallback(response, leaderUserId, toolSessionId){
        if (response.isSuccessful) {
            $.ajax({
    			'url' : '<c:url value="/monitoring/changeLeaderForGroup.do"/>',
    			'type': 'post',
    			'cache' : 'false',
    			'data': {
    				'toolSessionID' : toolSessionId,
    				'leaderUserId' : leaderUserId,
    				'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
    			},
    			success : function(){
    				alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.leader.successfully.changed'/></spring:escapeBody>");
    				location.reload();
    			},
    			error : function(){
    				alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.leader.not.changed'/></spring:escapeBody>");
        		}
            });
        	
		} else {
			alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.leader.not.changed'/></spring:escapeBody>");
		}
	}
	
	// pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '<lams:LAMSURL />',
		submissionDeadline: '${submissionDeadline}',
		submissionDateString: '${submissionDateString}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>?<csrf:token/>',
		toolContentID: '${scratchie.contentId}',
		messageNotification: '<spring:escapeBody javaScriptEscape='true'><fmt:message key="monitor.summary.notification" /></spring:escapeBody>',
		messageRestrictionSet: '<spring:escapeBody javaScriptEscape='true'><fmt:message key="monitor.summary.date.restriction.set" /></spring:escapeBody>',
		messageRestrictionRemoved: '<spring:escapeBody javaScriptEscape='true'><fmt:message key="monitor.summary.date.restriction.removed" /></spring:escapeBody>'
	};
</script>
<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<div class="instructions">
	<c:if test="${not empty summaryList}">
		<lams:WaitingSpinner id="messageArea_Busy"></lams:WaitingSpinner>
		<div class="clearfix">
			<div class="badge text-bg-info float-end px-5 py-3 mt-2" id="messageArea"></div>
		</div>
	
		<c:if test="${vsaPresent}">
			<a class="btn btn-sm btn-secondary float-end ms-2" target="_blank"
				   href='<lams:LAMSURL />qb/vsa/displayVsaAllocate.do?toolContentID=${scratchie.contentId}'>
				<i class="fa-solid fa-arrow-down-1-9 me-1"></i>
				<fmt:message key="label.vsa.allocate.button" />
			</a>
		</c:if>
	
		<button type="button" onclick="exportExcel()" class="btn btn-secondary btn-sm btn-disable-on-submit float-end">
			<i class="fa fa-file-excel me-1" aria-hidden="true"></i> 
			<fmt:message key="label.export.excel" />
		</button>
	</c:if>

	<div class="fs-4">
		<c:out value="${scratchie.title}" escapeXml="true"/>
	</div>
	
	<div class="mt-2">
		<c:out value="${scratchie.instructions}" escapeXml="false"/>
	</div>
</div>

<c:if test="${empty summaryList}">
	<lams:Alert5 type="info" id="no-session-summary">
		 <fmt:message key="message.monitoring.summary.no.session" />
	</lams:Alert5>
</c:if>
	
<c:set var="showStudentChoicesTableOnly" value="true" />
<c:set var="isTbl" value="false" />
<c:set var="toolContentID" value="${scratchie.contentId}" />
<div class="card-subheader fs-4">
	<fmt:message key="monitoring.tab.summary" />
</div>
<%@ include file="studentChoices.jsp"%>

<!-- Dropdown menu for choosing scratchie item -->
<div class="mb-4 mt-2">
	<div class="card-subheader fs-4">
		<label for="item-uid">
			<fmt:message key="label.monitoring.summary.report.by.scratchie" />
		</label>
	</div>
	
	<select id="item-uid" class="form-select">
		<option selected="selected" value="-1"><fmt:message key="label.monitoring.summary.choose" /></option>
   		<c:forEach var="item" items="${scratchie.scratchieItems}" varStatus="questionCount">
			<option value="${item.uid}">${questionCount.count})&nbsp;<c:out value="${item.qbQuestion.name}" escapeXml="true"/></option>
	   	</c:forEach>
	</select>
	<a href="#nogo" class="thickbox" id="item-summary-href" style="display: none;"></a>
</div>

<div class="mb-3">
	<div class="card-subheader fs-4">
		<fmt:message key="label.report.by.team.tra" />
	</div>
	<em>
		<fmt:message key="label.monitoring.summary.select.student" />
	</em>
</div>

<c:set var="summaryTitle"><fmt:message key="label.monitoring.summary.summary" /></c:set>
<c:forEach var="summary" items="${summaryList}" varStatus="status">
	<c:if test="${sessionMap.isGroupedActivity}">
		<c:set var="summaryTitle"><strong><fmt:message key="monitoring.label.group" /></strong> ${summary.sessionName}</c:set>
	</c:if>
		
	<div class="lcard" >
        <div class="card-header" id="heading${summary.sessionId}">
        	<span class="card-title collapsable-icon-left">
        		<button class="btn btn-secondary-darker no-shadow ${status.first ? '' : 'collapsed'}" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${summary.sessionId}" 
						aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${summary.sessionId}" >
					${summaryTitle}
				</button>
			</span>
			<c:if test="${fn:length(summary.users) > 0 and not summary.scratchingFinished}">
				<button type="button" class="btn btn-light btn-sm float-end"
						onClick="showChangeLeaderModal(${summary.sessionId})">
					<i class="fa-solid fa-user-pen me-1"></i>
					<fmt:message key='label.monitoring.change.leader'/>
				</button>
			</c:if>
        </div>
        
        <div id="collapse${summary.sessionId}" class="card-collapse collapse ${status.first ? 'show' : ''}">
			<table id="list${summary.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>	
		</div>
	</div>	
</c:forEach>

<!-- Display burningQuestionItemDtos -->
<c:if test="${scratchie.burningQuestionsEnabled}">
	<div class="lcard" >
		<div class="card-header collapsable-icon-left">
			<span class="card-title">
			    <button type="button" class="btn btn-secondary-darker no-shadow" data-bs-toggle="collapse" 
			    		data-bs-target="#collapseBurning" aria-expanded="true" aria-controls="collapseBurning" >
		          	<fmt:message key="label.burning.questions" />
		        </button>
			</span>
		</div>
		
		<div id="collapseBurning" class="card-body collapse show">
			<%@ include file="../tblmonitoring/burningQuestions.jsp"%>
		</div>
	</div>
</c:if>
	
<%@ include file="parts/advanceOptions.jsp"%>

<div id="time-limit-panel-placeholder"></div>

<lams:RestrictedUsageAccordian submissionDeadline="${submissionDeadline}" cssClass="my-2"/>

<div id="change-leader-modals"></div>
