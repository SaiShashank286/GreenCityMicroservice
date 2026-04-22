package com.cognizant.greencity.ProjectService.entity;

import com.cognizant.greencity.ProjectService.entity.enums.ImpactStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "impacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Impact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "impact_id")
	private Long impactId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "metrics_json", nullable = false)
	@Builder.Default
	private Map<String, Object> metricsJson = new HashMap<>();

	@Column(name = "recorded_date", nullable = false)
	private LocalDate recordedDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 32)
	private ImpactStatus status;
}
