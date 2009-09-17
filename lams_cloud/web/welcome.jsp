<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-function" prefix="fn"%>
<%@ taglib uri="tags-tiles" prefix="tiles" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<lams:html>
<lams:head>
	<title><fmt:message key="title.lams"/> :: <fmt:message key="index.welcome" /></title>
	<lams:css style="main" />
	<link rel="icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
	
	<link rel="stylesheet" type="text/css" href="<html:rewrite page='/includes/css/cloud.css'/>"/>
	<link type="text/css" href="includes/css/jquery-ui-1.7.2.custom.css" rel="stylesheet" />	
	<style media="screen,projection" type="text/css">
		#page-mycourses { /*whole layout of page*/
			background: url('includes/images/lams_logo2.gif') no-repeat 5% -7px;
		}
		.main_font_size{font-size: 13px;}
		.table_login{position: relative; top: -150px; left: 20px; padding: 15px; width: 200px; text-align: center;}
		
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
		
		var name = $("#name"),
			email = $("#email"),
			password = $("#password"),
			allFields = $([]).add(name).add(email).add(password),
			tips = $("#validateTips");

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
		
		$("#dialog").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 300,
			modal: true,
			buttons: {
				'Create an account': function() {
					var bValid = true;
					allFields.removeClass('ui-state-error');

					bValid = bValid && checkLength(name,"username",3,16);
					bValid = bValid && checkLength(email,"email",6,80);
					bValid = bValid && checkLength(password,"password",5,16);

					bValid = bValid && checkRegexp(name,/^[a-z]([0-9a-z_])+$/i,"Username may consist of a-z, 0-9, underscores, begin with a letter.");
					// From jquery.validate.js (by joern), contributed by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/
					bValid = bValid && checkRegexp(email,/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i,"eg. ui@jquery.com");
					bValid = bValid && checkRegexp(password,/^([0-9a-zA-Z])+$/,"Password field only allow : a-z 0-9");
					
					if (bValid) {
						$('#users tbody').append('<tr>' +
							'<td>' + name.val() + '</td>' + 
							'<td>' + email.val() + '</td>' + 
							'<td>' + password.val() + '</td>' +
							'</tr>'); 
						
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
		
		
		
		$('#create-user').click(function() {
			$('#dialog').dialog('open');
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
			autoOpen: true,
			height: 360,
			width: 350,
			modal: true,
			buttons: {
				'<fmt:message key="cloud.login.access.lams.cloud" />': function() {
					var bValid = true;
					allFields.removeClass('ui-state-error');

					//bValid = bValid && checkLength(email,"email",6,80);
					//bValid = bValid && checkLength(password,"password",5,16);

					//bValid = bValid && checkRegexp(email,/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i,"eg. ui@jquery.com");
					bValid = bValid && checkRegexp(password,/^([0-9a-zA-Z])+$/,"Password field only allow : a-z 0-9");
					
					if (bValid) {
						//$('#users tbody').append('<tr>' +
							//'<td>' + name.val() + '</td>' + 
							//'<td>' + email.val() + '</td>' + 
							//'<td>' + password.val() + '</td>' +
							//'</tr>'); 
						submitForm();
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
//		  alert($("#j_username").val());
//		  alert($("#j_password").val());
		  $("#j_password").val(hex_sha1(password));
//		  alert($("#j_password").val());
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
		
			<table cellpadding="0">
				<tbody>
					<tr>
						<td>
							<div class="welcome">
								<div class="float-left main_font_size">
									<div>
										<fmt:message key="cloud.welcome.you.are.about.create">
											<fmt:param value="${param.sequenceName}" />
										</fmt:message>
									</div>
									<div>
										<fmt:message key="cloud.welcome.this.is.as.simple" />
									</div>									
								</div>
							</div>
						
								<table style="text-align: center; margin-top: 40px; margin-left:70px; max-width: 600px; line-height: 1.3;">
									<tr >
										<td style="font-size: large; ">
											Login or
											<br>
											Sign up
										</td>
										<td>
											<img alt="" src="<lams:LAMSURL/>/images/css/blue_arrow_right.gif">
										</td>
										<td style="font-size: large;">
											Add students
											<br>
											to lesson
										</td>
										<td>
											<img alt="" src="<lams:LAMSURL/>/images/css/blue_arrow_right.gif">										
										</td>
										<td style="font-size: large;">
											Start and 
											<br>
											VOILA!
										</td>
									</tr>
								</table>
							

						</td>
					</tr>
				</tbody>
			</table>
		
			<div class="float-left main_font_size" style="margin-top: 40px; margin-left:40px;  ">
				See how this works
			</div>
			<div style="height: 180px; text-align: left; margin-top: 70px;">
				<object width="410" height="277" align="left" style="margin-left: 40px; margin-top: 0px;"><param name="movie" value="http://www.youtube.com/v/Vn29DvMITu4&hl=ru&fs=1&rel=0&color1=0x006699&color2=0x54abd6"></param><param name="allowFullScreen" value="true"></param><param name="allowscriptaccess" value="always"></param><param name="wmode" value="opaque" /><embed wmode="opaque" src="http://www.youtube.com/v/Vn29DvMITu4&hl=ru&fs=1&rel=0&color1=0x006699&color2=0x54abd6" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="410" height="277"></embed></object>
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
						<button id="create-user" class="ui-button ui-state-default ui-corner-all">
							<fmt:message key="cloud.welcome.signup.button" />
						</button>
					</td>
				</tr>	
			</table>
			
	</div>
	<div id="footer">
		<p>
			<fmt:message key="cloud.welcome.LAMS.version" /> <%=Configuration.get(ConfigurationKeys.VERSION)%>
			<a href="<lams:LAMSURL/>www/copyright.jsp" target='copyright' onClick="openCopyRight()">
				&copy; <fmt:message key="cloud.welcome.LAMS.copyright.short" /> 
			</a>
		</p>
	</div>
</div>

<div id="loginDialog" title="<fmt:message key='cloud.login.access.lams.cloud' />">
	<p id="validateLoginTips"><fmt:message key="cloud.login.enter.your.email.password" /></p>

	<form action="<lams:LAMSURL/>cloud/j_security_check" method="post" name="loginForm" id="loginForm">
		<c:if test="${!empty param.failed}">
			<div class="ui-widget">
				<div class="ui-state-error ui-corner-all" style="padding: 0pt 0.7em;"> 
					<p><span class="ui-icon ui-icon-alert" style="float: left; margin-right: 0.3em;"></span> 
					<strong>Alert:</strong> <fmt:message key="error.login"/></p>
				</div>
			</div>
		</c:if>		
	
	<fieldset>
		<label for="j_username"><fmt:message key="cloud.login.email" /></label>
		<input type="text" name="j_username" id="j_username" class="text ui-widget-content ui-corner-all" onkeypress="onEnter(event)"/>
		<label for="j_password"><fmt:message key="cloud.login.password" /></label>
		<input type="text" name="j_password" id="j_password" value="" class="text ui-widget-content ui-corner-all" onkeypress="onEnter(event)"/>
	</fieldset>
	</form>
	<fmt:message key="cloud.login.forgot.password" />
</div>

<div id="dialog" title="<fmt:message key='cloud.signup.signup.lams.cloud' />">
	<p id="validateTips"><fmt:message key="cloud.signup.enter.your.first.last.name.password" /></p>

	<form >
	<fieldset>
		<label for="name"><fmt:message key="cloud.signup.first.name" /></label>
		<input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" />
		<label for="email"><fmt:message key="cloud.signup.last.name" /></label>
		<input type="text" name="email" id="email" value="" class="text ui-widget-content ui-corner-all" />
		<label for="password"><fmt:message key="cloud.signup.email" /></label>
		<input type="password" name="password" id="password" value="" class="text ui-widget-content ui-corner-all" />
	</fieldset>
	</form>
</div>

</body>
</lams:html>

