package com.devices.demo.controller;

import com.devices.demo.dto.CreateDeviceRequest;
import com.devices.demo.dto.DeviceDto;
import com.devices.demo.dto.UpdateDeviceRequest;
import com.devices.demo.entity.DeviceState;
import com.devices.demo.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Device", description = "Device management API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    @Operation(summary = "Create a new device", description = "Creates a new device and returns the created device details")
    @ApiResponse(responseCode = "201", description = "Device created successfully", content = @Content(schema = @Schema(implementation = DeviceDto.class)))
    @PostMapping
    public ResponseEntity<DeviceDto> createDevice(@RequestBody @Valid CreateDeviceRequest device) {
        DeviceDto createdDevice = deviceService.createDevice(device);
        return new ResponseEntity<>(createdDevice, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a device by ID", description = "Returns a device as per the id")
    @ApiResponse(responseCode = "200", description = "Device found", content = @Content(schema = @Schema(implementation = DeviceDto.class)))
    @ApiResponse(responseCode = "404", description = "Device not found")
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDeviceById(@Parameter(description = "ID of the device to be obtained") @PathVariable Long id) {
        DeviceDto device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @Operation(summary = "Get devices by brand", description = "Returns a list of devices for a given brand")
    @ApiResponse(responseCode = "200", description = "Devices found", content = @Content(schema = @Schema(implementation = DeviceDto.class)))
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<DeviceDto>> getDevicesByBrand(@Parameter(description = "Brand of the devices to be obtained") @PathVariable String brand) {
        List<DeviceDto> devices = deviceService.getDevicesByBrand(brand);
        return ResponseEntity.ok(devices);
    }

    @Operation(summary = "Get devices by state", description = "Returns a list of devices for a given state")
    @ApiResponse(responseCode = "200", description = "Devices found", content = @Content(schema = @Schema(implementation = DeviceDto.class)))
    @GetMapping("/state/{state}")
    public ResponseEntity<List<DeviceDto>> getDevicesByState(@Parameter(description = "State of the devices to be obtained") @PathVariable DeviceState state) {
        List<DeviceDto> devices = deviceService.getDevicesByState(state);
        return ResponseEntity.ok(devices);
    }

    @Operation(summary = "Get all devices", description = "Returns a list of all devices")
    @ApiResponse(responseCode = "200", description = "Devices found", content = @Content(schema = @Schema(implementation = DeviceDto.class)))
    @GetMapping
    public ResponseEntity<List<DeviceDto>> getAllDevices() {
        List<DeviceDto> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @Operation(summary = "Update a device", description = "Updates an existing device and returns the updated device details")
    @ApiResponse(responseCode = "200", description = "Device updated successfully", content = @Content(schema = @Schema(implementation = DeviceDto.class)))
    @ApiResponse(responseCode = "404", description = "Device not found")
    @ApiResponse(responseCode = "400", description = "Cannot update device in use")
    @PatchMapping("/{id}")
    public ResponseEntity<DeviceDto> updateDevice(
        @Parameter(description = "ID of the device to be updated") @PathVariable Long id,
        @RequestBody @Valid UpdateDeviceRequest device) {
        DeviceDto updatedDevice = deviceService.updateDevice(id, device);
        return ResponseEntity.ok(updatedDevice);
    }

    @Operation(summary = "Delete a device", description = "Deletes an existing device")
    @ApiResponse(responseCode = "204", description = "Device deleted successfully")
    @ApiResponse(responseCode = "404", description = "Device not found")
    @ApiResponse(responseCode = "400", description = "Cannot delete device in use")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@Parameter(description = "ID of the device to be deleted") @PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
