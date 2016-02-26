<!DOCTYPE html> 
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="item" value="${taskListItem}" />
<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
		
	<link type="text/css" href="${lams}css/jquery-ui-redmond-theme.css" rel="stylesheet">
	<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">
	<link type="text/css" href="${lams}css/jquery.jqGrid.css" rel="stylesheet"/>
	<style media="screen,projection" type="text/css">
		.ui-jqgrid tr.jqgrow td {
		    white-space: normal !important;
		    height:auto;
		    vertical-align:text-top;
		    padding-top:2px;
		}
	</style>
		
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			<c:forEach var="sessionDto" items="${sessionDtos}">
				
				jQuery("#group${sessionDto.sessionId}").jqGrid({
					datatype: "json",
					url: "<c:url value='/monitoring/getPagedUsersByItem.do'/>?sessionMapID=${sessionMapID}&toolSessionID=${sessionDto.sessionId}&itemUid=${item.uid}",
					height: 'auto',
					autowidth: true,
					shrinkToFit: true,
					width: 'auto',
					pager: 'pager-${sessionDto.sessionId}',
					rowList:[10,20,30,40,50,100],
					rowNum:10,
					viewrecords:true,
				   	caption: "${sessionDto.sessionName}",
				   	colNames:[
							"<fmt:message key="label.monitoring.tasksummary.user" />",
							"<fmt:message key="label.monitoring.tasksummary.completed" />",
							"<fmt:message key="label.monitoring.tasksummary.time.and.date" />"
							<c:if test="${item.commentsAllowed || item.filesAllowed}">
								,'<fmt:message key="label.monitoring.tasksummary.comments.files" />'
							</c:if>
					],
				   	colModel:[
				   		{name:'userName', index:'userName', width:'20', searchoptions: { clearSearch: false }},
				   		{name:'completed', index:'completed', width:'15', sorttype:"int", search:false, align:"center"},
				   		{name:'accessDate', index:'accessDate', width:'20', search:false, align:"center"}
				   		<c:if test="${item.commentsAllowed || item.filesAllowed}">
				   			,{name:'commentsFiles',index:'commentsFiles', width:'45', sortable:false, search:false, align:"center"}
				   		</c:if>
				   	],
				   	loadError: function(xhr,st,err) {
				   		jQuery("#group${sessionDto.sessionId}").clearGridData();
				   		info_dialog("<fmt:message key="label.error"/>", "<fmt:message key="gradebook.error.loaderror"/>", "<fmt:message key="label.ok"/>");
				   	}
				})
				.jqGrid('filterToolbar', { 
	 	 			searchOnEnter: false
	 	 		})
	 	 		.navGrid("#pager-${sessionDto.sessionId}", {edit:false,add:false,del:false,search:false});
			</c:forEach>
		
			//jqgrid autowidth
			$(window).bind('resize', function() {
				var grid;
			    if (grid = jQuery(".ui-jqgrid-btable:visible")) {
			    	grid.each(function(index) {
				        var gridId = $(this).attr('id');
				        
					    var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
					    jQuery('#' + gridId).setGridWidth(gridParentWidth, false);
			        });
			    }
			});
		});
		
	</script>
</lams:head>
	
<body class="stripes">
	<div id="content">

		<h2>
			<fmt:message key="label.monitoring.tasksummary.task.summary" />
		</h2>
				
		<div>
			<c:out value="${item.title}" escapeXml="true"/>
				
			<c:if test="${item.required}">
					(<fmt:message key="label.monitoring.tasksummary.task.required.to.finish" />)
			</c:if>				
		</div>
		<br/>
				
		<c:if test="${item.childTask || item.commentsFilesAllowed || item.commentsAllowed || item.filesAllowed}">
			<ul>
				<c:if test="${item.childTask}">
					<li>
						(<fmt:message key="label.monitoring.tasksummary.parent.activity" />: ${item.parentTaskName})
					</li>
					</c:if>
<!-- 						
				<c:if test="${item.commentsFilesAllowed}">
					<li>
						<fmt:message key="label.monitoring.tasksummary.comments.files.enabled" />
					</li>
				</c:if>
-->					
				<c:if test="${item.commentsAllowed}">
					<li>
						<fmt:message key="label.monitoring.tasksummary.comments.allowed" />
					</li>
				</c:if>
					
				<c:if test="${item.commentsRequired}">
				    <ul>
						<li>
							<fmt:message key="label.monitoring.tasksummary.comments.required" />
						</li>
					</ul>
				</c:if>
					
				<c:if test="${item.filesAllowed}">
					<li>
						<fmt:message key="label.monitoring.tasksummary.files.allowed" />
					</li>
				</c:if>

				<c:if test="${item.filesRequired}">
					<ul>
						<li>
							<fmt:message key="label.monitoring.tasksummary.files.required" />
						</li>
					</ul>
				</c:if>
			</ul>
		</c:if>
		<br>
		<br>
			
		<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">
		
			<div style="padding-left: 30px; <c:if test='${! status.last}'>padding-bottom: 30px;</c:if><c:if test='${ status.last}'>padding-bottom: 15px;</c:if> ">
				<c:if test="${sessionMap.isGroupedActivity}">
					<div style="padding-bottom: 5px; font-size: small;">
						<B><fmt:message key="monitoring.label.group" /></B> ${sessionDto.sessionName}
					</div>
				</c:if>
				<c:if test="${sessionMap.isGroupedActivity}">
					<h1>
						<fmt:message key="monitoring.label.group" /> ${groupSummary.sessionName}
					</h1>
				</c:if>
					
				<table id="group${sessionDto.sessionId}"></table>
				<div id="pager-${sessionDto.sessionId}"></div>
			</div>
							
		</c:forEach>

	</div>
	<!--closes content-->
	
	<div id="footer">
	</div>
	<!--closes footer-->
	
</body>
</lams:html>
