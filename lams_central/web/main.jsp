<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="title.lams"/> :: <fmt:message key="index.welcome" /></title>
	<link rel="icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	
	<lams:css/>
	<lams:css suffix="main"/>
	<link rel="stylesheet" href="/lams/css/jquery.tablesorter.theme.bootstrap.css">
	<link rel="stylesheet" href="/lams/css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	<link rel="stylesheet" href="/lams/css/bootstrap-tourist.min.css" type="text/css" media="screen">
	<link rel="stylesheet" href="/lams/css/datatables.css">
	<style>
	
.cards tbody tr {
   float: left;
   width: 26rem;
   margin: 0.5rem;
   border: 0.0625rem solid rgba(0, 0, 0, .125);
   border-radius: .25rem;
   box-shadow: 0.25rem 0.25rem 0.5rem rgba(0, 0, 0, 0.25);
}

.cards tbody td {
   display: block;
}

.cards thead {
   display: none;
}

.cards td:before {
   content: attr(data-label);
   position: relative;
   float: left;
   color: #808080;
   min-width: 4rem;
   margin-left: 0;
   margin-right: 1rem;
   text-align: left;   
}

tr.selected td:before {
   color: #CCC;
}

.table.cards td, .table.cards th {
    border-top: 0; /* Overwrite bootstrap rule */
}

.dataTables_filter {
	margin-top: 10px;
	float: left;
}

.lesson-image {
	min-height: 200px;
	background-image: url(http://localhost:8080/lams/css/images/trianglify.png);
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center center;
}
.user-monitor .lesson-image{
	background-image: linear-gradient( rgba(255, 255, 255, .5), rgba(255, 255, 255, 0.5) ), url(http://localhost:8080/lams/css/images/trianglify.png);
}

table:not(.cards):not(.user-monitor) .lesson-image {
	display: none;
}

/* display chart for list view */

table:not(.cards).user-monitor .lesson-image {
    position: absolute;
    right: 270px;
    padding: 0;
    background-image: none;
}

table:not(.cards).user-monitor tr {
	height: 100px !important;
}
table:not(.cards).user-monitor .chart-holder {
	padding-top: 0;
}

/*  */

.auxiliary-links-menu {
	float: right;
}
.auxiliary-links-menu button{
	background-color: rgba(0,0,0,.03);
}
.card-view-label{
	display: none;
}

.learners-count {
	float: left;
}
.cards .learners-count {
	display: block;
}

.progress-bar span {
	color: #000;
    position: absolute;
    margin-left: 6px;
}
.progress {
	height: 1.3rem;
}
.cards .progress {
	margin-top: 10px;
}
table:not(.cards) .progress {
	width: 50%;
}

.cards:not(.user-monitor) .lesson-name {
	min-height: 60px;
}
.user-monitor:not(.cards) .lesson-name {
	padding-bottom: 40px;
}
.user-monitor:not(.cards) .learners-count {
	position: absolute;
    left: 15px;
    margin-top: 20px;
}

.cards td.row-reorder, .cards .auxiliary-links {
	display: none;
}

td.row-reorder {
	width: 20px;
	padding-left: .25rem
}

.chart-holder {
    width: 200px;
    float: right;
    clear: both;
    padding-top: 70px;
}

td.buttons-td {
    height: 20px;
}

	</style>

	<script type="text/javascript" src="${lams}includes/javascript/getSysInfo.js"></script>
	<script type="text/javascript" src="${lams}loadVars.jsp"></script>
	<script type="text/javascript" src="${lams}includes/javascript/openUrls.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script> 	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.dialogextend.js"></script>	
	<script type="text/javascript" src="${lams}includes/javascript/dialog.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/popper.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap-tourist.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.ui.touch-punch.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/datatables.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.slimscroll.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/main.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/chart.min.js"></script>
	<script type="text/javascript">
		var LAMS_URL = '<lams:LAMSURL/>',	
			decoderDiv = $('<div />'),
			LABELS = {
				<fmt:message key="index.emailnotifications" var="EMAIL_NOTIFICATIONS_TITLE_VAR"/>
				EMAIL_NOTIFICATIONS_TITLE : '<c:out value="${EMAIL_NOTIFICATIONS_TITLE_VAR}" />',
				<fmt:message key="index.remove.lesson.confirm1" var="REMOVE_LESSON_CONFIRM1_VAR"/>
				REMOVE_LESSON_CONFIRM1 : decoderDiv.html('<c:out value="${REMOVE_LESSON_CONFIRM1_VAR}" />').text(),
				<fmt:message key="index.remove.lesson.confirm2" var="REMOVE_LESSON_CONFIRM2_VAR"/>
				REMOVE_LESSON_CONFIRM2 : decoderDiv.html('<c:out value="${REMOVE_LESSON_CONFIRM2_VAR}" />').text(),
				<fmt:message key="index.addlesson" var="ADD_LESSON_TITLE_VAR"/>
				ADD_LESSON_TITLE : '<c:out value="${ADD_LESSON_TITLE_VAR}" />',
				<fmt:message key="index.single.activity.lesson.title" var="SINGLE_ACTIVITY_LESSON_TITLE_VAR"/>
				SINGLE_ACTIVITY_LESSON_TITLE : '<c:out value="${SINGLE_ACTIVITY_LESSON_TITLE_VAR}" />',
				<fmt:message key="index.gradebook.course.title" var="GRADEBOOK_COURSE_TITLE_VAR"/>
				GRADEBOOK_COURSE_TITLE : '<c:out value="${GRADEBOOK_COURSE_TITLE_VAR}" />',
				<fmt:message key="index.gradebook.lesson.title" var="GRADEBOOK_LESSON_TITLE_VAR"/>
				GRADEBOOK_LESSON_TITLE : '<c:out value="${GRADEBOOK_LESSON_TITLE_VAR}" />',
				<fmt:message key="index.gradebook.learner.title" var="GRADEBOOK_LEARNER_TITLE_VAR"/>
				GRADEBOOK_LEARNER_TITLE : '<c:out value="${GRADEBOOK_LEARNER_TITLE_VAR}" />',
				<fmt:message key="index.conditions.title" var="CONDITIONS_TITLE_VAR"/>
				CONDITIONS_TITLE : '<c:out value="${CONDITIONS_TITLE_VAR}" />',
				<fmt:message key="index.search.lesson.title" var="SEARCH_LESSON_TITLE_VAR"/>
				SEARCH_LESSON_TITLE : '<c:out value="${SEARCH_LESSON_TITLE_VAR}" />',
				<fmt:message key="index.course.groups.title" var="COURSE_GROUPS_TITLE_VAR"/>
				COURSE_GROUPS_TITLE : '<c:out value="${COURSE_GROUPS_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.navigate.away.confirm" var="NAVIGATE_AWAY_CONFIRM_VAR"/>
				NAVIGATE_AWAY_CONFIRM : decoderDiv.html('<c:out value="${NAVIGATE_AWAY_CONFIRM_VAR}" />').text(),
				<fmt:message key="authoring.fla.page.title" var="AUTHORING_TITLE_VAR"/>
				AUTHORING_TITLE : '<c:out value="${AUTHORING_TITLE_VAR}" />',
				<fmt:message key="index.monitoring.title" var="MONITORING_TITLE_VAR"/>
				MONITORING_TITLE : '<c:out value="${MONITORING_TITLE_VAR}" />',
				<fmt:message key="index.kumalive.rubric" var="KUMALIVE_RUBRICS_TITLE_VAR"/>
				KUMALIVE_RUBRICS_TITLE : '<c:out value="${KUMALIVE_RUBRICS_TITLE_VAR}" />',
				<fmt:message key="index.kumalive.report" var="KUMALIVE_REPORT_TITLE_VAR"/>
				KUMALIVE_REPORT_TITLE : '<c:out value="${KUMALIVE_REPORT_TITLE_VAR}" />',
				<fmt:message key="label.private.notifications.title" var="PRIVATE_NOTIFICATIONS_TITLE_VAR"/>
				PRIVATE_NOTIFICATIONS_TITLE : '<c:out value="${PRIVATE_NOTIFICATIONS_TITLE_VAR}" />',
				<fmt:message key="label.private.notifications.messages" var="PRIVATE_NOTIFICATIONS_MESSAGES_VAR"/>
				PRIVATE_NOTIFICATIONS_MESSAGES : '<c:out value="${PRIVATE_NOTIFICATIONS_MESSAGES_VAR}" />',
				<fmt:message key="label.private.notifications.read" var="PRIVATE_NOTIFICATIONS_READ_VAR"/>
				PRIVATE_NOTIFICATIONS_READ : '<c:out value="${PRIVATE_NOTIFICATIONS_READ_VAR}" />',
				<fmt:message key="label.private.notifications.read.hint" var="PRIVATE_NOTIFICATIONS_READ_HINT_VAR"/>
				PRIVATE_NOTIFICATIONS_READ_HINT : '<c:out value="${PRIVATE_NOTIFICATIONS_READ_HINT_VAR}" />',
				<fmt:message key="label.private.notifications.read.all.hint" var="PRIVATE_NOTIFICATIONS_READ_ALL_HINT_VAR"/>
				PRIVATE_NOTIFICATIONS_READ_ALL_HINT : '<c:out value="${PRIVATE_NOTIFICATIONS_READ_ALL_HINT_VAR}" />',
				<fmt:message key="index.myprofile" var="MY_PROFILE_VAR"/>
				MY_PROFILE : '<c:out value="${MY_PROFILE_VAR}" />',
				<fmt:message key="label.remove.org.favorite" var="REMOVE_ORG_FAVORITE_VAR"/>
				REMOVE_ORG_FAVORITE : '<c:out value="${REMOVE_ORG_FAVORITE_VAR}" />',
				<fmt:message key="label.mark.org.favorite" var="MARK_ORG_FAVORITE_VAR"/>
				MARK_ORG_FAVORITE : '<c:out value="${MARK_ORG_FAVORITE_VAR}" />',
				<fmt:message key="label.remove.org.favorite" var="REMOVE_LESSON_FAVORITE_VAR"/>
				REMOVE_LESSON_FAVORITE : '<c:out value="${REMOVE_LESSON_FAVORITE_VAR}" />',
				<fmt:message key="label.mark.org.favorite" var="MARK_LESSON_FAVORITE_VAR"/>
				MARK_LESSON_FAVORITE : '<c:out value="${MARK_LESSON_FAVORITE_VAR}" />',
				<fmt:message key="index.kumalive" var="KUMALIVE_TITLE_VAR"/>
				KUMALIVE_TITLE : '<c:out value="${KUMALIVE_TITLE_VAR}" />',
				<fmt:message key="index.outcome.manage" var="OUTCOME_MANAGE_TITLE_VAR"/>
				OUTCOME_MANAGE_TITLE : '<c:out value="${OUTCOME_MANAGE_TITLE_VAR}" />',
				<fmt:message key="outcome.manage.title" var="OUTCOME_COURSE_MANAGE_TITLE_VAR"/>
				OUTCOME_COURSE_MANAGE_TITLE : '<c:out value="${OUTCOME_COURSE_MANAGE_TITLE_VAR}" />'
			},
			activeOrgId = <c:choose><c:when test="${empty activeOrgId}">null</c:when><c:otherwise>${activeOrgId}</c:otherwise></c:choose>;

		$(document).ready(function(){
			<%-- If it's the user's first login, show tour --%>
			<c:if test="${firstLogin}">
				startTour();
			</c:if>

			<c:if test="${showTimezoneWarning}"> 
		    var current_date = new Date( );
		    var client_gmt_offset_minutes = current_date.getTimezoneOffset( );
		    $('#offset').html( client_gmt_offset_minutes / 60 );
		    var lams_gmt_offset_minutes = ( <lams:user property="timeZone.rawOffset"/> + <lams:user property="timeZone.DSTSavings"/> ) / 60000;
		    if ( client_gmt_offset_minutes != -lams_gmt_offset_minutes ) {
			    $('#timezoneWarning').html( '<BR/><fmt:message key="label.timezone.warning"/>');
				<c:if test="${showTimezoneWarningPopup}"> 
	 			    $.blockUI({ 
			            message: '<div class="growlUI"><h2><fmt:message key="label.timezone.warning"/></h2></div>', 
	 		            fadeIn: 700, 
			            fadeOut: 700, 
			            width: 500,
			            timeout: 8000, 
			            showOverlay: false, 
			            centerY: false, 
			            css: { 
			                backgroundColor: '#000', 
			                '-webkit-border-radius': '10px', 
			                '-moz-border-radius': '10px', 
			                opacity: .6, 
			                color: '#fff' 
			            } 
			        }); 
	 			</c:if>
  		    } 
		    </c:if>
		});

		function orderDatatable(button, tableId){
			var selectedOrderId = $(button).val();

			//hide row-reorder if non-default order is selected
			if (selectedOrderId == 0) {
				$("table:not(.cards) td.row-reorder", $("#" + tableId)).show();
			} else {
				$("table:not(.cards) td.row-reorder", $("#" + tableId)).hide();
			}
			
			var orderDirection = selectedOrderId == 0 ? 'asc':'desc';
			$("#" + tableId).DataTable().order([selectedOrderId, orderDirection]).draw();
		}

		function initDataTables() {
		    $('.lessons-table').each(function(i, obj) {
		    	var lessonsTable = $(this);
			    
		    	lessonsTable.DataTable({
			    	rowReorder: {
			            selector: 'td.row-reorder'
			        },
		         	'dom':
		            	"<'row'<'col-sm-12'<'float-xl-right ml-2'B>f>>" +
		            	"<'row'<'col-sm-12'tr>>" +
		            	"<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7'p>>",
		         	'buttons': [
			         {
			            'text': '<select class="custom-select" onchange="orderDatatable(this, \'' + lessonsTable.attr('id') + '\')">' +
			            	 		' <option value="0" selected>Default order</option>' +
			            	 		' <option value="1">Date</option>' +
			            	 		' <option value="2">In progress</option>' +
			            	 		' <option value="3">Completed</option>' +
			            	 		<c:if test="${isFavouriteLessonEnabled}">
			            	 		' <option value="4">Stared</option>' +
			            	 		</c:if>
			            		'</select>',
			            'action': function (e, dt, node) {
			            	//dt.table().order([1, 'asc']).draw(); 
			            	//console.log("aaa" + e.type);
			            	//$(dt.table().node()).data("table-id", dt.table().node().id);
			            },
			            'className': 'btn-sm btn-light mr-3',
			            'attr': {
			               'title': 'Change views',
			            }
			        }, 
			        {
		            'text': '<i class="fa fa-table fa-fw" aria-hidden="true"></i> <span class="card-view-label">Card view</span><span class="list-view-label">List view</span>',
		            'action': function (e, dt, node) {
			            var $table = $(dt.table().node());
			            var isUserMonitor = eval(lessonsTable.data("is-user-monitor"))
			            
			        	//add "cards" class
			        	var toggleClasses = isUserMonitor ? 'cards table-striped table-hover' : 'cards table-hover';
		                $table.toggleClass(toggleClasses);
		                //toggle button's icon
		                $('.fa', node).toggleClass(['fa-table', 'fa-id-badge']);
			            $('.list-view-label,.card-view-label', node).toggle();
			            
			            if ($table.hasClass('cards')) {
			               // Create an array of labels containing all table headers
			               var labels = [];
			               $('thead th', $table).each(function () {
			                  labels.push($(this).text());
			               });

			               // Add data-label attribute to each cell
			               $('tbody tr', $table).each(function () {
			                  $(this).find('td').each(function (column) {
			                     $(this).attr('data-label', labels[column]);
			                  });
			               });

			               var max = 0;
			               $('tbody tr', $table).each(function () {
			                  max = Math.max($(this).height(), max);
			               }).height(max);

			            } else {
			               // Remove data-label attribute from each cell
			               $('tbody td', $table).each(function () {
			                  $(this).removeAttr('data-label');
			               });

			               $('tbody tr', $table).each(function () {
			                  $(this).height('auto');
			               });
			        	}

			            //toggle buttons
			            $(".auxiliary-links-menu", $table).toggle();

			            if (!isUserMonitor) {
				            $("td.lesson-name", lessonsTable).toggleClass(" d-flex justify-content-between");
				        }

		                dt.draw('page');
		            },
		            'className': 'btn-sm btn-light',
		            'attr': {
		               'title': 'Change views',
		            }
		         }],
		         select: false,
		         paging: false,
		         info: false,
		         'columns': [
		        	{"visible": false},
		        	{"visible": false},
		        	{"visible": false},
		        	{"visible": false},
		        	//star lesson feature
		        	{"visible": true},//we need to mark it as visible:true, but set manually display:none to be able to access it from toggleFavoriteLesson() method
		        	//lesson image
		            {
		               'orderable': false,
		               'className': 'text-center'
		            },
		            {
		               'data': 'name'
		            },
		            {
		               'data': 'extn',
		               "visible": lessonsTable.data("is-user-monitor")
		            },
		        	//row-reordering feature
		        	{"width": "20px", "visible": lessonsTable.data("row-reordering-enabled")},
		         ]
				})
				.on( 'row-reorder', function ( e, diff, edit ) {
					//store new lesson order in DB
					var orgId = lessonsTable.data("orgid");
					var lessonIds = $("tbody tr", lessonsTable).map(function() { 
					    return $(this).data("lessonid"); 
					}).get().join(',');
					$.ajax({
						url : "servlet/saveLessonOrder",
						data : {
							orgId : orgId,
							ids : lessonIds
						},
						error : function() {
							alert("There was an error trying to save new lesson order.");
						}
					});
			    });
			});

		    $(".chart-area").each(function() {
			    var chart = $(this);
				new Chart(chart.get(0).getContext('2d'), {
					type: 'doughnut',
					data: {
						datasets: [{
							data: [
								chart.data("count-completed-learners"),
								chart.data("count-attempted-learners"),
								chart.data("count-not-started-learners")
							],
							backgroundColor: [
								'rgb(199, 234, 70)',//green
								'rgb(252, 226, 5)',//yellow
								'rgb(255, 146, 140)'//red
							],
							label: 'Dataset 1'
						}],
						labels: [
							'Completed',
							'Attempted',
							'Not Started'
						]
					},
					options: {
						responsive: true,
						legend: {
							position: 'left',
						}
					}
				});
			});
		}
	
		<%@ include file="mainTour.jsp" %>

	</script>
</lams:head>
<body <c:if test="${not empty activeOrgId}">class="offcanvas-hidden"</c:if>>

	<!-- header -->
	<div class="top-nav navbar navbar-light bg-light sticky-top">
	

		<button class="navbar-toggler offcanvas-toggle" type="button">
	    	<span class="navbar-toggler-icon"></span>
	    </button>

		<div id="navbar-logo" class="ml-4 mr-auto"></div>

		<ul class="nav navbar-nav2 navbar-right">
			<li class="nav-item">
				<a href="#" onclick="javascript:startTour();" class="nav-link">
					<i class="fa fa-question-circle"></i>
					<span class="xs-hidden"><fmt:message key="label.tour"/></span>
				</a>
			</li>

			<li class="nav-item">
				<a href="#" onclick="javascript:showPrivateNotificationsDialog();" class="tour-user-notifications nav-link">
					<i class="fa fa-envelope-o"></i>
               		<span id="notificationsPendingCount" class="btn-light"></span>
				</a>
			</li>
					
			<c:forEach var="headerlink" items="${headerLinks}">
				<c:choose>
					<c:when test="${fn:startsWith(headerlink.name, 'index')}">
						<c:set var="headerLinkName"><fmt:message key="${headerlink.name}" /></c:set>
						<c:set var="headerLinkIcon">fa-edit</c:set>
					</c:when>
							
					<c:otherwise>							
						<c:set var="headerLinkName"><c:out value="${headerlink.name}" /></c:set>
						<c:set var="headerLinkIcon">fa-at</c:set>
					</c:otherwise>
				</c:choose>
						
				<c:choose>
					<c:when test="${fn:length(headerLinkName) > 12}">
						<c:set var="headerLinkTitle" value="${headerLinkName}"/>
						<c:set var="headerLinkName" value="${fn:substring(headerLinkName, 0, 12-2)}..."/>
					</c:when>
					<c:otherwise>
						<c:set var="headerLinkName" value="${headerLinkName}"/>
					</c:otherwise>
				</c:choose>
						
				<li class="nav-item">
					<a href="<c:out value='${headerlink.url}' />" id="${headerlink.id}" class="tour-${headerlink.id} nav-link" title="${headerLinkTitle}">
						<i class="fa ${headerLinkIcon}"></i> 
						<span class="xs-hidden"><c:out value='${headerLinkName}'/></span>
					</a>
				</li>
			</c:forEach>
		
			<li class="dropdown nav-item">
				<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	           		<c:choose>
	           			<c:when test="${not empty portraitUuid}">
	           				<c:set var="portraitSrc">download/?uuid=${portraitUuid}&preferDownload=false&version=4</c:set>
	           			</c:when>
	           			<c:otherwise>
	           				<c:set var="portraitSrc">images/css/john-doe-portrait.jpg</c:set>
	           			</c:otherwise>
	           		</c:choose>
		            <img class="portrait-sm portrait-round" src="${portraitSrc}" alt="">
			                  
					<c:set var="firstName">
						<lams:user property="firstName" />
					</c:set>
					<c:set var="lastName">
						 <lams:user property="lastName" />
					</c:set>
					<span class="xs-hidden">
						<c:out value="${firstName}" escapeXml="true"/>&nbsp;<c:out value="${lastName}" escapeXml="true"/>								
					</span>
				</a>
				
				<div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
					<a href="#" onclick="javascript:showMyProfileDialog(); return false;" class="dropdown-item">
						<i class="fa fa-user"></i> <fmt:message key="index.myprofile"/>
					</a>
					
					<c:if test="${showQbCollectionsLink}">
						<a href="#" onclick="javascript:openQbCollections(); return false;" class="dropdown-item">
							<i class="fa fa-question"></i> <fmt:message key="index.qb.collections"/>
						</a>
					</c:if>
					
					<c:forEach var="adminlink" items="${adminLinks}">						
						<c:choose>
		               		<c:when test="${adminlink.name == 'index.courseman'}">
		               			<c:set var="iconClass">fa-users</c:set>
		               		</c:when>
		               		<c:when test="${adminlink.name == 'index.sysadmin'}">
		               			<c:set var="iconClass">fa-gear</c:set>
		               		</c:when>
		               	</c:choose>

						<a href="javascript:;" onclick="<c:out value="${adminlink.url}"/>" class="dropdown-item">
							<span><i class="fa ${iconClass}"></i> <fmt:message key="${adminlink.name}"/></span>
						</a>
					</c:forEach>
					
					<div class="dropdown-divider"></div>            
					
					<a href="#nogo" id="logoutButton" onclick="javascript:closeAllChildren(); document.location.href='home/logout.do?'" class="dropdown-item">
						<i class="fa fa-sign-out"></i> <fmt:message key="index.logout" />
					</a>
				</div>
			</li>
		</ul>
	</div>
	<!-- /header -->

<!-- Offcanvas Bar -->
    <nav id="offcanvas" role="navigation" class="bg-dark">
        <div class="offcanvas-scroll-area">
			<div class="offcanvas-header">
				<span class="courses-title ">
					<i class="fa fa-table"></i>&nbsp;<fmt:message key="organisations" />
				</span>
			</div>
        
			<%@ include file="favoriteOrganisations.jsp"%>
            
            <c:if test="${isCourseSearchOn}">
				<div class="form-group offcanvas-search">
					<input type="text" id="offcanvas-search-input" class="form-control input-sm" placeholder="<fmt:message key="label.search.for.courses" />..."
							data-column="1" type="search">
				</div>
			</c:if>
            
            <div class="tour-organisations">
				<lams:TSTable numColumns="2">
				</lams:TSTable>
			</div>
			
        </div>
    </nav>
<!-- /Offcanvas Bar -->

<div id="page-wrapper">
	<!-- content -->      
	<div class="content">
		<div id="messageCell"></div>
		
		<div class="row no-gutter">
			<div class="col-12">
	        	<div id="org-container" class="tour-org-container"></div>
			</div>
		</div>
	</div>
	<!-- /content -->
	        
	<!-- footer -->
	<footer>
		<div class="">
			<p class="text-muted text-center">
				<a href="<lams:LAMSURL/>/www/copyright.jsp" target='copyright' onClick="openCopyRight()">
					&copy; <fmt:message key="msg.LAMS.copyright.short" /> 
				</a>
				<span class="text-danger text-center" id="timezoneWarning"></span>
			</p>
		</div>
		<div class="clearfix"></div>
	</footer>
	<!-- /footer -->

</div>

<csrf:form id="csrf-form" method="post" action=""></csrf:form>

</body>
</lams:html>
