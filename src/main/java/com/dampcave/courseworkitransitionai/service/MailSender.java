package com.dampcave.courseworkitransitionai.service;

import com.dampcave.courseworkitransitionai.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class MailSender {


    private final JavaMailSender mailSender;

    @Value("${site.url}")
    private String urlSite;

    @Autowired
    public MailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String username;

    public void send(String emailTo, String subject, String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    public void messageActivation(User user){
        if (!ObjectUtils.isEmpty(user.getEmail())) {
            String message = String.format("Hello %s,\n"
                            + "Welcome to site Damp Cave.\n"
                            + "Please, for account activation visit next link: %s/activate/%s",
                    user.getUsername(),
                    urlSite,
                    user.getActivationCode());

            send(user.getEmail(), "Activation code", message);
        }
    }
}
