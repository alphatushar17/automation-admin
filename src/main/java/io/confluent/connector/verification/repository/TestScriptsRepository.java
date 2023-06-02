package io.confluent.connector.verification.repository;

import io.confluent.connector.verification.model.ReleaseInfo;
import io.confluent.connector.verification.model.TestScripts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestScriptsRepository extends JpaRepository<TestScripts, Long> {

    @Query(value = "SELECT * FROM testcase_info WHERE PARTNER_ID = ?1", nativeQuery = true)
    List<TestScripts> findByPartnerId(String partnerId);

    @Query(value = "SELECT * FROM testcase_info WHERE PARTNER_ID = ?1 AND TEST_ID = ?2", nativeQuery = true)
    TestScripts findByPartnerIdAndTestId(String partnerId, String testId);

    @Query(value = "DELETE FROM testcase_info WHERE PARTNER_ID = ?1", nativeQuery = true)
    TestScripts deleteByPartnerId(String partnerId);
}
