@DemoFeatureFile

Feature: Demo

#  Background:
#    Given Open the browser then lunch application

  Scenario Outline: Open Web - <Description>
    Given scenario executes <TestCase> to verify for journey
    Then Reset the credential
    When Enter the Username and Password
    Examples:
      | TestCase | Description              |
      | TC1      | login with first account |
      | TC2      | login with second account |
