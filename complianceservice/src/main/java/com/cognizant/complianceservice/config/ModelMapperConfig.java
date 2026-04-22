package com.cognizant.complianceservice.config;


import com.cognizant.complianceservice.dto.audit.AuditResponse;
import com.cognizant.complianceservice.entity.Audit;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

         modelMapper.typeMap(Audit.class, AuditResponse.class)
                .addMappings(mapper -> {
                    mapper.skip(AuditResponse::setComplianceId);
                    mapper.skip(AuditResponse::setOfficerId);
                });

        return modelMapper;
    }
}

