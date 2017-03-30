	<%--  The definition of Bootstrap Tour for monitor.jsp. Use storage: false so that it always starts at the first step otherwise the multiple tabs confuses matters. --%>
	var tour = null;
	function startTour() {

		var selectedTabID = getCurrentTabID();
		var startStep = 0;
		if ( selectedTabID == 2 ) {
			startStep = 10;
		} else if ( selectedTabID == 3 ) {
			startStep = 18;
		}

		tourInProgress = true;
		$('.tour-button').prop('disabled', true);

		if ( tour == null ) {
			tour = new Tour({
			  name: "LAMSMonitorTour",
		      steps: [
		          {
			        title: "<fmt:message key="tour.intro.title"/>",
			        content: "<fmt:message key="tour.intro.content"/>",
			        orphan: true
			      }, {
			        element: "#page-tabs",
			        title: "<fmt:message key="tour.tab.selection.title"/>",
			        content: "<p><fmt:message key="tour.tab.selection.content.1"/></p><p><fmt:message key="tour.tab.selection.content.2"/></p><p><fmt:message key="tour.tab.selection.content.3"/></p><p><fmt:message key="tour.tab.selection.content.4"/></p>",
		        	placement: "bottom",
			      },{
			      	element: "#tour-refresh-button",
			        title: "<fmt:message key="button.refresh"/>",
			        content: "<fmt:message key="tour.tab.refresh.content"/>",
		        	placement: "left",
			      },{
			        element: "#lessonStateLabel:first-child",
			        title: "<fmt:message key="lesson.state"/>",
			        content: "<p><fmt:message key="tour.lesson.state.content.1"/></p><p><fmt:message key="tour.lesson.state.content.2"/></p><p><fmt:message key="tour.lesson.state.content.3"/></p><p><fmt:message key="tour.lesson.state.content.4"/></p><p><fmt:message key="tour.lesson.state.content.5"/></p>",
			        placement: "right",
			      },{
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
			        element: "#tour-lesson-im",
			        title: "<fmt:message key="lesson.im"/>",
			        content: "<p><fmt:message key="tour.lesson.im.content.1"/></p><p><fmt:message key="tour.lesson.im.content.2"/></p><p><fmt:message key="tour.lesson.im.content.3"/></p><p><fmt:message key="tour.lesson.im.content.4"/></p>",
			        placement: "bottom",
			        onShow: revealIM,
			        onHide: hideIM
			      },{
			        element: "#chartDiv",
			        title: "<fmt:message key="lesson.chart.title"/>",
			        content: "<fmt:message key="tour.completion.chart.content"/>",
			        placement: "left",
			        onNext: switchToSequence
			      },{
			      	title: "<fmt:message key="tab.sequence"/>",
			        content: "<fmt:message key="tour.tab.sequence.content"/>",
			        orphan: true,
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
			      },{
			        element: "#completedLearnersContainer",
			        title: "<fmt:message key="tour.completed.learners.title"/>",
			        content: "<fmt:message key="tour.completed.learners.content"/>",
			        placement: "top",
			        backdrop: true
			      },{
			        element: "#sequenceSearchPhrase",
			        title: "<fmt:message key="tour.learner.search.title"/>",
			        content: "<fmt:message key="tour.learner.search.content"/>",
			        placement: "bottom",
			      },{
			        element: "#liveEditButton",
			        title: "<fmt:message key="tour.live.edit.title"/>",
			        content: "<fmt:message key="tour.live.edit.content"/>",
			        placement: "left",
			        onNext: switchToLearners
			      },{
			        title: "<fmt:message key="tab.learners"/>",
			        content: "<fmt:message key="tour.tab.learners.content"/>",
			        orphan: true,
			        onPrev: switchToSequence
			      },{
			        element: "#tabLearnersContainer",
			        title: "<fmt:message key="tab.learners"/>",
			        content: "<p><fmt:message key="tour.learners.progress.content.1"/></p><p><fmt:message key="tour.learners.progress.content.2"/></p><p><fmt:message key="tour.learners.progress.content.3"/></p>",
			        placement: "top",
			        backdrop: true,
			      },{
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
			      },{
					title: "<fmt:message key="tour.end.title"/>",
			        content: "<p><fmt:message key="tour.end.content.1"/></p><p><fmt:message key="tour.end.content.2"/></p>",
			        placement: "top",
			        orphan: true
			      }		     
			    ],
			  onEnd: tourEnd,
			  debug: false,
			  backdrop: false,
			  storage: false,
			  template: '<div class="popover" role="tooltip"> <div class="arrow"></div> <h3 class="popover-title"></h3> <div class="popover-content"></div> <div class="popover-navigation"> <div class="btn-group"> <button class="btn btn-sm btn-default" data-role="prev">&laquo; <fmt:message key="tour.prev"/></button> <button class="btn btn-sm btn-default" data-role="next"><fmt:message key="tour.next"/> &raquo;</button> </div> <button class="btn btn-sm btn-default loffset5" onclick="openWikiHelp()"><fmt:message key="tour.more.help"/></button>  <button class="btn btn-sm btn-default" data-role="end"><fmt:message key="tour.end.tour"/></button> </div>'
			  
			});

		    tour.init();
		    tour.start(true);

		} else {
			tour.restart();
		}
		tour.goTo(startStep);
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
	
	function switchToLesson() {
		actualDoSelectTab(1);
	}

	function switchToSequence() {
		actualDoSelectTab(2);
	}

	function switchToLearners() {
		actualDoSelectTab(3);
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
		}
		openPopUp(url,'<fmt:message key="button.help"/>', 648, 1152, false);
	}