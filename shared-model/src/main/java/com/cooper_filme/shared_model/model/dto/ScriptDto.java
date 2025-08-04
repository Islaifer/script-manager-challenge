package com.cooper_filme.shared_model.model.dto;

import com.cooper_filme.shared_model.model.ScriptStatus;
import com.cooper_filme.shared_model.util.StatusSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScriptDto {
    private Long id;

    private String clientFullName;
    private String clientEmail;
    private String clientPhone;
    private String script;

    @JsonSerialize(using = StatusSerializer.class)
    private ScriptStatus status;

    private String analystNotes;
    private String proofreaderNotes;

    private LocalDateTime createDate;

    private UserDto actualUser;

    private List<VoteDto> approversVotes;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ScriptDto other)
            return Objects.equals(id, other.id);;

        return false;
    }

    @Override
    public String toString() {
        return "Script: " + script;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, script, clientFullName, clientEmail, createDate);
    }
}
