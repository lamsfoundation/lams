<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<title>
			<fmt:message key="label.wiki.history.actions.view" />
		</title>
		
		<lams:headItems />
		
	</lams:head>
	
	<body class="stripes">
	
		<lams:Page title="${compareTitle}" type="learner">
		
			<h4>
				<fmt:message key="label.wiki.compare"><fmt:param>${compareVersions}</fmt:param></fmt:message>
			</h4>
			
			<span style='background-color:#99FFCC'>&nbsp;&nbsp;&nbsp;&nbsp;</span>
			&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="label.wiki.compare.added.line" />
			<br />
			<span style='background-color:#FF9999'>&nbsp;&nbsp;&nbsp;&nbsp;</span>
			&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="label.wiki.compare.removed.line" />
			<br /><br />
			
			<div id="viewBody">
				${compareString}
			</div>
			
			<div id="saveCancelButtons" class="voffset10"> 
				<a href="javascript:window.close()" class="btn btn-secondary"><fmt:message key="button.close" /></a>
			</div>

		<div id="footer"></div>
		</lams:Page>
		
	</body>
</lams:html>