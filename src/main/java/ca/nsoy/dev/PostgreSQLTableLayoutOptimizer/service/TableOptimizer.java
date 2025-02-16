package ca.nsoy.dev.PostgreSQLTableLayoutOptimizer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ca.nsoy.dev.PostgreSQLTableLayoutOptimizer.constant.TypeSizeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TableOptimizer {

	// Nom du fichier DDL d'entrée
	public static final String INPUT_FILE = "ddlFiles/input.sql";
	// Nom du fichier DDL de sortie
	public static final String OUTPUT_FILE = "ddlFiles/output.sql";

	private static final Logger logger = LoggerFactory.getLogger(TableOptimizer.class);

	public void run() throws IOException {
		// Lire le fichier DDL d'entrée
		List<String> ddlLines = FileManager.readFile(INPUT_FILE);

		// Réorganiser les colonnes dans l'ordre optimal
		List<String> optimizedDdlInstructions = optimizeColumnsOrder(ddlLines);

		// Écrire le DDL optimisé dans un fichier de sortie
		FileManager.writeFile(OUTPUT_FILE, optimizedDdlInstructions);

		logger.info("Optimisation terminée et sauvegardée dans " + OUTPUT_FILE);
	}

	private List<String> optimizeColumnsOrder(List<String> ddlLines) {
		List<String> ddlScriptOptimizedLines = new ArrayList<>();
		var columnInformationList = SQLParser.extractColumnsInformation(ddlLines);
		columnInformationList.sort(Comparator.comparingInt(column -> -getColumnSize(column.getRight().type())));

		// Ajouter la ligne CREATE TABLE avec les colonnes optimisées
		ddlScriptOptimizedLines.add(ddlLines.get(0));

		columnInformationList.forEach(column -> ddlScriptOptimizedLines.add(column.getLeft()));

		ddlScriptOptimizedLines.add(ddlLines.get(ddlLines.size()-1));

		return ddlScriptOptimizedLines;
	}

	private int getColumnSize(String type) {
		// Utiliser la taille approximative des types ou une valeur par défaut si le type est inconnu
		return TypeSizeConstants.TYPE_SIZE_MAP.getOrDefault(type.toLowerCase(), 10);
	}

}
