package vn.ducbao.springboot.webbansach_backend.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import vn.ducbao.springboot.webbansach_backend.dto.response.NotificationEvent;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendMessage(NotificationEvent notificationEvent) {
        // MimeMailMessage ==> có đính kèm file
        // SimpleMailMesage ==> Chi co text
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("truongducbao290402@gmail.com");
        simpleMailMessage.setTo(notificationEvent.getRecipient());
        simpleMailMessage.setSubject(notificationEvent.getSubject());
        simpleMailMessage.setText(notificationEvent.getBody());

        // thực hiện hành động gửi email
        emailSender.send(simpleMailMessage);
    }

    @Override
    public void sendMessage(String from, String to, String subject, String text) {
        // MimeMailMessage ==> có đính kèm file
        // SimpleMailMesage ==> Chi co text
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        // thực hiện hành động gửi email
        emailSender.send(simpleMailMessage);
    }
}
