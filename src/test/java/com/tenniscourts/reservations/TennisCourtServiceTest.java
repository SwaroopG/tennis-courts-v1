package com.tenniscourts.reservations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import com.tenniscourts.tenniscourts.TennisCourtMapper;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import com.tenniscourts.tenniscourts.TennisCourtService;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration
public class TennisCourtServiceTest {
    @Mock TennisCourtMapper mockTennisCourtMapper;
    @Mock TennisCourtRepository mockTennisCourtRepository;
    @InjectMocks TennisCourtService tennisCourtService;

    @Test
    public void testAddTennisCourt() {
        TennisCourtDTO expectedTennisCourtDTO = createTennisCourtDTO();
        when(mockTennisCourtMapper.map(mockTennisCourtRepository.saveAndFlush(any(TennisCourt.class)))).thenReturn(expectedTennisCourtDTO);

        TennisCourtDTO actualTennisCourtDTO = tennisCourtService.addTennisCourt(expectedTennisCourtDTO);
        assertEquals(expectedTennisCourtDTO.getName(), actualTennisCourtDTO.getName());
    }

    @Test
    public void testFindById() {
        TennisCourt mockTennisCourt = mock(TennisCourt.class);
        TennisCourtDTO expectedTennisCourtDTO = createTennisCourtDTO();
        expectedTennisCourtDTO.setTennisCourtSchedules(null);
        when(mockTennisCourtRepository.findById(1L)).thenReturn(Optional.of(mockTennisCourt));
        when(mockTennisCourtMapper.map(any(TennisCourt.class))).thenReturn(expectedTennisCourtDTO);

        TennisCourtDTO actualTennisCourtDTO = tennisCourtService.findTennisCourtById(1L);
        assertEquals(expectedTennisCourtDTO.getName(), actualTennisCourtDTO.getName());
    }


    @Test
    public void testFindTennisCourtWithSchedulesById() {
        ScheduleDTO mockSchedule = mock(ScheduleDTO.class);
        mockSchedule.setId(12345L);

        TennisCourt tennisCourt = mock(TennisCourt.class);
        TennisCourtDTO expectedTennisCourtDTO = createTennisCourtDTO();
        expectedTennisCourtDTO.setTennisCourtSchedules(Lists.newArrayList(mockSchedule));
        when(mockTennisCourtRepository.findById(1L)).thenReturn(Optional.of(tennisCourt));
        when(mockTennisCourtMapper.map(any(TennisCourt.class))).thenReturn(expectedTennisCourtDTO);

        TennisCourtDTO actualTennisCourtDTO = tennisCourtService.findTennisCourtById(1L);
        assertEquals(expectedTennisCourtDTO.getName(), actualTennisCourtDTO.getName());
        assertEquals(1, actualTennisCourtDTO.getTennisCourtSchedules().size());
        assertEquals(mockSchedule.getId(), actualTennisCourtDTO.getTennisCourtSchedules().get(0).getId());
    }

    private TennisCourtDTO createTennisCourtDTO() {
        TennisCourtDTO tennisCourtDTO = new TennisCourtDTO();
        tennisCourtDTO.setName("Roland Garros");
        tennisCourtDTO.setId(1L);

        return tennisCourtDTO;
    }
}
