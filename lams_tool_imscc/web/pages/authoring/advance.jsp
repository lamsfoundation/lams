<%@ include file="/common/taglibs.jsp"%>

<!-- Advance Tab Content -->

<lams:SimplePanel titleKey="label.resource.options">

<div class="checkbox">
	<label for="run-auto">
		<form:checkbox path="commonCartridge.runAuto" id="run-auto"/>
		<fmt:message key="label.authoring.advance.run.content.auto" />
	</label>
</div>

<div class="select">
	<form:select path="commonCartridge.miniViewCommonCartridgeNumber"
			id="viewNumList" cssClass="form-control input-sm form-control-inline roffset5">
		<c:forEach begin="1" end="${fn:length(commonCartridgeList)}" varStatus="status">
			<c:choose>
				<c:when test="${authoringForm.commonCartridge.miniViewCommonCartridgeNumber == status.index}">
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
	</form:select>

	<label for="viewNumList">
	<fmt:message key="label.authoring.advance.mini.number.resources.view" />
	</label>
</div>

</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${authoringForm.commonCartridge.contentId}" />


