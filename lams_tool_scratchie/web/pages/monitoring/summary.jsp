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
</style>

<script type="text/javascript">
	$(document).ready(function(){
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
					"<fmt:message key="label.monitoring.summary.user.name" />",
					"<fmt:message key="label.monitoring.summary.attempts" />",
					"<fmt:message key="label.monitoring.summary.mark" />",
					'portraitId'
				],
			   	colModel:[
			   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
			   		{name:'userId', index:'userId', width:0, hidden: true},
			   		{name:'sessionId', index:'sessionId', width:0, hidden: true},
			   		{name:'userName', index:'userName', width:570, formatter:userNameFormatter},
			   		{name:'totalAttempts', index:'totalAttempts', width:100, align:"right", sorttype:"int"},
			   		{name:'mark', index:'mark', width:100, align:"right", sorttype:"int", editable:true, editoptions: {size:4, maxlength: 4}},
			   		{name:'portraitId', index:'portraitId', width:0, hidden: true},
			   	],
			   	ondblClickRow: function(rowid) {
			   		var userId = jQuery("#list${summary.sessionId}").getCell(rowid, 'userId');
			   		var toolSessionId = jQuery("#list${summary.sessionId}").getCell(rowid, 'sessionId');

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
  					var intRegex = /^\d+$/;
  					if (!intRegex.test(val)) {
  						jQuery("#list${summary.sessionId}").restoreCell(iRow,iCol); 
  					}
				},	  		
  				beforeSubmitCell : function (rowid,name,val,iRow,iCol){
  					var intRegex = /^\d+$/;
  					if (!intRegex.test(val)) {
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
   	   	     		sessionId:"${user.session.sessionId}",
   	   	     		userName:"${user.lastName}, ${user.firstName}",
   	   				totalAttempts:"${summary.totalAttempts}",
   	   				mark:"<c:choose><c:when test='${summary.totalAttempts == 0}'>-</c:when><c:otherwise>${summary.mark}</c:otherwise></c:choose>",
   	   				portraitId:"${user.portraitId}"
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
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
			   	colNames:['#',
						"<fmt:message key='label.monitoring.summary.user.name' />",
					    "<fmt:message key='label.burning.questions' />",
						"<fmt:message key='label.count' />"
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
		
		<!-- Display reflection entries -->
		jQuery("#reflections").jqGrid({
			datatype: "local",
			rowNum: 10000,
			height: 'auto',
			autowidth: true,
			shrinkToFit: false,
		   	colNames:['#',
					"<fmt:message key="label.monitoring.summary.user.name" />",
				    "<fmt:message key='label.learners.feedback' />"
			],
		   	colModel:[
		   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
		   		{name:'groupName', index:'groupName', width:200},
		   		{name:'feedback', index:'feedback', width:570}
		   	],
		   	//caption: "<fmt:message key='label.learners.feedback' />"
		});
	    <c:forEach var="reflectDTO" items="${sessionMap.reflections}" varStatus="i">
	    	jQuery("#reflections").addRowData(${i.index + 1}, {
	   			id:"${i.index + 1}",
	   	     	groupName:"${reflectDTO.groupName}",
		   	    feedback:"<lams:out value='${reflectDTO.reflection}' escapeHtml='true' />"
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

        function userNameFormatter (cellvalue, options, rowObject) {
    			return definePortraitPopover(rowObject.portraitId, rowObject.userId,  rowObject.userName);
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
		
		//filter reflections by group name
		$("#reflection-group-selector").change(function() {
            var grid = $("#reflections");
            var searchFiler = $(this).val();

            if (searchFiler.length === 0) {
                grid[0].p.search = false;
                $.extend(grid[0].p.postData,{filters:""});
            }
            var f = {groupOp:"OR",rules:[]};
            f.rules.push({field:"groupName",op:"cn",data:searchFiler});
            grid[0].p.search = true;
            $.extend(grid[0].p.postData,{filters:JSON.stringify(f)});
            grid.trigger("reloadGrid",[{page:1,current:true}]);
	    });
	});
	
	function exportExcel(){
		var url = "<c:url value='/monitoring/exportExcel.do'/>?<csrf:token/>&sessionMapID=${sessionMapID}&reqID=" + (new Date()).getTime();
		return downloadFile(url, 'messageArea_Busy', '<fmt:message key="label.summary.downloaded"/>', 'messageArea', 'btn-disable-on-submit');
	};
	
	// pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '<lams:LAMSURL />',
		submissionDeadline: '${submissionDeadline}',
		submissionDateString: '${submissionDateString}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>?<csrf:token/>',
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
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
	<div class="form-group">
		<!-- Dropdown menu for choosing user -->
		<label for="userid-dropdown"><h4><fmt:message key="label.monitoring.summary.report.by.user" /></h4></label>
		<select id="userid-dropdown" class="form-control">
			<option selected="selected" value="-1"><fmt:message key="label.monitoring.summary.choose" /></option>
   			<c:forEach var="learner" items="${sessionMap.learners}">
				<option value="${learner.userId}" alt="${learner.session.sessionId}"><c:out value="${learner.firstName} ${learner.lastName} (${learner.session.sessionName})" escapeXml="true"/></option>
		   	</c:forEach>
		</select>
	</div>

	<c:set var="summaryTitle"><fmt:message key="label.monitoring.summary.summary" /></c:set>
	<c:if test="${sessionMap.isGroupedActivity}">
		<H4>${summaryTitle}</H4>
	</c:if>
	
	<fmt:message key="label.monitoring.summary.select.student" />

	<c:forEach var="summary" items="${summaryList}" varStatus="status">

		<c:if test="${sessionMap.isGroupedActivity}">
			<c:set var="summaryTitle"><fmt:message key="monitoring.label.group" /></B> ${summary.sessionName}</c:set>
		</c:if>
		
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${summary.sessionId}">
        	<span class="panel-title collapsable-icon-left">
        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${summary.sessionId}" 
					aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${summary.sessionId}" >
				${summaryTitle}</a>
			</span>
        </div>
        
        <div id="collapse${summary.sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${summary.sessionId}">

		<table id="list${summary.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
		
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	
	</c:forEach>

	<!-- Display burningQuestionItemDtos -->
	<c:if test="${scratchie.burningQuestionsEnabled}">
		<div class="panel-group" id="accordionBurning" role="tablist" aria-multiselectable="true"> 
		    <div class="panel panel-default" >
		        <div class="panel-heading collapsable-icon-left" id="headingBurning">
		        	<span class="panel-title">
			    	<a role="button" data-toggle="collapse" href="#collapseBurning" aria-expanded="false" aria-controls="collapseBurning" >
		          	<fmt:message key="label.burning.questions" />
		        	</a>
		      		</span>
		        </div>
		
		        <div id="collapseBurning" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingBurning">
					<c:forEach var="burningQuestionItemDto" items="${sessionMap.burningQuestionItemDtos}" varStatus="i">
						<div class="voffset5"><strong>
							<c:if test="${not i.last}">
								${i.count})&nbsp;
								
							</c:if>
							<c:if test="${not sessionMap.hideTitles}">
								<c:out value="${burningQuestionItemDto.scratchieItem.qbQuestion.name}" />
							</c:if>
						</strong></div>
						<table id="burningQuestions${burningQuestionItemDto.scratchieItem.uid}" class="scroll" cellpadding="0" cellspacing="0"></table>
					</c:forEach>
				</div>
			</div>
		</div>
	</c:if>
	
	<!-- Display reflection entries -->
	<c:if test="${sessionMap.reflectOn}">

		<div class="panel-group" id="accordionReflections" role="tablist" aria-multiselectable="true"> 
		    <div class="panel panel-default" >
		        <div class="panel-heading collapsable-icon-left" id="headingReflections">
		        	<span class="panel-title">
			    	<a role="button" data-toggle="collapse" href="#collapseReflections" aria-expanded="false" aria-controls="collapseReflections" >
		          	<fmt:message key="label.learners.feedback" />
		        	</a>
		        	<select id="reflection-group-selector" class="input-sm pull-right">
						<option selected="selected" value=""><fmt:message key="label.all" /></option>
		    			<c:forEach var="reflectDTO" items="${sessionMap.reflections}">
							<option value="${reflectDTO.groupName}">${reflectDTO.groupName}</option>
					   	</c:forEach>
					</select>
		      		</span>
		        </div>
		
		        <div id="collapseReflections" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingReflections">
					<table id="reflections" class="scroll" cellpadding="0" cellspacing="0"></table>
				</div>
			</div>
		</div>
	</c:if>
	
<%@ include file="parts/advanceOptions.jsp"%>

<%@ include file="parts/dateRestriction.jsp"%>
