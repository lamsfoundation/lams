<%@ include file="/common/taglibs.jsp"%>

<table cellpadding="0">
	<tr>
		<td>
			<span  class="field-name"><fmt:message key="message.label.subject" /></span><BR>		
			<html:text size="30" tabindex="1" property="message.subject" maxlength="60" />
			<br>
			<html:errors property="message.subject" />
		</td>
	</tr>
	<tr>
		<td>
			<span  class="field-name"><fmt:message key="message.label.body" />*</span><BR>
			<%@include file="bodyarea.jsp"%>
		</td>
	</tr>

	<div class="right-buttons">
	<tr>
		<td>
		<button type="submit" data-theme="b" id="submit-button">
			<fmt:message key="button.submit" />
		</button>
		<c:set var="backToForum">
			<html:rewrite page="/learning/viewForum.do?toolSessionID=${sessionMap.toolSessionID}&hideReflection=${sessionMap.hideReflection}" />
		</c:set>
		<a href="${backToForum}" data-role="button" data-theme="c">
			<fmt:message key="button.cancel" />
		</a>
		</td>
	</tr>
	</div>

</table>
