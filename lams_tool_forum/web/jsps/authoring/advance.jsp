<%@ include file="/includes/taglibs.jsp"%>

<!--   Advance Tab Content    -->

<table class="forms">
	<!-- Instructions Row -->
	<tr>
		<td colspan="2" class="formcontrol">
			<html:checkbox property="forum.lockWhenFinished">
				<fmt:message key="label.authoring.advance.lock.on.finished" />
			</html:checkbox>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="formcontrol">
			<html:checkbox property="forum.allowEdit">
				<fmt:message key="label.authoring.advance.allow.edit" />
			</html:checkbox>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="formcontrol">
			<html:checkbox property="forum.allowRichEditor" styleId="richEditor">
				<fmt:message key="label.authoring.advance.use.richeditor" />
			</html:checkbox>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="formcontrol">
			<html:checkbox property="forum.limitedInput" styleId="limitedInput">
				<fmt:message key="label.authoring.advance.limited.input" />
			</html:checkbox>
			<html:text property="forum.limitedChar" styleId="limitedChar" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<html:errors />
		</td>
	</tr>
</table>

<!-- Button Row -->
<hr/>
<p align="right">
	<html:submit property="save" styleClass="button">
		<fmt:message key="label.authoring.save.button" />
	</html:submit>
	<html:button property="cancel" onclick="window.close()" styleClass="button">
		<fmt:message key="label.authoring.cancel.button" />
	</html:button>
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
	</script>


