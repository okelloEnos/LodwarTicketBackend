package com.okellosoftwarez.lodwarticketbackend.ticket.service;

import com.okellosoftwarez.lodwarticketbackend.ticket.dto.CreateTicketRequest;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.Ticket;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.TicketPriority;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.TicketStatus;
import com.okellosoftwarez.lodwarticketbackend.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository repository;

    // create ticket
    public Ticket createTicket(CreateTicketRequest request) {

        Ticket ticket = Ticket.builder()
                .title(request.title())
                .description(request.description())
                .status(request.status() != null ? request.status() : TicketStatus.OPEN)
                .priority(request.priority() != null ? request.priority() : TicketPriority.MEDIUM)
                .build();

        return repository.save(ticket);

    }
}
