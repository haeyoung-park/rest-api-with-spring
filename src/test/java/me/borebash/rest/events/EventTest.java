package me.borebash.rest.events;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.Assert.assertThat;
import org.junit.Test;

// @RunWith(JUnitParamsRunner.class) 실행 시 에러 발생
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

    @Test
//  @Parameters(method = "paramsForTestFree") 실행 시 에러 발생
    public void testFree() {
        // Given
        Event event = Event.builder()
                .basePrice(0) // 100 -> false
                .maxPrice(0) // 100 -> false
                .build();
        // When
        event.update();
        
        // Then
        assertThat(event.isFree()).isTrue();
    }

    @Test
    public void testOffline() {
        //Given
        Event event = Event.builder()
         .location("강남역 네이버 D2 Starter Factory")
         .build();
        // When
        event.update();
        // Then
        assertThat(event.isOffline()).isTrue();

        //Given
        event = Event.builder()
         .build();
        // When
        event.update();
        // Then
        assertThat(event.isOffline()).isFalse();
    }

}