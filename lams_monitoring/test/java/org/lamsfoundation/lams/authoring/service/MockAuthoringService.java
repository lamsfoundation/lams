/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 9/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.authoring.service;

import java.io.IOException;
import java.util.List;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.workspace.exception.WorkspaceFolderException;


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
        return learningDesignDAO.getLearningDesignById(arg0);
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyLearningDesign(java.lang.Long)
     */
    public LearningDesign copyLearningDesign(LearningDesign learningDesign)
    {
        return learningDesign;
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
    public void updateLearningDesign(LearningDesign learningDesign)
    {
        learningDesignDAO.update(learningDesign);
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

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign, java.lang.Integer, org.lamsfoundation.lams.usermanagement.User, org.lamsfoundation.lams.usermanagement.WorkspaceFolder)
     */
    public LearningDesign copyLearningDesign(LearningDesign learningDesign, Integer arg1, User arg2, WorkspaceFolder arg3)
    {
        return learningDesign;
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#requestLearningLibraryWDDX(java.lang.Long)
     */
    public String requestLearningLibraryWDDX(Long arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#requestLearningLibraryListWDDX()
     */
    public String requestLearningLibraryListWDDX() throws IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#storeWDDXData(java.lang.String)
     */
    public String storeWDDXData(String arg0) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign, java.lang.Integer, org.lamsfoundation.lams.usermanagement.User)
	 */
	public LearningDesign copyLearningDesign(LearningDesign originalLearningDesign, Integer copyType, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyLearningDesign(java.lang.Long, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public String copyLearningDesign(Long originalLearningDesignID, Integer copyType, Integer userID, Integer workspaceFolder) throws UserException, LearningDesignException, WorkspaceFolderException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getLearningDesignDetails(java.lang.Long)
	 */
	public String getLearningDesignDetails(Long learningDesignID) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#storeLearningDesignDetails(java.lang.String)
	 */
	public String storeLearningDesignDetails(String wddxPacket) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningDesignDetails()
	 */
	public String getAllLearningDesignDetails() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getLearningDesignsForUser(java.lang.Long)
	 */
	public String getLearningDesignsForUser(Long userID) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningLibraryDetails()
	 */
	public String getAllLearningLibraryDetails() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
