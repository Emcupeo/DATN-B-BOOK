public class TestCreateNhanVien {
    public static void main(String[] args) {
        // Test logic tạo mã nhân viên với các trường hợp khác nhau
        
        // Trường hợp 1: Chưa có nhân viên nào
        testGenerateMaNhanVien(null, "NV00001");
        
        // Trường hợp 2: Có nhân viên NV00001
        testGenerateMaNhanVien("NV00001", "NV00002");
        
        // Trường hợp 3: Có nhân viên NV00005
        testGenerateMaNhanVien("NV00005", "NV00006");
        
        // Trường hợp 4: Có nhân viên NV00000 (không hợp lệ)
        testGenerateMaNhanVien("NV00000", "NV00001");
        
        // Trường hợp 5: Có nhân viên NV00099
        testGenerateMaNhanVien("NV00099", "NV00100");
    }
    
    private static void testGenerateMaNhanVien(String maxMaNhanVien, String expected) {
        int nextNumber = 1;
        
        if (maxMaNhanVien != null && maxMaNhanVien.startsWith("NV")) {
            try {
                String numberStr = maxMaNhanVien.substring(2);
                int currentNumber = Integer.parseInt(numberStr);
                // Đảm bảo không bao giờ trả về 0, luôn bắt đầu từ 1
                nextNumber = Math.max(currentNumber + 1, 1);
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }
        
        // Đảm bảo không bao giờ trả về NV00000
        if (nextNumber == 0) {
            nextNumber = 1;
        }
        
        String result = String.format("NV%05d", nextNumber);
        
        System.out.println("Input: " + maxMaNhanVien + " -> Expected: " + expected + " -> Actual: " + result + " -> " + (expected.equals(result) ? "PASS" : "FAIL"));
    }
} 