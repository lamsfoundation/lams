/*
 * Created on May 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dao.hibernate;

import java.util.List;

import net.sf.hibernate.FlushMode;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;

import org.lamsfoundation.lams.learningdesign.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.sbmt.InstructionFiles;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO;
import org.springframework.orm.hibernate.HibernateTemplate;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesContentDAO extends BaseDAO implements ISubmitFilesContentDAO {

	private static final String FIND_INSTRUCTION_FILE = "from " + InstructionFiles.class.getName() 
													+ " as i where content_id=? and i.uuID=? and i.versionID=? and i.type=?";
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO#getContentByID(java.lang.Long)
	 */
	public SubmitFilesContent getContentByID(Long contentID) {
		return (SubmitFilesContent) super.find(SubmitFilesContent.class,contentID);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO#save(org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent)
	 */
	public void save(SubmitFilesContent content) {
		this.getSession().setFlushMode(FlushMode.COMMIT);
		this.getHibernateTemplate().save(content);
		this.getHibernateTemplate().flush();
	}

	public void deleteInstructionFile(Long contentID, Long uuid, Long versionID, String type) {
		HibernateTemplate templ = this.getHibernateTemplate();
		List list = templ.find(FIND_INSTRUCTION_FILE,new Object[]{contentID,uuid,versionID,type}
				,new Type[]{Hibernate.LONG,Hibernate.LONG,Hibernate.LONG,Hibernate.STRING});
		if(list != null && list.size() > 0){
			InstructionFiles file = (InstructionFiles) list.get(0);
			this.getSession().setFlushMode(FlushMode.AUTO);
			templ.delete(file);
			templ.flush();
		}
		
	}
}
