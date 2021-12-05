<%@ include file="/common/taglibs.jsp"%>
 
 <c:set var="lams"><lams:LAMSURL /></c:set>
 <c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
 <c:set var="localeCountry"><lams:user property="localeCountry" /></c:set> 
  
<c:catch var="e">
<div id="wrapper" style="width:100%; text-align:center">
	<img class="img-responsive" src="${lams}/www/public/ld-templates/${param.templateName}/${param.templateName}.svg"/>
</div>
</c:catch>

<div class="voffset10">
<c:catch var="e">
    	<c:set var="helptext"><c:import url="${lams}/www/public/ld-templates/${param.templateName}/${param.templateName}intro_${localeLanguage}_${localeCountry}.html" /></c:set>
</c:catch>
<c:choose>
<c:when test="${(empty e) and (fn:indexOf(helptext, 'CUSTOMERRORPAGE404') lt 0) }">
	${helptext}
</c:when>
<c:otherwise>
	<!-- translated page was not found as the 404 error string is in the returned page. Use default English -->
    <c:catch var="e2">
   	 	<c:import url="${lams}/www/public/ld-templates/${param.templateName}/${param.templateName}intro.html" />
    	</c:catch>
</c:otherwise>
</c:choose>
</div>
