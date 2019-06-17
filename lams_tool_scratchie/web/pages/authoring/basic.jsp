<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${authoringForm.sessionMapID}" />
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

	function createNewQuestionInitHref() {
		var questionTypeDropdown = document.getElementById("questionType");
		var questionType = questionTypeDropdown.selectedIndex + 1;
		var newQuestionInitHref = "${newQuestionInitUrl}&questionType=" + questionType + "&referenceGrades=" + encodeURIComponent(serializeReferenceGrades()) + "&KeepThis=true&TB_iframe=true&modal=true";
		$("#newQuestionInitHref").attr("href", newQuestionInitHref)
	}
	
	function refreshThickbox(){
		tb_init('a.thickbox, area.thickbox, input.thickbox');//pass where to apply thickbox
	}
</script>

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="scratchie.title"><fmt:message key="label.authoring.basic.title"/></label>
    <form:input path="scratchie.title" cssClass="form-control"/>
</div>
<div class="form-group">
    <label for="scratchie.instructions"><fmt:message key="label.authoring.basic.instruction" /></label>
    <lams:CKEditor id="scratchie.instructions" value="${authoringForm.scratchie.instructions}" contentFolderID="${authoringForm.contentFolderID}"></lams:CKEditor>
</div>

<%-- <div id="resourceListArea">
	<c:set var="sessionMapID" value="${authoringForm.sessionMapID}" />
 	<%@ include file="/pages/authoring/parts/itemlist.jsp"%>
</div>
 --%>
<!-- Items -->
<div>
	<div  id="itemArea">
		<%@ include file="parts/itemlist.jsp"%>
	</div>
	
	<div>
		<c:set var="addItemUrl" >
			<c:url value='/authoring/addItem.do'/>?sessionMapID=${sessionMapID}&KeepThis=true&TB_iframe=true&modal=true
		</c:set>
		<a href="${addItemUrl}" class="btn btn-default btn-sm thickbox">
			<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.add.another.scratchie" /> 
		</a>
	</div>
</div>
