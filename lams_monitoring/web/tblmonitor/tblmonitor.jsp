<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
    <title><fmt:message key="label.tbl.monitor"/></title>
	
	<lams:css/>
	<lams:css suffix="main"/>
	<lams:css webapp="monitoring" suffix="tblmonitor"/>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/progressBar.js"></script>
	<script>
		var TOTAL_LESSON_LEARNERS_NUMBER = ${totalLearnersNumber},
			LAMS_URL = '<lams:LAMSURL/>',
			TAB_REFRESH_INTERVAL = 20 * 1000, // refresh tab every 20 seconds,
			refreshIntervalReference = null, // stores reference to interval so it can be cancelled
			lastTabMethod = null, // these variables are needed for tab refresh
			lastTabToolContentID = null,
			tlbMonitorHorizontalScrollElement = null; // keeps ID of element which can be scrolled right,
												      // so we can re-scroll it after refresh
	
		$(document).ready(function(){
			<!-- Menu Toggle Script -->
			$("#menu-toggle-bars").click(function(e) {
				e.preventDefault();
				$("#wrapper").toggleClass("toggled");
			});

			//handler for all tab opener links
			$('.tab-link').on('click', function () {
				$('.tab-link').parent().removeClass("active");
				$(this).parent().addClass("active");

				loadTab($(this).data("method"));
			});

			//get #hash from URL and open according tab. Open sequence tab by default
			var hash = window.location.hash ? window.location.hash.substring(1) : "sequence"; 
			$("#tab-link-" + hash).click();

			//hide burning-questions if it's disabled in a tool
			if ("${traToolContentId}" != "") {
		        $.ajax({
		            url: '<lams:LAMSURL/>tool/lascrt11/tblmonitoring/isBurningQuestionsEnabled.do',
		            data: 'toolContentID=${traToolContentId}',
		            type: 'post',
		            success: function (json) {
			            if (!json.isBurningQuestionsEnabled) {
				            $("#burning-questions-tr").hide();
						}
		            }
		       	});
			}
			
			restartRefreshInterval();
		});

		function restartRefreshInterval() {
			if (refreshIntervalReference) {
				window.clearInterval(refreshIntervalReference);
			}
			
			// refresh automatically every X seconds
			refreshIntervalReference = window.setInterval(function(){
				refresh(true);
			}, TAB_REFRESH_INTERVAL);
		}

		function loadTab(method, toolContentID, autoRefresh) {
			if (!method && !toolContentID) {
				// tab was refreshed, get stored parameters
				method = lastTabMethod;

				if (autoRefresh && (method == 'burningQuestions' || method == 'aes' || method == 'sequence' || 
									method == 'gates' || method == 'iraAssessment'  || $('.modal').hasClass('in'))){
					// do not auto refresh pages with mostly static content
					// or if a modal dialog is open
					return;
				}	
				
				toolContentID = lastTabToolContentID;
			} else {
				// tab was changed, store its parameters
				lastTabMethod = method;
				lastTabToolContentID = toolContentID;
			}

			if (!method) {
				// we should not have this situation!
				return;
			}

			// no need to refresh soon if we are loading new tab anyway
			restartRefreshInterval();
			
			var url = "<lams:LAMSURL/>monitoring/tblmonitor/"
			var options = {};
			// check if tab declared an element scrollable
			// if so, store where it was scrolled to
			var horizontalScroll = tlbMonitorHorizontalScrollElement == null ? null : $(tlbMonitorHorizontalScrollElement).scrollLeft();
			tlbMonitorHorizontalScrollElement = null;
			
			if (method == "tra" || method == "traStudentChoices" || method == "burningQuestions") {
				toolContentID = "${traToolContentId}";
				url = "<lams:LAMSURL/>tool/lascrt11/tblmonitoring/";
				
			} else if (method == "iraMcq" || method == "mcqStudentChoices") {
				toolContentID = "${iraToolContentId}";
				url = "<lams:LAMSURL/>tool/lamc11/tblmonitoring/";
				
			} else if (method == "iraAssessment" || method == "iraAssessmentStudentChoices") {
				toolContentID = "${iraToolContentId}";
				url = "<lams:LAMSURL/>tool/laasse10/tblmonitoring/";
	
			} else if (method == "aes") {
				options = {
					aeToolContentIds: "${aeToolContentIds}",
					aeToolTypes: "${aeToolTypes}",
					aeActivityTitles: "${aeActivityTitles}"
				};

			} else if (method == "forum") {
				options = {
					activityId: "${forumActivityId}"
				};
				
			} else if (method == "peerreview") {
				toolContentID = "${peerreviewToolContentId}";
				url = "<lams:LAMSURL/>tool/laprev11/tblmonitoring/";
			}

			// Merge additional options into existing options object, convert method to url call
			url = url + method + ".do";
			$.extend( options, {
				lessonID: ${lesson.lessonId},
				toolContentID: toolContentID
			});

			
			$("#tblmonitor-tab-content").load(
				url,
				options,
				function(){
					if (horizontalScroll == null || horizontalScroll == 0 || tlbMonitorHorizontalScrollElement == null) {
						return;
					}
					// re-scroll to the same position horizontally
					$(tlbMonitorHorizontalScrollElement).scrollLeft(horizontalScroll);
				}
			);
		}

		function printTable() {
	        var title = document.title;
	        var divElements = document.getElementById('questions-data').outerHTML;
	        var printWindow = window.open("", "_blank", "");
	        //open the window
	        printWindow.document.open();
	        //write the html to the new window, link to css file
	        printWindow.document.write('<html><head><title>' + title + '</title>\n');
	        printWindow.document.write('<style type="text/css">\n');
            printWindow.document.write('body {font-family: "Helvetica Neue",Helvetica,Arial,sans-serif; -webkit-print-color-adjust: exact;}\n');
	        printWindow.document.write('.table > tbody > tr > td.success {background-color: #81c466;}\n');
	        printWindow.document.write('table { border: 1px solid #ddd; width: 100%; max-width: 100%; margin-bottom: 20px; background-color: transparent; border-spacing: 0; border-collapse: collapse; }\n');
	        printWindow.document.write('* { -webkit-box-sizing: border-box; -moz-box-sizing: border-box; box-sizing: border-box; font-size: 16px; }\n');
	        printWindow.document.write('.table-striped > tbody > tr:nth-of-type(2n+1) {background-color: #f9f9f9; }\n');
	        printWindow.document.write('.table-bordered > tbody > tr > td, .table-bordered > tbody > tr > th, .table-bordered > tfoot > tr > td, .table-bordered > tfoot > tr > th, .table-bordered > thead > tr > td, .table-bordered > thead > tr > th {  border: 1px solid #ddd;  }\n');
	        printWindow.document.write('a { color: #337ab7; text-decoration: none; }\n');
	        printWindow.document.write('.text-center { text-align: center; }\n');
	        printWindow.document.write('</style>\n');
	        printWindow.document.write('</head><body>\n');
	        printWindow.document.write(divElements);
	        printWindow.document.write('\n</body></html>');
	        printWindow.document.close();
	        printWindow.focus();
	        //The Timeout is ONLY to make Safari work, but it still works with FF, IE & Chrome.
	        setTimeout(function() {
	            printWindow.print();
	            printWindow.close();
	        }, 100);
		}

		function refresh(autoRefresh) {
			loadTab(null, null, autoRefresh);
		}

        function switchToRegularMonitor() {
        	location.href = "<lams:LAMSURL/>home/monitorLesson.do?lessonID=${lesson.lessonId}";
    	}
	</script>
</lams:head>

<body>
	<div id="wrapper">
	
		<!-- Offcanvas Bar -->
	    <nav id="sidebar-wrapper" role="navigation">
	        <div class="offcanvas-scroll-area">
	        
			<div class="offcanvas-logo">
				<div class="logo">
				</div>
			</div>
				
			<div class="offcanvas-header">
				<span class="courses-title ">
					${lesson.lessonName}
				</span>
			</div>

			<table class="tablesorter">
				<tr>
					<td id="menu-item-teams">
						<a id="tab-link-teams" class="tab-link" href="#teams" data-method="teams">
							<i class="fa fa-users"></i>
							<fmt:message key="label.teams"/>
						</a>
					</td>
				</tr>
				
				<c:if test="${not empty isGatesAvailable}">
					<tr>
						<td id="menu-item-gates">
							<a id="tab-link-gates" class="tab-link" href="#gates" data-method="gates">
								<i class="fa fa-sign-in"></i>
								<fmt:message key="label.gates"/>
							</a>
						</td>
					</tr>
				</c:if>
				
				<c:if test="${not empty isIraMcqAvailable || not empty isIraAssessmentAvailable}">
					<c:set var="iraMethodName">
						<c:choose>
							<c:when test="${not empty isIraMcqAvailable}">iraMcq</c:when>
							<c:otherwise>iraAssessment</c:otherwise>
						</c:choose>
					</c:set>
				
					<tr>
						<td id="menu-item-ira">
							<a id="tab-link-ira" class="tab-link" href="#ira" data-method="${iraMethodName}">
								<i class="fa fa-user"></i>
								<fmt:message key="label.ira"/>
							</a>
						</td>
					</tr>
				</c:if>

				<c:if test="${not empty isScratchieAvailable}">
					<tr>
						<td id="menu-item-tra">
							<a id="tab-link-tra" class="tab-link" href="#tra" data-method="tra">
								<i class="fa fa-users"></i>
								<fmt:message key="label.tra"/>
							</a>
						</td>
					</tr>

					<tr id="burning-questions-tr">
						<td id="menu-item-burning-questions">
							<a id="tab-link-burningQuestions" class="tab-link" href="#burningQuestions" data-method="burningQuestions">
								<i class="fa fa-question-circle"></i>
								<fmt:message key="label.burning.questions"/>
							</a>
						</td>
					</tr>
				</c:if>
				
				<c:if test="${not empty isForumAvailable}">
					<tr>
						<td id="menu-item-forum">
							<a id="tab-link-forum" class="tab-link" href="#forum" data-method="forum">
								<i class="fa fa-comments"></i>
								<fmt:message key="label.forum"/>
							</a>
						</td>
					</tr>
				</c:if>
				
				<c:if test="${not empty isAeAvailable}">
					<tr>
						<td id="menu-item-aes">
							<a id="tab-link-aes" class="tab-link" href="#aes" data-method="aes">
								<i class="fa fa-dashboard"></i>
								<fmt:message key="label.aes"/>
							</a>
						</td>
					</tr>
				</c:if>
				
				<c:if test="${not empty isPeerreviewAvailable}">
					<tr>
						<td id="menu-item-peerreview">
							<a id="tab-link-peerreview" class="tab-link" href="#peerreview" data-method="peerreview">
								<i class="fa fa-users"></i>
								<fmt:message key="label.peer.review"/>
							</a>
						</td>
					</tr>
				</c:if>
				
				<tr>
					<td id="menu-item-sequence" class="active">
						<a id="tab-link-sequence" class="tab-link" href="#sequence" data-method="sequence">
							<i class="fa fa-cubes"></i>
							<fmt:message key="label.sequence"/>
						</a>
					</td>
				</tr>
				
			</table>
	        </div>
	    </nav>
		<!-- /Offcanvas Bar -->
    
    		<!-- Page Content -->
		<div id="page-content-wrapper">

			<!-- Top bar -->
			<div class="top-nav">
				<div class="col-xs-5 col-md-3 col-lg-3">
					<a href="#">
						<i class="fa fa-bars fa-lg" id="menu-toggle-bars"></i>
					</a>
				</div>
						
				<div>
					<button id="regular-monitor-button" type="button" class="btn btn-sm btn-default pull-right" onclick="return switchToRegularMonitor();" style="margin-right: 10px;">
						<i class="fa fa-heartbeat" title="<fmt:message key="label.monitor" />"></i><span class="hidden-xs">  <fmt:message key="label.switch.to.regular.monitor" /></span>
					</button>
					
					<button id="refresh-button" type="button" class="btn btn-sm btn-default pull-right" onclick="Javascript: refresh(); return false;" style="margin-right: 10px;">
						<i class="fa fa-refresh"></i><span class="hidden-xs">  <fmt:message key="button.refresh"/></span>
					</button>
					
					<button id="timer-button" type="button" class="btn btn-sm btn-default pull-right"  onclick="javascript:openPopUp('<lams:LAMSURL/>monitoring/timer.jsp', '<fmt:message key="label.countdown.timer"/>', 648, 1152, true);return false;" style="margin-right: 10px;">
							<i class="fa fa-hourglass-half"></i><span class="hidden-xs"> <fmt:message key="label.countdown.timer"/></span>
					</button>		
				</div>
			</div>
			<!-- End top bar -->
			
			<div class="content" id="tblmonitor-tab-content">
				
			</div>
		</div>
		<!-- /#page-content-wrapper -->

	</div>
    <!-- /#wrapper -->

</body>
</lams:html>
