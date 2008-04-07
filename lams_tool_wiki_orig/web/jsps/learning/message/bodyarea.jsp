<%@ include file="/includes/taglibs.jsp"%>

<div>
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<lams:FCKEditor id="message.body"
			value="${formBean.message.body}"
			toolbarSet="Default-Learner"></lams:FCKEditor>
<BR>
<html:errors property="message.body" />

<div>
