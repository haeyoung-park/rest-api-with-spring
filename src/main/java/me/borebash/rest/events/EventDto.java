package me.borebash.rest.events;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EventDto {
    
    // Event Name
    private String name;
    // Evnet Description
    private String description;
    // Event Begin Time
    private LocalDateTime beginEnrollmentDateTime;
    // Event Close Time
    private LocalDateTime closeEnrollmentDateTime;
    // Event Start Date
    private LocalDateTime beginEventDateTime;
    // Event End Date
    private LocalDateTime endEventDateTime;
    // Event Location(optional)
    private String location;
    // option
    private int basePrice;
    // option
    private int maxPrice;
    // option
    private int limitOfEnrollement;

}