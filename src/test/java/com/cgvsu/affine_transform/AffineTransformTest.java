package com.cgvsu.affine_transform;

import com.cgvsu.vectormath.vector.Vector3D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AffineTransformTest {

    @Test
    void transformVertex01() {
        AffineTransform A = new AffineTransform();
        Vector3D V = new Vector3D(2,3,4);
        Assertions.assertEquals(new Vector3D(2,3,4),A.transformVertex(V));
    }

    @Test
    void transformVertex02() {
        AffineTransform A = new AffineTransform();
        A.setRotationZ(-90F);
        Vector3D V = new Vector3D(0,1,0);
        Assertions.assertEquals(new Vector3D(-1,0,0),A.transformVertex(V));
    }

    @Test
    void transformVertex02a() {
        AffineTransform A = new AffineTransform();
        A.setRotationY(-90);
        Vector3D V = new Vector3D(1F,7F,0F);
        Assertions.assertEquals(new Vector3D(0F,7F,1F),A.transformVertex(V));
    }


    @Test
    void transformVertex03() {
        AffineTransform A = new AffineTransform(RotationOrder.ZYX,2,6,0.5F,0,0,-60,11,12,15);

        Vector3D V = new Vector3D((float)(Math.sqrt(3)/4),(float)1/12,4);
        Assertions.assertEquals(new Vector3D(11,13,17),A.transformVertex(V));
    }


    @Test
    void transformVertex04() {
        AffineTransform A = new AffineTransform();
        A.setRotationOrder(RotationOrder.ZYX);
        A.setRotationZ(-45);
        A.setRotationY(-90);
        Vector3D V = new Vector3D(1,0,0);
        Assertions.assertEquals(new Vector3D(0,(float) Math.sqrt(2)/2,(float) Math.sqrt(2)/2),A.transformVertex(V));
    }

    @Test
    void transformVertex04a() {
        AffineTransform A = new AffineTransform();
        A.setRotationOrder(RotationOrder.YXZ);
        A.setRotationZ(-45);
        A.setRotationY(-90);
        Vector3D V = new Vector3D(1,0,0);
        Assertions.assertEquals(new Vector3D(0,0,1),A.transformVertex(V));
    }
}