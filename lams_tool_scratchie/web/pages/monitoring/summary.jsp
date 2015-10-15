<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="scratchie" value="${sessionMap.scratchie}"/>

<style media="screen,projection" type="text/css">
	#user-dropdown-div {padding-left: 30px; margin-top: -5px; margin-bottom: 50px;}
	.bottom-buttons {margin: 20px 20px 0px; padding-bottom: 20px;}
	.section-header {padding-left: 20px; margin-bottom: 15px; margin-top: 60px;}
	.ui-jqgrid tr.jqgrow td {
	    white-space: normal !important;
	    height:auto;
	    vertical-align:text-top;
	    padding-top:2px;
	}
	.burning-question-dto {
		padding-left: 30px; 
		padding-bottom: 5px; 
		width:96%;
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
			   	ondblClickRow: function(rowid) {
			   		var userId = jQuery("#list${summary.sessionId}").getCell(rowid, 'userId');
			   		var toolSessionId = jQuery("#list${summary.sessionId}").getCell(rowid, 'sessionId');

			   		var userSummaryUrl = "<c:url value='/learning/start.do'/>?userID=" + userId + "&toolSessionID=" + toolSessionId + "&mode=teacher&reqId=" + (new Date()).getTime();
					launchPopup(userSummaryUrl, "MonitoringReview");		
			  	},
			   	colNames:['#',
						'userId',
						'sessionId',
						"<fmt:message key="label.monitoring.summary.user.name" />",
						"<fmt:message key="label.monitoring.summary.attempts" />",
					    "<fmt:message key="label.monitoring.summary.mark" />"
				],
			   	colModel:[
			   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
			   		{name:'userId', index:'userId', width:0, hidden: true},
			   		{name:'sessionId', index:'sessionId', width:0, hidden: true},
			   		{name:'userName', index:'userName', width:570},
			   		{name:'totalAttempts', index:'totalAttempts', width:100, align:"right", sorttype:"int"},
			   		{name:'mark', index:'mark', width:100, align:"right", sorttype:"int", editable:true, editoptions: {size:4, maxlength: 4}}		
			   	],
			   	caption: "${summary.sessionName}",
				cellurl: '<c:url value="/monitoring/saveUserMark.do"/>',
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
   	   				mark:"<c:choose><c:when test='${summary.totalAttempts == 0}'>-</c:when><c:otherwise>${summary.mark}</c:otherwise></c:choose>"
   	   	   	    });
	        </c:forEach>
			
		</c:forEach>
		
		<!-- Display burningQuestionDtos -->
		<c:forEach var="burningQuestionDto" items="${sessionMap.burningQuestionDtos}" varStatus="i">
			jQuery("#burningQuestions${burningQuestionDto.item.uid}").jqGrid({
				datatype: "local",
				rowNum: 10000,
				height: 'auto',
				autowidth: true,
				shrinkToFit: false,
			   	colNames:['#',
						"<fmt:message key='label.monitoring.summary.user.name' />",
					    "<fmt:message key='label.burning.questions' />"
				],
			   	colModel:[
			   		{name:'id', index:'id', width:0, sorttype:"int", hidden: true},
			   		{name:'groupName', index:'groupName', width:200},
			   		{name:'feedback', index:'feedback', width:570}
			   	],
			   	caption: "${burningQuestionDto.item.title}"
			});
		    <c:forEach var="entry" items="${burningQuestionDto.groupNameToBurningQuestion}" varStatus="i">
		    	<c:set var="groupName" value="${entry.key}"/>
		    	<c:set var="burningQuestion" value="${entry.value}"/>
		    
		    	jQuery("#burningQuestions${burningQuestionDto.item.uid}").addRowData(${i.index + 1}, {
		   			id:"${i.index + 1}",
		   	     	groupName:"${groupName}",
			   	    feedback:"<lams:out value='${burningQuestion}' escapeHtml='true' />"
		   	   	});
	        </c:forEach>
	        
	     	jQuery("#burningQuestions${burningQuestionDto.item.uid}").jqGrid('sortGrid','groupName', false, 'asc');
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
		   	caption: "<fmt:message key='label.learners.feedback' />"
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
			var grid;
		    if (grid = jQuery(".ui-jqgrid-btable:visible")) {
		    	grid.each(function(index) {
		        	var gridId = $(this).attr('id');
		        	var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
		        	jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
		    	});
		    }
		});

		$("#item-uid").change(function() {
			var itemUid = $(this).val();
			if (itemUid != -1) {
				var itemSummaryUrl = '<c:url value="/monitoring/itemSummary.do?sessionMapID=${sessionMapID}"/>';
				var itemSummaryHref = itemSummaryUrl + "&itemUid=" + itemUid + "&KeepThis=true&TB_iframe=true&height=400&width=650";
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
		location.href = "<c:url value='/monitoring/exportExcel.do'/>?sessionMapID=${sessionMapID}&reqID=" + (new Date()).getTime();
	};

	function resizeIframe() {
		if (document.getElementById('TB_iframeContent') != null) {
		    var height = top.window.innerHeight;
		    if ( height == undefined || height == 0 ) {
		    	// IE doesn't use window.innerHeight.
		    	height = document.documentElement.clientHeight;
		    }
		    height -= document.getElementById('TB_iframeContent').offsetTop + 60;
		    document.getElementById('TB_iframeContent').style.height = height +"px";
	
			TB_HEIGHT = height + 28;
			tb_position();
		}
	};
	window.onresize = resizeIframe;
	
	// pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '<lams:LAMSURL />',
		submissionDeadline: '${submissionDeadline}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>',
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};

</script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<h1><c:out value="${scratchie.title}" escapeXml="true"/></h1>

<div class"instructions space-top space-bottom">
	<c:out value="${scratchie.instructions }" escapeXml="false" />
</div>

<c:choose>
	<c:when test="${empty summaryList}">
		<div align="center">
			<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
		</div>	
	</c:when>
	
	<c:otherwise>
	
		<div style="padding-left: 20px; margin-bottom: 10px; margin-top: 15px;">
			<H1><fmt:message key="label.monitoring.summary.summary" /></H1>
		</div>	
	
		<div style="padding-left: 30px; font-size: small; margin-bottom: 20px; font-style: italic;">
			<fmt:message key="label.monitoring.summary.select.student" />
		</div>
	
		<c:forEach var="summary" items="${summaryList}" varStatus="status">
			<div style="width:96%; padding-left: 30px; <c:if test='${! status.last}'>padding-bottom: 30px;</c:if><c:if test='${ status.last}'>padding-bottom: 15px;</c:if> ">
				<c:if test="${sessionMap.isGroupedActivity}">
					<div style="padding-bottom: 5px; font-size: small;">
						<B><fmt:message key="monitoring.label.group" /></B> ${summary.sessionName}
					</div>
				</c:if>
				
				<table id="list${summary.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
			</div>
		</c:forEach>
		
		<!-- Dropdown menu for choosing scratchie item -->
		
		<div style="padding-left: 20px; margin-bottom: 15px; margin-top: 30px;">
			<H1><fmt:message key="label.monitoring.summary.report.by.scratchie" /></H1>
		</div>
		
		<div style="padding-left: 30px; margin-top: -5px; margin-bottom: 25px;">
			<select id="item-uid" style="float: left">
				<option selected="selected" value="-1"><fmt:message key="label.monitoring.summary.choose" /></option>
    			<c:forEach var="item" items="${scratchie.scratchieItems}">
					<option value="${item.uid}"><c:out value="${item.title}" escapeXml="true"/></option>
			   	</c:forEach>
			</select>
			
			<a href="#nogo" class="thickbox" id="item-summary-href" style="display: none;"></a>
		</div>
		
		<!-- Dropdown menu for choosing user -->
		
		<div class="section-header">
			<H1><fmt:message key="label.monitoring.summary.report.by.user" /></H1>
		</div>
		
		<div id="user-dropdown-div">

			<select id="userid-dropdown" class="float-left">
				<option selected="selected" value="-1"><fmt:message key="label.monitoring.summary.choose" /></option>
    			<c:forEach var="learner" items="${sessionMap.learners}">
					<option value="${learner.userId}" alt="${learner.session.sessionId}"><c:out value="${learner.firstName} ${learner.lastName} (${learner.session.sessionName})" escapeXml="true"/></option>
			   	</c:forEach>
			</select>
		</div>
		
		<!-- Display burningQuestionDtos -->
		<c:if test="${scratchie.burningQuestionsEnabled}">
		
			<div class="section-header">
				<H1>
					<fmt:message key="label.burning.questions" />
				</H1>
			</div>
			
			<c:forEach var="burningQuestionDto" items="${sessionMap.burningQuestionDtos}" varStatus="i">
				<div class="burning-question-dto">
					<table id="burningQuestions${burningQuestionDto.item.uid}" class="scroll" cellpadding="0" cellspacing="0"></table>
				</div>
			</c:forEach>
			
			<!-- General burning question's table -->
			<div class="burning-question-dto">
				<table id="burningQuestions0" class="scroll" cellpadding="0" cellspacing="0"></table>
			</div>
		</c:if>
		
		<!-- Display reflection entries -->
		<c:if test="${sessionMap.reflectOn}">
		
			<div class="section-header" style="margin-top: 30px;">
				<H1>
					<fmt:message key="label.learners.feedback" />
					
					<select id="reflection-group-selector" class="space-left">
						<option selected="selected" value=""><fmt:message key="label.all" /></option>
		    			<c:forEach var="reflectDTO" items="${sessionMap.reflections}">
							<option value="${reflectDTO.groupName}">${reflectDTO.groupName}</option>
					   	</c:forEach>
					</select>
				</H1>
			</div>
			
			<div style="padding-left: 30px; width:96%;">
				<table id="reflections" class="scroll" cellpadding="0" cellspacing="0"></table>
			</div>
		</c:if>
		
		<div class="bottom-buttons">
			<html:link href="javascript:exportExcel();" styleClass="button float-right space-left">
				<fmt:message key="label.export.excel" />
			</html:link>
		</div>
	
	</c:otherwise>
</c:choose>


<%@ include file="parts/advanceOptions.jsp"%>

<%@ include file="parts/dateRestriction.jsp"%>
