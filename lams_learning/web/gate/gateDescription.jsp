<%@ taglib uri="tags-core" prefix="c"%>

<div class="cardx lcardx">
    <c:if test="${!empty gateForm.gate.description}">
        <div class="card-bodyx alert alert-warning">
            <lams:out value="${gateForm.gate.description}" escapeHtml="true" />
        </div>
    </c:if>
</div>
