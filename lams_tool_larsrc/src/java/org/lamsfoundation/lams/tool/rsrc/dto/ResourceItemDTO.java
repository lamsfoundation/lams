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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.rsrc.dto;

import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;

/**
 * List contains following element: <br>
 *
 * <li>ResourceItem.uid</li>
 * <li>ResourceItem.item_type</li>
 * <li>ResourceItem.create_by_author</li>
 * <li>ResourceItem.is_hide</li>
 * <li>ResourceItem.title</li>
 * <li>User.login_name</li>
 * <li>count(resource_item_uid)</li>
 *
 * @author Steve.Ni
 */
public class ResourceItemDTO {

    private Long itemUid;
    private short itemType;
    private boolean itemCreateByAuthor;
    private boolean itemHide;
    private String itemTitle;
    private String username;
    private int viewNumber;
    private boolean allowRating;
    private boolean allowComments;
    private ItemRatingDTO ratingDTO;

    // true: initial group item, false, belong to some group.
    private boolean isInitGroup;

    /**
     * Contruction method for monitoring summary function.
     *
     * <B>Don't not set isInitGroup and viewNumber fields</B>
     *
     * @param sessionName
     * @param item
     * @param isInitGroup
     */
    public ResourceItemDTO(ResourceItem item) {
	if (item != null) {
	    this.itemUid = item.getUid();
	    this.itemType = item.getType();
	    this.itemCreateByAuthor = item.isCreateByAuthor();
	    this.itemHide = item.isHide();
	    this.itemTitle = item.getTitle();
	    ResourceUser user = item.getCreateBy();
	    if (user != null) {
		this.username = user.getFullName() + " (" + user.getLoginName() + ")";
	    }

	    this.setAllowRating(item.isAllowRating());
	    this.setAllowComments(item.isAllowComments());
	} else {
	    this.itemUid = -1L;
	}
    }

    public boolean isItemCreateByAuthor() {
	return itemCreateByAuthor;
    }

    public void setItemCreateByAuthor(boolean itemCreateByAuthor) {
	this.itemCreateByAuthor = itemCreateByAuthor;
    }

    public boolean isItemHide() {
	return itemHide;
    }

    public void setItemHide(boolean itemHide) {
	this.itemHide = itemHide;
    }

    public String getItemTitle() {
	return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
	this.itemTitle = itemTitle;
    }

    public short getItemType() {
	return itemType;
    }

    public void setItemType(short itemType) {
	this.itemType = itemType;
    }

    public Long getItemUid() {
	return itemUid;
    }

    public void setItemUid(Long itemUid) {
	this.itemUid = itemUid;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public int getViewNumber() {
	return viewNumber;
    }

    public void setViewNumber(int viewNumber) {
	this.viewNumber = viewNumber;
    }

    public boolean isInitGroup() {
	return isInitGroup;
    }

    public void setInitGroup(boolean isInitGroup) {
	this.isInitGroup = isInitGroup;
    }

    public ItemRatingDTO getRatingDTO() {
	return ratingDTO;
    }

    public void setRatingDTO(ItemRatingDTO ratingDTO) {
	this.ratingDTO = ratingDTO;
    }

    public boolean isAllowRating() {
	return allowRating;
    }

    public void setAllowRating(boolean allowRating) {
	this.allowRating = allowRating;
    }

    public boolean isAllowComments() {
	return allowComments;
    }

    public void setAllowComments(boolean allowComments) {
	this.allowComments = allowComments;
    }
}