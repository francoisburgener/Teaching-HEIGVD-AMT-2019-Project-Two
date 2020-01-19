Feature: Get trip by id

  Background:
    Given there is a travel server

  Scenario: Get our trip by id
    Given I have a admin token
    When I GET it to the /trips/1 endpoint
    Then I receive a 200 status code

  Scenario: Get trip of another account by id with admin token
    Given I have a admin token
    When I GET it to the /trips/15 endpoint
    Then I receive a 200 status code

  Scenario: Get trip of another account by id with user token
    Given I have a user token
    When I GET it to the /trips/1 endpoint
    Then I receive a 403 status code

  Scenario: Get a trip doesn't exist
    Given I have a admin token
    When I GET it to the /trips/100000 endpoint
    Then I receive a 404 status code