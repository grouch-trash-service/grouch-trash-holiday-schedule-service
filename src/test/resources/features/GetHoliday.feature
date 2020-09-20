Feature: A user should be able to get a holiday
  Scenario: The user asks for a holiday that does not exist
    Given A valid request for a holiday
    When Request for a holiday
    Then a holiday is not returned

  Scenario: The user asks for a holiday that does exist
    Given A valid request for a holiday
    And the holiday exists
    When Request for a holiday
    Then a holiday is returned