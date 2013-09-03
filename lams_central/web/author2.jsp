<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<lams:html>
<lams:head>
	<title>Flashless Authoring</title>
	<style type="text/css">
		#canvas {
			width: 100%;
			height: 100%;
		}
	</style>

	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/raphael/raphael.js"></script>
	<script type="text/javascript" src="includes/javascript/authoring.js"></script>
</lams:head>
<body>
	<div id="canvas"></div>
</body>
</lams:html>