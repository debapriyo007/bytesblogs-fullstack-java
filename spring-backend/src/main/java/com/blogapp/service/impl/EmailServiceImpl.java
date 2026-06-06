package com.blogapp.service.impl;

import com.blogapp.service.interfaces.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @org.springframework.beans.factory.annotation.Value("${spring.mail.from:${spring.mail.username:noreply@bugblogs.com}}")
    private String fromEmail;

    @Override
    public void sendOtpEmail(String toEmail, String username, String otp) {
        String subject = "Verify Your bugblogs Account";
        String htmlContent = loadOtpTemplate(username, otp);

        boolean emailSent = false;

        if (mailSender != null) {
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setFrom(fromEmail, "bugblogs");
                helper.setTo(toEmail);
                helper.setSubject(subject);
                helper.setText(htmlContent, true);
                mailSender.send(mimeMessage);
                logger.info("Verification OTP HTML email successfully dispatched to: {}", toEmail);
                emailSent = true;
            } catch (Exception e) {
                logger.warn("Could not dispatch SMTP email. Falling back to console logging: {}", e.getMessage(), e);
            }
        }

        // Highlighted ASCII output in backend terminal console for developer sandbox testing
        System.out.println("\n" +
                "┌────────────────────────────────────────────────────────┐\n" +
                "│               [ bugblogs DEVELOPMENT OTP ]             │\n" +
                "├────────────────────────────────────────────────────────┤\n" +
                "│  TO:       " + String.format("%-44s", toEmail) + "│\n" +
                "│  USER:     " + String.format("%-44s", username) + "│\n" +
                "│                                                        │\n" +
                "│  YOUR OTP CODE IS:                                     │\n" +
                "│  >>  \u001B[1;31m" + otp + "\u001B[0m  <<                                           │\n" +
                "│                                                        │\n" +
                "│  (Valid for 5 minutes. Enter this code on the UI)      │\n" +
                "└────────────────────────────────────────────────────────┘\n"
        );
    }

    private String loadOtpTemplate(String username, String otp) {
        try {
            ClassPathResource resource = new ClassPathResource("mail-templates/otp-template.html");
            String template = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            return template.replace("${username}", username).replace("${otp}", otp);
        } catch (Exception e) {
            logger.error("Failed to load email template, falling back to basic text", e);
            return "Hello " + username + ",\n\nTo verify your account, please enter the following OTP code: " + otp;
        }
    }
}
