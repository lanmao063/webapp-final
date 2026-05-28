-- ============================================================
-- 生成20个包裹测试数据
-- 单号格式: SF + yyyyMMddHHmmss(14位) + 4位随机HEX = 20位
-- zhang (13800001111): 5个已入库 + 5个运输中 (ID 11-20)
-- hu   (13600001111): 5个已入库 + 5个运输中 (ID 21-30)
-- ============================================================

-- 先清除可能的冲突数据（注意外键顺序）
DELETE FROM inbound_package WHERE package_id BETWEEN 11 AND 30;
DELETE FROM package WHERE id BETWEEN 11 AND 30;

-- ============================================================
-- zhang 已入库包裹 (package + inbound_package) — 5个
-- ============================================================

INSERT INTO package (id, tracking_number, package_name, weight, volume,
                     sender_name, sender_phone, sender_address,
                     receiver_name, receiver_phone, receiver_address)
VALUES
(11, 'SF20260525083000A1B2', '机械键盘', 0.9, 12000,
 '罗技旗舰店', '4001110011', '深圳市南山区',
 '张三', '13800001111', '北京市朝阳区1号'),
(12, 'SF20260525091500C3D4', '保温杯', 0.35, 2500,
 '膳魔师官方店', '4001110012', '上海市闵行区',
 '张三', '13800001111', '北京市朝阳区1号'),
(13, 'SF20260525100000E5F6', '帆布鞋', 0.7, 30000,
 '匡威旗舰店', '4001110013', '广州市白云区',
 '张三', '13800001111', '北京市朝阳区1号'),
(14, 'SF20260525133000A7B8', '瑜伽垫', 1.8, 80000,
 '迪卡侬', '4001110014', '苏州市工业园区',
 '张三', '13800001111', '北京市朝阳区1号'),
(15, 'SF20260525150000C9D0', '行李箱', 6.5, 160000,
 '小米有品', '4001110015', '北京市大兴区',
 '张三', '13800001111', '北京市朝阳区1号');

INSERT INTO inbound_package (package_id, pickup_code, cabinet_type, status, entered_by, enter_time)
VALUES
(11, '18-2-2001', 'SMALL',  'IN_WAREHOUSE', 2, '2026-05-25 08:30:00'),
(12, '25-4-2002', 'SMALL',  'IN_WAREHOUSE', 2, '2026-05-25 09:15:00'),
(13, '33-6-2003', 'MEDIUM', 'IN_WAREHOUSE', 2, '2026-05-25 10:00:00'),
(14, '47-1-2004', 'MEDIUM', 'IN_WAREHOUSE', 2, '2026-05-25 13:30:00'),
(15, '88-5-2005', 'LARGE',  'IN_WAREHOUSE', 2, '2026-05-25 15:00:00');

-- ============================================================
-- zhang 运输中包裹 (仅 package) — 5个
-- ============================================================

INSERT INTO package (id, tracking_number, package_name, weight, volume,
                     sender_name, sender_phone, sender_address,
                     receiver_name, receiver_phone, receiver_address)
VALUES
(16, 'SF20260527080000E1F2', '空气炸锅',   3.2, 60000,  '美的旗舰店', '4001110016', '佛山市顺德区', '张三', '13800001111', '北京市朝阳区1号'),
(17, 'SF20260527120000A3B4', '书包',       0.6, 15000,  '新秀丽',     '4001110017', '上海市长宁区', '张三', '13800001111', '北京市朝阳区1号'),
(18, 'SF20260527160000C5D6', '电动牙刷',   0.25, 2000,  '飞利浦',     '4001110018', '上海市静安区', '张三', '13800001111', '北京市朝阳区1号'),
(19, 'SF20260528090000E7F8', 'U型枕',      0.3, 8000,   '网易严选',   '4001110019', '杭州市滨江区', '张三', '13800001111', '北京市朝阳区1号'),
(20, 'SF20260528140000A9B0', '香薰机',     0.5, 5000,   'MUJI',       '4001110020', '日本东京都',   '张三', '13800001111', '北京市朝阳区1号');

-- ============================================================
-- hu 已入库包裹 (package + inbound_package) — 5个
-- ============================================================

INSERT INTO package (id, tracking_number, package_name, weight, volume,
                     sender_name, sender_phone, sender_address,
                     receiver_name, receiver_phone, receiver_address)
VALUES
(21, 'SF20260526080000C1D2', '无线鼠标',   0.12, 1500,  '罗技旗舰店', '4001110021', '深圳市南山区', '胡六', '13600001111', '北京市海淀区2号'),
(22, 'SF20260526093000E3F4', '遮阳帽',     0.2,  3000,  '优衣库',     '4001110022', '上海市徐汇区', '胡六', '13600001111', '北京市海淀区2号'),
(23, 'SF20260526103000A5B6', '折叠椅',     4.0,  95000, '迪卡侬',     '4001110023', '苏州市工业园区','胡六','13600001111', '北京市海淀区2号'),
(24, 'SF20260526140000C7D8', '蓝牙音箱',   0.8,  10000, 'JBL旗舰店',  '4001110024', '北京市朝阳区', '胡六', '13600001111', '北京市海淀区2号'),
(25, 'SF20260526160000E9F0', '收纳箱',     1.5,  70000, '宜家家居',   '4001110025', '上海市浦东新区','胡六','13600001111', '北京市海淀区2号');

INSERT INTO inbound_package (package_id, pickup_code, cabinet_type, status, entered_by, enter_time)
VALUES
(21, '05-3-3001', 'SMALL',  'IN_WAREHOUSE', 2, '2026-05-26 08:00:00'),
(22, '11-7-3002', 'SMALL',  'IN_WAREHOUSE', 2, '2026-05-26 09:30:00'),
(23, '60-2-3003', 'LARGE',  'IN_WAREHOUSE', 2, '2026-05-26 10:30:00'),
(24, '29-4-3004', 'SMALL',  'IN_WAREHOUSE', 2, '2026-05-26 14:00:00'),
(25, '41-8-3005', 'MEDIUM', 'IN_WAREHOUSE', 2, '2026-05-26 16:00:00');

-- ============================================================
-- hu 运输中包裹 (仅 package) — 5个
-- ============================================================

INSERT INTO package (id, tracking_number, package_name, weight, volume,
                     sender_name, sender_phone, sender_address,
                     receiver_name, receiver_phone, receiver_address)
VALUES
(26, 'SF20260527090000A1B3', '咖啡豆',     0.5,  4000,  '星巴克',     '4001110026', '上海市黄浦区', '胡六', '13600001111', '北京市海淀区2号'),
(27, 'SF20260527140000C2D4', '充电宝',     0.3,  2500,  '小米旗舰店', '4001110027', '北京市大兴区', '胡六', '13600001111', '北京市海淀区2号'),
(28, 'SF20260528080000E5F7', '拖鞋',       0.35, 5000,  '名创优品',   '4001110028', '广州市越秀区', '胡六', '13600001111', '北京市海淀区2号'),
(29, 'SF20260528100000A6B8', '数据线',     0.08, 1000,  '品胜旗舰店', '4001110029', '深圳市龙岗区', '胡六', '13600001111', '北京市海淀区2号'),
(30, 'SF20260528150000C9D1', '鼠标垫',     0.15, 2000,  '雷蛇旗舰店', '4001110030', '上海市浦东新区','胡六','13600001111', '北京市海淀区2号');

-- ============================================================
-- 验证查询
-- ============================================================
-- SELECT '=== zhang 已入库 ===' AS '';
-- SELECT p.tracking_number, LENGTH(p.tracking_number) AS len, p.package_name, ib.pickup_code, ib.cabinet_type, ib.status
-- FROM package p JOIN inbound_package ib ON p.id = ib.package_id
-- WHERE p.receiver_phone = '13800001111' ORDER BY p.id;

-- SELECT '=== zhang 运输中 ===' AS '';
-- SELECT p.id, p.tracking_number, LENGTH(p.tracking_number) AS len, p.package_name
-- FROM package p LEFT JOIN inbound_package ib ON p.id = ib.package_id
-- WHERE ib.id IS NULL AND p.receiver_phone = '13800001111' ORDER BY p.id;

-- SELECT '=== hu 已入库 ===' AS '';
-- SELECT p.tracking_number, LENGTH(p.tracking_number) AS len, p.package_name, ib.pickup_code, ib.cabinet_type, ib.status
-- FROM package p JOIN inbound_package ib ON p.id = ib.package_id
-- WHERE p.receiver_phone = '13600001111' ORDER BY p.id;

-- SELECT '=== hu 运输中 ===' AS '';
-- SELECT p.id, p.tracking_number, LENGTH(p.tracking_number) AS len, p.package_name
-- FROM package p LEFT JOIN inbound_package ib ON p.id = ib.package_id
-- WHERE ib.id IS NULL AND p.receiver_phone = '13600001111' ORDER BY p.id;
