package org.lamsfoundation.lams.tool.mdfrum.dao;


import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForumConfigItem;

public interface IMdlForumConfigItemDAO extends IBaseDAO
{
	void saveOrUpdate(MdlForumConfigItem toConfig);
	public MdlForumConfigItem getConfigItemByKey(final String configKey);
}
