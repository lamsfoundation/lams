/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.authoring.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.LearningDesignValidator;
import org.lamsfoundation.lams.authoring.ObjectExtractor;
import org.lamsfoundation.lams.authoring.ObjectExtractorException;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityOrderComparator;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.License;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupingDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningLibraryDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LicenseDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.TransitionDAO;
import org.lamsfoundation.lams.learningdesign.dto.DesignDetailDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.themes.dao.ICSSThemeDAO;
import org.lamsfoundation.lams.themes.dto.CSSThemeBriefDTO;
import org.lamsfoundation.lams.themes.dto.CSSThemeDTO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolContentIDGenerator;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.UserDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.WorkspaceFolderDAO;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;

import com.allaire.wddx.WddxDeserializationException;


/**
 * @author Manpreet Minhas 
 */
public class AuthoringService implements IAuthoringService {
	
	protected Logger log = Logger.getLogger(AuthoringService.class);	

	/** Required DAO's */
	protected LearningDesignDAO learningDesignDAO;
	protected LearningLibraryDAO learningLibraryDAO;
	protected ActivityDAO activityDAO;
	protected UserDAO userDAO;
	protected WorkspaceFolderDAO workspaceFolderDAO;
	protected TransitionDAO transitionDAO;
	protected ToolDAO toolDAO;
	protected LicenseDAO licenseDAO;
	protected GroupingDAO groupingDAO;
	protected GroupDAO groupDAO;
	protected ICSSThemeDAO themeDAO;
	protected MessageService messageService;
	
	protected ToolContentIDGenerator contentIDGenerator;
	
	public AuthoringService(){
		
	}
	
	/**********************************************
	 * Setter Methods
	 * *******************************************/
	/**
	 * Set i18n MessageService
	 */
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	/**
	 * @param groupDAO The groupDAO to set.
	 */
	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}
	public void setGroupingDAO(GroupingDAO groupingDAO) {
		this.groupingDAO = groupingDAO;
	}
	/** for sending acknowledgment/error messages back to flash */
	private FlashMessage flashMessage;
	
	/**
	 * @param transitionDAO The transitionDAO  to set
	 */
	public void setTransitionDAO(TransitionDAO transitionDAO) {
		this.transitionDAO = transitionDAO;
	}
	/**
	 * @param learningDesignDAO The learningDesignDAO to set.
	 */
	public void setLearningDesignDAO(LearningDesignDAO learningDesignDAO) {
		this.learningDesignDAO = learningDesignDAO;
	}	
	/**
	 * @param learningLibraryDAO The learningLibraryDAO to set.
	 */
	public void setLearningLibraryDAO(LearningLibraryDAO learningLibraryDAO) {
		this.learningLibraryDAO = learningLibraryDAO;
	}
	/**
	 * @param userDAO The userDAO to set.
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	/**
	 * @param activityDAO The activityDAO to set.
	 */
	public void setActivityDAO(ActivityDAO activityDAO) {
		this.activityDAO = activityDAO;
	}	
	/**
	 * @param workspaceFolderDAO The workspaceFolderDAO to set.
	 */
	public void setWorkspaceFolderDAO(WorkspaceFolderDAO workspaceFolderDAO) {
		this.workspaceFolderDAO = workspaceFolderDAO;
	}
	/**
	 * @param toolDAO The toolDAO to set 
	 */
	public void setToolDAO(ToolDAO toolDAO) {
		this.toolDAO = toolDAO;
	}
	/**
	 * @param licenseDAO The licenseDAO to set
	 */
	public void setLicenseDAO(LicenseDAO licenseDAO) {
		this.licenseDAO = licenseDAO;
	}	
	
    /**
     * @return Returns the themeDAO.
     */
    public ICSSThemeDAO getThemeDAO() {
        return themeDAO;
    }
    /**
     * @param themeDAO The ICSSThemeDAO to set.
     */
    public void setThemeDAO(ICSSThemeDAO themeDAO) {
        this.themeDAO = themeDAO;
    }
    
    /**
     * @param contentIDGenerator The contentIDGenerator to set.
     */
    public void setContentIDGenerator(ToolContentIDGenerator contentIDGenerator)
    {
        this.contentIDGenerator = contentIDGenerator;
    }
    
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getLearningDesign(java.lang.Long)
	 */
	public LearningDesign getLearningDesign(Long learningDesignID){
		return learningDesignDAO.getLearningDesignById(learningDesignID);
	}
	
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#saveLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign)
	 */
	public void saveLearningDesign(LearningDesign learningDesign){
		learningDesignDAO.insert(learningDesign);
	}
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningDesigns()
	 */
	public List getAllLearningDesigns(){
		return learningDesignDAO.getAllLearningDesigns();		
	}
	
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#updateLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign)
	 */
	public void updateLearningDesign(LearningDesign learningDesign) {		
		learningDesignDAO.update(learningDesign);
	}
	
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningLibraries()
	 */
	public List getAllLearningLibraries(){
		return learningLibraryDAO.getAllLearningLibraries();		
	}
	
	/**********************************************
	 * Utility/Service Methods
	 * *******************************************/
	
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getLearningDesignDetails(java.lang.Long)
	 */
	public String getLearningDesignDetails(Long learningDesignID)throws IOException{
		String wddxPacket = null;
		LearningDesign design = learningDesignDAO.getLearningDesignById(learningDesignID);
		if(design==null)
			flashMessage = FlashMessage.getNoSuchLearningDesignExists("getLearningDesignDetails",learningDesignID);
		else{
			LearningDesignDTO learningDesignDTO = design.getLearningDesignDTO();
			flashMessage = new FlashMessage("getLearningDesignDetails",learningDesignDTO);
		}
		return flashMessage.serializeMessage();
	}	
	public LearningDesign copyLearningDesign(Long originalDesignID,Integer copyType,
									Integer userID, Integer workspaceFolderID, boolean setOriginalDesign) 
																	throws UserException, LearningDesignException, 
											 							      WorkspaceFolderException, IOException{
		
		LearningDesign originalDesign = learningDesignDAO.getLearningDesignById(originalDesignID);
		if(originalDesign==null)
			throw new LearningDesignException(messageService.getMessage("no.such.learningdesign.exist",new Object[]{originalDesignID}));
		
		User user = userDAO.getUserById(userID);
		if(user==null)
			throw new UserException(messageService.getMessage("no.such.user.exist",new Object[]{userID}));
		
		WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(workspaceFolderID);
		if(workspaceFolder==null)
			throw new WorkspaceFolderException(messageService.getMessage("no.such.workspace.exist",new Object[]{workspaceFolderID}));
		
		return copyLearningDesign(originalDesign,copyType,user,workspaceFolder, setOriginalDesign);
	}
	
    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign, java.lang.Integer, org.lamsfoundation.lams.usermanagement.User, org.lamsfoundation.lams.usermanagement.WorkspaceFolder)
     */
    public LearningDesign copyLearningDesign(LearningDesign originalLearningDesign,Integer copyType,User user, WorkspaceFolder workspaceFolder, boolean setOriginalDesign)
    {
    	LearningDesign newLearningDesign  = LearningDesign.createLearningDesignCopy(originalLearningDesign,copyType, setOriginalDesign);
    	newLearningDesign.setUser(user);    	
    	newLearningDesign.setWorkspaceFolder(workspaceFolder);    	
    	learningDesignDAO.insert(newLearningDesign);
    	updateDesignActivities(originalLearningDesign,newLearningDesign); 
    	calculateFirstActivity(originalLearningDesign,newLearningDesign);
    	updateDesignTransitions(originalLearningDesign,newLearningDesign);
        return newLearningDesign;
    }
    
    /**
     * Calculates the First activity of the LearningDesign.
     * <p> 
     * The <em>activity_ui_id</em> is unique per LearningDesign. So when a LearningDesign is deep-copied
     * all the activities in the newDesign would have the same <em>activity_ui_id</em> as the oldDesign.  
     * This mean that the firstActivity of the newDesign would have the same <em>activity_ui_id</em>
     * as the oldDesign.So in order to determine the firstActivity of the newDesign we look for an 
     * activity which has an <em>activity_ui_id</em> same as that of the oldDesign and 
     * <em>learning_design_id</em> as the newDesign
     * </p>
     *   
     * @param oldDesign The LearningDesign to be copied
     * @param newDesign The copy of the originalLearningDesign
     */
    private void calculateFirstActivity(LearningDesign oldDesign,LearningDesign newDesign){
    	Integer oldUIID  = oldDesign.getFirstActivity().getActivityUIID();
    	Activity firstActivity = activityDAO.getActivityByUIID(oldUIID,newDesign);
    	newDesign.setFirstActivity(firstActivity);
    	Integer learning_design_ui_id = newDesign.getLearningDesignUIID();
    	newDesign.setLearningDesignUIID(learning_design_ui_id);
    }
    
    /**
     * Updates the Activity information in the newLearningDesign based 
     * on the originalLearningDesign
     * 
     * @param originalLearningDesign The LearningDesign to be copied
     * @param newLearningDesign The copy of the originalLearningDesign
     */
    private void updateDesignActivities(LearningDesign originalLearningDesign, LearningDesign newLearningDesign){
    	HashSet newActivities = new HashSet();    	
    	TreeSet oldParentActivities = new TreeSet(new ActivityOrderComparator());
    	oldParentActivities.addAll(originalLearningDesign.getParentActivities());    	    	
    	Iterator iterator = oldParentActivities.iterator();    	
    	while(iterator.hasNext()){
    		Activity parentActivity = (Activity)iterator.next();
    		Activity newParentActivity = getActivityCopy(parentActivity);
    		newParentActivity.setLearningDesign(newLearningDesign);
    		activityDAO.insert(newParentActivity);
    		newActivities.add(newParentActivity);
    		
    		TreeSet oldChildActivities = new TreeSet(new ActivityOrderComparator());
    		oldChildActivities.addAll(getChildActivities((Activity)parentActivity));    		
    		Iterator childIterator = oldChildActivities.iterator();
    		
    		while(childIterator.hasNext()){
    			Activity childActivity = (Activity)childIterator.next();
    			Activity newChildActivity = getActivityCopy(childActivity);
    			newChildActivity.setParentActivity(newParentActivity);
    			newChildActivity.setParentUIID(newParentActivity.getActivityUIID());
    			newChildActivity.setLearningDesign(newLearningDesign);
    			activityDAO.insert(newChildActivity);
    			newActivities.add(newChildActivity);
    		}
    	}
    	newLearningDesign.setActivities(newActivities);
    }
    
    /**
     * Updates the Transition information in the newLearningDesign based 
     * on the originalLearningDesign
     * 
     * @param originalLearningDesign The LearningDesign to be copied 
     * @param newLearningDesign The copy of the originalLearningDesign
     */
    public void updateDesignTransitions(LearningDesign originalLearningDesign, LearningDesign newLearningDesign){
    	HashSet newTransitions = new HashSet();
    	Set oldTransitions = originalLearningDesign.getTransitions();
    	Iterator iterator = oldTransitions.iterator();
    	while(iterator.hasNext()){
    		Transition transition = (Transition)iterator.next();
    		Transition newTransition = Transition.createCopy(transition);    		
    		Activity toActivity = null;
        	Activity fromActivity=null;
    		if(newTransition.getToUIID()!=null)
    			toActivity = activityDAO.getActivityByUIID(newTransition.getToUIID(),newLearningDesign);
    		if(newTransition.getFromUIID()!=null)
    			fromActivity = activityDAO.getActivityByUIID(newTransition.getFromUIID(),newLearningDesign);
    		newTransition.setToActivity(toActivity);
    		newTransition.setFromActivity(fromActivity);
    		newTransition.setLearningDesign(newLearningDesign);
    		transitionDAO.insert(newTransition);
    		newTransitions.add(newTransition);
    	}
    	newLearningDesign.setTransitions(newTransitions);
    }
    /**
     * Determines the type of activity and returns a deep-copy of the same
     * 
     * @param activity The object to be deep-copied
     * @return Activity The new deep-copied Activity object
     */
    private Activity getActivityCopy(Activity activity){
    	if ( Activity.GROUPING_ACTIVITY_TYPE == activity.getActivityTypeId().intValue() ) {
    		GroupingActivity newGroupingActivity = (GroupingActivity) activity.createCopy();
    		// now we need to manually add the grouping to the session, as we can't easily
    		// set up a cascade
    		Grouping grouping = newGroupingActivity.getCreateGrouping();
    		if ( grouping != null )
    			groupingDAO.insert(grouping);
    		return newGroupingActivity;
    	}
    	else 
    		return activity.createCopy();    	
    } 
    /**
     * Returns a set of child activities for the given parent activitity
     * 
     * @param parentActivity The parent activity 
     * @return HashSet Set of the activities that belong to the parentActivity 
     */
    private HashSet getChildActivities(Activity parentActivity){
    	HashSet childActivities = new HashSet();
    	List list = activityDAO.getActivitiesByParentActivityId(parentActivity.getActivityId());
    	if(list!=null)
    		childActivities.addAll(list);
    	return childActivities;
    }		
	/**
	 * This method saves a new Learning Design to the database.
	 * It received a WDDX packet from flash, deserializes it
	 * and then finally persists it to the database.
	 * 
	 * @param wddxPacket The WDDX packet received from Flash
	 * @return String The acknowledgement in WDDX format that the design has been
	 * 				  successfully saved.
	 * @throws Exception
	 */
	public String storeLearningDesignDetails(String wddxPacket) throws Exception{
		LearningDesignDTO learningDesignDTO = null;
		
		Hashtable table = (Hashtable)WDDXProcessor.deserialize(wddxPacket);
		ObjectExtractor extractor = new ObjectExtractor(userDAO,learningDesignDAO,
														activityDAO,workspaceFolderDAO,
														learningLibraryDAO,licenseDAO,
														groupingDAO,toolDAO,groupDAO,transitionDAO);
		
		try { 
			LearningDesign design = extractor.extractLearningDesign(table);	
			learningDesignDAO.insert(design);
			LearningDesignValidator validator = new LearningDesignValidator(learningDesignDAO, messageService);
			flashMessage = (FlashMessage)validator.validateLearningDesign(design);
			
			
			//flashMessage = new FlashMessage(IAuthoringService.STORE_LD_MESSAGE_KEY,design.getLearningDesignId());
		} catch ( ObjectExtractorException e ) {
			flashMessage = new FlashMessage(IAuthoringService.STORE_LD_MESSAGE_KEY,
											messageService.getMessage("invalid.wddx.packet",new Object[]{e.getMessage()}),
											FlashMessage.ERROR);
		}
	
		return flashMessage.serializeMessage(); 		
	}
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningDesignDetails()
	 */
	public String getAllLearningDesignDetails()throws IOException{
		Iterator iterator= getAllLearningDesigns().iterator();
		ArrayList arrayList = createDesignDetailsPacket(iterator);
		flashMessage = new FlashMessage("getAllLearningDesignDetails",arrayList);		
		return flashMessage.serializeMessage();
	}
	/**
	 * This is a utility method used by the method 
	 * <code>getAllLearningDesignDetails</code> to pack the 
	 * required information in a data transfer object.
	 * 	  
	 * @param iterator 
	 * @return Hashtable The required information in a Hashtable
	 */
	private ArrayList createDesignDetailsPacket(Iterator iterator){
	    ArrayList arrayList = new ArrayList();
		while(iterator.hasNext()){
			LearningDesign learningDesign = (LearningDesign)iterator.next();
			DesignDetailDTO designDetailDTO = learningDesign.getDesignDetailDTO();
			arrayList.add(designDetailDTO);
		}
		return arrayList;
	}
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getLearningDesignsForUser(java.lang.Long)
	 */
	public String getLearningDesignsForUser(Long userID) throws IOException{
		List list = learningDesignDAO.getLearningDesignByUserId(userID);
		ArrayList arrayList = createDesignDetailsPacket(list.iterator());
		flashMessage = new FlashMessage("getLearningDesignsForUser",arrayList);
		return flashMessage.serializeMessage();
	}	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningLibraryDetails()
	 */
	public String getAllLearningLibraryDetails()throws IOException{
		Iterator iterator= getAllLearningLibraries().iterator();
		ArrayList libraries = new ArrayList();
		while(iterator.hasNext()){
			LearningLibrary learningLibrary = (LearningLibrary)iterator.next();		
			List templateActivities = activityDAO.getActivitiesByLibraryID(learningLibrary.getLearningLibraryId());
			
			if (templateActivities!=null & templateActivities.size()==0)
			{
				log.error("Learning Library with ID " + learningLibrary.getLearningLibraryId() + " does not have a template activity");
			}
			libraries.add(learningLibrary.getLearningLibraryDTO(templateActivities));
			//libraries.add(learningLibrary.getLearningLibraryDTO());
		}
		flashMessage = new FlashMessage("getAllLearningLibraryDetails",libraries);
		return flashMessage.serializeMessage();
	}
	
	
	/**
	 * Store a theme created on a client.
	 * @param wddxPacket The WDDX packet received from Flash
	 * @return String The acknowledgement in WDDX format that the theme has been
	 * 				  successfully saved.
	 * @throws IOException
	 * @throws WddxDeserializationException
	 * @throws Exception
	 */
	public String storeTheme(String wddxPacket) throws Exception {
		
		Hashtable table = (Hashtable)WDDXProcessor.deserialize(wddxPacket);
		
		CSSThemeDTO themeDTO = new CSSThemeDTO(table);
		if ( log.isDebugEnabled() ) {
		    log.debug("Converted Theme packet. Packet was \n"+wddxPacket+
		            "\nDTO is\n"+themeDTO);
		}

		CSSThemeVisualElement dbTheme = null;
		CSSThemeVisualElement storedTheme = null;
		if ( themeDTO.getId() != null )  {
		    // Flash has supplied an id, get the record from the database for update
		    dbTheme = themeDAO.getThemeById(themeDTO.getId());
		}
		
		if ( dbTheme == null ) {
		    storedTheme = themeDTO.createCSSThemeVisualElement();
		} else {
		    storedTheme = themeDTO.updateCSSTheme(dbTheme);
		}
		
		themeDAO.saveOrUpdateTheme(storedTheme);
		flashMessage = new FlashMessage(IAuthoringService.STORE_THEME_MESSAGE_KEY,storedTheme.getId());
		return flashMessage.serializeMessage(); 		
	}

	/**
	 * Returns a string representing the requested theme in WDDX format
	 * 
	 * @param learningDesignID The learning_design_id of the design whose WDDX packet is requested 
	 * @return String The requested LearningDesign in WDDX format
	 * @throws Exception
	 */
	public String getTheme(Long themeId)throws IOException {
	    CSSThemeVisualElement theme = themeDAO.getThemeById(themeId);
		if(theme==null)
			flashMessage = FlashMessage.getNoSuchTheme("wddxPacket",themeId);
		else{
			CSSThemeDTO dto = new CSSThemeDTO(theme);
			flashMessage = new FlashMessage("getTheme",dto);
		}
		return flashMessage.serializeMessage();
	}


	/**
	 * This method returns a list of all available themes in
	 * WDDX format. We need to work out if this should be restricted
	 * by user.
	 * 
	 * @return String The required information in WDDX format
	 * @throws IOException
	 */
	public String getThemes() throws IOException {
	    List themes = themeDAO.getAllThemes();
		ArrayList themeList = new ArrayList();
		Iterator iterator = themes.iterator();
		while(iterator.hasNext()){
		    CSSThemeBriefDTO dto = new CSSThemeBriefDTO((CSSThemeVisualElement)iterator.next());
		    themeList.add(dto);
		}
		flashMessage = new FlashMessage("getThemes",themeList);		
		return flashMessage.serializeMessage();
	}
	
	
	/** @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getToolContentID(java.lang.Long) */

	public String getToolContentID(Long toolID) throws IOException
	{
	   Tool tool = toolDAO.getToolByID(toolID);
	   if (tool == null)
	   {
	       log.error("The toolID "+ toolID + " is not valid. A Tool with tool id " + toolID + " does not exist on the database.");
	       return FlashMessage.getNoSuchTool("getToolContentID", toolID).serializeMessage();
	   }
	   
	   Long newContentID = contentIDGenerator.getNextToolContentIDFor(tool);
	   flashMessage = new FlashMessage("getToolContentID", newContentID);
	   
	   return flashMessage.serializeMessage();
	}
	
	/** @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAvailableLicenses() */
	public Vector getAvailableLicenses() {
		List licenses = licenseDAO.findAll(License.class);
		Vector licenseDTOList = new Vector(licenses.size());
		Iterator iter = licenses.iterator(); 
		while ( iter.hasNext() ) {
			License element = (License) iter.next();
			licenseDTOList.add(element.getLicenseDTO(Configuration.get(ConfigurationKeys.SERVER_URL)));
		}
		return licenseDTOList;
	}

	
}