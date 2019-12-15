package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import itsfine.com.syncpaidparkingchecker.dto.ParkObject;
import itsfine.com.syncpaidparkingchecker.service.PaidParkingCheckerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootApplication(scanBasePackages = {"itsfine.com.syncpaidparkingchecker.service"})
class PaidParkingCheckerServiceTest {
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    private static final String CAR_NUMBER1 = "11122333";
    private static final String CAR_NUMBER2 = "1122233";

    ParkObject parkObject1 = new ParkObject(1, CAR_NUMBER1, LocalDateTime.now());
    ParkObject parkObject2 = new ParkObject(1, CAR_NUMBER2, LocalDateTime.now());

    String parkObjectData1;
    String parkObjectData2;

    ConfigurableApplicationContext configurableApplicationContext;
    PaidParkingCheckerService service;

    PaidParkingCheckerServiceTest() throws JsonProcessingException {
        this.parkObjectData1 = mapper.writeValueAsString(parkObject1);
        this.parkObjectData2 = mapper.writeValueAsString(parkObject2);
    }

    @BeforeEach
    void setUp() {
        configurableApplicationContext = SpringApplication.run(PaidParkingCheckerServiceTest.class);
        service = configurableApplicationContext.getBean(PaidParkingCheckerService.class);
    }

    @AfterEach
    void tearDown() {
        configurableApplicationContext.close();
    }

    @Test
    void isValidToSendData() {
        assertTrue(service.isValidToSendData(parkObjectData1, true));
        assertFalse(service.isValidToSendData(parkObjectData2, false));
    }
}