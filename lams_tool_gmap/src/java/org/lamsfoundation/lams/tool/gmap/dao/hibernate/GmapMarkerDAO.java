package org.lamsfoundation.lams.tool.gmap.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.gmap.dao.IGmapMarkerDAO;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.lamsfoundation.lams.tool.gmap.model.GmapMarker;
import org.lamsfoundation.lams.tool.gmap.model.GmapSession;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;

public class GmapMarkerDAO extends BaseDAO implements IGmapMarkerDAO
{
	private static final String FIND_MARKER_BY_CONTENTID = "from GmapMarker gmapMarker where gmapMarker.toolContentId=?";
	
	public void saveOrUpdate(GmapMarker gmapMarker) 
	{
		this.getHibernateTemplate().saveOrUpdate(gmapMarker);
		this.getHibernateTemplate().flush();
	}
}
