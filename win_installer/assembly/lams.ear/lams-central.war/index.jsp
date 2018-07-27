<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<meta HTTP-EQUIV="Refresh" CONTENT="0.5;URL=index.do">
	<title><fmt:message key="index.welcome" /></title>
	<lams:css />
</lams:head>

<body class="stripes">
	<!-- Index page flag - do not remove, TestHarness looks for it -->

	<lams:Page type="admin">

		<div class="text-center" style="margin-top: 20px; margin-bottom: 20px;">
			<i class="fa fa-2x fa-refresh fa-spin text-primary"></i>
			<h4>
				<fmt:message key="msg.loading" />
			</h4>
		</div>

		<div id="footer"></div>
		<!--closes footer-->
	</lams:Page>
</body>
</lams:html>