<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title" scope="request"><fmt:message key="authoring.learning.design.templates"/></c:set>
	<title>${title}</title>

	<lams:css />
</lams:head>

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
