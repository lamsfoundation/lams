<%@ include file="/includes/taglibs.jsp"%>
<%@ include file="../toolbarButtons.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<div id="fckbox" style="visibility: hidden">
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<lams:FCKEditor id="message.body"
			value="${formBean.message.body}"
			toolbarSet="Custom-Learner">
	</lams:FCKEditor>
<BR>

<script type="text/javascript">
	window.onload = function () {
		var oEditor = FCKeditorAPI.GetInstance('message.body');
		oEditor.toolSessionId ="${sessionMap.toolSessionID}";
		oEditor.sessionMapID ="${sessionMapID}";
		oEditor.wikiID ="${sessionMap.wiki_id}";
	}
</script> 
<html:errors property="message.body" />

<div>
