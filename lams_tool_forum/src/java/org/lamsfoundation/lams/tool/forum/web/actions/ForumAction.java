package org.lamsfoundation.lams.tool.forum.web.actions;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.CrNodeVersionProperty;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.tool.forum.core.PersistenceException;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.service.ForumManager;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.util.ForumToolContentHandler;
import org.lamsfoundation.lams.tool.forum.web.forms.ForumForm;
import org.lamsfoundation.lams.tool.forum.web.forms.MessageForm;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 10/06/2005
 * Time: 12:18:57
 * To change this template use File | Settings | File Templates.
 */
public class ForumAction extends Action {
  private static Logger log = Logger.getLogger(ForumAction.class.getName());
  private ForumManager forumManager;
  private ForumToolContentHandler toolContentHandler;

  public void setForumManager(ForumManager forumManager) {
      this.forumManager = forumManager;
  }

  //public ForumAction() {
       //this.forumManager = (ForumManager) GenericObjectFactoryImpl.getInstance().lookup(ForumConstants.FORUM_MANAGER);
       //this.toolContentHandler = (ForumToolContentHandler) GenericObjectFactoryImpl.getInstance().lookup(ForumConstants.CONTENT_HANDLER);
      //GenericObjectFactoryImpl.getInstance().configure(this);
  //}

  	private ForumManager getForumManager() {
  	    if ( forumManager == null ) {
  	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
  	      forumManager = (ForumManager) wac.getBean(ForumConstants.FORUM_MANAGER);
  	    }
  	    return forumManager;
  	}

  	private ForumToolContentHandler getToolContentHandler() {
  	    if ( toolContentHandler == null ) {
  	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
  	    toolContentHandler = (ForumToolContentHandler) wac.getBean(ForumToolContentHandler.SPRING_BEAN_NAME);
  	    }
  	    return toolContentHandler;
  	}

	public final ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
  		String param = mapping.getParameter();
	  	if (param.equals("createForum")) {
       		return createForum(mapping, form, request, response);
        }
	  	if (param.equals("editForum")) {
       		return editForum(mapping, form, request, response);
        }
	  	if (param.equals("getForum")) {
       		return getForum(mapping, form, request, response);
        }
	  	if (param.equals("deleteForum")) {
       		return deleteForum(mapping, form, request, response);
        }
	  	if (param.equals("createTopic")) {
       		return createTopic(mapping, form, request, response);
        }
        if (param.equals("instructions")) {
       		return saveInstructions(mapping, form, request, response);
        }
        if (param.equals("deleteAttachment")) {
       		return deleteAttachment(mapping, form, request, response);
        }
        if (param.equals("deleteTopic")) {
       		return deleteTopic(mapping, form, request, response);
        }
        if (param.equals("advanced")) {
            return mapping.findForward("success");
        }
		return mapping.findForward("error");
    }

    public ActionForward createForum(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
            throws IOException, ServletException, Exception {
        ForumForm forumForm = (ForumForm) form;
        Forum forum = forumForm.getForum();

        Map topics = (Map) request.getSession().getAttribute("topics");
        forum = getForumManager().createForum(forum, forumForm.getAttachments(), topics);
        forumForm.setForum(forum);

        //populate topics with new topics
        List topicList = getForumManager().getTopics(forum.getUuid());
        topics = new HashMap();
        Iterator it = topicList.iterator();
        while (it.hasNext()) {
            Message message = (Message) it.next();
            topics.put(message.getSubject(), message);
        }

        request.getSession().setAttribute("topics", topics);
        request.getSession().setAttribute("topicList", topicList);
        return mapping.findForward("success");
  }

  public ActionForward editForum(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
            throws IOException, ServletException, Exception {
        ForumForm forumForm = (ForumForm) form;
        Forum forum = forumForm.getForum();
        Map topics = (Map) request.getSession().getAttribute("topics");
        getForumManager().editForum(forum, forumForm.getAttachments(), topics);
        return mapping.findForward("success");
  }

  public ActionForward getForum(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
          throws IOException, ServletException, Exception {
        Long forumId = new Long((String) request.getParameter("forumId"));
        Forum forum = getForumManager().getForum(forumId);
        List topicList = getForumManager().getTopics(forum.getUuid());
        ForumForm forumForm = new ForumForm();

        forumForm.setForum(forum);
        request.getSession().setAttribute("forum", forumForm);

        Map topics = new HashMap();
        Iterator it = topicList.iterator();
        while (it.hasNext()) {
            Message message = (Message) it.next();
            topics.put(message.getSubject(), message);
        }

        request.getSession().setAttribute("topics", topics);
        request.getSession().setAttribute("topicList", topicList);

        List attachmentList = new ArrayList();
        Collection entries = forum.getAttachments();
        it = entries.iterator();
        while (it.hasNext()) {
            Attachment attachment = (Attachment) it.next();
            //ContentHandler handler = new ContentHandler();
            Set properties = getToolContentHandler().getFileProperties(attachment.getUuid());
            Iterator propIt = properties.iterator();
            while (propIt.hasNext()) {
                CrNodeVersionProperty property = (CrNodeVersionProperty) propIt.next();
                if ("FILENAME".equals(property.getName())) {
                    attachment.setName(property.getValue());
                }
                if ("TYPE".equals(property.getName())) {
                    attachment.setType(property.getValue());
                }
                if ("MIMETYPE".equals(property.getName())) {
                    attachment.setContentType(property.getValue());
                }
            }
            attachmentList.add(attachment);
        }
        request.getSession().setAttribute("attachmentList", attachmentList);
        return mapping.findForward("success");
  }

  public ActionForward deleteForum(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
          throws IOException, ServletException, Exception {
        Long forumId = new Long((String) request.getParameter("forumId"));
        getForumManager().deleteForum(forumId);
        return (mapping.findForward("success"));
  }

   public ActionForward createTopic(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
          throws IOException, ServletException, PersistenceException {
        MessageForm messageForm = (MessageForm) form;
        Message message = messageForm.getMessage();
        Map topics = (Map) request.getSession().getAttribute("topics");
        if (topics ==  null) {
            topics = new HashMap();
        }
        topics.put(message.getSubject(), message);
        request.getSession().setAttribute("topics", topics);
        request.getSession().setAttribute("topicList", new ArrayList(topics.values()));
        return mapping.findForward("success");
    }

    public ActionForward saveInstructions(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
          throws IOException, ServletException, PersistenceException {
        ForumForm forumForm = (ForumForm) form;

        try {
            processFile(forumForm, forumForm.getOnlineFile(), Attachment.TYPE_ONLINE);
            processFile(forumForm, forumForm.getOfflineFile(), Attachment.TYPE_OFFLINE);
        } catch (FileNotFoundException e) {
            // TODO Create proper error message and return to user
            log.error("Unable to uploadfile",e);
            throw new IOException("Unable to upload file, exception was "+e.getMessage());
        } catch (IOException e) {
            log.error("Unable to uploadfile",e);
            throw new IOException("Unable to upload file, exception was "+e.getMessage());
        } catch (RepositoryCheckedException e) {
            log.error("Unable to uploadfile",e);
            throw new IOException("Unable to upload file, exception was "+e.getMessage());
        }
            
        Collection entries = forumForm.getAttachments().values();
        List attachmentList = new ArrayList(entries);
        request.getSession().setAttribute("attachmentList", attachmentList);
        return mapping.findForward("success");
    }

    /**
     * Process an uploaded file.
     * 
     * @param forumForm
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private void processFile(ForumForm forumForm, FormFile file, String attachmentType) 
    	throws InvalidParameterException, FileNotFoundException, RepositoryCheckedException, IOException {
        
        Map attachmentsMap = forumForm.getAttachments();
        if (file!= null && !(file.getFileName().trim().equals(""))) {
            String fileName = file.getFileName();
            String keyName = fileName + "-" + attachmentType;
            if (!attachmentsMap.containsKey(keyName)) {
                NodeKey node = getToolContentHandler().uploadFile(file.getInputStream(), fileName, 
                        file.getContentType(), attachmentType);
                Attachment attachment = new Attachment();
                attachment.setName(fileName);
                attachment.setStream(file.getInputStream());
                attachment.setUuid(node.getUuid());
                attachment.setType(attachmentType);
                attachmentsMap.put(keyName, attachment);
                forumForm.setOnlineFile(null);
            }
        }
    }

    public ActionForward deleteAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = new ActionForward();
		forward.setPath(mapping.getInput());
        String fileName = (String) request.getParameter("fileName");
        String type = (String) request.getParameter("type");
        ForumForm forumForm = (ForumForm) form;
        Map attachments = forumForm.getAttachments();
        Attachment attachment = (Attachment) attachments.remove(fileName + "-" + type);
        getToolContentHandler().deleteFile(attachment.getUuid());
        if (attachment.getUuid() != null) {
            getForumManager().deleteForumAttachment(attachment.getUuid());
        }
        List attachmentList = new ArrayList(attachments.values());
        request.getSession().setAttribute("attachmentList", attachmentList);
        return mapping.findForward("success");
    }

    public ActionForward deleteTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String topicName = (String) request.getParameter("topicName");
        Map topics = (Map) request.getSession().getAttribute("topics");
        Message topic = (Message) topics.remove(topicName);
        if (topic.getUuid() != null) {
            getForumManager().deleteMessage(topic.getUuid());
        }
        request.getSession().setAttribute("topics", topics);
        request.getSession().setAttribute("topicList", new ArrayList(topics.values()));
        return mapping.findForward("success");
    }

}
