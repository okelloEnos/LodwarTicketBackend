package com.okellosoftwarez.lodwarticketbackend.ticket.repository;

import com.okellosoftwarez.lodwarticketbackend.ticket.entity.Ticket;
import com.okellosoftwarez.lodwarticketbackend.ticket.entity.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Page<Ticket> findByStatus(TicketStatus status, Pageable pageable);

    @Query("SELECT t.status AS status, COUNT(t) AS count FROM Ticket t GROUP BY t.status")
    List<StatusCount> countByStatus();
}
