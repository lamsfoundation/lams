<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.scratchie.ScratchieConstants"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:if test="${mode == null}"><c:set var="mode" value="${sessionMap.mode}" /></c:if>

<lams:html>
	<lams:head>
		<title><fmt:message key="label.author.title" /></title>

		<%@ include file="/common/tabbedheader.jsp"%>
		<link href="<lams:LAMSURL/>css/thickbox.css" rel="stylesheet" type="text/css" media="screen">
		<style type="text/css">
			#confidence-levels-activity, #activity-providing-vsa-answers {
				display: inline-block;
				width: 200px;
			}
			#display-confidence-levels-activities, #display-activities-providing-vsa-answers {
				margin-top: 8px;
			}
			#question-bank-div {
				margin-top: 75px;
			}
			.question-type-alert {
				white-space: nowrap;
				display: inline-block;
				margin-top: 8px;
			}
			.newer-version-prompt {
				text-align: left;
				color: orange;
				font-size: 1.3em;
			}
			.question-version-dropdown {
				margin-top: -3px;
			}

			.question-version-dropdown .dropdown-menu {
				min-width: 160px;
			}

			.question-version-dropdown li a {
				display: inline-block;
			}
			.question-version-dropdown li.disabled a:first-child {
				text-decoration: underline;
			}
		</style>

		<script>
			const hasMatchingRatActivity = ${not empty sessionMap.hasMatchingRatActivity and sessionMap.hasMatchingRatActivity};
			let questionsEdited = false;

			function init(){
				var tag = document.getElementById("currentTab");
				if (tag.value != "") {
					selectTab(tag.value);
				} else {
					selectTab(1); //select the default tab;
				}
			}

			function doSelectTab(tabId) {
				// start optional tab controller stuff
				var tag = document.getElementById("currentTab");
				tag.value = tabId;
				// end optional tab controller stuff
				selectTab(tabId);
			}

			// avoid name clash between bootstrap and jQuery UI
			$.fn.bootstrapTooltip = $.fn.tooltip.noConflict();

			function validateForm(){
				var timeLimit = $('#relativeTimeLimit').val();
				if (!timeLimit || timeLimit < 1) {
					$('#relativeTimeLimit').val(0);
				}

				$('#syncRatQuestions').val(hasMatchingRatActivity && questionsEdited &&
						confirm("<spring:escapeBody javaScriptEscape='true'><fmt:message key='message.authoring.rat.questions.sync'/></spring:escapeBody>"));

				return true;
			}
		</script>

	</lams:head>
	<body class="stripes" onLoad="init()">
	<form:form action="/lams/tool/lascrt11/authoring/update.do" modelAttribute="authoringForm" method="post" id="authoringForm"
			   onSubmit="javascript:return validateForm()">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<form:hidden path="scratchie.contentId" />
		<form:hidden path="sessionMapID" />
		<form:hidden path="contentFolderID" />
		<input type="hidden" name="mode" value="${mode}"/>
		<input type="hidden" id="syncRatQuestions" name="syncRatQuestions" value="false">
		<form:hidden path="currentTab" id="currentTab" />
		<input type="hidden" name="itemList" id="itemList" />

		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page title="${title}" type="navbar">

			<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ScratchieConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="label.authoring.heading.basic" />
				<lams:Tab id="2" key="label.authoring.heading.advance" />
			</lams:Tabs>

			<lams:TabBodyArea>
				<lams:errors/>

				<lams:TabBodys>
					<!-- tab content 1 (Basic) -->
					<lams:TabBody id="1" page="basic.jsp" />
					<!-- end of content (Basic) -->

					<!-- tab content 2 (Advanced) -->
					<lams:TabBody id="2" page="advance.jsp" />
					<!-- end of content (Advanced) -->
				</lams:TabBodys>

				<!-- Button Row -->
				<%--  Default value 
					cancelButtonLabelKey="label.authoring.cancel.button"
					saveButtonLabelKey="label.authoring.save.button"
					cancelConfirmMsgKey="authoring.msg.cancel.save"
					accessMode="author"
				--%>
				<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="<%=ScratchieConstants.TOOL_SIGNATURE%>"
									  toolContentID="${authoringForm.scratchie.contentId}" accessMode="${mode}" defineLater="${mode=='teacher'}"
									  customiseSessionID="${authoringForm.sessionMapID}" contentFolderID="${authoringForm.contentFolderID}" />
			</lams:TabBodyArea>

			<div id="footer"></div>

		</lams:Page>

	</form:form>

	</body>
</lams:html>