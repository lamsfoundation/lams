package org.lamsfoundation.lams.tool.dlfrum.dao;


import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForumConfigItem;

public interface IDotLRNForumConfigItemDAO extends IBaseDAO
{
	void saveOrUpdate(DotLRNForumConfigItem toConfig);
	public DotLRNForumConfigItem getConfigItemByKey(final String configKey);
}
