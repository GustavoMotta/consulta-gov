package com.consulta.gov.municipio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoDto {

    private Integer idEstado;

    private String siglaEstado;

    private String regiaoNome;

    private String nomeCidade;

    private String nomeMesorregiao;

    private String nomeFormatado;
}
