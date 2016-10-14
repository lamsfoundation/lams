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
	<c:choose>
	<c:when test="${page_direction == 'RTL'}">
		<title><fmt:message key="index.welcome" /> :: <fmt:message key="title.lams"/></title>
	</c:when>
	<c:otherwise>
		<title><fmt:message key="title.lams"/> :: <fmt:message key="index.welcome" /></title>
	</c:otherwise>
	</c:choose>
	
	<lams:css style="main" />
	<link href="/lams/includes/font-awesome/css/font-awesome.css" rel="stylesheet" type="text/css">
	<link href="/lams/css/defaultHTML_learner.css" rel="stylesheet" type="text/css">
	
	<link rel="icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/index.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">

	<script type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script type="text/javascript" src="loadVars.jsp"></script>
	<script type="text/javascript" src="includes/javascript/openUrls.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.dialogextend.js"></script>	
	<script type="text/javascript" src="includes/javascript/dialog.js"></script>
	<script type="text/javascript" src="includes/javascript/groupDisplay.js"></script>	
	<script type="text/javascript" src="/lams/includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/jquery.ui.touch-punch.js"></script>

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
				},
				
				tabName = '${tab}',
				stateId = tabName == 'profile' ? 3 : 1;

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
			});

	</script>
</lams:head>
<body>

<div id="page-mycourses">
	<div id="header-my-courses">
		<div id="nav-right">
			<div class="nav-box-right">
				<c:choose>
					<c:when test="${empty tab}">
						<div class="tab-left-selected"></div>
						<div class="tab-middle-selected"><a class="tab-middle-link-selected" style="border:0;" href="index.do"><fmt:message key="index.mycourses"/> </a></div>
						<div class="tab-right-selected"></div>
					</c:when>
					<c:otherwise>
						<div class="tab-left"></div>
						<div class="tab-middle"><a class="tab-middle-link" style="border:0;" href="index.do"><fmt:message key="index.mycourses"/> </a></div>
						<div class="tab-right"></div>
					</c:otherwise>
				</c:choose>
			</div>
			<c:forEach var="headerlink" items="${headerLinks}">
			<div class="nav-box-right">
				
				<c:set var="tabLeft" value="tab-left"/>
				<c:set var="tabMiddle" value="tab-middle"/>
				<c:set var="tabRight" value="tab-right"/>	
				<c:set var="highlight" value="false" />
				<c:if test="${tab eq 'profile'}">
					<c:if test="${headerlink.name eq 'index.myprofile'}">
						<c:set var="tabLeft" value="tab-left-selected"/>
						<c:set var="tabMiddle" value="tab-middle-selected"/>
						<c:set var="tabRight" value="tab-right-selected"/>	
						<c:set var="highlight" value="false" />
					</c:if>
				</c:if>
				<c:if test="${tab eq 'community'}">
					<c:if test="${headerlink.name eq 'index.community'}">
						<c:set var="tabLeft" value="tab-left-selected"/>
						<c:set var="tabMiddle" value="tab-middle-selected"/>
						<c:set var="tabRight" value="tab-right-selected"/>	
						<c:set var="highlight" value="false" />
					</c:if>
				</c:if>
				<c:if test="${headerlink.name eq 'index.author'}">
					<c:set var="tabLeft" value="tab-left-highlight"/>
					<c:set var="tabMiddle" value="tab-middle-highlight"/>
					<c:set var="tabRight" value="tab-right-highlight"/>	
					<c:set var="highlight" value="true" />					
				</c:if>
				<c:if test="${headerlink.name eq 'index.planner'}">
					<c:set var="tabLeft" value="tab-left-highlight"/>
					<c:set var="tabMiddle" value="tab-middle-highlight"/>
					<c:set var="tabRight" value="tab-right-highlight"/>	
					<c:set var="highlight" value="true" />					
				</c:if>
	
				<div class="${tabLeft}"></div>
				<div class="${tabMiddle}">
						<c:choose>
							<c:when test="${fn:startsWith(headerlink.name,'index')}">
								<lams:TabName url="${headerlink.url}" highlight="${highlight}"><fmt:message key="${headerlink.name}" /></lams:TabName>
							</c:when>
							<c:otherwise>
								<lams:TabName url="${headerlink.url}" highlight="${highlight}"><c:out value="${headerlink.name}" /></lams:TabName>						
							</c:otherwise>
						</c:choose>
				</div>
				<div class="${tabRight}"></div>
			</div>
			</c:forEach>
		</div>
	</div>
	<div id="content-main">
		<table>
			<tr>
				<c:if test="${not empty portraitUuid}">
					<td>
						<img class="img-border" src="download/?uuid=${portraitUuid}&preferDownload=false" />
					</td>
				</c:if>
				<td>
					<fmt:message key="index.welcome" />
					<c:set var="firstName">
						<lams:user property="firstName" />
					</c:set>
					<c:out value="${firstName}" escapeXml="true"/>
				</td>
				
				<td id="messageCell">
				<%--
					<div id="message">Important annoucements might be posted here...</div>
				--%>
				</td>
				
				<td class="linksCell">
					<c:if test="${not empty adminLinks}">
						<c:forEach var="adminlink" items="${adminLinks}">
							<div class="ui-button" id="<fmt:message key="${adminlink.name}"/>" 
							     onClick='<c:out value="${adminlink.url}"/>'>
									<fmt:message key="${adminlink.name}"/>
							</div> 
						</c:forEach>
					</c:if>
				</td>
				<td class="linksCell">
					<div id="refreshButton" class="ui-button" title="<fmt:message key="index.refresh.hint"/>"
						 onClick="javascript:refreshPrivateNotificationCount();loadOrgTab(null, true)">
							<fmt:message key="index.refresh" />
					</div>
					<div id="notificationsButton" class="ui-button" onClick="javascript:showPrivateNotificationsDialog()">
						Notifications <span id="notificationsPendingCount"></span>
					</div>
					<div id="logoutButton" class="ui-button" onClick="javascript:closeAllChildren();document.location.href='home.do?method=logout'">
							<fmt:message key="index.logout" />
					</div>
				</td>
			</tr>
		</table>
		<c:if test="${empty tab}">
			<table id="mainContentTable" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<c:if test="${not empty orgDTOs}">
							<table id="orgTabs" cellpadding="0" cellspacing="0">
								<tr>
									<td>
										<ul>
											<c:forEach items="${orgDTOs}" var="dto" varStatus="status">
												<li class="orgTabsHeader">
													<a href="#orgTab-${status.index}-org-${dto.orgId}"><c:out value="${dto.orgName}" /></a>
												</li>
											</c:forEach>
										</ul>
									</td>
								</tr>
							</table>
                                        </td>
                                </tr>
                        </table>
						</c:if>
		</c:if>
		<c:if test="${tab eq 'profile'}">
			<tiles:insert attribute="profile" />
		</c:if>
		<c:if test="${tab eq 'community'}">
			<tiles:insert attribute="community" />
		</c:if>
	</div>
	<div id="footer">
		<p>
			<fmt:message key="msg.LAMS.version" /> <%=Configuration.get(ConfigurationKeys.VERSION)%>
			<a href="<lams:LAMSURL/>/www/copyright.jsp" target='copyright' onClick="openCopyRight()">
				&copy; <fmt:message key="msg.LAMS.copyright.short" /> 
			</a>
		</p>
	</div>
</div>
<hr>
<div class="row no-gutter">
  <div class="col-xs-12">
    <div class="container">
      <c:forEach items="${orgDTOs}" var="dto" varStatus="status">
        <div id="orgTab-${status.index}-org-${dto.orgId}" class="orgTab"></div>
      </c:forEach>
     </div>
  </div>
</div>

</body>
</lams:html>
