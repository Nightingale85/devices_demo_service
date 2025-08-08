package com.devices.demo.service;

import com.devices.demo.exception.DeviceInUseException;
import com.devices.demo.exception.DeviceNotFoundException;
import com.devices.demo.dto.CreateDeviceRequest;
import com.devices.demo.dto.DeviceDto;
import com.devices.demo.dto.UpdateDeviceRequest;
import com.devices.demo.mapper.DeviceMapper;
import com.devices.demo.repository.DeviceRepository;

import com.devices.demo.entity.Device;
import com.devices.demo.entity.DeviceState;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    @Override
    @Transactional
    public DeviceDto createDevice(CreateDeviceRequest device) {
        Device newDevice = deviceMapper.toEntity(device);
        Device savedDevice = deviceRepository.save(newDevice);
        return deviceMapper.map(savedDevice);
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceDto getDeviceById(Long id) {
        return deviceRepository.findById(id)
            .map(deviceMapper::map)
            .orElseThrow(() -> new DeviceNotFoundException("Device not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDto> getDevicesByBrand(String brand) {
        List<Device> devices = deviceRepository.findByBrandIgnoreCase(brand);
        return devices.stream()
            .map(deviceMapper::map)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDto> getDevicesByState(DeviceState state) {
        List<Device> devices = deviceRepository.findByState(state);
        return devices.stream()
            .map(deviceMapper::map)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDto> getAllDevices() {
        List<Device> devices = deviceRepository.findAll();
        return devices.stream()
            .map(deviceMapper::map)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DeviceDto updateDevice(Long id, UpdateDeviceRequest device) {
        return deviceRepository.findById(id)
            .map(existingDevice -> {
                if (existingDevice.getState() == DeviceState.IN_USE) {
                    if (device.getName() != null || device.getBrand() != null) {
                        throw new DeviceInUseException("Cannot update name or brand of an 'IN_USE' device");
                    }
                }
                Optional.ofNullable(device.getName()).ifPresent(existingDevice::setName);
                Optional.ofNullable(device.getBrand()).ifPresent(existingDevice::setBrand);
                Optional.ofNullable(device.getState()).ifPresent(existingDevice::setState);

                Device updatedDevice = deviceRepository.save(existingDevice);
                return deviceMapper.map(updatedDevice);
            })
            .orElseThrow(
                () -> new DeviceNotFoundException("Device not found with ID: " + id)
            );
    }

    @Override
    @Transactional
    public void deleteDevice(Long id) {
        Optional<Device> device = deviceRepository.findById(id);
        if (device.isPresent()) {
            if (device.get().getState() == DeviceState.IN_USE) {
                throw new DeviceInUseException("Device in use cannot be deleted.");
            }
            deviceRepository.deleteById(id);
        } else {
            throw new DeviceNotFoundException("Device not found with ID: " + id);
        }
    }
}