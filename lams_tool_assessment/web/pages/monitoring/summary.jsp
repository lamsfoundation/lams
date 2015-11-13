<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>
<c:set var="assessment" value="${sessionMap.assessment}"/>

<script type="text/javascript">
	$(document).ready(function(){
		<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
		
			jQuery("#list${sessionDto.sessionId}").jqGrid({
			   	caption: "${sessionDto.sessionName}",
			   	multiselect: false,
				datatype: "json",
				url: "<c:url value="/monitoring/getUsers.do"/>?sessionMapID=${sessionMapID}&sessionId=${sessionDto.sessionId}",
				height: '100%',
				autowidth: true,
				shrinkToFit: false,
			    pager: 'listPager${sessionDto.sessionId}',
			    rowList:[10,20,30,40,50,100],
			    rowNum:10,
			    viewrecords:true,
			   	colNames:['#',
						'userId',
						'sessionId',
						"<fmt:message key="label.monitoring.summary.user.name" />",
					    "<fmt:message key="label.monitoring.summary.total" />"],
					    
			   	colModel:[
			   		{name:'id', index:'id', width:20, sorttype:"int", search:false},
			   		{name:'userId', index:'userId', width:0, hidden: true},
			   		{name:'sessionId', index:'sessionId', width:0, hidden: true},
			   		{name:'userName', index:'userName', width:570, searchoptions: { clearSearch: false }},
			   		{name:'total', index:'total', width:174, align:"right", formatter:'number', search:false}		
			   	],
			   	ondblClickRow: function(rowid) {
			   		var userId = jQuery("#list${sessionDto.sessionId}").getCell(rowid, 'userId');
			   		var sessionId = jQuery("#list${sessionDto.sessionId}").getCell(rowid, 'sessionId');
					var userSummaryUrl = '<c:url value="/monitoring/userSummary.do?sessionMapID=${sessionMapID}"/>';
					var newUserSummaryHref = userSummaryUrl + "&userID=" + userId + "&sessionId=" + sessionId + "&KeepThis=true&TB_iframe=true&height=540&width=750&modal=true";
					$("#userSummaryHref").attr("href", newUserSummaryHref);	
					$("#userSummaryHref").click(); 		
			  	},
			  	onSelectRow: function(rowid) { 
			  	    if(rowid == null) { 
			  	    	rowid=0; 
			  	    } 
			   		var userId = jQuery("#list${sessionDto.sessionId}").getCell(rowid, 'userId');
			   		var sessionId = jQuery("#list${sessionDto.sessionId}").getCell(rowid, 'sessionId');
					var userMasterDetailUrl = '<c:url value="/monitoring/userMasterDetail.do"/>';
		  	        jQuery("#userSummary${sessionDto.sessionId}").clearGridData().setGridParam({gridstate: "visible"}).trigger("reloadGrid");
		  	        $("#masterDetailArea").load(
		  	        	userMasterDetailUrl,
		  	        	{
		  	        		userID: userId,
		  	        		sessionId: sessionId
		  	       		}
		  	       	);    
	  	  		},
			    loadError: function(xhr,st,err) {
			    	jQuery("#list${sessionDto.sessionId}").clearGridData();
			    	$.jgrid.info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
			    }

			})
			<c:if test="${!sessionMap.assessment.useSelectLeaderToolOuput}">
				.jqGrid('filterToolbar', { 
					searchOnEnter: false
				})
			</c:if>
			.navGrid("#listPager${sessionDto.sessionId}", {edit:false,add:false,del:false,search:false});
	        
	        var oldValue = 0;
			jQuery("#userSummary${sessionDto.sessionId}").jqGrid({
				datatype: "local",
				rowNum: 10000,
				gridstate:"hidden",
				//hiddengrid:true,
				height: 180,
				autowidth: true,
				shrinkToFit: false,
				caption: "<fmt:message key="label.monitoring.summary.learner.summary" />",
			   	colNames:['#',
						'questionResultUid',
  						'Question',
  						"<fmt:message key="label.monitoring.user.summary.response" />",
  						"<fmt:message key="label.monitoring.user.summary.grade" />"],
					    
			   	colModel:[
	  			   		{name:'id', index:'id', width:20, sorttype:"int"},
	  			   		{name:'questionResultUid', index:'questionResultUid', width:0, hidden: true},
	  			   		{name:'title', index:'title', width: 200},
	  			   		{name:'response', index:'response', width:443, sortable:false},
	  			   		{name:'grade', index:'grade', width:80, sorttype:"float", editable:true, editoptions: {size:4, maxlength: 4}, align:"right" }
			   	],
			   	multiselect: false,

				cellurl: '<c:url value="/monitoring/saveUserGrade.do?sessionMapID=${sessionMapID}"/>',
  				cellEdit: true,
  				afterEditCell: function (rowid,name,val,iRow,iCol){
  					oldValue = eval(val);
				},
				beforeSaveCell : function(rowid, name, val, iRow, iCol) {
					if (isNaN(val)) {
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
  					if (isNaN(val)) {
  						jQuery("#userSummary${sessionDto.sessionId}").restoreCell(iRow,iCol); 
  					} else {
  						var parentSelectedRowId = jQuery("#list${sessionDto.sessionId}").getGridParam("selrow");
  						var previousTotal =  eval(jQuery("#list${sessionDto.sessionId}").getCell(parentSelectedRowId, 'total'));
  						jQuery("#list${sessionDto.sessionId}").setCell(parentSelectedRowId, 'total', previousTotal - oldValue + eval(val), {}, {});
  					}
				},	  		
  				beforeSubmitCell : function (rowid,name,val,iRow,iCol){
  					if (isNaN(val)) {
  						return {nan:true};
  					} else {
  						var questionResultUid = jQuery("#userSummary${sessionDto.sessionId}").getCell(rowid, 'questionResultUid');
  						return {questionResultUid:questionResultUid};		  				  		
  				  	}
  				}
			});
			
		</c:forEach>
		
		//jqgrid autowidth (http://stackoverflow.com/a/1610197)
		$(window).bind('resize', function() {
			jQuery(".ui-jqgrid-btable:visible").each(function(index) {
		    	var gridId = $(this).attr('id');
		    	var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
	        	jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
		    });
		}).trigger('resize');

		$("#questionUid").change(function() {
			var questionUid = $("#questionUid").val();
			if (questionUid != -1) {
				var questionSummaryUrl = '<c:url value="/monitoring/questionSummary.do?sessionMapID=${sessionMapID}"/>';
				var questionSummaryHref = questionSummaryUrl + "&questionUid=" + questionUid + "&KeepThis=true&TB_iframe=true&height=400&width=750&modal=true";
				$("#questionSummaryHref").attr("href", questionSummaryHref);	
				$("#questionSummaryHref").click(); 		 
			}
	    }); 
	});
	
	function exportSummary(){   
		var url = "<c:url value='/monitoring/exportSummary.do'/>";
	    var reqIDVar = new Date();
		var param = "?sessionMapID=${sessionMapID}&reqID="+reqIDVar.getTime();
		url = url + param;
		location.href=url;
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
			
		    var width = top.window.innerWidth;
		    if ( width == undefined || width == 0 ) {
		    	// IE doesn't use window.innerWidth.
		    	width = document.documentElement.clientWidth;
		    }
		    width -= document.getElementById('TB_iframeContent').offsetLeft + 60;
		    document.getElementById('TB_iframeContent').style.width = width +"px";
			TB_WIDTH = width + 1;
			
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

<h1><c:out value="${assessment.title}" escapeXml="true"/></h1>

<div class="instructions space-top">
	<c:out value="${assessment.instructions}" escapeXml="false"/>
</div>


<c:choose>
	<c:when test="${empty sessionDtos}">
		<div align="center">
			<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
		</div>	
	</c:when>
	<c:otherwise>
	
		<div style="padding-left: 20px; margin-bottom: 10px; margin-top: 15px;">
			<H1><fmt:message key="label.monitoring.summary.summary" /></H1>
		</div>	
	
		<div style="padding-left: 30px; font-size: small; margin-bottom: 20px; font-style: italic;">
			<fmt:message key="label.monitoring.summary.double.click" />
		</div>
			
		<div id="masterDetailArea">
			<%@ include file="parts/masterDetailLoadUp.jsp"%>
		</div>
		<a onclick="" href="return false;" class="thickbox" id="userSummaryHref" style="display: none;"></a>	
	
		<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
			<div class="session-container" 
				 	style="<c:if test='${!status.last}'>padding-bottom: 30px;</c:if><c:if test='${status.last}'>padding-bottom: 15px;</c:if> ">
				<c:if test="${sessionMap.isGroupedActivity}">
					<div class="session-header">
						<B><fmt:message key="monitoring.label.group" /></B> ${sessionDto.sessionName}
					</div>
				</c:if>
				
				<table id="list${sessionDto.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
				<table id="userSummary${sessionDto.sessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
				<div id="listPager${sessionDto.sessionId}" class="scroll"></div>
				
			</div>
		</c:forEach>	
		
		<html:link href="javascript:exportSummary();" property="exportExcel" styleClass="button space-right float-right">
			<fmt:message key="label.monitoring.summary.export.summary" />
		</html:link>
		
		<div style="padding-left: 20px; margin-bottom: 15px; margin-top: 30px;">
			<H1><fmt:message key="label.monitoring.summary.report.by.question" /></H1>
		</div>
		
		<!-- Dropdown menu for choosing a question type -->
		
		<div style="padding-left: 30px; margin-top: 10px; margin-bottom: 25px;">	
			<div style="margin-bottom: 6px; font-size: small; font-style: italic;">
				<fmt:message key="label.monitoring.summary.results.question" />
			</div>

			<select id="questionUid" style="float: left">
				<option selected="selected" value="-1"><fmt:message key="label.monitoring.summary.choose" /></option>
    			<c:forEach var="question" items="${sessionMap.questionList}">
					<option value="${question.uid}"><c:out value="${question.title}" escapeXml="true"/></option>
			   	</c:forEach>
			</select>
			
			<a onclick="" href="return false;" class="thickbox" id="questionSummaryHref" style="display: none;"></a>
		</div>
	
	</c:otherwise>
</c:choose>

<br/>

<c:if test="${assessment.reflectOnActivity && not empty sessionMap.reflectList}">
	<%@ include file="parts/reflections.jsp"%>
</c:if>

<%@ include file="parts/advanceoptions.jsp"%>

<%@ include file="parts/dateRestriction.jsp"%>
