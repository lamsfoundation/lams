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

/* $Id$ */
package org.lamsfoundation.lams.learningdesign;

import java.util.Comparator;

/**
 * Comparator for <code>TextSearchCondition</code>. Only the order ID is compared.
 *
 * @author Marcin Cieslak
 * @see org.lamsfoundation.lams.learningdesign.TextSearchCondition
 */
public class TextSearchConditionComparator implements Comparator<TextSearchCondition> {

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(TextSearchCondition o1, TextSearchCondition o2) {
	if (o1 != null && o2 != null) {
	    return o1.getOrderId() - o2.getOrderId();
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }
}