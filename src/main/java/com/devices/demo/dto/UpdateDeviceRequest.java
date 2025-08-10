package com.devices.demo.dto;

import com.devices.demo.entity.DeviceState;
import lombok.Data;
import lombok.Value;

@Data
public class UpdateDeviceRequest {
   String name;
   String brand;
   DeviceState state;
}
