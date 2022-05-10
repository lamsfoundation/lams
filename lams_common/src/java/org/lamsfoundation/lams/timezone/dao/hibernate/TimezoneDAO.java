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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.timezone.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.timezone.Timezone;
import org.lamsfoundation.lams.timezone.dao.ITimezoneDAO;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of <code>ITimezoneDAO</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.timezone.dao.ITimezoneDAO
 */
@Repository
public class TimezoneDAO extends LAMSBaseDAO implements ITimezoneDAO {

    private final static String FIND_DEFAULT_TIMEZONES = "from " + Timezone.class.getName()
	    + " timezone where timezone.serverTimezone=false";

    private final static String FIND_SERVER_TIMEZONE = "from " + Timezone.class.getName()
	    + " timezone where timezone.serverTimezone=true";

    @Override
    public List<Timezone> getDefaultTimezones() {
	List timezones = this.doFindCacheable(FIND_DEFAULT_TIMEZONES);
	return timezones;
    }

    @Override
    public void addTimezone(Timezone timezone) {
	super.insert(timezone);
    }

    @Override
    public void removeTimezone(Timezone timezone) {
	super.delete(timezone);
    }

    @Override
    public Timezone getServerTimezone() {
	List list = doFindCacheable(FIND_SERVER_TIMEZONE);
	if (list.size() > 0) {
	    return (Timezone) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public void setServerTimezone(Timezone serverTimezone) {
	super.update(serverTimezone);
    }
}