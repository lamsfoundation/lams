<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
	<lams:head>
		<title><fmt:message key="title.import" /></title>
		<!-- ********************  CSS ********************** -->
		<lams:css style="main" />
		<script type="text/javascript">
			function closeWin(){
				window.close();
			}
		</script>
	</lams:head>

	<body class="stripes">
		<div id="header">
		
		</div>
		<div id="content">
			
			<h1>
				<fmt:message key="title.import.lamscommunity" />
			</h1>
			
			<br />
			
			<iframe
				src="<lams:LAMSURL/>/lamsCommunityLogin.do?dest=import&customCSV=${customCSV}"
				id="lamsCommunityIframe" name="lamsCommunityIframe"
				style="width:95%;height:510px;border:0px;display:block;overflow:auto;margin-left:auto; margin-right:auto;" frameborder="no"
				>
			</iframe>
			<!--  
			<iframe
				src="http://172.20.100.188:8080/lams/authoring/importToolContent.do?method=importLCFinish"
				id="lamsCommunityIframe" name="lamsCommunityIframe"
				style="width:90%;height:500px;border:0px;display:block;overflow:auto" frameborder="0"
				>
			</iframe>
			-->
			<br />
		</div>
		<!--closes content-->

		<div id="footer">
		</div>
		<!--closes footer-->
	</body>
</lams:html>
