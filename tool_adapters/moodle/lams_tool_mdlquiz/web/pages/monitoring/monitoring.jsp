<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.mdquiz.util.MdlQuizConstants"%>

<div id="header">
	<lams:Tabs>
		<lams:Tab id="1" key="button.summary" />
	</lams:Tabs>
	<script type="text/javascript">
		var initialTabId = "${mdlQuizDTO.currentTab}";
	</script>
</div>

<div id="content">
	<lams:help toolSignature="<%= MdlQuizConstants.TOOL_SIGNATURE %>" module="monitoring"/>

	<lams:TabBody id="1" titleKey="button.summary" page="summary.jsp" />
</div>

<div id="footer"></div>
