package org.lamsfoundation.lams.tool.wiki.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.wiki.model.WikiPageContent;

public interface IWikiPageContentDAO extends IBaseDAO {

    void saveOrUpdate(WikiPageContent content);
}
