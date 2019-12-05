package itsfine.com.syncpaidparkingchecker.interfaces;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;

public interface IPaidParkingChecker extends Processor {

    @Output("output2")
    MessageChannel notPaidRout();

}
