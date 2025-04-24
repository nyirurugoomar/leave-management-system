-- First, make numberOfDays nullable
ALTER TABLE leave_requests ALTER COLUMN number_of_days DROP NOT NULL;

-- Then add the duration column
ALTER TABLE leave_requests
ADD COLUMN duration VARCHAR(20) NOT NULL DEFAULT 'FULL_DAY';

-- Add comment to explain the column
COMMENT ON COLUMN leave_requests.duration IS 'Duration of the leave request (HALF_DAY or FULL_DAY)';

-- Update existing records to have a default value
UPDATE leave_requests SET duration = 'FULL_DAY' WHERE duration IS NULL;

-- Update numberOfDays for existing records based on start and end dates
UPDATE leave_requests 
SET number_of_days = EXTRACT(DAY FROM (end_date - start_date)) + 1
WHERE number_of_days IS NULL; 