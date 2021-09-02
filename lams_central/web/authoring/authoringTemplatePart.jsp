<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:forEach var="tool" items="${tools}">
	<div
		 toolId="${tool.toolId}"
		 learningLibraryId="${tool.learningLibraryId}"
		 learningLibraryTitle="${tool.learningLibraryTitle}"
		 defaultToolContentId="${tool.defaultToolContentId}"
		 supportsOutputs="${tool.supportsOutputs}"
		 iconPath="${tool.iconPath}"
		 childToolIds="
		 <c:forEach var='childId' items='${tool.childToolIds}'>
		 	${childId},
		 </c:forEach>
		 "
		 class="template"
		 <%-- Hide invalid tools --%>
		 <c:if test="${not tool.valid}">
		 	style="display: none"
		 </c:if>
		 >
		 <div>
			<div class="media">
						<div class="media-left">
							<c:if test="${tool.valid}">
								<img src="<lams:LAMSURL/>${tool.iconPath}" />
							</c:if>
						</div>
						<div class="media-body media-middle tool-display-name">
					<c:out value="${tool.toolDisplayName}" />
				</div>
			</div>
		</div>
	</div>
</c:forEach>

<div
	 learningLibraryId="grouping"
	 learningLibraryTitle="grouping"
	 iconPath="images/svg/grouping.svg"
	 class="template"
	 >
	 <div>
		<div class="media">
					<div class="media-left">
						<img src="<lams:LAMSURL/>images/svg/grouping.svg" />
					</div>
					<div class="media-body media-middle tool-display-name">
				<fmt:message key="authoring.fla.page.menu.group" />
			</div>
		</div>
	</div>
</div>

<div
	 learningLibraryId="gate"
	 learningLibraryTitle="gate"
	 iconPath="images/svg/gateClosed.svg"
	 class="template"
	 >
	 <div>
		<div class="media">
					<div class="media-left">
						<img src="<lams:LAMSURL/>images/svg/gateClosed.svg" />
					</div>
					<div class="media-body media-middle tool-display-name">
				<fmt:message key="authoring.fla.page.menu.flow.gate" />
			</div>
		</div>
	</div>
</div>

<div
	 learningLibraryId="branching"
	 learningLibraryTitle="branching"
	 iconPath="images/svg/branchingStart.svg"
	 class="template"
	 >
	 <div>
		<div class="media">
					<div class="media-left">
						<img src="<lams:LAMSURL/>images/svg/branchingStart.svg" />
					</div>
					<div class="media-body media-middle tool-display-name">
				<fmt:message key="authoring.fla.page.menu.flow.branch" />
			</div>
		</div>
	</div>
</div>

<div
	 learningLibraryId="optional"
	 learningLibraryTitle="optional"
	 iconPath="images/svg/optional.svg"
	 class="template"
	 >
	 <div>
		<div class="media">
					<div class="media-left">
						<img src="<lams:LAMSURL/>images/svg/optional.svg" />
					</div>
					<div class="media-body media-middle tool-display-name">
				<fmt:message key="authoring.fla.page.menu.optional" />
			</div>
		</div>
	</div>
</div>

<div
	 learningLibraryId="floating"
	 learningLibraryTitle="floating"
	 iconPath="images/svg/floating.svg"
	 class="template"
	 >
	 <div>
		<div class="media">
					<div class="media-left">
						<img src="<lams:LAMSURL/>images/svg/floating.svg" />
					</div>
					<div class="media-body media-middle tool-display-name">
				<fmt:message key="authoring.fla.page.menu.optional.support" />
			</div>
		</div>
	</div>
</div>