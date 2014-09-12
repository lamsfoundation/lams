<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!--  Basic Tab Content -->

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("messageArea");
		if(area != null){
			area.style.width="100%";
			area.src=url;
			area.style.display="block";
		}
		
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="none";
		}
		
		location.hash = "messageArea";
	}
	function hideMessage(){
		var area=document.getElementById("messageArea");
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

	function editTopic(topicIndex, sessionMapID){
		var reqIDVar = new Date();
		var url = "<c:url value="/authoring/editTopic.do?topicIndex="/>" + topicIndex +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		showMessage(url);
	}
	//The panel of taskList list panel
	var topicListTargetDiv = "messageListArea";
	function deleteTopic(topicIndex, sessionMapID){
		var	deletionConfirmed = confirm("<fmt:message key='label.authoring.basic.do.you.want.to.delete'></fmt:message>");
		
		if (deletionConfirmed) {
			var url = "<c:url value="/authoring/deleteTopic.do"/>";
		    var reqIDVar = new Date();
			var param = "topicIndex=" + topicIndex +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
			deleteItemLoading();
		    var myAjax = new Ajax.Updater(
		    		topicListTargetDiv,
			    	url,
			    	{
			    		method:'get',
			    		parameters:param,
			    		onComplete:deleteItemComplete,
			    		evalScripts:true
			    	}
		    );
 		}
		
	}
	function deleteItemLoading(){
		showBusy(topicListTargetDiv);
	}
	function deleteItemComplete(){
		hideBusy(topicListTargetDiv);
	}
	
	function upTopic(topicIndex, sessionMapID){
		var url = "<c:url value="/authoring/upTopic.do"/>";
	    var reqIDVar = new Date();
		var param = "topicIndex=" + topicIndex +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		deleteItemLoading();
	    var myAjax = new Ajax.Updater(
	    		topicListTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:deleteItemComplete,
		    		evalScripts:true
		    	}
	    );
	}
	function downTopic(topicIndex, sessionMapID){
		var url = "<c:url value="/authoring/downTopic.do"/>";
	    var reqIDVar = new Date();
		var param = "topicIndex=" + topicIndex +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
		deleteItemLoading();
	    var myAjax = new Ajax.Updater(
	    		topicListTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:deleteItemComplete,
		    		evalScripts:true
		    	}
	    );
	}	

	function resizeOnMessageFrameLoad(){
            	var messageAreaFrame = document.getElementById("messageArea");
        	messageAreaFrame.style.height=messageAreaFrame.contentWindow.document.body.scrollHeight+'px';
 	}

</script>

<table>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.title"></fmt:message>
			</div>
			<html:text property="forum.title" style="width: 99%;"></html:text>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instruction"></fmt:message>
			</div>
			<lams:CKEditor id="forum.instructions"
				value="${formBean.forum.instructions}"
				contentFolderID="${formBean.contentFolderID}"></lams:CKEditor>
		</td>
	</tr>
</table>

<!-- Topics List Row -->
<div id="messageListArea">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/jsps/authoring/message/topiclist.jsp"%>
</div>

<p class="small-space-bottom">
	<a
		href="javascript:showMessage('<html:rewrite page="/authoring/newTopic.do?sessionMapID=${formBean.sessionMapID}"/>');"
		class="button-add-item"> <fmt:message
			key="label.authoring.create.new.topic" /> 
	</a>
</p>

<p>
	<iframe
		onload="javascript:resizeOnMessageFrameLoad();"
		id="messageArea" name="messageArea"
		style="width:0px;height:0px;border:0px;display:none" frameborder="no"
		scrolling="no">
	</iframe>
</p>
