<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/commonCartridgeItem.js'/>"></script>
</lams:head>
	
<body class="stripes">
	<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="commonCartridgeItemForm" enctype="multipart/form-data">
		<c:set var="itemAttachment" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	
		<c:set var="title"><fmt:message key="label.authoring.basic.upload.common.cartridge" /></c:set>
		<lams:Page title="${title}" type="learner">

			<%@ include file="/common/messages.jsp"%>
			
			<html:hidden property="sessionMapID" />
			<input type="hidden" name="itemType" id="itemType" value="2" />
			<html:hidden property="itemIndex" />

			<div class="form-group">
				<div id="itemAttachmentArea">
					<%@ include file="/pages/authoring/parts/itemattachment.jsp"%>
				</div>
			</div>		
		
			<div class="voffset10 pull-right">
				<a href="#nogo" onclick="javascript:submitCommonCartridgeItem();" onmousedown="self.focus();" class="btn btn-sm btn-default button-add-item">
					<fmt:message key="button.upload" />
				</a>
				<a href="#nogo" onclick="javascript:self.parent.tb_remove();" onmousedown="self.focus();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" /> 
				</a>
			</div>
			
		</lams:Page>

	</html:form>
</body>
</lams:html>
