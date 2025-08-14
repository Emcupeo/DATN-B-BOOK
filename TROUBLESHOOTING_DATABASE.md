# Hướng dẫn khắc phục lỗi Database Connection

## Lỗi: "Unable to determine Dialect without JDBC metadata"

### Nguyên nhân:
1. SQL Server không chạy
2. Database BBook không tồn tại
3. Cấu hình JPA/Hibernate thiếu
4. Username/password không đúng
5. SQL Server không cho phép SQL Authentication

### Các bước khắc phục:

#### 1. Kiểm tra SQL Server
```bash
# Chạy script kiểm tra
check_sqlserver.bat
```

#### 2. Kiểm tra SQL Server Service
- Mở Services (services.msc)
- Tìm "SQL Server (MSSQLSERVER)" hoặc "SQL Server (SQLEXPRESS)"
- Đảm bảo service đang chạy (Running)

#### 3. Kiểm tra SQL Server Configuration Manager
- Mở SQL Server Configuration Manager
- Kiểm tra SQL Server Network Configuration
- Đảm bảo TCP/IP được Enable
- Kiểm tra port 1433

#### 4. Kiểm tra SQL Server Authentication
```sql
-- Trong SQL Server Management Studio
-- 1. Right-click server -> Properties
-- 2. Security -> SQL Server and Windows Authentication mode
```

#### 5. Tạo database nếu chưa có
```sql
CREATE DATABASE BBook;
GO
USE BBook;
GO
```

#### 6. Kiểm tra user sa
```sql
-- Đảm bảo user sa có quyền truy cập
USE BBook;
IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = 'sa')
BEGIN
    CREATE USER [sa] FOR LOGIN [sa];
    ALTER ROLE db_owner ADD MEMBER [sa];
END
```

#### 7. Chạy migration scripts
```sql
-- Chạy các script migration trong thư mục src/main/resources/db/migration/
```

#### 8. Test kết nối
```sql
-- Test trong SQL Server Management Studio
SELECT @@VERSION;
SELECT DB_NAME();
SELECT COUNT(*) FROM nhan_vien;
```

### Cấu hình application.properties đã được cập nhật:

```properties
# Database Configuration
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=BBook;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=kienhuy
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
spring.jpa.properties.hibernate.jdbc.time_zone=Asia/Ho_Chi_Minh
```

### Nếu vẫn lỗi:

1. **Kiểm tra firewall**: Đảm bảo port 1433 không bị chặn
2. **Kiểm tra SQL Server Browser**: Service này phải chạy
3. **Thử connection string khác**:
   ```
   jdbc:sqlserver://localhost:1433;databaseName=BBook;encrypt=false;trustServerCertificate=true
   ```
4. **Kiểm tra SQL Server version**: Đảm bảo tương thích với driver

### Test kết nối:
```bash
# Chạy ứng dụng
./mvnw spring-boot:run
```

Nếu vẫn lỗi, hãy kiểm tra log để xem chi tiết lỗi. 