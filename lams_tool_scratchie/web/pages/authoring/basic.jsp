<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:if test="${itemList == null}">
	<c:set var="itemList" value="${sessionMap.itemList}"/>
</c:if>

<script lang="javascript">

	var itemTargetDiv = "#itemArea";

	function removeItem(idx){
		var	deletionConfirmed = confirm("<fmt:message key="warning.msg.authoring.do.you.want.to.delete"></fmt:message>");
		
		if (deletionConfirmed) {
			var url = "<c:url value="/authoring/removeItem.do"/>";
			$(itemTargetDiv).load(
				url,
				{
					itemIndex: idx,
					sessionMapID: "${sessionMapID}"
				},
				function(){
					refreshThickbox();
				}
			);
		};
	}
	function upItem(idx){
		var url = "<c:url value="/authoring/upItem.do"/>";
		$(itemTargetDiv).load(
			url,
			{
				itemIndex: idx,
				sessionMapID: "${sessionMapID}"
			},
			function(){
				refreshThickbox();
			}
		);
	}
	function downItem(idx){
		var url = "<c:url value="/authoring/downItem.do"/>";
		$(itemTargetDiv).load(
			url,
			{
				itemIndex: idx,
				sessionMapID: "${sessionMapID}"
			},
			function(){
				refreshThickbox();
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
		var newQuestionInitHref = "${newQuestionInitUrl}&questionType=" + questionType + "&referenceGrades=" + encodeURIComponent(serializeReferenceGrades()) + "&KeepThis=true&TB_iframe=true&height=640&width=950&modal=true";
		$("#newQuestionInitHref").attr("href", newQuestionInitHref)
	};
	
	function refreshThickbox(){
		tb_init('a.thickbox, area.thickbox, input.thickbox');//pass where to apply thickbox
	};
	
    function importQTI(){
    	window.open('<lams:LAMSURL/>questions/questionFile.jsp?limitType=mc',
    			    'QuestionFile','width=500,height=200,scrollbars=yes');
    }
	
    function saveQTI(formHTML, formName) {
    	var form = $($.parseHTML(formHTML));
		$.ajax({
			type: "POST",
			url: '<c:url value="/authoring/saveQTI.do?sessionMapID=${sessionMapID}" />',
			data: form.serializeArray(),
			success: function(response) {
				$(itemTargetDiv).html(response);
				refreshThickbox();
			}
		});
    }
    
    function exportQTI(){
    	var frame = document.getElementById("downloadFileDummyIframe"),
    		title = encodeURIComponent(document.getElementsByName("scratchie.title")[0].value);
    	frame.src = '<html:rewrite page="/authoring/exportQTI.do?sessionMapID=${sessionMapID}" />'
    			+ '&title=' + title;
    }
</script>
<!-- Basic Tab Content -->
<table class="space-bottom">
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.title"/>
			</div>
			<html:text property="scratchie.title" style="width: 99%;"/>
		</td>
	</tr>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instruction"/>
			</div>
			<lams:CKEditor id="scratchie.instructions" value="${formBean.scratchie.instructions}" contentFolderID="${formBean.contentFolderID}"/>
		</td>
	</tr>


</table>

<!-- Items -->
<div>
	<div  id="itemArea">
		<%@ include file="parts/itemlist.jsp"%>
	</div>
	
	<div  style="margin: 0 40px 80px;">
		<c:set var="addItemUrl" >
			<c:url value='/authoring/addItem.do'/>?sessionMapID=${sessionMapID}&KeepThis=true&TB_iframe=true&height=540&width=850&modal=true
		</c:set>
		<a href="${addItemUrl}" class="button-add-item right-buttons thickbox">
			<fmt:message key="label.authoring.basic.add.another.scratchie" /> 
		</a>
		<a href="#" onClick="javascript:importQTI()" style="margin-right: 30px">
			<fmt:message key="label.authoring.import.qti" /> 
		</a>
		<a href="#" onClick="javascript:exportQTI()">
			<fmt:message key="label.authoring.export.qti" />
		</a>
	</div>
</div>

<!-- For exporting QTI packages -->
<iframe id="downloadFileDummyIframe" style="display: none;"></iframe>