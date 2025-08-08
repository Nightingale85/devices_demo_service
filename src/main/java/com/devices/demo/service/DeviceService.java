package com.devices.demo.service;

import com.devices.demo.dto.CreateDeviceRequest;
import com.devices.demo.dto.DeviceDto;

import com.devices.demo.dto.UpdateDeviceRequest;
import com.devices.demo.entity.DeviceState;
import java.util.List;

public interface DeviceService {

    DeviceDto createDevice(CreateDeviceRequest device);

    DeviceDto getDeviceById(Long id);

    List<DeviceDto> getDevicesByBrand(String brand);

    List<DeviceDto> getDevicesByState(DeviceState state);

    List<DeviceDto> getAllDevices();

    DeviceDto updateDevice(Long id, UpdateDeviceRequest device);

    void deleteDevice(Long id);
}
