package ca.nsoy.dev.PostgreSQLTableLayoutOptimizer.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.nsoy.dev.PostgreSQLTableLayoutOptimizer.model.Column;
import org.apache.commons.lang3.tuple.Pair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;


public class TableOptimizer {

	// Map de la taille approximative des types de données en bytes (ordre optimisé pour PostgreSQL)
	private static final Map<String, Integer> typeSizeMap = new HashMap<>();

	private static final String REGEX_CREATE_TABLE = "(?i)CREATE TABLE\\s+([a-zA-Z0-9_]+)\\s*\\((.*)\\)";


	@Autowired
	private ResourceLoader resourceLoader;

	static {
		// Définir la taille approximative en bytes pour chaque type
		typeSizeMap.put("int8", 8);
		typeSizeMap.put("float8", 8);
		typeSizeMap.put("timestamp", 8);
		typeSizeMap.put("bigint", 8);
		typeSizeMap.put("int4", 4);
		typeSizeMap.put("date", 4);
		typeSizeMap.put("int2", 2);
		typeSizeMap.put("boolean", 1);
		typeSizeMap.put("char", 255);     // Taille par défaut
		typeSizeMap.put("varchar", 255);  // Taille moyenne d'un varchar
		typeSizeMap.put("text", 65535);   // Estimation maximale
		typeSizeMap.put("decimal", 16);   // Taille approximative pour un DECIMAL standard
	}

	public void run() throws IOException {
		// Nom du fichier DDL d'entrée
		String inputFile = "ddlFiles/input.sql";
		// Nom du fichier DDL de sortie
		String outputFile = "ddlFiles/output.sql";

		// Lire le fichier DDL d'entrée
		List<String> ddlLines = readFile(inputFile);

		// Réorganiser les colonnes dans l'ordre optimal
		List<String> optimizedDdl = optimizeColumns(ddlLines);

		// Écrire le DDL optimisé dans un fichier de sortie
		writeFile(outputFile, optimizedDdl);

		System.out.println("Optimisation terminée et sauvegardée dans " + outputFile);
	}

	private List<String> readFile(String filename) throws IOException {
		List<String> lines = new ArrayList<>();


		try (BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		}
		return lines;
	}

	private void writeFile(String filename, List<String> lines) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
		}
	}

	private List<String> optimizeColumns(List<String> ddlLines) {
		List<String> optimizedLines = new ArrayList<>();
		var columnList = SQLParser.extractColumns(ddlLines);
		columnList.sort(Comparator.comparingInt(column -> -getColumnSize(column.getRight().type())));
		StringBuilder optimizedColumns = new StringBuilder();
		// Ajouter la ligne CREATE TABLE avec les colonnes optimisées
		optimizedLines.add(ddlLines.get(0));
		for (Pair<String, Column> column : columnList) {
			if (optimizedColumns.length() > 0) {
				optimizedColumns.append(", ");
			}
			optimizedLines.add(column.getLeft());
		}
		optimizedLines.add(ddlLines.get(ddlLines.size()-1));

		return optimizedLines;
	}

	private void checkIfCreateTableIsMatcheable(List<String> ddlLines, Pattern createTablePattern) {
		if(!createTablePattern.matcher(ddlLines.get(0)).find()){
			System.out.println(ddlLines.get(0));
			throw new RuntimeException("It is not possible to find the CREATE TABLE instruction in your file");
		}
	}


	private String getTableName(String line) {
		Pattern pattern = Pattern.compile("CREATE TABLE (\\w+)");
		Matcher matcher = pattern.matcher(line);
		return matcher.find() ? matcher.group(1) : "unknown";
	}

	private int getColumnSize(String type) {
		// Utiliser la taille approximative des types ou une valeur par défaut si le type est inconnu
		return typeSizeMap.getOrDefault(type, 10);
	}

}
