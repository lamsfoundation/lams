<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.2FA.shared.secret" /></title>
	<lams:css/>
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
</lams:head>

<body>

    <!-- Fixed navbar -->
	<nav class="navbar navbar-light navbar-login">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand navbar-brand-login" href="#">
					<%=Configuration.get(ConfigurationKeys.SITE_NAME)%>
				</a>
			</div>
			<div class="navbar-collapse collapse ml-auto">
				<div class="pull-right login-logo" title="LAMS - Learning Activity Management System"></div>
			</div>
		</div>
	</nav>
	
	<div class="container">
		<div class="panel panel-default center-block" style="max-width: 300px;">
			<div class="panel-heading text-center">
				<div class="panel-title"> 
					<fmt:message key="label.2FA.shared.secret" />
				</div>
			</div>
			     			
			<div class="panel-body text-center">
					<div class="input-group">
						<p>
							<fmt:message key="label.your.new.shared.secret">
								<fmt:param>${sharedSecret}</fmt:param>
							</fmt:message>
						</p>
						<p>
							<img alt="" src="${QRCode}">
						</p>
					</div>
		
					<div class="form-group voffset10">
						<div class="col-md-12 controls">
							<input type="submit" class="btn btn-secondary pull-right" value="Ok"
								onClick="javascript:document.location='<lams:LAMSURL/>index.do';" tabindex="1"/>
						</div>	
					</div>
			</div>		
		</div>
	</div>

</body>
</lams:html>
