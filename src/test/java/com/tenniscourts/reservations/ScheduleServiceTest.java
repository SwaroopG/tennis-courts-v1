package com.tenniscourts.reservations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.tenniscourts.schedules.CreateScheduleRequestDTO;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.schedules.ScheduleMapper;
import com.tenniscourts.schedules.ScheduleRepository;
import com.tenniscourts.schedules.ScheduleService;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import com.tenniscourts.tenniscourts.TennisCourtMapper;
import com.tenniscourts.tenniscourts.TennisCourtRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {ScheduleService.class, ScheduleMapper.class})
public class ScheduleServiceTest {
    @InjectMocks ScheduleService scheduleService;

    @Mock ScheduleMapper mockScheduleMapper;
    @Mock ScheduleRepository mockScheduleRepository;
    @Mock TennisCourtMapper mockTennisCourtMapper;
    @Mock TennisCourtRepository mockTennisCourtRepository;
    @Mock TennisCourtDTO mockTennisCourtDTO;

    @Test
    public void testAddSchedule() {
        LocalDateTime startTime = LocalDateTime.now();

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setTennisCourt(mockTennisCourtDTO);
        scheduleDTO.setTennisCourtId(1L);
        scheduleDTO.setStartDateTime(startTime);
        scheduleDTO.setEndDateTime(startTime.plusMonths(1));

        when(mockScheduleMapper.map(mockScheduleRepository.saveAndFlush(any(Schedule.class)))).thenReturn(scheduleDTO);

        TennisCourt tennisCourt = TennisCourt.builder().name("Roland Garros").build();
        when(mockTennisCourtRepository.getOne(1L)).thenReturn(tennisCourt);

        CreateScheduleRequestDTO createScheduleRequestDTO = new CreateScheduleRequestDTO();
        createScheduleRequestDTO.setStartDateTime(startTime);
        createScheduleRequestDTO.setTennisCourtId(1L);

        ScheduleDTO actualSchedule = scheduleService.addSchedule(1L, createScheduleRequestDTO);
        assertEquals(startTime, actualSchedule.getStartDateTime());
    }
}
