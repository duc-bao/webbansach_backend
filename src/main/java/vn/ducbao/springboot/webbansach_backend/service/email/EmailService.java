package vn.ducbao.springboot.webbansach_backend.service.email;

import vn.ducbao.springboot.webbansach_backend.dto.response.NotificationEvent;

public interface EmailService {
    public void sendMessage(NotificationEvent notificationEvent);

    public void sendMessage(String from, String to, String subject, String text);
}
