package com.example.Project.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
    @JsonProperty("name")
    private String name;
    @JsonProperty("wins")
    private Integer wins;
    @JsonProperty("age")
    private Integer age;
}
