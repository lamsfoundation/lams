<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->

<p class="small-space-top">
	<html:checkbox property="peerreview.lockWhenFinished"
		styleClass="noBorder" styleId="lockWhenFinished">
	</html:checkbox>

	<label for="lockWhenFinished">
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</p>

<p>
	<fmt:message key="label.max.number.marks.each.user" />
	<html:select property="peerreview.maximumRatesPerUser">
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
</p>

<p class="small-space-top">
	<html:checkbox property="peerreview.showRatingsLeftForUser"
		styleClass="noBorder" styleId="show-ratings-left-for-user">
	</html:checkbox>

	<label for="show-ratings-left-for-user">
		<fmt:message key="label.show.ratings.left.for.user" />
	</label>
</p>

<p>
	<html:checkbox property="peerreview.reflectOnActivity"
		styleClass="noBorder" styleId="reflectOn">
	</html:checkbox>
	<label for="reflectOn">
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</label>
</p>

<p>
	<html:textarea property="peerreview.reflectInstructions"
		styleId="reflectInstructions" cols="30" rows="3" />
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
