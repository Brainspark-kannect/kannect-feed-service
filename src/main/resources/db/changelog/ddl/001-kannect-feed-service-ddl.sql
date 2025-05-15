-- changelog-001-create-social-tables.sql

-- 1. Feed table
CREATE TABLE IF NOT EXISTS feed (
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(255)    NOT NULL,
    content          TEXT            NOT NULL,
    type             VARCHAR(50)     NOT NULL,
    is_fun_friday    BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

-- Indexes for feed
CREATE INDEX IF NOT EXISTS idx_feed_type           ON feed(type);
CREATE INDEX IF NOT EXISTS idx_feed_funfriday      ON feed(is_fun_friday);
CREATE INDEX IF NOT EXISTS idx_feed_created_at     ON feed(created_at);

-- 2. Feed media (one-to-one with feed)
CREATE TABLE IF NOT EXISTS feed_media (
    id        BIGSERIAL PRIMARY KEY,
    feed_id   BIGINT      NOT NULL UNIQUE REFERENCES feed(id) ON DELETE CASCADE,
    gcp_url   TEXT        NOT NULL
);

-- 3. Feed like/dislike
CREATE TABLE IF NOT EXISTS feed_like (
    id         BIGSERIAL PRIMARY KEY,
    feed_id    BIGINT      NOT NULL REFERENCES feed(id) ON DELETE CASCADE,
    user_id    BIGINT      NOT NULL,
    liked      BOOLEAN     NOT NULL,
    reacted_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_feed_user_like UNIQUE (feed_id, user_id)
);

-- Indexes for feed_like
CREATE INDEX IF NOT EXISTS idx_feed_like_feed_id    ON feed_like(feed_id);
CREATE INDEX IF NOT EXISTS idx_feed_like_user_id    ON feed_like(user_id);

-- 4. Reel table
CREATE TABLE IF NOT EXISTS reel (
    id          BIGSERIAL PRIMARY KEY,
    caption     VARCHAR(500)   NOT NULL,
    video_url   TEXT           ,
    user_id     BIGINT         NOT NULL,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

-- Indexes for reel
CREATE INDEX IF NOT EXISTS idx_reel_user_id        ON reel(user_id);
CREATE INDEX IF NOT EXISTS idx_reel_created_at     ON reel(created_at);

-- 5. Reel like/dislike
CREATE TABLE IF NOT EXISTS reel_like (
    id         BIGSERIAL PRIMARY KEY,
    reel_id    BIGINT      NOT NULL REFERENCES reel(id) ON DELETE CASCADE,
    user_id    BIGINT      NOT NULL,
    liked      BOOLEAN     NOT NULL,
    reacted_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_reel_user_like UNIQUE (reel_id, user_id)
);

-- Indexes for reel_like
CREATE INDEX IF NOT EXISTS idx_reel_like_reel_id   ON reel_like(reel_id);
CREATE INDEX IF NOT EXISTS idx_reel_like_user_id   ON reel_like(user_id);

-- 6. Poll table
CREATE TABLE IF NOT EXISTS poll (
    id         BIGSERIAL PRIMARY KEY,
    question   VARCHAR(1000) NOT NULL,
    created_at TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

-- Indexes for poll
CREATE INDEX IF NOT EXISTS idx_poll_created_at     ON poll(created_at);

-- 7. Poll options
CREATE TABLE IF NOT EXISTS poll_option (
    id       BIGSERIAL PRIMARY KEY,
    poll_id  BIGINT     NOT NULL REFERENCES poll(id) ON DELETE CASCADE,
    text     VARCHAR(500) NOT NULL
);

-- Indexes for poll_option
CREATE INDEX IF NOT EXISTS idx_poll_option_poll_id ON poll_option(poll_id);

-- 8. Poll votes
CREATE TABLE IF NOT EXISTS poll_vote (
    id             BIGSERIAL PRIMARY KEY,
    poll_option_id BIGINT     NOT NULL REFERENCES poll_option(id) ON DELETE CASCADE,
    voter_id       BIGINT     NOT NULL,
    voted_at       TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    CONSTRAINT uq_vote_option_voter UNIQUE (poll_option_id, voter_id)
);

-- Indexes for poll_vote
CREATE INDEX IF NOT EXISTS idx_poll_vote_option_id ON poll_vote(poll_option_id);
CREATE INDEX IF NOT EXISTS idx_poll_vote_voter_id  ON poll_vote(voter_id);
