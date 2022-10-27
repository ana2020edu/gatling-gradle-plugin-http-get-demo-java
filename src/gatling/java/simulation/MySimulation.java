package simulation;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class MySimulation extends Simulation {
    public static final int TPS = 30;

    ScenarioBuilder myScenario = scenario("MyScenario").exec(
            http("Scenario1")
                    .get("http://localhost:7000/ping")
                    //.get("http://localhost:8000/ping")
                    .requestTimeout(Duration.ofSeconds(10))
                    .check(status().is(200))
    );

    {
        setUp(
                myScenario.injectOpen(
                        rampUsersPerSec(1).to(TPS).during(Duration.ofSeconds(30)),
                        rampUsersPerSec(TPS).to(1).during(Duration.ofSeconds(5))
                )
        ).protocols(http.shareConnections());
    }

}
