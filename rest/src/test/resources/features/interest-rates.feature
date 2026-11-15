Feature: Rates API

  Background:
    * url apiUrl

  Scenario: Default page of rates
    Given path 'rates'
    When method get
    Then status 200
    And match header Content-Type contains 'application/json'
    And match response.totalElements == 7
    And match response.totalPages == 1
    And match response.number == 0
    And match response.content == '#[7]'
    And match each response.content ==
      """
      {
        id: '#regex ^[0-9a-f]{8}-[0-9a-f]{4}-7[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$',
        maturityPeriod: '#number? _ >= 1',
        interestRate: '#number? _ >= 0',
        lastUpdate: '#string'
      }
      """

  Scenario: Default sort by maturityPeriod ascending
    Given path 'rates'
    When method get
    Then status 200
    And match response.content[*].maturityPeriod == [1, 5, 10, 15, 20, 25, 30]
    And match response.content[*].interestRate == [2.5, 3.0, 3.5, 3.75, 4.0, 4.25, 4.5]

  Scenario: Custom page and descending sort
    Given path 'rates'
    And param page = 1
    And param size = 3
    And param sort = 'maturityPeriod,desc'
    When method get
    Then status 200
    And match response.totalElements == 7
    And match response.totalPages == 3
    And match response.size == 3
    And match response.number == 1
    And match response.content == '#[3]'
    And match response.content[*].maturityPeriod == [15, 10, 5]

  Scenario: Missing or invalid API key returns 401
    * configure headers = { 'X-API-Key': 'invalid-key' }
    Given path 'rates'
    When method get
    Then status 401
    And match response.status == 401
    And match response.code == '4001'
