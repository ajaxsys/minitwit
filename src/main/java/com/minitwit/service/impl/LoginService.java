package com.minitwit.service.impl;

import com.minitwit.dao.AdminDao;
import com.minitwit.model.User;
import com.minitwit.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {
	
	@Autowired private AdminDao adminDao;
	
	public User getUserbyId(int userId) { return adminDao.getUserById(userId); }
	public void updateUser(User user) { adminDao.updateUser(user); }
	public void deleteUser(User user) { adminDao.deleteUser(user); }
	public List<User> getAllUsers() {
		return adminDao.getAllUsers();
	}

	public void registerAdminUser(User user) {
		user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
		adminDao.registerUser(user);
	}

	public void setAdminDao(AdminDao adminDao) {
		this.adminDao = adminDao;
	}
}
