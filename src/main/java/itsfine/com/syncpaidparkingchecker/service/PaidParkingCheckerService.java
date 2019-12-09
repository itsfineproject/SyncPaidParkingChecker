package itsfine.com.syncpaidparkingchecker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import itsfine.com.syncpaidparkingchecker.dto.ParkObject;
import itsfine.com.syncpaidparkingchecker.interfaces.IPaidParkingChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@EnableBinding(IPaidParkingChecker.class)
public class PaidParkingCheckerService {
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    private final String URL_IS_PAID;

    @Autowired
    public PaidParkingCheckerService(@Value("${urlcheckpaid}") String url) {
        this.URL_IS_PAID = url;
    }

    @Autowired
    IPaidParkingChecker paidParkingChecker;

    @StreamListener(IPaidParkingChecker.INPUT)
    void checkPaid(String parkObjectData) throws IOException {
        ParkObject parkObject = mapper.readValue(parkObjectData, ParkObject.class);

        if (checkParkingPaidByApi(parkObject.car_number)) {
            paidParkingChecker.output().send(MessageBuilder.withPayload(parkObjectData).build());
            paidParkingChecker.notPaidRout().send(MessageBuilder.withPayload(parkObjectData).build());
        }
    }

    private boolean checkParkingPaidByApi(String car_number) {

        RestTemplate restTemplate = new RestTemplate();

        //API request
        HttpEntity<String> httpEntity = new HttpEntity<>(null);

        String url = URL_IS_PAID + car_number;

        try {
            ResponseEntity<Boolean> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, httpEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return false;
    }

}
