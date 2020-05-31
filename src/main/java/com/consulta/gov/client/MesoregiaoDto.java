package com.consulta.gov.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MesoregiaoDto {

    private Integer id;

    private String nome;

    @JsonProperty("UF")
    private EstadoDto UF;
}
