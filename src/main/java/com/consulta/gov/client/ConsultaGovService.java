package com.consulta.gov.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Component
public class ConsultaGovService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsultaGovService.class);

    private final RestTemplate restTemplate;

    private final ParameterizedTypeReference<List<EstadoDto>> ESTADO_RESPONSE_TYPE =
            new ParameterizedTypeReference<List<EstadoDto>>() {
            };

    private final ParameterizedTypeReference<List<MunicipioDto>> MUNICIPIO_RESPONSE_TYPE =
            new ParameterizedTypeReference<List<MunicipioDto>>() {
            };

    public ConsultaGovService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "fallbackConsultaEstados", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000")})
    @Cacheable("estados")
    public List<EstadoDto> consultaEstados() {
        UriComponents url = UriComponentsBuilder.fromHttpUrl("https://servicodados.ibge.gov.br/")
                .pathSegment("api", "v1", "localidades", "estados")
                .build();
        return restTemplate.exchange(url.toUri(), HttpMethod.GET, HttpEntity.EMPTY, ESTADO_RESPONSE_TYPE)
                .getBody();
    }

    @HystrixCommand(fallbackMethod = "fallbackConsultaMunicipio", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000")})
    @Cacheable(cacheNames = "municipios", key = "#uf")
    public List<MunicipioDto> consultaMunicipio(String uf) {
        UriComponents url = UriComponentsBuilder.fromHttpUrl("https://servicodados.ibge.gov.br/")
                .pathSegment("api", "v1", "localidades", "estados", "{uf}", "municipios")
                .buildAndExpand(uf);

        return restTemplate.exchange(url.toUri(), HttpMethod.GET, HttpEntity.EMPTY, MUNICIPIO_RESPONSE_TYPE)
                .getBody();
    }

    private List<EstadoDto> fallbackConsultaEstados() {
        LOGGER.error("Falha ao localizar estados");
        return Collections.emptyList();
    }

    private List<MunicipioDto> fallbackConsultaMunicipio(String uf) {
        LOGGER.error("Falha ao localizar municipios");
        return Collections.emptyList();
    }
}
