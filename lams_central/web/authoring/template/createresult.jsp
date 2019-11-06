<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<!DOCTYPE html>
<lams:html>
<lams:head>
<c:set var="title" scope="request">
	<fmt:message key="authoring.learning.design.templates"/>
</c:set>

	<title>${title}</title>
	<!-- ********************  CSS ********************** -->
	<lams:css />
	
</lams:head>

<%-- <c:set var="learningDesignID" scope="request">
	${param.learningDesignID}
</c:set>
 --%>
<body class="stripes">
	<!-- Flashless Authoring is looks for this string: plearningDesignID=${param.learningDesignID} -->
	<!-- Flashless Authoring is looks for this string: learningDesignID=${learningDesignID} -->
	
	<lams:Page type="admin" title="${title}">

		<span id="resultMessage"><fmt:message key="authoring.template.successful"><fmt:param>${param.learningDesigntitle}</fmt:param></fmt:message></span>
		<div class="voffset10">
			 <a href="javascript:;" onclick="closeWindow();" class="btn btn-primary pull-right button-close"><fmt:message key="button.close" /></a>
		</div>

	</lams:Page>
</body>
</lams:html>
