<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">

	function showMessage(url) {
		$.ajaxSetup({ cache: true });
		$("#iframeArea").load(url, function() {
			$(this).show();
			$("#saveCancelButtons").hide();
		});
	}
	function hideMessage(){
		$("#iframeArea").hide();
		$("#saveCancelButtons").show();
	}
	function runUrl(url) {
		$.ajaxSetup({ cache: true });
		$("#iframeArea").load(url, function() {
			$(this).show();
		});
	}
	
</script>


<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />
<c:set var="sessionMapID" value="${authoringForm.sessionMapID}" />

<!-- ========== Basic Tab ========== -->
<div class="form-group">
    <label for="title"><fmt:message key="label.authoring.basic.title"/></label>
    <form:input path="title" cssClass="form-control"/>
</div>

<div class="form-group">
    <label for=instructions><fmt:message key="label.authoring.basic.instructions"/></label>
	<lams:CKEditor id="instructions" value="${authoringForm.instructions}"
				contentFolderID="${sessionMap.contentFolderID}"></lams:CKEditor>
</div>

<strong>
	<fmt:message key="label.authoring.basic.heading" />
</strong>

<div id="itemListArea">
	<%@ include file="parts/headingList.jsp"%>
</div>

<a 
	href="javascript:showMessage('<lams:WebAppURL />authoring/loadHeadingForm.do?sessionMapID=${authoringForm.sessionMapID}');"
	class="btn btn-sm btn-default"><i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.heading.add" /> </a>
</a>


<div id="iframeArea" name="iframeArea" class="voffset10"></div>

