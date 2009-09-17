
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-function" prefix="fn"%>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ page import="org.lamsfoundation.lams.security.JspRedirectStrategy" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%	
	if (JspRedirectStrategy.loginPageRedirected(request,response))
	{
		return;
	}		


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<lams:html>
<lams:head>
	<title><fmt:message key="title.lams"/> :: <fmt:message key="index.welcome" /></title>
	<lams:css style="main" />
	<link rel="icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	
	<link type="text/css" href="includes/css/cloud.css" rel="stylesheet" />
	<link type="text/css" href="includes/css/jquery-ui-1.7.2.custom.css" rel="stylesheet" />	
	<style media="screen,projection" type="text/css">
		input.text { margin-bottom:12px; width:95%; padding: .4em; }
		fieldset { padding:0; border:0; margin-top:25px; }
		h1 { font-size: 1.2em; margin: .6em 0; }
		div#users-contain {  width: 350px; margin: 20px 0; }
		div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
		div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
		.ui-button { outline: 0; margin:0; padding: .4em 1em .5em; text-decoration:none;  !important; cursor:pointer; position: relative; text-align: center; }
		.ui-dialog .ui-state-highlight, .ui-dialog .ui-state-error { padding: .3em;  }
	</style>

	<script type="text/javascript" src="includes/javascript/cloud.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-1.3.2.min.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui-1.7.2.custom.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/sha1.js"></script>	
	<script type="text/javascript">
	$(function() {
		
		var firstName = $("#firstName"),
			lastName = $("#lastName"),
			email = $("#email"),
			confirmEmail = $("#confirmEmail"),
			password = $("#password"),
			allFields = $([]).add(firstName).add(lastName).add(email).add(confirmEmail).add(password),
			tips = $("#validateSignupTips");

		function updateTips(t) {
			tips.text(t).effect("highlight",{},1500);
		}

		function checkLength(o,n,min,max) {
			if ( o.val().length > max || o.val().length < min ) {
				o.addClass('ui-state-error');
				updateTips("Length of " + n + " must be between "+min+" and "+max+".");
				return false;
			} else {
				return true;
			}
		}

		function checkRegexp(o,regexp,n) {
			if ( !( regexp.test( o.val() ) ) ) {
				o.addClass('ui-state-error');
				updateTips(n);
				return false;
			} else {
				return true;
			}
		}

		function checkEmailsEqual() {
			if ( !( email.val() == confirmEmail.val() ) ) {
				confirmEmail.addClass('ui-state-error');
				updateTips("Emails are not equal");
				return false;
			} else {
				return true;
			}
		}		
		
		$("#signupDialog").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 480,
			modal: true,
			buttons: {
				'<fmt:message key="cloud.signup.signup" />': function() {
					var bValid = true;
					allFields.removeClass('ui-state-error');

					bValid = bValid && checkLength(firstName,"firstName",2,16);
					bValid = bValid && checkLength(lastName,"lastName",2,16);
					bValid = bValid && checkLength(email,"email",3,80);
					bValid = bValid && checkLength(confirmEmail,"confirmEmail",3,80);
					bValid = bValid && checkLength(password,"password",3,16);

					bValid = bValid && checkRegexp(firstName,/^[a-z]([0-9a-z_])+$/i,"first name may consist of a-z, 0-9, underscores, begin with a letter.");
					bValid = bValid && checkRegexp(lastName,/^[a-z]([0-9a-z_])+$/i,"last name may consist of a-z, 0-9, underscores, begin with a letter.");
					bValid = bValid && checkRegexp(email,/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i,"eg. admin@lams.com");
					bValid = bValid && checkRegexp(confirmEmail,/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i,"eg. admin@lams.com");
					bValid = bValid && checkEmailsEqual();
					bValid = bValid && checkRegexp(password,/^([0-9a-zA-Z])+$/,"Password field only allow : a-z 0-9");
					
					if (bValid) {
						$(this).dialog('close');
					}
				},
				Cancel: function() {
					$(this).dialog('close');
				}
			},
			close: function() {
				allFields.val('').removeClass('ui-state-error');
			}
		});
		
		$('#signupButton').click(function() {
			$('#signupDialog').dialog('open');
		})
		.hover(
			function(){ 
				$(this).addClass("ui-state-hover"); 
			},
			function(){ 
				$(this).removeClass("ui-state-hover"); 
			}
		).mousedown(function(){
			$(this).addClass("ui-state-active"); 
		})
		.mouseup(function(){
				$(this).removeClass("ui-state-active");
		});
	});

	// --------------------------------------------------------------------------------------
	
	$(function() {
		
		var email = $("#j_username"),
			password = $("#j_password"),
			allFields = $([]).add(email).add(password),
			tips = $("#validateLoginTips");

		function updateTips(t) {
			tips.text(t).effect("highlight",{},1500);
		}

		function checkLength(o,n,min,max) {
			if ( o.val().length > max || o.val().length < min ) {
				o.addClass('ui-state-error');
				updateTips("Length of " + n + " must be between "+min+" and "+max+".");
				return false;
			} else {
				return true;
			}
		}

		function checkRegexp(o,regexp,n) {
			if ( !( regexp.test( o.val() ) ) ) {
				o.addClass('ui-state-error');
				updateTips(n);
				return false;
			} else {
				return true;
			}
		}
		
		$("#loginDialog").dialog({
			bgiframe: true,
			autoOpen: ${!empty param.failed},
			height: 350,
			width: 350,
			modal: true,
			buttons: {
				'<fmt:message key="cloud.login.login" />': function() {
					var bValid = true;
					allFields.removeClass('ui-state-error');

					bValid = bValid && checkLength(email,"email",2,16);
					bValid = bValid && checkLength(password,"password",3,80);

					//bValid = bValid && checkRegexp(email,/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i,"eg. admin@lams.com");
					bValid = bValid && checkRegexp(password,/^([0-9a-zA-Z])+$/,"Password field only allow : a-z 0-9");
					
					if (bValid) {
						submitForm();
					}
				},
				Cancel: function() {
					$(this).dialog('close');
				}
			},
			close: function() {
				allFields.val('').removeClass('ui-state-error');
			}
		});
		
		$('#loginButton').click(function() {
			$('#loginDialog').dialog('open');
		})
		.hover(
			function(){ 
				$(this).addClass("ui-state-hover"); 
			},
			function(){ 
				$(this).removeClass("ui-state-hover"); 
			}
		).mousedown(function(){
			$(this).addClass("ui-state-active"); 
		})
		.mouseup(function(){
				$(this).removeClass("ui-state-active");
		});
	});


	function submitForm(){
		  var password = $("#j_password").val();
		  $("#j_password").val(hex_sha1(password));
		  document.loginForm.submit();
	}
		
	function onEnter(event){
		intKeyCode = event.keyCode;
		if (intKeyCode == 13) {
			submitForm();
		}
	}
	</script>	
	
</lams:head>


<body class="my-courses">
<div id="page-mycourses">
	<div id="header-my-courses">
		<div id="nav-right">
		</div>
	</div>

	<div id="content-main">
		
		<div class="main_font_size" >
			<div>
				<fmt:message key="cloud.welcome.you.are.about.create">
					<fmt:param value="${param.sequenceName}" />
				</fmt:message>
			</div>
			<div>
				<fmt:message key="cloud.welcome.this.is.as.simple" />
			</div>									
		</div>
						
		<table class="current_phase_table">
			<tr >
				<td>
					<fmt:message key="cloud.welcome.login.or.signup">
						<fmt:param value="<br>" />
					</fmt:message>					
				</td>
				<td>
					<img alt="" src="<lams:LAMSURL/>/images/css/blue_arrow_right.gif">
				</td>
				<td>
					<fmt:message key="cloud.welcome.add.students">
						<fmt:param value="<br>" />
					</fmt:message>					
				</td>
				<td>
					<img alt="" src="<lams:LAMSURL/>/images/css/blue_arrow_right.gif">										
				</td>
				<td>
					<fmt:message key="cloud.welcome.start.and.voila">
						<fmt:param value="<br>" />
					</fmt:message>					
				</td>
			</tr>
		</table>

		<div class="main_font_size" style="margin-top: 70px;">
			<fmt:message key="cloud.welcome.see.how.this.works" />
		</div>
		<div style="height: 180px; text-align: left; margin-top: 15px;">
			<object width="410" height="277" align="left" style="margin-left: 20px; margin-top: 0px;"><param name="movie" value="http://www.youtube.com/v/Vn29DvMITu4&hl=ru&fs=1&rel=0&color1=0x006699&color2=0x54abd6"></param><param name="allowFullScreen" value="true"></param><param name="allowscriptaccess" value="always"></param><param name="wmode" value="opaque" /><embed wmode="opaque" src="http://www.youtube.com/v/Vn29DvMITu4&hl=ru&fs=1&rel=0&color1=0x006699&color2=0x54abd6" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="410" height="277"></embed></object>
		</div>
			
			<table class="table_login">
				<tr>
					<td>
						<button id="loginButton" class="ui-button ui-state-default ui-corner-all">
							<fmt:message key="cloud.welcome.login.button" />
						</button>
					</td>
				</tr>
				<tr>
					<td class="main_font_size">
						<fmt:message key="cloud.welcome.or.label" />					
					</td>
				</tr>
				<tr>
					<td>
						<button id="signupButton" class="ui-button ui-state-default ui-corner-all">
							<fmt:message key="cloud.welcome.signup.button" />
						</button>
					</td>
				</tr>	
			</table>
			
	</div>
	<div id="footer">
		<%@ include file="copyright.jsp"%>
	</div>
</div>

<div id="loginDialog" title="<fmt:message key='cloud.login.access.lams.cloud' />">
	<form action="<lams:LAMSURL/>cloud/j_security_check" method="post" name="loginForm" id="loginForm">
	
		<c:if test="${!empty param.failed}">
			<div class="ui-widget">
				<div class="ui-state-error ui-corner-all" style="padding: 0pt 0.7em;"> 
					<p><span class="ui-icon ui-icon-alert" style="float: left; margin-right: 0.3em;"></span> 
					<fmt:message key="error.login"/></p>
				</div>
			</div>
		</c:if>
		
		<p id="validateLoginTips"><fmt:message key="cloud.login.enter.your.email.password" /></p>			
	
	<fieldset>
		<label for="j_username"><fmt:message key="cloud.login.email" /></label>
		<input type="text" name="j_username" id="j_username" class="text ui-widget-content ui-corner-all" onkeypress="onEnter(event)"/>
		<label for="j_password"><fmt:message key="cloud.login.password" /></label>
		<input type="text" name="j_password" id="j_password" value="" class="text ui-widget-content ui-corner-all" onkeypress="onEnter(event)"/>
	</fieldset>
	</form>
	<fmt:message key="cloud.login.forgot.password" />
</div>

<div id="signupDialog" title="<fmt:message key='cloud.signup.signup.lams.cloud' />">
	<form >
		<p id="validateSignupTips"><fmt:message key="cloud.signup.enter.your.first.last.name.password" /></p>	
	
		<fieldset>
			<label for="firstName"><fmt:message key="cloud.signup.first.name" /></label>
			<input type="text" name="firstName" id="firstName" class="text ui-widget-content ui-corner-all" />
			<label for="lastName"><fmt:message key="cloud.signup.last.name" /></label>
			<input type="text" name="lastName" id="lastName" value="" class="text ui-widget-content ui-corner-all" />			
			<label for="email"><fmt:message key="cloud.signup.email" /></label>
			<input type="text" name="email" id="email" value="" class="text ui-widget-content ui-corner-all" />
			<label for="confirmEmail"><fmt:message key="cloud.signup.confirm.email" /></label>
			<input type="text" name="confirmEmail" id="confirmEmail" value="" class="text ui-widget-content ui-corner-all" />			
			<label for="password"><fmt:message key="cloud.signup.password" /></label>
			<input type="password" name="password" id="password" value="" class="text ui-widget-content ui-corner-all" />
		</fieldset>
	</form>
</div>

</body>
</lams:html>


