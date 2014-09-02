<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<link href="${lams}css/jquery.mobile.css" rel="stylesheet"/>
<link href="${lams}css/defaultHTML_learner_mobile.css" rel="stylesheet"/>
	
<script src="${lams}includes/javascript/jquery.js" type="text/javascript"></script>
<script src="${lams}includes/javascript/jquery.mobile.js" type="text/javascript"></script>
<script src="${tool}includes/javascript/fastclick.js" type="text/javascript"></script>

<script type="text/javascript">
	window.addEventListener('load', function() {
	    new FastClick(document.body);
	}, false);
</script>