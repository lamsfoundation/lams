<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<lams:html>
	<lams:head>
		<title><fmt:message key="label.monitoring.heading" /></title>
		<%@ include file="/common/header.jsp"%>

		<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/free.ui.jqgrid.min.css" rel="stylesheet">
		<link type="text/css" href="<lams:WebAppURL/>includes/css/learning.css" rel="stylesheet" />
		<style type="text/css">
			.ui-jqgrid-labels {
				display:none; !important;
			}
			.ui-search-toolbar th:nth-child(2){
				display:none !important;
			}
		</style>

		<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){

				//init all available jqGrids
				$("[id^=group]").each(function() {
					var groupTable = $(this),
							toolSessionId = groupTable.data("session-id");


					groupTable.jqGrid({
						url: "<c:url value='/monitoring/getManageUsers.do'/>?toolSessionId=" + toolSessionId,
						datatype: "json",
						height: 'auto',
						autowidth: true,
						shrinkToFit: false,
						guiStyle: "bootstrap",
						iconSet: 'fontAwesome',
						colNames:[
							'userUid',
							'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.user.hidden" /></spring:escapeBody>',
							'<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.user.name" /></spring:escapeBody>'
						],
						colModel:[
							{name:'userUid', index:'userUid', width:0, hidden: true},
							{name: 'hidden', index: 'hidden', width:10, editable:true, edittype:'checkbox', editoptions: {value:"True:False"},
								formatter: "checkbox", formatoptions: {disabled : false}, sortable:false, search: false},
							{name:'userName', index:'userName', width:300, searchoptions: { clearSearch: false }}
						],
						sortname: "userUid",
						rowNum:10,
						rowList:[10,20,30,40,50,100],
						pager: '#pager' + toolSessionId,
						viewrecords:true,
						// caption: "${groupSummary.sessionName}" use Bootstrap panels as the title bar
						loadComplete: function() {

							// storing isHidden change to DB
							$(":checkbox", groupTable).on("click", function(event){

								//if teacher doesn't confirm - prevent checkbox from being unchecked and do not save to DB
								if (!$(this).is(":checked") && !confirm('<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.hide.user.confirmation" /></spring:escapeBody>')) {
									event.preventDefault();
									event.stopPropagation();
									return false;
								}

								//store to DB in case checkbox wasn't checked previously or the teacher has confirmed hiding the user
								var userUid = $(this).parent().parent().prop('id');
								$.ajax({
									data: {
										toolContentID: ${sessionMap.toolContentID},
										userUid: userUid,
										hidden: this.checked
									},
									type: 'POST',
									url: '<c:url value="/monitoring/setUserHidden.do"/>'
								});

							});
						}
					}).jqGrid('filterToolbar', {
						searchOnEnter: false
					})
							.navGrid('#pager' + toolSessionId, {add:false,del:false,edit:false,search:false});
				});

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

	<c:set var="title"><fmt:message key="label.manage.users" /></c:set>
	<lams:Page type="monitor" title="${title}">

		<h5>
			<fmt:message key="label.manage.users.description" />
		</h5>

		<c:if test="${sessionMap.isGroupedActivity}">
			<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true">
		</c:if>

		<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">

			<c:if test="${sessionMap.isGroupedActivity}">
				<div class="panel panel-default" >
				<div class="panel-heading" id="heading${groupSummary.sessionId}">
			        	<span class="panel-title collapsable-icon-left">
				        	<a role="button" data-toggle="collapse" href="#collapse${groupSummary.sessionId}"
							   aria-expanded="true" aria-controls="collapse${groupSummary.sessionId}">
								<fmt:message key="monitoring.label.group" />: ${groupSummary.sessionName}
							</a>
						</span>
				</div>

				<div id="collapse${groupSummary.sessionId}" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="heading${groupSummary.sessionId}">
			</c:if>

			<table id="group${groupSummary.sessionId}" class="scroll" data-session-id="${groupSummary.sessionId}" cellpadding="0" cellspacing="0"></table>
			<div id="pager${groupSummary.sessionId}"></div>

			<c:if test="${sessionMap.isGroupedActivity}">
				</div> <!-- end collapse area  -->
				</div> <!-- end collapse panel  -->
			</c:if>
			${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}

		</c:forEach>

		<c:if test="${sessionMap.isGroupedActivity}">
			</div> <!--  end panel group -->
		</c:if>

		<span onclick="window.close()" class="btn btn-default voffset5 pull-right">
 			<fmt:message key="label.close"/>
 		</span>
	</lams:Page>

	<div id="footer"></div>

	</body>
</lams:html>