package com.devices.demo.dto;

import com.devices.demo.entity.DeviceState;
import lombok.Data;

@Data
public class UpdateDeviceRequest {

  private String name;
  private String brand;
  private DeviceState state;

}
