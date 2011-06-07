<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed"/>

		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/commonCartridgeItem.js'/>"></script>
	</lams:head>
	
	<body class="stripes" onload="parent.resizeIframe();">
	<div id="content" style="width: 90%; text-align: center;">
		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="commonCartridgeItemForm" enctype="multipart/form-data">
			<html:hidden property="sessionMapID" />
			<input type="hidden" name="itemType" id="itemType" value="2" />
			<html:hidden property="itemIndex" />

			<h2 class="no-space-left">
				<fmt:message key="label.authoring.basic.upload.common.cartridge" />
			</h2>
			<br />
			
			<c:set var="itemAttachment" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<div id="itemAttachmentArea">
				<%@ include file="/pages/authoring/parts/itemattachment.jsp"%>
			<a href="#" onclick="submitCommonCartridgeItem()" class="button"><fmt:message
					key="button.upload" /> </a>				
			</div>
			<br /><br />


		</html:form>
		
	</div>	
	</body>
</lams:html>
