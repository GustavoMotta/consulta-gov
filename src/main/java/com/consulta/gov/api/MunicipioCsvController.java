package com.consulta.gov.api;

import com.consulta.gov.client.MunicipioDto;
import com.consulta.gov.municipio.ConsultaMunicipioService;
import com.consulta.gov.municipio.CsvBuildException;
import com.consulta.gov.municipio.ResultadoDto;
import com.consulta.gov.municipio.TipoResultadoParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/csv/municipios")
public class MunicipioCsvController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MunicipioCsvController.class);

    private static final String[] HEADER_FILE = {"idEstado", "siglaEstado", "regiaoNome", "nomeCidade", "nomeMesorregiao", "nomeFormatado"};

    private final ConsultaMunicipioService consultaMunicipioService;

    public MunicipioCsvController(ConsultaMunicipioService consultaMunicipioService) {
        this.consultaMunicipioService = consultaMunicipioService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity getMunicipioFile(HttpServletResponse response) {
        try (OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8)) {
            response.setHeader("Content-Disposition", "attachment; filename=" + "municipios-" + System.currentTimeMillis() + ".csv");
            response.setContentType("text/csv");

            CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withRecordSeparator("\n");
            CSVPrinter printer = new CSVPrinter(osw, format);
            printer.printRecord(HEADER_FILE);

            List<MunicipioDto> municipios = consultaMunicipioService.consultaCidades();

            List<ResultadoDto> resultados = municipios.stream()
                    .map(TipoResultadoParser.MUNICIPIO::parse)
                    .collect(Collectors.toList());

            for (ResultadoDto resultado : resultados) {
                printer.printRecord(resultado.getIdEstado(), resultado.getSiglaEstado(), resultado.getRegiaoNome(),
                        resultado.getNomeCidade(), resultado.getNomeMesorregiao(), resultado.getNomeFormatado());
            }

            printer.flush();
            printer.close();
        } catch (IOException e) {
            LOGGER.error("Falha ao criar arquivo");
            throw new CsvBuildException("Falha ao criar arquivo");
        }
        return ResponseEntity.ok().build();
    }
}