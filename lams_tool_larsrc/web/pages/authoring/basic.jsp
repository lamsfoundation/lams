<%@ include file="/common/taglibs.jsp" %>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
<script lang="javascript">
<!-- Common Javascript functions for LAMS -->

	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("reourceInputArea");
		if(area != null){
			area.style.width="670px";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
		document.getElementById("saveCancelButtons").style.visibility="hidden";
	}
	function hideMessage(){
		var area=document.getElementById("reourceInputArea");
		if(area != null){
			area.style.width="0px";
			area.style.height="0px";
			area.style.display="none";
		}
		document.getElementById("saveCancelButtons").style.visibility="visible";
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
		var param = "itemIndex=" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		deleteItemLoading();
	    var myAjax = new Ajax.Updater(
		    	resourceListTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:deleteItemComplete,
		    		evalScripts:true
		    	}
	    );
	}
	function deleteItemLoading(){
		showBusy(resourceListTargetDiv);
	}
	function deleteItemComplete(){
		hideBusy(resourceListTargetDiv);
	}

</script>
	<!-- Basic Tab Content -->
	<table>
		<tr>
			<td colspan="2">
				<div class="field-name" style="text-align: left;">
					<fmt:message key="label.authoring.basic.title"></fmt:message>
				</div>
				<html:text property="resource.title" style="width: 100%;"></html:text>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div class="field-name" style="text-align: left;">
					<fmt:message key="label.authoring.basic.instruction"></fmt:message>
				</div>
				<lams:FCKEditor id="resource.instructions"
					value="${formBean.resource.instructions}"
					contentFolderID="${formBean.contentFolderID}"></lams:FCKEditor>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div id="resourceListArea">
						<c:set var="sessionMapID" value="${formBean.sessionMapID}"/>
						<%@ include file="/pages/authoring/parts/itemlist.jsp"%>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table class="forms">
					<tr>
						<td>
							<a href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=1"/>');">
								<fmt:message key="label.authoring.basic.add.url" />
							</a>
						</td>
						<td>
							<a href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=2"/>');">
								<fmt:message key="label.authoring.basic.add.file" />
							</a>
						</td>
						<td>
							<a href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=3"/>');">
								<fmt:message key="label.authoring.basic.add.website" />
							</a>
						</td>
						<td>
							<a href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?sessionMapID=${formBean.sessionMapID}&itemType=4"/>');">
								<fmt:message key="label.authoring.basic.add.learning.object"/>
							</a>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td  colspan="2">
				<iframe onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'" 
					id="reourceInputArea" name="reourceInputArea" style="width:0px;height:0px;border:0px;display:none" frameborder="no" scrolling="no">
				</iframe>
			</td>
		</tr>
	</table>
