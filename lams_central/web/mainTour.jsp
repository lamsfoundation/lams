	var tour = null;
	var courseIsHidden = null;
	function startTour() {
		courseIsHidden = $("body").hasClass("offcanvas-hidden");

		if ( tour == null ) {
			tour = new Tour({
			  name: "LAMSMainTour",
		      steps: [
		          {
			        title: "LAMS Tour",
			        content: "This short tour will show you the features of the main LAMS screen. You can stop the tour at any time by clicking End Tour, and restart it by clicking on the Tour button again.",
			        placement: "top",
			        orphan: true
			      }, {
			        element: ".tour-course-reveal",
			        title: "Courses",
			        content: "Click here to reveal or hide your courses.",
		        	placement: "bottom",
			      }, {
			        element: ".tour-organisations",
			        title: "Courses",
			        content: "Click on a course to make it the current course.",
		        	placement: "right",
		        	onShow: revealCourses
			      }, {
			        element: ".tour-organisations-favorites",
			        title: "Courses",
			        content: "Your favorite courses will appear at the top of the list.",
		        	placement: "right",
		        	onHidden: hideCourses
			      }, {
			        element: ".tour-org-container",
			        title: "Lessons",
			        content: "Lessons will appear here for the current course.",
		        	placement: "top",
		        	backdrop: true
			      }, {
			        element: ".tour-user-notifications",
			        title: "Notifications",
			        content: "Look at the notifications that you receive about your lessons.",
			        placement: "left"
			      }, {
			        element: ".tour-index-author",
			        title: "Authoring",
			        content: "Create or modify the learning designs used for lessons.",
			        placement: "left"
			      }, {
			        element: ".tour-user-profile",
			        title: "User Profile",
			        content: "Update your user settings.",
			        placement: "left"
			      }, {
			        element: ".tour-favorite-organisation",
			        title: "Course Name",
			        content: "Current course name. Click on the star to add this course to your favourites in the course list.",
			        placement: "right"
			      }, {
			        element: ".tour-add-lesson",
			        title: "Add Lesson",
			        content: "Create a new lesson for the course, or a course subgroup, depending on which button is used.",
			        placement: "left"
			      }, {
			        element: ".tour-add-single-lesson",
			        title: "Add Lesson",
			        content: "Use the down arrow to select an activity and create a lesson with just this single activity.",
			        placement: "left"
			      }, {
			        element: ".tour-index-coursegradebook-learner",
			        title: "Course Gradebook",
			        content: "View your marks for lessons in this course.",
			        placement: "left"
			      }, {
			        element: ".tour-more-options",
			        title: "More Options",
			        content: "General options for managing a course such as notifications, subgroups, gradebook. The contents of this menu will vary depending on your responsibilities.",
			        placement: "left"
			      }, {
			        element: ".tour-index-monitor",
			        title: "Lesson Monitoring",
			        content: "The monitoring screen shows you where the learners are in the lesson and to see the learner\'s contributions",
			        placement: "left"			        
			      }, {
			        element: ".tour-index-emailnotifications",
			        title: "Lesson Notifications",
			        content: "Send notifications to lesson learners.",
			        placement: "left"
			      }, {
			        element: ".tour-index-coursegradebookmonitor",
			        title: "Lesson Gradebook",
			        content: "Review learner\'s marks and the length of time taken in the lesson.",
			        placement: "left"
			      }, {
			        element: ".tour-index-conditions",
			        title: "Lesson Conditions",
			        content: "Set up conditions for starting or ending the lesson.",
			        placement: "left"
			      }, {
			        element: ".tour-index-remove-lesson",
			        title: "Remove Lesson",
			        content: "Delete the lesson completely. This is permanent - it cannot be undone.",
			        placement: "left"
			      }, {
			        element: ".tour-sorting",
			        title: "Lesson Sorting",
			        content: "Turn on or off lesson sorting.",
			        placement: "left"
			      }, {
			        title: "End Of Tour",
			        content: "Thank you for taking the tour. To restart the tour, click the Tour button again.",
			        placement: "top",
			        orphan: true
			      }			      
			    ],
			  onEnd: resetCourses,
			  debug: true,
			  backdrop: false,
			});

		    tour.init();
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
	