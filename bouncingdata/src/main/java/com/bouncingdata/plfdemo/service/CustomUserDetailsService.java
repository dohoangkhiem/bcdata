package com.bouncingdata.plfdemo.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.bouncingdata.plfdemo.datastore.DataStorage;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;

@Transactional
public class CustomUserDetailsService implements UserDetailsService {
  
  private DataStorage dataStorage;
  
  private Logger logger = LoggerFactory.getLogger(UserDetailsService.class);
  
  public void setDataStorage(DataStorage ds) {
    this.dataStorage = ds;
  }
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
    User user = dataStorage.findUserByUsername(username);
    if (user == null) {
      logger.debug("Failed to find user {}", username);
      throw new UsernameNotFoundException("Username " + username + " not found!");
    }
    
    Collection<String> authorities = dataStorage.getUserAuthorities(user.getId());
    user.setAuthorities(authorities);
    return user;
  }

}
