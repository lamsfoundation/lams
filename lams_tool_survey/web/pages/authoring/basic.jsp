<%@ include file="/common/taglibs.jsp" %>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
<script lang="javascript">
<!-- Common Javascript functions for LAMS -->

	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("questionInputArea");
		if(area != null){
			area.style.width="670px";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
	}
	function hideMessage(){
		var area=document.getElementById("questionInputArea");
		if(area != null){
			area.style.width="0px";
			area.style.height="0px";
			area.style.display="none";
		}
	}

	
	function editItem(idx,sessionMapID){
		 var reqIDVar = new Date();
		var url = "<c:url value="/authoring/editItemInit.do?itemIndex="/>" + idx +"&reqID="+reqIDVar.getTime()+"&sessionMapID="+sessionMapID;;
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
	function deleteItemLoading(){
		showBusy(surveyListTargetDiv);
	}
	function deleteItemComplete(){
		hideBusy(surveyListTargetDiv);
	}

</script>
	<!-- Basic Tab Content -->
	<table>
		<tr>
			<td colspan="2">
				<div class="field-name" style="text-align: left;">
					<fmt:message key="label.authoring.basic.title"></fmt:message>
				</div>
				<html:text property="survey.title" style="width: 100%;"></html:text>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div class="field-name" style="text-align: left;">
					<fmt:message key="label.authoring.basic.instruction"></fmt:message>
				</div>
				<lams:FCKEditor id="survey.instructions"
					value="${formBean.survey.instructions}"
					contentFolderID="${formBean.contentFolderID}"></lams:FCKEditor>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div id="surveyListArea">
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
							<a href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?itemType=1&sessionMapID=${formBean.sessionMapID}"/>');">
								<fmt:message key="label.authoring.basic.add.survey.question" />
							</a>
						</td>
						<td>
							<a href="javascript:showMessage('<html:rewrite page="/authoring/newItemInit.do?itemType=3&sessionMapID=${formBean.sessionMapID}"/>');">
								<fmt:message key="label.authoring.basic.add.survey.open.question" />
							</a>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td  colspan="2">
				<iframe onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'" 
					id="questionInputArea" name="questionInputArea" style="width:0px;height:0px;border:0px;display:none" frameborder="no" scrolling="no">
				</iframe>
			</td>
		</tr>
	</table>
