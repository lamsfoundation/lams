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
				title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='tour.intro.title'/></spring:escapeBody>",
				content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='tour.intro.content'/></spring:escapeBody>",
				placement: "top",
				orphan: true
			  },
			  {
				element: ".tour-course-reveal",
				title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='organisations'/></spring:escapeBody>",
				content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='tour.course.reveal.content'/></spring:escapeBody>",
				placement: "bottom",
			  },
			  {
				element: ".tour-organisations",
				title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='organisations'/></spring:escapeBody>",
				content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='tour.courses.content'/></spring:escapeBody>",
				placement: "top",
				onShow: revealCourses
			  },
			  {
				element: ".tour-organisations-favorites",
				title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='organisations'/></spring:escapeBody>",
				content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='tour.favorites.content'/></spring:escapeBody>",
				placement: "bottom",
				onHidden: hideCourses
			  },
			  {
				element: ".tour-org-container",
				title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='lessons'/></spring:escapeBody>",
				content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='tour.lessons.content'/></spring:escapeBody>",
				placement: "top",
				backdrop: true
			  },
			  {
				element: ".tour-user-notifications",
				title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='index.emailnotifications'/></spring:escapeBody>",
				content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='tour.notification.content'/></spring:escapeBody>",
				placement: "left"
			  },
			  // The rest of the steps...
			  // (I've omitted them for brevity)
			  // ...
			  {
				title: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='tour.end.title'/></spring:escapeBody>",
				content: "<spring:escapeBody javaScriptEscape='true'><fmt:message key='tour.end.content'/></spring:escapeBody>",
				placement: "top",
				orphan: true
			  }
			]x,
			  onEnd: resetCourses,
			  debug: true,
			  backdrop: false,
			  template: '<div class="popover" role="tooltip"> <div class="arrow"></div> <h3 class="popover-title"></h3> <div class="popover-content"></div> <div class="popover-navigation"> <div class="btn-group"> <button class="btn btn-sm btn-default" data-role="prev">&laquo; <fmt:message key="tour.prev"/></button> <button class="btn btn-sm btn-default" data-role="next"><fmt:message key="tour.next"/> &raquo;</button> </div>  <button class="btn btn-sm btn-default" data-role="end"><fmt:message key="tour.end.tour"/></button> </div>'		  
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
	
	

	