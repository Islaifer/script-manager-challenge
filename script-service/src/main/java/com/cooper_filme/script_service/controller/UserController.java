package com.cooper_filme.script_service.controller;

import com.cooper_filme.script_service.model.UserNote;
import com.cooper_filme.script_service.security.model.JwtSimpleUser;
import com.cooper_filme.script_service.service.ScriptManagerService;
import com.cooper_filme.script_service.service.UserManagerScriptService;
import com.cooper_filme.shared_model.model.ScriptStatus;
import com.cooper_filme.shared_model.model.dto.ScriptDto;
import com.cooper_filme.shared_model.model.entity.Script;
import com.cooper_filme.shared_model.util.StatusTranslator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
@RestController
@RequestMapping("/user/")
public class UserController {
    private final UserManagerScriptService userManagerService;
    private final ScriptManagerService scriptManagerService;

    @Autowired
    public UserController(UserManagerScriptService userManagerService,
                          ScriptManagerService scriptManagerService){
        this.userManagerService = userManagerService;
        this.scriptManagerService = scriptManagerService;
    }

    @GetMapping("v1/script")
    @PreAuthorize("hasAnyRole('ANALYST', 'PROOFREADER', 'APPROVER')")
    public ResponseEntity<List<ScriptDto>> getAllScripts(@RequestParam(required = false) String status,
                                                        @RequestParam(required = false) LocalDateTime beforeDate,
                                                        @RequestParam(required = false) LocalDateTime afterDate){
        log.info("Start to get all Scripts");
        Predicate<Script> query = null;

        Predicate<Script> statusQuery = p -> true;
        Predicate<Script> beforeDateQuery= p -> true;
        Predicate<Script> afterDateQuery= p -> true;

        if(status != null){
            log.info("Has status filter");
            ScriptStatus scriptStatus = StatusTranslator.translateStringToStatus(status);
            statusQuery = p -> p.getStatus().equals(scriptStatus);
        }

        if(beforeDate != null) {
            log.info("Has beforeDate filter");
            beforeDateQuery = p -> p.getCreateDate().isBefore(beforeDate);
        }

        if(afterDate != null){
            log.info("Has afterDate filter");
            afterDateQuery = p -> p.getCreateDate().isAfter(afterDate);
        }

        query = statusQuery.and(beforeDateQuery).and(afterDateQuery);

        List<ScriptDto> scripts = scriptManagerService.getScripts(query);
        log.debug("Scripts was get");

        return ResponseEntity.ok(scripts);
    }

    @GetMapping("v1/script/{scriptId}")
    @PreAuthorize("hasAnyRole('ANALYST', 'PROOFREADER', 'APPROVER')")
    public ResponseEntity<ScriptDto> getScriptById(@PathVariable("scriptId") Long scriptId){
        ScriptDto script = scriptManagerService.getScriptById(scriptId);

        return ResponseEntity.ok(script);
    }

    @GetMapping("v1/script/assigned")
    @PreAuthorize("hasAnyRole('ANALYST', 'PROOFREADER')")
    public ResponseEntity<List<ScriptDto>> getAssignedScripts(@AuthenticationPrincipal JwtSimpleUser user,
                                                              @RequestParam(required = false) String status,
                                                              @RequestParam(required = false) LocalDateTime beforeDate,
                                                              @RequestParam(required = false) LocalDateTime afterDate){
        log.info("Start to get all Scripts assigned");

        Long userId = user.userId();
        List<ScriptDto> scripts;
        if(status == null && beforeDate == null && afterDate == null){
            scripts = scriptManagerService.getScriptsInViewByUser(userId);
        }else{
            Predicate<ScriptDto> query = null;

            Predicate<ScriptDto> statusQuery = p -> true;
            Predicate<ScriptDto> beforeDateQuery= p -> true;
            Predicate<ScriptDto> afterDateQuery= p -> true;

            if(status != null){
                log.info("Has status filter");
                ScriptStatus scriptStatus = StatusTranslator.translateStringToStatus(status);
                statusQuery = p -> p.getStatus().equals(scriptStatus);
            }

            if(beforeDate != null){
                log.info("Has beforeDate filter");
                beforeDateQuery = p -> p.getCreateDate().isBefore(beforeDate);
            }

            if(afterDate != null){
                log.info("Has afterDate filter");
                afterDateQuery = p -> p.getCreateDate().isAfter(afterDate);
            }

            query = statusQuery.and(beforeDateQuery).and(afterDateQuery);
            scripts = scriptManagerService.getScriptsInViewByUser(userId, query);
        }

        return ResponseEntity.ok(scripts);
    }

    @PatchMapping("v1/script/{scriptId}/assign")
    @PreAuthorize("hasAnyRole('ANALYST', 'PROOFREADER')")
    public ResponseEntity<String> assignScript(@AuthenticationPrincipal JwtSimpleUser user,
                                              @PathVariable("scriptId") Long scriptId){
        log.info("Start assign script");
        userManagerService.assignScriptToUser(user.userId(), scriptId);

        log.debug("Script was assigned");
        return ResponseEntity.ok("Script with was assigned");
    }

    @PatchMapping("v1/script/{scriptId}/analyse/{approve}")
    @PreAuthorize("hasRole('ANALYST')")
    public ResponseEntity<String> analyseScript(@AuthenticationPrincipal JwtSimpleUser user,
                                                @PathVariable("scriptId") Long scriptId,
                                                @PathVariable("approve") Boolean approve,
                                                @RequestBody UserNote userNote){
        log.info("Start analyse script");
        userManagerService.analyseScript(user.userId(), scriptId, userNote.note(), approve);

        log.debug("Script was analysed");
        return ResponseEntity.ok("Script was analysed");
    }

    @PatchMapping("v1/script/{scriptId}/review/{approve}")
    @PreAuthorize("hasRole('PROOFREADER')")
    public ResponseEntity<String> reviewScript(@AuthenticationPrincipal JwtSimpleUser user,
                                               @PathVariable("scriptId") Long scriptId,
                                               @PathVariable("approve") Boolean approve,
                                               @RequestBody UserNote userNote){
        log.info("Start review script");
        userManagerService.reviewScript(user.userId(), scriptId, userNote.note(), approve);

        log.debug("Script was reviewed");
        return ResponseEntity.ok("Script was reviewed");
    }

    @PatchMapping("v1/script/{scriptId}/vote/{approve}")
    @PreAuthorize("hasRole('APPROVER')")
    public ResponseEntity<String> voteScript(@AuthenticationPrincipal JwtSimpleUser user,
                                             @PathVariable("scriptId") Long scriptId,
                                             @PathVariable("approve") Boolean approve){
        log.info("Start vote in script");
        userManagerService.vote(user.userId(), scriptId, approve);

        log.debug("Script was voted");
        return ResponseEntity.ok("Script was voted");
    }
}
