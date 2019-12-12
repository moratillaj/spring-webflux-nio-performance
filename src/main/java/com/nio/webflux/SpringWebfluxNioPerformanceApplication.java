package com.nio.webflux;

import com.nio.webflux.model.Car;
import com.nio.webflux.repository.CarsRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@SpringBootApplication
public class SpringWebfluxNioPerformanceApplication {

	@Autowired
	private CarsRepository carsRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxNioPerformanceApplication.class, args);
	}

	@Component
	public class CarsCreator implements CommandLineRunner {

		@Override
		public void run(String... args) {
			Stream.of(
					Car.builder().id("1111AAA").model("model1").year(2001).build(),
					Car.builder().id("2222BBB").model("model2").year(2002).build(),
					Car.builder().id("3333CCC").model("model3").year(2003).build(),
					Car.builder().id("4444DDD").model("model4").year(2004).build(),
					Car.builder().id("5555EEE").model("model5").year(2005).build()
			).forEach(carsRepository::save);

		}
	}
}
