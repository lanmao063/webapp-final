package com.neu.webapp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neu.webapp.common.Result;
import com.neu.webapp.service.InboundPackageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inbound")
public class InboundPackageController {

    private final InboundPackageService inboundPackageService;

    public InboundPackageController(InboundPackageService inboundPackageService) {
        this.inboundPackageService = inboundPackageService;
    }

    @PutMapping("/{trackingNumber}/warehouse-entry")
    public Result<String> warehouseEntry(@PathVariable String trackingNumber, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String pickupCode = inboundPackageService.warehouseEntry(trackingNumber, userId);
        return Result.ok(pickupCode);
    }

    @PutMapping("/{trackingNumber}/checkout")
    public Result<Void> checkout(@PathVariable String trackingNumber, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        inboundPackageService.checkout(trackingNumber, userId);
        return Result.ok();
    }

    @GetMapping("/search")
    public Result<Map<String, Object>> search(@RequestParam String trackingNumber, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Map<String, Object> data = inboundPackageService.searchByTrackingNumber(trackingNumber, userId);
        if (data == null) {
            return Result.fail(404, "未找到该包裹或包裹不在库");
        }
        return Result.ok(data);
    }

    @GetMapping("/my-pickup-codes")
    public Result<List<Map<String, Object>>> myPickupCodes(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return Result.ok(inboundPackageService.myPickupCodes(userId));
    }

    @PutMapping("/{id}/authorize-proxy")
    public Result<Void> authorizeProxy(@PathVariable Long id, @RequestBody Map<String, String> body, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        inboundPackageService.authorizeProxy(id, userId, body.get("proxyPhone"));
        return Result.ok();
    }

    @GetMapping("/public-search")
    public Result<Map<String, Object>> publicSearch(@RequestParam String trackingNumber,
                                                     @RequestParam String phone) {
        Map<String, Object> data = inboundPackageService.publicSearch(trackingNumber, phone);
        if (data == null) {
            return Result.fail(404, "未找到该包裹或包裹不在库");
        }
        return Result.ok(data);
    }

    @PutMapping("/public-checkout")
    public Result<Map<String, Object>> publicCheckout(@RequestBody Map<String, String> body) {
        String trackingNumber = body.get("trackingNumber");
        String phone = body.get("phone");
        if (trackingNumber == null || trackingNumber.isBlank()) {
            return Result.fail(400, "快递单号不能为空");
        }
        if (phone == null || phone.isBlank()) {
            return Result.fail(400, "手机号不能为空");
        }
        Map<String, Object> data = inboundPackageService.publicCheckout(trackingNumber, phone);
        return Result.ok(data);
    }

    @GetMapping("/auto-checkout-list")
    public Result<IPage<Map<String, Object>>> autoCheckoutList(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(inboundPackageService.searchAutoCheckout(keyword, page, size));
    }
}
