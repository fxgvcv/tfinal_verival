package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.domain.Immigrant;
import com.ages.informativoparaimigrantes.repository.IImmigrantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImmigrantServiceImplTest {

    IImmigrantRepository IImmigrantRepository = mock(IImmigrantRepository.class);
    ImmigrantServiceImpl immigrantServiceImpl = new ImmigrantServiceImpl(IImmigrantRepository);

    public final Immigrant immigrant = new Immigrant("email", "name", "countryOfOrigin");

    @Test
    public void testGetByEmail(){
        Optional<Immigrant> optionalImmigrant = Optional.of(immigrant);

        when(IImmigrantRepository.findById("email")).thenReturn(optionalImmigrant);

        assertEquals(immigrantServiceImpl.getByEmail("email"), optionalImmigrant);
    }

    @Test
    public void testGetALl(){
        List<Immigrant> immigrantList = List.of(immigrant);

        when(IImmigrantRepository.getAll()).thenReturn(immigrantList);

        assertEquals(immigrantServiceImpl.getAll(), immigrantList);
    }

}
