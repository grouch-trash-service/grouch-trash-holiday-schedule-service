Feature: A user can get a holiday
  Scenario: The user asks for a holiday that does not exist
    Given a valid request for a holiday
    When a user requets for a holiday
    Then a holiday is not returned

  Scenario: The user asks for a holiday that does exist
    Given a valid request for a holiday
    And the holiday exists
    When a user requets for a holiday
    Then a holiday is returned