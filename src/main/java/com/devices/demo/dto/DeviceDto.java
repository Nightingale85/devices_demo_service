package com.devices.demo.dto;

import com.devices.demo.entity.DeviceState;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DeviceDto {
    private Long id;
    private String name;
    private String brand;
    private DeviceState state;
    private LocalDateTime creationTime;
}
