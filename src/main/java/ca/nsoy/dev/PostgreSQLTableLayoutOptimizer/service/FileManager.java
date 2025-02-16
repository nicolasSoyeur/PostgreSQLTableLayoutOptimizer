package ca.nsoy.dev.PostgreSQLTableLayoutOptimizer.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

	public static List<String> readFile(String filename) throws IOException {
		List<String> lines = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		}
		return lines;
	}

	public static void writeFile(String filename, List<String> lines) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
		}
	}
}
