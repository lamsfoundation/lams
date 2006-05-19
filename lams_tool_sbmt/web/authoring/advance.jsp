
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<!---------------------------Advance Tab Content ------------------------>
<table class="forms">
	<!-- Instructions Row -->
	<tr>
		<td colspan="2 class="formcontrol">
			<html:checkbox property="lockOnFinished" value="1">
				<fmt:message key="label.authoring.advance.lock.on.finished" />
			</html:checkbox>
		</td>
	</tr>
</table>
