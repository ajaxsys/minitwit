package com.minitwit.dao.impl;

import com.minitwit.dao.AdminDao;
import com.minitwit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AdminDaoImpl implements AdminDao {

	private NamedParameterJdbcTemplate template;

	@Autowired
	public AdminDaoImpl(DataSource ds) {
		template = new NamedParameterJdbcTemplate(ds);
	}

	@Override
	public User getUserById(int userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", userId);

		String sql = "SELECT * FROM user WHERE user_id=:id";

		List<User> list = template.query(
				sql,
				params,
				userMapper);

		User result = null;
		if(list != null && !list.isEmpty()) {
			result = list.get(0);
		}

		return result;
	}

	@Override
	public List<User> getAllUsers() {
		String sql = "SELECT * FROM user";

		return template.query(
				sql,
				userMapper);
	}

	@Override
	public void registerUser(User user) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", user.getUsername());
        params.put("email", user.getEmail());
        params.put("pw", user.getPassword());

		String sql = "insert into user (username, email, pw) values (:username, :email, :pw)";

        template.update(sql,  params);
	}

	@Override
	public void updateUser(User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", user.getId());
		params.put("username", user.getUsername());
		params.put("email", user.getEmail());

		String sql = "update user set username=:username, email=:email where user_id=:id";

		template.update(sql,  params);
	}

	@Override
	public void deleteUser(User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", user.getId());

		String sql = "delete from user where user_id=:id";

		template.update(sql,  params);
	}

	private RowMapper<User> userMapper = (rs, rowNum) -> {
		User u = new User();

		u.setId(rs.getInt("user_id"));
		u.setEmail(rs.getString("email"));
		u.setUsername(rs.getString("username"));
		u.setPassword(rs.getString("pw"));

		return u;
	};
}
