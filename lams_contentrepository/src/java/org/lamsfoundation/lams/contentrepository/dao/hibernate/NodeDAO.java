/*
 * Created on Dec 20, 2004
 */
package org.lamsfoundation.lams.contentrepository.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.CrNode;
import org.lamsfoundation.lams.contentrepository.CrNodeVersion;
import org.lamsfoundation.lams.contentrepository.dao.INodeDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;



/**
 * Data access routines for Nodes
 * 
 * @author Fiona Malikoff
 */
public class NodeDAO extends HibernateDaoSupport implements INodeDAO  {

	protected Logger log = Logger.getLogger(NodeDAO.class);	

	public Object find(Class objClass, Serializable id) {
		return this.getHibernateTemplate().load(objClass,id);
	}
	
	public void insert(Object object) {
		this.getHibernateTemplate().saveOrUpdate(object);		
	}

	public void update(Object object) {
		this.getHibernateTemplate().update(object);
	}

	public void delete(Object object) {
		this.getHibernateTemplate().delete(object);
	}

	public List findAll(Class objClass) {
		String query="from obj in class " + objClass.getName(); 
		return this.getHibernateTemplate().find(query);
	}
	
	/** Get all child nodes for a node/version.
	 * 
	 * The child/parent relationship set is lazy loaded to make things
	 * more efficient. But as the repository doesn't keep the session
	 * open, the lazy loading can't occur! So load manually.
	 * 
	 * @param workspaceId
	 * @return Set of CrNodes that are child nodes of this node/version 
	 */
	public List findChildNodes(CrNodeVersion parentNodeVersion) {

		if ( log.isDebugEnabled() )
			log.debug("Getting all child nodes for "+parentNodeVersion);
		
		String queryString = "from CrNode as n where n.parentNodeVersion = ?";
		List nodes = getHibernateTemplate().find(queryString,parentNodeVersion);
		
		if(nodes.size() == 0){
			log.debug("No nodes found");
			return null;
		}else{
			if ( log.isDebugEnabled() )
				log.debug("Returning "+nodes.size()+" nodes.");
			return nodes;
		}
	}


	/** Get all child nodes for a node/version.
	 * 
	 * The child/parent relationship set is lazy loaded to make things
	 * more efficient. But as the repository doesn't keep the session
	 * open, the lazy loading can't occur! So load manually.
	 * 
	 * @param workspaceId
	 * @return Set of CrNodes that are child nodes of this node/version 
	 */
	public CrNode findChildNode(CrNodeVersion parentNodeVersion, String relPath) {

		long start = System.currentTimeMillis();

		if ( log.isDebugEnabled() )
			log.debug("Getting child node from "+parentNodeVersion+" path "+relPath);
		
		
		String queryString = "from CrNode as n where n.parentNodeVersion = ? and n.path = ? ";
		List nodes = getHibernateTemplate().find(queryString,new Object[] {parentNodeVersion, relPath});
		
		log.error("findChildNodeDBLookup"+(System.currentTimeMillis()-start));
		
		if(nodes.size() == 0){
			log.debug("No nodes found");
			return null;
		}else{
			if ( nodes.size() == 0 ) {
				log.error(nodes.size()+" matches found for "+parentNodeVersion+" path "+relPath);
			}
			return (CrNode) nodes.get(0);
		}
	}

}
