<%@ include file="/common/taglibs.jsp"%>

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.review.options">
	<div class="form-group">
		<label for="maximumRatesPerUser"><fmt:message key="label.max.number.marks.each.user" /></label>
		<form:select path="peerreview.maximumRatesPerUser" cssClass="form-control form-control-inline">
			<form:option value="0">
				<fmt:message key="label.no.maximum" />
			</form:option>
			<form:option value="1">1</form:option>
			<form:option value="2">2</form:option>
			<form:option value="3">3</form:option>
			<form:option value="4">4</form:option>
			<form:option value="5">5</form:option>
			<form:option value="6">6</form:option>
			<form:option value="7">7</form:option>
			<form:option value="8">8</form:option>
			<form:option value="9">9</form:option>
			<form:option value="10">10</form:option>
		</form:select>
	</div>
	
	<div class="checkbox">
	<label for="show-ratings-left-by-user"><form:checkbox path="peerreview.showRatingsLeftByUser" id="show-ratings-left-by-user"/>
	<fmt:message key="label.show.ratings.left.by.user" />
	</label>
	</div>

	<div class="checkbox">
	<label for="show-ratings-left-for-user"><form:checkbox path="peerreview.showRatingsLeftForUser" id="show-ratings-left-for-user"/>
	<fmt:message key="label.show.ratings.left.for.user" />
	</label>
	</div>

	<div class="checkbox">
	<label for="self-review"><form:checkbox path="peerreview.selfReview" id="self-review"/>
	<fmt:message key="label.self.review" />
	</label>
	</div>

	<div class="form-group" id="tolerance-container">
		<label for="tolerance">
		<fmt:message key="label.authoring.advanced.tolearnce" />
		<form:input type="number" min="0" max="50" path="peerreview.tolerance" id="tolerance" cssClass="form-control form-control-inline loffset5" />
		</label>
	</div>
	
	<div class="form-group">
		<label for="rubricsView"><fmt:message key="label.rating.rubrics.view" /></label>
		<form:select path="peerreview.rubricsView" cssClass="form-control form-control-inline">
			<form:option value="1">
				<fmt:message key="label.rating.rubrics.view.learner" />
			</form:option>
			<form:option value="2">
				<fmt:message key="label.rating.rubrics.view.row" />
			</form:option>
		</form:select>
	</div>
	
	<div class="checkbox">
		<label for="rubrics-in-between-columns">
			<form:checkbox path="peerreview.rubricsInBetweenColumns" id="rubrics-in-between-columns"/>
			<fmt:message key="label.rating.rubrics.in.between.enable" />
		</label>
	</div>	
	
	<div class="checkbox">
		<label for="rubrics-require-ratings">
			<form:checkbox path="peerreview.rubricsRequireRatings" id="rubrics-require-ratings"/>
			<fmt:message key="label.rating.rubrics.require.ratings" />
		</label>
		<i class="fa fa-question-circle" data-toggle="tooltip" data-placement="right"
			title="<fmt:message key="label.rating.rubrics.require.ratings.tooltip" />"></i>
	</div>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${peerreviewForm.peerreview.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">
	<div class="checkbox">
	<label for="lockWhenFinished"><form:checkbox path="peerreview.lockWhenFinished" id="lockWhenFinished" />
	<fmt:message key="label.authoring.advance.lock.on.finished" /></label>
	</div>
	
	<div class="checkbox">
	<label for="reflectOn"><form:checkbox path="peerreview.reflectOnActivity" id="reflectOn"/>
	<fmt:message key="label.authoring.advanced.reflectOnActivity" /></label>
	</div>
	<div class="form-group">
	<form:textarea path="peerreview.reflectInstructions" id="reflectInstructions"  cssClass="form-control" rows="3"/>
	</div>
</lams:SimplePanel>

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

	$('#self-review').change(function(){
		if ($(this).is(':checked')) {
			$('#tolerance-container').slideDown();
		} else {
			$('#tolerance-container').slideUp().find('#tolerance').val(0);
		}
	}).change();
</script>
