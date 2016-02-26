<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="main"/>

		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/commonCartridgeItem.js'/>"></script>
	</lams:head>
	
	<body class="stripes" onload="parent.resizeIframe();">
	<div id="content" style="width: 93%; text-align: center;">
		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="commonCartridgeItemForm" enctype="multipart/form-data">
			<html:hidden property="sessionMapID" />
			<input type="hidden" name="itemType" id="itemType" value="2" />
			<html:hidden property="itemIndex" />

			<h1 class="space-bottom">
				<fmt:message key="label.authoring.basic.upload.common.cartridge" />
			</h1>
			
			<c:set var="itemAttachment" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<div id="itemAttachmentArea">
				<%@ include file="/pages/authoring/parts/itemattachment.jsp"%>
			</div>
			
			<lams:ImgButtonWrapper >
				<a href="#" onclick="self.parent.tb_remove();" onmousedown="self.focus();" class="button space-left right-buttons">
					<fmt:message key="label.cancel" /> 
				</a>
				<a href="#" onclick="submitCommonCartridgeItem();" onmousedown="self.focus();" class="button right-buttons">
					<fmt:message key="button.upload" />
				</a>
			</lams:ImgButtonWrapper>
			<br />

		</html:form>
		
	</div>	
	</body>
</lams:html>
