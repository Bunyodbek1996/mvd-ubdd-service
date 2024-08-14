package uz.ciasev.ubdd_service.controller_ubdd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ciasev.ubdd_service.migration.CsvProcessorService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${mvd-ciasev.url-v0}/migration", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UbddMigrateController {


    private final CsvProcessorService csvProcessorService;


    @PostMapping("/{filePath}")
    public ResponseEntity<?> migrate(
            @PathVariable String filePath
    ) throws IOException {

        csvProcessorService.startProcess(filePath);

        return ResponseEntity.ok().build();
    }

}
