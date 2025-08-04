package com.cooper_filme.shared_model.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VoteDto {
    private boolean isApprove;
    private UserDto approver;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof VoteDto other)
            return Objects.equals(approver, other.approver) && isApprove == other.isApprove;

        return false;
    }

    @Override
    public String toString() {
        return "Is Approve: " + isApprove;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isApprove, approver);
    }
}
