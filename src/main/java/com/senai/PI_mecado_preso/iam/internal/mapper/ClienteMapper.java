package com.senai.PI_mecado_preso.iam.internal.mapper;

import com.senai.PI_mecado_preso.iam.api.dtos.ClienteRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.ClienteResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.entity.Role;
import com.senai.PI_mecado_preso.iam.internal.entity.Cliente;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "roles", ignore = true)
    Cliente toEntity(ClienteRequestDTO dto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    ClienteResponseDTO toResponse(Cliente cliente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateEntityFromDto(ClienteRequestDTO dto, @MappingTarget Cliente cliente);

    @Named("mapRoles")
    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) return Set.of();
        return roles.stream()
                .map(Role::name)
                .collect(Collectors.toSet());
    }
}
