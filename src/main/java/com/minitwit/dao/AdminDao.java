package com.minitwit.dao;

import com.minitwit.model.User;

import java.util.List;

public interface AdminDao {

	List<User> getAllUsers();
	User getUserById(int userId);

	void registerUser(User user);
	void updateUser(User user);
	void deleteUser(User user);

}
