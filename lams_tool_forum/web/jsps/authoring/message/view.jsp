<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
		<script type="text/javascript">
			function success(){
				var flag = "<c:out value="${SUCCESS_FLAG}"/>";
				if(flag == "EDIT_SUCCESS"){
					var d = new Date()
					var t = d.getTime()
					loadDoc("<html:rewrite page='/authoring/refreshTopic.do'/>"+"?="+escape(t),window.parent.document.getElementById("messageListArea"));
					window.parent.hideMessage();
				}
			}
		</script>

	</head>
	<body class="tabpart">
		<script type="text/javascript">
	success();
</script>
		<table class="forms">
			<!-- Basic Info Form-->
			<tr>
				<td>
					<div align="center">
						<%@ include file="/jsps/message/topicview.jsp"%>
					</div>
				</td>
			</tr>
		</table>

	</body>
</html>

