<script type="text/javascript" src="${lams}includes/javascript/uppy/uppy.min.js"></script>
<c:choose>
    <c:when test="${language eq 'es'}">
        <script type="text/javascript" src="${lams}includes/javascript/uppy/es_ES.min.js"></script>
    </c:when>
    <c:when test="${language eq 'fr'}">
        <script type="text/javascript" src="${lams}includes/javascript/uppy/fr_FR.min.js"></script>
    </c:when>
    <c:when test="${language eq 'el'}">
        <script type="text/javascript" src="${lams}includes/javascript/uppy/el_GR.min.js"></script>
    </c:when>
    <c:when test="${language eq 'it'}">
        <script type="text/javascript" src="${lams}includes/javascript/uppy/it_IT.min.js"></script>
    </c:when>
</c:choose>