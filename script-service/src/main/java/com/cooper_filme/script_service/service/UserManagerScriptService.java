package com.cooper_filme.script_service.service;

import com.cooper_filme.script_service.exceptions.IllegalUserOperationException;
import com.cooper_filme.script_service.exceptions.ScriptNotFoundException;
import com.cooper_filme.shared_model.model.ScriptStatus;
import com.cooper_filme.shared_model.model.entity.Role;
import com.cooper_filme.shared_model.model.entity.Script;
import com.cooper_filme.shared_model.model.entity.User;
import com.cooper_filme.shared_model.model.entity.Vote;
import com.cooper_filme.shared_model.repository.ScriptRepository;
import com.cooper_filme.shared_model.repository.UserRepository;
import com.cooper_filme.shared_model.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManagerScriptService {
    private final ScriptRepository scriptRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public UserManagerScriptService(ScriptRepository scriptRepository,
                                    UserRepository userRepository,
                                    VoteRepository voteRepository){
        this.scriptRepository = scriptRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public void assignScriptToUser(Long userId, Long scriptId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ScriptNotFoundException("User doesn't exist"));
        Script script = scriptRepository.findById(scriptId).orElseThrow(() -> new ScriptNotFoundException("Script doesn't exist"));

        List<String> roles = user.getRoles().stream()
                .map(Role::getName).toList();

        ScriptStatus status = script.getStatus();

        switch (status){
            case ScriptStatus.AWAITING_ANALYSIS:
                if(roles.contains("ANALYST"))
                    script.setStatus(ScriptStatus.UNDER_ANALYSIS);
                else
                    throw new IllegalUserOperationException("Script is waiting for analysis");
                break;

            case ScriptStatus.AWAITING_REVIEW:
                if(roles.contains("PROOFREADER"))
                    script.setStatus(ScriptStatus.UNDER_REVIEW);
                else
                    throw new IllegalUserOperationException("Script is waiting for review");
                break;

            default:
                throw new IllegalUserOperationException("Script is not waiting for analysis or review");
        }

        script.setActualUser(user);
        scriptRepository.save(script);
    }

    public void analyseScript(Long userId, Long scriptId, String userNote, boolean approve){
        Script script = scriptRepository.findById(scriptId).orElseThrow(() -> new ScriptNotFoundException("Script doesn't exist"));

        if(!script.getStatus().equals(ScriptStatus.UNDER_ANALYSIS))
            throw new IllegalUserOperationException("Script not in analysis phase");

        if(!userId.equals(script.getActualUser().getId()))
            throw new IllegalUserOperationException("User is not assign in this script");

        doTask(script, userNote, approve);
    }

    public void reviewScript(Long userId, Long scriptId, String userNote, boolean approve){
        Script script = scriptRepository.findById(scriptId).orElseThrow(() -> new ScriptNotFoundException("Script doesn't exist"));

        if(!script.getStatus().equals(ScriptStatus.UNDER_REVIEW))
            throw new IllegalUserOperationException("Script not in review phase");

        if(!userId.equals(script.getActualUser().getId()))
            throw new IllegalUserOperationException("User is not assign in this script");

        doTask(script, userNote, approve);
    }

    public void vote(Long userId, Long scriptId, boolean approve){
        User user = userRepository.findById(userId).orElseThrow(() -> new ScriptNotFoundException("User doesn't exist"));
        Script script = scriptRepository.findById(scriptId).orElseThrow(() -> new ScriptNotFoundException("Script doesn't exist"));

        ScriptStatus status = script.getStatus();

        switch (status){
            case ScriptStatus.WAITING_FOR_APPROVAL:
                script.setStatus(ScriptStatus.IN_APPROVAL);
                break;
            case ScriptStatus.IN_APPROVAL:
                break;

            default: throw new IllegalUserOperationException("Script is not in approval phase");
        }

        boolean userAlreadyVote = script.getApproversVotes().stream().anyMatch(p -> p.getApprover().getId().equals(user.getId()));
        if(userAlreadyVote)
            throw new IllegalUserOperationException("User already vote in this script");

        Vote vote = new Vote();
        vote.setApprover(user);
        vote.setApprove(approve);
        vote.setScript(script);

        if(!approve)
            script.setStatus(ScriptStatus.REJECTED);
        else if(script.getApproversVotes().size() == 2)
            script.setStatus(ScriptStatus.APPROVED);

        script.getApproversVotes().add(vote);

        voteRepository.save(vote);
        scriptRepository.save(script);
    }

    private void doTask(Script script, String userNote, boolean approve){
        ScriptStatus status = script.getStatus();

        if(approve){
            switch (status){
                case ScriptStatus.UNDER_ANALYSIS:
                    script.setStatus(ScriptStatus.AWAITING_REVIEW);
                    script.setAnalystNotes(userNote);
                    break;

                case ScriptStatus.UNDER_REVIEW:
                    script.setStatus(ScriptStatus.WAITING_FOR_APPROVAL);
                    script.setProofreaderNotes(userNote);
                    break;

                default:
            }
        }else{
            switch (status){
                case ScriptStatus.UNDER_ANALYSIS:
                    script.setAnalystNotes(userNote);
                    break;

                case ScriptStatus.UNDER_REVIEW:
                    script.setProofreaderNotes(userNote);
                    break;

                default:
            }
            script.setStatus(ScriptStatus.REJECTED);
        }

        script.setActualUser(null);
        scriptRepository.save(script);
    }
}
