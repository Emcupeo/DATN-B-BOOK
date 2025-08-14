public class TestMaNhanVien {
    public static void main(String[] args) {
        // Test logic tạo mã nhân viên
        String maxMaNhanVien = null; // Giả sử chưa có nhân viên nào
        int nextNumber = 1;
        
        if (maxMaNhanVien != null && maxMaNhanVien.startsWith("NV")) {
            try {
                String numberStr = maxMaNhanVien.substring(2);
                nextNumber = Integer.parseInt(numberStr) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }
        
        String result = String.format("NV%05d", nextNumber);
        System.out.println("Mã nhân viên đầu tiên: " + result); // Sẽ in: NV00001
        
        // Test với mã lớn nhất là NV00005
        maxMaNhanVien = "NV00005";
        nextNumber = 1;
        
        if (maxMaNhanVien != null && maxMaNhanVien.startsWith("NV")) {
            try {
                String numberStr = maxMaNhanVien.substring(2);
                nextNumber = Integer.parseInt(numberStr) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }
        
        result = String.format("NV%05d", nextNumber);
        System.out.println("Mã nhân viên tiếp theo: " + result); // Sẽ in: NV00006
    }
} 