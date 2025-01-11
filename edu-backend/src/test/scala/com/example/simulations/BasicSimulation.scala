package com.example.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  // Define HTTP protocol configuration
  val httpProtocol = http
    .baseUrl("http://localhost:8080") // Replace with your API base URL
    .acceptHeader("application/json") // Common headers
    .userAgentHeader("Gatling")

  // Define a scenario
  val scn = scenario("Basic Load Test")
    .exec(
      http("GET Request")
        .get("/quizAttempt/userId/user1") // Replace with your API endpoint
        .check(status.is(200))
    )

  // Setup load simulation
  setUp(
    scn.inject(
      atOnceUsers(2), // Simulate 10 users at once
      rampUsers(10).during(30.seconds) // Gradually ramp up to 10 users over 30 seconds
    )
  ).protocols(httpProtocol)
}
