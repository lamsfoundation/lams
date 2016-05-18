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
	
	function resizeIframe() {
		if (document.getElementById('TB_iframeContent') != null) {
		    var height = top.window.innerHeight;
		    if ( height == undefined || height == 0 ) {
		    	// IE doesn't use window.innerHeight.
		    	height = document.documentElement.clientHeight;
		    	// alert("using clientHeight");
		    }
			// alert("doc height "+height);
		    height -= document.getElementById('TB_iframeContent').offsetTop + 260;
		    document.getElementById('TB_iframeContent').style.height = height +"px";
	
			TB_HEIGHT = height + 28;
			tb_position();
		}
	};
	window.onresize = resizeIframe;

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
	<a href="<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=2"/>&KeepThis=true&TB_iframe=true&height=540&width=580&modal=true" class="btn btn-default btn-sm thickbox">
		<fmt:message key="label.authoring.basic.upload.common.cartridge" />
	</a>
</p>
