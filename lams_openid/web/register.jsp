<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="org.apache.axis.client.Call" %>
<%@ page import="org.apache.axis.client.Service" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.lamsfoundation.lams.util.HashUtil" %>
<%@ page import="org.lamsfoundation.lams.util.Emailer" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>


<!-- 
	Author: Luke Foxton
	
	This jsp file handles the entire SOAP registration process in LAMS. It is
	included in the openID project as an alternatice means of registration.
	Make sure you edit the configurations at the top appropriately before using
	this jsp file.

 -->

<% 
	//final String LAMS_SERVER_URL = "http://demo.lamscommunity.org/lams";
	final String LAMS_SERVER_NAME = "LAMS 2 Demo Server";
	final String LAMS_SERVER_URL = "http://172.20.100.188:8080/lams";
	final String LAMS_SERVICE = LAMS_SERVER_URL + "/services/RegisterService?wsdl";
	final String LAMS_SERVER_ID ="demolamsserver";
	final String LAMS_SERVER_KEY ="ixybitzy";
	final boolean ADD_TO_GROUP =true;
	final boolean GROUP_ADD_AS_TEACHER = true;
	final boolean ADD_TO_GROUP_LESSONS = true;
	final boolean ADD_TO_GROUP_LESSONS_AS_STAFF = false;
	final boolean ADD_TO_SUBGROUP = true;
	final boolean SUBGROUP_ADD_AS_TEACHER = false;
	final boolean ADD_TO_SUBGROUP_LESSONS = true;
	final boolean ADD_TO_SUBGROUP_LESSONS_AS_STAFF = false;
	final boolean EMAIL_USER = true;
	final String GROUP_ID = "Playpen";
	final String GROUP_NAME = "Playpen";
	final String GROUP_COUNTRY_ISO_CODE = "AU";
	final String GROUP_LANG_ISO_CODE = "en";
	
	String userName = request.getParameter("username");
	Boolean registered = new Boolean(false);
	Boolean registerAttempt = new Boolean(false);
	Boolean addedToGroup = new Boolean(false);
	Boolean addedToGroupLessons = new Boolean(false);
	Boolean addedToSubGroup = new Boolean(false);
	Boolean addedToSubGroupLessons = new Boolean(false);
	ArrayList errors = new ArrayList();

	if (userName != null && !userName.equals(""))
	{
	    registerAttempt = Boolean.TRUE;
	    String passwordOrig = request.getParameter("password");
	    String password = HashUtil.sha1(passwordOrig);
		String firstName = request.getParameter("firstname");
		String lastName = request.getParameter("lastname");
		String email = request.getParameter("email");
		String timestamp = "" + new Date().getTime();
		String plaintext = timestamp + LAMS_SERVER_ID + LAMS_SERVER_KEY;
		String hash = HashUtil.sha1(plaintext.toLowerCase());

	    try
	    {	    
		    Service  service = new Service();
		    Call call = (Call) service.createCall();
		    call.setTargetEndpointAddress( new java.net.URL(LAMS_SERVICE) );
			
		    // register the user
		    String[] registerParams = new String[] {userName, password, firstName, lastName, email, LAMS_SERVER_ID, timestamp, hash};
		    registered = (Boolean)call.invoke("createUser", registerParams);
		    
		    // add user to a group
		    if(ADD_TO_GROUP)
		    {
				Object[] groupParams = new Object[] {userName, LAMS_SERVER_ID, timestamp, hash, GROUP_ID, GROUP_NAME, GROUP_COUNTRY_ISO_CODE, GROUP_LANG_ISO_CODE, new Boolean(GROUP_ADD_AS_TEACHER)};
				addedToGroup = (Boolean)call.invoke("addUserToGroup", groupParams);
				System.out.println("GROUP ADDED: " + addedToGroup.toString());
		   
				if (addedToGroup == Boolean.TRUE)
				{
				    // email registered user if enabled
				    if (EMAIL_USER && Configuration.get(ConfigurationKeys.SMTP_SERVER) != null 
					    && !Configuration.get(ConfigurationKeys.SMTP_SERVER).equals(""))
					{
					    //public static void sendFromSupportEmail(String subject, String to, String body)
					    String message = "Congratulations " + firstName + " " + lastName + "! You have been registered to " +  LAMS_SERVER_NAME;
					    message += "\n\nYour details are:";
					    message += "\nUsername: " + userName;
					    message += "\nPassword: " + passwordOrig;
					    message += "\nFirst name: " + firstName;
					    message += "\nLast name: " + lastName;
					    message += "\nEmail: " + email;
					    message += "\n\nYou can now log at " + LAMS_SERVER_URL + " using this username.";
					    Emailer.sendFromSupportEmail("Registered at " + LAMS_SERVER_NAME, email, message);
					}

				    // add user to lessons group
					if(ADD_TO_GROUP_LESSONS)
					{
					    Object[] groupLessonsParams = new Object[] {userName, LAMS_SERVER_ID, timestamp, hash, GROUP_ID, GROUP_NAME, GROUP_COUNTRY_ISO_CODE, GROUP_LANG_ISO_CODE, new Boolean(ADD_TO_GROUP_LESSONS_AS_STAFF)};
						addedToGroupLessons = (Boolean)call.invoke("addUserToGroupLessons", groupLessonsParams);
						System.out.println("GROUP LESSONS ADDED: " + addedToGroupLessons.toString());
						
						if (addedToGroupLessons == Boolean.FALSE)
						{
						    errors.add("Your account could not be added to the group's lessons.");
						}
					}
					
					// add user to subgroup
					if (ADD_TO_SUBGROUP)
					{
					    // $parameters = array($username,LAMS_SERVER_ID,$datetime,$hash,$courseId,$courseName,$countryIsoCode,$langIsoCode,$subgroupId,$subgroupName,SUBGROUP_ADD_AS_TEACHER);
					    Object[] subGroupParams = new Object[] {userName, LAMS_SERVER_ID, timestamp, hash, GROUP_ID, GROUP_NAME, GROUP_COUNTRY_ISO_CODE, GROUP_LANG_ISO_CODE, GROUP_NAME + " subgroup", GROUP_NAME + " subgroup", new Boolean(SUBGROUP_ADD_AS_TEACHER)};
					    addedToSubGroup = (Boolean)call.invoke("addUserToSubgroup", subGroupParams);
						System.out.println("SUBGROUP ADDED: " + addedToSubGroup.toString());
						
						// add user to subgroup lessons
						if (addedToSubGroup == Boolean.TRUE)
						{
							if (ADD_TO_SUBGROUP_LESSONS) { 
							   	// $parameters = array($username,LAMS_SERVER_ID,$datetime,$hash,$courseId,$courseName,$countryIsoCode,$langIsoCode,$subgroupId,$subgroupName,ADD_TO_SUBGROUP_LESSONS_AS_STAFF);
								Object[] subGroupLessonsParams = new Object[] {userName, LAMS_SERVER_ID, timestamp, hash, GROUP_ID, GROUP_NAME, GROUP_COUNTRY_ISO_CODE, GROUP_LANG_ISO_CODE, GROUP_NAME + " subgroup", GROUP_NAME + " subgroup", new Boolean(ADD_TO_SUBGROUP_LESSONS_AS_STAFF)};
							    addedToSubGroupLessons = (Boolean)call.invoke("addUserToSubgroupLessons", subGroupLessonsParams);
								System.out.println("SUBGROUP LESSONS ADDED: " + addedToSubGroupLessons.toString()); 
							
								if (addedToSubGroupLessons == Boolean.FALSE)
								{
								    errors.add("Your account could not be added to your tutorial's lessons.");
								}
							}
						}
						else
						{
							errors.add("Your account could not be added to your tutorial group.");
						}
					}
				}
				else{
					errors.add("Your account could not be added to the group.");
				}
		    }
		} catch (Exception e){
		    e.printStackTrace();
		}
	}
%>
 


<html lang="en_au">
	<head>
		<title>Register - LAMS :: Learning Activity Management System</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<link href="<%=LAMS_SERVER_URL %>/css/defaultHTML.css" rel="stylesheet" type="text/css" />
		<link rel="icon" href="<%=LAMS_SERVER_URL %>/favicon.ico" type="image/x-icon" />
		<link rel="shortcut icon" href="<%=LAMS_SERVER_URL %>/favicon.ico" type="image/x-icon" />
		
		
		<script src="<%=LAMS_SERVER_URL %>/includes/javascript/jquery-latest.pack.js" type="text/javascript"></script>
		<script src="<%=LAMS_SERVER_URL %>/includes/javascript/jquery.validate.pack.js" type="text/javascript"></script>
		
		<script type="text/javascript">
		<!--
			
			$().ready(function() {
				// validate signup form on keyup and submit
				$("#details").validate({
					rules: {
						username: "required",
						password: "required",
						password2: {
							required: true,
							equalTo: "#password"
						},
						firstname: "required",
						lastname: "required",
						email: {
							required: true,
							email: true
						}
					},
					messages: {
						username: "<font color='red'><i> Username is required</i></font>",
						password: "<font color='red'><i> Please provide a password</i></font>",
						password2: {
							required: "<font color='red'><i> Please provide a password</i></font>",
							equalTo: "<font color='red'><i> Passwords do not match</i></font>"
						},
						firstname: "<font color='red'><i> First Name is required</i></font>",
						lastname: "<font color='red'><i> Last Name is required</i></font>",
						email: "<font color='red'><i> Please enter a valid email address</i></font>"
					}
				});
			});
		//-->
		</script>
	</head>
	<body class = stripes>
		<div id="login-page"><!--main box 'page'-->
			<h1 class="no-tabs-below">&nbsp;</h1>
		
			<div id="login-header">
			</div><!--closes header-->
	
			<div id="login-content">	
				<div style="padding-left:40px">	
					<h1><img src="<%=LAMS_SERVER_URL %>/images/css/lams_login.gif" alt="LAMS - Learning Activity Management System" width="186" height="90" /></h1>
					<br />	
					<%
					if(registered == Boolean.TRUE){ 
					%>
						<p class="info">
							Congratulations <%=userName %>, you have been successfully registered!
							<br />
							Click <a href="<%=LAMS_SERVER_URL %>">here</a> to continue to the login page.
						</p>
						<br />
						
						<%
						if(errors.size() > 0){ 
						%>
							<p class="warning">
								You have been registered, but there has been a few errors adding you to groups, please go to http://lamscommunity.org for support.
								<br />
								<br />
								Errors:
									<%
										for(int i=0; i<errors.size(); i++)
										{
											out.println("<li>" + errors.get(i) + "</li>");
										}
									%>
						<%
						}
						%>
					<%
					}else{
					    if (registerAttempt == Boolean.TRUE){
					%>
						<p class="warning">
							Registration failed, the user credentials may already exist. Please try again with a different username.
						</p>
					<%} %>
				
					<h1>Sign up to the <%=LAMS_SERVER_NAME %></h1>
					<br />
					
					<p>
						Just by completing this form, you can register and test LAMS as Teacher, Monitor and Learner.&nbsp;&nbsp;
						You'll be assigned to a Course where you can create sequences, assign them to your students, monitor their progress 
						and participate in them as well.&nbsp;&nbsp;New to LAMS? No worries, have a look at 
						<a href="http://wiki.lamsfoundation.org/display/lamsdocs/LAMS+2.0+Tutorials" target="_blank">these animations</a>.
					</p>
					<p>
						If you already have an account, please proceed to the <a href="http://demo.lamscommunity.org/lams/">login page</a>.
					</p>
					
					<form name="details" action="<%=LAMS_SERVER_URL %>/index2.jsp" method="post" id="details">
					<input type="hidden" name="submitted" value="true" />
						<table border="0" cellspacing="1" cellpadding="1" align="left">
						
							<tr>
								<td align="right" width="40%">Username *:</td>
								<td align="left" width="60%"><input name="username" id="username" type="text" maxlength="20"></td>
							</tr>
							<tr>
								<td align="right">Password *:</td>
								<td align="left"><input name="password" id="password" type="password" /></td>
						    </tr>
							<tr>
								<td align="right">Confirm Password *:</td>
								<td align="left"><input name="password2" id="password2" type="password" /></td>
						    </tr>
							<tr>
								<td align="right">First name *:</td>
								<td align="left"><input name="firstname" id="firstname" type="text" maxlength="128" /></td>
						    </tr>
							<tr>
								<td align="right">Last name *:</td>
								<td align="left"><input name="lastname" id="lastname" type="text" maxlength="128" /></td>
						    </tr>
							<tr>
						       	<td align="right">Email *:</td>
								<td align="left"><input name="email" type="text" id="email" maxlength="128"/></td>
						    </tr>
							<tr>
								<td colspan="2" align="center"><input type="submit" value="submit"/></td>
							</tr>
						</table>
					</form>

					<br />
					
				
					<%} %>
				</div>
				<div class="clear"></div>
			</div>  <!--closes login-content-->
	
			<div id="footer">
				<p>&copy; 2002-2007 LAMS Foundation.</p>
	  		</div><!--closes footer-->
		
		</div><!--closes page-->
	</body>
</html>