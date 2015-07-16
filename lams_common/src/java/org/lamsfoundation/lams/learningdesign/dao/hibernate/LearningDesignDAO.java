/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignAccess;
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

        private static final String COUNT_VALID_IN_FOLDER = "SELECT COUNT(*) from " + TABLENAME
        	    + " where valid_design_flag=true AND workspace_folder_id=?";
        
            private static final String COUNT_ALL_IN_FOLDER = "SELECT COUNT(*) from " + TABLENAME + " where workspace_folder_id=?";
        

	private static final String FIND_BY_ORIGINAL ="from " + TABLENAME +" in class " + LearningDesign.class.getName()+
												" where original_learning_design_id=?";
	private static final String FIND_LD_NAMES_IN_FOLDER = "select title from " + LearningDesign.class.getName()+
												" where workspace_folder_id=? and title like ?";
	
	private static final String ACCESS_BY_USER = "from " + LearningDesignAccess.class.getName()
	    + " as a where a.userId = ? order by a.accessDate desc";

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
	 * @see getLearningDesignsByOriginalDesign#getLearningDesignsByParent(java.lang.Long)
	 */
	public List getLearningDesignsByOriginalDesign(Long originalDesignID){
		List list = this.getHibernateTemplate().find(FIND_BY_ORIGINAL,originalDesignID);
		return list;
	}
	
	/**
	 * (non-Javadoc)
	 * @see  org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO#getLearningDesignTitlesByWorkspaceFolder(java.lang.Integer)
	 */
	public List getLearningDesignTitlesByWorkspaceFolder(Integer workspaceFolderID, String prefix){
		return this.getHibernateTemplate().find(FIND_LD_NAMES_IN_FOLDER,
    			new Object[] { workspaceFolderID, prefix + "%" });
        }
    
        @SuppressWarnings("unchecked")
        @Override
        public List<LearningDesignAccess> getAccessByUser(Integer userId) {
    		return this.getHibernateTemplate().find(ACCESS_BY_USER, userId);
        }
        
    	@SuppressWarnings("unchecked")
	public List<LearningDesign> getAllPagedLearningDesigns(Integer workspaceFolderID, Integer page, Integer size, String sortName, String sortDate) {
            String sortingOrder = setupSortString(sortName, sortDate);
            Query query = getSession().createQuery(ALL_IN_FOLDER + sortingOrder)
        	    .setParameter(0, workspaceFolderID.longValue());
    	    if ( page != null && size != null )
    		query.setFirstResult(page * size).setMaxResults(size);
    		
    	    return (List<LearningDesign>) query.list();
   	}

        @SuppressWarnings("unchecked")
	public List<LearningDesign> getValidPagedLearningDesigns(Integer workspaceFolderID, Integer page, Integer size, String sortName, String sortDate) {
            String sortingOrder = setupSortString(sortName, sortDate);
            Query query = getSession().createQuery(VALID_IN_FOLDER + sortingOrder)
        	    .setParameter(0, workspaceFolderID.longValue());
    	    if ( page != null && size != null )
    		query.setFirstResult(page * size).setMaxResults(size);
    		
    	    return (List<LearningDesign>) query.list();
        }

	private String setupSortString(String sortName, String sortDate) {
            if ( sortName != null && sortDate != null ) {
        	return " order by title "+sortName +", last_modified_date_time "+sortDate;
            } else if ( sortDate != null ) {
        	return " order by last_modified_date_time "+sortDate;
            } else {
        	// default to sorting by name
        	return " order by title "+ (sortName!=null ? sortName : "ASC");
            }
	}

        public long countAllLearningDesigns(Integer workspaceFolderID, boolean validDesignsOnly) {
            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put("workspaceFolder.workspaceFolderId", workspaceFolderID);
            if ( validDesignsOnly ) 
        	properties.put("validDesign", Boolean.valueOf(validDesignsOnly));
            return countByProperties(LearningDesign.class, properties);
        }

    }