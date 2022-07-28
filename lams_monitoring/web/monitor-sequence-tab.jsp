<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<div id="sequence-tab-content" class="container-fluid">
	<div class="row">
		<div class="col sequence-tab-layout-column">
			<div id="canvas-container" class="svg-learner-draggable-area text-start monitoring-panel">
				<div id="sequenceTopButtonsContainer" class="topButtonsContainer">
					<a id="liveEditButton" class="btn btn-sm btn-secondary float-end" style="display:none" title="<fmt:message key='button.live.edit.tooltip'/>"
				       href="#" onClick="javascript:openLiveEdit()">
						<i class="fa fa-pencil"></i> <span class="hidden-xs"><fmt:message key='button.live.edit'/></span>
					</a>
				</div>
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
				<div class="col sequence-tab-layout-column">
					<div id="completion-chart-panel" class="monitoring-panel">
						<h6><fmt:message key="lesson.chart.title"/></h6>
						<div class="panel-body">
							<canvas id="completion-chart" height="600px" width="240px"></canvas>
						</div>
					</div>
				</div>
				<div id="required-tasks" class="col sequence-tab-layout-column">
					<div class="monitoring-panel">
						<h6><fmt:message key="lesson.required.tasks"/></h6>
						<div id="required-tasks-content"></div>
					</div>
				</div>
			</div>

		</div>
	</div>
</div>