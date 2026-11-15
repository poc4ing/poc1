Feature: Mortgage Check API

  Background:
    * url apiUrl

  Scenario: Feasible mortgage
    Given path 'mortgage-check'
    And request { income: 60000.0, maturityPeriod: 20, loanValue: 200000.0, homeValue: 250000.0 }
    When method post
    Then status 200
    And match header Content-Type contains 'application/json'
    And match response == { feasible: true, monthlyCosts: 1211.96 }

  Scenario: Exact boundary (loan == 4x income and loan == homeValue)
    Given path 'mortgage-check'
    And request { income: 50000.0, maturityPeriod: 20, loanValue: 200000.0, homeValue: 200000.0 }
    When method post
    Then status 200
    And match response == { feasible: true, monthlyCosts: 1211.96 }

  Scenario: Not feasible - loan exceeds 4x income
    Given path 'mortgage-check'
    And request { income: 40000.0, maturityPeriod: 20, loanValue: 200000.0, homeValue: 250000.0 }
    When method post
    Then status 200
    And match response == { feasible: false, monthlyCosts: 1211.96 }

  Scenario: Not feasible - loan exceeds 4x income by 0.01
    Given path 'mortgage-check'
    And request { income: 50000.0, maturityPeriod: 20, loanValue: 200000.01, homeValue: 250000.0 }
    When method post
    Then status 200
    And match response.feasible == false
    And match response.monthlyCosts == '#number'

  Scenario: Not feasible - loan exceeds homeValue
    Given path 'mortgage-check'
    And request { income: 80000.0, maturityPeriod: 20, loanValue: 260000.0, homeValue: 250000.0 }
    When method post
    Then status 200
    And match response.feasible == false
    And match response.monthlyCosts == 1575.55

  Scenario: Not feasible - loan exceeds both 4x income and homeValue
    Given path 'mortgage-check'
    And request { income: 30000.0, maturityPeriod: 20, loanValue: 200000.0, homeValue: 150000.0 }
    When method post
    Then status 200
    And match response == { feasible: false, monthlyCosts: 1211.96 }

  Scenario Outline: Monthly costs for maturityPeriod <maturityPeriod>
    Given path 'mortgage-check'
    And request { income: 60000.0, maturityPeriod: <maturityPeriod>, loanValue: 200000.0, homeValue: 250000.0 }
    When method post
    Then status 200
    And match response.feasible == true
    And match response.monthlyCosts == <expectedMonthlyCosts>

    Examples:
      | maturityPeriod | expectedMonthlyCosts |
      | 1              | 16893.22             |
      | 10             | 1977.72              |
      | 20             | 1211.96              |
      | 30             | 1013.37              |

  Scenario: Fetch rate and run mortgage check
    Given path 'rates'
    When method get
    Then status 200
    * def selectedRate = response.content[2]
    * match selectedRate.maturityPeriod == 10

    Given path 'mortgage-check'
    And request { income: 50000.0, maturityPeriod: '#(selectedRate.maturityPeriod)', loanValue: 150000.0, homeValue: 200000.0 }
    When method post
    Then status 200
    And match response.feasible == true
    And match response.monthlyCosts == 1483.29

  Scenario: Unknown maturityPeriod returns 400
    Given path 'mortgage-check'
    And request { income: 60000.0, maturityPeriod: 99, loanValue: 200000.0, homeValue: 250000.0 }
    When method post
    Then status 400
    And match response.title == 'Invalid maturity period'
    And match response.status == 400
    And match response.code == '3003'
    And match response.detail contains '99'

  Scenario Outline: Invalid field <field> returns 400
    Given path 'mortgage-check'
    And request <payload>
    When method post
    Then status 400
    And match response.title == 'Validation failed'
    And match response.status == 400
    And match response.code == '2001'

    Examples:
      | field          | payload                                                                        |
      | income         | { income: -1000.0, maturityPeriod: 20, loanValue: 200000.0, homeValue: 250000.0 } |
      | maturityPeriod | { income: 60000.0, maturityPeriod: 0, loanValue: 200000.0, homeValue: 250000.0 }  |
      | loanValue      | { income: 60000.0, maturityPeriod: 20, loanValue: -50.0, homeValue: 250000.0 }    |
      | homeValue      | { income: 60000.0, maturityPeriod: 20, loanValue: 200000.0, homeValue: -10.0 }    |

  Scenario: Missing API key on mortgage-check returns 401
    * configure headers = {}
    Given path 'mortgage-check'
    And request { income: 60000.0, maturityPeriod: 20, loanValue: 200000.0, homeValue: 250000.0 }
    When method post
    Then status 401
    And match response.status == 401
    And match response.code == '4001'
