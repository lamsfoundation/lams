<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">

	function showMessage(url) {
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
		$("#iframeArea").load(url, function() {
			$(this).show();
		});
	}
	
</script>


<c:set var="formBean"
	value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />

<!-- ========== Basic Tab ========== -->
<div class="form-group">
    <label for="title"><fmt:message key="label.authoring.basic.title"/></label>
    <html:text property="title" styleClass="form-control"/>
</div>

<div class="form-group">
    <label for=instructions><fmt:message key="label.authoring.basic.instructions"/></label>
	<lams:CKEditor id="instructions" value="${formBean.instructions}"
				contentFolderID="${sessionMap.contentFolderID}"></lams:CKEditor>
</div>

<h4 class="spacer-left">
	<fmt:message key="label.authoring.basic.heading" />
</h4>

<div id="itemListArea">
	<%@ include file="parts/headingList.jsp"%>
</div>

<a 
	href="javascript:showMessage('<html:rewrite page="/authoring.do?dispatch=loadHeadingForm&amp;sessionMapID=${formBean.sessionMapID}"/>');"
	class="btn btn-default"><i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.heading.add" /> </a>
</p>

<div id="iframeArea" name="iframeArea" class="voffset10"></div>

