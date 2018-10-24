<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ page import="org.apache.struts.action.ActionMessages"
	import="org.lamsfoundation.lams.web.PasswordChangeActionForm"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>
	
<lams:html>
<lams:head>
	<lams:css/>

	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/profile.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript">
		 var mustHaveUppercase = ${mustHaveUppercase},
	     mustHaveNumerics  = ${mustHaveNumerics},
	     mustHaveLowercase  = ${mustHaveLowercase},
	     mustHaveSymbols   = ${mustHaveSymbols};
	
	     $.validator.addMethod("pwcheck", function(value) {
	      return (!mustHaveUppercase || /[A-Z]/.test(value)) && // has uppercase letters 
	    (!mustHaveNumerics || /\d/.test(value)) && // has a digit
	    (!mustHaveLowercase || /[a-z]/.test(value)) && // has a lower case
	    (!mustHaveSymbols || /[`~!@#$%^&*\(\)_\-+={}\[\]\\|:\;\"\'\<\>,.?\/]/.test(value)); //has symbols
	     });
			
		$.validator.addMethod("charactersAllowed", function(value) {
				return /^[A-Za-z0-9\d`~!@#$%^&*\(\)_\-+={}\[\]\\|:\;\"\'\<\>,.?\/]*$/.test(value)
			
			});
	
		$(function() {
	
		  // Setup form validation 
		  $("#change-password").validate({
		  		debug: true,
		  		errorClass: 'help-block',
		      //  validation rules
		      rules: {
		          	oldPassword: "required",
		          password: {
		              required: true,
		              minlength: <c:out value="${minNumChars}"/>,
		              maxlength: 25,
		              charactersAllowed: true,
		              pwcheck: true              
		          },
		          passwordConfirm: {
		        	  equalTo: "#password"
		          }
		      },
		      
		      // Specify the validation error messages
		      messages: {
		          oldPassword: {
		        	 	required: "<fmt:message key='label.password.old.must.entered'/>" 
		          },
		          password: {
		              required: "<fmt:message key='error.password.empty'/>",
		              minlength: "<fmt:message key='label.password.min.length'><fmt:param value='${minNumChars}'/></fmt:message>",
		              maxlength: "<fmt:message key='label.password.max.length'/>",
		              charactersAllowed: "<fmt:message key='label.password.symbols.allowed'/> ` ~ ! @ # $ % ^ & * ( ) _ - + = { } [ ] \ | : ; \" ' < > , . ? /",
		              pwcheck: "<fmt:message key='label.password.restrictions'/>"
		          },
		          passwordConfirm: {
		        	  equalTo: "<fmt:message key='error.newpassword.mismatch'/>"
		          },
		      },
		      
		      submitHandler: function(form) {
		          form.submit();
		      }
		  });
	
		});
	
		
		$(document).ready(function () {
			//update dialog's height and title
			updateMyProfileDialogSettings('<fmt:message key="title.password.change.screen" />', '430');
		});	
	</script>
</lams:head>

<body>
<form name="PasswordChangeActionForm" id="change-password" method="post" action="/lams/passwordChanged.do" autocomplete="off" >
	<div style="clear: both"></div>
	<div class="container">
		<div class="row vertical-center-row">
			<div class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
				<div class="panel voffset20">
					<logic:messagesPresent message="true">
						<p class="warning">
							<html:messages message="true" id="errMsg">
								<bean:write name="errMsg" />
								<br>
							</html:messages>
						</p>
					</logic:messagesPresent>
					<div class="panel-body">
					<input type="hidden" name="redirectURL" value="${param.redirectURL}" />
							<html:hidden name="<%=PasswordChangeActionForm.formName%>"
								property="login" />
							<div class="form-group">
								<label for="oldPassword"><fmt:message key="label.password.old.password" />:</label>
								<input class="form-control" type="password" maxlength="50" placeholder="<fmt:message key="label.password.old.password" />" name="oldPassword" id="oldPassword"/>			
							</div>
							
							<div class="col-xs-12">
								 <lams:Alert type="info"  id="passwordConditions" close="false">
								 <strong><fmt:message key='label.password.must.contain'/>:</strong> 
								 <ul class="list-unstyled" style="line-height: 1.2">
								  	<li><span class="fa fa-check"></span> <fmt:message key='label.password.min.length'><fmt:param value='${minNumChars}'/></fmt:message></li>
								  <c:if test="${mustHaveUppercase}">
										<li><span class="fa fa-check"></span> <fmt:message key='label.password.must.ucase'/></li>
								  </c:if>
								  <c:if test="${mustHaveLowercase}">
						                <li><span class="fa fa-check"></span> <fmt:message key='label.password.must.lcase' /></li>
					              </c:if>
								  <c:if test="${mustHaveNumerics}">
										<li><span class="fa fa-check"></span> <fmt:message key='label.password.must.number'/></li>
								  </c:if>	
								  <c:if test="${mustHaveSymbols}">
										<li><span class="fa fa-check"></span> <fmt:message key='label.password.must.symbol'/></li>
								  </c:if>	
								  </ul>
								 </lams:Alert> 
							</div>
							

							<div class="input-group voffset5">
								<span class="input-group-addon"><i class="fa fa-lock"></i></span>
								<input class="form-control" type="password"  
									placeholder="<fmt:message key='label.password.new.password' />" id="password" name="password" maxlength="25"/> 			
							</div>
							<div class="input-group voffset5">
								<span class="input-group-addon"><i class="fa fa-lock"></i></span>
								<input class="form-control" type="password" id="passwordConfirm" name="passwordConfirm"
									placeholder="<fmt:message key='label.password.confirm.new.password' />" maxlength="25"/>
							</div>
							<div class="form-group" align="right">
							<input type="submit" name="org.apache.struts.taglib.html.CANCEL" value="<fmt:message key="button.cancel"/>" formnovalidate="formnovalidate" onclick="bCancel=true;" id="cancelButton" class="btn btn-sm btn-default voffset5"/>
								&nbsp;&nbsp;
								<html:submit styleClass="btn btn-sm btn-primary voffset5">
									<fmt:message key="button.save" />
								</html:submit>

							</div>
					</div>

				</div>
			</div>
		</div>
	</div>
</form>
</body>
</lams:html>
