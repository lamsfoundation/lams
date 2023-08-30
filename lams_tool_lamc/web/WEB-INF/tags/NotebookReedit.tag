<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 

<%@ attribute name="isWidgetEnabled" required="true" rtexprvalue="true"%>
<%@ attribute name="reflectInstructions" required="true" rtexprvalue="true"%>
<%@ attribute name="reflectEntry" required="true" rtexprvalue="true"%>
<%@ attribute name="isReeditButtonEnabled" required="false" rtexprvalue="true"%>
<c:if test="${empty isReeditButtonEnabled}">
	<c:set var="isReeditButtonEnabled" value="true" />
</c:if>
<%@ attribute name="isReflectionsJqGridEnabled" required="false" rtexprvalue="true" %>
<c:if test="${empty isReflectionsJqGridEnabled}">
	<c:set var="isReflectionsJqGridEnabled" value="false" />
</c:if>

<%@ attribute name="notebookHeaderLabelKey" required="false" rtexprvalue="true"%>
<c:if test="${empty notebookHeaderLabelKey}">
	<c:set var="notebookHeaderLabelKey" value="label.notebook" />
</c:if>
<%@ attribute name="noReflectionLabelKey" required="false" rtexprvalue="true"%>
<c:if test="${empty noReflectionLabelKey}">
	<c:set var="noReflectionLabelKey" value="message.no.reflection.available" />
</c:if>
<%@ attribute name="editNotebookLabelKey" required="false" rtexprvalue="true"%>
<c:if test="${empty editNotebookLabelKey}">
	<c:set var="editNotebookLabelKey" value="label.edit" />
</c:if>

<c:if test="${isWidgetEnabled}">
	<div class="card shadow-sm mt-5">
		<div class="card-header">
			<fmt:message key="${notebookHeaderLabelKey}" />
		</div>

		<div class="card-body">
			<div class="m-2">
				<lams:out value="${reflectInstructions}" escapeHtml="true" />
			</div>
			<hr />

			<div class="m-2">
				<p>
					<c:choose>
						<c:when test="${empty reflectEntry}">
							<em><fmt:message key="${noReflectionLabelKey}" /></em>
						</c:when>
						<c:otherwise>
							<lams:out value="${reflectEntry}" escapeHtml="true" />
						</c:otherwise>
					</c:choose>
				</p>

				<c:if test="${isReeditButtonEnabled}">
					<div class="mt-2">
						<button name="editNotebookButton" id="edit-notebook-button" class="btn btn-sm btn-secondary" type="button"
							onclick="continueReflect()">
							<fmt:message key="${editNotebookLabelKey}" />
						</button>
					</div>
				</c:if>
			</div>
		</div>

		<c:if test="${isReflectionsJqGridEnabled && fn:length(reflections) > 0}">
			<div id="reflections-div">
				<table id="reflections" class="scroll" cellpadding="0" cellspacing="0"></table>
			</div>
		</c:if>
	</div>
</c:if>