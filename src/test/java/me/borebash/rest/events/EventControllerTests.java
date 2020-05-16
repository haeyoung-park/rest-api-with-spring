package me.borebash.rest.events;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
// import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
//import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import me.borebash.rest.common.RestDocsConfiguration;
import me.borebash.rest.common.TestDescription;
// @WebMvcTest
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
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
        EventDto eventDto = EventDto.builder().name("SpringBoot").description("REST API Development with SpringBoot")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 05, 12, 23, 57))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 05, 13, 23, 57))
                .beginEventDateTime(LocalDateTime.of(2020, 05, 12, 23, 57))
                .endEventDateTime(LocalDateTime.of(2020, 05, 13, 23, 57))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("REST API Center")
                .build();

        // Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
            .andExpect(jsonPath("free").value(false))
            .andExpect(jsonPath("offline").value(true))
            .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.update-event").exists())
            .andExpect(jsonPath("_links.query-events").exists())
            .andDo(document("create-event",
                    links(
                        linkWithRel("self").description("link to self"),
                        linkWithRel("query-events").description("link to query events"),
                        linkWithRel("update-event").description("link to update an existing event")

                    ),
                    requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                    ),
                    requestFields(
                        fieldWithPath("name").description("Name of new evnet"),
                        fieldWithPath("description").description("Description of new event"),
                        fieldWithPath("beginEnrollmentDateTime").description("Date time of begin of new event"),
                        fieldWithPath("closeEnrollmentDateTime").description("Date time of close of new event"),
                        fieldWithPath("beginEventDateTime").description("Date time of begin of new event"),
                        fieldWithPath("endEventDateTime").description("Date time of end of new event"),
                        fieldWithPath("location").description("location of new event"),
                        fieldWithPath("basePrice").description("base price of new event"),
                        fieldWithPath("maxPrice").description("max price of new event"),
                        fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                    ),
                    responseHeaders(
                        headerWithName(HttpHeaders.LOCATION).description("Location header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
                    ),
                    responseFields(
                        fieldWithPath("id").description("Identifier of new evnet"),
                        fieldWithPath("name").description("Name of new event"),
                        fieldWithPath("description").description("Description of new event"),
                        fieldWithPath("beginEnrollmentDateTime").description("Date time of begin of new event"),
                        fieldWithPath("closeEnrollmentDateTime").description("Date time of close of new event"),
                        fieldWithPath("beginEventDateTime").description("Date time of begin of new event"),
                        fieldWithPath("endEventDateTime").description("Date time of end of new event"),
                        fieldWithPath("location").description("location of new event"),
                        fieldWithPath("basePrice").description("base price of new event"),
                        fieldWithPath("maxPrice").description("max price of new event"),
                        fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                        fieldWithPath("free").description("It tells if this event is free or not"),
                        fieldWithPath("offline").description("It tells if event is offline event or not"),
                        fieldWithPath("eventStatus").description("Event Status"),
                    
                        fieldWithPath("_links.self.href").description("Link to self"),
                        fieldWithPath("_links.query-events.href").description("Link to query event list"),
                        fieldWithPath("_links.update-event.href").description("Link to update event  exiting event")
                    )

            ));

    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러 발생 검증")
    public void createEvent_BadRequest() throws Exception {
        Event event = Event.builder().id(100).name("SpringBoot").description("REST API Development with SpringBoot")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 05, 12, 23, 57))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 05, 13, 23, 57))
                .beginEventDateTime(LocalDateTime.of(2020, 05, 12, 23, 57))
                .endEventDateTime(LocalDateTime.of(2020, 05, 13, 23, 57)).basePrice(100).maxPrice(200)
                .limitOfEnrollment(100).location("REST API Center").free(true).offline(false)
                .eventStatus(EventStatus.DRAFT).build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status()
                .isBadRequest());
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
    public void createEvent_BadRequest_WrongInput() throws Exception {
        EventDto eventDto =  EventDto.builder()
            .name("SpringBoot")
            .description("REST API Development with SpringBoot")
            .beginEnrollmentDateTime(LocalDateTime.of(2021,05,12,23,57))
            .closeEnrollmentDateTime(LocalDateTime.of(2020,05,13,23,57))
            .beginEventDateTime(LocalDateTime.of(2020,05,12,23,57))
            .endEventDateTime(LocalDateTime.of(2020,05,13,23,57))
            .basePrice(10000)
            .maxPrice(200)
            .limitOfEnrollment(100)
            .location("REST API Center")
            .build();
        
        this.mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(eventDto)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].objectName").exists())
        .andExpect(jsonPath("$[0].defaultMessage").exists())
        .andExpect(jsonPath("$[0].code").exists());

    }

}