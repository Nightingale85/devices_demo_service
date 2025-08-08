package com.devices.demo.dto;

import com.devices.demo.entity.DeviceState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateDeviceRequest {

  @NotBlank(message = "Device name is required")
  private String name;

  @NotBlank(message = "Brand is required")
  private String brand;

  @NotNull(message = "Initial state is required")
  private DeviceState state;

}
