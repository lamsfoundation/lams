<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<head>

	<title>
		Chat Title, TODO please to use application resources
	</title>
	<lams:css/>
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
    <script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
   	<script type="text/javascript" src="${tool}includes/javascript/fckcontroller.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/tabcontroller.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/xmlrequest.js"></script>
</head>