/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usual;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author LucienFOTSA
 */
public class MailTools {

    private String emailLogin = "contact@mit2online.com";
    private String emailPass = "Mit2!!!3";

    public MailTools() {

    }

    public String msgToHtml_Simple(String msg, String subject) {
        String html = "<?xml version='1.0' encoding='UTF-8' ?>\n"
                + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
                + "<html style=\"padding: 0px; margin: 0px;\">\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<div style=\"padding: 10px; height: 25px; background-color: #0081c2; color: #FFFFFF; margin: auto; font-weight: bold; font-size: 15pt;\">";
        html += subject;
        html += "</div>\n"
                + "<div style=\"padding: 10px; color: #555555; margin: auto; font-size: 13pt;\">";
        html += msg;
        html += "</div>\n"
                + "<div style=\"font-style: italic; font-size: 9pt; color: #333333;  padding: 10px; border-top-width: 1px; border-top-style: solid; border-top-color: #0081c2;\"> Powered by <a target=\"blank\" href=\"http://www.mit2online.com\">MIT&sup2;</a></div>\n"
                + "</body></html>";
        return html;
    }

    public void sendEmail(String msg, String subject, String from, String to, Boolean parseHtml, String vDeco) {
        try {
            //Create email sending properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "false");
            props.put("mail.smtp.host", "mail.mit2online.com");
            props.put("mail.smtp.port", "25");
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailLogin, emailPass);
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from)); //Set from address of the email
            message.setContent((parseHtml ? msgToHtml_Simple(specialCharToHtml(msg), subject) : specialCharToHtml(msg.replace("vDeco", vDeco))), "text/html"); //set content type of the email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); //Set email recipient
            message.setSubject(subject); //Set email message subject
            Transport.send(message); //Send email message
            System.out.println("sent email successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmailDirect(String mail, String subject, String from, String to, String vDeco) {
        try {
            //Create email sending properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "false");
            props.put("mail.smtp.host", "mail.mit2online.com");
            props.put("mail.smtp.port", "25");
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailLogin, emailPass);
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from)); //Set from address of the email
            message.setContent(mail.replace("vDeco", vDeco), "text/html"); //set content type of the email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); //Set email recipient
            message.setSubject(subject); //Set email message subject
            Transport.send(message); //Send email message
            System.out.println("sent email successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String specialCharToHtml(String txt) {
        txt = txt.replace("°", "&deg;");
        txt = txt.replace("+", "&#43;");
        txt = txt.replace("²", "&sup2;");
        txt = txt.replace("³", "&sup3;");
        txt = txt.replace("À", "&Agrave;");
        txt = txt.replace("Ç", "&Ccedil;");
        txt = txt.replace("É", "&Eacute;");
        txt = txt.replace("Ê", "&Ecirc;");
        txt = txt.replace("Ë", "&Euml;");
        txt = txt.replace("Î", "&Icirc;");
        txt = txt.replace("Ï", "&Iuml;");
        txt = txt.replace("©", "&copy;");
        txt = txt.replace("®", "&reg;");
        txt = txt.replace("Ô", "&Ocirc;");
        txt = txt.replace("Ù", "&Ugrave;");
        txt = txt.replace("Û", "&Ucirc;");
        txt = txt.replace("à", "&agrave;");
        txt = txt.replace("á", "&aacute;");
        txt = txt.replace("â", "&acirc;");
        txt = txt.replace("ç", "&ccedil;");
        txt = txt.replace("è", "&egrave;");
        txt = txt.replace("é", "&eacute;");
        txt = txt.replace("ê", "&ecirc;");
        txt = txt.replace("ë", "&euml;");
        txt = txt.replace("î", "&icirc;");
        txt = txt.replace("ï", "&iuml;");
        txt = txt.replace("ô", "&ocirc;");
        txt = txt.replace("ù", "&ugrave;");
        txt = txt.replace("û", "&ucirc;");
        txt = txt.replace("ü", "&uuml;");
        txt = txt.replace("€", "&euro;");
        txt = txt.replace("œ", "&#339;");
        txt = txt.replace("Œ", "&#338;");
        return txt;
    }

    public String getEmailLogin() {
        return emailLogin;
    }

    public void setEmailLogin(String emailLogin) {
        this.emailLogin = emailLogin;
    }

    public String getEmailPass() {
        return emailPass;
    }

    public void setEmailPass(String emailPass) {
        this.emailPass = emailPass;
    }

}
