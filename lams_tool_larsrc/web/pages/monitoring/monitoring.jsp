<%@ include file="/common/taglibs.jsp"%>

<html>
	<head>
		 <%@ include file="/common/header.jsp" %>
	</head>
	<body>
		<lams:Tabs>
			<lams:Tab id="1" key="monitoring.tab.summary" />
			<lams:Tab id="2" key="monitoring.tab.statistics" />
		</lams:Tabs>


		<div class="tabbody">
			<lams:TabBody id="1" titleKey="monitoring.tab.summary" page="summary.jsp" />
			<lams:TabBody id="2" titleKey="monitoring.tab.statistics" page="statistic.jsp" />
		</div>
	</body>
</html>
