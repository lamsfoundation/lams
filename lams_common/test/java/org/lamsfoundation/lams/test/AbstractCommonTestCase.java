/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.test;



/**
 * Contains all the information shared across the lams_common junit tests.
 * In particular, it sets up the context file and hibernate session name.
 *  
 * @author Fiona Malikoff
 * 
 */
abstract public class AbstractCommonTestCase extends AbstractLamsTestCase
{
    /**
     * Constructor for AbstractCommonTestCase.
     * @param arg0
     */
    public AbstractCommonTestCase(String arg0) {
        super(arg0);
    }

    protected String[] getContextConfigLocation() {
		return new String[] {"org/lamsfoundation/lams/localApplicationContext.xml"};
	}

    /**
     * @see org.lamsfoundation.lams.test.AbstractLamsTestCase#getHibernateSessionFactoryName()
     */
    protected String getHibernateSessionFactoryName()
    {
        return "coreSessionFactory";
    }

}
