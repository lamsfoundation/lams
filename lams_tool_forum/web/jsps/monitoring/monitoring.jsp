<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" name="currentTab" id="currentTab" />
<div id="header">

	<lams:Tabs control="true">
		<lams:Tab id="1" key="monitoring.tab.summary" />
		<lams:Tab id="2" key="monitoring.tab.instructions" />
		<lams:Tab id="3" key="monitoring.tab.edit.activity" />
		<lams:Tab id="4" key="monitoring.tab.statistics" />
	</lams:Tabs>
</div>

<div id="content">
	<lams:TabBody id="1" titleKey="monitoring.tab.summary" page="summary.jsp" />
	<lams:TabBody id="2" titleKey="monitoring.tab.instructions" page="instructions.jsp" />
	<lams:TabBody id="3" titleKey="monitoring.tab.edit.activity" page="editactivity.jsp" />
	<lams:TabBody id="4" titleKey="monitoring.tab.statistics" page="statistic.jsp" />
</div>
