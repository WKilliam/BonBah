package com.codflix.backend.mail;

import javax.mail.*;

import javax.mail.Authenticator;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MailControler {


    //create session
    public static void sendMessage(String mail, String password, String recepient) throws Exception{
        System.out.println("prepare mail");
        // 1 -> Creation of session
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.post", "8889");



        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail,password);
            }
        });

        Message message = prepareMessage(session,mail,recepient);
        Transport.send(message);
        System.out.println("Message send");


    }

    //prepare message
    private static Message prepareMessage(Session session,String mail,String recepient)  {

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("This is mail ckeking accont");
            message.setText("This mail is testing your inscription");
            return message;
        }catch (Exception e){
            Logger.getLogger(MailControler.class.getName()).log(Level.SEVERE,null,e);
        }
        return null;

    }

}
