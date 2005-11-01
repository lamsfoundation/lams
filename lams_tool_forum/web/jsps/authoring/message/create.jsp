<%@ include file="/includes/taglibs.jsp" %>
<script type="text/javascript" src="<html:rewrite page='/includes/scripts.jsp'/>"></script>
<script type="text/javascript">
	function success(){
		var flag = "<c:out value="${SUCCESS_FLAG}"/>";
		if(flag == "SUCCESS"){
			closeWin();
		}
	}
	success();
</script>

<html:errors property="error" />
<div align="center">
<html:form action="/authoring/createTopic.do" focus="message.subject"
	onsubmit="return validateMessageForm(this);"  enctype="multipart/form-data">
<fieldset>
<%@ include file="/jsps/message/topiclist.jsp" %>
<%@ include file="/jsps/message/topicform.jsp" %>
 </fieldset>
 </html:form>
</div>
