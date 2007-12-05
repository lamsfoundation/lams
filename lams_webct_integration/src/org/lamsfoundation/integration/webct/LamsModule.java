package org.lamsfoundation.integration.webct;

import java.io.StringWriter;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import java.util.Calendar;
import java.sql.Timestamp;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;



import com.webct.platform.sdk.security.authentication.module.AuthenticationModule;
import com.webct.platform.sdkext.authmoduledata.*;


import org.apache.velocity.VelocityContext; 
import org.apache.velocity.Template; 
import org.apache.velocity.app.VelocityEngine; 
import org.apache.log4j.Logger;

import org.lamsfoundation.integration.webct.LamsSecurityUtil;
import org.lamsfoundation.integration.dao.ILamsLessonDao;
import org.lamsfoundation.integration.dao.LamsLessonDaoMySqlJDBC;

import org.lamsfoundation.integration.util.Constants;


/**
 * @author Luke Foxton
 *
 */
public class LamsModule extends AuthenticationModule
{	
    
	public static final String VERSION = "1.0.0";
	public static final String JARSTR = "lams2-webct-integration-" + VERSION + ".jar";
    private HttpServletRequest request = null;
    private Map settings = null;
    private String lamsServerUrl;
    private String lamsServerId;
    private String lamsServerSecretKey;
    private String webctRequestSource;
    private Long lcID;
    UserVO user;

    private static final Logger log = Logger.getLogger(LamsModule.class);
    
    
    public LamsModule()
    {
        super();	
    }
    
    public LamsModule(Hashtable hashtable)
    {
        super(hashtable);	
    }
    
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options)
    {
        super.initialize(subject, callbackHandler, sharedState, options);
    }
    
    public boolean validate() 
    {
    	return true;    
    }
    
    public boolean login() throws LoginException
    {
    	return true;
    }
    	
    public boolean abort() throws LoginException
    {
    	return super.abort();
    }
    
    public boolean commit() throws LoginException
    {        
    	if(!super.getCurrentMode().equals(super.OUTGOING_MODE))
    	{
    		// shouldn't be here
    		return false;
    	}
    	

    	
    	this.settings = super.getSettings();
    	//user = super.getUserId();
		lamsServerUrl = (String)settings.get(Constants.SETTING_LAMS_SERVER_URL);
        lamsServerId = (String)settings.get(Constants.SETTING_SERVER_ID);
        lamsServerSecretKey = (String)settings.get(Constants.SETTING_SECRET_KEY);
        webctRequestSource = (String)settings.get(Constants.SETTING_REQUEST_SRC);
		
        List roles;
		this.lcID = super.getCurrentLearningContextId();
		
		request = super.getRequest();
		Map params = new HashMap();            
        String html=null;
        String authorUrl = null;
        String learningDesigns = null;
        String learnerUrl = "";
        String monitorUrl = "";
		
		String ptid = request.getParameter("id");
		if(ptid==null)
	    {
	        // the id param was null, so PT was probably called from My WebCT
	        ptid = request.getParameter("tid");
	        
	        params.put("id_param_name", "tid");
	    }
		else
		{
			 params.put("id_param_name", "id");
		}
		params.put("pt_id", ptid);
		params.put("page_id", request.getParameter("page_id"));

		
		try{
	    	this.user = UserService.getInstance().getUser(super.getUserId(), lcID);
	    }
	    catch (VistaDataException e)
	    {
	    	e.printStackTrace();
	    	throw new LoginException("Problem getting user details from WebCt: " + e.getMessage());
	    }
	    	
	    boolean canAuthor = hasLamsRole("authorRoles");
		boolean canMonitor = hasLamsRole("monitorRoles");
		boolean canLearner = hasLamsRole("learnerRoles") || canMonitor;
		if (!canAuthor && !canLearner && !canMonitor)
		{
			throw new LoginException("User's role does not have access to these pages.");
		}
		params.put("canAuthor", new Boolean(canAuthor));
		params.put("canMonitor", new Boolean(canMonitor));
		params.put("canLearner", new Boolean(canLearner));
		
		boolean isTeacher = canAuthor || canMonitor;
        
        String action = request.getParameter("form_action");
        
        if(action==null || action.trim().length()==0)
        {
        	
        	try{
        		learnerUrl = generateRequestURL(user, lcID, "learner");
        	}
        	catch (Exception e)
        	{
        		throw new LoginException("Bad learner request: " + e.getMessage());
        	}
        	
        	if (isTeacher)
            {
            	// generate teacher page
        		// ie list of running lessons, and a create new lesson button
        		try{
        			ILamsLessonDao lessonDao = new LamsLessonDaoMySqlJDBC(settings);
        			List lessons = lessonDao.getDBLessons(lcID.longValue(), Long.parseLong(ptid));

        			monitorUrl = generateRequestURL(user, lcID, "monitor");
        			
        			params.put("lessons", lessons);
        			params.put("learnerUrl", learnerUrl);
        			params.put("monitorUrl", monitorUrl);
        			params.put("liveEditUrl", authorUrl);
        			
        			html = this.generatePage("web/teach.vm", params);
            	}
            	catch (Exception e)
            	{
            		e.printStackTrace();
            		log.error("Error creating LAMS teach page", e);
            		throw new LoginException("Error creating LAMS teach page");
            	}
            }
            else
            {
            	// generate student page
            	// ie list of running lessons
            	try{
            		ILamsLessonDao lessonDao = new LamsLessonDaoMySqlJDBC(settings);

        			// test
        			//List lessons = lessonDao.getDBLessons(1);
        			
            		java.util.Date today = new java.util.Date();
            	    Timestamp now = new Timestamp(today.getTime());
            		
            		List lessons = lessonDao.getDBLessonsForLearner(lcID.longValue(), Long.parseLong(ptid), now);
        			
        			params.put("lessons", lessons);
        			
        			params.put("learnerUrl", learnerUrl);
        			
            		html = this.generatePage("web/learner.vm", params);
            	}
            	catch (Exception e)
            	{
            		throw new LoginException("Error creating LAMS learner page");
            	}
            }
        }
        else if (action.equalsIgnoreCase("create_lesson"))
        {
        	// goto create lesson form
        	try {
            	// get the authorUrl for the author link
            	authorUrl = generateRequestURL(user, lcID, "author");
            	params.put("authorUrl", authorUrl);
            	
            	// get the learning designs to display the workspace tree
            	learningDesigns = getLearningDesigns(user, lcID);
            	params.put("learningDesigns", learningDesigns);
            	
            	html = this.generatePage("web/create.vm", params);
        	
        	}
        	catch (Exception e)
        	{
        		e.printStackTrace();
        		log.error("Error creating LAMS create lesson page: ", e);
        		throw new LoginException("Error creating LAMS create lesson page: " + e.getMessage());
        	}
        }
        else if (action.equals("delete_lesson"))
        {
        	String successMessage="LAMS lesson deleted.";
        	
        	String lsID = request.getParameter("lsID");
        	ILamsLessonDao lessonDao = new LamsLessonDaoMySqlJDBC(settings);

        	boolean success = lessonDao.deleteDbLesson(Long.parseLong(lsID));

        	if (!success)
        	{
        		successMessage="Failed to delete LAMS lesson.";
        	}
        	
        	
        	params.put("successMessage", successMessage);
        	
        	try{
        		html = this.generatePage("web/lessonCreated.vm", params);

        	}
        	catch (Exception e)
        	{
        		throw new LoginException("Error creating LAMSpage: " + e.getMessage());
        	}
        	
        }
        else if (action.equalsIgnoreCase("start_lesson"))
        {
        	String seqID = request.getParameter("sequence_id");
        	String title = request.getParameter("title");
        	String description = request.getParameter("description");
        	String successMessage="";
        	
        	long lsID = LamsSecurityUtil.startLesson(
        			lamsServerUrl, 
        			lamsServerId, 
        			lamsServerSecretKey,
        			webctRequestSource, 
        			user.getUserId(), 
        			"" + seqID, 
        			Constants.DEFAULT_LANGUAGE, 
        			Constants.DEFAULT_COUNTRY, 
        			user.getFirstname(), 
        			user.getLastname(), 
        			user.getEmail(), 
        			seqID, 
        			title, 
        			description,
        			"start");
        	
        	
        	if (lsID==-1)
        	{
        		successMessage="Failed to create LAMS lesson.";
        	}
        	else
        	{
        		Timestamp start = null;
        		Timestamp end = null;
        			
        		if (request.getParameter("schedule").equals("true"))
        		{
        			if (request.getParameter("dateStart")!=null && !request.getParameter("dateStart").equals(""))
            		{
        				start = getTimeStamp(request.getParameter("dateStart"), 
        							 request.getParameter("startHour"), 
        							 request.getParameter("startMin"), 
        							 request.getParameter("startAMPM"));
            		}
        			
        			if (request.getParameter("dateEnd")!=null && !request.getParameter("dateEnd").equals(""))
            		{
        			
        				end = getTimeStamp(request.getParameter("dateEnd"), 
							 	request.getParameter("endHour"), 
							 	request.getParameter("endMin"), 
							 	request.getParameter("endAMPM"));
            		}
        		}
        		
        		ILamsLessonDao lessonDao = new LamsLessonDaoMySqlJDBC(settings);
            	LamsLesson lesson = new LamsLesson(
            			lsID, 
            			Long.parseLong(ptid),
            			lcID.longValue(), 
    					Long.parseLong(seqID), 
    					title,
    					description, 
    					user.getUserId(), 
    					user.getFirstname(),
    					user.getLastname(), 
    					request.getParameter("isAvailable").equals("false"), 
    					request.getParameter("schedule").equals("true"),
    					start, 
    					end
            			);
            	
            	try{
            		// create the lesson
            		lessonDao.createDbLesson(lesson);
            	}
            	catch (Exception e)
            	{
            		log.error("Could not create LAMS lesson", e);
            		throw new LoginException("Could not create LAMS lesson");
            	}
        	
            	successMessage="LAMS lesson started.";
        	}
        	
        	params.put("successMessage", successMessage);
        	
        	try{
        		html = this.generatePage("web/lessonCreated.vm", params);
        	
        	
        	}
        	catch (Exception e)
        	{
        		log.error("Error creating LAMS lesson created page: ", e);
        		throw new LoginException("Error creating LAMS lesson created page: " + e.getMessage());
        	}
        }
        else if(action.equals("preview"))
        {
        	String title = request.getParameter("title");
        	String description = request.getParameter("description");
        	String ldID = request.getParameter("sequence_id");
        	
        	if (ldID==null ||  ldID.equals(""))
        	{
        		throw new LoginException("Bad preview request.");
        	}
        	
        	if (title==null) {title="";}
        	if (description==null) {description="";}
        	
        	try{
	        	long pvID = LamsSecurityUtil.startLesson(
	        			lamsServerUrl, 
	        			lamsServerId, 
	        			lamsServerSecretKey,
	        			webctRequestSource, 
	        			user.getUserId(), 
	        			"" + lcID, 
	        			Constants.DEFAULT_LANGUAGE, 
	        			Constants.DEFAULT_COUNTRY, 
	        			user.getFirstname(), 
	        			user.getLastname(), 
	        			user.getEmail(), 
	        			ldID, 
	        			title, 
	        			description,
	        			"preview");	
	        	
	        	String previewUrl = generateRequestURL(user, lcID, "learner") + "&lsid=" +pvID;
	        	System.out.println("PREVIEW URL: " + previewUrl);
	        	super.setRedirectUrl(previewUrl);
        	}
        	catch (Exception e)
        	{
        		log.error("Error generating LAMS preview", e);
        		throw new LoginException("Error generating LAMS preview");
        	}
        	return true;
        }
        else if(action.equals("modify_lesson"))
        {
        	try{
        		ILamsLessonDao lessonDao = new LamsLessonDaoMySqlJDBC(settings);
	        	LamsLesson modLesson = lessonDao.getDBLesson(request.getParameter("lsID"));
	        	
	        	
	        	params.put("lsID", request.getParameter("lsID"));
	        	params.put("title", modLesson.getTitle());
	        	params.put("description", modLesson.getDescription());
	        	
	        	
	        	if (modLesson.getStartTimestamp()!=null)
	        	{
	        		Calendar calendarStart = Calendar.getInstance();      	
	        		calendarStart.setTime(modLesson.getStartTimestamp());
	        		params.put("startStr", + calendarStart.get(Calendar.DATE) + "/" + calendarStart.get(Calendar.MONTH) + "/" + calendarStart.get(Calendar.YEAR));
	        		params.put("stHrCk" + calendarStart.get(Calendar.HOUR), "selected" );
	        		params.put("stMnCk" + calendarStart.get(Calendar.MINUTE), "selected" );
	        		params.put("stAmCk" + calendarStart.get(Calendar.AM_PM), "selected" );
	        	}
	        	else
	        	{
	        		params.put("startStr", "");
		        	params.put("stHrCk9", "selected" );
		        	params.put("stMnCk0", "selected" );
		        	params.put("stAmCk0", "selected" );
	        	}
	        	
	        	if (modLesson.getEndTimestamp()!=null)
	        	{
	        		Calendar calendarEnd = Calendar.getInstance();
	        		calendarEnd.setTime(modLesson.getEndTimestamp());
		        	params.put("endStr", calendarEnd.get(Calendar.DATE) + "/" + calendarEnd.get(Calendar.MONTH) + "/" + calendarEnd.get(Calendar.YEAR));
		        	params.put("edHrCk" + calendarEnd.get(Calendar.HOUR), "selected" );
		        	params.put("edMnCk" + calendarEnd.get(Calendar.MINUTE), "selected" );
		        	params.put("edAmCk" + calendarEnd.get(Calendar.AM_PM), "selected" );
	        	}
	        	else
	        	{
	        		params.put("endStr", "");
		        	params.put("edHrCk9", "selected" );
		        	params.put("edMnCk0", "selected" );
		        	params.put("edAmCk0", "selected" );
	        	}
	
	        	if (modLesson.getHidden()) 
	        	{
	        		params.put("hidden", "checked");
	        	}
	        	else
	        	{
	        		params.put("nothidden", "checked");
	        	}
	        	
	        	
	        	if (modLesson.getSchedule())
	        	{
	        		params.put("schedule", "checked");
	        	}
	        	else
	        	{
	        		params.put("notschedule", "checked");
	        	}
	        	
	        	html = this.generatePage("web/modify.vm", params);
        	}
        	catch (Exception e)
        	{
        		log.error("Error creating LAMS lesson modify page: ", e);
        		throw new LoginException("Error creating LAMS lesson modify page: " + e.getMessage());
        	}
        	
        }
        else if(action.equals("modify_proc"))
        {
        	ILamsLessonDao lessonDao = new LamsLessonDaoMySqlJDBC(settings);
        	
        	
        	try{
	        	LamsLesson modLesson = lessonDao.getDBLesson(request.getParameter("lsID"));
	        	
	        	modLesson.setTitle(request.getParameter("title"));
	        	modLesson.setDescription(request.getParameter("description"));
	        	modLesson.setHidden(request.getParameter("isAvailable").equals("false"));
	        	modLesson.setSchedule(request.getParameter("schedule").equals("true"));
	        	
	        	Timestamp start = null;
        		Timestamp end = null;
        			
        		if (request.getParameter("schedule").equals("true"))
        		{
        			if (request.getParameter("dateStart")!=null && !request.getParameter("dateStart").equals(""))
            		{
        				start = getTimeStamp(request.getParameter("dateStart"), 
        							 request.getParameter("startHour"), 
        							 request.getParameter("startMin"), 
        							 request.getParameter("startAMPM"));
            		}
        			
        			if (request.getParameter("dateEnd")!=null && !request.getParameter("dateEnd").equals(""))
            		{
        			
        				end = getTimeStamp(request.getParameter("dateEnd"), 
							 	request.getParameter("endHour"), 
							 	request.getParameter("endMin"), 
							 	request.getParameter("endAMPM"));
            		}
        		}
	        	
        		modLesson.setStartTimestamp(start);
	        	modLesson.setEndTimestamp(end);
	        	
	        	
	        	boolean success = lessonDao.updateLesson(modLesson);
	        	
	
	        	if (success)
	        	{
	        		params.put("successMessage", "LAMS lesson updated successfully.");
	        	}
	        	else
	        	{
	        		params.put("successMessage", "Unable to update LAMS lesson.");
	        	}

        		html = this.generatePage("web/lessonCreated.vm", params);
        	
        	
        	}
        	catch (Exception e)
        	{
        		log.error("Error creating LAMS lesson created page: ", e);
        		throw new LoginException("Error creating LAMS lesson created page: " + e.getMessage());
        	}
        	
        }
        
        
        super.setResponseContent(html);

        return true;
    
    }
    

    private String generatePage(String htmlTemplate, Map contextObjects) throws Exception
    {
       String jar_base = "./deployablecomponents/lams2/";
        
        VelocityEngine ve = new VelocityEngine();
        
        Properties props = new Properties();
        props.setProperty("resource.loader", "file, jar");
        props.setProperty("file.resource.loader.path", jar_base);
        props.setProperty("jar.resource.loader.class", "org.apache.velocity.runtime.resource.loader.JarResourceLoader");
        props.setProperty("jar.resource.loader.path", "jar:file:" + jar_base + JARSTR);            
        ve.init(props);
        
        VelocityContext context = new VelocityContext();
        
        for(Iterator it = contextObjects.keySet().iterator(); it.hasNext();)
        {
            String key = (String)it.next();
            context.put(key, contextObjects.get(key));
        }
        
        context.put("request", request);
        context.put("settings", settings);
        context.put("authmod", this);
    
        Template template = null; 
        template = ve.getTemplate(htmlTemplate);
        StringWriter html = new StringWriter();
        template.merge(context, html);
        return html.toString();
    }
    
    public String generateRequestURL(UserVO user, Long lcID, String method) throws LoginException
    {
    	String authorUrl="";
    	
    	try
        {
        	authorUrl = LamsSecurityUtil.generateRequestURL(
        						lamsServerUrl, 
        						lamsServerId,
        						lamsServerSecretKey,
        						webctRequestSource, 
        						user.getUserId(), 
        						"" + lcID, 
        						Constants.DEFAULT_LANGUAGE, 
        						Constants.DEFAULT_COUNTRY,         						
        						user.getFirstname(),
        						user.getLastname(),
        						user.getEmail(),
        						method);
        
        	
        	log.info("LAMS AUTHOR REQUEST: " + authorUrl);
        	System.out.println("LAMS AUTHOR REQUEST: " + authorUrl);
            return authorUrl;
        }
        catch (Exception e)
        {
        	log.error("Problem generating request url:", e);
        	System.out.println("Problem generating request url: " + e.getMessage() );
        	e.printStackTrace();
        	throw new LoginException("Problem getting author url:" + e.getMessage());
        }
    }
    
    public String getLearningDesigns(UserVO user, Long lcID) throws LoginException
    {
    	String learningDesigns = "";
    	try
        {
            learningDesigns = LamsSecurityUtil.getLearningDesigns(
            					lamsServerUrl, 
            					lamsServerId, 
            					lamsServerSecretKey, 
            					user.getUserId(), 
            					"" + lcID, 
            					Constants.DEFAULT_LANGUAGE, 
            					Constants.DEFAULT_COUNTRY, 
            					user.getFirstname(),
        						user.getLastname(),
        						user.getEmail(),
            					"1");
            
            log.info("LAMS LEARNING DESIGNS: " + learningDesigns);
        	System.out.println("LAMS LEARNING DESIGN: " + learningDesigns);
            return learningDesigns;
        
        }
        catch (Exception e)
        {
        	log.error("Problem getting learning desings:", e);
        	System.out.println("Problem getting learning desings: " + e.getMessage() );
        	e.printStackTrace();
        	throw new LoginException("Problem getting learning desings: " + e.getMessage());
        }
    }
    
    public Timestamp getTimeStamp(String date, String hours, String minutes, String ampm)
    {
    	
    	String dateSplit[] = date.split("/");
    	
    	date = dateSplit[2] + "-" + dateSplit[1] + "-" + dateSplit[0];
    	
    	if (ampm.equals("PM"))
    	{
    		int hoursInt = Integer.parseInt(hours);
    		hoursInt += 12;
    		hours = "" + hoursInt;
    	}
    	
    	String timestampStr = date + " " + hours + ":" + minutes + ":" + "00";
    	System.out.println("TIMESTAMP: " + timestampStr);
    	
    	
    	Timestamp t = Timestamp.valueOf(timestampStr);
    	System.out.println("TIMESTAMP To String: " + t.toString());
    	return t;
    	
    }
    
    /**
     * Checks if the user has the lams role
     * @param roleType
     * @return
     */
    public boolean hasLamsRole(String lamsRoleType)
    {
    	boolean hasLameRole = false;
    	String learnerRoles[] = (String[])settings.get(lamsRoleType);
    	
    	System.out.print("ROLES: ");
    	for(int i = 0; i<learnerRoles.length; i++)
        {
            String role = learnerRoles[i];
            System.out.print( role + ", ");
            if (hasSpecificRole(role))
            {
            	hasLameRole = true;
            }
        }
    	System.out.print("\n");
    	return hasLameRole;
    	
    	/*
    	for(Iterator it = learnerRoles.iterator(); it.hasNext();)
        {
            String role = (String)it.next();
            if (hasSpecificRole(role))
            {
            	hasLameRole = true;
            }
        }
    	return hasLameRole;
		*/
    }
    
    /**
     * Checks if a user's role list contains a specific role
     * @param roleParam the role to be checked
     * @return true if the user has teh specified role
     */
    public boolean hasSpecificRole(String roleParam)
    {
    	UserVO user = null;
    	try
    	{
    		user = UserService.getInstance().getUser(super.getUserId(), lcID);
    	}
    	catch (Exception e)
    	{
    		
    		e.printStackTrace();
    	}
    	
    	List roles = user.getUserRoles();
		Iterator it = roles.iterator();
    	
		boolean hasRole = false;
		
    	while (it.hasNext())
		{
			String role = it.next().toString().trim();
			if (role.equals(roleParam))
			{
				hasRole=true;
			}
		}
    	
    	return hasRole;
    
    }
    
    
    public boolean logout() throws LoginException
    {
        return super.logout();
    }
}
		
	