package com.consulta.gov.municipio;

import com.consulta.gov.client.EstadoDto;
import com.consulta.gov.client.GovResponseDto;
import com.consulta.gov.client.MesoregiaoDto;
import com.consulta.gov.client.MunicipioDto;

import java.util.Objects;

public enum TipoResultadoParser {

    MUNICIPIO() {
        @Override
        public ResultadoDto parse(GovResponseDto dto) {
            Objects.requireNonNull(dto, "Não é possível converter dado");
            if (!(dto instanceof MunicipioDto)) {
                throw new IllegalArgumentException("Não é possível converter dado");
            }
            MunicipioDto municipioDto = (MunicipioDto) dto;
            MesoregiaoDto mesoregiao = municipioDto.getMicrorregiao().getMesorregiao();
            EstadoDto estado = mesoregiao.getUF();
            return ResultadoDto.builder()
                    .idEstado(estado.getId())
                    .siglaEstado(estado.getSigla())
                    .regiaoNome(estado.getRegiao().getNome())
                    .nomeCidade(municipioDto.getNome())
                    .nomeMesorregiao(mesoregiao.getNome())
                    .nomeFormatado(municipioDto.getNome() + "/" + estado.getId())
                    .build();
        }
    };

    public abstract ResultadoDto parse(GovResponseDto dto);
}