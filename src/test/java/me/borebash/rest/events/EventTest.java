package me.borebash.rest.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import me.borebash.rest.Event;

public class EventTest {
    
    @Test
    public void builer() {
        Event event = Event.builder()
                .name("Name")
                .description("REST API development with SpringBoot")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        Event event = new Event();

        String name = "Name";
        String description = "REST API development with SpringBoot";
        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
    
}