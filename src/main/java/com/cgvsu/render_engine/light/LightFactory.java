package com.cgvsu.render_engine.light;


import java.util.EnumMap;
import java.util.Map;

public class LightFactory {
    private final Map<LightType, Light> lights;

    public LightFactory() {
        lights = new EnumMap<>(LightType.class);
    }

    public Light createLight(LightType type) {
        var light = lights.get(type);
        if (light == null) {
            switch (type) {
                case PRIMARY -> light = new PrimeLight();
                case POLYGON -> light = new PolygonLight();
                case SPECULAR -> light = new SpecularLight();
                case BORDER -> light = new BorderLight();
                case NONE -> light = new NoneLight();
                default -> {

                }
            }
            if (light != null) {
                lights.put(type, light);
            }
        }
        return light;
    }

    public void setSpecColor(int color){
        if (lights.get(LightType.SPECULAR)== null){
            lights.put(LightType.SPECULAR, new SpecularLight());
        }
        lights.get(LightType.SPECULAR).setSpecColor(color);

        if (lights.get(LightType.BORDER)== null){
            lights.put(LightType.BORDER, new BorderLight());
        }
        lights.get(LightType.BORDER).setSpecColor(color);

    }

    public void setReflectionColor(int color){
        if (lights.get(LightType.BORDER)== null){
            lights.put(LightType.BORDER, new BorderLight());
        }
        lights.get(LightType.BORDER).setReflectionColor(color);

    }
}
