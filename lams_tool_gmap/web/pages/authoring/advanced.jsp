<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->


<h2><fmt:message key="advanced.editingOptions" /></h2>

<p class="small-space-top">
	<html:checkbox property="lockOnFinished" value="1"
		styleClass="noBorder" styleId="lockOnFinished"></html:checkbox>
	<label for="lockOnFinished">
		<fmt:message key="advanced.lockOnFinished" />
	</label>
</p>

<p>
	<html:checkbox property="allowEditMarkers" value="1"
		styleClass="noBorder" styleId="allowEditMarkers"></html:checkbox>
	<label for="allowEditMarkers">
		<fmt:message key="advanced.allowEditMarkers" />
	</label>
</p>

<p>
	<html:checkbox property="allowShowAllMarkers" value="1"
		styleClass="noBorder" styleId="allowShowAllMarkers"></html:checkbox>
	<label for="allowShowAllMarkers">
		<fmt:message key="advanced.allowShowAllMarkers" />
	</label>
</p>

<h2><fmt:message key="advanced.limitMarkers" /></h2>

<p>
	<html:select property="maxMarkers" styleId="maxMarkers"  >
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

	<html:checkbox property="limitMarkers" value="1" onclick="javascript:toggleMaxMarkerMenu()" styleClass="noBorder" styleId="limitMarkers"></html:checkbox>
	<label for="limitMarkers">
		<fmt:message key="advanced.markerLimitsMessage" />
	</label>
</p>

<script type="text/javascript">
<!--
	toggleMaxMarkerMenu();
	function toggleMaxMarkerMenu()
	{
		document.getElementById("maxMarkers").disabled = !document.getElementById("limitMarkers").checked
	}
//-->
</script>

<h2><fmt:message key="advanced.mapOptions" /></h2>

<p>
	<html:checkbox property="allowZoom" value="1"
		styleClass="noBorder" styleId="allowZoom"></html:checkbox>
	<label for="allowZoom">
		<fmt:message key="advanced.allowZoom" />
	</label>
</p>

<p>
	<html:checkbox property="allowSatellite" value="1"
		styleClass="noBorder" styleId="allowSatellite"></html:checkbox>
	<label for="allowSatellite">
		<fmt:message key="advanced.allowSatellite" />
	</label>
</p>

<p>
	<html:checkbox property="allowHybrid" value="1"
		styleClass="noBorder" styleId="allowHybrid"></html:checkbox>
	<label for="allowHybrid">
		<fmt:message key="advanced.allowHybrid" />
	</label>
</p>

<p>
	<html:checkbox property="allowTerrain" value="1"
		styleClass="noBorder" styleId="allowTerrain"></html:checkbox>
	<label for="allowTerrain">
		<fmt:message key="advanced.allowTerrain" />
	</label>
</p>

