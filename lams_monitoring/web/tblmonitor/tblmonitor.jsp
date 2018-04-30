<%@ include file="/template/taglibs.jsp"%>

<!DOCTYPE html>
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
		var TOTAL_LESSON_LEARNERS_NUMBER = ${totalLearnersNumber};
	
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

			//open sequence tab by default
			$("#tab-link-sequence").click();

			//hide burning-questions if it's disabled in a tool
			if ("${traToolContentId}" != "") {
		        $.ajax({
		            url: '<lams:LAMSURL/>tool/lascrt11/tblmonitoring.do',
		            data: 'method=isBurningQuestionsEnabled&toolContentID=${traToolContentId}',
		            type: 'post',
		            success: function (json) {
			            if (!json.isBurningQuestionsEnabled) {
				            $("#burning-questions-tr").hide();
						}
		            }
		       	});
			}
		});

		function loadTab(method, toolContentID) {
			var url = "<c:url value='tblmonitor.do'/>";
			var options = {};
			
			if (method == "tra" || method == "traStudentChoices" || method == "burningQuestions") {
				toolContentID = "${traToolContentId}";
				url = "<lams:LAMSURL/>tool/lascrt11/tblmonitoring.do";
				
			} else if (method == "iraMcq" || method == "mcqStudentChoices") {
				toolContentID = "${iraToolContentId}";
				url = "<lams:LAMSURL/>tool/lamc11/tblmonitoring.do";
				
			} else if (method == "iraAssessment" || method == "iraAssessmentStudentChoices") {
				toolContentID = "${iraToolContentId}";
				url = "<lams:LAMSURL/>tool/laasse10/tblmonitoring.do";
	
			} else if (method == "aes" || method == "aesStudentChoices") {
				url = "<lams:LAMSURL/>tool/laasse10/tblmonitoring.do";
				options = {
					assessmentToolContentIds: "${assessmentToolContentIds}",
					assessmentActivityTitles: "${assessmentActivityTitles}"
				};

			} else if (method == "forum") {
				options = {
					activityId: "${forumActivityId}"
				};
				
			} else if (method == "peerreview") {
				toolContentID = "${peerreviewToolContentId}";
				url = "<lams:LAMSURL/>tool/laprev11/tblmonitoring.do";
			}

			// Merge additional options into existing options object
			$.extend( options, {
				method: method,
				lessonID: ${lesson.lessonId},
				toolContentID: toolContentID
			});

			$("#tblmonitor-tab-content").load(
				url,
				options
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

		function refresh() {
			$('.tab-link').parent().removeClass("active");
			$("#tab-link-sequence").parent().addClass("active");

			loadTab("sequence");
		}

        function switchToRegularMonitor() {
        		location.href = "<lams:LAMSURL/>home.do?method=monitorLesson&lessonID=${lesson.lessonId}";
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
				<c:set var="actionUrl"><c:url value='tblmonitor.do'/></c:set>
				<tr>
					<td id="menu-item-teams">
						<a class="tab-link" href="#nogo" data-method="teams">
							<i class="fa fa-users"></i>
							<fmt:message key="label.teams"/>
						</a>
					</td>
				</tr>
				
				<c:if test="${not empty isGatesAvailable}">
					<tr>
						<td id="menu-item-gates">
							<a class="tab-link" href="#nogo" data-method="gates">
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
							<a class="tab-link" href="#nogo" data-method="${iraMethodName}">
								<i class="fa fa-user"></i>
								<fmt:message key="label.ira"/>
							</a>
						</td>
					</tr>
				</c:if>

				<c:if test="${not empty isScratchieAvailable}">
					<tr>
						<td id="menu-item-tra">
							<a class="tab-link" href="#nogo" data-method="tra">
								<i class="fa fa-users"></i>
								<fmt:message key="label.tra"/>
							</a>
						</td>
					</tr>

					<tr id="burning-questions-tr">
						<td id="menu-item-burning-questions">
							<a class="tab-link" href="#nogo" data-method="burningQuestions">
								<i class="fa fa-question-circle"></i>
								<fmt:message key="label.burning.questions"/>
							</a>
						</td>
					</tr>
				</c:if>
				
				<c:if test="${not empty isForumAvailable}">
					<tr>
						<td id="menu-item-forum">
							<a class="tab-link" href="#nogo" data-method="forum">
								<i class="fa fa-comments"></i>
								<fmt:message key="label.forum"/>
							</a>
						</td>
					</tr>
				</c:if>
				
				<c:if test="${not empty isAeAvailable}">
					<tr>
						<td id="menu-item-aes">
							<a class="tab-link" href="#nogo" data-method="aes">
								<i class="fa fa-dashboard"></i>
								<fmt:message key="label.aes"/>
							</a>
						</td>
					</tr>
				</c:if>
				
				<c:if test="${not empty isPeerreviewAvailable}">
					<tr>
						<td id="menu-item-peerreview">
							<a class="tab-link" href="#nogo" data-method="peerreview">
								<i class="fa fa-users"></i>
								<fmt:message key="label.peer.review"/>
							</a>
						</td>
					</tr>
				</c:if>
				
				<tr>
					<td id="menu-item-sequence" class="active">
						<a id="tab-link-sequence" class="tab-link" href="#nogo" data-method="sequence">
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
				<div class="col-xs-6 col-md-4 col-lg-4">
					<a href="#">
						<i class="fa fa-bars fa-lg" id="menu-toggle-bars"></i>
					</a>
				</div>
						
				<div>
					<button id="regular-monitor-button" type="button" class="btn btn-sm btn-default pull-right" onclick="return switchToRegularMonitor();" style="margin-right: 10px;">
						<i class="fa fa-heartbeat" title="<fmt:message key="label.monitor" />"></i> <fmt:message key="label.switch.to.regular.monitor" />
					</button>
					
					<button id="refresh-button" type="button" class="btn btn-sm btn-default pull-right" onclick="Javascript: refresh(); return false;">
						<i class="fa fa-refresh"></i> <fmt:message key="button.refresh"/>
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
