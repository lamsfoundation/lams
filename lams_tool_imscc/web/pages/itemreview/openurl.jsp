<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	<body class="stripes">

		<div id="content">
			<p>
				<c:out value="${title}" escapeXml="true"/>
			</p>
			<p>
				<a href="javascript:;"
					onclick="javascipt:launchPopup('<c:url value='${popupUrl}'/>','popupUrl');"
					style="width:200px;float:none;" class="button"><fmt:message
						key="open.in.new.window" /></a>
			</p>
		<div>
	</body>
</lams:html>
