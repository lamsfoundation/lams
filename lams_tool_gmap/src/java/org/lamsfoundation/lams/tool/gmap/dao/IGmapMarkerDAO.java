package org.lamsfoundation.lams.tool.gmap.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.lamsfoundation.lams.tool.gmap.model.GmapMarker;

public interface IGmapMarkerDAO extends IBaseDAO
{
	void saveOrUpdate(GmapMarker toContent);
}
