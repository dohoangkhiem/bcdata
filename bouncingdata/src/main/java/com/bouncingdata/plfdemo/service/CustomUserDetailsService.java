package com.bouncingdata.plfdemo.service;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bouncingdata.plfdemo.datastore.DataStorage;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;

public class CustomUserDetailsService implements UserDetailsService {
  
  private DataStorage dataStorage;
  
  public void setDataStorage(DataStorage ds) {
    this.dataStorage = ds;
  }
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
    User user = dataStorage.findUserByUsername(username);
    if (user == null) return null;
    
    Collection<String> authorities = dataStorage.getUserAuthorities(user.getId());
    user.setAuthorities(authorities);
    return user;
  }

}
