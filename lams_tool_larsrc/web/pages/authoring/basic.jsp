<%@ include file="/common/taglibs.jsp"%>

<%-- If you change this file, remember to update the copy made for CNG-36 --%>

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<script lang="javascript">
<!-- Common Javascript functions for LAMS -->

	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("reourceInputArea");
		if(area != null){
			area.style.width="90%";
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

	function previewItem(type,idx,sessionMapID){
		//1:url, 2:file, 3:website,4:learning object
		//This mode is special for unsaved author page. It is different with the author mode in preview 
		var myUrl = "<c:url value='/reviewItem.do?mode=author_session&itemIndex='/>"+idx+"&sessionMapID="+sessionMapID;
		launchPopup(myUrl,"Review");
	}
	
	function editItem(idx,sessionMapID){
		 var reqIDVar = new Date();
		var url = "<c:url value="/authoring/editItemInit.do?itemIndex="/>" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		showMessage(url);
	}
	//The panel of resource list panel
	var resourceListTargetDiv = "resourceListArea";
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
		showBusy(resourceListTargetDiv);
	}
	function deleteItemComplete(){
		hideBusy(resourceListTargetDiv);
	}

	function switchItem(resourceItemOrderID1, resourceItemOrderID2, sessionMapID) {
		resourceItemOrderID1++;
		resourceItemOrderID2++;
		
		var url = "<c:url value="/authoring/switchResourceItemPosition.do"/>";
		deleteItemLoading();
		
		$("#" + resourceListTargetDiv).load(
			url,
			{
				sessionMapID: sessionMapID,
				resourceItemOrderID1: resourceItemOrderID1, 
				resourceItemOrderID2: resourceItemOrderID2
			},
			function() {
				deleteItemComplete();
			}
		);
	}

</script>
<!-- Basic Tab Content -->
<table>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.title"></fmt:message>
			</div>
			<html:text property="resource.title" style="width: 99%;"></html:text>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instruction"></fmt:message>
			</div>
			<lams:CKEditor id="resource.instructions"
				value="${formBean.resource.instructions}"
				contentFolderID="${formBean.contentFolderID}"></lams:CKEditor>
		</td>
	</tr>

</table>

<div id="resourceListArea">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/itemlist.jsp"%>
</div>

<p align="center">
			<a
				href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=1"/>');">
				<fmt:message key="label.authoring.basic.add.url" /></a> 
				 <a
				href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=2"/>');"  class="space-left">
				<fmt:message key="label.authoring.basic.add.file" /></a>
				 <a
				href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=3"/>');" class="space-left">
				<fmt:message key="label.authoring.basic.add.website" /></a>
				<a
				href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=4"/>');" class="space-left">
				<fmt:message key="label.authoring.basic.add.learning.object" /> </a>
</p>

<p>
	<iframe
		onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
		id="reourceInputArea" name="reourceInputArea"
		style="width:0px;height:0px;border:0px;display:none" frameborder="no"
		scrolling="no">
	</iframe>
</p>
