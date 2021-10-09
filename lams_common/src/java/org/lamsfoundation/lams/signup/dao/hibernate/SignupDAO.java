package org.lamsfoundation.lams.signup.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.signup.dao.ISignupDAO;
import org.lamsfoundation.lams.signup.model.SignupOrganisation;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.stereotype.Repository;

@Repository
public class SignupDAO extends LAMSBaseDAO implements ISignupDAO {

    @Override
    public SignupOrganisation getSignupOrganisation(String context) {
	List list = doFindCacheable("from SignupOrganisation s where s.disabled=" + Boolean.FALSE + " and s.context=?",
		context);
	if (list != null && list.size() > 0) {
	    return (SignupOrganisation) list.get(0);
	}
	return null;
    }

    @Override
    public List<SignupOrganisation> getSignupOrganisations() {
	return super.findAll(SignupOrganisation.class);
    }

    @Override
    public List<Organisation> getOrganisationCandidates() {
	String query = "from Organisation o where o.organisationState.organisationStateId=" + OrganisationState.ACTIVE;
	query += " and o.organisationType.organisationTypeId!=" + OrganisationType.ROOT_TYPE;
	return doFindCacheable(query);
    }

    @Override
    public boolean usernameExists(String username) {
	List list = super.findByProperty(User.class, "login", username);
	if (list != null && list.size() > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean contextExists(Integer soid, String context) {
	String query = "from SignupOrganisation s where s.signupOrganisationId!=? and s.context=?";
	Object[] values = new Object[2];
	values[0] = soid;
	values[1] = context;
	List list = doFind(query, values);
	if (list != null && list.size() > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean courseKeyIsValid(String context, String courseKey) {
	String query = "from SignupOrganisation s where s.context=? and s.courseKey=?";
	Object[] values = new Object[2];
	values[0] = context;
	values[1] = courseKey;
	List list = doFind(query, values);
	if (list != null && list.size() > 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
