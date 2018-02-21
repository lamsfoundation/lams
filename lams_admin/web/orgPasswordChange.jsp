<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<%@ page import="org.apache.struts.action.ActionMessages"%>
<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>

<lams:css />
<style type="text/css">
	.changeContainer .checkbox {
		display: inline-block;
	}
	.changeContainer .pass {
		display: inline-block;
		margin-left: 20px;
		width: 260px;
	}
	.changeContainer .fa {
		cursor: pointer;
	}
	h3 {
		text-align: center;
	}
</style>
<%-- javascript --%>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
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
		$('#isStaffChange, #isLearnerChange').change(function(){
			$(this).closest('.changeContainer').find('.pass').prop('disabled', !$(this).prop('checked'));
		});

		$('.generatePassword').click(function(){
			var container = $(this).closest('.changeContainer');
			if (!container.find('input[type="checkbox"]').prop('checked')) {
				return;
			}
			$.ajax({
				'url' : 'orgPasswordChange.do',
				'data': {
					'method' : 'generatePassword'
				}
			}).done(function(password){
				container.find('.pass').val(password);
			});
		});
		
		
		
		// Setup form validation 
		$("#OrgPasswordChangeForm").validate({
			debug : true,
			errorClass : 'help-block',
			//  validation rules
			rules : {
				learnerPassword : {
					required: false,
					minlength : <c:out value="${minNumChars}"/>,
					maxlength : 25,
					charactersAllowed : true,
					pwcheck : true
				},
				staffPassword: {
					required: false,
					minlength : <c:out value="${minNumChars}"/>,
					maxlength : 25,
					charactersAllowed : true,
					pwcheck : true
				}
				
			},

			// Specify the validation error messages
			messages : {
				learnerPassword : {
					required : "<fmt:message key='error.password.empty'/>",
					minlength : "<fmt:message key='label.password.min.length'><fmt:param value='${minNumChars}'/></fmt:message>",
					maxlength : "<fmt:message key='label.password.max.length'/>",
					charactersAllowed : "<fmt:message key='label.password.symbols.allowed'/> ` ~ ! @ # $ % ^ & * ( ) _ - + = { } [ ] \ | : ; \" ' < > , . ? /",
					pwcheck : "<fmt:message key='label.password.restrictions'/>"
				},
				staffPassword: {
					required : "<fmt:message key='error.password.empty'/>",
					minlength : "<fmt:message key='label.password.min.length'><fmt:param value='${minNumChars}'/></fmt:message>",
					maxlength : "<fmt:message key='label.password.max.length'/>",
					charactersAllowed : "<fmt:message key='label.password.symbols.allowed'/> ` ~ ! @ # $ % ^ & * ( ) _ - + = { } [ ] \ | : ; \" ' < > , . ? /",
					pwcheck : "<fmt:message key='label.password.restrictions'/>"
				}
				
			},

			submitHandler : function(form) {
				form.submit();
			}
		});

	});
</script>

<p>
	<a href="<lams:LAMSURL/>admin/usermanage.do?org=<bean:write name='OrgPasswordChangeForm' property='organisationID' />" class="btn btn-default">
	<fmt:message key="admin.user.manage" /></a>
</p>

<form id="OrgPasswordChangeForm" action="orgPasswordChange.do" method="post">
	<div class="panel panel-default panel-body">
		<h3><bean:write name='OrgPasswordChangeForm' property='orgName' /></h3>
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
		<logic:messagesPresent>
			<lams:Alert type="danger" id="form-error" close="false">
				<html:messages id="error">
					<c:out value="${error}" escapeXml="false" />
					<br />
				</html:messages>
			</lams:Alert>
		</logic:messagesPresent>
		
		<div class="changeContainer form-group">
			<table>
				<tr class="changeContainer">
					<td class="checkbox">
						<label>
							<input id="isStaffChange" name="isStaffChange" value="true" type="checkbox" checked="checked"/>
							<fmt:message key="admin.org.password.change.is.staff" />
						</label>
					</td>
					<td>
						<input type="text" id="staffPass" name="staffPass" class="pass form-control" maxlength="25"
						       placeholder="<fmt:message key='admin.org.password.change.custom' />"
						       title="<fmt:message key='admin.org.password.change.custom' />"
							   value="<bean:write name='OrgPasswordChangeForm' property='staffPass' />" />
						<i class="fa fa-refresh generatePassword" title="<fmt:message key='admin.org.password.change.generate' />"></i>
					</td>
				</tr>
				<tr class="changeContainer">
					<td class="checkbox">
						<label>
							<input id="isLearnerChange" name="isLearnerChange" value="true" type="checkbox" checked="checked"/>
							<fmt:message key="admin.org.password.change.is.learners" />
						</label>
					</td>
					<td>
						<input type="text" id="learnerPass" name="learnerPass" class="pass form-control" maxlength="25"
							   placeholder="<fmt:message key='admin.org.password.change.custom' />"
						       title="<fmt:message key='admin.org.password.change.custom' />"
							   value="<bean:write name='OrgPasswordChangeForm' property='learnerPass' />" />
						<i class="fa fa-refresh generatePassword" title="<fmt:message key='admin.org.password.change.generate' />"></i>
					</td>
				</tr>
			</table>
		</div>
		<div class="form-group">
			<div class="checkbox">
				<label>
					<input name="email" value="true" type="checkbox"/><fmt:message key="admin.org.password.change.email" />
				</label>
			</div>
			<div class="checkbox">
				<label>
					<input name="force" value="true" type="checkbox"/><fmt:message key="admin.org.password.change.force" />
				</label>
			</div>
		</div>
	</div>
</form>