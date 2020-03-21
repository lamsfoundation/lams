	<%--  The definition of Bootstrap Tour for main.jsp --%>
	var tour = null;
	var courseIsHidden = null;
	function startTour() {
		courseIsHidden = $("body").hasClass("offcanvas-hidden");

		if ( tour == null ) {
			tour = new Tour({
			  framework: "bootstrap3",
			  name: "LAMSTour",
		      steps: [
		          {
			        title: "<fmt:message key="tour.intro.title"/>",
			        content: "<fmt:message key="tour.intro.content"/>",
			        placement: "top",
			        orphan: true
			      }, {
			        element: ".tour-course-reveal",
			        title: "<fmt:message key="organisations"/>",
			        content: "<fmt:message key="tour.course.reveal.content"/>",
		        	placement: "bottom",
			      }, {
			        element: ".tour-organisations",
			        title: "<fmt:message key="organisations"/>",
			        content: "<fmt:message key="tour.courses.content"/>",
		        	placement: "top",
		        	onShow: revealCourses
			      }, {
			        element: ".tour-organisations-favorites",
			        title: "<fmt:message key="organisations"/>",
			        content: "<fmt:message key="tour.favorites.content"/>",
		        	placement: "bottom",
		        	onHidden: hideCourses
			      }, {
			        element: ".tour-org-container",
			        title: "<fmt:message key="lessons"/>",
			        content: "<fmt:message key="tour.lessons.content"/>",
		        	placement: "top",
		        	backdrop: true
			      }, {
			        element: ".tour-user-notifications",
			        title: "<fmt:message key="index.emailnotifications"/>",
			        content: "<fmt:message key="tour.notification.content"/>",
			        placement: "left"
			      }, {
			        element: ".tour-index-author",
			        title: "<fmt:message key="authoring.fla.page.title"/>",
			        content: "<fmt:message key="tour.authoring.content"/>",
			        placement: "left"
			      }, {
			        element: ".tour-user-profile",
			        title: "<fmt:message key="index.myprofile"/>",
			        content: "<fmt:message key="tour.user.profile.content"/>",
			        placement: "left"
			      }, {
			        element: ".tour-favorite-organisation",
			        title: "<fmt:message key="index.mycourses"/>",
			        content: "<fmt:message key="tour.course.name.content"/>",
			        placement: "right"
			      }, {
			        element: ".tour-add-lesson",
			        title: "<fmt:message key="index.addlesson"/>",
			        content: "<fmt:message key="tour.add.lesson.content"/>",
			        placement: "left"
			      }, {
			        element: ".tour-add-single-lesson",
			        title: "<fmt:message key="index.addlesson"/>",
			        content: "<fmt:message key="tour.add.single.activity.lesson.content"/>",
			        placement: "left"
			      }, {
			        element: ".tour-index-coursegradebook-learner",
			        title: "<fmt:message key="index.coursegradebook"/>",
			        content: "<fmt:message key="tour.course.gradebook.content"/>",
			        placement: "left"
			      }, {
			        element: ".tour-more-options",
			        title: "<fmt:message key="index.moreActions"/>",
			        content: "<fmt:message key="tour.more.actions.content"/>",
			        placement: "left"
			      }, {
			        element: ".tour-index-monitor",
			        title: "<fmt:message key="tour.lesson.monitoring.title"/>",
			        content: "<fmt:message key="tour.lesson.monitoring.content"/>",
			        placement: "left"			        
			      }, {
			        element: ".tour-index-emailnotifications",
			        title: "<fmt:message key="tour.lesson.notifications.title"/>",
			        content: "<fmt:message key="tour.lesson.notifications.content"/>",
			        placement: "left"
			      }, {
			        element: ".tour-index-coursegradebookmonitor",
			        title: "<fmt:message key="tour.lesson.gradebook.title"/>",
			        content: "<fmt:message key="tour.lesson.gradebook.content"/>",
			        placement: "left"
			      }, {
			        element: ".tour-index-conditions",
			        title: "<fmt:message key="tour.lesson.conditions.title"/>",
			        content: "<fmt:message key="tour.lesson.conditions.content"/>",
			        placement: "left"
			      }, {
			        element: ".tour-index-remove-lesson",
			        title: "<fmt:message key="tour.lesson.remove.title"/>",
			        content: "<fmt:message key="tour.lesson.remove.content"/>",
			        placement: "left"
			      }, {
			        title: "<fmt:message key="tour.end.title"/>",
			        content: "<fmt:message key="tour.end.content"/>",
			        placement: "top",
			        orphan: true
			      }			      
			    ],
			  onEnd: resetCourses,
			  debug: true,
			  backdrop: false,
			  template: '<div class="popover" role="tooltip"> <div class="arrow"></div> <h3 class="popover-title"></h3> <div class="popover-content"></div> <div class="popover-navigation"> <div class="btn-group"> <button class="btn btn-sm btn-secondary" data-role="prev">&laquo; <fmt:message key="tour.prev"/></button> <button class="btn btn-sm btn-secondary" data-role="next"><fmt:message key="tour.next"/> &raquo;</button> </div>  <button class="btn btn-sm btn-secondary" data-role="end"><fmt:message key="tour.end.tour"/></button> </div>'		  
			});

		    tour.start(true);

		} else {
			tour.restart();
		}
	}
	
	function revealCourses(tour) { 
		$("body").removeClass("offcanvas-hidden");		
	}
	
	function hideCourses(tour) {
		$("body").addClass("offcanvas-hidden");
	} 
	
	function resetCourses(tour) {
		if ( courseIsHidden ) 
			hideCourses(tour);
		else
			revealCourses(tour);
	}
	
	

	