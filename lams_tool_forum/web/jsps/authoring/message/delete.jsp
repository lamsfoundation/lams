<%@ include file="/includes/taglibs.jsp" %>

<script type="text/javascript" src="<html:rewrite page='/includes/javascript/xmlrequest.js'/>"></script>
<script type="text/javascript">
	function success(){
		var flag = "<c:out value="${SUCCESS_FLAG}"/>";
		if(flag == "DELETE_SUCCESS"){
			var d = new Date()
			var t = d.getTime()
			loadDoc("<html:rewrite page='/authoring/refreshTopic.do'/>"+"?="+escape(t),window.parent.document.getElementById("messageListArea"));
			window.parent.hideMessage();
		}
	}
	success();
</script>