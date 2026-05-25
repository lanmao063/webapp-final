-- ============================================================
-- 快递驿站管理系统 测试数据
-- 所有测试用户密码: 123456
-- ============================================================

-- ============================================================
-- 1. 用户 (system_user)
-- ============================================================
INSERT INTO system_user (id, username, password, role, real_name, phone, address) VALUES
(1, 'zhang', '$2a$10$iaNp2.xxlTtqWyDaZjKP0OBfPr2.gBYgkirsWmhPUrbXGc7h9kd7.', 'REGULAR', '张用户', '13800001111', '北京市朝阳区1号'),
(2, 'li',    '$2a$10$iaNp2.xxlTtqWyDaZjKP0OBfPr2.gBYgkirsWmhPUrbXGc7h9kd7.', 'COURIER', '李快递', '13900001111', NULL),
(3, 'wang',  '$2a$10$iaNp2.xxlTtqWyDaZjKP0OBfPr2.gBYgkirsWmhPUrbXGc7h9kd7.', 'MANAGER', '王管理', '13700001111', NULL),
(4, 'hu',    '$2a$10$iaNp2.xxlTtqWyDaZjKP0OBfPr2.gBYgkirsWmhPUrbXGc7h9kd7.', 'REGULAR', '胡代取', '13600001111', '北京市海淀区2号');

-- ============================================================
-- 2. 已入库包裹 (package + inbound_package) — 5个
--    收件人均为 zhang (phone: 13800001111)
-- ============================================================

-- 包裹1: 小件
INSERT INTO package (id, tracking_number, package_name, weight, volume,
                     sender_name, sender_phone, sender_address,
                     receiver_name, receiver_phone, receiver_address)
VALUES (1, 'SF0000000001', '蓝牙耳机', 0.3, 3000,
        '数码旗舰店', '4001110001', '深圳市南山区',
        '张用户', '13800001111', '北京市朝阳区1号');

INSERT INTO inbound_package (package_id, pickup_code, cabinet_type, status, entered_by, enter_time)
VALUES (1, '03-5-1001', 'SMALL', 'IN_WAREHOUSE', 2, '2026-05-24 09:30:00');

-- 包裹2: 小件 (代取人: hu, 手机号 13600001111)
INSERT INTO package (id, tracking_number, package_name, weight, volume,
                     sender_name, sender_phone, sender_address,
                     receiver_name, receiver_phone, receiver_address)
VALUES (2, 'SF0000000002', '手机壳', 0.1, 1500,
        '天猫旗舰店', '4002220002', '杭州市余杭区',
        '张用户', '13800001111', '北京市朝阳区1号');

INSERT INTO inbound_package (package_id, pickup_code, cabinet_type, proxy_phone, status, entered_by, enter_time)
VALUES (2, '12-3-1002', 'SMALL', '13600001111', 'IN_WAREHOUSE', 2, '2026-05-24 10:00:00');

-- 包裹3: 中件
INSERT INTO package (id, tracking_number, package_name, weight, volume,
                     sender_name, sender_phone, sender_address,
                     receiver_name, receiver_phone, receiver_address)
VALUES (3, 'SF0000000003', '运动鞋', 1.2, 45000,
        '京东自营', '4003330003', '北京市通州区',
        '张用户', '13800001111', '北京市朝阳区1号');

INSERT INTO inbound_package (package_id, pickup_code, cabinet_type, status, entered_by, enter_time)
VALUES (3, '55-7-1003', 'MEDIUM', 'IN_WAREHOUSE', 2, '2026-05-24 11:00:00');

-- 包裹4: 中件
INSERT INTO package (id, tracking_number, package_name, weight, volume,
                     sender_name, sender_phone, sender_address,
                     receiver_name, receiver_phone, receiver_address)
VALUES (4, 'SF0000000004', '电热水壶', 2.0, 72000,
        '苏宁易购', '4004440004', '南京市玄武区',
        '张用户', '13800001111', '北京市朝阳区1号');

INSERT INTO inbound_package (package_id, pickup_code, cabinet_type, status, entered_by, enter_time)
VALUES (4, '72-2-1004', 'MEDIUM', 'IN_WAREHOUSE', 2, '2026-05-24 14:00:00');

-- 包裹5: 大件
INSERT INTO package (id, tracking_number, package_name, weight, volume,
                     sender_name, sender_phone, sender_address,
                     receiver_name, receiver_phone, receiver_address)
VALUES (5, 'SF0000000005', '办公椅', 12.0, 180000,
        '宜家家居', '4005550005', '上海市浦东新区',
        '张用户', '13800001111', '北京市朝阳区1号');

INSERT INTO inbound_package (package_id, pickup_code, cabinet_type, status, entered_by, enter_time)
VALUES (5, '120-6-1005', 'LARGE', 'IN_WAREHOUSE', 2, '2026-05-24 16:00:00');

-- ============================================================
-- 3. 运输中包裹 (仅 package) — 5个
--    收件人均为 zhang，尚未入库
-- ============================================================
INSERT INTO package (id, tracking_number, package_name, weight, volume,
                     sender_name, sender_phone, sender_address,
                     receiver_name, receiver_phone, receiver_address)
VALUES
(6,  'SF0000000006', '笔记本电脑支架', 1.5, 20000,  '联想商城', '4006660006', '北京市海淀区',   '张用户', '13800001111', '北京市朝阳区1号'),
(7,  'SF0000000007', '台灯',           0.8, 9000,   '小米有品', '4007770007', '北京市大兴区',   '张用户', '13800001111', '北京市朝阳区1号'),
(8,  'SF0000000008', '雨伞',           0.4, 4000,   '网易严选', '4008880008', '杭州市滨江区',   '张用户', '13800001111', '北京市朝阳区1号'),
(9,  'SF0000000009', '桌面收纳盒',     0.6, 35000,  '名创优品', '4009990009', '广州市越秀区',   '张用户', '13800001111', '北京市朝阳区1号'),
(10, 'SF0000000010', '加湿器',         2.5, 90000,  '飞利浦',   '4000000010', '上海市静安区',   '张用户', '13800001111', '北京市朝阳区1号');

-- ============================================================
-- 验证查询
-- ============================================================
-- SELECT '=== 用户 ===' AS '';
-- SELECT id, username, role, real_name, phone FROM system_user ORDER BY id;
-- SELECT '=== 已入库包裹 ===' AS '';
-- SELECT p.tracking_number, p.package_name, ib.pickup_code, ib.cabinet_type, ib.proxy_phone, ib.status
-- FROM package p JOIN inbound_package ib ON p.id = ib.package_id ORDER BY p.id;
-- SELECT '=== 运输中包裹 ===' AS '';
-- SELECT p.id, p.tracking_number, p.package_name
-- FROM package p LEFT JOIN inbound_package ib ON p.id = ib.package_id
-- WHERE ib.id IS NULL ORDER BY p.id;
