package com.devices.demo.repository;

import com.devices.demo.entity.Device;
import com.devices.demo.entity.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
  List<Device> findByBrandIgnoreCase(String brand);
  List<Device> findByState(DeviceState state);
}
