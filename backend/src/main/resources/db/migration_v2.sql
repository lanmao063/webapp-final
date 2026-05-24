-- ============================================================
-- 数据库迁移 v2: 删除旧 parcel 系统，error_parcel 改为关联 package
-- 执行前请备份数据库
-- ============================================================

-- 1. error_parcel 表结构迁移: parcel_id → package_id
ALTER TABLE error_parcel ADD COLUMN package_id BIGINT AFTER id;

-- 2. 删除旧列
ALTER TABLE error_parcel DROP COLUMN parcel_id;

-- 3. 可选: 删除不再使用的 parcel 表
-- DROP TABLE IF EXISTS parcel;

-- 4. inbound_package 添加自动出库字段
ALTER TABLE inbound_package ADD COLUMN is_auto_checkout TINYINT DEFAULT 0 AFTER status;
ALTER TABLE inbound_package ADD COLUMN auto_checkout_time DATETIME AFTER out_time;

-- 验证
-- SHOW COLUMNS FROM error_parcel;
-- SHOW COLUMNS FROM inbound_package;
