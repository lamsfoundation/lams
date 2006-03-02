<%@ include file="/includes/taglibs.jsp" %>
<script type="text/javascript" src="<html:rewrite page='/includes/javascript/xmlrequest.js'/>"></script>
<script type="text/javascript">
	function success(){
		var flag = "<c:out value="${SUCCESS_FLAG}"/>";
		if(flag == "CREATE_SUCCESS"){
			var d = new Date()
			var t = d.getTime()
			loadDoc("<html:rewrite page='/authoring/refreshTopic.do'/>"+"?reqID="+escape(t),window.parent.document.getElementById("messageListArea"));
			window.parent.hideMessage();
		}
	}
	success();
</script>

<html:errors property="error" />
<div align="center">
<html:form action="/authoring/createTopic.do" focus="message.subject" enctype="multipart/form-data">
	<fieldset>
		<%@ include file="/jsps/message/topicform.jsp" %>
	 </fieldset>
</html:form>
</div>
