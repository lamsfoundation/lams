/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.tool.gmap.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.gmap.dao.IGmapMarkerDAO;
import org.lamsfoundation.lams.tool.gmap.model.GmapMarker;
import org.springframework.stereotype.Repository;

@Repository
public class GmapMarkerDAO extends LAMSBaseDAO implements IGmapMarkerDAO {
    private static final String SQL_QUERY_BY_SESSION = "from " + GmapMarker.class.getName() + " gm "
	    + " where gm.gmapSession.sessionId=?";

    @Override
    public void saveOrUpdate(GmapMarker gmapMarker) {
	getSession().saveOrUpdate(gmapMarker);
	getSession().flush();
    }

    @Override
    public List<GmapMarker> getByToolSessionId(Long toolSessionId) {
	return (List<GmapMarker>) (doFind(SQL_QUERY_BY_SESSION, toolSessionId));
    }
}
