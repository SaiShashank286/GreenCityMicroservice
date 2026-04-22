package com.cognizant.citizenservice.service;

import com.cognizant.citizenservice.dto.report.CitizenReportCreateRequest;
import com.cognizant.citizenservice.dto.report.CitizenReportResponse;
import com.cognizant.citizenservice.dto.report.CitizenReportUpdateRequest;
import com.cognizant.citizenservice.dto.user.UserDTO;
import com.cognizant.citizenservice.entity.CitizenReport;

import com.cognizant.citizenservice.exception.NotFoundException;
import com.cognizant.citizenservice.exception.UnauthorizedException;
import com.cognizant.citizenservice.feignclient.UserClient;
import com.cognizant.citizenservice.repository.CitizenReportRepository;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitizenReportService {

    private final CitizenReportRepository citizenReportRepository;
    private final UserClient userClient;
    private final ModelMapper modelMapper;

    public List<CitizenReportResponse> listMine(Authentication authentication) {
        UserDTO user = userClient.getuserId(authentication.getName());
        Integer userId = user.getUserId();
        // Change findByCitizen_UserId to findByCitizen
        return citizenReportRepository.findByCitizen(userId).stream()
                .map(this::toResponse).toList();
    }

    public CitizenReportResponse getMine(Integer id, Authentication authentication) {
        CitizenReport report = getEntity(id);
        enforceOwner(report, authentication);

        return toResponse(report);
    }

    public CitizenReportResponse create(CitizenReportCreateRequest request, Authentication authentication) {
        UserDTO user=userClient.getuserId(authentication.getName());
        Integer userId = user.getUserId();

        CitizenReport report = new CitizenReport();
        report.setCitizen(userId);
        report.setType(request.getType());
        report.setLocation(request.getLocation());
        report.setStatus(request.getStatus() != null ? request.getStatus() : "OPEN");
        report.setDate(LocalDateTime.now());

        CitizenReport saved = citizenReportRepository.save(report);

        return toResponse(saved);
    }

    public CitizenReportResponse update(Integer id, CitizenReportUpdateRequest request, Authentication authentication) {
        CitizenReport report = getEntity(id);
        enforceOwner(report, authentication);

        if (request.getType() != null) report.setType(request.getType());
        if (request.getLocation() != null) report.setLocation(request.getLocation());
        if (request.getStatus() != null) report.setStatus(request.getStatus());

        CitizenReport saved = citizenReportRepository.save(report);

        return toResponse(saved);
    }

    public void delete(Integer id, Authentication authentication) {
        CitizenReport report = getEntity(id);
        enforceOwner(report, authentication);
        citizenReportRepository.delete(report);

    }

    private CitizenReport getEntity(Integer id) {
        return citizenReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Report not found"));
    }

    private CitizenReportResponse toResponse(CitizenReport report) {
        CitizenReportResponse response = modelMapper.map(report, CitizenReportResponse.class);
        response.setCitizenId(report.getCitizen() != null ? report.getCitizen() : null);
        return response;
    }



    private void enforceOwner(CitizenReport report, Authentication authentication) {
        UserDTO user=userClient.getuserId(authentication.getName());
        Integer userId = user.getUserId();

        if (report.getCitizen() == null  || !userId.equals(report.getCitizen())) {
            throw new UnauthorizedException("Not allowed");
        }
    }
}

