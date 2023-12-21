<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>
<c:set var="taskList" value="${sessionMap.taskList}"/>
<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<script type="text/javascript">
	$(document).ready(function(){
		<c:forEach var="sessionDto" items="${sessionDtos}">
		
			jQuery("#group${sessionDto.sessionId}").jqGrid({
				datatype: "json",
				url: "<c:url value='/monitoring/getPagedUsers.do'/>?sessionMapID=${sessionMapID}&toolSessionID=${sessionDto.sessionId}",
				autoencode:false,
				height: '100%',
				autowidth: true,
				shrinkToFit: false,
				pager: 'pager-${sessionDto.sessionId}',
				rowList:[10,20,30,40,50,100],
				rowNum:10,
				viewrecords:true,
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
			   	colNames:[
						'userUid'
						,"<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.summary.user' /></spring:escapeBody>"
						<c:forEach var="item" items="${sessionDto.taskListItems}">
							,'<a href="javascript:;" onclick="return summaryTask(${item.uid})"><c:out value="${item.title}" escapeXml="true"/></a>'
						</c:forEach>
						<c:if test="${sessionMap.monitorVerificationRequired}">
							,'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.confirm.completion" /></spring:escapeBody>'
						</c:if>
						,'portraitId'
				],
			   	colModel:[
			   		{name:'userUid',index:'userUid', width:0, hidden: true}
			   		,{name:'userName',index:'userName', width:200, searchoptions: { clearSearch: false }, formatter:userNameFormatter}
			   		<c:forEach var="item" items="${sessionDto.taskListItems}" varStatus="status">
			   			,{name:'item${status.index}',index:'item${status.index}', width:100, sortable:false, search:false, align:"center"}
			   		</c:forEach>
			   		<c:if test="${sessionMap.monitorVerificationRequired}">
			   			,{name:'monitorVerificationRequired',index:'monitorVerificationRequired', width:130, sortable:false, search:false, align:"center"}
			   		</c:if>
			   		,{name:'portraitId', index:'portraitId', width:0, hidden: true},
			   	],
			   	loadError: function(xhr,st,err) {
			   		jQuery("#group${sessionDto.sessionId}").clearGridData();
			   		info_dialog("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.error'/></spring:escapeBody>", "<spring:escapeBody javaScriptEscape='true'><fmt:message key='gradebook.error.loaderror'/></spring:escapeBody>", "<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.ok'/></spring:escapeBody>");
			   	},
			    loadComplete: function () {
			   	 	initializePortraitPopover('<lams:LAMSURL/>');
			    }
			})
			.jqGrid('filterToolbar', { 
 	 			searchOnEnter: false
 	 		})
 	 		.navGrid("#pager-${sessionDto.sessionId}", {edit:false,add:false,del:false,search:false});
	        			
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
	});

	function userNameFormatter (cellvalue, options, rowObject) {
		return definePortraitPopover(rowObject[rowObject.length-1], rowObject[0],  rowObject[1]);
	}

	function summaryTask(itemUid){
		var myUrl = "<c:url value="/monitoring/itemSummary.do"/>?sessionMapID=${sessionMapID}&toolContentID=${toolContentID}&itemUid=" + itemUid;
		launchPopup(myUrl,"LearnerView");
	}
	
	function setVerifiedByMonitor(link, userUid) {
		$.ajax({
			type: "POST",
			url: '<c:url value="/monitoring/setVerifiedByMonitor.do"/>?<csrf:token/>',
			data: { userUid: userUid },
			success: function(response) {
				$("#verif-"+response).html('<i class="fa fa-check"></i>');
			}
		});
		
		return false;
	}

</script>
<div class="panel">
	<h4>
	    <c:out value="${taskList.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${taskList.instructions}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty sessionDtos}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			 <fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:if>
</div>

<%-- Summary list  --%>
<c:if test="${sessionMap.isGroupedActivity}">
<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
			
	<c:if test="${sessionMap.isGroupedActivity}">	
	    <div class="panel panel-default" >
	       <div class="panel-heading" id="heading${sessionDto.sessionId}">
	       	<span class="panel-title collapsable-icon-left">
	       	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${sessionDto.sessionId}" 
					aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${sessionDto.sessionId}" >
			<fmt:message key="monitoring.label.group" />&nbsp;${sessionDto.sessionName}</a>
			</span>
	       </div>
	       
	       <div id="collapse${sessionDto.sessionId}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${sessionDto.sessionId}">
	</c:if>
			
	<table id="group${sessionDto.sessionId}"></table>
	<div id="pager-${sessionDto.sessionId}"></div>

	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
	
</c:forEach>

<c:if test="${sessionMap.isGroupedActivity}">
</div> <!--  end accordianSessions --> 
</c:if>
				
<br/>
<c:if test="${taskList.reflectOnActivity && not empty sessionMap.reflectList}">
	<%@ include file="parts/reflections.jsp"%>
</c:if>

<%@include file="parts/advanceoptions.jsp"%>
<%@include file="parts/daterestriction.jsp"%>
				
