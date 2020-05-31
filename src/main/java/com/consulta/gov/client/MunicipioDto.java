package com.consulta.gov.client;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MunicipioDto implements GovResponseDto {

    private Integer id;

    private String nome;

    private MicrorregiaoDto microrregiao;
}
