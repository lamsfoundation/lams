<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<!DOCTYPE html>
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
			
			<c:choose>
				<c:when test="${registered}" >
					<iframe
						src="<lams:LAMSURL/>/lamsCommunityLogin.do?dest=import&customCSV=${customCSV}"
						id="lamsCommunityIframe" name="lamsCommunityIframe"
						style="width:95%;height:510px;border:0px;display:block;overflow:auto;margin-left:auto; margin-right:auto;" frameborder="no"
						>
					</iframe>
				</c:when>
				<c:otherwise>
					
					<p>
					<fmt:message key="label.lamscommunity.notRegistered.1" />
					</p>
					
					<p>
					<fmt:message key="label.lamscommunity.notRegistered.2" /> <a href="http://lamscommunity.org/lamscentral">http://lamscommunity.org/lamscentral</a> 
					</p>

				</c:otherwise>
			</c:choose>
			<br />
		</div>
		<!--closes content-->

		<div id="footer">
		</div>
		<!--closes footer-->
	</body>
</lams:html>
