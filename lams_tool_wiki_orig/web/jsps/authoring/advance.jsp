<%@ include file="/includes/taglibs.jsp"%>

<!--   Advance Tab Content    -->

<!-- Advance options Row -->

<p class="small-space-top">
	<html:checkbox property="wiki.lockWhenFinished" styleClass="noBorder"
		styleId="lockWhenFinished">
	</html:checkbox>
	<label for="lockWhenFinished">
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</p>

<p>
	<html:checkbox property="wiki.allowNewWikiPage" styleClass="noBorder"
		styleId="allowNewWikiPage">
	</html:checkbox>
	<label for="allowNewWikiPage">
		<fmt:message key="label.authoring.advance.allow.new.wiki.page" />
	</label>
</p>

<p>
	<html:checkbox property="wiki.allowAttachImage" styleClass="noBorder"
		styleId="allowAttachImage">
	</html:checkbox>
	<label for="allowAttachImage">
		<fmt:message key="label.authoring.advance.allow.attach.image" />
	</label>
</p>

<p>
	<html:checkbox property="wiki.allowInsertWikiLink" styleClass="noBorder"
		styleId="allowInsertWikiLink">
	</html:checkbox>
	<label for="allowInsertWikiLink">
		<fmt:message key="label.authoring.advance.allow.insert.wiki.link" />
	</label>
</p>

<p>
	<html:checkbox property="wiki.allowInsertExternalLink" styleClass="noBorder"
		styleId="allowInsertExternalLink">
	</html:checkbox>
	<label for="allowInsertExternalLink">
		<fmt:message key="label.authoring.advance.allow.insert.external.link" />
	</label>
</p>

<p>
	<html:checkbox property="wiki.allowRevertPage" styleClass="noBorder"
		styleId="allowRevertPage">
	</html:checkbox>
	<label for="allowRevertPage">
		<fmt:message key="label.authoring.advance.allow.revert.page" />
	</label>
</p>

<p>
	<html:checkbox property="wiki.reflectOnActivity" styleClass="noBorder"	styleId="reflectOn">
	</html:checkbox>
	<label for="reflectOn">
		<fmt:message key="advanced.reflectOnActivity" />
	</label>
</p>

<p>
	<span class="space-left"> <html:textarea
			property="wiki.reflectInstructions" styleId="reflectInstructions"
			cols="30" rows="3" /> </span>
</p>
<script type="text/javascript">
<!--
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
//-->
</script>

<h2>
	<fmt:message key="label.authoring.advance.editing.limits.heading" />
</h2>

<p>
	<fmt:message key="label.authoring.advance.num.edits" />
</p>

<p>
	<fmt:message key="label.authoring.advance.minimum.reply" />
	<html:select property="wiki.minimumReply" styleId="minimumReply">
		<html:option value="0">
			<fmt:message key="label.authoring.advance.no.minimum" />
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


	<fmt:message key="label.authoring.advance.maximum.reply" />
	<html:select property="wiki.maximumReply" styleId="maximumReply">
		<html:option value="0">
			<fmt:message key="label.authoring.advance.no.maximum" />
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

<script type="text/javascript">
			function checkReflection(){
				var ropt = document.getElementById("reflectOn");
				var rins = document.getElementById("reflectInstructions");
				if(ropt.checked){
					rins.disabled=false;
				}else{
					rins.disabled=true;
				}
			}
</script>


