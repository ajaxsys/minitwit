package com.minitwit.dao;

import java.util.List;
import java.util.Optional;

import com.minitwit.model.Message;
import com.minitwit.model.User;

public interface MessageDao {
    List<Message> getUserTimelineMessages(User user);

    List<Message> getUserFullTimelineMessages(User user);

    List<Message> getPublicTimelineMessages();

    void insertMessage(Message m);

    @Deprecated
    List<Message> getMessagesByPage(
        int start,
        int length,
        Optional<String> searchUserName);

    List<Message> getMessagesByPage(
        int start,
        int length,
        User searchCondition);

    @Deprecated
    int getMessageCount(Optional<String> searchUserName);
    int getMessageCount(User searchCondition);
}
