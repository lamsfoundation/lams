<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.changePassword"/></c:set>
	
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
    <link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">

	<%-- javascript --%>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
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
		return /^[A-Za-z0-9\d`~!@#$%^&*\(\)_\-+={}\[\]\\|:\;\"\'\<\>,.?\/]*$/
				.test(value)
	});

	$(function() {
		// Setup form validation 
		$("#userForm").validate({
							validClass: "is-valid",
		  					errorClass: 'is-invalid',
							//  validation rules
							rules : {
								password : {
									required: true,
									minlength : <c:out value="${minNumChars}"/>,
									maxlength : 50,
									charactersAllowed : true,
									pwcheck : true
									 
								},
								password2 : {
									equalTo : "#password"
								}
							},

							// Specify the validation error messages
							messages : {
								login : {
									required: "<fmt:message key='error.login.required'/>"
								},
								password : {
									required : "<fmt:message key='error.password.empty'/>",
									minlength : "<fmt:message key='label.password.min.length'><fmt:param value='${minNumChars}'/></fmt:message>",
									maxlength : "<fmt:message key='label.password.max.length'/>",
									charactersAllowed : "<fmt:message key='label.password.symbols.allowed'/> ` ~ ! @ # $ % ^ & * ( ) _ - + = { } [ ] \ | : ; \" ' < > , . ? /",
									pwcheck : "<fmt:message key='label.password.restrictions'/>"
								},
								password2: {
									equalTo : "<fmt:message key='error.password.mismatch'/>"
								}
							},

							submitHandler : function(form) {
								form.submit();
							}
		});
	});
	</script>
</lams:head>

<body class="component pb-4 pt-2 px-2 px-sm-4">
	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/orgmanage.do?org=1 | <fmt:message key="admin.course.manage" /></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/usersearch.do | <fmt:message key="admin.user.management"/></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/user/edit.do?userId=${param.userId} | <fmt:message key="admin.user.edit"/></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message key="admin.user.changePassword" /></c:set>	

	<lams:Page5 type="admin" title="${title}" formID="userForm" breadcrumbItems="${breadcrumbItems}">	
		<lams:Alert5 type="info" id="passwordConditions" close="false">
			<fmt:message key='label.password.must.contain' />:
			<ul class="list-unstyled ml-2" style="line-height: 1.2">
				<li><span class="fa fa-check"  aria-hidden="true"></span> <fmt:message
										key='label.password.min.length'>
										<fmt:param value='${minNumChars}' />
									</fmt:message>
				</li>

								<c:if test="${mustHaveUppercase}">
				<li><span class="fa fa-check" aria-hidden="true"></span> <fmt:message
											key='label.password.must.ucase' /></li>
								</c:if>
								<c:if test="${mustHaveLowercase}">
						<li><span class="fa fa-check aria-hidden="true""></span> <fmt:message
											key='label.password.must.lcase' /></li>
								</c:if>

								<c:if test="${mustHaveNumerics}">
				<li><span class="fa fa-check" aria-hidden="true"></span> <fmt:message
											key='label.password.must.number' /></li>
								</c:if>


								<c:if test="${mustHaveSymbols}">
				<li><span class="fa fa-check" aria-hidden="true"></span> <fmt:message
											key='label.password.must.symbol' /></li>
								</c:if>
								
								<li><span class="fa fa-check"></span>
									<fmt:message key='label.password.user.details' />
								</li>
								<li><span class="fa fa-check"></span>
									<fmt:message key='label.password.common' />
								</li>
			</ul>
		</lams:Alert5>
	
		<div class="col-12 col-md-8 offset-md-2 col-lg-6 offset-lg-3">
			<form id="userForm" modelAttribute="userForm" action="usersave/changePass.do" method="post">
				<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
				<input type="hidden" name="userId" value='<c:out value="${param.userId}" />' />
							
				<lams:errors5 path="password"/>
						
				<div>
					<h2><fmt:message key="admin.user.login" />:
					<span id="login"><c:out value="${param.login}" escapeXml="true"/></span></h2>
				</div>
				<div class="mb-3">
					<label for="password"><fmt:message
							key="admin.user.password" />:</label> 
					<input type="password" name="password" maxlength="50" id="password" minlength="${minNumChars}"
						class="form-control  form-control-sm" required >
				</div>
				<div class="mb-3">
					<label for="password2"><fmt:message
							key="admin.user.password.confirm" />:</label> 
					<input type="password" name="password2" maxlength="50"  minlength="${minNumChars}" id="password2" class="form-control form-control-sm" required>
				</div>
				<div class="float-end">
					<a href="javascript:history.back();" class="btn btn-secondary">
						<fmt:message key="admin.cancel" />
					</a> 
					<input type="submit" id="saveButton" class="btn btn-primary" value="<fmt:message key="admin.save" />" />
				</div>
			</form>		
		</div>
	</lams:Page5>	
</body>
</lams:html>
