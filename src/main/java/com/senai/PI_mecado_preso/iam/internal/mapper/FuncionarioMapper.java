/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.internal.mapper;

import com.senai.PI_mecado_preso.iam.api.dtos.FuncionarioRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.FuncionarioResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.entity.Funcionario;
import com.senai.PI_mecado_preso.iam.internal.entity.Role;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Cansei2
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FuncionarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "roles", ignore = true)
    Funcionario toEntity(FuncionarioRequestDTO dto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    FuncionarioResponseDTO toResponse(Funcionario admin);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true) 
    @Mapping(target = "roles", ignore = true)
    void updateEntityFromDto(FuncionarioRequestDTO dto, @MappingTarget Funcionario admin);

    // Método auxiliar para converter Set<Role> em Set<String>
    @Named("mapRoles")
    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Role::name)
                .collect(Collectors.toSet());
    }
    
}
