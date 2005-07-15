<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head><title>Learning Design Details</title></head>
	
<body>
	<%
		session.setAttribute("title","Submission Upload");
		session.setAttribute("instructions","Upload you file by this tool");
	%>
	<form name="learnerForm" method="get" action="learner.do">
		<input type ="hidden" name="toolSessionID" value="1">
		<input type ="hidden" name="userID" value="1">
		<input type ="hidden" name="method" value="listFiles">
		<input type="submit" name="submit" value="Learner1 Logon">
	</form>
	<form name="learnerForm" method="get" action="learner.do">
		<input type ="hidden" name="toolSessionID" value="1">
		<input type ="hidden" name="userID" value="2">
		<input type ="hidden" name="method" value="listFiles">
		<input type="submit" name="submit" value="Learner2 Logon">
	</form>

	<form name="monitoringForm" method="get" action="monitoring.do">
		<input type ="hidden" name="contentID" value="1">
		<input type ="hidden" name="method" value="userList">
		<input type="submit" name="submit" value="Monitoring Logon">
	</form>

</body>
</html>