Feature: A User can create holidays
  Scenario: The user creates a new holiday
    Given a valid request to create a new holiday
    When a user requests to create a holiday
    Then the holiday is created

  Scenario: The user attempts to create a new holiday that already exists
    Given a valid request to create a new holiday
    And the holiday exists
    When a user requests to create a holiday
    Then the holiday is not created