package com.consulta.gov.api;

import com.consulta.gov.client.*;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MunicipiosJsonControllerTest {

    private final ParameterizedTypeReference<List<EstadoDto>> ESTADO_RESPONSE_TYPE =
            new ParameterizedTypeReference<List<EstadoDto>>() {
            };

    private final ParameterizedTypeReference<List<MunicipioDto>> MUNICIPIO_RESPONSE_TYPE =
            new ParameterizedTypeReference<List<MunicipioDto>>() {
            };

    @Autowired
    private MockMvc mvc;
    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void deveConsultarMunicipios() throws Exception {
        RegiaoDto regiaoDto = mock(RegiaoDto.class);
        when(regiaoDto.getNome()).thenReturn("Norte");

        EstadoDto estadoDto = mock(EstadoDto.class);
        when(estadoDto.getId()).thenReturn(11);
        when(estadoDto.getSigla()).thenReturn("RO");
        when(estadoDto.getRegiao()).thenReturn(regiaoDto);

        List<EstadoDto> estados = Collections.singletonList(estadoDto);
        ResponseEntity<List<EstadoDto>> response = ResponseEntity.ok(estados);
        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ESTADO_RESPONSE_TYPE)))
                .thenReturn(response);

        MesoregiaoDto mesoregiaoDto = mock(MesoregiaoDto.class);
        when(mesoregiaoDto.getUF()).thenReturn(estadoDto);
        when(mesoregiaoDto.getNome()).thenReturn("Leste Rondoniense");

        MicrorregiaoDto microrregiaoDto = mock(MicrorregiaoDto.class);
        when(microrregiaoDto.getMesorregiao()).thenReturn(mesoregiaoDto);

        MunicipioDto municipioDto = mock(MunicipioDto.class);
        when(municipioDto.getNome()).thenReturn("Alta Floresta D'Oeste");
        when(municipioDto.getMicrorregiao()).thenReturn(microrregiaoDto);

        List<MunicipioDto> municipios = Collections.singletonList(municipioDto);
        ResponseEntity<List<MunicipioDto>> responseMunicipio = ResponseEntity.ok(municipios);
        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(HttpEntity.class), eq(MUNICIPIO_RESPONSE_TYPE)))
                .thenReturn(responseMunicipio);

        mvc.perform(get("/api/json/municipios")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(IOUtils.resourceToString("/municipios.json", Charset.defaultCharset())));
    }

    @Test
    public void deveConsultarMunicipiosByName() throws Exception {
        RegiaoDto regiaoDto = mock(RegiaoDto.class);
        when(regiaoDto.getNome()).thenReturn("Norte");

        EstadoDto estadoDto = mock(EstadoDto.class);
        when(estadoDto.getId()).thenReturn(11);
        when(estadoDto.getSigla()).thenReturn("RO");
        when(estadoDto.getRegiao()).thenReturn(regiaoDto);

        List<EstadoDto> estados = Collections.singletonList(estadoDto);
        ResponseEntity<List<EstadoDto>> response = ResponseEntity.ok(estados);
        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ESTADO_RESPONSE_TYPE)))
                .thenReturn(response);

        MesoregiaoDto mesoregiaoDto = mock(MesoregiaoDto.class);
        when(mesoregiaoDto.getUF()).thenReturn(estadoDto);
        when(mesoregiaoDto.getNome()).thenReturn("Leste Rondoniense");

        MicrorregiaoDto microrregiaoDto = mock(MicrorregiaoDto.class);
        when(microrregiaoDto.getMesorregiao()).thenReturn(mesoregiaoDto);

        MunicipioDto municipioDto = mock(MunicipioDto.class);
        when(municipioDto.getId()).thenReturn(4204609);
        when(municipioDto.getNome()).thenReturn("Alta Floresta D'Oeste");
        when(municipioDto.getMicrorregiao()).thenReturn(microrregiaoDto);

        MunicipioDto municipioDto2 = mock(MunicipioDto.class);
        when(municipioDto2.getId()).thenReturn(4204608);
        when(municipioDto2.getNome()).thenReturn("Criciúma");
        when(municipioDto2.getMicrorregiao()).thenReturn(microrregiaoDto);

        List<MunicipioDto> municipios = Arrays.asList(municipioDto, municipioDto2);
        ResponseEntity<List<MunicipioDto>> responseMunicipio = ResponseEntity.ok(municipios);
        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(HttpEntity.class), eq(MUNICIPIO_RESPONSE_TYPE)))
                .thenReturn(responseMunicipio);

        mvc.perform(get("/api/json/municipios/by-name?nomeCidade=Criciúma")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("4204608"));
    }

}