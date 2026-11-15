Feature: Health and OpenAPI Documentation Endpoints

  Background:
    * url baseUrl

  Scenario: Actuator health endpoint returns UP
    Given path 'actuator/health'
    When method get
    Then status 200
    And match response.status == 'UP'

  Scenario: Actuator prometheus endpoint exposes metrics
    Given path 'actuator/prometheus'
    When method get
    Then status 200
    And match response contains 'jvm_'

  Scenario: Actuator jolokia endpoint exposes JMX metadata
    Given path 'actuator/jolokia'
    When method get
    Then status 200
    And match response.status == 200
    And match response.value.protocol == '7.2'
    And match response.value.mbeanCount == '#number? _ > 0'

  Scenario: Actuator caches endpoint lists Ehcache caches
    Given path 'actuator/caches'
    When method get
    Then status 200
    And match response.cacheManagers.cacheManager.caches contains { ratesPageCache: '#notnull', rateByMaturityCache: '#notnull' }

  Scenario: Swagger UI page is accessible
    Given path 'swagger-ui/index.html'
    When method get
    Then status 200
