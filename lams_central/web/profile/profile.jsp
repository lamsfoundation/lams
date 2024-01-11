<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.AuthenticationMethod"
	import="org.lamsfoundation.lams.util.Configuration"
	import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<c:set var="showAllMyLessonLink"><%=Configuration.get(ConfigurationKeys.SHOW_ALL_MY_LESSON_LINK)%></c:set>
<c:set var="lams"><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<link rel="stylesheet" href="${lams}css/components.css">
    <link rel="stylesheet" href="${lams}includes/font-awesome6/css/all.css">
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap5.bundle.min.js"></script>
	<lams:JSImport src="includes/javascript/profile.js" />
	<script type="text/javascript">
		$(document).ready(function () {
			//update dialog's height and title
			updateMyProfileDialogSettings('<fmt:message key="index.myprofile" />', '480');
		});
	</script>
</lams:head>
<body class="component no-decoration">
	<div class="container">
		<div class="col-12 col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-2 mx-auto">
					<div class="text-center">
						<c:choose>
							<c:when test="${empty portraitUuid}">
								<div class="text-center">
									<span class="fa-stack fa-4x text-muted" title="<fmt:message key="msg.portrait.none" />"> 
										<i class="fa fa-circle fa-stack-2x"></i> 
										<i class="fa fa-user fa-stack-1x fa-inverse"></i>
									</span>
								</div>
							</c:when>
							<c:otherwise>
								<img style="margin: 0 auto;" class="img-circle img-responsive"
									title="<c:out value='${fullName}' />"
									src="/lams/download/?uuid=<c:out value="${portraitUuid}" />&version=2&preferDownload=false" />
							</c:otherwise>
						</c:choose>

						<p class="text-center">
							<c:out value="${fullName}" />
								<br /> <i class="fa fa-envelope small"></i>
							<c:out value="${email}" />
						</p>
					</div>
					<hr>
					
					<!-- Split button -->
					<div class="col-12 text-center">
						<a class="btn btn-sm btn-light m-1"
								href="<lams:LAMSURL/>index.do?redirect=editprofile" id="editProfileButton" role="button">
							<i class="fa fa-fw fa-pencil"></i> 
							<span class="float-end d-none d-sm-block ms-1">
								<fmt:message key="title.profile.edit.screen" />
							</span>
						</a>&nbsp;&nbsp;
						
						<c:set var="authenticationMethodId">
							<lams:user property="authenticationMethodId" />
						</c:set>
						<c:set var="dbId"><%=AuthenticationMethod.DB%></c:set>
						<c:if test="${authenticationMethodId eq dbId}">
							<a class="btn btn-sm btn-light m-1"
									href="<lams:LAMSURL/>index.do?redirect=password&redirectURL=index.do%3Fstate%3Dactive%26redirect%3Dprofile"  id="changePasswordButton" role="button">
								<i class="fa fa-fw fa-lock"></i>
								<span class="float-end d-none d-sm-block ms-1">
									<fmt:message key="title.password.change.screen" />
								</span>
							</a>
						</c:if>

						<a class="btn btn-sm btn-light m-1" href="<lams:LAMSURL/>index.do?redirect=portrait"  id="editPortraitButton" role="button">
							<i class="fa fa-fw fa-camera"></i> 
							<span class="float-end d-none d-sm-block ms-1">
								<fmt:message key="title.portrait.change.screen" />
							</span>
						</a>&nbsp;&nbsp;

						<a class="btn btn-sm btn-light m-1" href="profile/policyConsents.do"  id="viewPolicies" role="button">
							<i class="fa-regular fa-calendar-check"></i>
							<span class="float-end d-none d-sm-block ms-1">
								<fmt:message key="label.policies.consents" />
							</span>
						</a>&nbsp;&nbsp;

						<c:if test="${showAllMyLessonLink}">
							<a class="btn btn-sm btn-light m-1" href="<lams:LAMSURL/>index.do?redirect=lessons" id="viewAllLessons"  role="button">
								<i class="fa fa-fw fa-book"></i> 
								<span class="float-end d-none d-sm-block ms-1">
									<fmt:message key="title.all.my.lessons" />
								</span>
							</a>
						</c:if>
					</div>
		</div>
	</div>
</body>
</lams:html>