<!DOCTYPE html> 
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="item" value="${taskListItem}" />
<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/monitorheader.jsp"%>
		
	<script type="text/javascript">
		$(document).ready(function(){
			<c:forEach var="sessionDto" items="${sessionDtos}">
				
				jQuery("#group${sessionDto.sessionId}").jqGrid({
					datatype: "json",
					url: "<c:url value='/monitoring/getPagedUsersByItem.do'/>?sessionMapID=${sessionMapID}&toolSessionID=${sessionDto.sessionId}&itemUid=${item.uid}",
					autoencode:false,
					height: 'auto',
					autowidth: true,
					shrinkToFit: true,
					width: 'auto',
					pager: 'pager-${sessionDto.sessionId}',
					rowList:[10,20,30,40,50,100],
					rowNum:10,
					viewrecords:true,
				   	guiStyle: "bootstrap",
					iconSet: 'fontAwesome',
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
		
	</script>
</lams:head>
	
<body class="stripes">

	<c:set var="title"><c:out value="${item.title}" escapeXml="true"/></c:set>
	<lams:Page type="admin" title="${title}">
				
		<c:if test="${item.required}">
		<div><em><fmt:message key="label.monitoring.tasksummary.task.required.to.finish" /></em></div>
		</c:if>				

		<c:if test="${item.childTask || item.commentsAllowed || item.filesAllowed}">
			<ul class="list-unstyled">
				<c:if test="${item.childTask}">
					<li>
						(<fmt:message key="label.monitoring.tasksummary.parent.activity" />: ${item.parentTaskName})
					</li>
				</c:if>

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

	</lams:Page>
	<!--closes content-->
	
	<div id="footer">
	</div>
	<!--closes footer-->
	
</body>
</lams:html>
