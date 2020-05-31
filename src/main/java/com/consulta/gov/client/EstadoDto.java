package com.consulta.gov.client;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EstadoDto {

    private Integer id;

    private String sigla;

    private String nome;

    private RegiaoDto regiao;

}