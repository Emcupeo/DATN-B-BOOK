package org.example.datnbbook.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendVoucherEmail(String to, String customerName, String voucherType, Double voucherValue) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Đảm bảo mã hóa UTF-8 để hỗ trợ tiếng Việt đúng
        helper.setTo(to);
        helper.setSubject("Chúc mừng bạn nhận voucher giảm giá!");
        helper.setText(
                "<html>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +  // Đảm bảo mã hóa UTF-8
                        "<style>" +
                        "body { font-family: 'Arial', sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; color: #333; }" +
                        ".email-container { width: 100%; max-width: 600px; margin: 20px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1); }" +
                        ".email-header { text-align: center; padding-bottom: 20px; }" +
                        ".email-header h1 { font-size: 24px; color: #333; margin-bottom: 10px; }" +
                        ".email-body { font-size: 16px; line-height: 1.5; margin-bottom: 20px; }" +
                        ".voucher-info { font-size: 18px; font-weight: bold; color: #4CAF50; padding: 10px; background-color: #e8f5e9; border-radius: 5px; }" +
                        ".footer { text-align: center; font-size: 14px; color: #777; padding-top: 20px; border-top: 1px solid #ddd; }" +
                        ".footer a { color: #4CAF50; text-decoration: none; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='email-container'>" +
                        "<div class='email-header'>" +
                        "<h1>Xin chào " + customerName + "!</h1>" +
                        "<p>Chúc mừng bạn đã nhận được một phần quà đặc biệt từ chúng tôi!</p>" +
                        "</div>" +
                        "<div class='email-body'>" +
                        "<p>Chúng tôi rất vui mừng thông báo rằng bạn đã nhận được một <strong>Voucher Giảm Giá</strong> tuyệt vời từ chúng tôi!</p>" +
                        "<p class='voucher-info'>Voucher giảm giá: " + voucherType + "</p>" +
                        "<p class='voucher-info'>Giá trị voucher: " + voucherValue + "</p>" +
                        "<p>Hãy sử dụng voucher này để tận hưởng các ưu đãi hấp dẫn khi mua sắm tại cửa hàng của chúng tôi. Đừng bỏ lỡ cơ hội này!</p>" +
                        "<p>Chúc bạn có một ngày tuyệt vời và hy vọng sẽ sớm gặp lại bạn trong những lần mua sắm tới!</p>" +
                        "</div>" +
                        "<div class='footer'>" +
                        "<p>Cảm ơn bạn đã luôn ủng hộ chúng tôi!</p>" +
                        "<p><a href='http://www.yourwebsite.com'>Truy cập website của chúng tôi</a></p>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>",
                true // true cho phép sử dụng HTML
        );

        // Gửi email
        emailSender.send(message);
    }
}
