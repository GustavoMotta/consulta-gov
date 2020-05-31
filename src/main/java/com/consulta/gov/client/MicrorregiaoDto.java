package com.consulta.gov.client;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MicrorregiaoDto {

    private Integer id;

    private String nome;

    private MesoregiaoDto mesorregiao;

}
