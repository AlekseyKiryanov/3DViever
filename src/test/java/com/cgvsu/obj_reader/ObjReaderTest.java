package com.cgvsu.obj_reader;

import com.cgvsu.vectormath.vector.Vector3D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;

class ObjReaderTest {

    @Test
    public void testParseVertex01() {
        ArrayList<String> wordsInLineWithoutToken = new ArrayList<>(Arrays.asList("1.01", "1.02", "1.03"));
        Vector3D result = ObjReader.parseVertex(wordsInLineWithoutToken, 5);
        Vector3D expectedResult = new Vector3D(1.01f, 1.02f, 1.03f);
        Assertions.assertEquals(result, expectedResult);
    }

    @Test
    public void testParseVertex02() {
        ArrayList<String> wordsInLineWithoutToken = new ArrayList<>(Arrays.asList("1.01", "1.02", "1.03"));
        Vector3D result = ObjReader.parseVertex(wordsInLineWithoutToken, 5);
        Vector3D expectedResult = new Vector3D(1.01f, 1.02f, 1.10f);
        Assertions.assertNotEquals(result, expectedResult);
    }



}