package com.senai.PI_mecado_preso.iam.internal.mapper;

import com.senai.PI_mecado_preso.iam.api.dtos.FuncionarioRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.FuncionarioResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.entity.Funcionario;
import com.senai.PI_mecado_preso.iam.internal.entity.Role;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FuncionarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "roles", ignore = true)
    Funcionario toEntity(FuncionarioRequestDTO dto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    FuncionarioResponseDTO toResponse(Funcionario funcionario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateEntityFromDto(FuncionarioRequestDTO dto, @MappingTarget Funcionario funcionario);

    @Named("mapRoles")
    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) return Set.of();
        return roles.stream()
                .map(Role::name)
                .collect(Collectors.toSet());
    }
}
