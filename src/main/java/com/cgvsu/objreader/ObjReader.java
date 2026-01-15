package com.cgvsu.objreader;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.*;

public class ObjReader {

	private static final String V = "v";
	private static final String VT = "vt";
	private static final String VN = "vn";
	private static final String F = "f";

	public static Model read(String fileContent) {
		Model model = new Model();

		int lineInd = 0;
		Scanner scanner = new Scanner(fileContent);

		while (scanner.hasNextLine()) {
			lineInd++;
			String line = scanner.nextLine().trim();

			if (line.isEmpty() || line.startsWith("#")) {
				continue;
			}

			String[] tokens = line.split("\\s+");
			String keyword = tokens[0];

			ArrayList<String> args =
					new ArrayList<>(Arrays.asList(tokens).subList(1, tokens.length));

			switch (keyword) {
				case V -> model.vertices.add(parseVertex(args, lineInd));
				case VT -> model.textureVertices.add(parseTextureVertex(args, lineInd));
				case VN -> model.normals.add(parseNormal(args, lineInd));
				case F -> model.polygons.add(parseFace(args, model, lineInd));
				default -> {
				}
			}
		}

		validateModel(model);

		return model;
	}

	protected static Vector3f parseVertex(List<String> args, int lineInd) {
		if (args.size() != 3) {
			throw new ObjReaderException("Vertex must have exactly 3 coordinates.", lineInd);
		}
		return new Vector3f(parseFloat(args.get(0), lineInd),
				parseFloat(args.get(1), lineInd),
				parseFloat(args.get(2), lineInd));
	}

	protected static Vector2f parseTextureVertex(List<String> args, int lineInd) {
		if (args.size() < 2) {
			throw new ObjReaderException("Texture vertex must have at least 2 coordinates.", lineInd);
		}
		return new Vector2f(parseFloat(args.get(0), lineInd),
				parseFloat(args.get(1), lineInd));
	}

	protected static Vector3f parseNormal(List<String> args, int lineInd) {
		if (args.size() != 3) {
			throw new ObjReaderException("Normal must have exactly 3 coordinates.", lineInd);
		}
		return new Vector3f(parseFloat(args.get(0), lineInd),
				parseFloat(args.get(1), lineInd),
				parseFloat(args.get(2), lineInd));
	}

	protected static Polygon parseFace(
			List<String> args, Model model, int lineInd) {

		if (args.size() < 3) {
			throw new ObjReaderException("Polygon must have at least 3 vertices.", lineInd);
		}

		Polygon polygon = new Polygon();

		boolean hasVT = false;
		boolean hasVN = false;

		for (String word : args) {
			String[] parts = word.split("/", -1);

			int v = parseIndex(parts[0], model.vertices.size(), lineInd);
			polygon.getVertexIndices().add(v);

			if (parts.length > 1 && !parts[1].isEmpty()) {
				hasVT = true;
				polygon.getTextureVertexIndices().add(
						parseIndex(parts[1], model.textureVertices.size(), lineInd));
			}

			if (parts.length > 2 && !parts[2].isEmpty()) {
				hasVN = true;
				polygon.getNormalIndices().add(
						parseIndex(parts[2], model.normals.size(), lineInd));
			}
		}

		if (hasVT && polygon.getTextureVertexIndices().size() != polygon.getVertexIndices().size()) {
			throw new ObjReaderException("Polygon has incomplete texture coordinates.", lineInd);
		}

		if (hasVN && polygon.getNormalIndices().size() != polygon.getVertexIndices().size()) {
			throw new ObjReaderException("Polygon has incomplete normals.", lineInd);
		}

		return polygon;
	}
	protected static void validateModel(Model model) {
		if (model.vertices.isEmpty()) {
			throw new ObjReaderException("Model has no vertices.", -1);
		}

		if (model.polygons.isEmpty()) {
			throw new ObjReaderException("Model has no polygons.", -1);
		}
	}

	private static float parseFloat(String value, int lineInd) {
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			throw new ObjReaderException("Invalid float value.", lineInd);
		}
	}



	private static int parseIndex(String value, int size, int lineInd) {
		try {
			int idx = Integer.parseInt(value);

			if (idx == 0) {
				throw new ObjReaderException("OBJ indices start from 1.", lineInd);
			}

			int result = idx > 0 ? idx - 1 : size + idx;

			if (result < 0 || result >= size) {
				throw new ObjReaderException("Index out of bounds.", lineInd);
			}

			return result;

		} catch (NumberFormatException e) {
			throw new ObjReaderException("Invalid index value.", lineInd);
		}
	}
}

