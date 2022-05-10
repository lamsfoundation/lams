<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<lams:html>

<lams:head>
	<lams:css />
	<title><fmt:message key="title.forgot.password" /></title>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
</lams:head>

<script type="text/javascript">
	function toHome() {
		window.location = "<lams:LAMSURL/>index.do";
	}
</script>

<body class="stripes">
	<c:set var="title"><fmt:message key="title.forgot.password" /></c:set>
	<lams:Page type="admin" title="${title}">
		<c:choose>
			<c:when test="${showErrorMessage}">
				<c:set var="type" value="danger"/>
			</c:when>
			<c:otherwise>
				<c:set var="type" value="info"/>
			</c:otherwise>
		</c:choose>
		
		<lams:Alert id="output" type="${type}" close="false">
			<fmt:message key="${languageKey}" />
		</lams:Alert>

		<button type="button" name="cancel" class="btn btn-primary pull-right voffset10" onclick="javascript:toHome();">
			<fmt:message key="label.ok" />
		</button>

	</lams:Page>
</body>

</lams:html>
