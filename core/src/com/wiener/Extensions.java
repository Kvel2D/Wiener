package com.wiener;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Scanner;

public class Extensions
{
    public static void rotateAround(Vector2 point, float xOrigin, float yOrigin, float angle)
    {
        float angleConverted = angle * Constants.DEGTORAD;

        float cos = (float)Math.cos(angleConverted);
        float sin = (float)Math.sin(angleConverted);

        point.add(-xOrigin, -yOrigin);
        Vector2 temp = point.cpy();
        point.x = temp.x * cos - temp.y * sin;
        point.y = temp.x * sin + temp.y * cos;
        point.add(xOrigin, yOrigin);
    }

    public static void translate(float[] vertices, float xOrigin, float yOrigin, float angle) {
        int i = 0;
        while (i < vertices.length)
        {
            Vector2 rotatedPoint = new Vector2(vertices[i], vertices[i + 1]);
            rotateAround(rotatedPoint, 0f, 0f, angle);
            vertices[i] = rotatedPoint.x + xOrigin;
            vertices[i + 1] = rotatedPoint.y + yOrigin;
            i += 2;
        }
    }

    public static String[] toStringArray(FileHandle file)
    {
        String text = file.readString();

        return text.split("[\\r\\n]+");
    }

    public static float[] toVertices(FileHandle file)
    {
        String[] strings = toStringArray(file);
        ArrayList<Vector2> linesBuffer = new ArrayList<Vector2>();

        for (String line : strings)
        {
            int spaceSeparator = line.indexOf(" ");

            float x = Float.parseFloat(line.substring(0, spaceSeparator));
            float y = Float.parseFloat(line.substring(spaceSeparator + 1));

            linesBuffer.add(new Vector2(x, y));
        }

        float[] lines = new float[linesBuffer.size() * 2];

        for (int i = 0; i < linesBuffer.size(); i++)
        {
            lines[i * 2] = linesBuffer.get(i).x;
            lines[i * 2 + 1] = linesBuffer.get(i).y;
        }

        return lines;
    }
}
