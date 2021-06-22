<%@ include file="/common/taglibs.jsp"%>
<c:url var="showMessageURL" value='/authoring/newQuestion.do'>
	<c:param name="sessionMapID" value="${authoringForm.sessionMapID}" />
</c:url>
	<lams:JSImport src="includes/javascript/dacoAuthoring.js" relative="true" />

<script type="text/javascript">
	//Showes the add/edit question area
	function showQuestionInputArea(url) {
		$.ajaxSetup({ cache: true });
		$("#questionInputArea").load(url, function() {
			var area=document.getElementById("questionInputArea");
			if(area != null){
				area.style.width="100%";
				area.style.height="100%";
				area.style.display="block";
			}
			var elem = document.getElementById("saveCancelButtons");
			if (elem != null) {
				elem.style.display="none";
			}
		} );
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
	 var questionTypeDropdown=document.getElementById("questionTypeDropdown");
	 showQuestionInputArea(url+"&questionType="+(questionTypeDropdown.selectedIndex + 1));
}
	
	
	function editQuestion(questionIndex,sessionMapID){
		var url = '<c:url value="/authoring/editQuestion.do?questionIndex="/>' + questionIndex +"&reqID="+(new Date()).getTime()+"&sessionMapID="+sessionMapID;
		showQuestionInputArea(url);
	}
	
	function deleteQuestion(questionIndex,sessionMapID){
		var url = "<c:url value='/authoring/removeQuestion.do'/>";
		var param = "questionIndex=" + questionIndex +"&reqID="+(new Date()).getTime()+"&sessionMapID="+sessionMapID;;
		deleteQuestionLoading();
		$.ajax({
            type: 'post',
            url: url,
            data: param,
            success: function(data) {
            	$("#questionListArea").html(data);
            	deleteQuestionComplete();
            }
        });
	}
	

	function deleteQuestionLoading(){
		showBusy("#questionListArea");
	}
	
	function deleteQuestionComplete(){
		hideBusy("#questionListArea");
	}
</script>

<!-- Basic Tab Content -->
 <div class="form-group voffset10">
    <label for="daco.title"><fmt:message key="label.authoring.basic.title" /></label>
    <form:input path="daco.title" style="width: 100%;" cssClass="form-control"/>
 </div>
 <div class="form-group">
    <label for="daco.instructions"><fmt:message key="label.authoring.basic.instruction" /></label>
    <lams:CKEditor id="daco.instructions" value="${authoringForm.daco.instructions}" contentFolderID="${authoringForm.contentFolderID}"></lams:CKEditor>
 </div>
  
<!-- Dropdown menu for choosing a question type -->
<div id="questionListArea"><c:set var="sessionMapID" value="${authoringForm.sessionMapID}" />
<%@ include	file="/pages/authoring/parts/questionlist.jsp"%></div>
<div class="form-inline"><select id="questionTypeDropdown" class="form-control input-sm">
	<option selected="selected"><fmt:message key="label.authoring.basic.textfield"/></option>
	<option><fmt:message key="label.authoring.basic.textarea"/></option>
	<option><fmt:message key="label.authoring.basic.number"/></option>
	<option><fmt:message key="label.authoring.basic.date"/></option>
	<option><fmt:message key="label.authoring.basic.file"/></option>
	<option><fmt:message key="label.authoring.basic.image"/></option>
	<option><fmt:message key="label.authoring.basic.radio"/></option>
	<option><fmt:message key="label.authoring.basic.dropdown"/></option>
	<option><fmt:message key="label.authoring.basic.checkbox"/></option>
	<option><fmt:message key="label.authoring.basic.longlat"/></option>
</select><a href="#" class="btn btn-default btn-sm loffset5" onclick="javascript:showQuestionInputAreaByType('${showMessageURL }')">
	<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.question.add" />
</a></p></div>
<a name="questionInputAreaAnchor"></a>
<p><div onload="javascript:window.location.hash = '#questionInputArea';" id="questionInputArea"
	name="questionInputArea" style="voffset10"> </div></p>
