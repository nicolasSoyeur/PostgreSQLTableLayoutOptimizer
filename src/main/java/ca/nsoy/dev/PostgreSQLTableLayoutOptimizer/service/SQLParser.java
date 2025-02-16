package ca.nsoy.dev.PostgreSQLTableLayoutOptimizer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.nsoy.dev.PostgreSQLTableLayoutOptimizer.model.Column;
import org.apache.commons.lang3.tuple.Pair;

public class SQLParser {

	public static List<Pair<String, Column>> extractColumns(List<String> ddlScript) {
		List<Pair<String, Column>> columnsWithLineDefinition = new ArrayList<>();
		// On copie la liste en ne prennant pas la premiere ligne CREATE TABLE
		var ddlScriptWithoutCreateTable = ddlScript.subList(1, ddlScript.size());

		// Expression régulière pour capturer le nom et le type de la colonne
		Pattern pattern = Pattern.compile("\\s*(\\w+)\\s+(\\w+)(\\(\\d+\\))?");

		for (String line : ddlScriptWithoutCreateTable) {
			line = line.trim(); // Supprime les espaces autour de la ligne

			if (line.startsWith(")")) {
				break; // On termine l'analyse des colonnes si on atteint la parenthèse fermante
			}

			// Appliquer l'expression régulière pour extraire le nom et le type de la colonne
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				String columnName = matcher.group(1);
				String columnType = matcher.group(2);
				columnsWithLineDefinition.add(Pair.of(line,new Column(columnName, columnType)));
				System.out.println("Column: "+columnsWithLineDefinition);
			}
			else{
				System.out.println("Matcher not find");
			}
		}

		return columnsWithLineDefinition;
	}
}
