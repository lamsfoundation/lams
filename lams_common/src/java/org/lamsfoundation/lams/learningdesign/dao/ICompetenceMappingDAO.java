package org.lamsfoundation.lams.learningdesign.dao;

import java.util.Set;

import org.lamsfoundation.lams.learningdesign.Competence;
import org.lamsfoundation.lams.learningdesign.CompetenceMapping;
import org.lamsfoundation.lams.learningdesign.ToolActivity;

public interface ICompetenceMappingDAO {
    public void saveOrUpdate(CompetenceMapping competenceMapping);

    public CompetenceMapping getCompetenceMapping(ToolActivity toolActivity, Competence competence);

    public void delete(CompetenceMapping competenceMapping);

    public void deleteAll(Set<CompetenceMapping> competenceMappings);

    public void insert(CompetenceMapping competenceMapping);
}
