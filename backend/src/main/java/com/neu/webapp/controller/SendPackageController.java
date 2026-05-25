package com.neu.webapp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neu.webapp.common.Result;
import com.neu.webapp.dto.SendPackageRequest;
import com.neu.webapp.entity.SendPackage;
import com.neu.webapp.service.SendPackageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/send-package")
public class SendPackageController {

    private final SendPackageService sendPackageService;

    public SendPackageController(SendPackageService sendPackageService) {
        this.sendPackageService = sendPackageService;
    }

    @PostMapping
    public Result<SendPackage> create(@RequestBody SendPackageRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return Result.ok(sendPackageService.createSendPackage(request, userId));
    }

    @GetMapping("/{id}")
    public Result<SendPackage> getById(@PathVariable Long id) {
        SendPackage sp = sendPackageService.getById(id);
        if (sp == null) {
            return Result.fail(404, "寄件订单不存在");
        }
        return Result.ok(sp);
    }

    @GetMapping("/search")
    public Result<IPage<SendPackage>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(sendPackageService.search(keyword, status, page, size));
    }

    @GetMapping("/pending-list")
    public Result<IPage<SendPackage>> pendingList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(sendPackageService.pendingList(page, size));
    }

    @PutMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable Long id, @RequestBody SendPackage updateData, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        sendPackageService.approve(id, updateData, userId);
        return Result.ok();
    }

    @PutMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        sendPackageService.reject(id, userId);
        return Result.ok();
    }

    @GetMapping("/my-unpaid")
    public Result<IPage<SendPackage>> myUnpaid(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return Result.ok(sendPackageService.myUnpaid(userId, page, size));
    }

    @PutMapping("/{id}/pay")
    public Result<Void> pay(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        sendPackageService.pay(id, userId);
        return Result.ok();
    }

    @GetMapping("/paid-list")
    public Result<IPage<SendPackage>> paidList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return Result.ok(sendPackageService.paidList(userId, page, size));
    }

    @PutMapping("/{id}/collect")
    public Result<Void> collect(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        sendPackageService.collect(id, userId);
        return Result.ok();
    }
}
