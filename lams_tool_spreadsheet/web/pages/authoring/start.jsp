<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp" %>
<html>
	<body class="stripes">
		<form:form action="init.do?mode=${mode}" method="post" modelAttribute="spreadsheetForm" id="spreadsheetForm">
			<form:hidden path="spreadsheet.contentId"/>
			<form:hidden path="sessionMapID" id="ssss"/>
		</form:form>
		
		<script type="text/javascript">
			document.getElementById("spreadsheetForm").submit();
		</script>
	<body class="stripes">
</html>
