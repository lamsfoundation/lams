/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 9/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.authoring.service;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.usermanagement.User;


/**
 * 
 * @author Jacky Fang 9/02/2005
 * 
 */
public class MockAuthoringService implements IAuthoringService
{
	protected LearningDesignDAO learningDesignDAO;
	
	/**
	 * @param learningDesignDAO The learningDesignDAO to set.
	 */
	public void setLearningDesignDAO(LearningDesignDAO learningDesignDAO) {
		this.learningDesignDAO = learningDesignDAO;
	}

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getLearningDesign(java.lang.Long)
     */
    public LearningDesign getLearningDesign(Long arg0)
    {
        return null;
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyLearningDesign(java.lang.Long)
     */
    public LearningDesign copyLearningDesign(Long learningDesignId)
    {
        return learningDesignDAO.getLearningDesignById(new Long(1));
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningDesigns()
     */
    public List getAllLearningDesigns()
    {
        return null;
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#saveLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign)
     */
    public void saveLearningDesign(LearningDesign arg0)
    {
        // TODO Auto-generated method stub
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#updateLearningDesign(java.lang.Long)
     */
    public void updateLearningDesign(Long arg0)
    {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningLibraries()
     */
    public List getAllLearningLibraries()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#requestLearningLibraryWDDX()
     */
    public String requestLearningLibraryWDDX()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#requestLearningDesignWDDX(java.lang.Long)
     */
    public String requestLearningDesignWDDX(Long arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#requestLearningDesignListWDDX()
     */
    public String requestLearningDesignListWDDX()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#requestLearningDesignWDDX(org.lamsfoundation.lams.usermanagement.User)
     */
    public String requestLearningDesignWDDX(User arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
