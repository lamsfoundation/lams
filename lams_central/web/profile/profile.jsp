<!DOCTYPE html>

<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.AuthenticationMethod"
	import="org.lamsfoundation.lams.util.Configuration"
	import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<c:set var="showAllMyLessonLink"><%=Configuration.get(ConfigurationKeys.SHOW_ALL_MY_LESSON_LINK)%></c:set>

<lams:html>
<lams:head>
	<lams:css/>
	
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/profile.js"></script>
	<script type="text/javascript">
		$(document).ready(function () {
			//update dialog's height and title
			updateMyProfileDialogSettings('<fmt:message key="index.myprofile" />', '480');
		});
	</script>
</lams:head>
<body>

<div class="container">
	<div class="row vertical-center-row">
		<div class="col-xs-12 col-sm-10 col-sm-offset-1 col-md-6 col-md-offset-3">
			<div class="panel voffset20">
				<div class="panel-body">
					<div class="text-center">
						<c:choose>
							<c:when test="${empty portraitUuid}">
								<div class="text-center">
									<span class="fa-stack fa-4x text-muted"
										title="<fmt:message key="msg.portrait.none" />"> <i
										class="fa fa-circle fa-stack-2x"></i> <i
										class="fa fa-user fa-stack-1x fa-inverse"></i>
									</span>
								</div>

							</c:when>
							<c:otherwise>
								<img style="margin: 0 auto;" class="img-circle img-responsive"
									title="<bean:write name="fullName" />"
									src="/lams/download/?uuid=<bean:write name="portraitUuid" />&version=2&preferDownload=false" />
							</c:otherwise>
						</c:choose>

						<p class="text-center">
							<bean:write name="fullName" />
								<br /> <i class="fa fa-envelope small"></i>
							<bean:write name="email" />
						</p>
					</div>
					<hr>
					<!-- Split button -->
					<div class="col-xs-12 text-center">
						<a class="btn btn-sm btn-default offset5"
							href="index.do?method=editprofile" role="button"><i
							class="fa fa-fw fa-pencil"></i> <span class="hidden-xs"><fmt:message
									key="title.profile.edit.screen" /></span></i>
						</a>&nbsp;&nbsp;
						
						<c:set var="authenticationMethodId">
							<lams:user property="authenticationMethodId" />
						</c:set>
						<c:set var="dbId"><%=AuthenticationMethod.DB%></c:set>
						<c:if test="${authenticationMethodId eq dbId}">

							<a class="btn btn-sm btn-default offset5"
								href="index.do?method=password&redirectURL=index.do%3Fstate%3Dactive%26method%3Dprofile" role="button">
									<i class="fa fa-fw fa-lock"></i> 
										<span class="hidden-xs"><fmt:message
											key="title.password.change.screen" />
										</span>
									</i>
							</a>
						</c:if>
						<a class="btn btn-sm btn-default offset5"
							href="index.do?method=portrait" role="button">
							<i class="fa fa-fw fa-camera"></i> <span class="hidden-xs"><fmt:message
									key="title.portrait.change.screen" /></span></i>
						</a>&nbsp;&nbsp;
						
						<a class="btn btn-sm btn-default offset5" href="profile.do?method=policyConsents" role="button">
							<i class="fa fa-fw fa-calendar-check-o"></i> 
							<span class="hidden-xs">
								<fmt:message key="label.policies.consents" />
							</span>
						</a>&nbsp;&nbsp;

						<c:if test="${showAllMyLessonLink}">
							<a class="btn btn-sm btn-default offset5"
								href="index.do?method=lessons" role="button">
								<i class="fa fa-fw fa-book"></i> <span class="hidden-xs"><fmt:message
										key="title.all.my.lessons" /></span>
								</i>
							</a>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>

</div>

</body>
</lams:html>

