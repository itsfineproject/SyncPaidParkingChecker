package itsfine.com.syncpaidparkingchecker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import itsfine.com.syncpaidparkingchecker.dto.Sensor;
import itsfine.com.syncpaidparkingchecker.interfaces.IPaidParkingChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@EnableBinding(IPaidParkingChecker.class)
public class PaidParkingCheckerService {
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    IPaidParkingChecker paidParkingChecker;

    @StreamListener(IPaidParkingChecker.INPUT)
    void checkPaid(String sensorStrData) throws IOException {
        Sensor sensor = mapper.readValue(sensorStrData, Sensor.class);

        if (checkParkingPaidByApi(sensor.car_number)) {
            paidParkingChecker.output().send(MessageBuilder.withPayload(sensorStrData).build());
            paidParkingChecker.notPaidRout().send(MessageBuilder.withPayload(sensorStrData).build());
        }
    }

    private boolean checkParkingPaidByApi(String car_number){

        RestTemplate restTemplate = new RestTemplate();

        //API request
        HttpEntity<String> httpEntity = new HttpEntity<>(null);

        String url = "http://localhost:8989/checkpaid?car_number=" + car_number;

        ResponseEntity<Boolean> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, httpEntity, Boolean.class);
        if (responseEntity.getBody()) return responseEntity.getBody();
        return false;
    }

}
