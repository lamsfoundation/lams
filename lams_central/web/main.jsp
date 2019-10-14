<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-function" prefix="fn"%>
<c:set var="lams"><lams:LAMSURL/></c:set>

<!DOCTYPE html>
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
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap-tourist.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.ui.touch-punch.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.slimscroll.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/main.js"></script>
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
				<fmt:message key="label.enable.lesson.sorting" var="SORTING_ENABLE_VAR"/>
				SORTING_ENABLE : '<c:out value="${SORTING_ENABLE_VAR}" />',
				<fmt:message key="label.disable.lesson.sorting" var="SORTING_DISABLE_VAR"/>
				SORTING_DISABLE : '<c:out value="${SORTING_DISABLE_VAR}" />',
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
	
		<%@ include file="mainTour.jsp" %>

	</script>
</lams:head>
<body <c:if test="${not empty activeOrgId}">class="offcanvas-hidden"</c:if>>

<!-- Offcanvas Bar -->
    <nav id="offcanvas" role="navigation">
        <div class="offcanvas-scroll-area">
        
			<div class="offcanvas-logo">
				<div class="logo">
				</div>
				<a class="offcanvas-toggle"><i class="icon-remove fa fa-bars fa-lg"></i></a>
			</div>
			
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

	<!-- header -->
	<div class="top-nav">
	
		<div class="offcanvas-toggle offcanvas-toggle-header">
			<i class="fa fa-bars tour-course-reveal"></i>
		</div>

		<ul class="nav navbar-nav navbar-right">
			<li>
				<a href="javascript:;" id="index-profile" class="user-profile dropdown-toggle tour-user-profile" data-toggle="dropdown" aria-expanded="false">
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
					<span class=" fa fa-angle-down"></span>
				</a>
						
				<ul class="dropdown-menu dropdown-usermenu pull-right">
					<li>
						<a href="#" onclick="javascript:showMyProfileDialog(); return false;">
							<i class="fa fa-user"></i> <fmt:message key="index.myprofile"/>
						</a>
					</li>
							
					<c:forEach var="adminlink" items="${adminLinks}">
						
						<c:choose>
		               		<c:when test="${adminlink.name == 'index.courseman'}">
		               			<c:set var="iconClass">fa-users</c:set>
		               		</c:when>
		               		<c:when test="${adminlink.name == 'index.sysadmin'}">
		               			<c:set var="iconClass">fa-gear</c:set>
		               		</c:when>
		               	</c:choose>
									
						<li>
							<a href="javascript:;" onclick="<c:out value="${adminlink.url}"/>">
								<span><i class="fa ${iconClass}"></i> <fmt:message key="${adminlink.name}"/></span>
							</a>
						</li>
					</c:forEach>
							                  
					<li>
						<a href="#nogo" id="logoutButton" onclick="javascript:closeAllChildren(); document.location.href='home/logout.do?'">
							<i class="fa fa-sign-out"></i> <fmt:message key="index.logout" />
						</a>
					</li>
				</ul>
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
						
				<li role="presentation">
					<a href="<c:out value='${headerlink.url}' />"  id="${headerlink.id}" class="tour-${headerlink.id}" title="${headerLinkTitle}">
						<i class="fa ${headerLinkIcon}"></i> 
						<span class="xs-hidden"><c:out value='${headerLinkName}'/></span>
					</a>
				</li>
			</c:forEach>

			<li role="presentation" class="dropdown">
				<a href="javascript:;" onclick="javascript:showPrivateNotificationsDialog();" class="dropdown-toggle info-number tour-user-notifications" data-toggle="dropdown" aria-expanded="false">
					<i class="fa fa-envelope-o"></i>
               		<span id="notificationsPendingCount" class="btn-default"></span>
				</a>
			</li>
					
			<li role="presentation" class="dropdown">
				<a href="javascript:;" id="index-tour" onclick="javascript:startTour();" class="dropdown-toggle info-number" data-toggle="dropdown" aria-expanded="false">
					<i class="fa fa-question-circle"></i>
					<span class="xs-hidden"><fmt:message key="label.tour"/></span>
				</a>
			</li>
					
		</ul>

	</div>
	<!-- /header -->

	<!-- content -->      
	<div class="content">
		<div id="messageCell">
			<%--
				<div id="message">Important annoucements might be posted here...</div>
			--%>
		</div>
		
		<div class="row no-gutter">
			<div class="col-xs-12">
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

</body>
</lams:html>
