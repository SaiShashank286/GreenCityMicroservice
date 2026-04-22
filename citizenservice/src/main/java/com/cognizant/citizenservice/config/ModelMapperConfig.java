package com.cognizant.citizenservice.config;


import com.cognizant.citizenservice.dto.feedback.FeedbackResponse;
import com.cognizant.citizenservice.dto.report.CitizenReportResponse;
import com.cognizant.citizenservice.entity.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        // These ID fields are populated manually in services from nested entities.

        modelMapper.typeMap(CitizenReport.class, CitizenReportResponse.class)
                .addMappings(mapper -> mapper.skip(CitizenReportResponse::setCitizenId));
        modelMapper.typeMap(Feedback.class, FeedbackResponse.class)
                .addMappings(mapper -> mapper.skip(FeedbackResponse::setCitizenId));

        return modelMapper;
    }
}

