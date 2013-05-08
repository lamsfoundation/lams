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
package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaUploadedFile;
import org.lamsfoundation.lams.tool.qa.dao.IQaUploadedFileDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 * 
 */

public class QaUploadedFileDAO extends HibernateDaoSupport implements IQaUploadedFileDAO {
    private static final String GET_UPLOADED_FILES = "from QaUploadedFile qaUploadedFile where qaUploadedFile.qaContent.qaContentId = :contentId";

    public QaUploadedFile getUploadedFileById(long submissionId) {
	return (QaUploadedFile) this.getHibernateTemplate().load(QaUploadedFile.class, new Long(submissionId));
    }

    /**
     * 
     * return null if not found
     */
    public QaUploadedFile loadUploadedFileById(long submissionId) {
	return (QaUploadedFile) this.getHibernateTemplate().get(QaUploadedFile.class, new Long(submissionId));
    }

    public void updateUploadFile(QaUploadedFile qaUploadedFile) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().update(qaUploadedFile);
    }

    public void saveUploadFile(QaUploadedFile qaUploadedFile) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().save(qaUploadedFile);
    }

    public void createUploadFile(QaUploadedFile qaUploadedFile) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().save(qaUploadedFile);
    }

    public void UpdateUploadFile(QaUploadedFile qaUploadedFile) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().update(qaUploadedFile);
    }

    public void removeUploadFile(Long submissionId) {
	if (submissionId != null) {

	    String query = "from uploadedFile in class org.lamsfoundation.lams.tool.qa.QaUploadedFile"
		    + " where uploadedFile.submissionId = ?";
	    Object obj = this.getSession().createQuery(query).setLong(0, submissionId.longValue()).uniqueResult();
	    if (obj != null) {
		this.getSession().setFlushMode(FlushMode.AUTO);
		this.getHibernateTemplate().delete(obj);
	    }
	}
    }

    public List retrieveQaUploadedFiles(QaContent qa) {
	List listFilenames = null;
	listFilenames = (getHibernateTemplate().findByNamedParam(GET_UPLOADED_FILES, "contentId", qa.getQaContentId()));
	return listFilenames;
    }

    public void deleteUploadFile(QaUploadedFile qaUploadedFile) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().delete(qaUploadedFile);
    }

    public void flush() {
	this.getHibernateTemplate().flush();
    }

}