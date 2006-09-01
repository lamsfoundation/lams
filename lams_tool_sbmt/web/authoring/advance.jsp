<%@include file="/common/taglibs.jsp"%>

<!-- Advance Tab Content -->
<table cellpadding="0">
	<!-- Instructions Row -->
	<tr>
		<td>
			<html:checkbox property="lockOnFinished" styleClass="noBorder">
				<fmt:message key="label.authoring.advance.lock.on.finished" />
			</html:checkbox>
		</td>
	</tr>
	<tr>
		<td >
			<html:checkbox property="reflectOnActivity" styleClass="noBorder"  styleId="reflectOn">
				<fmt:message key="label.authoring.advanced.reflectOnActivity" />
			</html:checkbox>
		</td>
	</tr>
	<tr>
		<td >
			<lams:STRUTS-textarea property="reflectInstructions" styleId="reflectInstructions" cols="30" rows="3" />
		</td>
	</tr>		
</table>
