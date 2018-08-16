<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.review.options">

	<div class="form-group">
	<label for="maximumRatesPerUser"><fmt:message key="label.max.number.marks.each.user" /></label>
		<html:select property="peerreview.maximumRatesPerUser" styleClass="form-control form-control-inline">
			<html:option value="0">
				<fmt:message key="label.no.maximum" />
			</html:option>
			<html:option value="1">1</html:option>
			<html:option value="2">2</html:option>
			<html:option value="3">3</html:option>
			<html:option value="4">4</html:option>
			<html:option value="5">5</html:option>
			<html:option value="6">6</html:option>
			<html:option value="7">7</html:option>
			<html:option value="8">8</html:option>
			<html:option value="9">9</html:option>
			<html:option value="10">10</html:option>
		</html:select>
	</div>
	
	<div class="checkbox">
	<label for="show-ratings-left-by-user"><html:checkbox property="peerreview.showRatingsLeftByUser" styleId="show-ratings-left-by-user"/>
	<fmt:message key="label.show.ratings.left.by.user" />
	</label>
	</div>

	<div class="checkbox">
	<label for="show-ratings-left-for-user"><html:checkbox property="peerreview.showRatingsLeftForUser" styleId="show-ratings-left-for-user"/>
	<fmt:message key="label.show.ratings.left.for.user" />
	</label>
	</div>

	<div class="checkbox">
	<label for="self-review"><html:checkbox property="peerreview.selfReview" styleId="self-review"/>
	<fmt:message key="label.self.review" />
	</label>
	</div>

	<div class="checkbox">
	<label for="notify-users-of-results"><html:checkbox property="peerreview.notifyUsersOfResults" styleId="notify-users-of-results"/>
	<fmt:message key="label.notify.user.of.results" />
	</label>
	</div>


</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${formBean.peerreview.contentId}" />

<lams:SimplePanel titleKey="label.activity.completion">
	<div class="checkbox">
	<label for="lockWhenFinished"><html:checkbox property="peerreview.lockWhenFinished" styleId="lockWhenFinished" />
	<fmt:message key="label.authoring.advance.lock.on.finished" /></label>
	</div>
	
	<div class="checkbox">
	<label for="reflectOn"><html:checkbox property="peerreview.reflectOnActivity" styleId="reflectOn"/>
	<fmt:message key="label.authoring.advanced.reflectOnActivity" /></label>
	</div>
	<div class="form-group">
	<html:textarea property="peerreview.reflectInstructions" styleId="reflectInstructions"  styleClass="form-control" rows="3"/>
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
</script>
