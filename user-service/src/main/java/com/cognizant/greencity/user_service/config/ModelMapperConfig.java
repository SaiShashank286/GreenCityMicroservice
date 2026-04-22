package com.cognizant.greencity.user_service.config;


import com.cognizant.greencity.user_service.dto.audit.AuditLogResponse;
import com.cognizant.greencity.user_service.entity.AuditLog;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        modelMapper.typeMap(AuditLog.class, AuditLogResponse.class)
                .addMappings(mapper -> mapper.skip(AuditLogResponse::setUserId));

        return modelMapper;
    }
}
