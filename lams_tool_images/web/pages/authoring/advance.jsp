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
		document.imageGalleryForm.allowVote.disabled = ! eval(document.imageGalleryForm.allowVote.disabled); 
		document.imageGalleryForm.allowRank.disabled = ! eval(document.imageGalleryForm.allowRank.disabled);
		if (document.imageGalleryForm.allowRatingsOrVote.checked) {
			document.imageGalleryForm.allowVote.checked = true;
		} else {
			document.imageGalleryForm.allowVote.checked = false;
			document.imageGalleryForm.allowRank.checked = false;
			$(".rating-criteria-tag").hide("slow");
		}
	}

	function uncheckNotifyTeachersOnImageSumbit() {
		document.imageGalleryForm.notifyTeachersOnImageSumbit.checked = false;
		document.imageGalleryForm.notifyTeachersOnImageSumbit.disabled = ! eval(document.imageGalleryForm.notifyTeachersOnImageSumbit.disabled);
	}
</script>
	
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMap"	value="${sessionScope[formBean.sessionMapID]}" />

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.image.options">

<div class="checkbox">
	<label for="allowShareImages">
		<html:checkbox property="imageGallery.allowShareImages" styleId="allowShareImages"
				onclick="uncheckNotifyTeachersOnImageSumbit();"/>
		<fmt:message key="label.authoring.advance.allow.learner.share.images" />
	</label>
</div>

<div class="checkbox loffset20">
	<label for="notifyTeachersOnImageSumbit">
		<html:checkbox property="imageGallery.notifyTeachersOnImageSumbit" styleId="notifyTeachersOnImageSumbit"
				disabled="${not formBean.imageGallery.allowShareImages}"/>
		<fmt:message key="label.authoring.advance.notify.monitoring.teachers" />
	</label>
</div>

<div class="checkbox">
	<label for="allowRatingsOrVote">
		<html:checkbox property="allowRatingsOrVote" styleId="allowRatingsOrVote"
				onclick="allowRatingsOrVoteClicked();"/>
		<fmt:message key="label.authoring.advance.allow.learner.ratings.or.vote" />
	</label>
</div>
	
<div class="loffset20">

	<div class="radio">
		<label for="allowVote">
			<input type="radio" name="imageGallery.allowVote" value="${true}" id="allowVote"
				<c:if test="${formBean.imageGallery.allowVote}">checked="checked"</c:if>
				<c:if test="${not (formBean.imageGallery.allowVote or formBean.imageGallery.allowRank)}">disabled="disabled"</c:if> 
			/>
			<fmt:message key="label.authoring.advance.allow.learner.vote" />
		</label>
	</div>
	
	<div class="radio">
		<label for="allowRank">
			<input type="radio" name="imageGallery.allowVote" value="${false}" id="allowRank"
				<c:if test="${formBean.imageGallery.allowRank}">checked="checked"</c:if> 
				<c:if test="${not (formBean.imageGallery.allowVote or formBean.imageGallery.allowRank)}">disabled="disabled"</c:if>		
			/>
			<fmt:message key="label.authoring.advance.allow.learner.rank" />
		</label>
	</div>
		
	<c:if test='${!formBean.imageGallery.allowRank}'><c:set var="styleId">display:none;</c:set></c:if>
	<lams:AuthoringRatingCriteria criterias="${sessionMap.ratingCriterias}" hasRatingLimits="true"
		upLabel="label.authoring.up" downLabel="label.authoring.down"
		allowCommentsLabel="label.authoring.advance.allow.learner.comment.images"
		formContentPrefix="imageGallery"
	    styleId="${styleId}"/>
	
</div>

</lams:SimplePanel>

<lams:SimplePanel titleKey="label.activity.completion">

<div class="checkbox">
	<label for="lock-when-finished">
		<html:checkbox property="imageGallery.lockWhenFinished" styleId="lock-when-finished"/>
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</div>

<div class="checkbox">
	<label for="reflect-on">
		<html:checkbox property="imageGallery.reflectOnActivity" styleId="reflect-on"/>
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</label>
</div>

<div class="form-group">
	<html:textarea property="imageGallery.reflectInstructions" cols="60" rows="3" styleId="reflect-instructions" styleClass="form-control"/>
</div>

</lams:SimplePanel>

<script type="text/javascript">
	//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflect-instructions");
	var rao = document.getElementById("reflect-on");
	function turnOnRefect(){
		if (isEmpty(ra.value)) {
		//turn off	
			rao.checked = false;
		} else {
		//turn on
			rao.checked = true;		
		}
	}

	ra.onkeyup=turnOnRefect;
</script>
