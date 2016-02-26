<!DOCTYPE html>
            

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<tiles:insert attribute="header" />
	<body class="stripes" onload="initLearnerGmap()" onunload="GUnload()">
		<tiles:insert attribute="body" />
		<div class="footer">
		</div>					
	</body>
</lams:html>
