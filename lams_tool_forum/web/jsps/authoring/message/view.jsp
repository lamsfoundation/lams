<%@ include file="/includes/taglibs.jsp" %>
<script type="text/javascript" src="<html:rewrite page='/includes/scripts.jsp'/>"></script>
<script type="text/javascript">
	function success(){
		var flag = "<c:out value="${SUCCESS_FLAG}"/>";
		if(flag == "EDIT_SUCCESS"){
			window.opener.parent.location.href = "<html:rewrite page='/authoring/finishTopic.do'/>";
		}
	}
	success();
</script>
<div align="center">
<fieldset>
	<%@ include file="/jsps/message/topicview.jsp" %>
 </fieldset>
</div>
