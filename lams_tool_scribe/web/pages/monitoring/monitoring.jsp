<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.scribe.util.ScribeConstants"%>

<div id="header">
	<lams:Tabs>
		<lams:Tab id="1" key="button.summary" />
		<lams:Tab id="2" key="button.editActivity" />
		<lams:Tab id="3" key="button.statistics" />
	</lams:Tabs>
</div>

<div id="content">
	<lams:help toolSignature="<%= ScribeConstants.TOOL_SIGNATURE %>" module="monitoring"/>

	<lams:TabBody id="1" titleKey="button.summary" page="summary.jsp" />
	<lams:TabBody id="2" titleKey="button.editActivity" page="editActivity.jsp" />
	<lams:TabBody id="3" titleKey="button.statistics" page="statistics.jsp" />
</div>

<div id="footer"></div>
