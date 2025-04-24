-- Add duration column to leave_requests table
ALTER TABLE leave_requests
ADD COLUMN duration VARCHAR(20) NOT NULL DEFAULT 'FULL_DAY';

-- Add comment to explain the column
COMMENT ON COLUMN leave_requests.duration IS 'Duration of the leave request (HALF_DAY or FULL_DAY)';

-- Update existing records to have a default value
UPDATE leave_requests SET duration = 'FULL_DAY' WHERE duration IS NULL; 