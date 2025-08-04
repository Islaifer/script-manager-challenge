package com.cooper_filme.script_service.configuration;

import com.cooper_filme.shared_model.model.dto.RoleDto;
import com.cooper_filme.shared_model.model.dto.ScriptDto;
import com.cooper_filme.shared_model.model.dto.UserDto;
import com.cooper_filme.shared_model.model.dto.VoteDto;
import com.cooper_filme.shared_model.model.entity.Role;
import com.cooper_filme.shared_model.model.entity.Script;
import com.cooper_filme.shared_model.model.entity.User;
import com.cooper_filme.shared_model.model.entity.Vote;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrikaConfig {

    @Bean
    public MapperFacade mapperFacade(){
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        registerMappings(factory);

        return factory.getMapperFacade();
    }

    private void registerMappings(MapperFactory factory){
        factory.classMap(Role.class, RoleDto.class)
                .byDefault()
                .register();

        factory.classMap(Script.class, ScriptDto.class)
                .field("actualUser", "actualUser")
                .field("approversVotes", "approversVotes")
                .byDefault()
                .register();

        factory.classMap(User.class, UserDto.class)
                .field("roles", "roles")
                .byDefault()
                .register();

        factory.classMap(Vote.class, VoteDto.class)
                .field("approver", "approver")
                .byDefault()
                .register();
    }
}

