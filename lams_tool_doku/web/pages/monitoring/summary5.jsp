<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="dokumaran" value="${sessionMap.dokumaran}" />

<c:set var="timeLimitPanelUrl"><lams:LAMSURL/>monitoring/timeLimit5.jsp</c:set>
<c:url var="timeLimitPanelUrl" value="${timeLimitPanelUrl}">
	<c:param name="toolContentId" value="${dokumaran.contentId}"/>
	<c:param name="absoluteTimeLimitFinish" value="${dokumaran.absoluteTimeLimitFinishSeconds}"/>
	<c:param name="absoluteTimeLimit" value="${dokumaran.absoluteTimeLimit}"/>
	<c:param name="relativeTimeLimit" value="${dokumaran.relativeTimeLimit}"/>
	<c:param name="isTbl" value="true" />
	<c:param name="controllerContext" value="tool/ladoku11/monitoring" />
</c:url>

<%@ page import="org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants"%>

<lams:css suffix="jquery.jRating"/>
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css"/>
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager5.css" />

<style media="screen,projection" type="text/css">

	.doku-monitoring-summary .countdown-timeout {
		color: #FF3333 !important;
	}

	.doku-monitoring-summary #time-limit-table th {
		vertical-align: middle;
	}

	.doku-monitoring-summary #time-limit-table td.centered {
		text-align: center;
	}

	.doku-monitoring-summary .panel {
		overflow: auto;
	}

	.doku-monitoring-summary #gallery-walk-panel {
		width: 30%;
		margin: auto;
		margin-bottom: 20px;
		text-align: center;
	}

	.doku-monitoring-summary #gallery-walk-panel.gallery-walk-panel-ratings {
		width: 100%;
	}

	.doku-monitoring-summary #gallery-walk-show-clusters {
		margin-top: 20px;
	}

	.doku-monitoring-summary #gallery-walk-learner-edit {
		margin-top: 20px;
		margin-bottom: 20px;
	}

	.doku-monitoring-summary #gallery-walk-skip {
		margin-top: 20px;
	}

	.doku-monitoring-summary #gallery-walk-rating-table th {
		font-weight: bold;
		font-style: normal;
		text-align: center;
	}

	.doku-monitoring-summary #gallery-walk-rating-table td {
		text-align: center;
	}

	.doku-monitoring-summary #gallery-walk-rating-table th:first-child, .doku-monitoring-summary #gallery-walk-rating-table td:first-child {
		text-align: right;
	}

	.doku-monitoring-summary .tablesorter tbody > tr > td > div[contenteditable=true]:focus {
		outline: #337ab7 2px solid;
	}

	/* We need to overwrite settings coming from main CSS as in Doku monitoring they look different */

	.doku-monitoring-summary .ts-pager {
		color: black;
	}

	.doku-monitoring-summary .ts-pager .btn {
		background-color: #eee;
	}
	.doku-monitoring-summary .tablesorter tfoot th {
		background-color: #eee !important;
	}

	.doku-monitoring-summary .pagesize {
		border: black;
		background: white;
	}
</style>

<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.plugin.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-editable.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.countdown.js"></script>
<lams:JSImport src="includes/javascript/portrait.js" />
<lams:JSImport src="includes/javascript/etherpad.js" />
<lams:JSImport src="includes/javascript/monitorToolSummaryAdvanced.js" />
<script type="text/javascript">
	//var for jquery.jRating.js
	var pathToImageFolder = "${lams}images/css/",
			//vars for rating.js
			AVG_RATING_LABEL = '<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message></spring:escapeBody>',
			YOUR_RATING_LABEL = '<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message></spring:escapeBody>',
			MAX_RATES = 0,
			MIN_RATES = 0,
			LAMS_URL = '${lams}',
			COUNT_RATED_ITEMS = true,
			ALLOW_RERATE = false;

	$(document).ready(function(){
		// show etherpads only on Group expand
		$('#doku-monitoring-summary-${sessionMap.toolContentID} .etherpad-collapse').on('show.bs.collapse', function(){
			var etherpad = $('.etherpad-container', this);
			if (!etherpad.hasClass('initialised')) {
				var id = etherpad.attr('id'),
						groupId = id.substring('etherpad-container-'.length);
				etherpadInitMethods[groupId]();
			}
		});

		$("#doku-monitoring-summary-${sessionMap.toolContentID} .fix-faulty-pad").click(function() {
			var toolSessionId = $(this).data("session-id");
			var button = $(this);

			//block #buttons
			$(this).block({
				message: '<h4 style="color:#fff";><spring:escapeBody javaScriptEscape='true'><fmt:message key="label.pad.started.fixing" /></spring:escapeBody></h4>',
				baseZ: 1000000,
				fadeIn: 0,
				css: {
					border: 'none',
					padding: "2px 7px",
					backgroundColor: '#000',
					'-webkit-border-radius': '10px',
					'-moz-border-radius': '10px',
					opacity: .98 ,
					left: "0px",
					width: "360px"
				},
				overlayCSS: {
					opacity: 0
				}
			});

			$.ajax({
				async: true,
				url: '<c:url value="/monitoring/fixFaultySession.do"/>',
				data : 'toolSessionID=' + toolSessionId,
				type: 'post',
				success: function (response) {
					button.parent().html('<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.pad.fixed" /></spring:escapeBody>');
					alert('<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.pad.fixed" /></spring:escapeBody>');
				},
				error: function (request, status, error) {
					button.unblock();
					alert(request.responseText);
				}
			});
		});

		// marks table for each group
		var tablesorters = $("#doku-monitoring-summary-${sessionMap.toolContentID} .tablesorter");
		// intialise tablesorter tables
		tablesorters.tablesorter({
			theme: 'bootstrap',
			headerTemplate : '{content} {icon}',
			sortInitialOrder: 'asc',
			sortList: [[0]],
			widgets: [ "uitheme", "resizable", "editable" ],
			headers: { 0: { sorter: true}, 1: { sorter: true}  },
			sortList : [[0,1]],
			showProcessing: false,
			widgetOptions: {
				resizable: true,

				// only marks is editable
				editable_columns       : [1],
				editable_enterToAccept : true,          // press enter to accept content, or click outside if false
				editable_autoAccept    : false,          // accepts any changes made to the table cell automatically
				editable_autoResort    : false,         // auto resort after the content has changed.
				editable_validate      : function (text, original, columnIndex) {
					// removing all text produces "&nbsp;", so get rid of it
					text = text ? text.replace(/&nbsp;/g, '').trim() : null;
					// acceptable values are empty text or a number
					return !text || !isNaN(text) ? text : original;
				},
				editable_selectAll     : function(txt, columnIndex, $element) {
					// note $element is the div inside of the table cell, so use $element.closest('td') to get the cell
					// only select everthing within the element when the content starts with the letter "B"
					return true;
				},
				editable_wrapContent   : '<div>',       // wrap all editable cell content... makes this widget work in IE, and with autocomplete
				editable_trimContent   : true,          // trim content ( removes outer tabs & carriage returns )
				editable_editComplete  : 'editComplete' // event fired after the table content has been edited
			}
		});

		// update mark on edit
		tablesorters.each(function(){
			// config event variable new in v2.17.6
			$(this).children('tbody').on('editComplete', 'td', function(event, config) {
				var $this = $(this),
						mark = $this.text() ? +$this.text() : null,
						toolSessionId = +$this.closest('.tablesorter').attr('toolSessionId'),
						userId = +$this.closest('tr').attr('userId');

				// max mark is 100
				if (mark > 100) {
					mark = 100;
					$this.text(mark);
				}

				$.ajax({
					async: true,
					url: '<c:url value="/monitoring/updateLearnerMark.do"/>',
					data : {
						'toolSessionId' : toolSessionId,
						'userId'		: userId,
						'mark'			: mark,
						'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
					},
					type: 'post',
					error: function (request, status, error) {
						alert('<spring:escapeBody javaScriptEscape='true'><fmt:message key="messsage.monitoring.learner.marks.update.fail" /></spring:escapeBody>');
					}
				});

			});
		});

		// pager processing
		tablesorters.each(function() {
			var toolSessionId = $(this).attr('toolSessionId');

			$(this).tablesorterPager({
				processAjaxOnInit: true,
				initialRows: {
					total: 10
				},
				savePages: false,
				container: $(this).find(".ts-pager"),
				output: '{startRow} to {endRow} ({totalRows})',
				cssPageDisplay: '.pagedisplay',
				cssPageSize: '.pagesize',
				cssDisabled: 'disabled',
				ajaxUrl : "<c:url value='/monitoring/getLearnerMarks.do?{sortList:column}&page={page}&size={size}&toolSessionId='/>" + toolSessionId,
				ajaxProcessing: function (data, table) {
					if (data && data.hasOwnProperty('rows')) {
						var rows = [],
								json = {};


						for (i = 0; i < data.rows.length; i++){
							var userData = data.rows[i],
									isLeader = userData['isLeader'];

							rows += '<tr userId="' + userData['userId'] + '" ' + (isLeader ? 'class="info"' : '') + '>';

							rows += '<td style="width: 80%">';
							rows += 	userData['firstName'] + ' ' +userData['lastName'];
							if (isLeader) {
								rows += '&nbsp;<i title="<fmt:message key="label.monitoring.team.leader"/>" class="text-primary fa fa-star"></i>';
							}
							rows += '</td>';

							rows += '<td>';
							rows += 	userData['mark'];
							rows += '</td>';

							rows += '</tr>';
						}

						json.total = data.total_rows;
						json.rows = $(rows);
						return json;
					}
				}
			})
					.bind('pagerInitialized pagerComplete', function(event, options){
						if ( options.totalRows == 0 ) {
							$.tablesorter.showError($(this), '<fmt:message key="messsage.monitoring.learner.marks.no.data"/>');
						}
					});
		});

		//insert total learners number taken from the parent tblmonitor.jsp
		$("#doku-monitoring-summary-${sessionMap.toolContentID} .total-learners-number").text(TOTAL_LESSON_LEARNERS_NUMBER);

		openEventSource('<lams:WebAppURL />monitoring/getTimeLimitPanelUpdateFlux.do?toolContentId=${dokumaran.contentId}', function(event) {
			if (!event.data) {
				return;
			}

			// destroy existing absolute time limit counter before refresh
			$('.absolute-time-limit-counter').countdown('destroy');
			let data = JSON.parse(event.data);
			$('#time-limit-panel-placeholder').load('<lams:LAMSURL/>monitoring/timeLimit5.jsp?toolContentId=${dokumaran.contentId}&absoluteTimeLimitFinish=' + data.absoluteTimeLimitFinish
					+ '&relativeTimeLimit=' + data.relativeTimeLimit + '&absoluteTimeLimit=' + data.absoluteTimeLimit
					+ '&isTbl=true&controllerContext=tool/ladoku11/monitoring');
		});
	});

	function startGalleryWalk(){
		if (!confirm('<spring:escapeBody javaScriptEscape='true'><fmt:message key="monitoring.summary.gallery.walk.start.confirm" /></spring:escapeBody>')) {
			return;
		}

		$.ajax({
			'url' : '<c:url value="/monitoring/startGalleryWalk.do"/>',
			'data': {
				toolContentID : ${dokumaran.contentId}
			},
			'success' : function(){
				// reload current tab with Doku summary
				loadTab(null, null, false);
			}
		});
	}

	function startGalleryWalk(){
		if (!confirm('<spring:escapeBody javaScriptEscape='true'><fmt:message key="monitoring.summary.gallery.walk.skip.confirm" /></spring:escapeBody>')) {
			return;
		}

		$.ajax({
			'url' : '<c:url value="/monitoring/skipGalleryWalk.do"/>',
			'data': {
				toolContentID : ${dokumaran.contentId}
			},
			'success' : function(){
				// reload current tab with Doku summary
				loadTab(null, null, false);
			}
		});
	}

	function finishGalleryWalk(){
		if (!confirm('<spring:escapeBody javaScriptEscape='true'><fmt:message key="monitoring.summary.gallery.walk.finish.confirm" /></spring:escapeBody>')) {
			return;
		}

		$.ajax({
			'url' : '<c:url value="/monitoring/finishGalleryWalk.do"/>',
			'data': {
				toolContentID : ${dokumaran.contentId}
			},
			'success' : function(){
				// reload current tab with Doku summary
				loadTab(null, null, false);
			}
		});
	}

	function openGalleryWalkClusters(){
		window.open('<lams:WebAppURL/>monitoring/showGalleryWalkClusters.do?toolContentID=${dokumaran.contentId}', '_blank');
	}


	function enableGalleryWalkLearnerEdit(){
		if (!confirm('<spring:escapeBody javaScriptEscape='true'><fmt:message key="monitoring.summary.gallery.walk.learner.edit.confirm" /></spring:escapeBody>')) {
			return;
		}

		$.ajax({
			'url' : '<c:url value="/monitoring/enableGalleryWalkLearnerEdit.do"/>',
			'data': {
				toolContentID : ${dokumaran.contentId}
			},
			'success' : function(){
				// reload current tab with Doku summary
				loadTab(null, null, false);
			}
		});
	}


	function showChangeLeaderModal(toolSessionId) {
		$('#doku-monitoring-summary-${sessionMap.toolContentID} #change-leader-modals').empty()
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
					alert("<fmt:message key='label.monitoring.leader.successfully.changed'/>");
				},
				error : function(){
					alert("<fmt:message key='label.monitoring.leader.not.changed'/>");
				}
			});

		} else {
			alert("<fmt:message key='label.monitoring.leader.not.changed'/>");
		}
	}

</script>
<lams:JSImport src="includes/javascript/rating.js" />
<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>

<!-- Extra container div to isolate content from multiple Application Excercise tabs in TBL monitoring -->
<div id="doku-monitoring-summary-${sessionMap.toolContentID}" class="doku-monitoring-summary container-fluid">

	<div class="row mb-3">
		<div class="col-10 offset-1 pt-1">
			<h4>
				<i class="fa fa-users" style="color:gray" ></i>
				<fmt:message key="label.attendance"/>: <span>${attemptedLearnersNumber}</span>/<span class="total-learners-number"></span>
			</h4>
		</div>
	</div>

	<div class="row mb-3">
		<div class="col-10 offset-1">
			<div class="card">
				<div id="description-heading" class="card-header collapsed" role="button"
					 data-bs-toggle="collapse" href="#description-collapse" aria-expanded="false">
			       	<span class="card-title">
			       		<fmt:message key="label.authoring.basic.description" />
			     	</span>
				</div>

				<div id="description-collapse" class="collapse card-body" role="tabpanel" aria-labelledby="description-heading">
					<c:out value="${dokumaran.description}" escapeXml="false"/>
				</div>
			</div>
		</div>
	</div>

	<div class="row mb-3">
		<div class="col-6 offset-4">
			<c:if test="${empty summaryList}">
				<lams:Alert type="info" id="no-session-summary" close="false">
					<fmt:message key="message.monitoring.summary.no.session" />
				</lams:Alert>
			</c:if>

			<!--For release marks feature-->
			<i class="fa fa-spinner" style="display:none" id="message-area-busy"></i>
			<div id="message-area"></div>
		</div>
	</div>

	<div class="row mb-3">
		<div class="col-10 offset-1">
			<c:if test="${not empty summaryList and dokumaran.galleryWalkEnabled}">
				<div class="card ${dokumaran.galleryWalkFinished and not dokumaran.galleryWalkReadOnly ? 'gallery-walk-panel-ratings' : ''}"
					 id="gallery-walk-panel">
					<div class="card-header">
						<h3 class="card-title">
							<fmt:message key="label.gallery.walk" />&nbsp;
							<b>
								<c:choose>
									<c:when test="${not dokumaran.galleryWalkStarted and not dokumaran.galleryWalkFinished}">
										<fmt:message key="label.gallery.walk.state.not.started" />
									</c:when>
									<c:when test="${dokumaran.galleryWalkStarted and not dokumaran.galleryWalkFinished}">
										<fmt:message key="label.gallery.walk.state.started" />
									</c:when>
									<c:when test="${dokumaran.galleryWalkFinished}">
										<fmt:message key="label.gallery.walk.state.finished" />
									</c:when>
								</c:choose>
								<c:if test="${dokumaran.galleryWalkEditEnabled}">
									<fmt:message key="label.gallery.walk.state.learner.edit.enabled" />
								</c:if>
							</b>
						</h3>
					</div>
					<div class="card-body">
						<button id="gallery-walk-start" type="button"
								class="btn btn-primary
						        	   ${not dokumaran.galleryWalkStarted and not dokumaran.galleryWalkFinished ? '' : 'd-none'}"
								onClick="javascript:startGalleryWalk()">
							<fmt:message key="monitoring.summary.gallery.walk.start" />
						</button>

						<button id="gallery-walk-finish" type="button"
								class="btn btn-primary ${dokumaran.galleryWalkStarted and not dokumaran.galleryWalkFinished ? '' : 'd-none'}"
								onClick="javascript:finishGalleryWalk()">
							<fmt:message key="monitoring.summary.gallery.walk.finish" />
						</button>

						<br>

						<button id="gallery-walk-skip" type="button"
								class="btn btn-danger
						        	   ${not dokumaran.galleryWalkStarted and not dokumaran.galleryWalkFinished ? '' : 'd-none'}"
								onClick="javascript:skipGalleryWalk()">
							<fmt:message key="monitoring.summary.gallery.walk.skip" />
						</button>

						<button id="gallery-walk-show-clusters" type="button"
								class="btn btn-primary
						        	   ${dokumaran.galleryWalkClusterSize > 0 and dokumaran.galleryWalkStarted ? '' : 'd-none'}"
								onClick="javascript:openGalleryWalkClusters()">
							<i class="fa-solid fa-arrow-up-right-from-square"></i>
							<fmt:message key="monitoring.summary.gallery.walk.cluster.view.button" />
						</button>

						<br>

						<button id="gallery-walk-learner-edit" type="button"
								class="btn btn-secondary ${not dokumaran.galleryWalkEditEnabled and dokumaran.galleryWalkStarted ? '' : 'd-none'}"
								onClick="javascript:enableGalleryWalkLearnerEdit()">
							<fmt:message key="monitoring.summary.gallery.walk.learner.edit" />
						</button>

						<c:if test="${dokumaran.galleryWalkFinished and not dokumaran.galleryWalkReadOnly}">
							<h4 style="text-align: center"><fmt:message key="label.gallery.walk.ratings.header" /></h4>
							<table id="gallery-walk-rating-table" class="table table-hover table-condensed">
								<thead class="thead-light">
								<tr>
									<th scope="col"><fmt:message key="monitoring.label.group" /></th>
									<th scope="col"><fmt:message key="label.rating" /></th>
								</tr>
								</thead>
								<tbody>
								<c:forEach var="groupSummary" items="${summaryList}">
									<tr>
										<td>${groupSummary.sessionName}</td>
										<td>
											<lams:Rating itemRatingDto="${groupSummary.itemRatingDto}"
														 isItemAuthoredByUser="true"
														 hideCriteriaTitle="true" />
										</td>
									</tr>
								</c:forEach>
								</tbody>
							</table>
						</c:if>
					</div>
				</div>
			</c:if>

			<c:if test="${sessionMap.isGroupedActivity}">
			<div class="accordion" id="accordionSessions">
				</c:if>

				<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
					<c:choose>
						<c:when test="${sessionMap.isGroupedActivity}">
							<div class="accordion-item" >
							<h2 class="accordion-header" id="heading${groupSummary.sessionId}">
								<button class="accordion-button collapsed" type="button"
										data-bs-toggle="collapse" data-bs-target="#collapse${groupSummary.sessionId}"
										aria-expanded="false">
									<fmt:message key="monitoring.label.group" />&nbsp;${groupSummary.sessionName}
								</button>
								<c:if test="${dokumaran.useSelectLeaderToolOuput and groupSummary.numberOfLearners > 0 and not groupSummary.sessionFinished}">
									<button type="button" class="btn btn-secondary float-end"
											onClick="javascript:showChangeLeaderModal(${groupSummary.sessionId})">
										<fmt:message key='label.monitoring.change.leader'/>
									</button>
								</c:if>
							</h2>

							<div id="collapse${groupSummary.sessionId}" class="accordion-collapse collapse etherpad-collapse"
							role="tabpanel" aria-labelledby="heading${groupSummary.sessionId}">
						</c:when>
						<c:when test="${dokumaran.useSelectLeaderToolOuput and groupSummary.numberOfLearners > 0 and not groupSummary.sessionFinished}">
							<div class="text-end">
								<button type="button" class="btn btn-secondary" style="margin-bottom: 10px"
										onClick="javascript:showChangeLeaderModal(${groupSummary.sessionId})">
									<fmt:message key='label.monitoring.change.leader'/>
								</button>
							</div>
						</c:when>
					</c:choose>

					<c:choose>
						<c:when test="${groupSummary.sessionFaulty}">

							<div class="faulty-pad-container">
								<fmt:message key="label.cant.display.faulty.pad" />

								<a href="#nogo" class="btn btn-default btn-xs fix-faulty-pad" data-session-id="${groupSummary.sessionId}">
									<fmt:message key="label.recreate.faulty.pad" />
								</a>
							</div>

						</c:when>
						<c:otherwise>
							<c:if test="${dokumaran.galleryWalkStarted and not dokumaran.galleryWalkReadOnly}">
								<lams:Rating itemRatingDto="${groupSummary.itemRatingDto}" isItemAuthoredByUser="true" />
							</c:if>

							<lams:Etherpad groupId="${groupSummary.sessionId}" padId="${groupSummary.padId}"
										   showControls="true" showChat="${dokumaran.showChat}" showOnDemand="${sessionMap.isGroupedActivity}"
										   heightAutoGrow="true" height="600" />
						</c:otherwise>
					</c:choose>


					<!-- Editable marks section -->
					<div class="mt-2">
						<h4>
							<fmt:message key="label.monitoring.learner.marks.header"/>
						</h4>
						<lams:TSTable numColumns="2" dataId='toolSessionId="${groupSummary.sessionId}"'>
							<th><fmt:message key="label.monitoring.learner.marks.name"/></th>
							<th><fmt:message key="label.monitoring.learner.marks.mark"/>&nbsp;
								<small><fmt:message key="label.monitoring.learner.marks.mark.tip"/></small>
							</th>
						</lams:TSTable>
					</div>

					<c:if test="${sessionMap.isGroupedActivity}">
						</div> <!-- end collapse area  -->
						</div> <!-- end collapse panel  -->
					</c:if>
					${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="mt-3">&nbsp;</div>' :  ''}

				</c:forEach>

				<c:if test="${sessionMap.isGroupedActivity}">
			</div> <!--  end accordianSessions -->
			</c:if>

		</div>
	</div>

	<div class="row mb-3">
		<div class="col-10 offset-1">
			<c:if test="${sessionMap.dokumaran.reflectOnActivity}">
				<%@ include file="reflections.jsp"%>
			</c:if>
		</div>
	</div>

	<div class="row">
		<div class="col-10 offset-1" id="time-limit-panel-placeholder">
		</div>
	</div>

	<div id="change-leader-modals"></div>
</div>