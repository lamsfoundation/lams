<%@ include file="/includes/taglibs.jsp"%>


	
			<div class="field-name title-space-top">
				<fmt:message key="message.label.wikiTitle" />
			</div>
			
			
			<html:text size="50" tabindex="1" property="message.subject" />
			<html:errors property="message.subject" />
		
			<div class="field-name title-space-top">
				<fmt:message key="message.label.pageContent" />*
			</div>
			
			<%@include file="bodyarea.jsp"%>
		
		<div class="space-bottom-top">
<div class="right-buttons">
			<html:submit styleClass="button">
				<fmt:message key="button.submit" />
			</html:submit>
			<c:set var="backToTopic">
				<html:rewrite page="/learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${sessionMap.rootUid}&create=${topic.message.created.time}" />
			</c:set>
			<html:button property="goback" onclick="javascript:location.href='${backToTopic}';" styleClass="button">
				<fmt:message key="button.cancel" />
			</html:button>
			
</div>
</div>
