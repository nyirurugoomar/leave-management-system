-- First add the column as nullable
ALTER TABLE leave_requests
ADD COLUMN half_day BOOLEAN;

-- Update existing rows to have a default value
UPDATE leave_requests
SET half_day = false
WHERE half_day IS NULL;

-- Now make the column non-nullable
ALTER TABLE leave_requests
ALTER COLUMN half_day SET NOT NULL; 