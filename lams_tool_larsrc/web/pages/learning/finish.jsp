<%@ include file="/common/taglibs.jsp"%>
<%-- This page just for : redir finish page to parent rather that part of the frame --%>
<html>
	<body>
	<script type="text/javascript">
	<!--
		if(${runAuto})
			document.location.href = "${nextActivityUrl}";
		else
			parent.location = "${nextActivityUrl}";
	-->        
    </script>
	
	<body>
</html>
