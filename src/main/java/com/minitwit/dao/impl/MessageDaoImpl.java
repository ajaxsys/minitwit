package com.minitwit.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.minitwit.dao.MessageDao;
import com.minitwit.model.Message;
import com.minitwit.model.User;
import com.minitwit.util.GravatarUtil;
import com.minitwit.util.StringUtil;

@Repository
public class MessageDaoImpl implements MessageDao {

    private static final String GRAVATAR_DEFAULT_IMAGE_TYPE = "monsterid";
    private static final int GRAVATAR_SIZE = 48;
    private NamedParameterJdbcTemplate template;

    @Autowired
    public MessageDaoImpl(DataSource ds) {
        template = new NamedParameterJdbcTemplate(ds);
    }

    @Override
    public List<Message> getUserTimelineMessages(User user) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", user.getId());

        String sql = "select message.*, user.* from message, user where " +
                "user.user_id = message.author_id and user.user_id = :id " +
                "order by message.pub_date desc";
        List<Message> result = template.query(sql, params, messageMapper);

        return result;
    }

    @Override
    public List<Message> getUserFullTimelineMessages(User user) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", user.getId());

        String sql = "select message.*, user.* from message, user " +
                "where message.author_id = user.user_id and ( " +
                "user.user_id = :id or " +
                "user.user_id in (select followee_id from follower " +
                                    "where follower_id = :id))" +
                "order by message.pub_date desc";
        List<Message> result = template.query(sql, params, messageMapper);

        return result;
    }

    @Override
    public List<Message> getPublicTimelineMessages() {
        Map<String, Object> params = new HashMap<String, Object>();

        String sql = "select message.*, user.* from message, user " +
                "where message.author_id = user.user_id " +
                "order by message.pub_date desc";
        List<Message> result = template.query(sql, params, messageMapper);

        return result;
    }

    @Deprecated
    @Override
    public
    List<Message> getMessagesByPage(
        int start,
        int length,
        Optional<String> searchUserName) {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("limit", length);
        params.put("offset", start);

        StringBuilder sb = new StringBuilder();
        sb.append("select message.*, user.* from message, user ");
        sb.append("where message.author_id = user.user_id ");
        searchUserName.ifPresent(
            v-> {
                params.put("username", "%"+v+"%");
                sb.append("and user.username like :username ");
            });

        sb.append("order by message.pub_date desc ");
        sb.append("limit :limit OFFSET :offset");

        List<Message> result = template.query(
            sb.toString(), params, messageMapper);

        return result;
    }

    @Deprecated
    @Override
    public
    int getMessageCount(
        Optional<String> searchUserName) {

        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();

        StringUtil.anyPresent(
            () -> {
                sb.append("select count(*) from message, user ");
                sb.append("where message.author_id = user.user_id ");

                searchUserName.ifPresent(
                    v-> {
                        params.put("username", "%"+v+"%");
                        sb.append("and user.username like :username ");
                    });
            },
            searchUserName);

        StringUtil.allEmpty(
            ()->
                sb.append( "SELECT count(*) FROM message"),
            searchUserName);

        return template.queryForObject(
            sb.toString(),
            params,
            Integer.class);
    }

    @Override
    public void insertMessage(Message m) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", m.getUserId());
        params.put("text", m.getText());
        params.put("pubDate", m.getPubDate());

        String sql = "insert into message (author_id, text, pub_date) values (:userId, :text, :pubDate)";
        template.update(sql, params);
    }

    @Override
    public
    List<Message> getMessagesByPage(
        int start,
        int length,
        User searchConditon) {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("limit", length);
        params.put("offset", start);

        StringBuilder sb = new StringBuilder();
        sb.append("select message.*, user.* from message, user ");
        sb.append("where message.author_id = user.user_id ");
        searchConditon.mayNullUsername().ifPresent(
            v-> {
                params.put("username", "%"+v+"%");
                sb.append("and user.username like :username ");
            });
        searchConditon.mayNullEmail().ifPresent(
            v-> {
                params.put("email", "%"+v+"%");
                sb.append("and user.email like :email ");
            });

        sb.append("order by message.pub_date desc ");
        sb.append("limit :limit OFFSET :offset");

        List<Message> result = template.query(
            sb.toString(), params, messageMapper);

        return result;
    }

    @Override
    public
    int getMessageCount(
        User searchConditon) {

        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();

        StringUtil.anyPresent(
            () -> {
                sb.append("select count(*) from message, user ");
                sb.append("where message.author_id = user.user_id ");

                searchConditon.mayNullUsername().ifPresent(
                    v-> {
                        params.put("username", "%"+v+"%");
                        sb.append("and user.username like :username ");
                    });
                searchConditon.mayNullEmail().ifPresent(
                    v-> {
                        params.put("email", "%"+v+"%");
                        sb.append("and user.email like :email ");
                    });
            },
            searchConditon.mayNullUsername(),
            searchConditon.mayNullEmail());

        StringUtil.allEmpty(
            ()->
                sb.append( "SELECT count(*) FROM message"),
            searchConditon.mayNullUsername(),
            searchConditon.mayNullEmail());

        return template.queryForObject(
            sb.toString(),
            params,
            Integer.class);
    }

    private RowMapper<Message> messageMapper = (rs, rowNum) -> {
        Message m = new Message();

        m.setId(rs.getInt("message_id"));
        m.setUserId(rs.getInt("author_id"));
        m.setUsername(rs.getString("username"));
        m.setText(rs.getString("text"));
        m.setPubDate(rs.getTimestamp("pub_date"));
        m.setGravatar(GravatarUtil.gravatarURL(rs.getString("email"), GRAVATAR_DEFAULT_IMAGE_TYPE, GRAVATAR_SIZE));

        return m;
    };

}
