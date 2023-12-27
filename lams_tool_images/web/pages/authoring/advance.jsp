<%@ include file="/common/taglibs.jsp"%>

<script lang="javascript">

$(document).ready(function() {
	    $('#allowRank').click(function() {
	        $(".rating-criteria-tag").show("slow");
	    });
	    $('#allowVote').click(function() {
	    	$(".rating-criteria-tag").hide("slow");
	    });//if(!$(this).is(":checked")) {
	})

	/**
	 * Processes mouse click event on allowRatingsOrVote ckeckbox
	 */
	function allowRatingsOrVoteClicked() {
		document.forms.imageGalleryForm.allowVote.disabled = ! eval(document.forms.imageGalleryForm.allowVote.disabled); 
		document.forms.imageGalleryForm.allowRank.disabled = ! eval(document.forms.imageGalleryForm.allowRank.disabled);
		if (document.forms.imageGalleryForm.allowRatingsOrVote.checked) {
			document.forms.imageGalleryForm.allowVote.checked = true;
		} else {
			document.forms.imageGalleryForm.allowVote.checked = false;
			document.forms.imageGalleryForm.allowRank.checked = false;
			$(".rating-criteria-tag").hide("slow");
		}
	}

	function uncheckNotifyTeachersOnImageSumbit() {
		document.forms.imageGalleryForm.notifyTeachersOnImageSumbit.checked = false;
		document.forms.imageGalleryForm.notifyTeachersOnImageSumbit.disabled = ! eval(document.forms.imageGalleryForm.notifyTeachersOnImageSumbit.disabled);
	}
</script>
	
<c:set var="sessionMap"	value="${sessionScope[imageGalleryForm.sessionMapID]}" />

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.image.options">

<div class="checkbox">
	<label for="allowShareImages">
		<form:checkbox path="imageGallery.allowShareImages" id="allowShareImages"
				onclick="uncheckNotifyTeachersOnImageSumbit();"/>
		<fmt:message key="label.authoring.advance.allow.learner.share.images" />
	</label>
</div>

<div class="checkbox loffset20">
	<label for="notifyTeachersOnImageSumbit">
		<form:checkbox path="imageGallery.notifyTeachersOnImageSumbit" id="notifyTeachersOnImageSumbit"
				disabled="${not imageGalleryForm.imageGallery.allowShareImages}"/>
		<fmt:message key="label.authoring.advance.notify.monitoring.teachers" />
	</label>
</div>

<div class="checkbox">
	<label for="allowRatingsOrVote">
		<form:checkbox path="allowRatingsOrVote" id="allowRatingsOrVote"
				onclick="allowRatingsOrVoteClicked();"/>
		<fmt:message key="label.authoring.advance.allow.learner.ratings.or.vote" />
	</label>
</div>
	
<div class="loffset20">

	<div class="radio">
		<label for="allowVote">
			<input type="radio" name="imageGallery.allowVote" value="${true}" id="allowVote"
				<c:if test="${imageGalleryForm.imageGallery.allowVote}">checked="checked"</c:if>
				<c:if test="${not (imageGalleryForm.imageGallery.allowVote or imageGalleryForm.imageGallery.allowRank)}">disabled="disabled"</c:if> 
			/>
			<fmt:message key="label.authoring.advance.allow.learner.vote" />
		</label>
	</div>
	
	<div class="radio">
		<label for="allowRank">
			<input type="radio" name="imageGallery.allowVote" value="${false}" id="allowRank"
				<c:if test="${imageGalleryForm.imageGallery.allowRank}">checked="checked"</c:if> 
				<c:if test="${not (imageGalleryForm.imageGallery.allowVote or imageGalleryForm.imageGallery.allowRank)}">disabled="disabled"</c:if>		
			/>
			<fmt:message key="label.authoring.advance.allow.learner.rank" />
		</label>
	</div>
		
	<c:if test='${!imageGalleryForm.imageGallery.allowRank}'><c:set var="styleId">display:none;</c:set></c:if>
	<lams:AuthoringRatingCriteria criterias="${sessionMap.ratingCriterias}" hasRatingLimits="true"
		upLabel="label.authoring.up" downLabel="label.authoring.down"
		allowCommentsLabel="label.authoring.advance.allow.learner.comment.images"
		formContentPrefix="imageGallery"
	    styleId="${styleId}"/>
	
</div>

</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${imageGalleryForm.imageGallery.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">
<div class="checkbox">
	<label for="lock-when-finished">
		<form:checkbox path="imageGallery.lockWhenFinished" id="lock-when-finished"/>
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</div>
</lams:SimplePanel>

