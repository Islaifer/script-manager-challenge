package com.cooper_filme.script_service.controller;

import com.cooper_filme.script_service.service.ScriptManagerService;
import com.cooper_filme.shared_model.model.dto.ScriptDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/client/")
public class ClientController {
    private final ScriptManagerService scriptService;

    @Autowired
    public ClientController(ScriptManagerService scriptService){
        this.scriptService = scriptService;
    }

    @GetMapping("v1/{clientFullName}/{clientEmail}/{clientPhone}")
    public ResponseEntity<List<ScriptDto>> getScriptsByClient(@PathVariable("clientFullName") String clientFullName,
                                                              @PathVariable("clientEmail") String clientEmail,
                                                              @PathVariable("clientPhone") String clientPhone){
        log.info("Start get script");
        List<ScriptDto> scripts = scriptService.getScriptByClient(clientFullName, clientEmail, clientPhone);

        log.debug("Scripts was found");
        return ResponseEntity.ok(scripts);
    }

    @PostMapping("v1")
    public ResponseEntity<String> postScript(@RequestBody ScriptDto scriptDto){
        log.info("Start post new script");
        this.scriptService.postScript(scriptDto);

        log.debug("New script was posted");
        return ResponseEntity.status(HttpStatus.CREATED).body("Created");
    }
}
