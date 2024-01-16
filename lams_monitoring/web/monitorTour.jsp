	<%--  The definition of Bootstrap Tour for monitor.jsp. Use storage: false so that it always starts at the first step otherwise the multiple tabs confuses matters. --%>
	var tour = null;
	function startTour() {

		var selectedTabID = getCurrentTabID();
		var startStep;
		if ( selectedTabID == 2 ) {
			startStep = 15;
		} else if ( selectedTabID == 3 ) {
			startStep = 23;
		}  else if ( selectedTabID == 4 ) {
			startStep = 29;
		}

		tourInProgress = true;
		$('.tour-button').prop('disabled', true);

		if ( tour == null ) {
			tour = new Tour({
			  framework: "bootstrap3",
			  name: "LAMSMonitorTour",
		      steps: [
		          {  // 0
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.intro.title"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.intro.content"/></spring:escapeBody>",
			        orphan: true
			      }, {
			        element: "#page-tabs",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.tab.selection.title"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><p><fmt:message key="tour.tab.selection.content.1"/></p><p><fmt:message key="tour.tab.selection.content.2"/></p><p><fmt:message key="tour.tab.selection.content.3"/></p><p><fmt:message key="tour.tab.selection.content.4"/></p><p><fmt:message key="tour.tab.selection.content.5"/></p></spring:escapeBody>",
		        	placement: "bottom",
			      },{
			      	element: "#tour-refresh-button",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="button.refresh"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.tab.refresh.content"/></spring:escapeBody>",
		        	placement: "left",
			      },{
			        element: "#description",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="summery.desc.lbl"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.lesson.description"/></spring:escapeBody>",
			        placement: "bottom",
			      },{ 
			        element: "#lessonStateLabel:first-child",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="lesson.state"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><p><fmt:message key="tour.lesson.state.content.1"/></p><p><fmt:message key="tour.lesson.state.content.2"/></p><p><fmt:message key="tour.lesson.state.content.3"/></p><p><fmt:message key="tour.lesson.state.content.4"/></p><p><fmt:message key="tour.lesson.state.content.5"/></p></spring:escapeBody>",
			        placement: "right",
			      },{  // 5
			        element: "#tour-learner-count",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="lesson.learners"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><p><fmt:message key="tour.lesson.count.learners.content.1"/></p><p><fmt:message key="tour.lesson.count.learners.content.2"/></p></spring:escapeBody>",
			        placement: "right",
			      },{
			        element: "#viewLearnersButton",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="button.view.learners"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.learner.in.class.content"/></spring:escapeBody>",
			        placement: "top",
			      },{
			        element: "#editClassButton",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="button.edit.class"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.edit.class.content"/></spring:escapeBody>",
			        placement: "top",
			      },{
			        element: "#notificationButton",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="email.notifications"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.email.notifications.content"/></spring:escapeBody>",
			        placement: "top",
			      },{   
			        element: "#editIntroButton",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.lesson.introduction"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.edit.lesson.introduction"/></spring:escapeBody>",
			        placement: "top",
			      },{  // 10
			        element: "#gradebookOnCompleteButton",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.display.activity.scores"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.display.activity.scores.content"/></spring:escapeBody>",
			        placement: "top",
			      },{
			        element: "#sendProgressEmail", 
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.progress.email.send.title"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.progress.email.send.content"/></spring:escapeBody>",
			        placement: "bottom"
			      },{
			        element: "#configureProgressEmail", 
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.progress.email.configure.title"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.progress.email.confure.content"/></spring:escapeBody>",
			        placement: "bottom"
			      },{ 
			        element: "#chartDiv",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="lesson.chart.title"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.completion.chart.content"/></spring:escapeBody>",
			        placement: "left",
			        onNext: switchToSequence
			      },{   // 15
			      	title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tab.sequence"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.tab.sequence.content"/></spring:escapeBody>",
			        orphan: true,
					delayOnElement:	{ delayElement: "#sequenceTopButtonsContainer", maxDelay: 2000 },
			        onPrev: switchToLesson
			      },{
			        element: "#sequenceCanvas",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tab.sequence"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.activity.content"/></spring:escapeBody>",
			        placement: "top",
			        backdrop: true
			      },{
			        element: "#sequenceCanvas",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.move.learners.title"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.move.learners.small.content"/></spring:escapeBody>",
			        placement: "top",
					backdrop: true
			      },{
			        element: "#sequenceCanvas",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.move.learners.title"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.move.learners.affect.content"/></spring:escapeBody>",
			        placement: "top",
			        backdrop: true
			      },{ 
			        element: "#sequenceCanvas",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.move.learners.title"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.move.learners.large.content"/></spring:escapeBody>",
			        placement: "top",
			        backdrop: true
			      },{ // 20
			        element: "#completedLearnersContainer",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.completed.learners.title"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.completed.learners.content"/></spring:escapeBody>",
			        placement: "top",
			        backdrop: true
			      },{
			        element: "#liveEditButton",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.live.edit.title"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.live.edit.content"/></spring:escapeBody>",
			        placement: "left"
			      },{
			        element: "#sequenceSearchPhrase",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.learner.search.title"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.learner.search.content"/></spring:escapeBody>",
					delayOnElement:	{ delayElement: "element", maxDelay: 2000 },
			        onNext: switchToLearners,
			        placement: "bottom"
			      },{
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tab.learners"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.tab.learners.content"/></spring:escapeBody>",
			        orphan: true,
					delayOnElement:	{ delayElement: "#tabLearnerControlTable", maxDelay: 2000 },
			        onPrev: switchToSequence
			      },{ 
			        element: "#tabLearnersContainer",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tab.learners"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><p><fmt:message key="tour.learners.progress.content.1"/></p><p><fmt:message key="tour.learners.progress.content.2"/></p><p><fmt:message key="tour.learners.progress.content.3"/></p></spring:escapeBody>",
			        placement: "top",
			        backdrop: true,
			      },{  // 25
			        element: "#learnersSearchPhrase",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.learner.search.title"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.learner.search.content"/></spring:escapeBody>",
			        placement: "bottom",
			      },{
			        element: "#orderByCompletionCheckbox",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.completion.sorting.title"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.completion.sorting.content"/></spring:escapeBody>",
			        placement: "bottom",
			      },{
			        element: "#journalButton", 
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="button.journal.entries"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.journal.entries.content"/></spring:escapeBody>",
			        placement: "bottom",
			      },{
			        element: ".tour-email-button:first", 
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="label.email"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.email.content"/></spring:escapeBody>",
			        placement: "left",
			        delayOnElement:	{ delayElement: "element", maxDelay: 2000 },
			        onNext: switchToGradebook
			      },{ 
			        element: "#userView",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.learner.grades"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.learner.grades.content"/></spring:escapeBody>",
			        placement: "top",
			        delayOnElement:	{ delayElement: "#gradebookTopButtonsContainer", maxDelay: 2000 },
			        onPrev: switchToLearners
			      },{ // 30
			        element: "#activityView",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.activity.grades"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.activity.grades.content"/></spring:escapeBody>",
			        placement: "top"
			      },{
			        element: "#export-grades-button",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.export.grades"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.export.grades.content"/></spring:escapeBody>",
			        placement: "bottom"
			      },{
			        element: "#tour-release-marks",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.release.marks"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.release.marks.content"/></spring:escapeBody>",
			        placement: "bottom"
			      },{
			        element: "#tour-mark-chart-button",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.show.marks.chart"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.show.marks.chart.content"/></spring:escapeBody>",
			        placement: "bottom",
			        onNext: showMarkChart
			      },{  
			        element: "#markChartDiv",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.marks.chart"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.histogram.help.content"/></spring:escapeBody>",
			        backdrop: true,
			        placement: "top",
			        onNext: hideMarkChart
			      },{ // 35
			        element: "#tour-weight-button",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.show.weight"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.show.weight.content"/></spring:escapeBody>",
			        placement: "bottom",
			        onPrev: showMarkChart
			      },{
			        element: "#tour-dates",
			        title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.show.dates"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.gradebook.show.dates.content"/></spring:escapeBody>",
			        placement: "bottom",
			        onPrev: showMarkChart
			      },{  // 37
					title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key="tour.end.title"/></spring:escapeBody>",
			        content: "<spring:escapeBody javaScriptEscape='true'><p><fmt:message key="tour.end.content.1"/></p><p><fmt:message key="tour.end.content.2"/></p></spring:escapeBody>",
			        placement: "top",
			        orphan: true
			      }		     
			    ],
			  onEnd: tourEnd,
			  debug: true,
			  backdrop: false,
			  storage: false,
			  showProgressText: false,
			  showProgressBar: true,
			  sanitizeWhitelist:	{ "button"	: ["data-morehelp"] },
			  template: '<div class="popover" role="tooltip"> <div class="arrow"></div> <h3 class="popover-title"></h3> <div class="popover-content"></div> <div class="popover-navigation"> <div class="btn-group"> <button class="btn btn-sm btn-default" data-role="prev">&laquo; <fmt:message key="tour.prev"/></button> <button class="btn btn-sm btn-default" data-role="next"><fmt:message key="tour.next"/> &raquo;</button> </div> <button data-morehelp="true" class="btn btn-sm btn-default loffset5 morehelp"><fmt:message key="tour.more.help"/></button>  <button class="btn btn-sm btn-default" data-role="end"><fmt:message key="tour.end.tour"/></button> </div>'
			  
			});

		    tour.start(true);

		} else {
			tour.restart();
		}
		if ( startStep ) {
			tour.goTo(startStep);
		}
		
		$("body").on("click","button.morehelp", openWikiHelp);
	}

	// These two functions call functions from gradebook page so do not risk breaking tour if they are missing or broken
	function showMarkChart() {
		try{
			showMarkChart();
 		} catch(e){};
	}

	function hideMarkChart() {
		try{
			hideMarkChart();
 		} catch(e){};
	}
	
	function switchToLesson() {
		actualDoSelectTab(1);
	}

	function switchToSequence() {
		actualDoSelectTab(2);
	}

	function switchToLearners() {
		actualDoSelectTab(3);
	}

	function switchToGradebook() {
		actualDoSelectTab(4);
		var check = 0;
	}
	
	function tourEnd() {
		tourInProgress = false;
		$('.tour-button').prop('disabled', false);
	}
	
	function openWikiHelp() {
		var selectedTabID = getCurrentTabID();
		var url = "http://wiki.lamsfoundation.org/display/lamsdocs/monitoringlesson";
		if ( selectedTabID == 2 ) {
			url = "http://wiki.lamsfoundation.org/display/lamsdocs/monitoringsequence";
		} else if ( selectedTabID == 3 ) {
			url = "http://wiki.lamsfoundation.org/display/lamsdocs/monitoringlearners";
		} else if ( selectedTabID == 4 ) {
			url = "http://wiki.lamsfoundation.org/display/lamsdocs/Gradebook+Lesson+Marking";
		}
		openPopUp(url,'<fmt:message key="button.help"/>', 648, 1152, false);
	}