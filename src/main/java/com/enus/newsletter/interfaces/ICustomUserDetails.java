
package com.enus.newsletter.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface ICustomUserDetails extends UserDetails {
    public String getEmail();
}
