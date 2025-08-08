package com.devices.demo.functional;

import com.devices.demo.entity.Device;
import com.devices.demo.entity.DeviceState;
import com.devices.demo.repository.DeviceRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BaseFunctionalTest {

	@LocalServerPort
	private Integer port;

	@Autowired
	private DeviceRepository deviceRepository;

	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
			"postgres:16-alpine"
	);

	@BeforeAll
	static void beforeAll() {
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

	@BeforeEach
	void setUp() {
		deviceRepository.deleteAll();
		RestAssured.baseURI = "http://localhost:" + port;
		prepareDevicesData();
	}

	private void prepareDevicesData() {
		Device device1 = Device.builder()
				.name("Device 1")
				.brand("BrandX")
				.state(DeviceState.AVAILABLE)
				.build();

		Device device2 = Device.builder()
				.name("Device 2")
				.brand("BrandY")
				.state(DeviceState.IN_USE)
				.build();

		Device device3 = Device.builder()
				.name("Device 3")
				.brand("BrandX")
				.state(DeviceState.AVAILABLE)
				.build();

		Device device4 = Device.builder()
				.name("Device 4")
				.brand("BrandZ")
				.state(DeviceState.AVAILABLE)
				.build();

		Device device5 = Device.builder()
				.name("Device 5")
				.brand("BrandY")
				.state(DeviceState.IN_USE)
				.build();

		deviceRepository.saveAll(List.of(device1, device2, device3, device4, device5));
	}
}
