<%@ include file="/includes/taglibs.jsp" %>
<script type="text/javascript" src="<html:rewrite page='/includes/scripts.jsp'/>"></script>
<script type="text/javascript">
	function success(){
		var flag = "<c:out value="${SUCCESS_FLAG}"/>";
		if(flag == "ATT_SUCCESS_FLAG"){
			window.opener.parent.location.href = "<html:rewrite page='/authoring/finishTopic.do'/>";
		}
	}
	success();
</script>
<html:errors property="error" />
<div align="center">
<html:form action="/authoring/updateTopic.do" focus="message.subject"
	onsubmit="return validateMessageForm(this);"  enctype="multipart/form-data">
	<fieldset>
		<input type="hidden" name="topicIndex" value="<c:out value="${topicIndex}"/>">
		<%@ include file="/jsps/message/topiceditform.jsp" %>
	 </fieldset>
 </html:form>
</div>
