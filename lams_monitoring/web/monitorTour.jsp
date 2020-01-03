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
			        title: "<fmt:message key="tour.intro.title"/>",
			        content: "<fmt:message key="tour.intro.content"/>",
			        orphan: true
			      }, {
			        element: "#page-tabs",
			        title: "<fmt:message key="tour.tab.selection.title"/>",
			        content: "<p><fmt:message key="tour.tab.selection.content.1"/></p><p><fmt:message key="tour.tab.selection.content.2"/></p><p><fmt:message key="tour.tab.selection.content.3"/></p><p><fmt:message key="tour.tab.selection.content.4"/></p><p><fmt:message key="tour.tab.selection.content.5"/></p>",
		        	placement: "bottom",
			      },{
			      	element: "#tour-refresh-button",
			        title: "<fmt:message key="button.refresh"/>",
			        content: "<fmt:message key="tour.tab.refresh.content"/>",
		        	placement: "left",
			      },{
			        element: "#description",
			        title: "<fmt:message key="summery.desc.lbl"/>",
			        content: "<fmt:message key="tour.lesson.description"/>",
			        placement: "bottom",
			      },{ 
			        element: "#lessonStateLabel:first-child",
			        title: "<fmt:message key="lesson.state"/>",
			        content: "<p><fmt:message key="tour.lesson.state.content.1"/></p><p><fmt:message key="tour.lesson.state.content.2"/></p><p><fmt:message key="tour.lesson.state.content.3"/></p><p><fmt:message key="tour.lesson.state.content.4"/></p><p><fmt:message key="tour.lesson.state.content.5"/></p>",
			        placement: "right",
			      },{  // 5
			        element: "#tour-learner-count",
			        title: "<fmt:message key="lesson.learners"/>",
			        content: "<p><fmt:message key="tour.lesson.count.learners.content.1"/></p><p><fmt:message key="tour.lesson.count.learners.content.2"/></p>",
			        placement: "right",
			      },{
			        element: "#viewLearnersButton",
			        title: "<fmt:message key="button.view.learners"/>",
			        content: "<fmt:message key="tour.learner.in.class.content"/>",
			        placement: "top",
			      },{
			        element: "#editClassButton",
			        title: "<fmt:message key="button.edit.class"/>",
			        content: "<fmt:message key="tour.edit.class.content"/>",
			        placement: "top",
			      },{
			        element: "#notificationButton",
			        title: "<fmt:message key="email.notifications"/>",
			        content: "<fmt:message key="tour.email.notifications.content"/>",
			        placement: "top",
			      },{   
			        element: "#editIntroButton",
			        title: "<fmt:message key="label.lesson.introduction"/>",
			        content: "<fmt:message key="tour.edit.lesson.introduction"/>",
			        placement: "top",
			      },{  // 10
			        element: "#gradebookOnCompleteButton",
			        title: "<fmt:message key="label.display.activity.scores"/>",
			        content: "<fmt:message key="tour.display.activity.scores.content"/>",
			        placement: "top",
			      },{
			        element: "#tour-lesson-im",
			        title: "<fmt:message key="lesson.im"/>",
			        content: "<p><fmt:message key="tour.lesson.im.content.1"/></p><p><fmt:message key="tour.lesson.im.content.2"/></p><p><fmt:message key="tour.lesson.im.content.3"/></p><p><fmt:message key="tour.lesson.im.content.4"/></p>",
			        placement: "bottom",
			        onShow: revealIM,
			        onHide: hideIM
			      },{
			        element: "#sendProgressEmail", 
			        title: "<fmt:message key="tour.progress.email.send.title"/>",
			        content: "<fmt:message key="tour.progress.email.send.content"/>",
			        placement: "bottom"
			      },{
			        element: "#configureProgressEmail", 
			        title: "<fmt:message key="tour.progress.email.configure.title"/>",
			        content: "<fmt:message key="tour.progress.email.confure.content"/>",
			        placement: "bottom"
			      },{ 
			        element: "#chartDiv",
			        title: "<fmt:message key="lesson.chart.title"/>",
			        content: "<fmt:message key="tour.completion.chart.content"/>",
			        placement: "left",
			        onNext: switchToSequence
			      },{   // 15
			      	title: "<fmt:message key="tab.sequence"/>",
			        content: "<fmt:message key="tour.tab.sequence.content"/>",
			        orphan: true,
					delayOnElement:	{ delayElement: "#sequenceTopButtonsContainer", maxDelay: 2000 },
			        onPrev: switchToLesson
			      },{
			        element: "#sequenceCanvas",
			        title: "<fmt:message key="tab.sequence"/>",
			        content: "<fmt:message key="tour.activity.content"/>",
			        placement: "top",
			        backdrop: true
			      },{
			        element: "#sequenceCanvas",
			        title: "<fmt:message key="tour.move.learners.title"/>",
			        content: "<fmt:message key="tour.move.learners.small.content"/>",
			        placement: "top",
					backdrop: true
			      },{
			        element: "#sequenceCanvas",
			        title: "<fmt:message key="tour.move.learners.title"/>",
			        content: "<fmt:message key="tour.move.learners.affect.content"/>",
			        placement: "top",
			        backdrop: true
			      },{ 
			        element: "#sequenceCanvas",
			        title: "<fmt:message key="tour.move.learners.title"/>",
			        content: "<fmt:message key="tour.move.learners.large.content"/>",
			        placement: "top",
			        backdrop: true
			      },{ // 20
			        element: "#completedLearnersContainer",
			        title: "<fmt:message key="tour.completed.learners.title"/>",
			        content: "<fmt:message key="tour.completed.learners.content"/>",
			        placement: "top",
			        backdrop: true
			      },{
			        element: "#liveEditButton",
			        title: "<fmt:message key="tour.live.edit.title"/>",
			        content: "<fmt:message key="tour.live.edit.content"/>",
			        placement: "left"
			      },{
			        element: "#sequenceSearchPhrase",
			        title: "<fmt:message key="tour.learner.search.title"/>",
			        content: "<fmt:message key="tour.learner.search.content"/>",
					delayOnElement:	{ delayElement: "element", maxDelay: 2000 },
			        onNext: switchToLearners,
			        placement: "bottom"
			      },{
			        title: "<fmt:message key="tab.learners"/>",
			        content: "<fmt:message key="tour.tab.learners.content"/>",
			        orphan: true,
					delayOnElement:	{ delayElement: "#tabLearnerControlTable", maxDelay: 2000 },
			        onPrev: switchToSequence
			      },{ 
			        element: "#tabLearnersContainer",
			        title: "<fmt:message key="tab.learners"/>",
			        content: "<p><fmt:message key="tour.learners.progress.content.1"/></p><p><fmt:message key="tour.learners.progress.content.2"/></p><p><fmt:message key="tour.learners.progress.content.3"/></p>",
			        placement: "top",
			        backdrop: true,
			      },{  // 25
			        element: "#learnersSearchPhrase",
			        title: "<fmt:message key="tour.learner.search.title"/>",
			        content: "<fmt:message key="tour.learner.search.content"/>",
			        placement: "bottom",
			      },{
			        element: "#orderByCompletionCheckbox",
			        title: "<fmt:message key="tour.completion.sorting.title"/>",
			        content: "<fmt:message key="tour.completion.sorting.content"/>",
			        placement: "bottom",
			      },{
			        element: "#journalButton", 
			        title: "<fmt:message key="button.journal.entries"/>",
			        content: "<fmt:message key="tour.journal.entries.content"/>",
			        placement: "bottom",
			      },{
			        element: ".tour-email-button:first", 
			        title: "<fmt:message key="label.email"/>",
			        content: "<fmt:message key="tour.email.content"/>",
			        placement: "left",
			        delayOnElement:	{ delayElement: "element", maxDelay: 2000 },
			        onNext: switchToGradebook
			      },{ 
			        element: "#userView",
			        title: "<fmt:message key="tour.gradebook.learner.grades"/>",
			        content: "<fmt:message key="tour.gradebook.learner.grades.content"/>",
			        placement: "top",
			        delayOnElement:	{ delayElement: "#gradebookTopButtonsContainer", maxDelay: 2000 },
			        onPrev: switchToLearners
			      },{ // 30
			        element: "#activityView",
			        title: "<fmt:message key="tour.gradebook.activity.grades"/>",
			        content: "<fmt:message key="tour.gradebook.activity.grades.content"/>",
			        placement: "top"
			      },{
			        element: "#export-grades-button",
			        title: "<fmt:message key="tour.gradebook.export.grades"/>",
			        content: "<fmt:message key="tour.gradebook.export.grades.content"/>",
			        placement: "bottom"
			      },{
			        element: "#tour-release-marks",
			        title: "<fmt:message key="tour.gradebook.release.marks"/>",
			        content: "<fmt:message key="tour.gradebook.release.marks.content"/>",
			        placement: "bottom"
			      },{
			        element: "#tour-mark-chart-button",
			        title: "<fmt:message key="tour.gradebook.show.marks.chart"/>",
			        content: "<fmt:message key="tour.gradebook.show.marks.chart.content"/>",
			        placement: "bottom",
			        onNext: showMarkChart
			      },{  
			        element: "#markChartDiv",
			        title: "<fmt:message key="tour.gradebook.marks.chart"/>",
			        content: "<fmt:message key="tour.histogram.help.content"/>",
			        backdrop: true,
			        placement: "top",
			        onNext: hideMarkChart
			      },{ // 35
			        element: "#tour-weight-button",
			        title: "<fmt:message key="tour.gradebook.show.weight"/>",
			        content: "<fmt:message key="tour.gradebook.show.weight.content"/>",
			        placement: "bottom",
			        onPrev: showMarkChart
			      },{
			        element: "#tour-dates",
			        title: "<fmt:message key="tour.gradebook.show.dates"/>",
			        content: "<fmt:message key="tour.gradebook.show.dates.content"/>",
			        placement: "bottom",
			        onPrev: showMarkChart
			      },{  // 37
					title: "<fmt:message key="tour.end.title"/>",
			        content: "<p><fmt:message key="tour.end.content.1"/></p><p><fmt:message key="tour.end.content.2"/></p>",
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
	
 	function revealIM(tour) { 
		var checked = $('#presenceButton').hasClass('btn-success');
		if (!checked) {
			$("#imButton").css('display', 'block');
			$('#imButton').prop('disabled', true);
		}			
		checked = $('#imButton').hasClass('btn-success');
		if (!checked) {
			$("#openImButton").css('display', 'block');
			$('#openImButton').prop('disabled', true);
		}				
	}
	
 	function hideIM(tour) { 
		$('#imButton').prop('disabled', false);
		var checked = $('#presenceButton').hasClass('btn-success');
		if (!checked) {
			$("#imButton").css('display', 'none');
		}				
		$('#openImButton').prop('disabled', false);
		checked = $('#imButton').hasClass('btn-success');
		if (!checked) {
			$("#openImButton").css('display', 'none');
		}				
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