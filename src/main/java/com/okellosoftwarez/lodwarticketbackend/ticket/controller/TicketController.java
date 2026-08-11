package com.okellosoftwarez.lodwarticketbackend.ticket.controller;

import com.okellosoftwarez.lodwarticketbackend.ticket.dto.CreateTicketRequest;
import com.okellosoftwarez.lodwarticketbackend.ticket.dto.TicketResponse;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.Ticket;
import com.okellosoftwarez.lodwarticketbackend.ticket.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService service;

    @PostMapping
    public ResponseEntity<TicketResponse> create(@RequestBody CreateTicketRequest request) {
        Ticket ticket = service.createTicket(request);
        TicketResponse response = TicketResponse.from(ticket);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
