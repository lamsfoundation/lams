<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:url var="newQuestionInitUrl" value='/authoring/newQuestionInit.do'>
	<c:param name="sessionMapID" value="${formBean.sessionMapID}" />
</c:url>	

<script lang="javascript">
	//The panel of assessment list panel
	var questionListTargetDiv = "#questionListArea";
	function deleteQuestion(idx,sessionMapID){
		var url = "<c:url value="/authoring/removeQuestion.do"/>";
		$(questionListTargetDiv).load(
			url,
			{
				questionIndex: idx, 
				sessionMapID: sessionMapID
			}
		);
	}
	function upQuestion(idx, sessionMapID){
		var url = "<c:url value="/authoring/upQuestion.do"/>";
		$(questionListTargetDiv).load(
				url,
				{
					questionIndex: idx, 
					sessionMapID: sessionMapID
				}
		);
	}
	function downQuestion(idx, sessionMapID){
		var url = "<c:url value="/authoring/downQuestion.do"/>";
		$(questionListTargetDiv).load(
				url,
				{
					questionIndex: idx, 
					sessionMapID: sessionMapID
				}
		);
	}

	function resizeIframe() {
		if (document.getElementById('TB_iframeContent') != null) {
		    var height = top.window.innerHeight;
		    if ( height == undefined || height == 0 ) {
		    	// IE doesn't use window.innerHeight.
		    	height = document.documentElement.clientHeight;
		    	// alert("using clientHeight");
		    }
			// alert("doc height "+height);
		    height -= document.getElementById('TB_iframeContent').offsetTop + 60;
		    document.getElementById('TB_iframeContent').style.height = height +"px";
	
			TB_HEIGHT = height + 28;
			tb_position();
		}
	};
	window.onresize = resizeIframe;

	function createNewQuestionInitHref() {
		var questionTypeDropdown = document.getElementById("questionType");
		var questionType = questionTypeDropdown.selectedIndex + 1;
		var newQuestionInitHref = "${newQuestionInitUrl}&questionType=" + questionType + "&KeepThis=true&TB_iframe=true&height=540&width=850&modal=true";
		$("#newQuestionInitHref").attr("href", newQuestionInitHref)
	};
	function refreshThickbox(){   
		tb_init('a.thickbox, area.thickbox, input.thickbox');//pass where to apply thickbox
		imgLoader = new Image();// preload image
		imgLoader.src = tb_pathToImage;
	};


</script>

<!-- Basic Tab Content -->
<table>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.title"></fmt:message>
			</div>
			<html:text property="assessment.title" style="width: 99%;"></html:text>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instruction"></fmt:message>
			</div>
			<lams:FCKEditor id="assessment.instructions" value="${formBean.assessment.instructions}"
				contentFolderID="${formBean.contentFolderID}">
			</lams:FCKEditor>
		</td>
	</tr>

</table>

<div id="questionListArea">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/pages/authoring/parts/questionlist.jsp"%>
</div>

<!-- Dropdown menu for choosing a question type -->
<p>
	<select id="questionType" style="float: left">
		<option selected="selected"><fmt:message key="label.authoring.basic.type.multiple.choice" /></option>
		<option><fmt:message key="label.authoring.basic.type.matching.pairs" /></option>
		<option><fmt:message key="label.authoring.basic.type.short.answer" /></option>
		<option><fmt:message key="label.authoring.basic.type.numerical" /></option>
		<option><fmt:message key="label.authoring.basic.type.true.false" /></option>
		<option><fmt:message key="label.authoring.basic.type.essay" /></option>
		<option><fmt:message key="label.authoring.basic.type.ordering" /></option>
	</select>
	
	<a onclick="createNewQuestionInitHref();return false;" href="" class="button-add-item space-left thickbox" id="newQuestionInitHref">  
		<fmt:message key="label.authoring.basic.add.question" />
	</a>
</p>
 
<p>
	<iframe
		onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
		id="reourceInputArea" name="reourceInputArea" 
		style="width:0px;height:0px;border:0px;display:none" frameborder="no" scrolling="no">
	</iframe>
</p>
