package org.lamsfoundation.lams.tool.gmap.dao.hibernate;

import java.util.List;


import org.lamsfoundation.lams.tool.gmap.dao.IGmapMarkerDAO;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.lamsfoundation.lams.tool.gmap.model.GmapMarker;
import org.lamsfoundation.lams.tool.gmap.model.GmapSession;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;

public class GmapMarkerDAO extends BaseDAO implements IGmapMarkerDAO
{
	private static final String SQL_QUERY_BY_SESSION = "from " + GmapMarker.class.getName() + " gm "
	+ " where gm.gmapSession.sessionId=?";
	
	public void saveOrUpdate(GmapMarker gmapMarker) 
	{
		this.getHibernateTemplate().saveOrUpdate(gmapMarker);
		this.getHibernateTemplate().flush();
	}
	
	public List<GmapMarker> getByToolSessionId(Long toolSessionId)
	{
		return (List<GmapMarker>)(this.getHibernateTemplate().find(SQL_QUERY_BY_SESSION, toolSessionId));
	}
}
