<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="scratchie" value="${sessionMap.scratchie}"/>
	
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

<script type="text/javascript">
	$(document).ready(function(){
		 $('[data-toggle="tooltip"]').bootstrapTooltip();
		 
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
		$('#change-leader-modals').empty()
		.load('<c:url value="/monitoring/displayChangeLeaderForGroupDialogFromActivity.do" />',{
			toolSessionID : toolSessionId
		});
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
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

	<c:if test="${not empty summaryList}">
		<button onclick="return exportExcel();" class="btn btn-default btn-sm btn-disable-on-submit pull-right">
			<i class="fa fa-download" aria-hidden="true"></i> 
			<fmt:message key="label.export.excel" />
		</button>
	</c:if>

	<div class="panel">
		<h4>
		    <c:out value="${scratchie.title}" escapeXml="true"/>
		</h4>
		<div class="instructions voffset5">
		    <c:out value="${scratchie.instructions}" escapeXml="false"/>
		</div>
		
		<c:if test="${empty summaryList}">
			<lams:Alert type="info" id="no-session-summary" close="false">
				 <fmt:message key="message.monitoring.summary.no.session" />
			</lams:Alert>
		</c:if>
	
		<lams:WaitingSpinner id="messageArea_Busy"></lams:WaitingSpinner>
		<div class="voffset5 help-block" id="messageArea"></div>
	
	</div>
	
	<c:set var="showStudentChoicesTableOnly" value="true" />
	<c:set var="isTbl" value="false" />
	<h4><fmt:message key="monitoring.tab.summary" /></h4>
	<%@ include file="studentChoices.jsp"%>
	
	<div class="form-group">
		<!-- Dropdown menu for choosing scratchie item -->
		<label for="item-uid"><h4><fmt:message key="label.monitoring.summary.report.by.scratchie" /></h4></label>
		<select id="item-uid" class="form-control">
			<option selected="selected" value="-1"><fmt:message key="label.monitoring.summary.choose" /></option>
   			<c:forEach var="item" items="${scratchie.scratchieItems}" varStatus="questionCount">
				<option value="${item.uid}">${questionCount.count})&nbsp;<c:out value="${item.qbQuestion.name}" escapeXml="true"/></option>
		   	</c:forEach>
		</select>
		<a href="#nogo" class="thickbox" id="item-summary-href" style="display: none;"></a>
	</div>
	
	<c:if test="${vsaPresent}">
		<a class="btn btn-sm btn-default buttons_column" target="_blank"
		   href='<lams:LAMSURL />qb/vsa/displayVsaAllocate.do?toolContentID=${scratchie.contentId}'>
			<fmt:message key="label.vsa.allocate.button" />
		</a>
	</c:if>

	<h4 style="padding-top: 10px"><fmt:message key="label.report.by.team.tra" /></h4>
	<fmt:message key="label.monitoring.summary.select.student" />

	<c:set var="summaryTitle"><fmt:message key="label.monitoring.summary.summary" /></c:set>
	<c:forEach var="summary" items="${summaryList}" varStatus="status">

		<c:if test="${sessionMap.isGroupedActivity}">
			<c:set var="summaryTitle"><strong><fmt:message key="monitoring.label.group" /></strong> ${summary.sessionName}</c:set>
		</c:if>
		
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${summary.sessionId}">
        	<span class="panel-title collapsable-icon-left">
        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${summary.sessionId}" 
					aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${summary.sessionId}" >
				${summaryTitle}</a>
			</span>
			<c:if test="${fn:length(summary.users) > 0 and not summary.scratchingFinished}">
				<button type="button" class="btn btn-default btn-xs pull-right"
						onClick="javascript:showChangeLeaderModal(${summary.sessionId})">
					<fmt:message key='label.monitoring.change.leader'/>
				</button>
			</c:if>
        </div>
        
        <div id="collapse${summary.sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${summary.sessionId}">

		<table id="list${summary.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
		
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	
	</c:forEach>

	<!-- Display burningQuestionItemDtos -->
	<c:if test="${scratchie.burningQuestionsEnabled}">
		<div class="panel-group" style="padding-top: 10px" id="accordionBurning" role="tablist" aria-multiselectable="true"> 
		    <div class="panel panel-default" >
		        <div class="panel-heading collapsable-icon-left" id="headingBurning">
		        	<span class="panel-title">
			    	<a role="button" data-toggle="collapse" href="#collapseBurning" aria-expanded="false" aria-controls="collapseBurning" >
		          	<fmt:message key="label.burning.questions" />
		        	</a>
		      		</span>
		        </div>
		
		        <div id="collapseBurning" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingBurning" style="padding: 10px;">
					<%@ include file="parts/burningQuestions.jsp"%>
				</div>
			</div>
		</div>
	</c:if>
	
<%@ include file="parts/advanceOptions.jsp"%>

<div id="time-limit-panel-placeholder"></div>

<%@ include file="parts/dateRestriction.jsp"%>

<div id="change-leader-modals"></div>
