<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"
	value="<%=request
									.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->

<p class="small-space-top">
	<html:checkbox property="survey.lockWhenFinished" styleClass="noBorder" styleId="lockWhenFinished">
	</html:checkbox>
	<label for="lockWhenFinished">
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</label>
</p>

<p>
	<html:checkbox property="survey.showOnePage" styleClass="noBorder" styleId="showOnePage">
	</html:checkbox>
	<label for="showOnePage">
		<fmt:message key="label.authoring.advance.show.on.one.page" />
	</label>
</p>

<p>
	<html:checkbox property="survey.reflectOnActivity"
		styleClass="noBorder" styleId="reflectOn">
	</html:checkbox>
	<label for="reflectOn">
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</label>
</p>

<p>
	<html:textarea property="survey.reflectInstructions"
		styleId="reflectInstructions" cols="30" rows="3" />
</p>

