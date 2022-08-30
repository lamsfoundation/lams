<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<div id="sequence-tab-content" class="container-fluid">
	<div class="row">
		<div class="col sequence-tab-layout-column">
			<div style="display:none" class="text-danger fw-bold" id="liveEditWarning"></div>
			<div id="canvas-container" class="svg-learner-draggable-area text-start monitoring-panel">
				<div id="sequenceCanvas"></div>
				<div id="completedLearnersContainer" class="mt-2" title="<fmt:message key='force.complete.end.lesson.tooltip' />">
					<img id="completedLessonLearnersIcon" src="<lams:LAMSURL/>images/completed.svg" />
				</div>
				<img id="sequenceCanvasLoading"
				     src="<lams:LAMSURL/>images/ajax-loader-big.gif" />
				<img id="sequenceSearchedLearnerHighlighter"
				     src="<lams:LAMSURL/>images/pedag_down_arrow.gif" />
			</div>
		</div>
		<div class="col">
			<div class="row">
				<div id="required-tasks" class="col sequence-tab-layout-column">
					<div class="monitoring-panel">
						<h6><fmt:message key="lesson.required.tasks"/></h6>
						<div id="required-tasks-content"></div>
					</div>
				</div>
				<div class="col sequence-tab-layout-column">
					<div id="completion-chart-panel" class="monitoring-panel">
						<h6><fmt:message key="lesson.chart.title"/></h6>
						<div class="panel-body">
							<canvas id="completion-chart" height="300px" width="240px"></canvas>
							<div class="mt-3 row">
								<div class="col-6 offset-md-2 text-end"><fmt:message key="lesson.ratio.learners.started"/></div>
								<div class="col-2 text-start" id="learner-started-count"></div>
							</div>
							<div class="row">
								<div class="col col-6 offset-md-2 text-end"><fmt:message key="lesson.ratio.learners.total"/></div>
								<div class="col-2 text-start" id="learner-total-count"></div>
							</div>
							<div id="lesson-time-limits" class="d-none mt-3">
								<div class="text-center fw-bold mb-1">
									<fmt:message key="label.monitoring.time.limit"/>
								</div>
							</div>
						</div>

					</div>
				</div>
				<div class="col sequence-tab-layout-column d-none" id="lesson-instructions-panel">
					<div class="monitoring-panel">
						<h6><fmt:message key="tab.instructions.title" /></h6>
						<div id="lesson-instructions-content" class="text-start">
							<c:out value="${lesson.lessonInstructions}" escapeXml="false" />
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
</div>