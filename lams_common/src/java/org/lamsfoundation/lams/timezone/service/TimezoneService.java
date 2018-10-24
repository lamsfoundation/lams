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


package org.lamsfoundation.lams.timezone.service;

import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.timezone.Timezone;
import org.lamsfoundation.lams.timezone.dao.ITimezoneDAO;

/**
 * Class implements <code>ITimezoneService</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.timezone.service.ITimezoneService
 */
public class TimezoneService implements ITimezoneService {

    protected Logger log = Logger.getLogger(TimezoneService.class);

    /** Required DAO's */
    protected ITimezoneDAO timezoneDAO;

    @Override
    public List<Timezone> getDefaultTimezones() {
	return timezoneDAO.getDefaultTimezones();
    }

    @Override
    public void updateTimezones(Collection<Timezone> newTimezones) {
	log.debug("Updating list of available timezones.");

	List<Timezone> oldTimezones = getDefaultTimezones();
	Collection<Timezone> timezonesToDelete = CollectionUtils.subtract(oldTimezones, newTimezones);
	Collection<Timezone> timezonesToAdd = CollectionUtils.subtract(newTimezones, oldTimezones);

	for (Timezone timezone : timezonesToDelete) {
	    timezoneDAO.removeTimezone(timezone);
	}

	for (Timezone timezone : timezonesToAdd) {
	    timezoneDAO.addTimezone(timezone);
	}
    }

    @Override
    public Timezone getServerTimezone() {
	Timezone serverTimezone = timezoneDAO.getServerTimezone();
	if (serverTimezone == null) {
	    serverTimezone = new Timezone();
	    serverTimezone.setTimezoneId(TimeZone.getDefault().getID());
	    serverTimezone.setServerTimezone(true);
	    timezoneDAO.addTimezone(serverTimezone);
	}

	return serverTimezone;
    }

    @Override
    public void setServerTimezone(String timeZoneId) {
	Timezone serverTimezone = timezoneDAO.getServerTimezone();
	serverTimezone.setTimezoneId(timeZoneId);
	timezoneDAO.setServerTimezone(serverTimezone);
    }

    /**
     * @return Returns the timezoneDAO.
     */
    public ITimezoneDAO getTimezoneDAO() {
	return timezoneDAO;
    }

    /**
     *
     * @param timezoneDAO
     *            The timezoneDAO to set.
     */
    public void setTimezoneDAO(ITimezoneDAO timezoneDAO) {
	this.timezoneDAO = timezoneDAO;
    }

}
