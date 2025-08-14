// Test logic tạo mã nhân viên
public class TestMaNhanVienLogic {
    
    public static void main(String[] args) {
        testGenerateMaNhanVien();
    }
    
    public static void testGenerateMaNhanVien() {
        // Test các trường hợp khác nhau
        String[] testCases = {
            null,           // Không có dữ liệu
            "NV00000",      // Mã không hợp lệ
            "NV00001",      // Mã đầu tiên
            "NV00005",      // Mã giữa
            "NV99999",      // Mã cuối
            "NV12345",      // Mã bất kỳ
            "ABC123",       // Mã không đúng format
            "NV00001",      // Mã đã tồn tại
            "NV00002",      // Mã đã tồn tại
        };
        
        for (String maxMaNhanVien : testCases) {
            String result = generateSafeMaNhanVien(maxMaNhanVien);
            System.out.println("Input: " + maxMaNhanVien + " -> Output: " + result);
        }
    }
    
    private static String generateSafeMaNhanVien(String maxMaNhanVien) {
        int nextNumber = 1;
        
        if (maxMaNhanVien != null && maxMaNhanVien.startsWith("NV")) {
            try {
                String numberStr = maxMaNhanVien.substring(2);
                int currentNumber = Integer.parseInt(numberStr);
                // Đảm bảo không bao giờ trả về 0, luôn bắt đầu từ 1
                nextNumber = Math.max(currentNumber + 1, 1);
            } catch (NumberFormatException e) {
                // Nếu không parse được, bắt đầu từ 1
                nextNumber = 1;
            }
        }
        
        // Đảm bảo không bao giờ trả về NV00000
        if (nextNumber == 0) {
            nextNumber = 1;
        }
        
        // Tạo mã và kiểm tra xem đã tồn tại chưa
        String candidateMaNhanVien = String.format("NV%05d", nextNumber);
        
        // Giả lập kiểm tra tồn tại (trong thực tế sẽ query database)
        String[] existingCodes = {"NV00001", "NV00002", "NV00005"};
        while (contains(existingCodes, candidateMaNhanVien)) {
            nextNumber++;
            candidateMaNhanVien = String.format("NV%05d", nextNumber);
            
            // Đảm bảo không vượt quá giới hạn hợp lý
            if (nextNumber > 99999) {
                throw new RuntimeException("Đã đạt giới hạn số lượng nhân viên (99999)");
            }
        }
        
        return candidateMaNhanVien;
    }
    
    private static boolean contains(String[] array, String value) {
        for (String item : array) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }
} 