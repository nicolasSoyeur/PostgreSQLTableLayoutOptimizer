package ca.nsoy.dev.PostgreSQLTableLayoutOptimizer.constant;

import java.util.HashMap;
import java.util.Map;

public class TypeSizeConstants {

	// Map de la taille approximative des types de données en bytes
	public static final Map<String, Integer> TYPE_SIZE_MAP = new HashMap<>();

	static {
		// Initialisation de la map avec les tailles approximatives
		TYPE_SIZE_MAP.put("int8", 8);
		TYPE_SIZE_MAP.put("float8", 8);
		TYPE_SIZE_MAP.put("timestamp", 8);
		TYPE_SIZE_MAP.put("bigint", 8);
		TYPE_SIZE_MAP.put("int4", 4);
		TYPE_SIZE_MAP.put("int", 4);
		TYPE_SIZE_MAP.put("date", 4);
		TYPE_SIZE_MAP.put("int2", 2);
		TYPE_SIZE_MAP.put("boolean", 1);
		TYPE_SIZE_MAP.put("char", 255);     // Taille par défaut
		TYPE_SIZE_MAP.put("varchar", 255);  // Taille moyenne d'un varchar
		TYPE_SIZE_MAP.put("text", 65535);   // Estimation maximale
		TYPE_SIZE_MAP.put("decimal", 16);   // Taille approximative pour un DECIMAL standard
		TYPE_SIZE_MAP.put("uuid", 16);      // Taille approximative pour un UUID
	}
}

