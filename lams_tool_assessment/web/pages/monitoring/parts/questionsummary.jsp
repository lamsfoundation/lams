<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionDto" value="${questionSummary.questionDto}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="assessment" value="${sessionMap.assessment}"/>
<c:set var="sessionDtos" value="${sessionMap.sessionDtos}"/>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css suffix="jquery.jRating"/>

		<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/free.ui.jqgrid.min.css" rel="stylesheet">
		<link type="text/css" href="${lams}css/jquery.jqGrid.confidence-level-formattter.css" rel="stylesheet">
		<c:if test="${not empty questionDto.codeStyle}">
			<link rel="stylesheet" type="text/css" href="${lams}css/codemirror.css" />
		</c:if>
		<style>
			pre {
				background-color: initial;
				border: none;
			}

			#bottom-buttons {
				text-align: right;
				margin: 10px 10px 10px 0;
			}

			.requires-grading {
				background-color: rgba(255, 195, 55, .6);
			}
		</style>

		<script>
			// pass settings to jquery.jqGrid.confidence-level-formattter.js
			var confidenceLevelsSettings = {
				type: "${assessment.confidenceLevelsType}",
				LABEL_NOT_CONFIDENT : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.not.confident" /></spring:escapeBody>',
				LABEL_CONFIDENT : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.confident" /></spring:escapeBody>',
				LABEL_VERY_CONFIDENT : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.very.confident" /></spring:escapeBody>',
				LABEL_NOT_SURE : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.not.sure" /></spring:escapeBody>',
				LABEL_SURE : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.sure" /></spring:escapeBody>',
				LABEL_VERY_SURE : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.very.sure" /></spring:escapeBody>'
			};

			//var for jquery.jRating.js
			var pathToImageFolder = "${lams}images/css/";

			//vars for rating.js
			var MAX_RATES = 0,
					MIN_RATES = 0,
					COMMENTS_MIN_WORDS_LIMIT = 0,
					LAMS_URL = '',
					COUNT_RATED_ITEMS = 0,
					COMMENT_TEXTAREA_TIP_LABEL = '',
					WARN_COMMENTS_IS_BLANK_LABEL = '',
					WARN_MIN_NUMBER_WORDS_LABEL = '';
		</script>
		<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.confidence-level-formattter.js"></script>
		<lams:JSImport src="includes/javascript/portrait.js" />
		<script type="text/javascript" src="${lams}includes/javascript/Sortable.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
		<lams:JSImport src="includes/javascript/rating.js" />

		<c:if test="${not empty questionDto.codeStyle}">
			<script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/runmode/runmode-standalone.js"></script>
			<script type="text/javascript" src="${lams}includes/javascript/codemirror/addon/runmode/colorize.js"></script>
			<c:choose>
				<c:when test="${questionDto.codeStyle == 1}">
					<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/python.js"></script>
				</c:when>
				<c:when test="${questionDto.codeStyle == 2}">
					<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/javascript.js"></script>
				</c:when>
				<c:when test="${questionDto.codeStyle >= 3}">
					<script type="text/javascript" src="${lams}includes/javascript/codemirror/mode/clike.js"></script>
				</c:when>
			</c:choose>
		</c:if>

		<script>
			var isEdited = false;
			var previousCellValue = "";
			$(document).ready(function(){
				<c:forEach var="sessionDto" items="${sessionDtos}" varStatus="status">

				jQuery("#session${sessionDto.sessionId}").jqGrid({
					datatype: "json",
					url: "<c:url value="/monitoring/getUsersByQuestion.do"/>?sessionMapID=${sessionMapID}&sessionId=${sessionDto.sessionId}&questionUid=${questionDto.uid}",
					autoencode:false,
					height: 'auto',
					autowidth: true,
					shrinkToFit: false,
					pager: 'pager${sessionDto.sessionId}',
					rowList:[10,20,30,40,50,100],
					rowNum:10,
					viewrecords:true,
					guiStyle: "bootstrap",
					iconSet: 'fontAwesome',
					colNames:[
						'questionResultUid',
						'maxMark',
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.monitoring.summary.user.name" /></spring:escapeBody>",
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.monitoring.user.summary.response" /></spring:escapeBody>",
						<c:if test="${assessment.enableConfidenceLevels and questionDto.type != 8}">
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.confidence" /></spring:escapeBody>",
						</c:if>
						<c:if test="${questionDto.groupsAnswersDisclosed}">
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.answer.rating.title" /></spring:escapeBody>",
						</c:if>
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.monitoring.user.summary.grade" /></spring:escapeBody>",
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.monitoring.user.summary.marker" /></spring:escapeBody>",
						"<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.monitoring.user.summary.marker.comment" /></spring:escapeBody>",
						'userId',
						'portraitId'
					],

					colModel:[
						{name:'questionResultUid', index:'questionResultUid', width:0, hidden: true},
						{name:'maxMark', index:'maxMark', width:0, hidden: true},
						{name:'userName',index:'userName', width:83, searchoptions: { clearSearch: false }, formatter : function(cellvalue, options, rowObject) {
								return definePortraitPopover(rowObject[rowObject.length - 1], rowObject[rowObject.length - 2], rowObject[2]);
							}},
						{name:'response', index:'response', width:400, sortable:false, search:false, formatter: responseFormatter},
						<c:if test="${sessionMap.assessment.enableConfidenceLevels and questionDto.type != 8}">
						{name:'confidence', index:'confidence', width: 80, search:false, classes: 'vertical-align', formatter: gradientNumberFormatter},
						</c:if>
						<c:if test="${questionDto.groupsAnswersDisclosed}">
						{name:'rating', index:'rating', width:120, align:"center", sortable:false, search:false},
						</c:if>
						{name:'grade', index:'grade', width:30, sorttype:"float", search:false, editable:true,
							editoptions: {size:4, maxlength: 4}, align:"right", classes: 'vertical-align', title : false},
						{name:'marker', index:'marker', width: 110, search:false, title: false},
						{name:'markerComment', index:'markerComment', width:300, editable:true, edittype: 'textarea',
							sortable: false, editoptions: {maxlength: 3000, rows: 6}, title : false,
							formatter:function(cellvalue, options, rowObject, event) {
								if (event == "edit") {
									cellvalue = cellvalue.replace(/\n/g, '\n<br>');
								}
								return cellvalue;
							},
							unformat:function(cellvalue, options, rowObject) {
								return rowObject.innerText;
							}
						},
						{name:'userId', index:'userId', width:0, hidden: true},
						{name:'portraitId', index:'portraitId', width:0, hidden: true}
					],
					multiselect: false,
					caption: "${sessionDto.sessionName}",
					cellurl: '<c:url value="/monitoring/saveUserGrade.do?sessionMapID=${sessionMapID}"/>&<csrf:token/>',
					cellEdit: true,
					formatCell: function(rowid, name, value, iRow, iCol){
						if (name != "grade") {
							return value;
						}
						if (value == "-") {
							value = "";
						}
						return value;
					},
					beforeEditCell: function (rowid,name,val,iRow,iCol){
						if (name != "grade") {
							return;
						}
						previousCellValue = val;
					},
					beforeSaveCell : function(rowid, name, val, iRow, iCol) {
						if (name != "grade") {
							return val;
						}
						if (isNaN(val)) {
							return null;
						}

						var maxMark = jQuery("#session${sessionDto.sessionId}").getCell(rowid, 'maxMark');

						if (+val > +maxMark) {
							return maxMark;
						}
					},
					afterSaveCell : function (rowid,name,val,iRow,iCol){
						if (name != "grade") {
							return;
						}
						var questionResultUid = jQuery("#session${sessionDto.sessionId}").getCell(rowid, 'questionResultUid');
						if (isNaN(val) || (questionResultUid=="")) {
							jQuery("#session${sessionDto.sessionId}").restoreCell(iRow,iCol);
						} else {
							isEdited = true;
						}
					},
					beforeSubmitCell : function (rowid,name,val,iRow,iCol){
						if (name == "grade" && isNaN(val)) {
							return {nan:true};
						} else {
							var questionResultUid = jQuery("#session${sessionDto.sessionId}").getCell(rowid, 'questionResultUid');
							return {
								questionResultUid:questionResultUid,
								column:name
							};
						}
					},
					afterSubmitCell : function (serverresponse, rowid, name, value, iRow, iCol) {
						if (serverresponse.statusText == "OK") {
							if (serverresponse.responseText != "") {
								$(this).setCell(rowid, 'marker', serverresponse.responseText, {}, {});
							}
							return [true, ""];
						}
					},
					loadComplete: function () {
						initializeJRating();
						initializePortraitPopover('<lams:LAMSURL/>');

						if (typeof CodeMirror != 'undefined') {
							CodeMirror.colorize($('.code-style'));
						}
					},
					subGrid: true,
					subGridOptions: {
						reloadOnExpand : false,
						hasSubgrid: function (options) {
							// if there are no ratings for the given answer, there will be no subgrid
							return options.data.rating != '-';
						}
					},
					subGridRowExpanded: function(subgrid_id, row_id) {
						var subgridTableId = subgrid_id+"_t";
						var questionResultUid = jQuery("#session${sessionDto.sessionId}")
								.getRowData(row_id)["questionResultUid"];

						jQuery("#"+subgrid_id).html("<table id='" + subgridTableId + "' class='scroll'></table>");

						jQuery("#"+subgridTableId).jqGrid({
							datatype: "json",
							loadonce:true,
							rowNum: 100,
							url: "<c:url value='/monitoring/getAnswerRatings.do'/>?questionResultUid=" + questionResultUid,
							height: "100%",
							autowidth:true,
							autoencode:false,
							cmTemplate: { title: false, search: false, sortable: false},
							guiStyle: "bootstrap",
							iconSet: 'fontAwesome',
							colNames:[
								'ratingId',
								'<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.comment.name" /></spring:escapeBody>',
								'<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.rating" /></spring:escapeBody>',
								'<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.comment" /></spring:escapeBody>',
								'userId',
								'portraitId'
							],
							colModel:[
								{name:'ratingId', index:'ratingId', width:0, hidden:true},
								{name:'userName',index:'userName', width: 35, formatter : function(cellvalue, options, rowObject) {
										var columnParts = rowObject[1].split('<BR>'),
												userName = columnParts[0];
										// get portrait UUID, user ID and user name
										userName = definePortraitPopover(rowObject[rowObject.length - 1], rowObject[rowObject.length - 2], userName);
										columnParts[0] = userName;
										columnParts[1] = '<b>' + columnParts[1] + '</b>';
										return columnParts.join('<BR>');
									}},
								{name:'rating', index:'rating', width: 10,  align:"center"},
								{name:'comment', index:'comment'},
								{name:'userId', index:'userId', width:0, hidden: true},
								{name:'portraitId', index:'portraitId', width:0, hidden: true}
							],
							loadComplete: function () {
								initializePortraitPopover('<lams:LAMSURL/>');
							},
							loadError: function(xhr,st,err) {
								jQuery("#"+subgridTableId).clearGridData();
							}
						});
					}
				})
				<c:if test="${!sessionMap.assessment.useSelectLeaderToolOuput}">
						.jqGrid('filterToolbar', {
							searchOnEnter: false
						})
						</c:if>
						.navGrid("#pager${sessionDto.sessionId}", {edit:false,add:false,del:false,search:false, refresh:true});

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
				setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);
			});

			function refreshSummaryPage()  {
				if (isEdited) {
					self.parent.window.parent.location.href = "<c:url value="/monitoring/summary.do"/>?toolContentID=${sessionMap.toolContentID}&contentFolderID=${sessionMap.contentFolderID}";
				} else {
					self.parent.tb_remove();
				}
			}

			function refreshQuestionSummaryPage()  {
				location.reload();
			}

			function responseFormatter (cellvalue) {
				var codeStyle = ${empty questionDto.codeStyle ? 'null' : questionDto.codeStyle};
				return codeStyle ? "<pre class='code-style' data-lang='${questionDto.codeStyleMime}'>" + cellvalue + "</pre>" : cellvalue;
			}
		</script>
	</lams:head>

	<body class="stripes">

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="label.monitoring.question.summary.question" />: <c:out value="${questionDto.title}" escapeXml="true"/>
			</div>
		</div>

		<div class="panel-body">
			<lams:errors/>

			<c:out value="${questionDto.question}" escapeXml="false"/>

			<div class="row"><div class="col-xs-12 col-sm-6">
				<h5><fmt:message key="label.question.options"/></h5>
				<table class="table table-condensed table-striped">
					<c:if test="${questionDto.type == 1}">
						<tr>
							<td>
								<fmt:message key="label.incorrect.answer.nullifies.mark" />:
							</td>
							<td style="text-align: right;">
								<c:out value="${questionDto.incorrectAnswerNullifiesMark}" escapeXml="false"/>
							</td>
						</tr>
					</c:if>
					<tr>
						<td>
							<fmt:message key="label.monitoring.question.summary.default.mark" />:
						</td>
						<td style="text-align: right;">
							<c:out value="${questionDto.maxMark}" escapeXml="true"/>
						</td>
					</tr>
					<tr>
						<td>
							<fmt:message key="label.monitoring.question.summary.penalty" />:
						</td>
						<td style="text-align: right;">
							<c:out value="${questionDto.penaltyFactor}" escapeXml="true"/>
						</td>
					</tr>
				</table>
			</div></div>

			<!--history responses-->
			<c:if test="${not empty sessionDtos}">
				<h5><fmt:message key="label.monitoring.question.summary.history.responses" /></h5>
				<c:forEach var="sessionDto" items="${sessionDtos}">
					<div class="voffset20">
						<table id="session${sessionDto.sessionId}"></table>
						<div id="pager${sessionDto.sessionId}"></div>
					</div>
				</c:forEach>
			</c:if>

			<div id="bottom-buttons">
				<a href="#nogo" onclick="javascript:refreshQuestionSummaryPage()" class="btn btn-default btn-sm">
					<i class="fa fa-refresh"></i>
					<fmt:message key="label.refresh" />
				</a>

				<a href="#nogo" onclick="javascript:refreshSummaryPage()" class="btn btn-default btn-sm loffset10">
					<i class="fa fa-close"></i>
					<fmt:message key="label.close" />
				</a>
			</div>
		</div>
		<!--closes content-->

		<div id="footer">
		</div>
		<!--closes footer-->
	</div>
	</body>
</lams:html>