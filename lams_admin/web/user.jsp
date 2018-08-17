<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ page import="org.apache.struts.action.ActionMessages"%>

<lams:html>
<lams:head>
	<c:set var="title"><tiles:getAsString name="title"/></c:set>
	<c:set var="title"><fmt:message key="${title}"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>/admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	
	<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
	<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
	<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
	<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
	<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>
	
	<lams:css/>
	
	<script src="/lams/includes/javascript/jquery.js"></script>
	<script src="/lams/includes/javascript/jquery-ui.js"></script>
	<script src="/lams/includes/javascript/jquery.validate.js"></script>
	<script src="/lams/includes/javascript/portrait.js"></script>
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
								debug : true,
								errorClass : 'help-block',
								//  validation rules
								rules : {
									login : {
										required: true,
										maxlength : 50
									},
									password : {
										required: true,
										minlength : <c:out value="${minNumChars}"/>,
										maxlength : 25,
										charactersAllowed : true,
										pwcheck : true
									},
									password2 : {
										equalTo : "#password"
									},
									firstName : {
										required : true
									},
									lastName : {
										required : true
									},
									email : {
										required: true,
										email: true
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
									},
									firstName: {
										required: "<fmt:message key='error.firstname.required'/>"
									},
									lastName: {
										required: "<fmt:message key='error.lastname.required'/>"
									},
									email: {
										required: "<fmt:message key='error.email.required'/>",
										email: "<fmt:message key='error.valid.email.required'/>"
									}
								},
	
								submitHandler : function(form) {
									form.submit();
								}
			});
	
		});
		
		<c:if test="${not empty userForm.userId}">
		$(document).ready(function(){
			var portraitId = '<c:out value="${userForm.initialPortraitId}" />';
			loadPortrait(portraitId);
		});
	
		function loadPortrait(portraitId) {
			$("#portraitPicture").removeClass();
			$("#portraitPicture").css('background-image','');
			addPortrait( $("#portraitPicture"), portraitId, 
					'<c:out value="${userForm.userId}" />', 'large', true, '/lams/' );
			<c:if test="${isSysadmin}">
			if ( portraitId.length > 0 )  {
				$("#portraitButton").css('display','block');
			} else {
				$("#portraitButton").css('display','none');
			}
			</c:if>
		}
		
		<c:if test="${isSysadmin}">
		function deletePortrait() {
			$("#portraitButton").css('display','none');
			$.ajax({
				url : '/lams/saveportrait/deletePortrait.do',
				data : { 	'userId'  : '<c:out value="${userForm.userId}" />' },		
			success : function(response) {
				if ( response == 'deleted') {
					loadPortrait('');
				} else {
					alert("<fmt:message key='error.portrait.removal.failed'/>");
				}
			}});
		}
		</c:if>
		</c:if>
	</script>
</lams:head>
    
<body class="stripes">
	<c:set var="subtitle"><tiles:getAsString name="subtitle" ignore="true"/></c:set>	
	<c:if test="${not empty subtitle}">
		<c:set var="title">${title}: <fmt:message key="${subtitle}"/></c:set>
	</c:if>
	
	<c:set var="help"><tiles:getAsString name='help'  ignore="true"/></c:set>
	<c:choose>
		<c:when test="${not empty help}">
			<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
			<lams:Page type="admin" title="${title}" titleHelpURL="${help}">
				<form:form id="userForm" action="saveUserDetails.do" modelAttribute="userForm" method="post">
				<form:hidden path="userId" />
				<form:hidden path="orgId" />
			
				<c:if test="${not empty userForm.orgId}">
					<a href="orgmanage.do?org=1" class="btn btn-default">
						<fmt:message key="admin.course.manage" />
					</a>
					<c:if test="${not empty pOrgId}">
						: <a href="orgmanage.do?org=<c:out value="${pOrgId}" />" class="btn btn-default">
							<c:out value="${parentName}" />
						  </a>
						: <a href="usermanage.do?org=<c:out value="${userForm.orgId}" />" class="btn btn-default">
							<c:out value="${orgName}" />
						  </a>
					</c:if>
					<c:if test="${empty pOrgId}">
						: <a href="orgmanage.do?org=<c:out value="${userForm.orgId}" />" class="btn btn-default">
							<c:out value="${orgName}" />
						  </a>
					</c:if>
				</c:if>
			
				<c:if test="${empty userForm.orgId}">
					<a href="orgmanage.do?org=1" class="btn btn-default">
						<fmt:message key="admin.course.manage" />
					</a>
				</c:if>
			
				<%-- Error Messages --%>
				<c:set var="errorKey" value="GLOBAL" />
			        <c:if test="${not empty errorMap and not empty errorMap[errorKey]}">
			            <lams:Alert id="error" type="danger" close="false">
			                <c:forEach var="error" items="${errorMap[errorKey]}">
			                    <c:out value="${error}" />
			                </c:forEach>
			            </lams:Alert>
			        </c:if
			
				<div class="panel panel-default voffset5">
					<div class="panel-heading">
						<span class="panel-title"> <c:if test="${not empty userForm.userId}>
								<fmt:message key="admin.user.edit" />
							</c:if> <c:if test="${empty userForm.userId}">
								<fmt:message key="admin.user.create" />
							</c:if>
						</span>
					</div>
			
					<div class="panel-body">
					    <div class="row">
						<div class="col-md-12">
						
							<c:if test="${empty userForm.userId}">
							<lams:Alert type="info" id="passwordConditions" close="false">
							<fmt:message key='label.password.must.contain' />:
								<ul class="list-unstyled" style="line-height: 1.2">
									<li><span class="fa fa-check"></span> <fmt:message
											key='label.password.min.length'>
											<fmt:param value='${minNumChars}' />
										</fmt:message></li>
				
									<c:if test="${mustHaveUppercase}">
										<li><span class="fa fa-check"></span> <fmt:message
												key='label.password.must.ucase' /></li>
									</c:if>
									<c:if test="${mustHaveLowercase}">
												<li><span class="fa fa-check"></span> <fmt:message
														key='label.password.must.lcase' /></li>
											</c:if>
				
									<c:if test="${mustHaveNumerics}">
										<li><span class="fa fa-check"></span> <fmt:message
												key='label.password.must.number' /></li>
									</c:if>
				
				
									<c:if test="${mustHaveSymbols}">
										<li><span class="fa fa-check"></span> <fmt:message
												key='label.password.must.symbol' /></li>
									</c:if>
								</ul>
							</lams:Alert>
						</c:if>
					</div>
					</div>
					
					<!--  Main panel. Do not show portrait area for new user. -->
					<c:if test="${not empty userForm.userId}">
				    <div class="row">
					<div class="col-md-3">
			    			<div class="text-center"><div id="portraitPicture" ></div></div>
						<c:if test="${isSysadmin}">
			    			<div id="portraitButton" class="text-center voffset10" style="display:none; margin-bottom: 5px;">
			    			<a href="#" onclick="javascript:deletePortrait();" class="btn btn-primary btn-sm"><fmt:message key="label.delete.portrait" /></a></div>
			    			</c:if>
			    		</div>
					<div class="col-md-9">
					</c:if>
					<c:if test="${empty userForm.userId}">
				    <div class="row">
					<div class="col-md-12">
					</c:if>
					
						<table class="table table-condensed table-no-border">
							<tr>
								<td class="align-right"><fmt:message key="admin.user.login" />
									*:</td>
								<td><input type="text" id="login" name="login"  maxlength="50"
										class="form-control" /></td>
							</tr>
							<c:if test="${empty userForm.userId}">
							<tr>
								<td class="align-right"><fmt:message key="admin.user.password" />
									*:</td>
								<td><input type="password" name="password" 
										maxlength="25" id="password" class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.password.confirm" /> *:</td>
								<td><input type="password" name="password2" 
										maxlength="25" id="password2" class="form-control" /></td>
							</tr>
							</c:if>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.authentication.method" />:</td>
								<td><form:select path="authenticationMethodId"
										cssClass="form-control">
										<c:forEach items="${authenticationMethods}" var="method">
											<form:option value="${method.authenticationMethodId}">
												<c:out value="${method.authenticationMethodName}" />
											</form:option>
										</c:forEach>
									</form:select></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message key="admin.user.title" />:</td>
								<td><input type="text" name="title" size="32" maxlength="32"
										id="title" class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.first_name" /> *:</td>
								<td><input type="text" name="firstName" 
										id="firstName" maxlength="128" class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.last_name" /> *:</td>
								<td><input type="text" name="lastName" 
										id="lastName" maxlength="128" class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message key="admin.user.email" />
									*:</td>
								<td><input type="text" name="email" maxlength="128"
										class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.address_line_1" />:</td>
								<td><input type="text" name="addressLine1" 
										maxlength="64" class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.address_line_2" />:</td>
								<td><input type="text" name="addressLine2" 
										maxlength="64" class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.address_line_3" />:</td>
								<td><input type="text" name="addressLine3" 
										maxlength="64" class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message key="admin.user.city" />:</td>
								<td><input type="text" name="city"  maxlength="64"
										class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.postcode" />:
								</td>
								<td>
									<input type="text" name="postcode" size="10" maxlength="10" class="form-control" />
								</td>
							</tr>
							<tr>
								<td class="align-right">	
									<fmt:message key="admin.user.state" />:
								</td>
								<td><input type="text" name="state"  maxlength="64"
										class="form-control" />
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.country" />:
								</td>
								<td><input type="text" name="country"  maxlength="64"
										class="form-control" />
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.day_phone" />:
								</td>
								<td>
									<input type="text" name="dayPhone"  maxlength="64" class="form-control" />
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.evening_phone" />:
								</td>
								<td>
									<input type="text" name="eveningPhone" maxlength="64" class="form-control" />		
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.mobile_phone" />:
								</td>
								<td>
									<input type="text" name="mobilePhone" maxlength="64" class="form-control" />
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.fax" />:</td>
								<td>
									<input type="text" name="fax"  maxlength="64" class="form-control" />
								</td>
							</tr>
			
							<tr>
								<td class="align-right">
									<fmt:message key="admin.organisation.locale" />:
								</td>
								<td>
									<form:select path="localeId" cssClass="form-control">
										<c:forEach items="${locales}" var="locale">
											<form:option value="${locale.localeId}">
												<c:out value="${locale.description}" />
											</form:option>
										</c:forEach>
									</form:select>
								</td>
							</tr>
			
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.time.zone" />:
								</td>
								<td>
									<form:select property="timeZone" cssClass="form-control">
										<c:forEach items="${timezoneDtos}" var="timezoneDto">
											<form:option value="${timezoneDto.timeZoneId}">
												${timezoneDto.timeZoneId} - ${timezoneDto.displayName}
											</form:option>
										</c:forEach>
									</form:select>
								</td>
							</tr>
			
							<tr>
								<td class="align-right">
									<fmt:message key="label.theme" />:
								</td>
								<td>
									<form:select path="userTheme" styleClass="form-control">
										<c:forEach items="${themes}" var="theme">
											<form:option value="${theme.themeId}">${theme.name}</form:option>
										</c:forEach>
									</form:select>
								</td>
							</tr>
							
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.change.password" />:
								</td>
								<td>
									<form:checkbox path="changePassword" value="true" id="changePassword"  />
								</td>
							</tr>
							
							<c:if test="${isSysadmin}">
								<tr>
									<td class="align-right">
										<fmt:message key="label.2FA.property.enable" />:
									</td>
									<td>
										<form:checkbox path="twoFactorAuthenticationEnabled" value="true"  />
									</td>
								</tr>
							</c:if>
							
						</table>
					</div>
					</div>
					
					<div class="row">
					<div class="col-md-12">
						<c:if test="${not empty userForm.userId}>
							<div class="pull-left">
							<a href="userChangePass.jsp?userId=<c:out value="${userForm.userId}" />&login=<c:out value="${userForm.login}" />" class="btn btn-primary"><fmt:message key="admin.user.changePassword" /></a>
							</div>
						</c:if>
						<div class="pull-right">
							<input type="submit" name="org.apache.struts.taglib.html.CANCEL" value="<fmt:message key="admin.cancel" />" 
									formnovalidate="formnovalidate" onclick="bCancel=true;" id="cancelButton" class="btn btn-default"/>
							<input type="submit" id="saveButton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
						</div>
					</div>
					</div>
					
					</div>
				</div> <!-- End of panel -->
			
			
				<c:if test="${not empty globalRoles || not empty userOrgRoles}">
			
					<div class="panel panel-default voffset5">
						<div class="panel-heading">
							<span class="panel-title"> <fmt:message
									key="admin.organisation" />
							</span>
						</div>
			
						<table class="table table-no-border">
			
							<c:if test="${not empty globalRoles}">
								<tr>
									<td>
										<table class="table table-striped table-condensed">
											<tr>
												<th><fmt:message key="label.global.roles" />:</th>
											</tr>
											<tr>
												<td>
													<c:forEach var="role" items="${globalRoles.roles}">
														<fmt:message>role.<lams:role role="${role}" />
														</fmt:message>&nbsp;
													</c:forEach>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</c:if>
			
							<c:if test="${not empty userOrgRoles}">
								<tr>
									<td>
										<table id="tableRoles" class="table table-striped table-condensed">
											<tr>
												<th><fmt:message key="label.member.of" />:</th>
												<th><fmt:message key="label.with.roles" />:</th>
											</tr>
											
											<c:forEach var="userOrgRole" items="${userOrgRoles}">
												<tr>
													<td><c:out value="${userOrgRole.orgName}" /></td>
													<td><c:forEach var="role" items="${userOrgRole.roles}">
															<fmt:message>role.<lams:role role="${role}" />
															</fmt:message>&nbsp;
														</c:forEach>
													</td>
												</tr>
												
												<c:if test="${not empty userOrgRole.childDTOs}">
													<c:forEach var="child" items="${userOrgRole.childDTOs}">
														<tr>
															<td><i class="fa fa-square"></i>&nbsp;<c:out
																	value="${child.orgName}" /></td>
															<td><c:forEach var="role" items="${child.roles}">
																	<fmt:message>role.<lams:role role="${role}" />
																	</fmt:message>&nbsp;
																</c:forEach>
															</td>
														</tr>
													</c:forEach>
												</c:if>
											</c:forEach>
											
										</table>
									</td>
								</tr>
							</c:if>
			
						</table>
					</div>
				</c:if>
			
			</form:form>

			</lams:Page>
		</c:when>
		<c:otherwise>
			<lams:Page type="admin" title="${title}" >
				
			<form:form id="userForm" action="saveUserDetails.do" modelAttribute="userForm" method="post">
				<form:hidden path="userId" />
				<form:hidden path="orgId" />
			
				<c:if test="${not empty userForm.orgId}">
					<a href="orgmanage.do?org=1" class="btn btn-default">
						<fmt:message key="admin.course.manage" />
					</a>
					<c:if test="${not empty pOrgId}">
						: <a href="orgmanage.do?org=<c:out value="${pOrgId}" />" class="btn btn-default">
							<c:out value="${parentName}" />
						  </a>
						: <a href="usermanage.do?org=<c:out value="${userForm.orgId}" />" class="btn btn-default">
							<c:out value="${orgName}" />
						  </a>
					</c:if>
					<c:if test="${empty pOrgId}">
						: <a href="orgmanage.do?org=<c:out value="${userForm.orgId}" />" class="btn btn-default">
							<c:out value="${orgName}" />
						  </a>
					</c:if>
				</c:if>
			
				<c:if test="${empty userForm.orgId}">
					<a href="orgmanage.do?org=1" class="btn btn-default">
						<fmt:message key="admin.course.manage" />
					</a>
				</c:if>
			
				<%-- Error Messages --%>
				<c:set var="errorKey" value="GLOBAL" />
			        <c:if test="${not empty errorMap and not empty errorMap[errorKey]}">
			            <lams:Alert id="error" type="danger" close="false">
			                <c:forEach var="error" items="${errorMap[errorKey]}">
			                    <c:out value="${error}" />
			                </c:forEach>
			            </lams:Alert>
			        </c:if
			
				<div class="panel panel-default voffset5">
					<div class="panel-heading">
						<span class="panel-title"> <c:if test="${not empty userForm.userId}>
								<fmt:message key="admin.user.edit" />
							</c:if> <c:if test="${empty userForm.userId}">
								<fmt:message key="admin.user.create" />
							</c:if>
						</span>
					</div>
			
					<div class="panel-body">
					    <div class="row">
						<div class="col-md-12">
						
							<c:if test="${empty userForm.userId}">
							<lams:Alert type="info" id="passwordConditions" close="false">
							<fmt:message key='label.password.must.contain' />:
								<ul class="list-unstyled" style="line-height: 1.2">
									<li><span class="fa fa-check"></span> <fmt:message
											key='label.password.min.length'>
											<fmt:param value='${minNumChars}' />
										</fmt:message></li>
				
									<c:if test="${mustHaveUppercase}">
										<li><span class="fa fa-check"></span> <fmt:message
												key='label.password.must.ucase' /></li>
									</c:if>
									<c:if test="${mustHaveLowercase}">
												<li><span class="fa fa-check"></span> <fmt:message
														key='label.password.must.lcase' /></li>
											</c:if>
				
									<c:if test="${mustHaveNumerics}">
										<li><span class="fa fa-check"></span> <fmt:message
												key='label.password.must.number' /></li>
									</c:if>
				
				
									<c:if test="${mustHaveSymbols}">
										<li><span class="fa fa-check"></span> <fmt:message
												key='label.password.must.symbol' /></li>
									</c:if>
								</ul>
							</lams:Alert>
						</c:if>
					</div>
					</div>
					
					<!--  Main panel. Do not show portrait area for new user. -->
					<c:if test="${not empty userForm.userId}">
				    <div class="row">
					<div class="col-md-3">
			    			<div class="text-center"><div id="portraitPicture" ></div></div>
						<c:if test="${isSysadmin}">
			    			<div id="portraitButton" class="text-center voffset10" style="display:none; margin-bottom: 5px;">
			    			<a href="#" onclick="javascript:deletePortrait();" class="btn btn-primary btn-sm"><fmt:message key="label.delete.portrait" /></a></div>
			    			</c:if>
			    		</div>
					<div class="col-md-9">
					</c:if>
					<c:if test="${empty userForm.userId}">
				    <div class="row">
					<div class="col-md-12">
					</c:if>
					
						<table class="table table-condensed table-no-border">
							<tr>
								<td class="align-right"><fmt:message key="admin.user.login" />
									*:</td>
								<td><input type="text" id="login" name="login"  maxlength="50"
										class="form-control" /></td>
							</tr>
							<c:if test="${empty userForm.userId}">
							<tr>
								<td class="align-right"><fmt:message key="admin.user.password" />
									*:</td>
								<td><input type="password" name="password" 
										maxlength="25" id="password" class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.password.confirm" /> *:</td>
								<td><input type="password" name="password2" 
										maxlength="25" id="password2" class="form-control" /></td>
							</tr>
							</c:if>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.authentication.method" />:</td>
								<td><form:select path="authenticationMethodId"
										cssClass="form-control">
										<c:forEach items="${authenticationMethods}" var="method">
											<form:option value="${method.authenticationMethodId}">
												<c:out value="${method.authenticationMethodName}" />
											</form:option>
										</c:forEach>
									</form:select></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message key="admin.user.title" />:</td>
								<td><input type="text" name="title" size="32" maxlength="32"
										id="title" class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.first_name" /> *:</td>
								<td><input type="text" name="firstName" 
										id="firstName" maxlength="128" class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.last_name" /> *:</td>
								<td><input type="text" name="lastName" 
										id="lastName" maxlength="128" class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message key="admin.user.email" />
									*:</td>
								<td><input type="text" name="email" maxlength="128"
										class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.address_line_1" />:</td>
								<td><input type="text" name="addressLine1" 
										maxlength="64" class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.address_line_2" />:</td>
								<td><input type="text" name="addressLine2" 
										maxlength="64" class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.address_line_3" />:</td>
								<td><input type="text" name="addressLine3" 
										maxlength="64" class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message key="admin.user.city" />:</td>
								<td><input type="text" name="city"  maxlength="64"
										class="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.postcode" />:
								</td>
								<td>
									<input type="text" name="postcode" size="10" maxlength="10" class="form-control" />
								</td>
							</tr>
							<tr>
								<td class="align-right">	
									<fmt:message key="admin.user.state" />:
								</td>
								<td><input type="text" name="state"  maxlength="64"
										class="form-control" />
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.country" />:
								</td>
								<td><input type="text" name="country"  maxlength="64"
										class="form-control" />
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.day_phone" />:
								</td>
								<td>
									<input type="text" name="dayPhone"  maxlength="64" class="form-control" />
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.evening_phone" />:
								</td>
								<td>
									<input type="text" name="eveningPhone" maxlength="64" class="form-control" />		
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.mobile_phone" />:
								</td>
								<td>
									<input type="text" name="mobilePhone" maxlength="64" class="form-control" />
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.fax" />:</td>
								<td>
									<input type="text" name="fax"  maxlength="64" class="form-control" />
								</td>
							</tr>
			
							<tr>
								<td class="align-right">
									<fmt:message key="admin.organisation.locale" />:
								</td>
								<td>
									<form:select path="localeId" cssClass="form-control">
										<c:forEach items="${locales}" var="locale">
											<form:option value="${locale.localeId}">
												<c:out value="${locale.description}" />
											</form:option>
										</c:forEach>
									</form:select>
								</td>
							</tr>
			
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.time.zone" />:
								</td>
								<td>
									<form:select property="timeZone" cssClass="form-control">
										<c:forEach items="${timezoneDtos}" var="timezoneDto">
											<form:option value="${timezoneDto.timeZoneId}">
												${timezoneDto.timeZoneId} - ${timezoneDto.displayName}
											</form:option>
										</c:forEach>
									</form:select>
								</td>
							</tr>
			
							<tr>
								<td class="align-right">
									<fmt:message key="label.theme" />:
								</td>
								<td>
									<form:select path="userTheme" styleClass="form-control">
										<c:forEach items="${themes}" var="theme">
											<form:option value="${theme.themeId}">${theme.name}</form:option>
										</c:forEach>
									</form:select>
								</td>
							</tr>
							
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.change.password" />:
								</td>
								<td>
									<form:checkbox path="changePassword" value="true" id="changePassword"  />
								</td>
							</tr>
							
							<c:if test="${isSysadmin}">
								<tr>
									<td class="align-right">
										<fmt:message key="label.2FA.property.enable" />:
									</td>
									<td>
										<form:checkbox path="twoFactorAuthenticationEnabled" value="true"  />
									</td>
								</tr>
							</c:if>
							
						</table>
					</div>
					</div>
					
					<div class="row">
					<div class="col-md-12">
						<c:if test="${not empty userForm.userId}>
							<div class="pull-left">
							<a href="userChangePass.jsp?userId=<c:out value="${userForm.userId}" />&login=<c:out value="${userForm.login}" />" class="btn btn-primary"><fmt:message key="admin.user.changePassword" /></a>
							</div>
						</c:if>
						<div class="pull-right">
							<input type="submit" name="org.apache.struts.taglib.html.CANCEL" value="<fmt:message key="admin.cancel" />" 
									formnovalidate="formnovalidate" onclick="bCancel=true;" id="cancelButton" class="btn btn-default"/>
							<input type="submit" id="saveButton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
						</div>
					</div>
					</div>
					
					</div>
				</div> <!-- End of panel -->
			
			
				<c:if test="${not empty globalRoles || not empty userOrgRoles}">
			
					<div class="panel panel-default voffset5">
						<div class="panel-heading">
							<span class="panel-title"> <fmt:message
									key="admin.organisation" />
							</span>
						</div>
			
						<table class="table table-no-border">
			
							<c:if test="${not empty globalRoles}">
								<tr>
									<td>
										<table class="table table-striped table-condensed">
											<tr>
												<th><fmt:message key="label.global.roles" />:</th>
											</tr>
											<tr>
												<td>
													<c:forEach var="role" items="${globalRoles.roles}">
														<fmt:message>role.<lams:role role="${role}" />
														</fmt:message>&nbsp;
													</c:forEach>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</c:if>
			
							<c:if test="${not empty userOrgRoles}">
								<tr>
									<td>
										<table id="tableRoles" class="table table-striped table-condensed">
											<tr>
												<th><fmt:message key="label.member.of" />:</th>
												<th><fmt:message key="label.with.roles" />:</th>
											</tr>
											
											<c:forEach var="userOrgRole" items="${userOrgRoles}">
												<tr>
													<td><c:out value="${userOrgRole.orgName}" /></td>
													<td><c:forEach var="role" items="${userOrgRole.roles}">
															<fmt:message>role.<lams:role role="${role}" />
															</fmt:message>&nbsp;
														</c:forEach>
													</td>
												</tr>
												
												<c:if test="${not empty userOrgRole.childDTOs}">
													<c:forEach var="child" items="${userOrgRole.childDTOs}">
														<tr>
															<td><i class="fa fa-square"></i>&nbsp;<c:out
																	value="${child.orgName}" /></td>
															<td><c:forEach var="role" items="${child.roles}">
																	<fmt:message>role.<lams:role role="${role}" />
																	</fmt:message>&nbsp;
																</c:forEach>
															</td>
														</tr>
													</c:forEach>
												</c:if>
											</c:forEach>
											
										</table>
									</td>
								</tr>
							</c:if>
			
						</table>
					</div>
				</c:if>
			
			</form:form>

			</lams:Page>
		</c:otherwise>
	</c:choose>


</body>
</lams:html>



