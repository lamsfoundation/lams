<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp" %>
<html>
	<body class="stripes">
		<form:form action="init.do?mode=${mode}" modelAttribute="peerreviewForm" method="post">
			<form:hidden path="peerreview.contentId"/>
			<form:hidden path="sessionMapID"/>
		</form:form>
		
		<script type="text/javascript">
			document.getElementById("peerreviewForm").submit();
		</script>
	</body>
</html>
