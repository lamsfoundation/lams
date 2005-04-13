/*
 * Created on Dec 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.type.Type;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;

/**
 * @author manpreet
 */
public class LearningDesignDAO extends BaseDAO implements ILearningDesignDAO {

	private static final String TABLENAME ="lams_learning_design";
	private static final String FIND_BY_USERID = "from " + TABLENAME +" in class " + LearningDesign.class.getName()+ " where user_id =?";
	
	private static final String VALID_IN_FOLDER ="from " + TABLENAME +" in class " + LearningDesign.class.getName()+
												 " where valid_design_flag=true AND workspace_folder_id=?";
	
	private static final String ALL_IN_FOLDER ="from " + TABLENAME +" in class " + LearningDesign.class.getName()+
												" where workspace_folder_id=?";
		
	
	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.ILearningDesignDAO#getLearningDesignById(java.lang.Long)
	 */
	public LearningDesign getLearningDesignById(Long learningDesignId) {
		return (LearningDesign)super.find(LearningDesign.class,learningDesignId);	
	}

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.ILearningDesignDAO#getLearningDesignByTitle(java.lang.String)
	 */
	public LearningDesign getLearningDesignByTitle(String title) {
			return (LearningDesign) super.find(LearningDesign.class,title);
	}

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.ILearningDesignDAO#getAllLearningDesigns()
	 */
	public List getAllLearningDesigns() {
		return super.findAll(LearningDesign.class);		
	}
	
	public List getLearningDesignByUserId(Long userID){		
		try{
			return this.getSession().find(FIND_BY_USERID,new Object[]{userID}, new Type[]{Hibernate.LONG});
		}catch(HibernateException he){
			return null;
			/*TODO Exception Handling*/
		}		
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO#getAllValidLearningDesignsInFolder(java.lang.Integer)
	 */
	public List getAllValidLearningDesignsInFolder(Integer workspaceFolderID) {		
		return this.getHibernateTemplate().find(VALID_IN_FOLDER,workspaceFolderID);		
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO#getAllLearningDesignsInFolder(java.lang.Integer)
	 */
	public List getAllLearningDesignsInFolder(Integer workspaceFolderID) {
		return this.getHibernateTemplate().find(ALL_IN_FOLDER,workspaceFolderID);
	}
	
}
