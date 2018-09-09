<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->


<lams:SimplePanel titleKey="advanced.editingOptions">

	<div class="checkbox">
		<label for="allowEditMarkers">
		<html:checkbox property="allowEditMarkers" value="1" styleId="allowEditMarkers"></html:checkbox>
		<fmt:message key="advanced.allowEditMarkers" /></label>
	</div>
	
	<div class="checkbox">
		<label for="allowShowAllMarkers">
		<html:checkbox property="allowShowAllMarkers" value="1" styleId="allowShowAllMarkers"></html:checkbox>
		<fmt:message key="advanced.allowShowAllMarkers" /></label>
	</div>
</lams:SimplePanel>

<lams:SimplePanel titleKey="advanced.limitMarkers">
	<div class="checkbox">
		<label for="limitMarkers">
		<html:checkbox property="limitMarkers" value="1" onclick="javascript:toggleMaxMarkerMenu()" styleId="limitMarkers"></html:checkbox>
		<fmt:message key="advanced.markerLimitsMessage" />
		</label>

	<html:select property="maxMarkers" styleId="maxMarkers" styleClass="form-control form-control-inline input-sm" >
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
			<html:option value="10">11</html:option>
			<html:option value="10">12</html:option>
			<html:option value="10">13</html:option>
			<html:option value="10">14</html:option>
			<html:option value="10">15</html:option>
			<html:option value="10">16</html:option>
			<html:option value="10">17</html:option>
			<html:option value="10">18</html:option>
			<html:option value="10">19</html:option>
			<html:option value="10">20</html:option>
	</html:select>
	
	</div>
	
	<script type="text/javascript">
	<!--
		toggleMaxMarkerMenu();
		function toggleMaxMarkerMenu()
		{
			document.getElementById("maxMarkers").disabled = !document.getElementById("limitMarkers").checked
		}
	//-->
	</script>

</lams:SimplePanel>

<lams:SimplePanel titleKey="advanced.mapOptions">

	<div class="checkbox">
		<label for="allowZoom">
		<html:checkbox property="allowZoom" value="1" styleId="allowZoom"></html:checkbox>
		<fmt:message key="advanced.allowZoom" /></label>
	</div>
	
	<div class="checkbox">
		<label for="allowSatellite">
		<html:checkbox property="allowSatellite" value="1" styleId="allowSatellite"></html:checkbox>
		<fmt:message key="advanced.allowSatellite" /></label>
	</div>
	
	<div class="checkbox">
		<label for="allowHybrid">
		<html:checkbox property="allowHybrid" value="1"	styleId="allowHybrid"></html:checkbox>
		<fmt:message key="advanced.allowHybrid" /></label>
	</div>
	
	<div class="checkbox">
		<label for="allowTerrain">
		<html:checkbox property="allowTerrain" value="1" styleId="allowTerrain"></html:checkbox>
		<fmt:message key="advanced.allowTerrain" /></label>
	</div>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${sessionMap.toolContentID}" />

<lams:SimplePanel titleKey="label.activity.completion">	
	<div class="checkbox">
		<label for="lockOnFinished">
		<html:checkbox property="lockOnFinished" value="1" styleId="lockOnFinished"></html:checkbox>
		<fmt:message key="advanced.lockOnFinished" /></label>
	</div>
	
	<div class="checkbox">
		<label for="reflectOnActivity">
		<html:checkbox property="reflectOnActivity" value="1" styleId="reflectOnActivity"></html:checkbox>
		<fmt:message key="advanced.reflectOnActivity" /></label>
	</div>
	<div class="form-group">
	<html:textarea property="reflectInstructions" rows="3" styleId="reflectInstructions"  styleClass="form-control"/>
	</div>
</lams:SimplePanel>

	<script type="text/javascript">
	<!--
	//automatically turn on refect option if there are text input in refect instruction area
		var ra = document.getElementById("reflectInstructions");
		var rao = document.getElementById("reflectOnActivity");
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
	//-->
	</script>


