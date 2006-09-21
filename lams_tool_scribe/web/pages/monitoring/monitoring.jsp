<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.scribe.util.ScribeConstants"%>

<div id="header">
	<lams:Tabs>
		<lams:Tab id="1" key="button.summary" />
		<lams:Tab id="2" key="button.instructions" />
		<lams:Tab id="3" key="button.editActivity" />
		<lams:Tab id="4" key="button.statistics" />
	</lams:Tabs>
</div>

<lams:help toolSignature="<%= ScribeConstants.TOOL_SIGNATURE %>" module="monitoring"/>

<div id="content">
	<lams:TabBody id="1" titleKey="button.summary" page="summary.jsp" />
	<lams:TabBody id="2" titleKey="button.instructions" page="instructions.jsp" />
	<lams:TabBody id="3" titleKey="button.editActivity" page="editActivity.jsp" />
	<lams:TabBody id="4" titleKey="button.statistics" page="statistics.jsp" />
</div>

<div id="footer"></div>
