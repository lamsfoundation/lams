<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<link href="${lams}css/thickbox.css" rel="stylesheet" type="text/css" media="screen">

<script type="text/javascript" src="${tool}includes/javascript/authoring.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript">
	var tb_pathToImage = "${lams}images/loadingAnimation.gif";
</script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/thickbox-compressed.js"></script>
