package com.neu.webapp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neu.webapp.common.Result;
import com.neu.webapp.service.PackageService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/package")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping("/track")
    public Result<Map<String, Object>> track(@RequestParam String trackingNumber) {
        Map<String, Object> data = packageService.track(trackingNumber);
        if (data == null) {
            return Result.fail(404, "包裹不存在");
        }
        return Result.ok(data);
    }

    @GetMapping("/search")
    public Result<IPage<Map<String, Object>>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(packageService.search(keyword, page, size));
    }
}
