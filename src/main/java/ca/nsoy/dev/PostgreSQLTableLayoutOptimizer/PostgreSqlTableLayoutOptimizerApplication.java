package ca.nsoy.dev.PostgreSQLTableLayoutOptimizer;

import java.io.IOException;

import ca.nsoy.dev.PostgreSQLTableLayoutOptimizer.service.TableOptimizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PostgreSqlTableLayoutOptimizerApplication {

	private static final TableOptimizer tableOptimizer = new TableOptimizer();

	public static void main(String[] args) throws IOException {
		SpringApplication.run(PostgreSqlTableLayoutOptimizerApplication.class, args);
		tableOptimizer.run();
	}

}
