<!DOCTYPE html>
        

<%@include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/mobileheader.jsp"%>
	<meta http-equiv="refresh" content="60">
</lams:head>

<body>
	<div data-role="page" data-cache="false">
	
		<div data-role="header" data-theme="b" data-nobackbtn="true">
			<h1>
				<fmt:message key="activity.title" />
			</h1>
		</div>
	
		<div data-role="content">
			<lams:DefineLater />
		</div>
		
		<div data-role="footer" data-theme="b">
			<h2>&nbsp;</h2>
		</div>
	
	</div>
</body>
</lams:html>
