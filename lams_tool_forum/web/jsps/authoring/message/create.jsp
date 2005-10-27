<%@ include file="/includes/taglibs.jsp" %>
<script type="text/javascript">
<!--

function closeWin(){
	window.opener.parent.location.href = "<html:rewrite page='/authoring/finishTopic.do'/>";
	window.close();
}
//-->
</script>
<html:errors property="error" />
<div align="center">
<html:form action="/authoring/createTopic.do" focus="message.subject"
	onsubmit="return validateMessageForm(this);" >
<fieldset>
<%@ include file="/jsps/message/topiclist.jsp" %>
<%@ include file="/jsps/message/form.jsp" %>
 </fieldset>
 </html:form>
</div>
