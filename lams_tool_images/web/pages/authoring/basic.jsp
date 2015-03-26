<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript"> 
	var pathToImageFolder = "<html:rewrite page='/includes/images/'/>"; 
</script>
<script type="text/javascript" src="<html:rewrite page='/includes/javascript/lytebox.js'/>" ></script>
<link rel="stylesheet" href="<html:rewrite page='/includes/css/lytebox.css'/>"  type="text/css">

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<script lang="javascript">
<!-- Common Javascript functions for LAMS -->

	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("reourceInputArea");
		if(area != null){
			area.style.width="100%";
			area.src=url;
			area.style.display="block";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="none";
		}
		location.hash = "reourceInputArea";
	}
	function hideMessage(){
		var area=document.getElementById("reourceInputArea");
		if(area != null){
			area.style.width="0px";
			area.style.height="0px";
			area.style.display="none";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="block";
		}
	}
	
	function editItem(idx,sessionMapID){
		var url = "<c:url value="/authoring/editImage.do?imageIndex="/>" + idx +"&sessionMapID="+sessionMapID;;
		showMessage(url);
	}
	//The panel of imageGallery list panel
	var imageGalleryListTargetDiv = "imageGalleryListArea";
	function deleteItem(idx, sessionMapID) {

		var	deletionConfirmed = confirm("<fmt:message key="warning.msg.authoring.do.you.want.to.delete"></fmt:message>");
		
		if (deletionConfirmed) {
			var url = "<c:url value="/authoring/removeImage.do"/>";
			var param = "imageIndex=" + idx +"&sessionMapID="+sessionMapID;;
			deleteItemLoading();
			$("#" + imageGalleryListTargetDiv).load(
				url,
				{
					itemIndex: idx,
					reqID: reqIDVar.getTime(), 
					sessionMapID: sessionMapID
				},
				function() {
					deleteItemComplete();
				}
			);
		}
	}
	
	function deleteItem(idx,sessionMapID){
		var url = "<c:url value="/authoring/removeItem.do"/>";
	    var reqIDVar = new Date();
		deleteItemLoading();
	    
		$("#" + resourceListTargetDiv).load(
			url,
			{
				itemIndex: idx,
				reqID: reqIDVar.getTime(), 
				sessionMapID: sessionMapID
			},
			function() {
				deleteItemComplete();
			}
		);
	}
	
	function deleteItemLoading(){
		showBusy(imageGalleryListTargetDiv);
	}
	function deleteItemComplete(){
		hideBusy(imageGalleryListTargetDiv);
		initLytebox();
	}
	
	function upImage(idx, sessionMapID){
		var url = "<c:url value="/authoring/upImage.do"/>";
	    var reqIDVar = new Date();
		deleteItemLoading();
		$("#" + imageGalleryListTargetDiv).load(
			url,
			{
				itemIndex: idx,
				reqID: reqIDVar.getTime(), 
				sessionMapID: sessionMapID
			},
			function() {
				deleteItemComplete();
			}
		);
	}
	function downImage(idx, sessionMapID){
		var url = "<c:url value="/authoring/downImage.do"/>";
	    var reqIDVar = new Date();
		deleteItemLoading();
		$("#" + imageGalleryListTargetDiv).load(
			url,
			{
				itemIndex: idx,
				reqID: reqIDVar.getTime(), 
				sessionMapID: sessionMapID
			},
			function() {
				deleteItemComplete();
			}
		);
	}
	
	function resizeOnMessageFrameLoad(){
		var messageAreaFrame = document.getElementById("reourceInputArea");
		messageAreaFrame.style.height=messageAreaFrame.contentWindow.document.body.scrollHeight+'px';
	}

</script>
<!-- Basic Tab Content -->
<table>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.title"></fmt:message>
			</div>
			<html:text property="imageGallery.title" style="width: 99%;"></html:text>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instruction"></fmt:message>
			</div>
			<lams:CKEditor id="imageGallery.instructions"
				value="${formBean.imageGallery.instructions}"
				contentFolderID="${formBean.contentFolderID}"
				width="100%">
			</lams:CKEditor>
		</td>
	</tr>

</table>

<div id="imageGalleryListArea">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/itemlist.jsp"%>
</div>

<p align="small-space-bottom">
	<a href="javascript:showMessage('<html:rewrite page="/authoring/newImageInit.do?sessionMapID=${formBean.sessionMapID}"/>');"  class="button-add-item">
		<fmt:message key="label.authoring.basic.add.image" />
	</a>
</p>

<p>
	<iframe
		onload="javascript:resizeOnMessageFrameLoad();"
		id="reourceInputArea" name="reourceInputArea"
		style="width:0px;height:0px;border:0px;display:none" frameborder="no"
		scrolling="no">
	</iframe>
</p>
