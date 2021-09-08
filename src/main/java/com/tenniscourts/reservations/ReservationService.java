package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.schedules.ScheduleService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final ScheduleService scheduleService;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        ScheduleDTO scheduleDTO =
            scheduleService.findSchedule(createReservationRequestDTO.getScheduleId());
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setReservationStatus(ReservationStatus.READY_TO_PLAY.name());
        reservationDTO.setScheduledId(createReservationRequestDTO.getScheduleId());
        reservationDTO.setSchedule(scheduleDTO);
        reservationDTO.setStartDateTime(scheduleDTO.getStartDateTime());
        reservationDTO.setEndDateTime(scheduleDTO.getStartDateTime().plusHours(1));
        reservationDTO.setGuestId(createReservationRequestDTO.getGuestId());
        reservationDTO.setValue(BigDecimal.valueOf(10));
        return reservationMapper
            .map(reservationRepository.saveAndFlush(reservationMapper.map(reservationDTO)));
    }

    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
            .map(reservationMapper::map).<EntityNotFoundException>orElseThrow(() -> {
                throw new EntityNotFoundException("Reservation not found.");
            });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {
            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);
        }).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(
        Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException(
                "Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        long hours = ChronoUnit.HOURS
            .between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (hours >= 24) {
            return reservation.getValue();
        }

        return BigDecimal.ZERO;
    }

    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        ReservationDTO reservationDTO = findReservation(previousReservationId);
        ScheduleDTO oldSchedule = reservationDTO.getSchedule();
        ScheduleDTO newSchedule = scheduleService.findSchedule(scheduleId);

        if (scheduleId.equals(reservationDTO.getSchedule().getId()) || checkIfTimeSlotIsTheSame(
            oldSchedule, newSchedule)) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }

        Reservation previousReservation = cancel(previousReservationId);

        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
            .guestId(previousReservation.getGuest().getId())
            .scheduleId(scheduleId)
            .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }

    private boolean checkIfTimeSlotIsTheSame(ScheduleDTO oldSchedule, ScheduleDTO newSchedule) {
        return oldSchedule.getStartDateTime().equals(newSchedule.getStartDateTime()) &&
            oldSchedule.getEndDateTime().equals(newSchedule.getEndDateTime());
    }
}
