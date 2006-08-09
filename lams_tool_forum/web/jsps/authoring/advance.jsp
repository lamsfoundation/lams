<%@ include file="/includes/taglibs.jsp"%>

<!--   Advance Tab Content    -->

<table cellpadding="0">

	<!-- Advance options Row -->
	<tr>
		<td>
			<html:checkbox property="forum.lockWhenFinished">
				<fmt:message key="label.authoring.advance.lock.on.finished" />
			</html:checkbox>
		</td>
	</tr>
	<tr>
		<td>
			<html:checkbox property="forum.allowEdit">
				<fmt:message key="label.authoring.advance.allow.edit" />
			</html:checkbox>
		</td>
	</tr>
	<tr>
		<td>
			<html:checkbox property="forum.allowUpload">
				<fmt:message key="label.authoring.advance.allow.upload" />
			</html:checkbox>
		</td>
	</tr>
	<tr>
		<td>
			<html:radio property="forum.allowNewTopic" value="true" onclick="allowNewTopic()">
				<fmt:message key="label.authoring.advance.allow.new.topics" />
			</html:radio>
		</td>
	</tr>
	<tr>
		<td>
			<html:radio property="forum.allowNewTopic" value="false" onclick="allowNewTopic()">
				<fmt:message key="label.authoring.advance.number.reply" />
			</html:radio>
			<BR>&nbsp;&nbsp;
			<fmt:message key="label.authoring.advance.minimum.reply" />
			<html:select property="forum.minimumReply" styleId="minimumReply">
				<html:option value="0"><fmt:message key="label.authoring.advance.no.minimum"/></html:option>
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
				<html:option value="0"><fmt:message key="label.authoring.advance.no.maximum"/></html:option>
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
		</td>
	</tr>
	<tr>
		<td>
			<html:checkbox property="forum.allowRichEditor" styleId="richEditor">
				<fmt:message key="label.authoring.advance.use.richeditor" />
			</html:checkbox>
		</td>
	</tr>	
	<tr>
		<td>
			<html:checkbox property="forum.limitedInput" styleId="limitedInput">
				<fmt:message key="label.authoring.advance.limited.input" />
			</html:checkbox>
			<html:text property="forum.limitedChar" styleId="limitedChar" />
		</td>
	</tr>
</table>

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
			
</script>


