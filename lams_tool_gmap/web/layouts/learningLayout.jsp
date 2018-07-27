<!DOCTYPE html>
            

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<tiles:insert attribute="header" />
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
	<body class="stripes" onload="initLearnerGmap()" onunload="GUnload()">
		<tiles:insert attribute="body" />
		<div class="footer">
		</div>					
	</body>
</lams:html>
