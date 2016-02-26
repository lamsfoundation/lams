<!DOCTYPE html>
            
<html>
<lams:head><title>Learning Design Details</title></lams:head>
	
<body class="stripes">
	<form name="authoring" method="get" action="authoring.do">
		<input type ="hidden" name="toolContentID" value="1">
		<input type="submit" name="submit" value="Author1 Logon">
	</form>		
	<form name="authoring" method="get" action="authoring.do">
		<input type ="hidden" name="toolContentID" value="2">
		<input type ="hidden" name="method" value="initPage">
		<input type="submit" name="submit" value="Author2 Logon">
	</form>		
	<form name="learnerForm" method="get" action="learner.do">
		<input type ="hidden" name="toolSessionID" value="1">
		<input type ="hidden" name="method" value="listFiles">
		<input type="submit" name="submit" value="Learner1 Logon">
	</form>
	<form name="learnerForm" method="get" action="learner.do">
		<input type ="hidden" name="toolSessionID" value="1">
		<input type ="hidden" name="method" value="listFiles">
		<input type="submit" name="submit" value="Learner2 Logon">
	</form>

	<form name="monitoringForm" method="get" action="monitoring.do">
		<input type ="hidden" name="toolSessionID" value="1">
		<input type ="hidden" name="method" value="userList">
		<input type="submit" name="submit" value="Monitoring Logon">
	</form>
	<form name="monitoringForm" method="get" action="monitoring.do">
		<input type ="hidden" name="toolContentID" value="1">
		<input type ="hidden" name="method" value="instructions">
		<input type="submit" name="submit" value="Monitoring Instructions">
	</form>
	<form name="authoring" method="get" action="monitoring.do">
		<input type ="hidden" name="toolContentID" value="1">
		<input type ="hidden" name="method" value="showActivity">
		<input type="submit" name="submit" value="Edit Activity">
	</form>	
		<form name="monitoringForm" method="get" action="monitoring.do">
		<input type ="hidden" name="toolSessionID" value="1">
		<input type ="hidden" name="method" value="statistic">
		<input type="submit" name="submit" value="Monitoring Statistic">
	</form>
</body>
</html>