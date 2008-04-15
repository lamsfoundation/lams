<%@ include file="/includes/taglibs.jsp"%>
<%@ include file="../toolbarButtons.jsp"%>

<div id="fckbox" style="visibility: hidden">
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<lams:FCKEditor id="message.body"
			value="${formBean.message.body}"
			toolbarSet="Custom-Learner"></lams:FCKEditor>
<BR>
<html:errors property="message.body" />

<div>
