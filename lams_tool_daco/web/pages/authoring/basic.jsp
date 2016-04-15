<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:url var="showMessageURL" value='/authoring/newQuestion.do'>
	<c:param name="sessionMapID" value="${formBean.sessionMapID}" />
</c:url>

<script type="text/javascript">
	//Showes the add/edit question area
	function showQuestionInputArea(url) {

		var area=document.getElementById("questionInputArea");
		if(area != null){
			area.style.width="100%";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="none";
		}
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
	 showQuestionInputArea(url+"&questionType="+(questionTypeDropdown.selectedIndex + 1));
}
	
	
	function editQuestion(questionIndex,sessionMapID){
		var url = "<c:url value="/authoring/editQuestion.do?questionIndex="/>" + questionIndex +"&reqID="+(new Date()).getTime()+"&sessionMapID="+sessionMapID;
		showQuestionInputArea(url);
	}
	
	//Question list panel
	var questionListTargetDiv = "questionListArea";
		
	function deleteQuestion(questionIndex,sessionMapID){
		var url = "<c:url value='/authoring/removeQuestion.do'/>";
		var param = "questionIndex=" + questionIndex +"&reqID="+(new Date()).getTime()+"&sessionMapID="+sessionMapID;;
		deleteQuestionLoading();
	    var myAjax = new Ajax.Updater(
		    	questionListTargetDiv,
		    	url,
		    	{
		    		method:'post',
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
	function resizeOnMessageFrameLoad(){
		var messageAreaFrame = document.getElementById("questionInputArea");
		messageAreaFrame.style.height=messageAreaFrame.contentWindow.document.body.scrollHeight+'px';
	}
</script>

<!-- Basic Tab Content -->
 <div class="form-group">
    <label for="daco.title"><fmt:message key="label.authoring.basic.title" /></label>
    <html:text property="daco.title" style="width: 100%;"></html:text>
 </div>
 <div class="form-group">
    <label for="daco.title"><fmt:message key="label.authoring.basic.instruction" /></label>
    <lams:CKEditor id="daco.instructions" value="${formBean.daco.instructions}" contentFolderID="${formBean.contentFolderID}"></lams:CKEditor>
 </div>
  
<!-- Dropdown menu for choosing a question type -->
<div id="questionListArea"><c:set var="sessionMapID" value="${formBean.sessionMapID}" />
<%@ include	file="/pages/authoring/parts/questionlist.jsp"%></div>
<div><select id="questionType">
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
</select><html:link href="#" styleClass="btn btn-default btn-xs loffset5" onclick="javascript:showQuestionInputAreaByType('${showMessageURL }')">
	<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.question.add" />
</html:link></p></div>
<a name="questionInputAreaAnchor"></a>
<p><iframe onload="javascript:this.style.height=resizeOnMessageFrameLoad();window.location.hash = '#questionInputArea';" id="questionInputArea"
	name="questionInputArea" style="width: 0px; height: 0px; border: 0px; display: none" frameborder="no" scrolling="no"> </iframe></p>
