<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<script lang="javascript">

	function previewItem(type,idx,sessionMapID) {
		//This mode is special for unsaved author page. It is different with the author mode in preview 
		var myUrl = "<c:url value='/reviewItem.do?mode=author_session&itemIndex='/>"+idx+"&sessionMapID="+sessionMapID;
		launchPopup(myUrl,"Review");
	}
	
	var commonCartridgeListTargetDiv = "#commonCartridgeListArea";
	function deleteItem(idx,sessionMapID) {
		var	deletionConfirmed = confirm("<fmt:message key="warning.msg.authoring.do.you.want.to.delete"></fmt:message>");

		if (deletionConfirmed) {
			var reqIDVar = new Date();
			var url = "<c:url value="/authoring/removeItem.do"/>";
			//deleteItemLoading();
			$(commonCartridgeListTargetDiv).load(
				url,
				{
					itemIndex: idx, 
					sessionMapID: sessionMapID,
					reqID: reqIDVar.getTime()
				},
				function(){
					//deleteItemComplete();
					refreshThickbox();
				}
			);
		};
	}
	
	function deleteItemLoading() {
		showBusy(commonCartridgeListTargetDiv);
	}
	function deleteItemComplete() {
		hideBusy(commonCartridgeListTargetDiv);
	}
	
	function refreshThickbox() {   
		tb_init('a.thickbox, area.thickbox, input.thickbox');//pass where to apply thickbox
	};

</script>

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="commonCartridge.title">
    	<fmt:message key="label.authoring.basic.title"/>
    </label>
    <html:text property="commonCartridge.title" styleClass="form-control"/>
</div>

<div class="form-group">
    <label for="commonCartridge.instructions">
    	<fmt:message key="label.authoring.basic.instruction"/>
    </label>
	<lams:CKEditor id="commonCartridge.instructions" value="${formBean.commonCartridge.instructions}"
			contentFolderID="${formBean.contentFolderID}" height="400">
	</lams:CKEditor>
</div>

<div id="commonCartridgeListArea">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/itemlist.jsp"%>
</div>

<p align="center">
<!--
	<a href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=1"/>');">
		<fmt:message key="label.authoring.basic.add.basiclti.tool" />
	</a>
-->	
	<a href="<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=2"/>&KeepThis=true&TB_iframe=true&modal=true" class="btn btn-default btn-sm thickbox">
		<fmt:message key="label.authoring.basic.upload.common.cartridge" />
	</a>
</p>
