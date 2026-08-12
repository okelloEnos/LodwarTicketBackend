package com.okellosoftwarez.lodwarticketbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okellosoftwarez.lodwarticketbackend.ticket.controller.TicketController;
import com.okellosoftwarez.lodwarticketbackend.ticket.dto.CreateTicketRequest;
import com.okellosoftwarez.lodwarticketbackend.ticket.dto.PageResponse;
import com.okellosoftwarez.lodwarticketbackend.ticket.dto.PaginationMeta;
import com.okellosoftwarez.lodwarticketbackend.ticket.dto.TicketResponse;
import com.okellosoftwarez.lodwarticketbackend.ticket.dto.UpdateTicketRequest;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.Ticket;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.TicketPriority;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.TicketStatus;
import com.okellosoftwarez.lodwarticketbackend.ticket.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TicketService ticketService;

    @Test
    void createTicket() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest(
                "Test title",
                "Test description",
                TicketStatus.OPEN,
                TicketPriority.HIGH
        );

        UUID id = UUID.randomUUID();
        Ticket saved = Ticket.builder()
                .id(id)
                .title(request.title())
                .description(request.description())
                .status(request.status())
                .priority(request.priority())
                .build();

        when(ticketService.createTicket(any(CreateTicketRequest.class))).thenReturn(saved);

        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Test title"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        verify(ticketService).createTicket(any(CreateTicketRequest.class));
    }

    @Test
    void fetchTickets() throws Exception {
        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .title("Test title")
                .description("Test description")
                .status(TicketStatus.OPEN)
                .priority(TicketPriority.MEDIUM)
                .build();

        PageResponse<TicketResponse> page = new PageResponse<>(
                List.of(TicketResponse.from(ticket)),
                new PaginationMeta(1, 10, 1, 1)
        );

        when(ticketService.fetchTickets("OPEN", 1, 10)).thenReturn(page);

        mockMvc.perform(get("/tickets")
                        .param("status", "OPEN")
                        .param("page", "1")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].title").value("Test title"))
                .andExpect(jsonPath("$.data[0].status").value("OPEN"))
                .andExpect(jsonPath("$.data[0].priority").value("MEDIUM"))
                .andExpect(jsonPath("$.pagination.total").value(1))
                .andExpect(jsonPath("$.pagination.page").value(1));

        verify(ticketService).fetchTickets("OPEN", 1, 10);
    }

    @Test
    void updateTicketStatusAndPriority() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateTicketRequest request = new UpdateTicketRequest("CLOSED", "HIGH");

        Ticket updated = Ticket.builder()
                .id(id)
                .title("Test title")
                .description("Test description")
                .status(TicketStatus.CLOSED)
                .priority(TicketPriority.HIGH)
                .build();

        when(ticketService.updateTicket(eq(id), any(UpdateTicketRequest.class))).thenReturn(updated);

        mockMvc.perform(patch("/tickets/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.status").value("CLOSED"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        verify(ticketService).updateTicket(eq(id), any(UpdateTicketRequest.class));
    }

    @Test
    void statusCount() throws Exception {
        when(ticketService.fetchStatusCount()).thenReturn(Map.of(
                "OPEN", 4L,
                "IN_PROGRESS", 2L,
                "CLOSED", 9L
        ));

        mockMvc.perform(get("/tickets/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.OPEN").value(4))
                .andExpect(jsonPath("$.IN_PROGRESS").value(2))
                .andExpect(jsonPath("$.CLOSED").value(9));

        verify(ticketService).fetchStatusCount();
    }
}