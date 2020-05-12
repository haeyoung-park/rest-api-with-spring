package me.borebash.rest.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import me.borebash.rest.common.TestDescription;

// @WebMvcTest
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    // @MockBean
    // EventRepository eventRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("정상적으로 이벤트를 생성 검증")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder().name("SpringBoot").description("REST API Development with SpringBoot")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 05, 12, 23, 57))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 05, 13, 23, 57))
                .beginEventDateTime(LocalDateTime.of(2020, 05, 12, 23, 57))
                .endEventDateTime(LocalDateTime.of(2020, 05, 13, 23, 57)).basePrice(100).maxPrice(200)
                .limitOfEnrollement(100).location("REST API Center").build();

        // Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/").contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))).andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists()).andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(Matchers.is(EventStatus.DRAFT)));
    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러 발생 검증")
    public void createEvent_BadRequest() throws Exception {
        Event event = Event.builder().id(100).name("SpringBoot").description("REST API Development with SpringBoot")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 05, 12, 23, 57))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 05, 13, 23, 57))
                .beginEventDateTime(LocalDateTime.of(2020, 05, 12, 23, 57))
                .endEventDateTime(LocalDateTime.of(2020, 05, 13, 23, 57)).basePrice(100).maxPrice(200)
                .limitOfEnrollement(100).location("REST API Center").free(true).offline(false)
                .eventStatus(EventStatus.DRAFT).build();

        mockMvc.perform(post("/api/events/").contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 비어있는 경우 에러 검증")
    public void createEvent_BadRequest_EmptyInput() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto))).andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 잘못된 경우 에러 검증 테스트")
    public void createEvent_BadRequest_WrongInput() throws JsonProcessingException, Exception {
        EventDto eventDto =  EventDto.builder()
            .name("SpringBoot")
            .description("REST API Development with SpringBoot")
            .beginEnrollmentDateTime(LocalDateTime.of(2021,05,12,23,57))
            .closeEnrollmentDateTime(LocalDateTime.of(2020,05,13,23,57))
            .beginEventDateTime(LocalDateTime.of(2020,05,12,23,57))
            .endEventDateTime(LocalDateTime.of(2020,05,13,23,57))
            .basePrice(20000)
            .maxPrice(200)
            .limitOfEnrollement(100)
            .location("REST API Center")
            .build();
        
            this.mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(eventDto))
        )
        .andExpect(status().isBadRequest());
    }

}