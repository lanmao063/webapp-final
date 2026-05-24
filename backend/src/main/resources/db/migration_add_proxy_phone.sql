-- Add proxy_phone column for proxy pickup feature
-- Run this migration if send_package table already exists without proxy_phone
ALTER TABLE send_package ADD COLUMN proxy_phone VARCHAR(20) COMMENT '代取人手机号' AFTER notes;
