<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
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
					onclick="javascipt:launchPopup('${popupUrl}','popupUrl');"
					style="width:200px;float:none;" class="button"><fmt:message
						key="open.in.new.window" /></a>
			</p>
		<div>
	</body>
</lams:html>
