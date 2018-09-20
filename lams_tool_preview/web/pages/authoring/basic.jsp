<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[peerreviewForm.sessionMapID]}"/>

<!-- Basic Tab Content -->
<div class="form-group">
    <label for="peerreview.title"><fmt:message key="label.authoring.basic.title"/></label>
    <input type="text" name="peerreview.title" value="${peerreviewForm.peerreview.title}" class="form-control"/>
</div>
<div class="form-group">
    <label for="peerreview.instructions"><fmt:message key="label.authoring.basic.instruction" /></label>
     <lams:CKEditor id="peerreview.instructions" value="${peerreviewForm.peerreview.instructions}" contentFolderID="${peerreviewForm.contentFolderID}"></lams:CKEditor>
</div>

<div id="criterias-holder">
 	<lams:AuthoringRatingAllStyleCriteria criterias="${sessionMap.ratingCriterias}" hasRatingLimits="true"
		formContentPrefix="peerreview"/>
</div>
