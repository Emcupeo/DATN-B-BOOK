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
    private JavaMailSender mailSender;

    public void sendEmployeeCredentials(String toEmail, String employeeCode, String password) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom("duyennckth02248@fpt.edu.vn");
        helper.setTo(toEmail);
        helper.setSubject("Thông tin đăng nhập hệ thống");
        
        String content = String.format("""
            <html>
            <body>
                <h2>Thông tin đăng nhập của bạn</h2>
                <p>Xin chào,</p>
                <p>Dưới đây là thông tin đăng nhập của bạn:</p>
                <p><strong>Tên đăng nhập:</strong> %s</p>
                <p><strong>Mật khẩu:</strong> %s</p>
                <p>Vui lòng đổi mật khẩu sau khi đăng nhập lần đầu.</p>
                <p>Trân trọng,</p>
                <p>B-Book</p>
            </body>
            </html>
            """, employeeCode, password);
        
        helper.setText(content, true);
        mailSender.send(message);
    }

    public void sendCustomerCredentials(String toEmail, String customerCode, String password) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom("duyennckth02248@fpt.edu.vn");
        helper.setTo(toEmail);
        helper.setSubject("Chào mừng bạn đến với B-Book!");
        
        String content = String.format("""
            <html>
            <body>
                <h2>Chào mừng bạn đến với B-Book!</h2>
                <p>Xin chào,</p>
                <p>Cảm ơn bạn đã đăng ký tài khoản tại B-Book. Dưới đây là thông tin đăng nhập của bạn:</p>
                <p><strong>Mã khách hàng:</strong> %s</p>
                <p><strong>Tên đăng nhập:</strong> %s</p>
                <p><strong>Mật khẩu:</strong> %s</p>
                <p>Bạn có thể sử dụng thông tin này để đăng nhập vào hệ thống và khám phá những cuốn sách tuyệt vời.</p>
                <p>Vui lòng đổi mật khẩu sau khi đăng nhập lần đầu để bảo mật tài khoản.</p>
                <p>Chúc bạn có những trải nghiệm mua sắm tuyệt vời!</p>
                <p>Trân trọng,</p>
                <p>Đội ngũ B-Book</p>
            </body>
            </html>
            """, customerCode, customerCode, password);
        
        helper.setText(content, true);
        mailSender.send(message);
    }

    public void sendVoucherEmail(String to, String customerName, String voucherType, Double voucherValue) throws MessagingException {
        sendVoucherEmail(to, customerName, voucherType, voucherValue, false, null);
    }

    public void sendVoucherEmail(String to, String customerName, String voucherType, Double voucherValue, boolean isUpdate) throws MessagingException {
        sendVoucherEmail(to, customerName, voucherType, voucherValue, isUpdate, null);
    }

    public void sendVoucherEmail(String to, String customerName, String voucherType, Double voucherValue, boolean isUpdate, String voucherCode) throws MessagingException {
        sendVoucherEmail(to, customerName, voucherType, voucherValue, isUpdate, voucherCode, null, null, null, null);
    }

    public void sendVoucherEmail(String to, String customerName, String voucherType, Double voucherValue, boolean isUpdate, String voucherCode, 
                                String voucherName, String description, String startDate, String endDate) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("h5studiogl@gmail.com");
        helper.setTo(to);
        helper.setSubject(isUpdate ? "Cập nhật voucher giảm giá của bạn!" : "Chúc mừng bạn nhận voucher giảm giá!");
        String content = String.format("""
            <html>
            <head>
                <meta charset='UTF-8'>
                <style>
                    body { font-family: 'Arial', sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; color: #333; }
                    .email-container { width: 100%%; max-width: 600px; margin: 20px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1); }
                    .email-header { text-align: center; padding-bottom: 20px; border-bottom: 2px solid #4CAF50; }
                    .email-header h1 { font-size: 24px; color: #4CAF50; margin-bottom: 10px; }
                    .email-body { font-size: 16px; line-height: 1.6; margin-bottom: 20px; }
                    .voucher-info { background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 15px 0; }
                    .voucher-info h3 { color: #4CAF50; margin-top: 0; }
                    .info-row { display: flex; justify-content: space-between; margin: 8px 0; }
                    .info-label { font-weight: bold; color: #555; }
                    .info-value { color: #333; }
                    .footer { text-align: center; font-size: 14px; color: #777; padding-top: 20px; border-top: 1px solid #ddd; }
                    .footer a { color: #4CAF50; text-decoration: none; }
                    .success-message { background-color: #d4edda; color: #155724; padding: 15px; border-radius: 5px; margin: 15px 0; }
                    .voucher-code { background-color: #e8f5e9; padding: 8px 12px; border-radius: 4px; font-family: monospace; font-weight: bold; color: #2e7d32; }
                </style>
            </head>
            <body>
                <div class='email-container'>
                    <div class='email-header'>
                        <h1>🎉 %s</h1>
                        <p>Cảm ơn bạn đã tin tưởng B-Book</p>
                    </div>
                    
                    <div class='success-message'>
                        <strong>Xin chào %s!</strong><br>
                        %s
                    </div>
                    
                    <div class='voucher-info'>
                        <h3>📋 Thông tin phiếu giảm giá</h3>
                        %s
                        <div class='info-row'>
                            <span class='info-label'>Loại giảm giá:</span>
                            <span class='info-value'>%s</span>
                        </div>
                        <div class='info-row'>
                            <span class='info-label'>Giá trị giảm:</span>
                            <span class='info-value'><strong style='color: #4CAF50; font-size: 18px;'>%s</strong></span>
                        </div>
                        %s
                    </div>
                    
                    <div class='voucher-info'>
                        <h3>⏰ Thời gian áp dụng</h3>
                        %s
                        %s
                    </div>
                    
                    <div class='email-body'>
                        <p><strong>Hướng dẫn sử dụng:</strong></p>
                        <ul>
                            <li>Nhập mã phiếu khi thanh toán để áp dụng giảm giá</li>
                            <li>Phiếu có thể sử dụng cho tất cả sản phẩm phù hợp</li>
                            <li>Mỗi đơn hàng chỉ được áp dụng một phiếu giảm giá</li>
                        </ul>
                        
                        <p>Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi qua hotline hoặc email.</p>
                    </div>
                    
                    <div class='footer'>
                        <p>Cảm ơn bạn đã tin tưởng và lựa chọn B-Book!</p>
                        <p><a href='http://localhost:3000'>Truy cập website của chúng tôi</a></p>
                        <p><a href='http://localhost:3000/order-lookup'>Tra cứu đơn hàng</a></p>
                    </div>
                </div>
            </body>
            </html>
            """, 
            isUpdate ? "Cập nhật voucher thành công!" : "Chúc mừng bạn nhận voucher!",
            customerName,
            isUpdate ? "Chúng tôi đã cập nhật thông tin voucher giảm giá của bạn. Dưới đây là thông tin chi tiết:" : "Chúng tôi rất vui mừng thông báo rằng bạn đã nhận được một Voucher Giảm Giá tuyệt vời từ chúng tôi!",
            voucherCode != null ? String.format("<div class='info-row'><span class='info-label'>Mã phiếu:</span><span class='info-value'><span class='voucher-code'>%s</span></span></div>", voucherCode) : "",
            voucherType,
            formatVoucherValue(voucherType, voucherValue),
            voucherName != null && !voucherName.trim().isEmpty() ? String.format("<div class='info-row'><span class='info-label'>Tên phiếu:</span><span class='info-value'>%s</span></div>", voucherName) : "",
            description != null && !description.trim().isEmpty() ? String.format("<div class='info-row'><span class='info-label'>Mô tả:</span><span class='info-value'>%s</span></div>", description) : "",
            startDate != null ? String.format("<div class='info-row'><span class='info-label'>Từ ngày:</span><span class='info-value'>%s</span></div>", formatDate(startDate)) : "",
            endDate != null ? String.format("<div class='info-row'><span class='info-label'>Đến ngày:</span><span class='info-value'>%s</span></div>", formatDate(endDate)) : ""
        );
        
        helper.setText(content, true);

        mailSender.send(message);
    }

    private String formatVoucherValue(String voucherType, Double voucherValue) {
        if (voucherValue == null) {
            return "Không xác định";
        }
        
        if ("Giảm theo phần trăm".equals(voucherType)) {
            return String.format("%.0f%%", voucherValue);
        } else if ("Giảm theo giá trị tiền".equals(voucherType)) {
            return String.format("%,.0f ₫", voucherValue);
        } else {
            return voucherValue.toString();
        }
    }

    private String formatDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return "Không xác định";
        }
        
        try {
            // Parse date string and format it nicely
            if (dateString.contains("T")) {
                // Handle ISO format: 2024-01-01T00:00:00
                String datePart = dateString.split("T")[0];
                String[] parts = datePart.split("-");
                if (parts.length == 3) {
                    return String.format("%s/%s/%s", parts[2], parts[1], parts[0]);
                }
            }
            return dateString;
        } catch (Exception e) {
            return dateString;
        }
    }

    public void sendOrderConfirmationEmail(String toEmail, String customerName, String orderCode, 
                                         String orderDate, String totalAmount, String paymentMethod, 
                                         String deliveryAddress, String phoneNumber) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom("h5studiogl@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject("Xác nhận đơn hàng #" + orderCode + " - B-Book");
        
        String content = String.format("""
            <html>
            <head>
                <meta charset='UTF-8'>
                <style>
                    body { font-family: 'Arial', sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; color: #333; }
                    .email-container { width: 100%%; max-width: 600px; margin: 20px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1); }
                    .email-header { text-align: center; padding-bottom: 20px; border-bottom: 2px solid #4CAF50; }
                    .email-header h1 { font-size: 24px; color: #4CAF50; margin-bottom: 10px; }
                    .email-body { font-size: 16px; line-height: 1.6; margin-bottom: 20px; }
                    .order-info { background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 15px 0; }
                    .order-info h3 { color: #4CAF50; margin-top: 0; }
                    .info-row { display: flex; justify-content: space-between; margin: 8px 0; }
                    .info-label { font-weight: bold; color: #555; }
                    .info-value { color: #333; }
                    .footer { text-align: center; font-size: 14px; color: #777; padding-top: 20px; border-top: 1px solid #ddd; }
                    .footer a { color: #4CAF50; text-decoration: none; }
                    .success-message { background-color: #d4edda; color: #155724; padding: 15px; border-radius: 5px; margin: 15px 0; }
                </style>
            </head>
            <body>
                <div class='email-container'>
                    <div class='email-header'>
                        <h1>🎉 Đặt hàng thành công!</h1>
                        <p>Cảm ơn bạn đã mua sắm tại B-Book</p>
                    </div>
                    
                    <div class='success-message'>
                        <strong>Xin chào %s!</strong><br>
                        Chúng tôi đã nhận được đơn hàng của bạn và đang xử lý. Dưới đây là thông tin chi tiết đơn hàng:
                    </div>
                    
                    <div class='order-info'>
                        <h3>📋 Thông tin đơn hàng</h3>
                        <div class='info-row'>
                            <span class='info-label'>Mã đơn hàng:</span>
                            <span class='info-value'><strong>%s</strong></span>
                        </div>
                        <div class='info-row'>
                            <span class='info-label'>Ngày đặt hàng:</span>
                            <span class='info-value'>%s</span>
                        </div>
                        <div class='info-row'>
                            <span class='info-label'>Tổng tiền:</span>
                            <span class='info-value'><strong style='color: #4CAF50;'>%s</strong></span>
                        </div>
                        <div class='info-row'>
                            <span class='info-label'>Phương thức thanh toán:</span>
                            <span class='info-value'>%s</span>
                        </div>
                    </div>
                    
                    <div class='order-info'>
                        <h3>🚚 Thông tin giao hàng</h3>
                        <div class='info-row'>
                            <span class='info-label'>Địa chỉ giao hàng:</span>
                            <span class='info-value'>%s</span>
                        </div>
                        <div class='info-row'>
                            <span class='info-label'>Số điện thoại:</span>
                            <span class='info-value'>%s</span>
                        </div>
                    </div>
                    
                    <div class='email-body'>
                        <p><strong>Bước tiếp theo:</strong></p>
                        <ul>
                            <li>Chúng tôi sẽ xác nhận đơn hàng trong vòng 24 giờ</li>
                            <li>Bạn có thể tra cứu trạng thái đơn hàng bằng mã đơn hàng và số điện thoại</li>
                        </ul>
                        
                        <p>Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi qua hotline hoặc email.</p>
                    </div>
                    
                    <div class='footer'>
                        <p>Cảm ơn bạn đã tin tưởng và lựa chọn B-Book!</p>
                        <p><a href='http://localhost:3000'>Truy cập website của chúng tôi</a></p>
                        <p><a href='http://localhost:3000/order-lookup'>Tra cứu đơn hàng</a></p>
                    </div>
                </div>
            </body>
            </html>
            """, customerName, orderCode, orderDate, totalAmount, paymentMethod, deliveryAddress, phoneNumber);
        
        helper.setText(content, true);
        mailSender.send(message);
    }
}
