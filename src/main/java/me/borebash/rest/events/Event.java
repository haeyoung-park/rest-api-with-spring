package me.borebash.rest.events;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = {"id", "name"})
public class Event {

    @Id @GeneratedValue
    private Integer id;
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
    // options
    private int basePrice;
    // options
    private int maxPrice;
    private int limitOfEnrollement;
    private boolean offline;
    private boolean free;
    private EventStatus eventStatus;
}