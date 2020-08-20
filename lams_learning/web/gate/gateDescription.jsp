<%@ taglib uri="tags-core" prefix="c"%>
<div class="panel panel-default">
    <c:if test="${!empty gateForm.gate.title}">
        <div class="panel-heading">
         <div class="panel-title">
            <span id="gate-name"><lams:out value="${gateForm.gate.title}" escapeHtml="true" /></span>
         </div>
        </div>
    </c:if>
    <c:if test="${!empty gateForm.gate.description}">
        <div class="panel-body">
            <lams:out value="${gateForm.gate.description}" escapeHtml="true" />
        </div>
    </c:if>
</div>
