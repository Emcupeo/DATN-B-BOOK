-- Create sequence for customer codes
IF NOT EXISTS (SELECT * FROM sys.sequences WHERE name = 'KHSeq')
BEGIN
    CREATE SEQUENCE KHSeq
    START WITH 1
    INCREMENT BY 1;
END; 