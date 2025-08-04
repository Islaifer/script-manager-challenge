package com.cooper_filme.script_service.service;

import com.cooper_filme.script_service.exceptions.ScriptNotFoundException;
import com.cooper_filme.script_service.exceptions.ScriptNotValidException;
import com.cooper_filme.shared_model.model.ScriptStatus;
import com.cooper_filme.shared_model.model.dto.ScriptDto;
import com.cooper_filme.shared_model.model.entity.Script;
import com.cooper_filme.shared_model.model.entity.User;
import com.cooper_filme.shared_model.repository.ScriptRepository;
import com.cooper_filme.shared_model.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class ScriptManagerService {
    private final ScriptRepository scriptRepository;
    private final UserRepository userRepository;
    private final MapperFacade mapperFacade;

    @Autowired
    public ScriptManagerService(ScriptRepository scriptRepository,
                                UserRepository userRepository,
                                MapperFacade mapperFacade){
        this.scriptRepository = scriptRepository;
        this.userRepository = userRepository;
        this.mapperFacade = mapperFacade;
    }

    public List<ScriptDto> getScriptByClient(String name, String email, String phone){
        Set<Script> scripts = scriptRepository.findByClientFullNameAndClientEmailAndClientPhone(name, email, phone)
                .orElseThrow(() -> new ScriptNotFoundException("Script's client with name "
                        + name + " and email: "
                        + email + " and phone: "
                        + phone + " not found"));

        return mapperFacade.mapAsList(scripts, ScriptDto.class);
    }

    public ScriptDto getScriptById(Long scriptId){
        Script script = scriptRepository.findById(scriptId).orElseThrow(() -> new ScriptNotFoundException("Script with id " + scriptId + " not found"));

        return mapperFacade.map(script, ScriptDto.class);
    }

    public List<ScriptDto> getScriptsInViewByUser(Long userId){
        return getScriptsInViewByUser(userId, p -> true);
    }

    public List<ScriptDto> getScriptsInViewByUser(Long userId, Predicate<? super ScriptDto> query){
        User user = userRepository.findById(userId).orElseThrow(() -> new ScriptNotFoundException("User doesn't exist"));

        log.info("Start conversion and filter");
        List<ScriptDto> scripts = mapperFacade.mapAsList(user.getScripts(), ScriptDto.class).stream().filter(query).toList();

        if(scripts.isEmpty())
            throw new ScriptNotFoundException("No one scripts are found for user " + user.getName());

        return scripts;
    }

    public List<ScriptDto> getScripts(Predicate<? super Script> query){
        Iterable<Script> scriptsFound = scriptRepository.findAll();

        log.info("Start conversion and filter");
        List<ScriptDto> scripts = StreamSupport.stream(scriptsFound.spliterator(), false)
                .filter(query)
                .map(p -> mapperFacade.map(p, ScriptDto.class))
                .toList();

        if(scripts.isEmpty()){
            log.warn("No one scripts are found");
            throw new ScriptNotFoundException("No one scripts are found");
        }


        return scripts;
    }

    public void postScript(ScriptDto scriptRequest){
        validateScript(scriptRequest);

        Script newScript = mapperFacade.map(scriptRequest, Script.class);
        scriptRepository.save(newScript);
    }

    private void validateScript(ScriptDto script){
        if(script.getClientFullName() == null || script.getClientFullName().isEmpty()){
            log.warn("Client Name cannot be null or empty");
            throw new ScriptNotValidException("Client Name cannot be null or empty");
        }

        if(script.getClientEmail() == null || script.getClientEmail().isEmpty()){
            log.warn("Client Email cannot be null or empty");
            throw new ScriptNotValidException("Client Email cannot be null or empty");
        }

        if(script.getClientPhone() == null || script.getClientPhone().isEmpty()){
            log.warn("Client Phone number cannot be null or empty");
            throw new ScriptNotValidException("Client Phone number cannot be null or empty");
        }

        if(script.getScript() == null || script.getScript().isEmpty()){
            log.warn("Script cannot be null or empty");
            throw new ScriptNotValidException("Script cannot be null or empty");
        }

        script.setId(null);
        script.setActualUser(null);
        script.setAnalystNotes(null);
        script.setApproversVotes(new LinkedList<>());
        script.setProofreaderNotes(null);
        script.setCreateDate(LocalDateTime.now());
        script.setStatus(ScriptStatus.AWAITING_ANALYSIS);
    }

}
