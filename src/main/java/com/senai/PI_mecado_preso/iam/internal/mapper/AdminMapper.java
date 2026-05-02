/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.senai.PI_mecado_preso.iam.internal.mapper;

import com.senai.PI_mecado_preso.iam.api.dtos.AdminRequestDTO;
import com.senai.PI_mecado_preso.iam.api.dtos.AdminResponseDTO;
import com.senai.PI_mecado_preso.iam.internal.entity.Role;
import com.senai.PI_mecado_preso.iam.internal.entity.Admin;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

/**
 *
 * @author Cansei2
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "roles", ignore = true) 
    Admin toEntity(AdminRequestDTO dto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    AdminResponseDTO toResponse(Admin admin);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true) 
    @Mapping(target = "roles", ignore = true)
    void updateEntityFromDto(AdminRequestDTO dto, @MappingTarget Admin admin);

    // Método auxiliar para converter Set<Role> em Set<String>
    @Named("mapRoles")
    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Role::getNome)
                .collect(Collectors.toSet());
    }
    
}
