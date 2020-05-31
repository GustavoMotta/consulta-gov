package com.consulta.gov.municipio;

import com.consulta.gov.client.ConsultaGovService;
import com.consulta.gov.client.EstadoDto;
import com.consulta.gov.client.MunicipioDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConsultaMunicipioService {

    private final ConsultaGovService consultaGovService;

    public ConsultaMunicipioService(ConsultaGovService consultaGovService) {
        this.consultaGovService = consultaGovService;
    }

    public List<MunicipioDto> consultaCidades(String uf) {
        return consultaGovService.consultaMunicipio(uf);
    }

    @Cacheable("consultaCidades")
    public List<MunicipioDto> consultaCidades() {
        List<EstadoDto> estadoDtos = consultaGovService.consultaEstados();

        String ufs = estadoDtos.stream()
                .map(EstadoDto::getId)
                .map(String::valueOf)
                .collect(Collectors.joining("|"));

        return consultaGovService.consultaMunicipio(ufs);
    }

    @Cacheable(cacheNames = "consultaCidadesByName", key = "#nomeCidade")
    public MunicipioDto consultaCidade(String nomeCidade) {
        List<MunicipioDto> municipios = consultaCidades();
        return municipios.stream()
                .filter(m -> m.getNome().equals(nomeCidade))
                .findFirst()
                .orElseThrow(() -> new MunicipioNotFoundException("Não foi encontrado um município com o nome informado"));
    }

}
