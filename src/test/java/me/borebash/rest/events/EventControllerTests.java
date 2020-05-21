package me.borebash.rest.events;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
// import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.junit.Test;
//import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import me.borebash.rest.common.BaseControllerTest;
import me.borebash.rest.common.TestDescription;

// @WebMvcTest

public class EventControllerTests extends BaseControllerTest {

    // @MockBean
    // EventRepository eventRepository;

    @Autowired
    EventRepository eventRepository;

    @Test
    @TestDescription("정상인 이벤트 생성")
    public void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("SpringBoot")
                .description("REST API Development with SpringBoot")
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

            .andDo(document("create-event",
                    links(
                        linkWithRel("self").description("link to self"),
                        linkWithRel("query-events").description("link to query events"),
                        linkWithRel("update-event").description("link to update an existing event"),
                        linkWithRel("profile").description("link to update an existing event")

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
                        fieldWithPath("_links.update-event.href").description("Link to update event exiting event"),

                        fieldWithPath("_links.profile.href").description("Link to profile")
                    )

            ));

    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우")
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
    @TestDescription("입력 값이 비어있는 경우")
    public void createEvent_BadRequest_EmptyInput() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto))).andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 잘못된 경우")
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
        .andExpect(jsonPath("content[0].objectName").exists())
        .andExpect(jsonPath("content[0].defaultMessage").exists())
        .andExpect(jsonPath("content[0].code").exists())
        .andExpect(jsonPath("_links.index").exists());

    }

    @Test
    @TestDescription("30개 이벤트를 10개씩 조회(페이징)")
    public void queryEvents() throws Exception{
        // Given
        IntStream.range(0, 30).forEach(this::generateEvent);

        // When & Then
        this.mockMvc.perform(get("/api/events")
                    .param("page", "1") // paging & sorting
                    .param("size", "10")
                    .param("sort", "name,DESC")) 
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("page").exists())
            .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.profile").exists())
            .andDo(document("query-events", 
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
                ),
                relaxedResponseFields(
                    fieldWithPath("_links.first").description("Move to the first page"),
                    fieldWithPath("_links.prev").description("Move to the next page"),
                    fieldWithPath("_links.self").description("Move to the current page"),
                    fieldWithPath("_links.next").description("Move to the next page"),
                    fieldWithPath("_links.last").description("Move to the last page"),
                    fieldWithPath("_links.profile").description("Move to the profile page"),

                    fieldWithPath("page.size").description("Number of posts shown"),
                    fieldWithPath("page.totalElements").description("Number of all posts"),
                    fieldWithPath("page.totalPages").description("Number of all posts"),
                    fieldWithPath("page.number").description("Current Number")
                )
            ));
            ;
    }

    @Test
    @TestDescription("기존 이벤트 한 개 조회")
    public void getEvent() throws Exception {
        // Given
        Event event = this.generateEvent(100);

        // When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("name").exists())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.profile").exists())

            .andDo(document("get-and-event")
            )
            ;
    }

    @Test
    @TestDescription("없는 이벤트 조회 -> Response 404")
    public void getEvent404() throws Exception {
        // When & Then
        this.mockMvc.perform(get("/api/events/99999"))
            .andDo(print())
            .andExpect(status().isNotFound())
        ;
    }

    @Test
    @TestDescription("이벤트 정상 수정")
    public void updateEvent() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        String updateName = "Updated Event";
        eventDto.setName(updateName);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(eventDto))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("name").value(updateName))
            .andExpect(jsonPath("_links.self").exists())
            ;
    }

    @Test
    @TestDescription("입력 값이 없는 경우 수정 실패")
    public void updateEvent400_Empty() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = new EventDto();

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsBytes(eventDto))
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            ;
    }

    @Test
    @TestDescription("입력 값이 잘못된 경우 수정 실패")
    public void updateEvent400_Wrong() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(2000);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsBytes(eventDto))
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            ;
    }

    @Test
    @TestDescription("존재하지 않는 이벤트 수정 실패")
    public void updateEvent404() throws Exception {
        // Givent
        Event event = this.generateEvent(100);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        // When & Then
        this.mockMvc.perform(put("/api/events/123456789")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsBytes(eventDto))
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            ;
    }


    private Event generateEvent(int i) {
        Event event = Event.builder()
            .name("name" + i)
            .description("test event")
            .beginEnrollmentDateTime(LocalDateTime.of(2020, 05, 12, 23, 57))
            .closeEnrollmentDateTime(LocalDateTime.of(2020, 05, 13, 23, 57))
            .beginEventDateTime(LocalDateTime.of(2020, 05, 12, 23, 57))
            .endEventDateTime(LocalDateTime.of(2020, 05, 13, 23, 57))
            .basePrice(100)
            .maxPrice(200)
            .limitOfEnrollment(100)
            .location("REST API Center")
            .free(false)
            .offline(false)
            .eventStatus(EventStatus.DRAFT)
            .build();
        
        return this.eventRepository.save(event);
    }

}