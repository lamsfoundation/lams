<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="org.lamsfoundation.lams.security.JspRedirectStrategy"%>
<%@ page import="org.lamsfoundation.lams.web.util.HttpSessionManager"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-function" prefix="fn"%>
<%@ taglib uri="tags-tiles" prefix="tiles" %>

<%-- If you change this file, remember to update the copy made for CNG-21 --%>

<%JspRedirectStrategy.welcomePageStatusUpdate(request, response);%>
<%HttpSessionManager.getInstance().updateHttpSessionByLogin(request.getSession(),request.getRemoteUser());%>
<!DOCTYPE HTML>
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
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="stylesheet" href="<lams:LAMSURL/>/css/thickbox.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" type="text/css" media="screen">
	<style type="text/css">
		#content-main {
			padding: 10px;
		}
		
		td#messageCell {
			width: 100%;
		}
		
		#message {
			padding:8px 10px 10px 40px;
			text-align: left;
			background: url('images/css/edit.gif') no-repeat #d8e4f1 10px 8px;
			border: 1px solid #3c78b5;
		}
		
		#linksCell div {
			margin: 0 5px 15px 0;
		}
		
		.dialogContainer {
			display: none;
		}
		
		.dialogContainer iframe {
			width: 100%;
			height: 100%;
			border: none;
		}
		
		#mainContentTable td {
			vertical-align: top;
			padding: 0;
		}
		
		#orgTabs {
			padding-right: 10px;
		}
		
		#orgTabs td {
			padding: 0;
			vertical-align: top;
			font-family: verdana,arial,helvetica,sans-serif;
		}
		
		#orgTabsPanelCell {
			width: 100%;
			border-left: none;
		}
		
		.ui-tabs-vertical {
			border: none;
		}
		
		.ui-tabs-vertical .ui-tabs-nav {
			background: none;
			border: none;
		}
		
		.ui-tabs-vertical .ui-tabs-nav li {
			width: 100%;
			margin-bottom: 7px;
			border: 1px solid #C5DBEC;
			padding: 3px 0 3px 0 !important;
			
		}
		
		.ui-tabs-vertical .ui-tabs-nav li.ui-tabs-active {
			border: 1px solid #79B7E7;
			border-right-width: 0px;
			margin-bottom: 7px;
			padding: 3px 0 3px 0 !important;
		}	
			
		.ui-tabs-vertical .ui-tabs-nav li a {
			display: block;
			float: none;
			border: none;
		}
		
		td#actionAccord {
			width: 20%;
			padding-top: 1px;
		}
	</style>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script language="javascript" type="text/javascript" src="loadVars.jsp"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/openUrls.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
    <script language="JavaScript" type="text/javascript" src="includes/javascript/thickbox.js"></script>
	<c:if test="${empty tab}">
		<script language="JavaScript" type="text/javascript" src="includes/javascript/groupDisplay.js"></script>	
	</c:if>
	<script language="javascript" type="text/javascript">
			var LAMS_URL = '<lams:LAMSURL/>';
			var LABELS = {
					EMAIL_NOTIFICATIONS_TITLE : '<fmt:message key="index.emailnotifications" />',
					REMOVE_LESSON_CONFIRM1 : '<fmt:message key="index.remove.lesson.confirm1"/>',
					REMOVE_LESSON_CONFIRM2 : '<fmt:message key="index.remove.lesson.confirm2"/>',
					SORTING_ENABLE : '<fmt:message key="label.enable.lesson.sorting"/>',
					SORTING_DISABLE : '<fmt:message key="label.disable.lesson.sorting"/>'
			}
			
			var tabName = '${tab}';

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
<body class="my-courses">
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
					<lams:user property="firstName" />
				</td>
				<td id="messageCell">
					<div id="message">Important annoucements might be posted here...</div>
				</td>
				<td id="linksCell">
					<c:if test="${not empty adminLinks}">
						<c:forEach var="adminlink" items="${adminLinks}">
							<div>
								<a title="<fmt:message key="${adminlink.name}"/>" href='<c:out value="${adminlink.url}"/>' class="button">
									<fmt:message key="${adminlink.name}"/>
								</a>
							</div> 
						</c:forEach>
					</c:if>
					<div>
						<a title="<fmt:message key="index.refresh.hint"/>" href="javascript:refresh()" class="button">
							<fmt:message key="index.refresh" />
						</a>
					</div>
					<div>
						<a href="home.do?method=logout" onClick="closeAllChildren()" class="button">
							<fmt:message key="index.logout" />
						</a>
					</div>
				</td>
			</tr>
		</table>
		<c:if test="${empty tab}">
			<table id="mainContentTable" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<table id="orgTabs" cellpadding="0" cellspacing="0">
							<tr>
								<td>
									<ul>
										<c:forEach items="${collapsedOrgDTOs}" var="dto">
											<li id="orgTabHeader-${dto.orgId}" class="orgTabHeader">
												<a href="#orgTab-${dto.orgId}"></a>
											</li>
										</c:forEach>
									</ul>
								</td>
								<td id="orgTabsPanelCell" class="ui-widget-content ui-corner-all">
									<c:forEach items="${collapsedOrgDTOs}" var="dto">
										<div id="orgTab-${dto.orgId}" class="orgTab"></div>
									</c:forEach>
								</td>
							</tr>
						</table>
					</td>
					<td id="actionAccord">
							<h3>New lessons</h3>
							<div>New lessons content panel<br />
							TEXT TEXT TEXT TEXT<br />
							TEXT TEXT TEXT TEXT<br />
							TEXT TEXT TEXT TEXT<br />
							TEXT TEXT TEXT TEXT<br />
							TEXT TEXT TEXT TEXT<br />
							</div>
							<h3>Recent activity</h3>
							<div>Recent activity content panel</div>
							<h3>Gradebooks</h3>
							<div>Gradebooks content panel</div>
							<h3>Announcements</h3>
							<div>Announcements content panel</div>
						
					</td>
				</tr>
			</table>
			
			<%--
			<c:forEach items="${collapsedOrgDTOs}" var="dto">
				<div id="<c:out value="${dto.orgId}"/>" style="display:none" class="course-bg">
					<c:if test="${dto.collapsed}">header</c:if><c:if test="${!dto.collapsed}">group</c:if>
				</div>
			</c:forEach>
			<c:if test="${empty collapsedOrgDTOs}">
				<c:if test="${not empty showGroups}">
					<div class="course-bg"><div class="row"><div class="mycourses-right-buttons">
						<a class="show-all-groups-button" onclick="document.location.href='index.do?groups=show';"><fmt:message key="label.show.groups"/></a>
					</div></div></div>
				</c:if>
				<c:if test="${empty showGroups}">
					<p class="align-left"><fmt:message key="msg.groups.empty" /></p>
				</c:if>
			</c:if>
			 --%>
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
			<a style="color: #999999; text-decoration: none; border: none;" href="index.do?newLayout=false">
				<fmt:message key="msg.LAMS.version" /> <%=Configuration.get(ConfigurationKeys.VERSION)%>
			</a>
			<a href="<lams:LAMSURL/>/www/copyright.jsp" target='copyright' onClick="openCopyRight()">
				&copy; <fmt:message key="msg.LAMS.copyright.short" /> 
			</a>
		</p>
	</div>
</div>

<div id="addLessonDialog" class="dialogContainer">
	<iframe id="addLessonFrame"></iframe>
</div>
<div id="monitorDialog" class="dialogContainer">
	<iframe id="monitorFrame"></iframe>
</div>
<div id="notificationsDialog" class="dialogContainer">
	<iframe id="notificationsFrame"></iframe>
</div>

</body>
</lams:html>