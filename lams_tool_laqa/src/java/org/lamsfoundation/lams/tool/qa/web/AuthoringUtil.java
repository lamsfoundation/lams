/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.web;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaComparator;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQuestionContentDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * Keeps all operations needed for Authoring mode. 
 * @author Ozgur Demirtas
 *
 */
public class AuthoringUtil implements QaAppConstants {
	static Logger logger = Logger.getLogger(AuthoringUtil.class.getName());
    
	
    /**
     * reconstructQuestionContentMapForAdd(TreeMap mapQuestionContent, HttpServletRequest request)
     * return void
     * adds a new entry to the Map
     */
    protected void reconstructQuestionContentMapForAdd(Map mapQuestionContent, QaGeneralAuthoringDTO qaGeneralAuthoringDTO, 
            HttpServletRequest request)
    {
    	logger.debug("pre-add Map content: " + mapQuestionContent);
    	logger.debug("pre-add Map size: " + mapQuestionContent.size());
    	
    	//repopulateMap(mapQuestionContent, request);
    	
    	mapQuestionContent.put(new Long(mapQuestionContent.size()+1).toString(), "");
    	//request.getSession().setAttribute("mapQuestionContent", mapQuestionContent);
    	qaGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);
	     
    	logger.debug("post-add Map is: " + mapQuestionContent);    	
	   	logger.debug("post-add count " + mapQuestionContent.size());
    }


    /**
     * reconstructQuestionContentMapForRemove(TreeMap mapQuestionContent, HttpServletRequest request)
     * return void
     * deletes the requested entry from the Map
     */
    protected Map reconstructQuestionContentMapForRemove(Map mapQuestionContent, HttpServletRequest request, 
            QaAuthoringForm qaAuthoringForm, String activeModule)
    {
    		logger.debug("doing reconstructQuestionContentMapForRemove with activeModule: " + activeModule);
    	 	String questionIndex =qaAuthoringForm.getQuestionIndex();
    	 	logger.debug("pre-delete map content:  " + mapQuestionContent);
    	 	logger.debug("questionIndex: " + questionIndex);
    	 	logger.debug("final removableQuestionIndex: " + questionIndex);
    	 	
    	 	long longQuestionIndex= new Long(questionIndex).longValue();
    	 	logger.debug("pre-delete count: " + mapQuestionContent.size());
    	 	
        	//repopulateMap(mapQuestionContent, request);
        	//logger.debug("post-repopulateMap questionIndex: " + questionIndex);
        	
	 		mapQuestionContent.remove(new Long(longQuestionIndex).toString());	
	 		logger.debug("removed the question content with index: " + longQuestionIndex);
	 		
	 		logger.debug("returning mapQuestionContent:" + mapQuestionContent);
	 		return mapQuestionContent;

    }

    
    protected static List swapNodes(List listQuestionContentDTO, String questionIndex, String direction)
    {
        logger.debug("swapNodes:");
        logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
        logger.debug("questionIndex:" + questionIndex);
        logger.debug("direction:" + direction);

        int intQuestionIndex=new Integer(questionIndex).intValue();
        int intOriginalQuestionIndex=intQuestionIndex;
        logger.debug("intQuestionIndex:" + intQuestionIndex);
        
        int replacedNodeIndex=0;
        if (direction.equals("down"))
        {
            logger.debug("direction down:");
            replacedNodeIndex=++intQuestionIndex;
        }
        else
        {
            logger.debug("direction up:");
            replacedNodeIndex=--intQuestionIndex;
            
        }
        logger.debug("replacedNodeIndex:" + replacedNodeIndex);
        logger.debug("replacing nodes:" + intOriginalQuestionIndex + " and " + replacedNodeIndex);
        
        QaQuestionContentDTO mainNode=extractNodeAtDisplayOrder(listQuestionContentDTO, intOriginalQuestionIndex);
        logger.debug("mainNode:" + mainNode);
        

        QaQuestionContentDTO replacedNode=extractNodeAtDisplayOrder(listQuestionContentDTO, replacedNodeIndex);
        logger.debug("replacedNode:" + replacedNode);

        List listFinalQuestionContentDTO=new LinkedList();
        
        listFinalQuestionContentDTO=reorderSwappedListQuestionContentDTO(listQuestionContentDTO, intOriginalQuestionIndex,
                replacedNodeIndex, mainNode, replacedNode);
        
        
	    logger.debug("listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
        return listFinalQuestionContentDTO;
    }
    
    
    
    protected  static List reorderSwappedListQuestionContentDTO(List listQuestionContentDTO, int intOriginalQuestionIndex, 
            int replacedNodeIndex, QaQuestionContentDTO mainNode, QaQuestionContentDTO replacedNode)
    {
        logger.debug("reorderSwappedListQuestionContentDTO: intOriginalQuestionIndex:" + intOriginalQuestionIndex);
        logger.debug("reorderSwappedListQuestionContentDTO: replacedNodeIndex:" + replacedNodeIndex);
        logger.debug("mainNode: " + mainNode);
        logger.debug("replacedNode: " + replacedNode);
        
        
        List listFinalQuestionContentDTO=new LinkedList();
    	
	    int queIndex=0;
	    Iterator listIterator=listQuestionContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        QaQuestionContentDTO qaQuestionContentDTO= (QaQuestionContentDTO)listIterator.next();
	        logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
	        logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());
	        queIndex++;
	        QaQuestionContentDTO tempNode=new QaQuestionContentDTO();
	        
            if ((!qaQuestionContentDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())) && 
                 !qaQuestionContentDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString()))
            {
            	logger.debug("normal copy ");
            	tempNode.setQuestion(qaQuestionContentDTO.getQuestion());
            	tempNode.setDisplayOrder(qaQuestionContentDTO.getDisplayOrder());
            	tempNode.setFeedback(qaQuestionContentDTO.getFeedback());
            }
            else if (qaQuestionContentDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString()))
            {
            	logger.debug("move type 1 ");
            	tempNode.setQuestion(replacedNode.getQuestion());
            	tempNode.setDisplayOrder(replacedNode.getDisplayOrder());
            	tempNode.setFeedback(replacedNode.getFeedback());
            }
            else if (qaQuestionContentDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString()))
            {
            	logger.debug("move type 1 ");
            	tempNode.setQuestion(mainNode.getQuestion());
            	tempNode.setDisplayOrder(mainNode.getDisplayOrder());
            	tempNode.setFeedback(mainNode.getFeedback());
            }
	        
	        /*
	        if (qaQuestionContentDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())) 
	        {
	            logger.debug("placing swapped content1");
	            qaQuestionContentDTO.setQuestion(replacedNode.getQuestion());
	            qaQuestionContentDTO.setDisplayOrder(replacedNode.getDisplayOrder());
	            qaQuestionContentDTO.setFeedback(replacedNode.getFeedback());
	        }
	        
	        if (qaQuestionContentDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString())) 
	        {
	            logger.debug("placing swapped content2");
	            qaQuestionContentDTO.setQuestion(mainNode.getQuestion());
	            qaQuestionContentDTO.setDisplayOrder(mainNode.getDisplayOrder());
	            qaQuestionContentDTO.setFeedback(mainNode.getFeedback());
	        }
	        */
	        
	        listFinalQuestionContentDTO.add(tempNode);
	    }

	    
	    
        logger.debug("final listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
        return listFinalQuestionContentDTO;
    }


    

    
    protected static QaQuestionContentDTO extractNodeAtDisplayOrder(List listQuestionContentDTO, int intOriginalQuestionIndex)
    {
        logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
        logger.debug("intOriginalQuestionIndex:" + intOriginalQuestionIndex);
        
        Iterator listIterator=listQuestionContentDTO.iterator();
        while (listIterator.hasNext())
        {
            QaQuestionContentDTO qaQuestionContentDTO=(QaQuestionContentDTO)listIterator.next();
            logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
            logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());
            
            logger.debug("intOriginalQuestionIndex versus displayOrder:" + new Integer(intOriginalQuestionIndex).toString() + " versus " + 
                    qaQuestionContentDTO.getDisplayOrder());
            if (new Integer(intOriginalQuestionIndex).toString().equals(qaQuestionContentDTO.getDisplayOrder()))
            {
                logger.debug("node found:" + qaQuestionContentDTO);
                return qaQuestionContentDTO;
            }
        }
        return null;
    }


    
    protected static Map extractMapQuestionContent(List listQuestionContentDTO)
    {
        logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
        Map mapQuestionContent= new TreeMap(new QaComparator());
        
        Iterator listIterator=listQuestionContentDTO.iterator();
        int queIndex=0;
        while (listIterator.hasNext())
        {
            QaQuestionContentDTO qaQuestionContentDTO=(QaQuestionContentDTO)listIterator.next();
            logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
            logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion()); 
        
            queIndex++;
            logger.debug("queIndex:" + queIndex);
            mapQuestionContent.put(new Integer(queIndex).toString(), qaQuestionContentDTO.getQuestion());
        }
        logger.debug("mapQuestionContent:" + mapQuestionContent);
        return mapQuestionContent;
    }

    protected static Map extractMapFeedback(List listQuestionContentDTO)
    {
        logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
        Map mapFeedbackContent= new TreeMap(new QaComparator());
        
        Iterator listIterator=listQuestionContentDTO.iterator();
        int queIndex=0;
        while (listIterator.hasNext())
        {
            QaQuestionContentDTO qaQuestionContentDTO=(QaQuestionContentDTO)listIterator.next();
            logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
            logger.debug("qaQuestionContentDTO feedback:" + qaQuestionContentDTO.getFeedback()); 
        
            queIndex++;
            logger.debug("queIndex:" + queIndex);
            mapFeedbackContent.put(new Integer(queIndex).toString(), qaQuestionContentDTO.getFeedback());
        }
        logger.debug("mapFeedbackContent:" + mapFeedbackContent);
        return mapFeedbackContent;
    }

    

    
    /**
     * reconstructQuestionContentMapForSubmit(TreeMap mapQuestionContent, HttpServletRequest request)
     * return void
     * 
    */
    protected  void reconstructQuestionContentMapForSubmit(Map mapQuestionContent, HttpServletRequest request)
    {
    	logger.debug("pre-submit Map:" + mapQuestionContent);
    	logger.debug("pre-submit Map size :" + mapQuestionContent.size());
    	
    	//repopulateMap(mapQuestionContent, request);
    	Map mapFinalQuestionContent = new TreeMap(new QaComparator());
    	
    	Iterator itMap = mapQuestionContent.entrySet().iterator();
	    while (itMap.hasNext()) {
	        Map.Entry pairs = (Map.Entry)itMap.next();
	        if ((pairs.getValue() != null) && (!pairs.getValue().equals("")))
    		{
	        	mapFinalQuestionContent.put(pairs.getKey(), pairs.getValue());
	        	logger.debug("adding the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
    		}
	    }
	    
	    mapQuestionContent=(TreeMap)mapFinalQuestionContent;
	    request.getSession().setAttribute("mapQuestionContent", mapQuestionContent);
	    logger.debug("final mapQuestionContent:" + mapQuestionContent);
    }

    
    protected  static Map reorderQuestionContentMap(Map mapQuestionContent)
    {
    	logger.debug("reorderQuestionContentMap:" + mapQuestionContent);
    	
    	Map mapFinalQuestionContent = new TreeMap(new QaComparator());
    	
    	int queIndex=0;
    	Iterator itMap = mapQuestionContent.entrySet().iterator();
	    while (itMap.hasNext()) {
	        Map.Entry pairs = (Map.Entry)itMap.next();
	        if ((pairs.getValue() != null) && (!pairs.getValue().equals("")))
    		{
	            ++queIndex;
	            logger.debug("using queIndex:" + queIndex);
	        	mapFinalQuestionContent.put(new Integer(queIndex).toString(), pairs.getValue());

    		}
	    }
	    
        logger.debug("final mapFinalQuestionContent:" + mapFinalQuestionContent);
        return mapFinalQuestionContent;
    }


    protected  static List reorderListQuestionContentDTO(List listQuestionContentDTO, String excludeQuestionIndex)
    {
        logger.debug("reorderListQuestionContentDTO");
    	logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
    	logger.debug("excludeQuestionIndex:" + excludeQuestionIndex);
    	
    	List listFinalQuestionContentDTO=new LinkedList();
    	
	    int queIndex=0;
	    Iterator listIterator=listQuestionContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        QaQuestionContentDTO qaQuestionContentDTO= (QaQuestionContentDTO)listIterator.next();
	        logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
	        logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());
	        
	        String question=qaQuestionContentDTO.getQuestion();
	        logger.debug("question:" + question);
	        
	        String displayOrder=qaQuestionContentDTO.getDisplayOrder();
	        logger.debug("displayOrder:" + displayOrder);
	        
	        String feedback=qaQuestionContentDTO.getFeedback();
	        logger.debug("feedback:" + feedback);
	        
	        logger.debug("displayOrder versus excludeQuestionIndex :" + displayOrder + " versus " + excludeQuestionIndex);
	        
	        if ((question != null) && (!question.equals("")))
    		{
	            if (!displayOrder.equals(excludeQuestionIndex))
	            {
		            ++queIndex;
		            logger.debug("using queIndex:" + queIndex);
		            
		            qaQuestionContentDTO.setQuestion(question);
		            qaQuestionContentDTO.setDisplayOrder(new Integer(queIndex).toString());
		            qaQuestionContentDTO.setFeedback(feedback);
		            
		            listFinalQuestionContentDTO.add(qaQuestionContentDTO);
	            }
    		}
	    }
	    
	    
        logger.debug("final listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
        return listFinalQuestionContentDTO;
    }

    
    protected  static List reorderSimpleListQuestionContentDTO(List listQuestionContentDTO)
    {
        logger.debug("reorderListQuestionContentDTO");
    	logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
    	List listFinalQuestionContentDTO=new LinkedList();
    	
	    int queIndex=0;
	    Iterator listIterator=listQuestionContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        QaQuestionContentDTO qaQuestionContentDTO= (QaQuestionContentDTO)listIterator.next();
	        logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
	        logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());
	        
	        String question=qaQuestionContentDTO.getQuestion();
	        logger.debug("question:" + question);
	        
	        String displayOrder=qaQuestionContentDTO.getDisplayOrder();
	        logger.debug("displayOrder:" + displayOrder);
	        
	        String feedback=qaQuestionContentDTO.getFeedback();
	        logger.debug("feedback:" + feedback);
	        
	        if ((question != null) && (!question.equals("")))
    		{
		            ++queIndex;
		            logger.debug("using queIndex:" + queIndex);
		            
		            qaQuestionContentDTO.setQuestion(question);
		            qaQuestionContentDTO.setDisplayOrder(new Integer(queIndex).toString());
		            qaQuestionContentDTO.setFeedback(feedback);
		            
		            listFinalQuestionContentDTO.add(qaQuestionContentDTO);
    		}
	    }
	    
	    
        logger.debug("final listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
        return listFinalQuestionContentDTO;
    }

    
    
    protected  static List reorderUpdateListQuestionContentDTO(List listQuestionContentDTO, 
            										QaQuestionContentDTO qaQuestionContentDTONew, 
            										String editableQuestionIndex)
    {
        logger.debug("reorderUpdateListQuestionContentDTO");
    	logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
    	logger.debug("qaQuestionContentDTONew:" + qaQuestionContentDTONew);
    	logger.debug("editableQuestionIndex:" + editableQuestionIndex);
    	
    	
    	List listFinalQuestionContentDTO=new LinkedList();
    	
	    int queIndex=0;
	    Iterator listIterator=listQuestionContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        QaQuestionContentDTO qaQuestionContentDTO= (QaQuestionContentDTO)listIterator.next();
	        logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
	        logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());
	        
	        ++queIndex;
	        logger.debug("using queIndex:" + queIndex);
	        String question=qaQuestionContentDTO.getQuestion();
	        logger.debug("question:" + question);
	        
	        String displayOrder=qaQuestionContentDTO.getDisplayOrder();
	        logger.debug("displayOrder:" + displayOrder);
	        
	        String feedback=qaQuestionContentDTO.getFeedback();
	        logger.debug("feedback:" + feedback);
	        
	        if (displayOrder.equals(editableQuestionIndex))
            {
	            logger.debug("displayOrder equals editableQuestionIndex:" + editableQuestionIndex);
		        qaQuestionContentDTO.setQuestion(qaQuestionContentDTONew.getQuestion());
	            qaQuestionContentDTO.setDisplayOrder(qaQuestionContentDTONew.getDisplayOrder());
	            qaQuestionContentDTO.setFeedback(qaQuestionContentDTONew.getFeedback());
	            
	            listFinalQuestionContentDTO.add(qaQuestionContentDTO);
	        }
	        else
	        {
	            logger.debug("displayOrder does not equal editableQuestionIndex:" + editableQuestionIndex);
	            qaQuestionContentDTO.setQuestion(question);
	            qaQuestionContentDTO.setDisplayOrder(displayOrder);
	            qaQuestionContentDTO.setFeedback(feedback);

	            listFinalQuestionContentDTO.add(qaQuestionContentDTO);
	            
	        }
	         
    	}
	    
	    logger.debug("listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
        return listFinalQuestionContentDTO;
    }
    
    
  
    
    /**
     * repopulateMap(TreeMap mapQuestionContent, HttpServletRequest request)
     * return void 
     * repopulates the user entries into the Map
     */
    protected void repopulateMap(Map mapQuestionContent, HttpServletRequest request)
    {
        logger.debug("starting repopulateMap");
        int intQuestionIndex= mapQuestionContent.size();
        logger.debug("intQuestionIndex: " + intQuestionIndex);

    	/* if there is data in the Map remaining from previous session remove those */
		mapQuestionContent.clear();
		logger.debug("Map got initialized: " + mapQuestionContent);
		
		for (long i=0; i < intQuestionIndex  ; i++)
		{
			String candidateQuestionEntry =request.getParameter("questionContent" + i);
			if (i==0)
    		{
    			logger.debug("defaultQuestionContent set to: " + candidateQuestionEntry);
    		}
			if ((candidateQuestionEntry != null) && (candidateQuestionEntry.length() > 0))
			{
				logger.debug("using key: " + i);
				mapQuestionContent.put(new Long(i+1).toString(), candidateQuestionEntry);
				logger.debug("added new entry.");	
			}
		}
    }


    public QaContent  saveOrUpdateQaContent(Map mapQuestionContent, Map mapFeedback, IQaService qaService, QaAuthoringForm qaAuthoringForm, 
            HttpServletRequest request, QaContent qaContent, String strToolContentID)
    {
        UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
        
        boolean isQuestionsSequenced=false;
        boolean isSynchInMonitor=false;
        boolean isUsernameVisible=false;

        String richTextTitle = request.getParameter(TITLE);
        String richTextInstructions = request.getParameter(INSTRUCTIONS);

        logger.debug("richTextTitle: " + richTextTitle);
        logger.debug("richTextInstructions: " + richTextInstructions);

		String synchInMonitor=request.getParameter(SYNC_IN_MONITOR);
		logger.debug("synchInMonitor: " + synchInMonitor);
		String usernameVisible=request.getParameter(USERNAME_VISIBLE);
		logger.debug("usernameVisible: " + usernameVisible);
		String questionsSequenced=request.getParameter(QUESTIONS_SEQUENCED);
		logger.debug("questionsSequenced: " + questionsSequenced);
		String richTextOfflineInstructions=request.getParameter(OFFLINE_INSTRUCTIONS);
		String richTextOnlineInstructions=request.getParameter(ONLINE_INSTRUCTIONS);
		String reflect=request.getParameter(REFLECT);
		logger.debug("reflect: " + reflect);
		
		String reflectionSubject=request.getParameter(REFLECTION_SUBJECT);
		logger.debug("reflectionSubject: " + reflectionSubject);
		
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
        
        
        boolean setCommonContent=true; 
        if ((questionsSequenced == null) || (synchInMonitor == null) || 
             (usernameVisible == null) || (reflect == null))
        {
        	setCommonContent=false;
        }
        logger.debug("setCommonContent: " + setCommonContent);
		
        boolean questionsSequencedBoolean=false;
        boolean synchInMonitorBoolean=false;
        boolean usernameVisibleBoolean=false;
        boolean reflectBoolean=false;

    	if ((questionsSequenced != null) && (questionsSequenced.equalsIgnoreCase("1")))
            questionsSequencedBoolean=true;            
        
        if ((synchInMonitor != null) && (synchInMonitor.equalsIgnoreCase("1")))
            synchInMonitorBoolean=true;            
        
        if ((usernameVisible != null) && (usernameVisible.equalsIgnoreCase("1")))
            usernameVisibleBoolean=true;            

        if ((reflect != null) && (reflect.equalsIgnoreCase("1")))
            reflectBoolean=true;
            
            
        logger.debug("questionsSequencedBoolean: " + questionsSequencedBoolean);
        logger.debug("synchInMonitorBoolean: " + synchInMonitorBoolean);
        logger.debug("usernameVisibleBoolean: " + usernameVisibleBoolean);
        logger.debug("reflectBoolean: " + reflectBoolean);
        
        long userId=0;
        if (toolUser != null)
        {
        	userId = toolUser.getUserID().longValue();	
        }
        else
        {
    		HttpSession ss = SessionManager.getSession();
    		logger.debug("ss: " + ss);
    		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
    		logger.debug("user" + user);
    		if (user != null)
    		{
    			userId = user.getUserID().longValue();	
    		}
    		else
    		{
    			logger.debug("should not reach here");
    			userId=0;
    		}
        }
        logger.debug("userId: " + userId);
     	logger.debug("qaContent: " + qaContent);
     	
     	boolean newContent=false;
        if(qaContent == null)
        {
        	qaContent = new QaContent();
        	newContent=true;
        }


    	logger.debug("setting common content values..." + richTextTitle + " " + richTextInstructions);
    	qaContent.setQaContentId(new Long(strToolContentID));
     	qaContent.setTitle(richTextTitle);
     	qaContent.setInstructions(richTextInstructions);
     	qaContent.setUpdateDate(new Date(System.currentTimeMillis())); /**keep updating this one*/
     	logger.debug("userId: " + userId);
     	qaContent.setCreatedBy(userId); /**make sure we are setting the userId from the User object above*/
     	logger.debug("end of setting common content values...");

     	

     	logger.debug("activeModule:" + activeModule);
     	if (activeModule.equals(AUTHORING))
		{
        	logger.debug("setting other content values...");
         	qaContent.setQuestionsSequenced(isQuestionsSequenced); 
         	qaContent.setSynchInMonitor(isSynchInMonitor);
         	qaContent.setOnlineInstructions(richTextOnlineInstructions);
         	qaContent.setOfflineInstructions(richTextOfflineInstructions);
         	qaContent.setUsernameVisible(usernameVisibleBoolean);
         	qaContent.setQuestionsSequenced(questionsSequencedBoolean);
         	qaContent.setSynchInMonitor(synchInMonitorBoolean);	
         	qaContent.setReflect(reflectBoolean);
         	qaContent.setReflectionSubject(reflectionSubject);
		}
        	
 
        if (newContent)
        {
        	logger.debug("will create: " + qaContent);
         	qaService.createQa(qaContent);
        }
        else
        {
        	logger.debug("will update: " + qaContent);
            qaService.updateQa(qaContent);
        }
        
        qaContent=qaService.loadQa(new Long(strToolContentID).longValue());
     	logger.debug("qaContent: " + qaContent);
        
        qaContent=createQuestionContent(mapQuestionContent, mapFeedback, qaService, qaContent);
        
        return qaContent;
    }
    
    
    /**
     * removes unused question entries from db
     * removeRedundantQuestions (Map mapQuestionContent, IQaService qaService, QaAuthoringForm qaAuthoringForm)
     * 
     * @param mapQuestionContent
     * @param qaService
     * @param qaAuthoringForm
     */
    public void removeRedundantQuestions (Map mapQuestionContent, IQaService qaService, QaAuthoringForm qaAuthoringForm, 
            HttpServletRequest request, String toolContentID)
	{
    	logger.debug("removing unused entries... ");
    	logger.debug("mapQuestionContent:  " + mapQuestionContent);
    	logger.debug("toolContentID:  " + toolContentID);
    	
    	QaContent qaContent=qaService.loadQa( new Long(toolContentID).longValue());
    	logger.debug("qaContent:  " + qaContent);
    	
    	if (qaContent != null)
    	{
        	logger.debug("qaContent uid: " + qaContent.getUid());
        	List allQuestions=qaService.getAllQuestionEntries(qaContent.getUid());
        	logger.debug("allQuestions: " + allQuestions);
        	
        	Iterator listIterator=allQuestions.iterator();
    		int mapIndex=0;
    		boolean entryUsed=false;
    		while (listIterator.hasNext())
    		{
	            ++mapIndex;
	            logger.debug("current mapIndex: " +  mapIndex);

    			QaQueContent queContent=(QaQueContent)listIterator.next();
    			logger.debug("queContent data: " + queContent);
    			logger.debug("queContent: " + queContent.getQuestion() + " " + queContent.getDisplayOrder());
    			
    			entryUsed=false;
    	        Iterator itMap = mapQuestionContent.entrySet().iterator();
    	        int displayOrder=0;
    	        while (itMap.hasNext()) 
    		    {
    	            ++displayOrder;
    	            logger.debug("current displayOrder: " +  displayOrder);
    	        	entryUsed=false;
    		        Map.Entry pairs = (Map.Entry)itMap.next();
    		        logger.debug("using the pair: " +  pairs.getKey() + " = " + pairs.getValue());
    		        
    		        if (pairs.getValue().toString().length() != 0)
    		        {
    		        	logger.debug("text from map:" + pairs.getValue().toString());
    		        	logger.debug("text from db:" + queContent.getQuestion());

		        	    logger.debug("mapIndex versus displayOrder:" + mapIndex + " versus " + displayOrder);
    		        	if (mapIndex == displayOrder)
    		        	{
    		        		//logger.debug("used entry in db:" + queContent.getQuestion());
    		        	    logger.debug("used displayOrder position:" + displayOrder);
    		        		entryUsed=true;
    		        		break;
    		        	}
    		        	
    		        }
    		    }
    	        
    	        if (entryUsed == false)
    	        {
    	        	logger.debug("removing unused entry in db:" + queContent.getQuestion());
    	        	
    	        	QaQueContent removeableQaQueContent=qaService.getQuestionContentByQuestionText(queContent.getQuestion(), qaContent.getUid());
        			logger.debug("removeableQaQueContent"  + removeableQaQueContent);
        			if (removeableQaQueContent != null)
        			{
        			    //qaContent.getQaQueContents().remove(removeableQaQueContent);
        				qaService.removeQaQueContent(removeableQaQueContent);
            			logger.debug("removed removeableQaQueContent from the db: " + removeableQaQueContent);	
        			}
    	        	
    	        }
    		}    		
    	}
	
	}
    
    /**
     * createQuestionContent(TreeMap mapQuestionContent, HttpServletRequest request)
     * return void
     * 
     * persist the questions in the Map the user has submitted 
     */
    protected QaContent createQuestionContent(Map mapQuestionContent, Map mapFeedback, IQaService qaService, QaContent qaContent)
    {    
        logger.debug("createQuestionContent: ");
        logger.debug("content uid is: " + qaContent.getUid());
        List questions=qaService.retrieveQaQueContentsByToolContentId(qaContent.getUid().longValue());
        logger.debug("questions: " + questions);
        
        logger.debug("mapQuestionContent: " + mapQuestionContent);
        logger.debug("mapFeedback: " + mapFeedback);

        
        Iterator itMap = mapQuestionContent.entrySet().iterator();
        int displayOrder=0;
        while (itMap.hasNext()) 
	    {
	        Map.Entry pairs = (Map.Entry)itMap.next();
	        logger.debug("using the pair: " +  pairs.getKey() + " = " + pairs.getValue());
	        
	        if (pairs.getValue().toString().length() != 0)
	        {
	        	logger.debug("starting createQuestionContent: pairs.getValue().toString():" + pairs.getValue().toString());
	        	logger.debug("starting createQuestionContent: qaContent: " + qaContent);
	        	
	        	++displayOrder;
	        	logger.debug("starting createQuestionContent: displayOrder: " + displayOrder);
	        	String currentFeedback=(String)mapFeedback.get(new Integer(displayOrder).toString());
	        	logger.debug("currentFeedback: " + currentFeedback);
	        	
	        	QaQueContent queContent=  new QaQueContent(pairs.getValue().toString(), 
		        											displayOrder,
		        											currentFeedback,
															qaContent,
															null,
															null);
		        
		        
	        	logger.debug("queContent: " + queContent);
	        	
			       /* checks if the question is already recorded*/
			       logger.debug("question text is: " + pairs.getValue().toString());
			       logger.debug("content uid is: " + qaContent.getUid());
			       logger.debug("question display order is: " + displayOrder);
			       QaQueContent existingQaQueContent=qaService.getQuestionContentByDisplayOrder(new Long(displayOrder), qaContent.getUid());
			       logger.debug("existingQaQueContent: " + existingQaQueContent);
			       
			       if (existingQaQueContent == null)
			       {
			       	/*make sure a question with the same question text is not already saved*/
			    	QaQueContent duplicateQaQueContent=qaService.getQuestionContentByQuestionText(pairs.getValue().toString(), qaContent.getUid());
			    	logger.debug("duplicateQaQueContent: " + duplicateQaQueContent);
				       	//if (duplicateQaQueContent == null)
				       	//{
				       		logger.debug("adding a new question to content: " + queContent);
				       		qaContent.getQaQueContents().add(queContent);
				       		queContent.setQaContent(qaContent);
		
				       		qaService.createQaQue(queContent);
				       	//}
			       }
			       else
			       {

				       String existingQuestion=existingQaQueContent.getQuestion(); 
				       logger.debug("existingQuestion: " + existingQuestion);
				       
				       logger.debug("map question versus existingQuestion: " + pairs.getValue().toString() + 
				               " versus db question value: " + existingQuestion);

			       		existingQaQueContent.setQuestion(pairs.getValue().toString());
			       		existingQaQueContent.setFeedback(currentFeedback);
			       		existingQaQueContent.setDisplayOrder(displayOrder);
			       		
			       		logger.debug("updating the existing question content: " + existingQaQueContent);
			       		qaService.updateQaQueContent(existingQaQueContent);
			       }
	        }      
	    }
        return qaContent;
    }
    
    
    public static boolean checkDuplicateQuestions(List listQuestionContentDTO, String newQuestion)
    {
        logger.debug("checkDuplicateQuestions: " + listQuestionContentDTO);
        logger.debug("newQuestion: " + newQuestion);
        
        Map mapQuestionContent=extractMapQuestionContent(listQuestionContentDTO);
        logger.debug("mapQuestionContent: " + mapQuestionContent);
        
    	Iterator itMap = mapQuestionContent.entrySet().iterator();
	    while (itMap.hasNext()) {
	        Map.Entry pairs = (Map.Entry)itMap.next();
	        if ((pairs.getValue() != null) && (!pairs.getValue().equals("")))
    		{
	        	logger.debug("checking the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
	        	
	        	if (pairs.getValue().equals(newQuestion))
	        	{
	        	    logger.debug("entry found: " +  newQuestion);
	        	    return true;
	        	}
    		}
	    }
	    return false;
    }
    

    
    
    /**
     * sorts the questions by the display order
     * reOrganizeDisplayOrder(Map mapQuestionContent, IQaService qaService, QaAuthoringForm qaAuthoringForm, QaContent qaContent)
     * 
     * @param mapQuestionContent
     * @param qaService
     * @param qaAuthoringForm
     * @param qaContent
     */
    public void reOrganizeDisplayOrder(Map mapQuestionContent, IQaService qaService, QaAuthoringForm qaAuthoringForm, QaContent qaContent)
    {    
        logger.debug("qaContent: " + qaContent);
        if (qaContent != null)
        {
        	logger.debug("content uid: " + qaContent.getUid());
        	List sortedQuestions=qaService.getAllQuestionEntriesSorted(qaContent.getUid().longValue());
        	logger.debug("sortedQuestions: " + sortedQuestions);
        	
    		Iterator listIterator=sortedQuestions.iterator();
    		int displayOrder=1;
    		while (listIterator.hasNext())
    		{
    			QaQueContent queContent=(QaQueContent)listIterator.next();
    			logger.debug("queContent data: " + queContent);
    			logger.debug("queContent: " + queContent.getQuestion() + " " + queContent.getDisplayOrder());
    			
    			QaQueContent existingQaQueContent=qaService.getQuestionContentByQuestionText(queContent.getQuestion(), qaContent.getUid());
    	    	logger.debug("existingQaQueContent: " + existingQaQueContent);
    	    	existingQaQueContent.setDisplayOrder(displayOrder);
    	    	logger.debug("updating the existing question content for displayOrder: " + existingQaQueContent);
           		qaService.updateQaQueContent(existingQaQueContent);
    			displayOrder++;
    		}
        }
		logger.debug("done with reOrganizeDisplayOrder...");
    }
    
    
    /**
     * checks if any entry is duplicate 
     * verifyDuplicatesOptionsMap(Map mapQuestions)
     * 
     * @param mapQuestions
     * @return
     */
    public static boolean verifyDuplicatesOptionsMap(Map mapQuestions)
	{
    	Map originalMap=mapQuestions;
    	Map backupMap=mapQuestions;
    	
    	int optionCount=0;
    	for (long i=1; i <= MAX_QUESTION_COUNT.longValue()  ; i++)
		{
    		String currentOption=(String)originalMap.get(new Long(i).toString());
    		
    		optionCount=0;
    		for (long j=1; j <= MAX_QUESTION_COUNT.longValue() ; j++)
    		{
        		String backedOption=(String)backupMap.get(new Long(j).toString());
        		
        		if ((currentOption != null) && (backedOption !=null))
        		{
        			if (currentOption.equals(backedOption))
    				{
    					optionCount++;
    				}
    				
            		if (optionCount > 1)
            			return false;	
        		}
    		}	
		}
    	return true;
	}
}