<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
<!--
	window.resizeTo(550,300)
	window.opener.refresh();
-->
</script>

<lams:html>

	<lams:head>  
		<lams:css/>
	</lams:head>
	
	<body class="stripes">
	
		<div id="content" align="center">

			<c:choose>
				<c:when test="${success}">
					<p class="info">
						<fmt:message key="message.imageEdited" />
					<p>
				</c:when>
				<c:otherwise>
					<p class="warning">
						<fmt:message key="error.retreiving.image" />
					</p>
				</c:otherwise>
			</c:choose>
			<div class="space-bottom-top">
			<html:submit styleClass="button" onclick="javascript:window.close();">
					<fmt:message key="button.close" />
			</html:submit>
			</div>
		</div>
		<div class="footer"></div>
	</body>
</lams:html>


