package itsfine.com.syncpaidparkingchecker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class Sensor {
    public int parking_id;
    public String car_number;
    public LocalDateTime date_time;
}
