Feature: A User can update holidays
  Scenario: The user updates an existing holiday
    Given the holiday exists
    And a valid request to update a holiday
    When a user requests to update a holiday
    Then the holiday is updated

  Scenario: The user updates a holiday that does not exist
    Given a valid request to update a holiday
    When a user requests to update a holiday
    Then the holiday is created
