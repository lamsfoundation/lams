<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title>
			<fmt:message key="label.wiki.history.actions.view" />
		</title>
		
		<lams:headItems />
		
		<link href="<lams:WebAppURL />/includes/css/wiki_style.css" rel="stylesheet" type="text/css">
	</lams:head>
	
	<div id="page">
	<body class="stripes">
		<div id="header">
		</div>
		
		<div id="content" style="margin-right:75px;">
			<h1>
				${compareTitle} 
			</h1>
			
			<h3>
				<fmt:message key="label.wiki.compare"><fmt:param>${compareVersions}</fmt:param></fmt:message>
			</h3>
			
			<span style='background-color:#99FFCC'>&nbsp;&nbsp;&nbsp;&nbsp;</span>
			&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="label.wiki.compare.added.line" />
			<br />
			<span style='background-color:#FF9999'>&nbsp;&nbsp;&nbsp;&nbsp;</span>
			&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="label.wiki.compare.removed.line" />
			<br /><br />
			
			<div id="viewBody">
				${compareString}
			</div>
			
			<br />
			<p id="saveCancelButtons" >
				<a href="javascript:window.close()" class="button right-buttons space-left"><fmt:message key="button.close" /></a>
			</p>
		</div>
		<div id="footer">
		</div>
	</body>
	</div>
</lams:html>