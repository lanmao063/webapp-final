package com.neu.webapp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neu.webapp.entity.InboundPackage;
import com.neu.webapp.entity.SendPackage;
import com.neu.webapp.mapper.InboundPackageMapper;
import com.neu.webapp.mapper.PackageMapper;
import com.neu.webapp.mapper.SendPackageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AutoCheckoutScheduler {

    private static final Logger log = LoggerFactory.getLogger(AutoCheckoutScheduler.class);

    private final InboundPackageMapper inboundPackageMapper;
    private final PackageMapper packageMapper;
    private final SendPackageMapper sendPackageMapper;

    public AutoCheckoutScheduler(InboundPackageMapper inboundPackageMapper,
                                  PackageMapper packageMapper,
                                  SendPackageMapper sendPackageMapper) {
        this.inboundPackageMapper = inboundPackageMapper;
        this.packageMapper = packageMapper;
        this.sendPackageMapper = sendPackageMapper;
    }

    @Scheduled(cron = "0 0 * * * ?")//每小时整点执行一次自动出库任务
    public void autoCheckout() {
        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);
        InboundPackage update = new InboundPackage();
        update.setStatus("CHECKED_OUT");
        update.setIsAutoCheckout(1);
        update.setOutTime(LocalDateTime.now());
        update.setAutoCheckoutTime(LocalDateTime.now());

        QueryWrapper<InboundPackage> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "IN_WAREHOUSE")
               .lt("enter_time", fiveDaysAgo);

        int count = inboundPackageMapper.update(update, wrapper);
        if (count > 0) {
            log.info("Auto-checkout: {} packages checked out", count);
        }
    }

    @Scheduled(cron = "0 0 2 * * ?")//每天凌晨2点执行一次自动删除包裹入库任务
    @Transactional
    public void autoDelete() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        QueryWrapper<InboundPackage> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "CHECKED_OUT")
               .lt("out_time", sevenDaysAgo);

        List<InboundPackage> expiredList = inboundPackageMapper.selectList(wrapper);
        if (expiredList.isEmpty()) {
            return;
        }

        List<Long> packageIds = expiredList.stream()
                .map(InboundPackage::getPackageId)
                .collect(Collectors.toList());

        packageMapper.deleteByIds(packageIds);
        int count = inboundPackageMapper.delete(wrapper);

        log.info("Auto-delete: {} checked-out records and {} package records deleted", count, packageIds.size());
    }

    @Scheduled(cron = "0 30 2 * * ?")
    @Transactional
    public void autoDeleteSendPackages() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        QueryWrapper<SendPackage> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "COLLECTED")
               .lt("collected_at", sevenDaysAgo);

        List<SendPackage> expiredList = sendPackageMapper.selectList(wrapper);
        if (expiredList.isEmpty()) {
            return;
        }

        List<Long> packageIds = expiredList.stream()
                .map(SendPackage::getPackageId)
                .collect(Collectors.toList());

        packageMapper.deleteByIds(packageIds);
        int count = sendPackageMapper.delete(wrapper);

        log.info("Auto-delete send: {} collected records and {} package records deleted", count, packageIds.size());
    }
}
