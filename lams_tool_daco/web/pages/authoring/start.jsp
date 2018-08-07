<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp" %>
<html>
<body class="stripes">
<form:form action="init.do?mode=${mode}" modelAttribute="authoringForm" method="post" id="authoringForm">
	<form:hidden path="daco.contentId"/>
	<form:hidden path="sessionMapID"/>
</form:form>

<script type="text/javascript">
document.getElementById("authoringForm").submit();
</script>
<body class="stripes">
</html>
