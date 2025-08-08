package com.devices.demo.exception;

public class DeviceInUseException extends RuntimeException {

  public DeviceInUseException(String message) {
    super(message);
  }
}
