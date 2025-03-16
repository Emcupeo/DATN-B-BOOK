-- Drop existing default constraint if any
IF EXISTS (
    SELECT * 
    FROM sys.default_constraints 
    WHERE parent_object_id = OBJECT_ID('khach_hang')
    AND col_name(parent_object_id, parent_column_id) = 'ma_khach_hang'
)
BEGIN
    DECLARE @ConstraintName nvarchar(200)
    SELECT @ConstraintName = name
    FROM sys.default_constraints
    WHERE parent_object_id = OBJECT_ID('khach_hang')
    AND col_name(parent_object_id, parent_column_id) = 'ma_khach_hang'
    
    EXEC('ALTER TABLE khach_hang DROP CONSTRAINT ' + @ConstraintName)
END

-- Add default constraint for ma_khach_hang
ALTER TABLE khach_hang 
ADD CONSTRAINT DF_khach_hang_ma_khach_hang 
DEFAULT 'KH'+right('00000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[KHSeq]), 5) 
FOR ma_khach_hang; 