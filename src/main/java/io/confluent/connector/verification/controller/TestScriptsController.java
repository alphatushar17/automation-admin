package io.confluent.connector.verification.controller;

import io.confluent.connector.verification.exception.ResourceNotFoundException;
import io.confluent.connector.verification.model.TestScripts;
import io.confluent.connector.verification.repository.TestScriptsRepository;
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
public class TestScriptsController {

	private static final Logger LOGGER = LogManager.getLogger(TestScriptsController.class);

	@Autowired
	private TestScriptsRepository testScriptsRepository;

	@GetMapping("/testScripts/{partnerId}")
	public ResponseEntity<List<TestScripts>> getTestcaseByPartnerId(@PathVariable(value = "partnerId") String partnerId)
			throws ResourceNotFoundException {

		LOGGER.info("API call, GET: /testScripts/partnerId");
		List<TestScripts> testScripts = Optional.ofNullable(testScriptsRepository.findByPartnerId(partnerId))
				.orElseThrow(() -> new ResourceNotFoundException("Test script details not found for the id :: " + partnerId));
		return ResponseEntity.ok().body(testScripts);
	}

	@GetMapping("/testScripts/{partnerId}/{testId}")
	public ResponseEntity<TestScripts> getTestcaseByTestId(@PathVariable(value = "partnerId") String partnerId,
															 @PathVariable(value = "testId") String testId)
			throws ResourceNotFoundException {
		LOGGER.info("API call, GET: /testScripts/partnerId/testId");
		TestScripts testScripts = Optional.ofNullable(testScriptsRepository.findByPartnerIdAndTestId(partnerId, testId))
				.orElseThrow(() -> new ResourceNotFoundException("Test script details not found for the id :: " + partnerId));
		return ResponseEntity.ok().body(testScripts);
	}

	@PostMapping("/testScripts")
	public Map<String, Boolean> addTests(@Valid @RequestBody List<TestScripts> testScripts) {
		LOGGER.info("API call, POST: /testScripts");
		for(TestScripts testScript : testScripts) {
			testScriptsRepository.save(testScript);
		}
		Map<String, Boolean> response = new HashMap<>();
		response.put("inserted", Boolean.TRUE);
		return response;
	}

	@PutMapping("/testScripts/{partnerId}/{testId}")
	public ResponseEntity<TestScripts> updateTestcaseDetails(@PathVariable(value = "partnerId") String partnerId,
														 @PathVariable(value = "testId") String testId,
			@Valid @RequestBody TestScripts testInfoDetails) throws ResourceNotFoundException {
		LOGGER.info("API call, PUT: /testScripts/partnerId/testId");
		TestScripts testScripts = Optional.ofNullable(testScriptsRepository.findByPartnerIdAndTestId(partnerId, testId))
				.orElseThrow(() -> new ResourceNotFoundException("Test details not found for the id :: " + partnerId));

		BeanUtils.copyProperties(testInfoDetails, testScripts);
		final TestScripts updatedEmployee = testScriptsRepository.save(testScripts);
		return ResponseEntity.ok(updatedEmployee);
	}

	@DeleteMapping("/testScripts/{partnerId}")
	public Map<String, Boolean> deleteTestcaseByPartnerId(@PathVariable(value = "partnerId") String partnerId)
			throws ResourceNotFoundException {
		LOGGER.info("API call, DELETE: /testScripts/partnerId");
		Optional.ofNullable(testScriptsRepository.findByPartnerId(partnerId))
				.orElseThrow(() -> new ResourceNotFoundException("Test details not found for the id :: " + partnerId));

		testScriptsRepository.deleteByPartnerId(partnerId);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@DeleteMapping("/testScripts/{partnerId}/{testId}")
	public Map<String, Boolean> deleteTestcase(@PathVariable(value = "partnerId") String partnerId,
	                                           @PathVariable(value = "testId") String testId)
			throws ResourceNotFoundException {
		LOGGER.info("API call, DELETE: /testScripts/partnerId/testId");
		TestScripts testScripts = Optional.ofNullable(testScriptsRepository.findByPartnerIdAndTestId(partnerId, testId))
				.orElseThrow(() -> new ResourceNotFoundException("Test details not found for the id :: " + partnerId+"/"+testId));

		testScriptsRepository.delete(testScripts);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

}