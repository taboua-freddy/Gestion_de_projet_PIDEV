/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import entity.user.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 *
 * @author Freddy
 */
public class SendMail {
    public static void sendMail(ArrayList<User> users,String object,String corps) {
        Properties properties = new Properties();
        
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        
        String MonEmail = "tabouaf@gmail.com";
        String password = "tabouapiter";
        
        Session session = Session.getInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(MonEmail, password);
            }
        
        });
        
        Message message = prepareMessage(session,MonEmail,users,object,corps);
        
        try {
            Transport.send(message);
        } catch (MessagingException ex) {
            Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.err.println("Message envoyé avec succès");
    }
    
    private static Message prepareMessage(Session session,String email,ArrayList<User> users,String object,String corps) 
    {
        Message message = new MimeMessage(session);
        
        ArrayList<Address> adress = new ArrayList<Address>();
        for (User user : users) {
            try {
                adress.add(new InternetAddress(user.getEmail()));
            } catch (AddressException ex) {
                Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            message.setFrom(new InternetAddress(email));
            
            message.setSubject(object);
            //message.setRecipient(Message.RecipientType.TO, new InternetAddress(receveursList));
            message.setRecipients(Message.RecipientType.TO, adress.toArray(new InternetAddress[adress.size()]));
            message.setText(corps);
            
            return message;
        } catch (MessagingException ex) {
            Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    
    
    
   
}
