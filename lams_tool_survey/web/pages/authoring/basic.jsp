<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"
	value="<%=request
									.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<script lang="javascript">
<!-- Common Javascript functions for LAMS -->

	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("questionInputArea");
		if(area != null){
			area.style.width="100%";
			area.src=url;
			area.style.display="block";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="none";
		}
		
		location.hash = "questionInputArea";
	}
	function hideMessage(){
		var area=document.getElementById("questionInputArea");
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

	
	function editItem(idx,itemType,sessionMapID,contentFolderID){
		var reqIDVar = new Date();
		var url = "<c:url value="/authoring/editItemInit.do?itemIndex="/>" + idx +"&itemType="+itemType+"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID+"&contentFolderID="+contentFolderID;
		showMessage(url);
	}

	function copyItem(idx,itemType,sessionMapID,contentFolderID){
		var reqIDVar = new Date();
		var url = "<c:url value="/authoring/copyItemInit.do?itemIndex="/>" + idx +"&itemType="+itemType+"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID+"&contentFolderID="+contentFolderID;
		showMessage(url);
	}
	
	//The panel of survey list panel
	var surveyListTargetDiv = "surveyListArea";
	function deleteItem(idx,sessionMapID){
		var url = "<c:url value="/authoring/removeItem.do"/>";
	    var reqIDVar = new Date();
		var param = "itemIndex=" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		deleteItemLoading();
	    var myAjax = new Ajax.Updater(
		    	surveyListTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:deleteItemComplete,
		    		evalScripts:true
		    	}
	    );
	}
	function upQuestion(idx,sessionMapID){
		var url = "<c:url value="/authoring/upItem.do"/>";
	    var reqIDVar = new Date();
		var param = "itemIndex=" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		deleteItemLoading();
	    var myAjax = new Ajax.Updater(
		    	surveyListTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:deleteItemComplete,
		    		evalScripts:true
		    	}
	    );
	}
	function downQuestion(idx,sessionMapID){
		var url = "<c:url value="/authoring/downItem.do"/>";
	    var reqIDVar = new Date();
		var param = "itemIndex=" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		deleteItemLoading();
	    var myAjax = new Ajax.Updater(
		    	surveyListTargetDiv,
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
		showBusy(surveyListTargetDiv);
	}
	function deleteItemComplete(){
		hideBusy(surveyListTargetDiv);
	}

	function showBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			Element.show(targetDiv+"_Busy");
		}
	}
	function hideBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			Element.hide(targetDiv+"_Busy");
		}				
	}

	function resizeOnMessageFrameLoad(){
		var messageAreaFrame = document.getElementById("questionInputArea");
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
			<html:text property="survey.title" style="width: 99%;"></html:text>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instruction"></fmt:message>
			</div>
			<lams:CKEditor id="survey.instructions"
				value="${formBean.survey.instructions}"
				contentFolderID="${formBean.contentFolderID}"></lams:CKEditor>
		</td>
	</tr>

</table>
<div id="surveyListArea">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/itemlist.jsp"%>
</div>

<p align="center">
	<a
		href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?itemType=1&contentFolderID=${formBean.contentFolderID}&sessionMapID=${formBean.sessionMapID}"/>');">
		<fmt:message key="label.authoring.basic.add.survey.question" /> </a>

	<a
		href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?itemType=3&contentFolderID=${formBean.contentFolderID}&sessionMapID=${formBean.sessionMapID}"/>');"
		class="space-left"> <fmt:message
			key="label.authoring.basic.add.survey.open.question" /> </a>
</p>

<p>
	<iframe
		onload="javascript:resizeOnMessageFrameLoad();"
		id="questionInputArea" name="questionInputArea"
		style="width:0px;height:0px;border:0px;display:none" frameborder="no"
		scrolling="no">
	</iframe>
</p>

