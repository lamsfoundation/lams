<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="main" />
	<script type="text/javascript">
	   <%-- user for  eadventureitem.js --%>
	   var removeGameAttachmentUrl = "<c:url value='/authoring/removeGameAttachment.do'/>";
	</script>
	<script type="text/javascript"
		src="<html:rewrite page='/includes/javascript/eadventureitem.js'/>">
	</script>
	</lams:head>
	<body class="tabpart">

	<script lang="javascript">
		if (window.parent && window.parent.hideShowGame) {
			window.parent.hideShowGame();
		} else if (window.top && window.top.hideShowGame){
			window.top.hideShowGame();
		}
	</script>
		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>
		
		<html:form action="/authoring/saveOrUpdateGame" method="post"
			styleId="eadventureGameForm" enctype="multipart/form-data">
			<html:hidden property="sessionMapID" />
			
			<div class="field-name space-top">
				eAdventure exported game zip
			</div>

			<!--<c:set var="gameAttachment"
				value="<%=request
										.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />-->
			<div id="gameAttachmentArea">
				<input type="file" name="file" />
			</div>

		</html:form>
		
		<lams:ImgButtonWrapper>
			<a href="#" onclick="submitEadventureGame()" class="button-add-item">Add eAdventure game </a>
			<a href="javascript:;" onclick="cancelEadventureGame()"
				class="button space-left"><fmt:message key="label.cancel" /> </a>
		</lams:ImgButtonWrapper>
		<br />
	</body>
</lams:html>
