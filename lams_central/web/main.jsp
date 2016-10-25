<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-function" prefix="fn"%>
<%@ taglib uri="tags-tiles" prefix="tiles" %>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title><fmt:message key="title.lams"/> :: <fmt:message key="index.welcome" /></title>
	<link rel="icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	
	<lams:css style="main"/>
	<link rel="stylesheet" href="/lams/includes/font-awesome/css/font-awesome.css" type="text/css">
	<link rel="stylesheet" href="/lams/css/jquery.tablesorter.theme.bootstrap.css">
	<link rel="stylesheet" href="/lams/css/defaultHTML_learner.css" type="text/css">
	<link rel="stylesheet" href="/lams/css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	<link rel="stylesheet" href="/lams/css/index.css" type="text/css" media="screen">
	<link rel="stylesheet" href="/lams/css/main.css" type="text/css" media="screen">

	<script type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script type="text/javascript" src="loadVars.jsp"></script>
	<script type="text/javascript" src="includes/javascript/openUrls.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.tablesorter.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.tablesorter-pager.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.tablesorter-widgets.js"></script> 	
	<script type="text/javascript" src="includes/javascript/jquery.dialogextend.js"></script>	
	<script type="text/javascript" src="includes/javascript/dialog.js"></script>
	<script type="text/javascript" src="includes/javascript/groupDisplay.js"></script>	
	<script type="text/javascript" src="includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.ui.touch-punch.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.slimscroll.js"></script>
	<script type="text/javascript" src="includes/javascript/main.js"></script>
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
				MY_PROFILE : '<fmt:message key="index.myprofile" />',
			},
			stateId = 1, // TODO Figure out when stateId is required to be equal 3. Old version code: stateId = tabName == 'profile' ? 3 : 1;
			activeOrgId = -1; //not set for now

		$(document).ready(function(){
			initMainPage();
				
			<%-- If it's the user's first login, display a dialog asking if tutorial videos should be shown --%>
			<c:if test="${firstLogin}">
				<c:url var="disableAllTutorialVideosUrl" value="tutorial.do">
					<c:param name="method" value="disableAllTutorialVideos" />
				</c:url>
				if (!confirm("<fmt:message key='label.tutorial.disable.all' />")){
			 		$.get("${disableAllTutorialVideosUrl}");
				}
			</c:if>

			var $tablesorter = $(".tablesorter").tablesorter({
				theme: 'bootstrap',
				headerTemplate : '{content} {icon}',
				widgets: ["filter"],
			    widgetOptions : {
			        filter_columnFilters: false
			    },
			    widthFixed: true,
			    sortInitialOrder: 'desc',
	            sortList: [[1]],
	            headers: { 0: { sorter: false, filter: false} }
			});

			$.tablesorter.filter.bindSearch($tablesorter, $('#offcanvas-search-input') );

			$(".tablesorter").each(function() {
				$(this).tablesorterPager({
					savePages: false,
	                container: $(this).find(".ts-pager"),
	                output: '{startRow} to {endRow} ({totalRows})',
	                cssPageDisplay: '.pagedisplay',
	                cssPageSize: '.pagesize',
	                cssDisabled: 'disabled',
	                ajaxUrl : "<c:url value='/index.do'/>?dispatch=getOrgs&page={page}&size={size}&{sortList:column}&{filterList:fcol}",
					ajaxProcessing: function (data, table) {
				    	if (data && data.hasOwnProperty('rows')) {
				    		var rows = [],
				            json = {};
				    		
							for (i = 0; i < data.rows.length; i++){
								var orgData = data.rows[i];
								var orgId = orgData["id"];
								
								rows += '<tr>';

								rows += '<td style="display: none;">';
								rows += '</td>';
								
								rows += '<td id="org-row-' + orgId +'">';
								rows += 	'<a data-id="' + orgId + '" href="#" onClick="javascript:selectOrganisation(' + orgId + ')">';
								rows += 		orgData["name"];
								rows += 	'</a>';
								rows += '</td>';
								
								rows += '</tr>';
							}
				            
							json.total = data.total_rows;
							json.rows = $(rows);
							return json;
				    	}
					}
				})
			});

			$('.tablesorter').bind('filterEnd pagerComplete', function(event, data){
				
				//hide pager if total amount of courses is less than 10
	    		if (data.totalRows < 10) {
	    			$(".tablesorter-pager").hide();
		    	} else {
		    		$(".tablesorter-pager").show();
			    }

	    		//in case active course is not yet chosen by user, select the fist one from the list
			    if ((activeOrgId < 0) && (event.type == "pagerComplete") && (data.totalRows > 0)) {
			    	var firstOrgId = $('.tablesorter a').first().data("id");
					selectOrganisation(firstOrgId);
				}
			});

		});
	</script>
</lams:head>
<body > <!-- class="offcanvas-hidden" -->

<!-- Offcanvas Bar -->
    <nav id="offcanvas" role="navigation">
        <div class="offcanvas-scroll-area">
        
        <div class="offcanvas-collapse">
            <ul class="nav" id="offcanvas-menu">
                <li class="offcanvas-logo">
                    <div class="logo-element">
                        <img alt="image" class="offcanvas-logo" src="images/svg/lams_logo_white.svg">
                    </div>
        			<i class="icon-remove fa fa-close fa-lg offcanvas-toggle"></i>
                </li>
                <li class="offcanvas-header">
            	    <span class="courses-title "><i class="fa fa-table"></i>&nbsp;&nbsp;Courses</span>
        		</li> 
                <li>
                    <a href="#nogo">
                    	<span class="nav-label">Previous course asdadljl oijoiu s 1</span>
                    	<span class="label label-warning2 pull-right"><i class="fa fa-star"></i></span>
                    </a>
                </li>
                <li>
                    <a href="#nogo">
                    	<span class="nav-label">Previous course 2</span> 
                    	<span class="label label-warning2 pull-right"><i class="fa fa-star"></i></span>
                    </a>
                </li> 
                <li>
                    <a href="#nogo">
                    	<span class="nav-label">Previous course 3</span> 
                    	<span class="label label-warning2 pull-right"><i class="fa fa-star"></i></span>
                    </a>
                </li>
                
                <li class="form-group offcanvas-search">
						<input type="text" id="offcanvas-search-input" class="form-control input-sm" placeholder="Search for courses..."
								data-column="1" type="search">
                </li>
<!-- 
                <li class="active">
                    <a href="#"> Course B  </a>
                </li>
 -->

            </ul>
            
			<lams:TSTable numColumns="2">
			</lams:TSTable>

        </div>

        </div>
    </nav>
<!-- /Offcanvas Bar -->

<div id="page-wrapper">

	<!-- header -->
	<div class="top_nav">
		<div class="nav_menu">
			<nav>
				<div class="nav offcanvas-toggle-wrapper">
					<a class="offcanvas-toggle"><i class="fa fa-bars"></i></a>
				</div>

				<ul class="nav navbar-nav navbar-right">
					<li>
						<a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
	                  		<c:choose>
	                  			<c:when test="${not empty portraitUuid}">
	                  				<c:set var="portraitSrc">download/?uuid=${portraitUuid}&preferDownload=false</c:set>
	                  			</c:when>
	                  			<c:otherwise>
	                  				<c:set var="portraitSrc">images/css/john-doe-portrait.jpg</c:set>
	                  			</c:otherwise>
	                  		</c:choose>
			                <img src="${portraitSrc}" alt="">
			                  
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
								<a href="#nogo" onclick="javascript:closeAllChildren(); document.location.href='home.do?method=logout'">
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
							<a href="<c:out value='${headerlink.url}' />" title="${headerLinkTitle}">
								<i class="fa ${headerLinkIcon}"></i> 
								<span class="xs-hidden"><c:out value='${headerLinkName}'/></span>
							</a>
						</li>
					</c:forEach>
					
					<li role="presentation" class="dropdown">       
						<a href="javascript:;" onclick="javascript:showPrivateNotificationsDialog();" class="dropdown-toggle info-number" data-toggle="dropdown" aria-expanded="false">
							<i class="fa fa-envelope-o"></i>
                    		<span id="notificationsPendingCount"></span>
                    		
							<span class="xs-hidden">&nbsp;&nbsp;&nbsp;Notifications</span>
						</a>
					</li>
				</ul>
			</nav>
		</div>
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
		    	<div class="container-turned-off-lams">
		        	<div id="org-container" class="orgTab"></div>
		     	</div>
		  	</div>
		</div>
	
	<!-- TODO ADD this button	<div id="refreshButton" class="btn btn-default btn-sm pull-right" title="<fmt:message key="index.refresh.hint"/>"
				 onClick="javascript:refreshPrivateNotificationCount();loadOrganisation()">
				<fmt:message key="index.refresh" />
		</div>
	 -->
	</div>
	<!-- /content -->
	        
	<!-- footer -->
	<footer>
		<div class="">
			<p class="text-muted text-center">
				<fmt:message key="msg.LAMS.version" /> <%=Configuration.get(ConfigurationKeys.VERSION)%>
				<a href="<lams:LAMSURL/>/www/copyright.jsp" target='copyright' onClick="openCopyRight()">
					&copy; <fmt:message key="msg.LAMS.copyright.short" /> 
				</a>
			</p>
		</div>
		<div class="clearfix"></div>
	</footer>
	<!-- /footer -->

</div>

</body>
</lams:html>
