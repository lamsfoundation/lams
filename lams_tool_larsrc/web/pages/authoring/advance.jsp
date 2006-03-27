
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<!---------------------------Advance Tab Content ------------------------>	
	<table class="forms">
		<!-- Instructions Row -->
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="resource.lockWhenFinished" value="1">
					<fmt:message key="label.authoring.advance.lock.on.finished" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="resource.runAuto" value="1">
					<fmt:message key="label.authoring.advance.run.content.auto" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td class="formcontrol">
				<html:select property="resource.minViewResourceNumber" value="1">
					<html:option value="1">1</html:option>
					<html:option value="2">2</html:option>
					<html:option value="3">3</html:option>
					<html:option value="4">4</html:option>
					<html:option value="5">5</html:option>
				</html:select>
			</td>
			<td class="formcontrol"  width="97%">
					<fmt:message key="label.authoring.advance.mini.number.resources.view" />
			</td>
		</tr>
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="resource.allowAddUrls" value="1">
					<fmt:message key="label.authoring.advance.allow.learner.add.urls" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="resource.allowAddFiles" value="1">
					<fmt:message key="label.authoring.advance.allow.learner.add.files" />
				</html:checkbox>
			</td>
		</tr>
		<tr><td colspan="2"><html:errors/></td></tr>
	</table>