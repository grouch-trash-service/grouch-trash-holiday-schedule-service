Feature: A User can delete holidays
  Scenario: The user deletes an existing holiday
    Given the holiday exists
    And a valid request to delete a holiday
    When a user requests to delete a holiday
    Then the holiday is deleted

  Scenario: The user deletes a holiday that does not exist
    Given a valid request to delete a holiday
    When a user requests to delete a holiday
    Then the holiday is deleted