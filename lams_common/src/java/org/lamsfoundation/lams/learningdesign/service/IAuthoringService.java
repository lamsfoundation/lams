/*
 * Created on Dec 7, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.service;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author MMINHAS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IAuthoringService {
	
	public LearningDesign getLearningDesign(Long learningDesignID);
	public List getAllLearningDesigns();
	public void saveLearningDesign(LearningDesign learningDesign);
	public void updateLearningDesign(Long learningDesignID);
	public List getAllLearningLibraries();
	public String requestLearningLibraryWDDX();
	public String requestLearningDesignWDDX(Long learningDesignID);
	public String requestLearningDesignListWDDX();
	public String requestLearningDesignWDDX(User user);	

}
