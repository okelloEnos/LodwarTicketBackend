package com.okellosoftwarez.lodwarticketbackend.ticket.dto;

import com.okellosoftwarez.lodwarticketbackend.ticket.entity.Ticket;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.TicketPriority;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.TicketStatus;

import java.time.Instant;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        String title,
        String description,
        TicketStatus status,
        TicketPriority priority,
        Instant createdAt
) {

    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getCreatedAt()
        );
    }
}
