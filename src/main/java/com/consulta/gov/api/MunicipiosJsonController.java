package com.consulta.gov.api;

import com.consulta.gov.municipio.ConsultaMunicipioService;
import com.consulta.gov.client.MunicipioDto;
import com.consulta.gov.municipio.TipoResultadoParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/json/municipios")
public class MunicipiosJsonController {

    private final ConsultaMunicipioService consultaMunicipioService;

    public MunicipiosJsonController(ConsultaMunicipioService consultaMunicipioService) {
        this.consultaMunicipioService = consultaMunicipioService;
    }

    @GetMapping
    public ResponseEntity<Object> getMunicipios() {
        List<MunicipioDto> municipios = consultaMunicipioService.consultaCidades();
        return ResponseEntity.ok(municipios.stream()
                .map(TipoResultadoParser.MUNICIPIO::parse)
                .collect(Collectors.toList()));
    }

    @GetMapping("by-name")
    public ResponseEntity<Integer> getMunicipioByName(@RequestParam(name = "nomeCidade") String nomeCidade) {
        MunicipioDto municipio = consultaMunicipioService.consultaCidade(nomeCidade);
        return ResponseEntity.ok(municipio.getId());
    }
}