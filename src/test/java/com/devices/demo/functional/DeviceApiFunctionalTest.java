package com.devices.demo.functional;

import com.devices.demo.dto.CreateDeviceRequest;
import com.devices.demo.dto.DeviceDto;
import com.devices.demo.dto.UpdateDeviceRequest;
import com.devices.demo.entity.DeviceState;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class DeviceApiFunctionalTest extends BaseFunctionalTest {

    @Test
    void shouldFetchAllDevices() {
        given()
                .contentType(ContentType.JSON)
                .when()
            .get("api/devices")
                .then()
                .statusCode(200)
                .body("content", hasSize(5));
    }

    @Test
    void shouldCreateNewDevice() {
        CreateDeviceRequest request = new CreateDeviceRequest("New Device", "Apple", DeviceState.AVAILABLE);

        DeviceDto createdDevice = given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("/api/devices")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract().as(DeviceDto.class);

        given()
            .contentType(ContentType.JSON)
            .when()
            .get("api/devices")
            .then()
            .statusCode(200)
            .body("content", hasSize(6));

        assertThat(createdDevice.getId()).isNotNull();
        assertThat(createdDevice.getName()).isEqualTo(request.getName());
        assertThat(createdDevice.getBrand()).isEqualTo(request.getBrand());
        assertThat(createdDevice.getState()).isEqualTo(request.getState());
    }

    @Test
    void shouldGetDeviceById() {
        Long deviceId = fetchExistingDeviceId();

        DeviceDto device = given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/devices/{id}", deviceId)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(DeviceDto.class);

        assertThat(device.getId()).isEqualTo(deviceId);
    }

    @Test
    void shouldGetDevicesByBrand() {
        String brand = "BrandX";

        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/devices/brand/{brand}", brand)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(is(2)))
            .body("brand", everyItem(equalTo(brand)));
    }

    @Test
    void shouldGetDevicesByState() {
        DeviceState state = DeviceState.AVAILABLE;

        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/devices/state/{state}", state.name())
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(is(3)))
            .body("state", everyItem(equalTo(state.name())));
    }

    @Test
    void shouldUpdateDeviceSuccessfully() {
        Long deviceId = fetchExistingDeviceId();

        UpdateDeviceRequest updateRequest = new UpdateDeviceRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setBrand("Updated Brand");
        updateRequest.setState(DeviceState.INACTIVE);

        given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
            .when()
            .patch("/api/devices/{id}", deviceId)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(deviceId.intValue()))
            .body("name", equalTo("Updated Name"))
            .body("brand", equalTo("Updated Brand"))
            .body("state", equalTo("INACTIVE"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingDevice() {
        Long nonExistingId = Long.MAX_VALUE;

        UpdateDeviceRequest updateRequest = new UpdateDeviceRequest();
        updateRequest.setName("Name");
        updateRequest.setBrand("Brand");
        updateRequest.setState(DeviceState.AVAILABLE);

        given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
            .when()
            .patch("/api/devices/{id}", nonExistingId)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingInUseDevice() {
        Long inUseDeviceId = fetchInUseDeviceId();

        UpdateDeviceRequest updateRequest = new UpdateDeviceRequest();
        updateRequest.setName("New Name");
        updateRequest.setBrand("New Brand");
        updateRequest.setState(DeviceState.AVAILABLE);

        given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
            .when()
            .patch("/api/devices/{id}", inUseDeviceId)
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldDeleteDevice() {
        Long deviceId = fetchExistingDeviceId();

        given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/api/devices/{id}", deviceId)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldNotDeleteDeviceInUse() {
        Long deviceId = fetchInUseDeviceId();

        given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/api/devices/{id}", deviceId)
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnNotFoundWhileDeviceDelete() {

        Long notExistingDeviceId = Long.MIN_VALUE;

        given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/api/devices/{id}", notExistingDeviceId)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private Long fetchExistingDeviceId() {
        return given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/devices")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .as(new TypeRef<List<DeviceDto>>() {})
            .get(0)
            .getId();
    }

    private Long fetchInUseDeviceId() {
        return given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/devices")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .as(new TypeRef<List<DeviceDto>>() {})
            .stream()
            .filter(d -> d.getState() == DeviceState.IN_USE)
            .findFirst()
            .orElseThrow()
            .getId();
    }
}
