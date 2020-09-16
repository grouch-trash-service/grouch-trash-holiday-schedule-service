package net.mporter.grouch.holiday.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetHolidaysResponse {
    @JsonProperty("data")
    private List<Holiday> data;
}
