package com.okellosoftwarez.lodwarticketbackend.ticket.service;

import com.okellosoftwarez.lodwarticketbackend.ticket.dto.CreateTicketRequest;
import com.okellosoftwarez.lodwarticketbackend.ticket.dto.PageResponse;
import com.okellosoftwarez.lodwarticketbackend.ticket.dto.PaginationMeta;
import com.okellosoftwarez.lodwarticketbackend.ticket.dto.TicketResponse;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.Ticket;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.TicketPriority;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.TicketStatus;
import com.okellosoftwarez.lodwarticketbackend.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
//                ? repository.findByStatus(TicketStatus.fromValue(statusParam), pageable)
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
}
