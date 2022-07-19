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
					<!-- 
						<a id="canvasFitScreenButton" class="btn btn-sm btn-default" title="<fmt:message key='button.canvas.fit.screen.tooltip'/>"
					       href="#" onClick="javascript:canvasFitScreen(true)">
							<i class="fa fa-arrows-alt"></i> <span class="hidden-xs"><fmt:message key='button.canvas.fit.screen'/></span>
						</a>
						<a id="canvasOriginalSizeButton" class="btn btn-sm btn-default" title="<fmt:message key='button.canvas.original.size.tooltip'/>"
					       href="#" onClick="javascript:canvasFitScreen(false)">
							<i class="fa fa-arrow-circle-o-up"></i> <span class="hidden-xs"><fmt:message key='button.canvas.original.size'/></span>
						</a>
					 -->
				</div>
				<div id="sequenceCanvas"></div>
				<div id="completedLearnersContainer" class="mt-2" title="<fmt:message key='force.complete.end.lesson.tooltip' />">
					<img id="completedLessonLearnersIcon" src="<lams:LAMSURL/>images/completed.svg" />
				</div>
				<img id="sequenceCanvasLoading"
				     src="<lams:LAMSURL/>images/ajax-loader-big.gif" />
				<img id="sequenceSearchedLearnerHighlighter"
				     src="<lams:LAMSURL/>images/pedag_down_arrow.gif" />
				<!--
				<img src="<lams:LAMSURL/>images/components/user-map.png" alt="#" />
				<div class="map-pn">
					<span><img src="<lams:LAMSURL/>images/components/plus.svg" alt="#" /></span>
					<span><img src="<lams:LAMSURL/>images/components/minus.svg" alt="#" /></span>
				</div>
				-->
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
						<!--
						<div class="graph-con">
							<canvas id="myChart" class="chartjs-render-monitor"></canvas>
						</div>
						<ul>
							<li>
								<h6>completion</h6>
							</li>
							<li>
								<span class="graph-count"></span> - Not started
							</li>
							<li>
								<span class="graph-count"></span> - In process
							</li>
							<li>
								<span class="graph-count"></span> - Completed
							</li>
						</ul>
						-->
					</div>
				</div>
				<div id="required-tasks" class="col sequence-tab-layout-column">
					<div class="monitoring-panel">
						<h6><fmt:message key="lesson.required.tasks"/></h6>
						<div id="required-tasks-content"></div>
					</div>
				</div>
				<!-- 
				<div class="col mb-5">
					<div id="insights" class="monitoring-panel">
						<h6>insights</h6>
						<div class="insight-col-content">
							<p>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem</p>
							<p><a href="#">read more...</a></p>
						</div>
					</div>
				</div>
				 -->
			</div>

		</div>
	</div>
</div>


<!-- 
<div class="monitoring-layout-element">
	<div class="graph-grades">
		<div class="graph-star-col">
			<img src="<lams:LAMSURL/>images/components/star.png" alt="#" />
			<p>grades</p>
		</div>
		<div class="grades-progress-col">
			<div class="grades-left">
				<div class="grades-score d-flex">
					<p><span>High:</span> 99</p>
					<div class="grades-progress-bar">
						<div class="progress-div" id="progress-bar-1" style="width: 99%">
							<span></span>
						</div>
					</div>
				</div>
				<div class="grades-score d-flex">
					<p><span>Median:</span> 68</p>
					<div class="grades-progress-bar">
						<div class="progress-div" id="progress-bar-2" style="width: 68%">
							<span></span>
						</div>
					</div>
				</div>
				<div class="grades-score d-flex">
					<p><span>Low:</span> 28</p>
					<div class="grades-progress-bar">
						<div class="progress-div" id="progress-bar-3" style="width: 28%">
							<span></span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>	
				
<div class="monitoring-layout-element">
	<div class="row">
		<div class="col-12 col-sm-6">
			<div class="score-col-inner" id="score-col1">
				<h1>8<span>%</span></h1>
				<p>Students <br>at risk</p>
				<img src="<lams:LAMSURL/>images/components/icon1.png" alt="#" />
			</div>
		</div>
		<div class="col-12 col-sm-6">
			<div class="score-col-inner" id="score-col2">
				<h1>98<span>%</span></h1>
				<p>Average <br>Score</p>
				<img src="<lams:LAMSURL/>images/components/icon2.png" alt="#" />
			</div>
		</div>
	</div>
</div>
-->