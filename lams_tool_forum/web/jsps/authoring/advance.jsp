<%@ include file="/includes/taglibs.jsp"%>

<!--   Advance Tab Content    -->

<!-- Advance options Row -->

<p class="small-space-top">
	<html:checkbox property="forum.lockWhenFinished" styleClass="noBorder"
		styleId="lockWhenFinished">
	</html:checkbox>
	<label for="lockWhenFinished">
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</p>

<p>
	<html:checkbox property="forum.allowEdit" styleClass="noBorder"
		styleId="allowEdit">
	</html:checkbox>
	<label for="allowEdit">
		<fmt:message key="label.authoring.advance.allow.edit" />
	</label>
</p>

<p>
	<html:checkbox property="forum.allowUpload" styleClass="noBorder"
		styleId="allowUpload">
	</html:checkbox>
	<label for="allowUpload">
		<fmt:message key="label.authoring.advance.allow.upload" />
	</label>
</p>

<p>
	<html:checkbox property="forum.allowRichEditor" styleId="richEditor"
		styleClass="noBorder">
	</html:checkbox>
	<label for="richEditor">
		<fmt:message key="label.authoring.advance.use.richeditor" />
	</label>
</p>

<p>
	<html:checkbox property="forum.limitedInput" styleId="limitedInput"	styleClass="noBorder">
	</html:checkbox>
	<label for="limitedInput">
		<fmt:message key="label.authoring.advance.limited.input" />
	</label>

	<html:text property="forum.limitedChar" styleId="limitedChar" />
</p>

<p>
	<html:checkbox property="forum.reflectOnActivity" styleClass="noBorder"	styleId="reflectOn">
	</html:checkbox>
	<label for="reflectOn">
		<fmt:message key="advanced.reflectOnActivity" />
	</label>
</p>

<p>
	<span class="space-left"> <html:textarea
			property="forum.reflectInstructions" styleId="reflectInstructions"
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
	ra.onkeydown=turnOnRefect;
	ra.onkeyup=turnOnRefect;
//-->
</script>

<h2>
	<fmt:message key="message.posting.limiting" />
</h2>

<p>
	<html:radio property="forum.allowNewTopic" value="true"
		onclick="allowNewTopic()" styleId="allowNewTopic1" styleClass="noBorder">
	</html:radio>
	<label for="allowNewTopic1">
		<fmt:message key="label.authoring.advance.allow.new.topics" />
	</label>
</p>

<p>
	<html:radio property="forum.allowNewTopic" value="false"
		onclick="allowNewTopic()" styleId="allowNewTopic2" styleClass="noBorder">
	</html:radio>
	<label for="allowNewTopic2">
		<fmt:message key="label.authoring.advance.number.reply" />
	</label>
</p>

<p>
	<fmt:message key="label.authoring.advance.minimum.reply" />
	<html:select property="forum.minimumReply" styleId="minimumReply">
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
	<html:select property="forum.maximumReply" styleId="maximumReply">
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
			var limit = document.getElementById("limitedInput");
			var rich = document.getElementById("richEditor");
			var limitChar = document.getElementById("limitedChar");

			limit.onclick= initAdvanced;
			function initAdvanced(){
				if(limit.checked){
					limitChar.disabled=false;
					rich.checked=false;
				}else{
					limitChar.disabled=true;
				}
				
			}
			rich.onclick = function(){
				if(this.checked){
					limitChar.disabled=true;
					limit.checked=false;
				}else{
					limitChar.disabled=false;
				}
			}
			initAdvanced();
			limitChar.onblur=function(){
				var min = 0;
				var errors = '';
				var num = parseFloat(this.value);
				if(isNaN(num)) 
					errors = '<fmt:message key="js.error.invalid.number"/>\n';
				else if (num <= min)
					errors = '<fmt:message key="js.error.min.number"/>';
				if(errors)
					alert('<fmt:message key="js.error.title"/>\n'+errors);
				
			}
			function allowNewTopic(){
				var allowNew = document.getElementsByName("forum.allowNewTopic");
				var min= document.getElementById("minimumReply");
				var max= document.getElementById("maximumReply");
				
				//disable reply limited drop list
				if(allowNew[0].checked){
					min.disabled=true;
					max.disabled=true;
				}
				//enable reply limited drop list
				if(allowNew[1].checked){
					min.disabled=false;
					max.disabled=false;
				}
			}
			allowNewTopic();
			
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


