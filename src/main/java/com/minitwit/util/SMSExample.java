package com.minitwit.util;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;

// Doc https://jp.twilio.com/docs/libraries/java#installation-nextgen
public class SMSExample {

  // Find your Account Sid and Token at twilio.com/console
  public static final String ACCOUNT_SID = "AC7d91642f9e97fc792331797a179cce0a";
  public static final String AUTH_TOKEN = "{{ auth_token }}";

  public static void main(String[] args) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

    Message message = Message.create(
      new PhoneNumber("+12345678901"), // TO number
      new PhoneNumber("+12345678901"), // From Twilio number
      "Hello from Java"
    ).execute();

    System.out.println(message.getSid());
  }
}