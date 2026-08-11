package com.okellosoftwarez.lodwarticketbackend.ticket.dto;

import com.okellosoftwarez.lodwarticketbackend.ticket.entity.TicketPriority;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.TicketStatus;
import jakarta.validation.constraints.NotBlank;

public record CreateTicketRequest(
        @NotBlank(message = "Title is required") String title,
        String description,
        TicketStatus status,
        TicketPriority priority
) {
}
