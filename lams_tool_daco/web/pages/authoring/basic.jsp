<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="showMessageURL">
	<html:rewrite page='/authoring/newQuestionInit.do?sessionMapID=${formBean.sessionMapID}&questionType=' />
</c:set>

<script type="text/javascript">
	function showQuestionInputArea(url) {

		var area=document.getElementById("questionInputArea");
		if(area != null){
			area.style.width="650px";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="none";
		}
		location.hash = "questionInputArea";
	}
	
	//Hides the add/edit question area
	function hideQuestionInputArea(){
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
	
	//Shows the add/edit question area for the corresponding question type
   function showQuestionInputAreaByType(url){
	 var questionTypeDropdown=document.getElementById("questionType");
	 showQuestionInputArea(url.concat(questionTypeDropdown.selectedIndex + 1));
}

   //This mode is special for unsaved author page. It is different with the author mode in preview 
	function previewQuestion(questionIndex,sessionMapID){
		var myUrl = "<c:url value='/reviewQuestion.do?mode=author_session&questionIndex='/>"+questionIndex+"&sessionMapID="+sessionMapID;
		launchPopup(myUrl,"Review");
	}
	
	
	function editQuestion(questionIndex,sessionMapID){
		var reqID = new Date();
		var url = "<c:url value="/authoring/editQuestionInit.do?questionIndex="/>" + questionIndex +"&reqID="+reqID.getTime()+"&sessionMapID="+sessionMapID;
		showQuestionInputArea(url);
	}
	
	//Question list panel
	var questionListTargetDiv = "questionListArea";
		
	function deleteQuestion(questionIndex,sessionMapID){
		var url = "<c:url value='/authoring/removeQuestion.do'/>";
	    var reqID = new Date();
		var param = "questionIndex=" + questionIndex +"&reqID="+reqID.getTime()+"&sessionMapID="+sessionMapID;;
		deleteQuestionLoading();
	    var myAjax = new Ajax.Updater(
		    	questionListTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:deleteQuestionComplete,
		    		evalScripts:true
		    	}
	    );
	}
	
	function deleteQuestionLoading(){
		showBusy("#questionListArea");
	}
	function deleteQuestionComplete(){
		hideBusy("#questionListArea");
	}
</script>

<!-- Basic Tab Content -->

<table>
	<tr>
		<td colspan="2">
		<div class="field-name"><fmt:message key="label.authoring.basic.title" /></div>
		<html:text property="daco.title" style="width: 99%;"></html:text></td>
	</tr>
	<tr>
		<td colspan="2">
		<div class="field-name"><fmt:message key="label.authoring.basic.instruction"></fmt:message></div>
		<lams:FCKEditor id="daco.instructions" value="${formBean.daco.instructions}" contentFolderID="${formBean.contentFolderID}"></lams:FCKEditor></td>
	</tr>
</table>

<div id="questionListArea"><c:set var="sessionMapID" value="${formBean.sessionMapID}" />
<%@ include	file="/pages/authoring/parts/questionlist.jsp"%></div>
<p><select id="questionType" style="float: left">
	<option selected="selected"><fmt:message key="label.authoring.basic.textfield" /></option>
	<option><fmt:message key="label.authoring.basic.textarea" /></option>
	<option><fmt:message key="label.authoring.basic.number" /></option>
	<option><fmt:message key="label.authoring.basic.date" /></option>
	<option><fmt:message key="label.authoring.basic.file" /></option>
	<option><fmt:message key="label.authoring.basic.image" /></option>
	<option><fmt:message key="label.authoring.basic.radio" /></option>
	<option><fmt:message key="label.authoring.basic.dropdown" /></option>
	<option><fmt:message key="label.authoring.basic.checkbox" /></option>
	<option><fmt:message key="label.authoring.basic.longlat" /></option>
</select><html:link href="#" styleClass="button space-left" onclick="javascript:showQuestionInputAreaByType('${showMessageURL }')">
	<fmt:message key="label.authoring.basic.question.add" />
</html:link></p>
<p><iframe onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'" id="questionInputArea"
	name="questionInputArea" style="width: 0px; height: 0px; border: 0px; display: none" frameborder="no" scrolling="no"> </iframe></p>