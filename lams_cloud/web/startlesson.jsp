<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-function" prefix="fn"%>
<%@ taglib uri="tags-tiles" prefix="tiles" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<lams:html>
<lams:head>
	<title><fmt:message key="title.lams"/> :: <fmt:message key="index.welcome" /></title>
	<lams:css style="main" />
	<link rel="icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	
	<link type="text/css" href="includes/css/cloud.css" rel="stylesheet" />
	<link type="text/css" href="includes/css/jquery-ui-1.7.2.custom.css" rel="stylesheet" />	
	<style media="screen,projection" type="text/css">
		input.text { margin-bottom:12px; width:95%; padding: .4em; }
		fieldset { padding:0; border:0; margin-top:25px; }
		h1 { font-size: 1.2em; margin: .6em 0; }
		div#users-contain {  width: 350px; margin: 20px 0; }
		div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
		div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
		.ui-button { outline: 0; margin:0; padding: .4em 1em .5em; text-decoration:none;  !important; cursor:pointer; position: relative; text-align: center; }
		.ui-dialog .ui-state-highlight, .ui-dialog .ui-state-error { padding: .3em;  }
	</style>

	<script type="text/javascript" src="includes/javascript/cloud.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-1.3.2.min.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui-1.7.2.custom.min.js"></script>
	<script type="text/javascript">
		function addStudents(){"${lams}index.jsp?sequenceName=${param.sequenceName}&sequenceLocation=${param.sequenceLocation}";
			document.location.href ='<c:url value="/cloud/addStudents.do?sequenceName=${param.sequenceName}&sequenceLocation=${param.sequenceLocation}"/>';
			return false;
		}
		function startLesson(){
			document.location.href ='<c:url value="/cloud/startLesson.do?sequenceName=${param.sequenceName}&sequenceLocation=${param.sequenceLocation}"/>';
			return false;
		}
	</script>
	
</lams:head>


<body class="my-courses">
<div id="page-mycourses">
	<div id="header-my-courses">
		<div id="nav-right">
		</div>
	</div>

	<div id="content-main">
		
		<div class="main_font_size">
			<fmt:message key="cloud.index.hi" />
			<lams:user property="firstName"/> <lams:user property="lastName"/>!
		</div>
						
		<table class="current_phase_table">
			<tr >
				<td class ="not_active" >
					<fmt:message key="cloud.welcome.login.or.signup">
						<fmt:param value="<br>" />
					</fmt:message>					
				</td>
				<td>
					<img alt="" src="<lams:LAMSURL/>/images/css/blue_arrow_right.gif">
				</td>
				<td class="not_active" >
					<fmt:message key="cloud.welcome.add.students">
						<fmt:param value="<br>" />
					</fmt:message>					
				</td>
				<td>
					<img alt="" src="<lams:LAMSURL/>/images/css/blue_arrow_right.gif">										
				</td>
				<td class ="active" >
					<fmt:message key="cloud.welcome.start.and.voila">
						<fmt:param value="<br>" />
					</fmt:message>					
				</td>
			</tr>
			<tr>
				<td colspan="5" style="text-align: center;">
					<fmt:message key="cloud.startlesson.done" />
				</td>
			</tr>
		</table>
		
		<div class="main_font_size">
			<fmt:message key="cloud.startlesson.your.lesson.created" >
				<fmt:param value="${param.sequenceName}" />
			</fmt:message>
		</div>
		<div class="main_font_size">
			<fmt:message key="cloud.startlesson.details" />
		</div>			
		<div class="main_font_size" >
			<fmt:message key="cloud.startlesson.url" /> http://lamscloud.com/${param.url}
			<button class="ui-button ui-state-default ui-corner-all">
				<fmt:message key="cloud.startlesson.copy.to.clipboard" />
			</button>				
		</div>
		<c:if test="${!empty param.code}">
			<div class="main_font_size">
				<fmt:message key="cloud.startlesson.code" /> ${param.code}
			</div>
		</c:if>
			
	</div>
	<div id="footer">
		<%@ include file="copyright.jsp"%>
	</div>
</div>


</body>
</lams:html>

