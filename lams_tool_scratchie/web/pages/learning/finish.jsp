<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%-- This page just for : redir finish page to parent rather that part of the frame --%>
<html>
	<body class="stripes">
	<script type="text/javascript">
		document.location.href = "${nextActivityUrl}";
    </script>
	</body>
</html>
