	<!---------------------------Advance Tab Content ------------------------>	
	<div id='content_a'  class="tabbody content_a" >
	<h2><fmt:message key="label.authoring.heading.advance.desc" /></h2>
	<table class="forms">
		<!-- Instructions Row -->
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="forum.lockWhenFinished" value="1">
					<fmt:message key="label.authoring.advance.lock.on.finished" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="forum.allowEdit" value="1">
					<fmt:message key="label.authoring.advance.allow.edit" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="forum.allowRichEditor" value="1">
					<fmt:message key="label.authoring.advance.use.richeditor" />
				</html:checkbox>
			</td>
		</tr>
		<tr><td colspan="2"><html:errors/></td></tr>
	</table>

		<!-- Button Row -->
		<HR>
		<p align="right">
			<html:submit property="save" styleClass="a.button">
				<fmt:message key="label.authoring.save.button" />
			</html:submit>
			<html:button property="cancel"
				onclick="window.close()" styleClass="a.button">
				<fmt:message key="label.authoring.cancel.button" />
			</html:button>
		</p>
	</div>