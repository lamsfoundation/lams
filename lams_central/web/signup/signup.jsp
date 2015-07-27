<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<lams:html>
<lams:head>
	<title><fmt:message key="title.lams.signup"/></title>
	<lams:css style="core"/>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" rel="stylesheet" />
	<style media="screen,projection" type="text/css">
		#tabs {width: 90%; margin: 10px 25px ;}
		#tabs-1, #tabs-2 {padding: 0;}
		#tabs-1 table {width:90%;}		
		#tabs-2 table {width:90%;}
		.ui-icon-alert, .ui-icon-info, .ui-icon-circle-plus, .ui-icon-print, .ui-icon-mail-closed, .ui-icon-scissors {float: left; margin-right: 0.3em;}
		#signup-intro {color: #808080; font-size: 12px; padding: 30px 20px 0px 30px;}
		#content a {border-bottom: none !important; color: #2E6E9E;}
		.ui-tabs .ui-tabs-nav li a {text-decoration: none !important; }
		.ui-state-active a, .ui-state-active a:link, .ui-state-active a:visited {color: #E17009 !important;}
		#content a:hover{color: #2E6E9E;}
		.table-row-caption {text-align: right; width: 40%;}
		#selectLoginTabA {cursor: pointer; color: #47bc23;}
	</style>	
	
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script language="JavaScript" type="text/javascript">
		jQuery(document).ready(function(){
			var selectedTab = (${(signupOrganisation != null) && signupOrganisation.loginTabActive}) ? 1 : 0;
			$("#tabs").tabs({
				selected: selectedTab
			});
		});
	</script>
</lams:head>
    
<body class="stripes">
<div id="page">
	<div id="content">
		<h1 align="center">
			<img src="<lams:LAMSURL/>/images/css/lams_login.gif" alt="LAMS - Learning Activity Management System" width="186" height="90" ></img>
		</h1>
		
		<c:if test="${not empty signupOrganisation}">
			<h1 align="center">
				<c:out value="${signupOrganisation.organisation.name}" /> 
				<c:if test="${not empty signupOrganisation.organisation.code}">
					(<c:out value="${signupOrganisation.organisation.code}" />)
				</c:if>
			</h1>
			<c:if test="${not empty signupOrganisation.blurb}">
				<div id="signup-intro">
					<c:out value="${signupOrganisation.blurb}" escapeXml="false" />
				</div>
			</c:if>
		</c:if>
		
		<div id="signup-intro">
			<span class="ui-icon ui-icon-info"></span>
			<fmt:message key="register.if.you.want.to.signup"/>
		</div>
		
		<c:if test="${not empty error}">
			<p class="warning"><c:out value="${error}" /></p>
		</c:if>
		
		<!-- Tabs -->
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1"><fmt:message key="register.signup.to.lams"/></a></li>
				<li><a href="#tabs-2"><fmt:message key="register.login"/></a></li>
			</ul>
			<div id="tabs-1">
				<p>
					<%@ include file="singupTab.jsp"%>
				</p>
			</div>
			<div id="tabs-2">
				<p>
					<%@ include file="loginTab.jsp"%>
				</p>
			</div>
		</div>
	</div>
</div>
</body>
</lams:html>




