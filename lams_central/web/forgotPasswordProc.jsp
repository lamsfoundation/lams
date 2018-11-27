<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>

<!DOCTYPE html>
<lams:html>

<lams:head>
	<title><fmt:message key="title.forgot.password" /></title>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<lams:css />
</lams:head>

<script type="text/javascript">
	function toHome() {
		window.location = "<lams:LAMSURL/>index.do";
	};
</script>

<body class="stripes">
	<c:set var="title"><fmt:message key="title.forgot.password" /></c:set>
	<lams:Page type="admin" title="${title}">
		<c:choose>
			<c:when test="${param.showErrorMessage}">
				<c:set var="type" value="danger"/>
			</c:when>
			<c:otherwise>
				<c:set var="type" value="info"/>
			</c:otherwise>
		</c:choose>
		
		<lams:Alert id="output" type="${type}" close="false">
			<fmt:message key="${param.languageKey}" />
		</lams:Alert>

		<html:button property="cancel" styleClass="btn btn-primary pull-right voffset10" onclick="javascript:toHome();">
			<fmt:message key="label.ok" />
		</html:button>
	</lams:Page>
</body>

</lams:html>
