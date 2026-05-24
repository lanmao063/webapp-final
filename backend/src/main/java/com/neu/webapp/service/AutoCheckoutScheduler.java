package com.neu.webapp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neu.webapp.entity.InboundPackage;
import com.neu.webapp.mapper.InboundPackageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AutoCheckoutScheduler {

    private static final Logger log = LoggerFactory.getLogger(AutoCheckoutScheduler.class);

    private final InboundPackageMapper inboundPackageMapper;

    public AutoCheckoutScheduler(InboundPackageMapper inboundPackageMapper) {
        this.inboundPackageMapper = inboundPackageMapper;
    }

    @Scheduled(cron = "0 0 * * * ?")
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

    @Scheduled(cron = "0 0 2 * * ?")
    public void autoDelete() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        QueryWrapper<InboundPackage> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "CHECKED_OUT")
               .lt("out_time", sevenDaysAgo);

        int count = inboundPackageMapper.delete(wrapper);
        if (count > 0) {
            log.info("Auto-delete: {} checked-out records deleted", count);
        }
    }
}
