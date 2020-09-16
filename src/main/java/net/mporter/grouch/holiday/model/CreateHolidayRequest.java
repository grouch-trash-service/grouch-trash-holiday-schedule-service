package net.mporter.grouch.holiday.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateHolidayRequest {
    @JsonProperty("data")
    private Holiday data;
}
