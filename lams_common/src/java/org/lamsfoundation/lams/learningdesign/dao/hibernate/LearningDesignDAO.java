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
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;

/**
 * @author Manpreet Minhas
 */
public class LearningDesignDAO extends BaseDAO implements ILearningDesignDAO {

	private static final String TABLENAME ="lams_learning_design";
	private static final String FIND_BY_USERID = "from " + TABLENAME +" in class " + LearningDesign.class.getName()+ " where user_id =?";
	
	private static final String VALID_IN_FOLDER ="from " + TABLENAME +" in class " + LearningDesign.class.getName()+
												 " where valid_design_flag=true AND workspace_folder_id=?";
	
	private static final String ALL_IN_FOLDER ="from " + TABLENAME +" in class " + LearningDesign.class.getName()+
												" where workspace_folder_id=?";
	private static final String FIND_BY_PARENT ="from " + TABLENAME +" in class " + LearningDesign.class.getName()+
												" where parent_learning_design_id=?";
		
	
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
		if ( userID != null ) {
			try{
				Query query = this.getSession().createQuery(FIND_BY_USERID);
				query.setLong(0,userID.longValue());
				return query.list();
			}catch(HibernateException he){
			}		
		}
		return null;			
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
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO#getLearningDesignsByParent(java.lang.Long)
	 */
	public List getLearningDesignsByParent(Long parentDesignID){
		List list = this.getHibernateTemplate().find(FIND_BY_PARENT,parentDesignID);
		return list;
	}
	
}
