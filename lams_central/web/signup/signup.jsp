<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
<script type="text/javascript"
	src="/lams/includes/javascript/jquery-ui.js"></script>
<script type="text/javascript"
	src="/lams/includes/javascript/jquery.validate.js"></script>
<link rel="stylesheet" href="/lams/css/defaultHTML_learner.css"
	type="text/css" />
<script type="text/javascript"
	src="/lams/includes/javascript/bootstrap.min.js"></script>

<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title><fmt:message key="title.lams.signup" /></title>
	<lams:css/>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico"
		type="image/x-icon" />
	<link type="text/css"
		href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" rel="stylesheet" />

	<style media="screen,projection" type="text/css">
.with-nav-tabs.panel-default .nav-tabs>li>a, .with-nav-tabs.panel-default .nav-tabs>li>a:hover,
	.with-nav-tabs.panel-default .nav-tabs>li>a:focus {
	color: #777;
}

.with-nav-tabs.panel-default .nav-tabs>.open>a, .with-nav-tabs.panel-default .nav-tabs>.open>a:hover,
	.with-nav-tabs.panel-default .nav-tabs>.open>a:focus, .with-nav-tabs.panel-default .nav-tabs>li>a:hover,
	.with-nav-tabs.panel-default .nav-tabs>li>a:focus {
	color: #777;
	background-color: #ddd;
	border-color: transparent;
}

.with-nav-tabs.panel-default .nav-tabs>li.active>a, .with-nav-tabs.panel-default .nav-tabs>li.active>a:hover,
	.with-nav-tabs.panel-default .nav-tabs>li.active>a:focus {
	color: #555;
	background-color: #fff;
	border-color: #ddd;
	border-bottom-color: transparent;
}

.with-nav-tabs.panel-default .nav-tabs>li.dropdown .dropdown-menu {
	background-color: #f5f5f5;
	border-color: #ddd;
}

.with-nav-tabs.panel-default .nav-tabs>li.dropdown .dropdown-menu>li>a {
	color: #777;
}

.with-nav-tabs.panel-default .nav-tabs>li.dropdown .dropdown-menu>li>a:hover,
	.with-nav-tabs.panel-default .nav-tabs>li.dropdown .dropdown-menu>li>a:focus
	{
	background-color: #ddd;
}

.with-nav-tabs.panel-default .nav-tabs>li.dropdown .dropdown-menu>.active>a,
	.with-nav-tabs.panel-default .nav-tabs>li.dropdown .dropdown-menu>.active>a:hover,
	.with-nav-tabs.panel-default .nav-tabs>li.dropdown .dropdown-menu>.active>a:focus
	{
	color: #fff;
	background-color: #555;
}
</style>

	<script type="text/javascript"
		src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript"
		src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript"
		src="/lams/includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function(){
			var selectedTab = (${(signupOrganisation != null) && signupOrganisation.loginTabActive}) ? 1 : 0;
			$("#tabs").tabs({
				selected: selectedTab
			});
		});
	</script>
</lams:head>

<body class="stripes">
	<lams:Page type="admin">
		<div class="page-header">
			<p class="text-center">
				<img src="<lams:LAMSURL/>/images/svg/lams_logo_black.svg"
					alt="LAMS - Learning Activity Management System" width="200px"></img>
			</p>
			<c:if test="${not empty signupOrganisation}">
				<h1 align="center">
					<c:out value="${signupOrganisation.organisation.name}" />
					<c:if test="${not empty signupOrganisation.organisation.code}">
					(<c:out value="${signupOrganisation.organisation.code}" />)
				</c:if>
				</h1>
				<c:if test="${not empty signupOrganisation.blurb}">
					<div id="signup-intro" class="panel">
						<c:out value="${signupOrganisation.blurb}" escapeXml="false" />
					</div>
				</c:if>
			</c:if>

			<div class="media">
  				<div class="media-left">
					<i class="ui-icon ui-icon-info"></i>
				</div>
				<div class="media-body">
					<small><fmt:message key="register.if.you.want.to.signup" /></small>
				</div>
			</div>

			<c:if test="${not empty error}">
				<lams:Alert type="danger" id="errors" close="false">
					<c:out value="${error}" />
				</lams:Alert>
			</c:if>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="panel with-nav-tabs panel-default">
					<div class="panel-heading" style="height: 51px">
						<ul class="nav nav-tabs">
							<li class="active"><a href="#tab1default" data-toggle="tab"><fmt:message
										key="register.signup.to.lams" /></a></li>
							<li><a href="#tab2default" data-toggle="tab"><fmt:message
										key="register.login" /></a></li>
						</ul>
					</div>
					<div class="panel-body">
						<div class="tab-content">
							<div class="tab-pane fade in active" id="tab1default">
								<p>
									<%@ include file="singupTab.jsp"%>
								</p>
							</div>
							<div class="tab-pane fade" id="tab2default">
								<p>
									<%@ include file="loginTab.jsp"%>
								</p>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
	</lams:Page>
</body>
</lams:html>
