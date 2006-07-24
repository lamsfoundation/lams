<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf8">
		<title><fmt:message key="403.title" /></title>
		<!-- ********************  CSS ********************** -->
		<lams:css />
	</head>

	<body>
		<div id="page-learner">
			<!--main box 'page'-->

			<h1 class="no-tabs-below">
				<fmt:message key="403.title" />
			</h1>
			<div id="header-no-tabs-learner"></div>
			<!--closes header-->

			<div id="content-learner">
				<%-- Error Messages --%>
				<p class="warning">
					<fmt:message key="403.message" />
				</p>
				<!--
				<div class="right-buttons">
					<a href="javascript:;" onclick="closeWin();" class="button"> <fmt:message
							key="button.close" /> </a>
				</div>
				-->
			</div>
			<!--closes content-->

			<div id="footer-learner"></div>
			<!--closes footer-->
		</div>
		<!--closes page-->
	</body>
</html>
