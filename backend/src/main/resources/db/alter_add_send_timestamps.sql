ALTER TABLE send_package
  ADD COLUMN approved_at DATETIME COMMENT '审核通过时间',
  ADD COLUMN paid_at DATETIME COMMENT '付款时间',
  ADD COLUMN collected_at DATETIME COMMENT '揽收时间';
