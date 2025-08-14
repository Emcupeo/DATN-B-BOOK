-- Create sequence for employee codes
IF NOT EXISTS (SELECT * FROM sys.sequences WHERE name = 'NVSeq')
BEGIN
    CREATE SEQUENCE NVSeq
    START WITH 1
    INCREMENT BY 1;
END;