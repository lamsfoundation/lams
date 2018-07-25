<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="peerreview.title"><fmt:message key="label.authoring.basic.title"/></label>
    <html:text property="peerreview.title" styleClass="form-control"></html:text>
</div>
<div class="form-group">
    <label for="peerreview.instructions"><fmt:message key="label.authoring.basic.instruction" /></label>
    <lams:CKEditor id="peerreview.instructions" value="${formBean.peerreview.instructions}" contentFolderID="${formBean.contentFolderID}"></lams:CKEditor>
</div>

<div id="criterias-holder">
 	<lams:AuthoringRatingAllStyleCriteria criterias="${sessionMap.ratingCriterias}" hasRatingLimits="true"
		formContentPrefix="peerreview"/>
</div>
