
<%@ page info="removeItem_proc.jsp" %>
<%@ page isThreadSafe="true" errorPage="../error.jsp" %>
<%@ page import="
                 java.io.*,
                 java.util.*,
                 blackboard.platform.BbServiceManager,
                 blackboard.platform.intl.BundleManager,
                 blackboard.platform.intl.BbResourceBundle,
                 blackboard.platform.filesystem.*,
                 blackboard.platform.plugin.*,
                 blackboard.servlet.tags.TextboxTag,
                 blackboard.servlet.data.WysiwygText,              
                 blackboard.db.DbUtil,
                 blackboard.base.*,
                 blackboard.persist.*,
                 blackboard.persist.content.*, 
                 blackboard.persist.navigation.*,
                 blackboard.data.content.*,                 
                 blackboard.data.course.*,
                 blackboard.util.*,
                 blackboard.util.resolver.*,
                 blackboard.util.StringUtil,
                 blackboard.platform.plugin.PlugInUtil,
                 blackboard.platform.context.ContextUrlHandler,
                 blackboard.platform.log.*,
                 blackboard.platform.session.*,
                 blackboard.platform.security.*
                "%>                 

<%@ taglib uri="/bbData" prefix="bbData"%>                
<%@ taglib uri="/bbUI" prefix="bbUI"%>

<bbData:context id="ctx">
<% 
   // Authorization Check
  try {
      BbSessionManagerService sessionService = BbServiceManager.getSessionManagerService();
      BbSession bbSession = sessionService.getSession( request );
      AccessManagerService accessManager = (AccessManagerService) BbServiceManager.lookupService( AccessManagerService.class );

      if( ! bbSession.isAuthenticated() ) {
        accessManager.sendLoginRedirect(request,response);
      }
      if( ! SecurityUtil.userHasEntitlement( "course.content.DELETE" ) ) {
        %><jsp:forward page="content_access_denied.jsp" /><%
      }
    }
    catch( Exception e ) {
      throw new PlugInException("Authentication failed.", e);
    }
  
   // Resource Bundle Load

   //
   // Locale-based String Defs 
   //

   //
   // Loaders and Managers
   //   
   BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
   ContentDbPersister persisterContent = ContentDbPersister.Default.getInstance(); 
   FileSystemService fileSysService = (FileSystemService)BbServiceManager.lookupService( FileSystemService.class );    
   BbSessionManagerService sessionService = BbServiceManager.getSessionManagerService();
   BbSession bbSession = sessionService.getSession( request );

   //
   // Request Parameters
   //
   String strContentId = RequestUtil.getStringParameter(request,"content_id",null); 
   String strCourseId = RequestUtil.getStringParameter(request,"course_id",null);

   // Added because the Admin: Services Tab and Hot Links for any Tab uses the SYSTEM course
   // to store the Content Items that are rendered in the portal.  Used for navigation purposes  
   String strArea       = RequestUtil.getStringParameter(request,"area","");
   String strTabId      = RequestUtil.getStringParameter(request,"tab_id",""); 
   boolean bIsTabContent = (StringUtil.notEmpty(strArea)&&StringUtil.notEmpty(strTabId))?true:false;
   String strHrefTabParams = "";  // will be blank for non tabcontent items
   if (bIsTabContent) 
   {
       strHrefTabParams = "&area="+strArea+"&tab_id="+strTabId;
   }

   //
   // Load Java Ids from pkIDs
   //
   Id idContent = bbPm.generateId(Content.DATA_TYPE, strContentId);   

   //
   // Page processing
   //
   try {

     if (idContent != null) {

       // Need to load the content object so that we can get the parent (needed for the return)
       Id idParent = ContentDbLoader.Default.getInstance().loadById(bbPm.generateId(Content.DATA_TYPE,strContentId)).getParentId();

       // if we are deleteing a folder, get a list of all of the contentIds under that folder
       BbList folderContentIds = ContentDbLoader.Default.getInstance().loadListById(idContent);
		// reverse order since we want to delete children before parents.
       Collections.reverse(folderContentIds);
       Iterator contentIter = folderContentIds.iterator();

       while (contentIter.hasNext()) 
       {

         try {

           // load the content item
           Content contentToRemove = (Content) contentIter.next();

           try
           {
             // see if the content item handler has an additional remove processing event
             ContentHandler cHandler = ContentHandlerDbLoader.Default.getInstance().loadByHandle(contentToRemove.getContentHandler());
  
             // if the content handler is from a plugin and has a remove action defined AND is not the same removeItem_proc (to prevent circular references)
             if (cHandler.getPlugInId().isSet() && StringUtil.notEmpty(cHandler.getHttpActionRemove())) 
             {           
               if (!cHandler.getHttpActionRemove().matches(".*removeItem_proc.jsp.*"))
               {             
                 try
                 {
                   StringTokenizer tokens = new StringTokenizer(cHandler.getHttpActionRemove(),"/");
                   tokens.nextToken(); // skip /webapps/ part of url
                   String strWebApp = "/webapps/" + tokens.nextToken();
                   String strRelPath = "/" + tokens.nextToken(" ");
              
                   //Encodes url if @X@ signals are included.

				   Resolver resolver = Resolver.getDefaultResolver(contentToRemove);              
				   strRelPath = resolver.resolve(strRelPath);

                   ServletContext  ctxServlet = application.getContext(strWebApp);
                   RequestDispatcher rd  = ctxServlet.getRequestDispatcher(strRelPath);                
  
                   //System.out.println("Dispatching content removal for Type: "+ cHandler.getHandle() +" URL is : "+ strRelPath);


                   // need to override response in case of output
				   HttpServletResponse wrappedResponse = new HttpServletResponseWrapper(response) {
						public PrintWriter getWriter() throws IOException {
							return new PrintWriter(new ByteArrayOutputStream());
						}

				   };
                   rd.include(request, wrappedResponse);
                 }
                 catch (Exception e)
                 {
                   // custom removal jsp file doesn't exist, so ignore
                 }
               }
             }
           }
           catch (Exception e)
           {
             // something else went wrong...
           }

           // cleanup the filesystem
           File contentDir = fileSysService.getContentDirectory(ctx.getCourse(), contentToRemove.getId());
           FileUtil.recycle(contentDir);
  
           // Remove from the database
           persisterContent.deleteById(contentToRemove.getId());

           //notify the cache on tab hot link content updats
           if (bIsTabContent) {
               TabLinkDbLoader tl = (TabLinkDbLoader) bbPm.getLoader(TabLinkDbLoader.TYPE);
               tl.refreshCache();
           }

         }
         catch (Exception e) 
         {
           BbServiceManager.getLogService().logError("Error removing content", e);
         }

       }
       response.sendRedirect(PlugInUtil.getEditableContentReturnURL(idParent)+strHrefTabParams);
       return;
     }

   } catch (Exception e) 
   {
     throw e;
   }
   

   
%>

</bbData:context>

