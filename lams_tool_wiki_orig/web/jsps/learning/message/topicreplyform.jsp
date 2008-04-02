<%@ include file="/includes/taglibs.jsp"%>


		<div class="field-name title-space-top">
			<fmt:message key="message.label.subject" />
		</div>
		
		
		<html:text size="30" tabindex="1" property="message.subject" />
		<html:errors property="message.subject" />
		
		<div class="field-name title-space-top">
			<fmt:message key="message.label.body" />
			*
		</div>
		<%@include file="bodyarea.jsp"%>
		
		<c:if test="${sessionMap.allowUpload}">
		
			<div class="field-name title-space-top">
				<fmt:message key="message.label.attachment" />
			</div>
			<html:file tabindex="3" property="attachmentFile" />
			<html:errors property="message.attachment" />
		
		</c:if>
		<div class="space-bottom-top">
		<div class="right-buttons">
			<html:submit styleClass="button">
				<fmt:message key="button.submit" />
			</html:submit>
			<c:set var="backToTopic">
				<html:rewrite
					page="/learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${sessionMap.rootUid}&create=${message.created.time}" />
			</c:set>
			<html:button property="goback"
				onclick="javascript:location.href='${backToTopic}';"
				styleClass="button">
				<fmt:message key="button.cancel" />
			</html:button>
		</div>
		</div>
