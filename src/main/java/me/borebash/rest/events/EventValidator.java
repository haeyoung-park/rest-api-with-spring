package me.borebash.rest.events;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator {
    
    public void validate(EventDto eventDto,  Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is Wrong");
            errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is Wrong");
        }

        LocalDateTime endEventTime = eventDto.getEndEventDateTime();

        if (endEventTime.isBefore(eventDto.getBeginEventDateTime()) ||
        endEventTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
        endEventTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is Wrong");
        }

        // TODO BeginEventDateTime
        // TODO CloseEnrollmentDateTime

    }

}