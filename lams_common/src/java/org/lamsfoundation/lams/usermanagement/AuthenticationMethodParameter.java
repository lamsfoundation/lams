package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;

public class AuthenticationMethodParameter implements Serializable {

    private String name;

    private String value;

    public AuthenticationMethodParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /** default constructor */
    public AuthenticationMethodParameter() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
