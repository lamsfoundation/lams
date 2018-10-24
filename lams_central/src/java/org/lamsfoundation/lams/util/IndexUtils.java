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


package org.lamsfoundation.lams.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.index.IndexLessonBean;

/**
 * @author jliew
 *
 */
public class IndexUtils {

    // sort mapped lesson beans according to orderedLessonIds
    public static List<IndexLessonBean> sortLessonBeans(String orderedLessonIds, Map<Long, IndexLessonBean> map) {
	ArrayList<IndexLessonBean> orderedList = new ArrayList<IndexLessonBean>();
	if (orderedLessonIds != null) {
	    List<String> idList = Arrays.asList(orderedLessonIds.split(","));

	    for (String idString : idList) {
		try {
		    Long id = new Long(Long.parseLong(idString));
		    if (map.containsKey(id)) {
			orderedList.add(map.get(id));
			map.remove(id);
		    }
		} catch (NumberFormatException e) {
		    continue;
		}
	    }
	}

	// prepend lesson beans not mentioned in orderedLessonIds
	if (!map.values().isEmpty()) {
	    for (Object obj : map.values()) {
		orderedList.add(0, (IndexLessonBean) obj);
	    }
	}

	return orderedList;
    }
}
