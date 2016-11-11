<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
<c:url var="newQuestionInitUrl" value='/authoring/newQuestionInit.do'>
	<c:param name="sessionMapID" value="${sessionMapID}" />
</c:url>	

<script lang="javascript">
	$(document).ready(function(){
		reinitializePassingMarkSelect(true);
	});

	//The panel of assessment list panel
	var questionListTargetDiv = "#questionListArea";
	function deleteQuestion(idx){
		var	deletionConfirmed = confirm("<fmt:message key="warning.msg.authoring.do.you.want.to.delete"></fmt:message>");

		if (deletionConfirmed) {
			var url = "<c:url value="/authoring/removeQuestion.do"/>";
			$(questionListTargetDiv).load(
				url,
				{
					questionIndex: idx, 
					sessionMapID: "${sessionMapID}",
					referenceGrades: serializeReferenceGrades()
				},
				function(){
					reinitializePassingMarkSelect(false);
					refreshThickbox();
				}
			);
		};
	}
	function addQuestionReference(){
		var questionTypeDropdown = document.getElementById("questionSelect");
		var idx = questionTypeDropdown.value;
		
		var url = "<c:url value="/authoring/addQuestionReference.do"/>";
		$(questionListTargetDiv).load(
				url,
				{
					questionIndex: idx, 
					sessionMapID: "${sessionMapID}",
					referenceGrades: serializeReferenceGrades()
				},
				function(){
					reinitializePassingMarkSelect(false);
					refreshThickbox();
				}
		);
	}
	function deleteQuestionReference(idx){
		var url = "<c:url value="/authoring/removeQuestionReference.do"/>";
		$(questionListTargetDiv).load(
			url,
			{
				questionReferenceIndex: idx, 
				sessionMapID: "${sessionMapID}",
				referenceGrades: serializeReferenceGrades()
			},
			function(){
				reinitializePassingMarkSelect(false);
				refreshThickbox();
			}
		);
	}
	function upQuestionReference(idx){
		var url = "<c:url value="/authoring/upQuestionReference.do"/>";
		$(questionListTargetDiv).load(
				url,
				{
					questionReferenceIndex: idx, 
					sessionMapID: "${sessionMapID}",
					referenceGrades: serializeReferenceGrades()
				},
				function(){
					refreshThickbox();
				}
		);
	}
	function downQuestionReference(idx){
		var url = "<c:url value="/authoring/downQuestionReference.do"/>";
		$(questionListTargetDiv).load(
				url,
				{
					questionReferenceIndex: idx, 
					sessionMapID: "${sessionMapID}",
					referenceGrades: serializeReferenceGrades()
				},
				function(){
					refreshThickbox();
				}
		);
	}
	function serializeReferenceGrades(){
		var serializedGrades = "";
		$("[name^=grade]").each(function() {
			serializedGrades += "&" + this.name + "="  + this.value;
		});
		return serializedGrades;
	}
	
	function exportQuestions(){   
	    var reqIDVar = new Date();
		var param = "?sessionMapID=${sessionMapID}&reqID="+reqIDVar.getTime();
		location.href="<c:url value='/authoring/exportQuestions.do'/>" + param;
	};

	function createNewQuestionInitHref() {
		var questionTypeDropdown = document.getElementById("questionType");
		var questionType = questionTypeDropdown.selectedIndex + 1;
		var newQuestionInitHref = "${newQuestionInitUrl}&questionType=" + questionType + "&referenceGrades=" + encodeURIComponent(serializeReferenceGrades()) + "&KeepThis=true&TB_iframe=true&modal=true";
		$("#newQuestionInitHref").attr("href", newQuestionInitHref)
	};
	function refreshThickbox(){
		tb_init('a.thickbox, area.thickbox, input.thickbox');//pass where to apply thickbox
	};
	function reinitializePassingMarkSelect(isPageFresh){
		var oldValue = (isPageFresh) ? "${formBean.assessment.passingMark}" : $("#passingMark").val();
		$('#passingMark').empty();
		$('#passingMark').append( new Option("<fmt:message key='label.authoring.advance.passing.mark.none' />",0) );
		
		var maxGrade = 0;
		$("[name^=grade]").each(function() {
			maxGrade += eval(this.value);
		});
		
		for (var i = 1; i<=maxGrade; i++) {
			var isSelected = (oldValue == i);
		    $('#passingMark').append( new Option(i, i, isSelected, isSelected) );
		}
	};	
	
    function importQTI(){
    	window.open('<lams:LAMSURL/>questions/questionFile.jsp',
    			    'QuestionFile','width=500,height=200,scrollbars=yes');
    }
	
    function saveQTI(formHTML, formName) {
    	var form = $($.parseHTML(formHTML));
		$.ajax({
			type: "POST",
			url: '<c:url value="/authoring/saveQTI.do?sessionMapID=${sessionMapID}" />',
			data: form.serializeArray(),
			success: function(response) {
				$(questionListTargetDiv).html(response);
				refreshThickbox();
			}
		});
    }

    function exportQTI(){
    	var frame = document.getElementById("downloadFileDummyIframe"),
    		title = encodeURIComponent(document.getElementsByName("assessment.title")[0].value);
    	frame.src = '<c:url value="/authoring/exportQTI.do?sessionMapID=${sessionMapID}" />'
    			+ '&title=' + title;
    }
</script>

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="assessment.title">
    	<fmt:message key="label.authoring.basic.title"/>
    </label>
    <html:text property="assessment.title" styleClass="form-control"/>
</div>

<div class="form-group">
    <label for="assessment.instructions">
    	<fmt:message key="label.authoring.basic.instruction"/>
    </label>
	<lams:CKEditor id="assessment.instructions" value="${formBean.assessment.instructions}"
			contentFolderID="${formBean.contentFolderID}">
	</lams:CKEditor>
</div>

<div id="questionListArea">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/questionlist.jsp"%>
</div>

<!-- Dropdown menu for choosing a question type -->
<div class="form-inline form-group">
	<select id="questionType" class="form-control input-sm">
		<option selected="selected"><fmt:message key="label.authoring.basic.type.multiple.choice" /></option>
		<option><fmt:message key="label.authoring.basic.type.matching.pairs" /></option>
		<option><fmt:message key="label.authoring.basic.type.short.answer" /></option>
		<option><fmt:message key="label.authoring.basic.type.numerical" /></option>
		<option><fmt:message key="label.authoring.basic.type.true.false" /></option>
		<option><fmt:message key="label.authoring.basic.type.essay" /></option>
		<option><fmt:message key="label.authoring.basic.type.ordering" /></option>
		<option><fmt:message key="label.authoring.basic.type.mark.hedging" /></option>
	</select>
	
	<a onclick="createNewQuestionInitHref();return false;" href="" class="btn btn-default btn-sm button-add-item thickbox" id="newQuestionInitHref">  
		<fmt:message key="label.authoring.basic.add.question.to.pool" />
	</a>
		
</div>
<br><br>

<!-- For exporting QTI packages -->
<iframe id="downloadFileDummyIframe" style="display: none;"></iframe>
