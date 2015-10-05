<%@ include file="/common/taglibs.jsp"%>

<script lang="javascript">
	$(document).ready(function() {
	    $('#allowRank').click(function() {
	        $("#criterias-holder").show("slow");
	    });
	    $('#allowVote').click(function() {
	    	$("#criterias-holder").hide("slow");
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
			$("#criterias-holder").hide("slow");
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

<p class="small-space-top">
	<html:checkbox property="imageGallery.lockWhenFinished"	styleClass="noBorder" styleId="lockWhenFinished">
	</html:checkbox>

	<label for="lockWhenFinished">
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</p>

<p>
	<html:checkbox property="imageGallery.allowShareImages" styleClass="noBorder" styleId="allowShareImages"
	onclick="uncheckNotifyTeachersOnImageSumbit();">
	</html:checkbox>
	<label for="allowShareImages">
		<fmt:message key="label.authoring.advance.allow.learner.share.images" />
	</label>
</p>

<p style="margin-left:40px;">
	<html:checkbox property="imageGallery.notifyTeachersOnImageSumbit" styleClass="noBorder" styleId="notifyTeachersOnImageSumbit"
			disabled="${not formBean.imageGallery.allowShareImages}" >
	</html:checkbox>
	<label for="notifyTeachersOnImageSumbit">
		<fmt:message key="label.authoring.advance.notify.monitoring.teachers" />
	</label>
</p>

<p>
	<html:checkbox property="allowRatingsOrVote" styleClass="noBorder" styleId="allowRatingsOrVote"
		onclick="allowRatingsOrVoteClicked();"
	>
	</html:checkbox>
	<label for="allowRatingsOrVote">
		<fmt:message key="label.authoring.advance.allow.learner.ratings.or.vote" />
	</label>
	
	<div style="margin-left:40px;">
		<input type="radio" name="imageGallery.allowVote" value="${true}" id="allowVote"
			<c:if test="${formBean.imageGallery.allowVote}">checked="checked"</c:if>
			<c:if test="${not (formBean.imageGallery.allowVote or formBean.imageGallery.allowRank)}">disabled="disabled"</c:if> 
		/>
		<label for="allowVote">
			<fmt:message key="label.authoring.advance.allow.learner.vote" />
		</label>
		<br><br>
	
		<input type="radio" name="imageGallery.allowVote" value="${false}" id="allowRank"
			<c:if test="${formBean.imageGallery.allowRank}">checked="checked"</c:if> 
			<c:if test="${not (formBean.imageGallery.allowVote or formBean.imageGallery.allowRank)}">disabled="disabled"</c:if>		
		/>
		<label for="allowRank">
			<fmt:message key="label.authoring.advance.allow.learner.rank" />
		</label>
		
		<div id="criterias-holder" <c:if test="${!formBean.imageGallery.allowRank}"> style="display:none;"</c:if> >
			<lams:AuthoringRatingCriteria criterias="${sessionMap.ratingCriterias}" hasRatingLimits="true"
				upLabel="label.authoring.up" downLabel="label.authoring.down"
				allowCommentsLabel="label.authoring.advance.allow.learner.comment.images"
				formContentPrefix="imageGallery"/>
		</div>
	</div>
</p>

<p>
	<html:checkbox property="imageGallery.reflectOnActivity" styleClass="noBorder" styleId="reflectOn"/>
	<label for="reflectOn">
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</label>
</p>

<p>
	<html:textarea property="imageGallery.reflectInstructions" styleId="reflectInstructions" cols="30" rows="3" />
</p>
<script type="text/javascript">
//automatically turn on refect option if there are text input in refect instruction area
	var ra = document.getElementById("reflectInstructions");
	var rao = document.getElementById("reflectOn");
	function turnOnRefect(){
		if(isEmpty(ra.value)){
		//turn off	
			rao.checked = false;
		}else{
		//turn on
			rao.checked = true;		
		}
	}

	ra.onkeyup=turnOnRefect;
</script>
