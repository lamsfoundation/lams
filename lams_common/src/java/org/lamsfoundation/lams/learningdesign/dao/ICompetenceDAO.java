package org.lamsfoundation.lams.learningdesign.dao;

import java.util.Set;

import org.lamsfoundation.lams.learningdesign.Competence;
import org.lamsfoundation.lams.learningdesign.LearningDesign;

public interface ICompetenceDAO {
    public void saveOrUpdate(Competence competence);

    public Competence getCompetence(LearningDesign design, String competenceTitle);

    public void delete(Competence competence);

    public void deleteAll(Set<Competence> competence);
}
