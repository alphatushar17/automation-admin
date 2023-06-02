package io.confluent.connector.verification.controller;

import io.confluent.connector.verification.exception.ResourceNotFoundException;
import io.confluent.connector.verification.model.ReleaseInfo;
import io.confluent.connector.verification.repository.ReleaseInfoRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/automation/v1")
public class ReleaseInfoController {

	private static final Logger LOGGER = LogManager.getLogger(ReleaseInfoController.class);

	@Autowired
	private ReleaseInfoRepository releaseInfoRepository;

	@GetMapping("/partners")
	public List<ReleaseInfo> getAllPartners() {

		LOGGER.info("API call, GET: /partners");
		return releaseInfoRepository.findAll();
	}

	@GetMapping("/partners/{id}")
	public ResponseEntity<ReleaseInfo> getEmployeeById(@PathVariable(value = "id") String partnerId)
			throws ResourceNotFoundException {
		LOGGER.info("API call, GET: /partners/id");
		ReleaseInfo releaseInfo = Optional.ofNullable(releaseInfoRepository.findByPartnerId(partnerId))
				.orElseThrow(() -> new ResourceNotFoundException("Partner details not found for id :: " + partnerId));
		return ResponseEntity.ok().body(releaseInfo);
	}

	@PostMapping("/partners")
	public ReleaseInfo createReleaseInfo(@Valid @RequestBody ReleaseInfo releaseInfo) {
		LOGGER.info("API call, POST: /partners");
		return releaseInfoRepository.save(releaseInfo);
	}

	@PutMapping("/partners/{id}")
	public ResponseEntity<ReleaseInfo> updateReleaseInfo(@PathVariable(value = "id") String partnerId,
			@Valid @RequestBody ReleaseInfo releaseInfoDetails) throws ResourceNotFoundException {
		LOGGER.info("API call, PUT: /partners/id");
		ReleaseInfo releaseInfo = Optional.ofNullable(releaseInfoRepository.findByPartnerId(partnerId))
				.orElseThrow(() -> new ResourceNotFoundException("Partner details not found for the id :: " + partnerId));

		BeanUtils.copyProperties(releaseInfoDetails, releaseInfo);
		final ReleaseInfo updatedEmployee = releaseInfoRepository.save(releaseInfo);
		return ResponseEntity.ok(updatedEmployee);
	}

	@DeleteMapping("/partners/{id}")
	public Map<String, Boolean> deleteReleaseInfo(@PathVariable(value = "id") String partnerId)
			throws ResourceNotFoundException {
		LOGGER.info("API call, DELETE: /partners/id");
		ReleaseInfo employee = Optional.ofNullable(releaseInfoRepository.findByPartnerId(partnerId))
				.orElseThrow(() -> new ResourceNotFoundException("Partner details not found for the id :: " + partnerId));

		releaseInfoRepository.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

}