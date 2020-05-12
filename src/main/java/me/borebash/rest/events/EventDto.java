package me.borebash.rest.events;

import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EventDto {
    
    // Event Name
    @NotEmpty
    private String name;
    // Evnet Description
    @NotEmpty
    private String description;
    // Event Begin Time
    @NotNull
    private LocalDateTime beginEnrollmentDateTime;
    // Event Close Time
    @NotNull
    private LocalDateTime closeEnrollmentDateTime;
    // Event Start Date
    @NotNull
    private LocalDateTime beginEventDateTime;
    // Event End Date
    @NotNull
    private LocalDateTime endEventDateTime;
    // Event Location(optional)
    @NotNull
    private String location;
    @Min(0)
    private int basePrice;
    @Min(0)
    private int maxPrice;
    @Min(0)
    private int limitOfEnrollement;

}