<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"
	value="<%=request
									.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Advance Tab Content -->

<p class="small-space-top">
	<html:checkbox property="resource.lockWhenFinished"
		styleClass="noBorder">
		<fmt:message key="label.authoring.advance.lock.on.finished" />
	</html:checkbox>
</p>

<p>
	<html:checkbox property="resource.runAuto" styleClass="noBorder">
		<fmt:message key="label.authoring.advance.run.content.auto" />
	</html:checkbox>
</p>

<p>
	<html:select property="resource.miniViewResourceNumber"
		styleId="viewNumList">
		<c:forEach begin="1" end="${fn:length(resourceList)}"
			varStatus="status">
			<c:choose>
				<c:when
					test="${formBean.resource.miniViewResourceNumber == status.index}">
					<option value="${status.index}" selected="true">
						${status.index}
					</option>
				</c:when>
				<c:otherwise>
					<option value="${status.index}">
						${status.index}
					</option>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</html:select>

	<fmt:message key="label.authoring.advance.mini.number.resources.view" />
</p>

<p>
	<html:checkbox property="resource.allowAddUrls" styleClass="noBorder">
		<fmt:message key="label.authoring.advance.allow.learner.add.urls" />
	</html:checkbox>
</p>

<p>
	<html:checkbox property="resource.allowAddFiles" styleClass="noBorder">
		<fmt:message key="label.authoring.advance.allow.learner.add.files" />
	</html:checkbox>
</p>

<p>
	<html:checkbox property="resource.reflectOnActivity"
		styleClass="noBorder" styleId="reflectOn">
		<fmt:message key="label.authoring.advanced.reflectOnActivity" />
	</html:checkbox>
</p>

<p>
	<html:textarea property="resource.reflectInstructions"
		styleId="reflectInstructions" cols="30" rows="3" />
</p>
