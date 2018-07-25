<%@ taglib uri="tags-core" prefix="c"%>
<div class="panel panel-default">
    <c:if test="${!empty GateForm.map.gate.title}">
        <div class="panel-heading">
         <div class="panel-title">
            <lams:out value="${GateForm.map.gate.title}" escapeHtml="true" />
         </div>
        </div>
    </c:if>
    <c:if test="${!empty GateForm.map.gate.description}">
        <div class="panel-body">
            <lams:out value="${GateForm.map.gate.description}" escapeHtml="true" />
        </div>
    </c:if>
</div>
