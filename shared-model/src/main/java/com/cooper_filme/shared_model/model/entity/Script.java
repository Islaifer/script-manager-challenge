package com.cooper_filme.shared_model.model.entity;

import com.cooper_filme.shared_model.model.ScriptStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Script {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ScriptStatus status;

    private String clientFullName;
    private String clientEmail;
    private String clientPhone;
    private String script;

    private String analystNotes;
    private String proofreaderNotes;

    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn
    private User actualUser;

    @OneToMany(mappedBy = "script")
    private Set<Vote> approversVotes;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Script other)
            return Objects.equals(id, other.id);

        return false;
    }

    @Override
    public String toString() {
        return "Id: " + id + "Script: " + script;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, script, clientFullName, clientEmail, createDate);
    }
}
