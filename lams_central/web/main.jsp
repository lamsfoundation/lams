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

<%JspRedirectStrategy.welcomePageStatusUpdate(request, response);%>
<%HttpSessionManager.getInstance().updateHttpSessionByLogin(request.getSession(),request.getRemoteUser());%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
	<script language="JavaScript" type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script language="javascript" type="text/javascript" src="loadVars.jsp"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/openUrls.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/jquery-latest.pack.js"></script>
	<script type="text/javascript">
		var pathToImageFolder = "<lams:LAMSURL/>/images/";
	</script>
    <script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/thickbox.patched.js"></script>
	<c:if test="${empty tab}">
	<script language="JavaScript" type="text/javascript" src="includes/javascript/jquery.dimensions.pack.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/interface.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/groupDisplay.js"></script>	
	</c:if>
	<script language="javascript" type="text/javascript">
		<!--
			jQuery(document).ready(function(){
				<c:if test="${not empty collapsedOrgDTOs}">
				jQuery("div.course-bg").each(function(){
					var display = jQuery.trim(jQuery(this).text());
					initLoadGroup(this, <c:if test="${empty tab}">1</c:if><c:if test="${tab eq 'profile'}">3</c:if>, display);
				});
				</c:if>
				<%-- If it's the user's first login, display a dialog asking if tutorial videos should be shown --%>
				<c:if test="${firstLogin}">
					<c:url var="disableAllTutorialVideosUrl" value="tutorial.do">
						<c:param name="method" value="disableAllTutorialVideos" />
					</c:url>
					if (!confirm("<fmt:message key='label.tutorial.disable.all' />")){
				 		$.get("${disableAllTutorialVideosUrl}");
					}
				</c:if>
				
				$(".split-menu-button li em").live("click", function(event) {
					var hidden = $(this).parents("li").children("ul").is(":hidden");
					$(this).parents("li").children("ul").hide();
					$(this).parents("li").children("a").removeClass("zoneCur");
						
					if (hidden) {
						$(this)
							.parents("li").children("ul").toggle()
							.parents("li").children("a").addClass("zoneCur");
						} 

	                event.stopPropagation();
				   });
		       $("html").click(function() {
	                $(".split-menu-button>ul>li>ul").hide();
	           });
			});
		
			function getEnableSortingText() {
				return "<fmt:message key="label.enable.lesson.sorting"/>";
			}
			
			function getSortingEnabledText() {
				return "<fmt:message key="label.lesson.sorting.enabled"/>";
			}
			
			function refresh(){
				document.location.reload();
			}

			function closeWizard() {
				setTimeout(refresh, 1000);
				tb_remove();
			}
		//-->
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
		<div id="message" style="text-align: center;"></div>
		<div style="display: block;" class="box">
			<table cellpadding="0">
				<tbody>
					<tr>
						<td>
							<div class="welcome">
								<div class="float-left">
									<span class="user-img">
									<fmt:message key="index.welcome" /> 
									<lams:user property="firstName" /></span>
								</div>
								<div class="float-right">
									<c:if test="${not empty adminLinks}">
										<c:forEach var="adminlink" items="${adminLinks}">
											<a title="<fmt:message key="${adminlink.name}"/>" href='<c:out value="${adminlink.url}"/>'><fmt:message key="${adminlink.name}"/></a> | 
										</c:forEach>
									</c:if>
									<a title="<fmt:message key="index.refresh.hint"/>" href="javascript:refresh()">
										<fmt:message key="index.refresh" />
									</a> | <a href="home.do?method=logout" onClick="closeAllChildren()">
										<fmt:message key="index.logout" />
									</a>
								</div>
							</div>
							<c:if test="${empty tab}">
								<c:forEach items="${collapsedOrgDTOs}" var="dto">
									<div id="<c:out value="${dto.orgId}"/>" style="display:none" class="course-bg">
										<c:if test="${dto.collapsed}">header</c:if><c:if test="${!dto.collapsed}">group</c:if>
									</div>
								</c:forEach>
								<c:if test="${empty collapsedOrgDTOs}">
									<c:if test="${not empty showGroups}">
										<!-- div align="center" style="padding:30px;">
											<a onclick="document.location.href='index.do?groups=show';"><fmt:message key="label.show.groups"/></a>
										</div-->
										<div class="course-bg"><div class="row"><div class="mycourses-right-buttons">
											<a class="show-all-groups-button" onclick="document.location.href='index.do?groups=show';"><fmt:message key="label.show.groups"/></a>
										</div></div></div>
									</c:if>
									<c:if test="${empty showGroups}">
										<p class="align-left"><fmt:message key="msg.groups.empty" /></p>
									</c:if>
								</c:if>
							</c:if>
							<c:if test="${tab eq 'profile'}">
								<tiles:insert attribute="profile" />
							</c:if>
							<c:if test="${tab eq 'community'}">
								<tiles:insert attribute="community" />
							</c:if>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
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

</body>
</lams:html>

