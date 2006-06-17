<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<tiles:insert attribute="header" />
	<body>
		<div id="page">
			<div id="header"></div>
			<div id="content">
				<tiles:insert attribute="body" />
			</div>
			<div id="footer"></div>
		</div>
	</body>
</html>
