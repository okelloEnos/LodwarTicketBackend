package com.okellosoftwarez.lodwarticketbackend.ticket.service;

import com.okellosoftwarez.lodwarticketbackend.ticket.dto.*;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.Ticket;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.TicketPriority;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.TicketStatus;
import com.okellosoftwarez.lodwarticketbackend.ticket.exception.TicketBadRequestException;
import com.okellosoftwarez.lodwarticketbackend.ticket.exception.TicketNotFoundException;
import com.okellosoftwarez.lodwarticketbackend.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository repository;
    private static final int DEFAULT_LIMIT = 20;
    private static final int MAX_LIMIT = 100;

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

    // fetch paginated tickets
    public PageResponse<TicketResponse> fetchTickets(String statusParam, Integer page, Integer limit) {
        int safePage = Math.max(page != null ? page : 1, 1);
        int safeLimit = Math.min(Math.max(limit != null ? limit : DEFAULT_LIMIT, 1), MAX_LIMIT);

        Pageable pageable = PageRequest.of(safePage - 1, safeLimit, Sort.by("createdAt").descending());

        Page<Ticket> result = statusParam != null
                ? repository.findByStatus(TicketStatus.valueOf(statusParam), pageable)
                : repository.findAll(pageable);

        List<TicketResponse> data = result.getContent().stream()
                .map(TicketResponse::from)
                .toList();

        PaginationMeta pagination = new PaginationMeta(
                safePage,
                safeLimit,
                result.getTotalElements(),
                Math.max(result.getTotalPages(), 1)
        );

        return new PageResponse<>(data, pagination);
    }

    // update ticket
    public Ticket updateTicket(UUID id, UpdateTicketRequest request) {
        if (request.status() == null && request.priority() == null) {
            throw new TicketBadRequestException("provide at least one of: status, priority");
        }

        Ticket ticket = repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("ticket not found"));

        if (request.status() != null) {
            ticket.setStatus(TicketStatus.valueOf(request.status()));
        }
        if (request.priority() != null) {
            ticket.setPriority(TicketPriority.valueOf(request.priority()));
        }

        return repository.save(ticket);
    }
}
