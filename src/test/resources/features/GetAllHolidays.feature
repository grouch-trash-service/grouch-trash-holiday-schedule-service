Feature: A User can get all Holidays
  Scenario: The user asks all holidays
    Given a valid request for all holidays
    When a user requests for all holidays
    Then all holidays are returned