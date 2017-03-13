	<%--  The definition of Bootstrap Tour for monitor.jsp. Use storage: false so that it always starts at the first step otherwise the multiple tabs confuses matters. --%>
	var tour = null;
	function startTour() {
	debugger;
		tourInProgress = true;
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
			        content: "<fmt:message key="tour.tab.selection.content"/>",
		        	placement: "bottom",
			      },{
			      	element: "#tour-refresh-button",
			        title: "<fmt:message key="button.refresh"/>",
			        content: "<fmt:message key="tour.tab.refresh.content"/>",
		        	placement: "bottom",
			      }, {
			      	element: "#tour-help-button",
			        title: "<fmt:message key="button.help"/>",
			        content: "<fmt:message key="tour.help.content"/>",
		        	placement: "left",
			      },{
			        element: "#lessonStateLabel:first-child",
			        title: "<fmt:message key="lesson.state"/>",
			        content: "<fmt:message key="tour.lesson.state.content"/>",
			        placement: "right",
			      },{
			        element: "#tour-learner-count",
			        title: "<fmt:message key="lesson.learners"/>",
			        content: "<fmt:message key="tour.lesson.count.learners.content"/>",
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
			        content: "<fmt:message key="tour.lesson.im.content"/>",
			        placement: "bottom",
			        onShow: revealIM,
			        onHide: hideIM
			      },{
			        element: "#chartDiv",
			        title: "<fmt:message key="lesson.chart.title"/>",
			        content: "<fmt:message key="tour.completion.chart.content"/>",
			        placement: "bottom",
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
			        placement: "bottom",
			        onNext: switchToLearners
			      },{
			        title: "<fmt:message key="tab.learners"/>",
			        content: "<fmt:message key="tour.tab.learners.content"/>",
			        orphan: true,
			        onPrev: switchToSequence
			      },{
			        element: "#tabLearnersContainer",
			        title: "<fmt:message key="tab.learners"/>",
			        content: "<fmt:message key="tour.learners.progress.content"/>",
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
			        content: "<fmt:message key="tour.end.content"/>",
			        placement: "top",
			        orphan: true
			      }		     
			    ],
			  onEnd: tourEnd,
			  debug: true,
			  backdrop: false,
			  storage: false
			});

		    tour.init();
		    tour.start(true);

		} else {
			tour.restart();
		}
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
	}