package com.cooper_filme.shared_model.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isApprove;

    @ManyToOne
    private Script script;

    @ManyToOne
    private User approver;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vote other)
            return Objects.equals(id, other.id);

        return false;
    }

    @Override
    public String toString() {
        return "Id: " + id + "Is Approve: " + isApprove;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isApprove, script);
    }
}
