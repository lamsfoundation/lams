<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="org.lamsfoundation.lams.security.JspRedirectStrategy"%>
<%@ page import="org.lamsfoundation.lams.web.util.HttpSessionManager"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
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
	
	<lams:css style="tabbed" />
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<script language="JavaScript" type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/openUrls.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/jquery-1.1.4.pack.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/jquery.dimensions.pack.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/interface.js"></script>
	<script language="javascript" type="text/javascript">
		<!--
			<c:if test="${empty tab}">
				jQuery(document).ready(function(){
					jQuery("div.j-display-group").each(function(){
						jQuery(this).load("displayGroup.do", {stateId: 1, orgId: this.id}, function() {
							jQuery("a.disabled-sequence-name-link, a.sequence-name-link", this).ToolTip({
								className: 'sequence-description-tooltip',
								position: 'mouse',
								delay: 300
							});
							jQuery("a.disabled-sequence-name-link, a.sequence-name-link", this).each(function(i, element) {
								var title = jQuery(element).attr("title");
								if (title!=null) {
									var newTitle = title.replace(/\r\n/g,"<BR>").replace(/\n/g,"<BR>")
									jQuery(element).attr("title", newTitle);
								}
							});
						});
					});
					jQuery("body").click(function(event) {
						if (jQuery(event.target).is("a.j-group-header")){
							var course = jQuery(event.target).parent("h2").parent("div.left-buttons").parent("div.row").next("div.j-course-contents");
							if (jQuery.browser.msie) {
								if (jQuery.browser.version == '6.0') {
									course.slideToggle("fast");
								}
							} else {
								course.toggle("fast");
							}
							return false;
						}
					});
				});
				
				function makeSortable(element, acceptClass) {
					jQuery(element).Sortable({
						accept: acceptClass,
						axis: "vertically",
						containment: [jQuery(element).offset().left,
							jQuery(element).offset().top,
							jQuery(element).width(),
							jQuery(element).height()],
						onStop: function() {
							var ids = [];
							jQuery(this).parent().children("p").each(function(i, element) {
								ids.push(element.id);
							});
							var jLessonsId = jQuery(this).parent().attr("id");
							var dashIndex = jLessonsId.indexOf("-");
							var orgId = (dashIndex>0 ? jLessonsId.substring(0, dashIndex) : jLessonsId);
							$.ajax({
								url: "servlet/saveLessonOrder",
								data: {orgId: orgId, 
									ids: ids.join(",")
								},
								error: function(a,b) {
									refresh();
								}
							});
						}
					});
				}
			</c:if>
								
			function refresh(){
				document.location.reload();
			}
		//->
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
	
				<div class="${tabLeft}"></div>
				<div class="${tabMiddle}">
						<lams:TabName url="${headerlink.url}" highlight="${highlight}"><fmt:message key="${headerlink.name}" /></lams:TabName>
				</div>
				<div class="${tabRight}"></div>
			</div>
			</c:forEach>
		</div>
	</div>
	<div id="content">
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
											<a title='<fmt:message key="${adminlink.name}"/>' href='<c:out value="${adminlink.url}"/>'><fmt:message key="${adminlink.name}"/></a> | 
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
								<c:forEach items="${courseIds}" var="courseId">
									<div id="<c:out value="${courseId}"/>" class="j-display-group"></div>
								</c:forEach>
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

