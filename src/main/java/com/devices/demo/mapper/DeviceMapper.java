package com.devices.demo.mapper;

import com.devices.demo.dto.CreateDeviceRequest;
import com.devices.demo.dto.DeviceDto;
import com.devices.demo.entity.Device;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    DeviceDto map(Device device);
    Device toEntity(CreateDeviceRequest device);
}
