package org.lamsfoundation.integration.webct;

import java.io.StringWriter;
import java.net.URLEncoder;
import java.net.URL;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import java.sql.Date;
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
import org.lamsfoundation.integration.dao.LamsLessonDaoJDBC;
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

    

    
    private static final Logger log = Logger.getLogger(LamsModule.class);
    
    
    
    private static final String GUTENBERG_QUERY_URL = "http://www.gutenberg.org/catalog/world/results";
    
    
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
    	

    	
    	Map settings = super.getSettings();
    	//user = super.getUserId();
		lamsServerUrl = (String)settings.get(Constants.SETTING_LAMS_SERVER_URL);
        lamsServerId = (String)settings.get(Constants.SETTING_SERVER_ID);
        lamsServerSecretKey = (String)settings.get(Constants.SETTING_SECRET_KEY);
        webctRequestSource = (String)settings.get(Constants.SETTING_REQUEST_SRC);
    	

		UserVO user = null;
		List roles;
		Long lcID = super.getCurrentLearningContextId();
		
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

	    
		boolean isTeacher = false;
		boolean isStudent = false;
		try {
    		user = UserService.getInstance().getUser(super.getUserId(), lcID);
    		roles = user.getUserRoles();
    		Iterator it = roles.iterator();
    		while (it.hasNext())
    		{
    			String role = it.next().toString().trim();
    			
    			System.out.println("ROLE: " + role);
    			if (role.equals(UserRole.COURSE_INSTRUCTOR_ROLE.toString()) || role.equals(UserRole.SECTION_INSTRUCTOR_ROLE.toString()) )
    			{
    				isTeacher=true;
    			}
    			else if (role.equals(UserRole.STUDENT_ROLE.toString()))
    			{
    				isStudent=true;
    			}			
    		}
    		if (!isStudent && !isTeacher)
    		{
				throw new LoginException("User's role does not have access to these pages.");
			}	
		}
		catch (VistaDataException e)
		{
			log.error("Problem getting user info:", e);
        	e.printStackTrace();
        	return false;
		}
		
        
        
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
        			LamsLessonDaoJDBC lessonDao = new LamsLessonDaoJDBC(settings);
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
            		log.error("Error creating LAMS teach page", e);
            		throw new LoginException("Error creating LAMS teach page");
            	}
            }
            else
            {
            	// generate student page
            	// ie list of running lessons
            	try{
            		LamsLessonDaoJDBC lessonDao = new LamsLessonDaoJDBC(settings);

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
        	LamsLessonDaoJDBC lessonDao = new LamsLessonDaoJDBC(settings);

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
        		
        		LamsLessonDaoJDBC lessonDao = new LamsLessonDaoJDBC(settings);
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
	        	LamsLessonDaoJDBC lessonDao = new LamsLessonDaoJDBC(settings);
	        	LamsLesson modLesson = lessonDao.getDBLesson(request.getParameter("lsID"));
	        	
	        	
	        	params.put("lsID", request.getParameter("lsID"));
	        	params.put("title", modLesson.getTitle());
	        	params.put("description", modLesson.getDescription());
	        	params.put("start", modLesson.getStartTimestamp());
	        	params.put("end", modLesson.getEndTimestamp());
	        	
	
	        	if (modLesson.getHidden()) 
	        	{
	        		params.put("hidden", "true");
	        		params.put("notHidden", "false");
	        	}
	        	else
	        	{
	        		params.put("hidden", "false");
	        		params.put("notHidden", "true");
	        	}
	        	
	        	
	        	if (modLesson.getSchedule())
	        	{
	        		params.put("schedule", "true");
	        		params.put("notSchedule", "false");
	        	}
	        	else
	        	{
	        		params.put("schedule", "false");
	        		params.put("notSchedule", "true");
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
        	LamsLessonDaoJDBC lessonDao = new LamsLessonDaoJDBC(settings);
        	
        	
        	try{
	        	LamsLesson modLesson = lessonDao.getDBLesson(request.getParameter("lsID"));
	        	
	        	modLesson.setTitle(request.getParameter("title"));
	        	modLesson.setDescription(request.getParameter("description"));
	        	modLesson.setHidden(request.getParameter("isAvailable").equals("true"));
	        	modLesson.setSchedule(request.getParameter("schedule").equals("true"));
	        	
	        	
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
    public boolean logout() throws LoginException
    {
        return super.logout();
    }
}
		
	