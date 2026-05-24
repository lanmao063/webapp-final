package com.neu.webapp.controller;

import com.neu.webapp.common.Result;
import com.neu.webapp.entity.InboundPackage;
import com.neu.webapp.entity.Package;
import com.neu.webapp.mapper.InboundPackageMapper;
import com.neu.webapp.mapper.PackageMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Value("${admin.passcode}")
    private String adminPasscode;

    private final PackageMapper packageMapper;
    private final InboundPackageMapper inboundPackageMapper;

    public AdminController(PackageMapper packageMapper, InboundPackageMapper inboundPackageMapper) {
        this.packageMapper = packageMapper;
        this.inboundPackageMapper = inboundPackageMapper;
    }

    @PostMapping("/package-init")
    public Result<Map<String, Object>> packageInit(@RequestBody Map<String, Object> body) {
        String passcode = (String) body.get("passcode");
        if (passcode == null || !passcode.equals(adminPasscode)) {
            return Result.fail(403, "通行码错误");
        }

        Package pkg = new Package();
        pkg.setTrackingNumber((String) body.get("trackingNumber"));
        pkg.setPackageName((String) body.get("packageName"));
        pkg.setWeight(body.get("weight") != null ? Double.valueOf(body.get("weight").toString()) : null);
        pkg.setVolume(body.get("volume") != null ? Double.valueOf(body.get("volume").toString()) : null);
        pkg.setSenderName((String) body.get("senderName"));
        pkg.setSenderPhone((String) body.get("senderPhone"));
        pkg.setSenderAddress((String) body.get("senderAddress"));
        pkg.setReceiverName((String) body.get("receiverName"));
        pkg.setReceiverPhone((String) body.get("receiverPhone"));
        pkg.setReceiverAddress((String) body.get("receiverAddress"));
        pkg.setNotes((String) body.get("notes"));
        packageMapper.insert(pkg);

        boolean createInbound = Boolean.TRUE.equals(body.get("createInbound"));
        InboundPackage inbound = null;
        if (createInbound) {
            inbound = new InboundPackage();
            inbound.setPackageId(pkg.getId());
            inbound.setStatus("PENDING");
            // 根据体积预分配柜型
            if (pkg.getVolume() != null) {
                if (pkg.getVolume() < 12000) {
                    inbound.setCabinetType("SMALL");
                } else if (pkg.getVolume() > 125000) {
                    inbound.setCabinetType("LARGE");
                } else {
                    inbound.setCabinetType("MEDIUM");
                }
            }
            inboundPackageMapper.insert(inbound);
        }

        Map<String, Object> result = Map.of(
            "id", pkg.getId(),
            "trackingNumber", pkg.getTrackingNumber(),
            "inboundId", inbound != null ? inbound.getId() : null
        );
        return Result.ok(result);
    }
}
