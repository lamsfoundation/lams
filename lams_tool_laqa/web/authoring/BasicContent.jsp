<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("messageArea");
		if(area != null){
			area.style.width="95%";
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
	
	function removeQuestion(questionIndex)
	{
		document.QaAuthoringForm.questionIndex.value=questionIndex;
        submitMethod('removeQuestion');
	}

	function removeMonitoringQuestion(questionIndex)
	{
		document.QaMonitoringForm.questionIndex.value=questionIndex;
        submitMonitoringMethod('removeQuestion');
	}
	
	function resizeIframe(heightOffSet)
	{
		var iframe = document.getElementById("messageArea");
		iframe.style.height = parseInt(iframe.style.height) + heightOffSet + "px";
	}

</script>

<html:hidden property="questionIndex" />
<table cellpadding="0">

	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.title.col"></fmt:message>
			</div>
			<html:text property="title" style="width: 100%;"></html:text>
		</td>
	</tr>


	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.instructions.col"></fmt:message>
			</div>
			<lams:CKEditor id="instructions"
				value="${formBean.instructions}"
				contentFolderID="${formBean.contentFolderID}"></lams:CKEditor>
		</td>
	</tr>
</table>


<div id="resourceListArea">
	<%@ include file="/authoring/itemlist.jsp"%>
</div>

<p>
	<a href="javascript:showMessage('<html:rewrite page="/authoring.do?dispatch=newQuestionBox&contentFolderID=${formBean.contentFolderID}&httpSessionID=${formBean.httpSessionID}&toolContentID=${formBean.toolContentID}&usernameVisible=${formBean.usernameVisible}&showOtherAnswers=${formBean.showOtherAnswers}&lockWhenFinished=${formBean.lockWhenFinished}&questionsSequenced=${formBean.questionsSequenced}"/>');"
			class="button-add-item"> 
		<fmt:message key="label.add.new.question" /> 
	</a>
</p>

<p>
	<iframe onload="javascript:this.style.height=eval(this.contentWindow.document.body.scrollHeight+220)+'px';"
		id="messageArea" name="messageArea"
		style="width:0px;height:0px;border:0px;display:none" frameborder="no"
		scrolling="no">
	</iframe>
</p>
