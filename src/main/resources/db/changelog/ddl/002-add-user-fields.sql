-- First, add columns without constraints and make them nullable initially
ALTER TABLE feed ADD COLUMN IF NOT EXISTS created_by BIGINT;
ALTER TABLE poll ADD COLUMN IF NOT EXISTS created_by BIGINT;

-- Create default system user if not exists
INSERT INTO users (id, first_name, last_name, email, user_name, password)
VALUES (1, 'System', 'User', 'sshelar110.ss4@gmail.com', 'shreyasdefault', '$2a$10$bBpnY1/HetNNP9b1Kz01ORc9Jx8hoDXn39OPIOLmtocxlKj7Stgy')
ON CONFLICT (id) DO NOTHING;

-- Update existing records to use the system user
UPDATE feed SET created_by = 1 WHERE created_by IS NULL;
UPDATE poll SET created_by = 1 WHERE created_by IS NULL;

-- Now make the columns NOT NULL
ALTER TABLE feed ALTER COLUMN created_by SET NOT NULL;
ALTER TABLE poll ALTER COLUMN created_by SET NOT NULL;

-- Add indexes
CREATE INDEX IF NOT EXISTS idx_users_name ON users(first_name, last_name);
CREATE INDEX IF NOT EXISTS idx_feed_created_by ON feed(created_by);
CREATE INDEX IF NOT EXISTS idx_poll_created_by ON poll(created_by);

-- Add foreign key constraints
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_feed_created_by') THEN
        ALTER TABLE feed ADD CONSTRAINT fk_feed_created_by FOREIGN KEY (created_by) REFERENCES users(id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_reel_user') THEN
        ALTER TABLE reel ADD CONSTRAINT fk_reel_user FOREIGN KEY (user_id) REFERENCES users(id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_feed_like_user') THEN
        ALTER TABLE feed_like ADD CONSTRAINT fk_feed_like_user FOREIGN KEY (user_id) REFERENCES users(id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_poll_created_by') THEN
        ALTER TABLE poll ADD CONSTRAINT fk_poll_created_by FOREIGN KEY (created_by) REFERENCES users(id);
    END IF;
END $$;

-- Update poll_vote table to use BIGINT for voter_id to match user IDs
ALTER TABLE poll_vote ALTER COLUMN voter_id TYPE BIGINT USING voter_id::bigint;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_poll_vote_voter') THEN
        ALTER TABLE poll_vote ADD CONSTRAINT fk_poll_vote_voter FOREIGN KEY (voter_id) REFERENCES users(id);
    END IF;
END $$; 