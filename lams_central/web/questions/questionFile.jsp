<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<title><fmt:message key="title.lams" /> :: <fmt:message key="label.questions.file.title" /></title>

	<lams:css />
	<style type="text/css">
.button {
	float: right;
	margin-left: 10px;
}

#buttonsDiv {
	padding: 15px 0px 15px 0px;
}

div#errorArea {
	display: none;
}
</style>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			if ($.trim($('#errorArea').html()) != "") {
				window.resizeTo(600, 350);
				$('#errorArea').show();
			}
		});
	</script>
</lams:head>

<body class="stripes">

	<c:set var="title" scope="request">
		<fmt:message key="label.questions.file.title" />
	</c:set>
	<lams:Page type="admin" title="${title}">
		<div id="content">

			<lams:Alert id="errorArea" type="danger" close="false">
				<html:messages id="error">
					<c:out value="${error}" escapeXml="false" />
					<br />
				</html:messages>
			</lams:Alert>

			<form id="questionForm" action="<lams:LAMSURL/>questions.do" enctype="multipart/form-data" method="post">
				<input type="hidden" name="returnURL" value="${empty param.returnURL ? returnURL : param.returnURL}" /> <input
					type="hidden" name="limitType" value="${empty param.limitType ? limitType : param.limitType}" /> <input
					type="file" name="file" size="53" />
				<div class="voffset10" id="buttonsDiv">
					<input class="btn btn-sm btn-default" value='<fmt:message key="button.cancel"/>' type="button"
						onClick="javascript:window.close()" />
					<button class="btn btn-sm btn-primary" value='<fmt:message key="label.upload"/>' type="submit">
						<i class="fa fa-sm fa-upload"></i>&nbsp;<fmt:message key="button.import" />
					</button>
				</div>
			</form>

			<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>