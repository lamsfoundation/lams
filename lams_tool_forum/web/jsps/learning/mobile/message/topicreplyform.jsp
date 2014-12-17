<%@ include file="/common/taglibs.jsp"%>

		<div class="field-name title-space-top">
			<fmt:message key="message.label.subject" />
		</div>
		
		<html:text size="30" tabindex="1" property="message.subject" maxlength="60" />
		<html:errors property="message.subject" />
		
		<div class="field-name title-space-top">
			<fmt:message key="message.label.body" />
			*
		</div>
		<%@include file="bodyarea.jsp"%>
		
		<div class="space-bottom-top">
		<div class="right-buttons">
			<button type="submit" data-theme="b" class="noLineHeight" id="submit-button">
				<fmt:message key="button.submit" />
			</button>
			<c:set var="backToTopic">
				<html:rewrite
					page="/learning/viewTopic.do?sessionMapID=${sessionMapID}&topicID=${sessionMap.rootUid}&create=${message.created.time}&hideReflection=${sessionMap.hideReflection}" />
			</c:set>
			<a name="goback" href="${backToTopic}" data-role="button" data-theme="c">
				<fmt:message key="button.cancel" />
			</a>
		</div>
		</div>
