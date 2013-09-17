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
</style>

<script type="text/javascript">
	<!--	
	$(document).ready(function(){
		var oldValue = 0;
		
		<c:forEach var="summary" items="${summaryList}" varStatus="status">
		
			jQuery("#list${summary.sessionId}").jqGrid({
				datatype: "local",
				height: 'auto',
				width: 780,
				shrinkToFit: true,
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
			   		{name:'userName', index:'userName', width:400},
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
   	   				totalAttempts:"${user.totalAttempts}",
   	   				mark:"<c:choose><c:when test='${user.totalAttempts == 0}'>-</c:when><c:otherwise>${user.mark}</c:otherwise></c:choose>"
   	   	   	    });
	        </c:forEach>
			
		</c:forEach>

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
	-->		
</script>

<%@ include file="parts/advanceOptions.jsp"%>

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
			<div style="padding-left: 30px; <c:if test='${! status.last}'>padding-bottom: 30px;</c:if><c:if test='${ status.last}'>padding-bottom: 15px;</c:if> ">
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
					<option value="${item.uid}">${item.title}</option>
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
					<option value="${learner.userId}" alt="${learner.session.sessionId}">${learner.firstName} ${learner.lastName}</option>
			   	</c:forEach>
			</select>
		</div>
		
		<!-- Display reflection entries -->
		
		<c:if test="${sessionMap.reflectOn}">
		
			<div class="section-header">
				<H1><fmt:message key="label.learners.feedback" /></H1>
			</div>
		
			<c:forEach var="reflectDTO" items="${sessionMap.reflections}">
				<div style="padding-left: 30px;">
					<b>
						${reflectDTO.fullName}
					</b>
					
					<c:if test="${reflectDTO.groupLeader}">
						(<fmt:message key="label.monitoring.team.leader" />)
					</c:if>
					
					: <lams:out value="${reflectDTO.reflection}" escapeHtml="true" />
				</div>
			</c:forEach>
			
		</c:if>
		
		<div class="bottom-buttons">
			<html:link href="javascript:exportExcel();" styleClass="button float-right space-left">
				<fmt:message key="label.export.excel" />
			</html:link>
		</div>
	
	</c:otherwise>
</c:choose>
