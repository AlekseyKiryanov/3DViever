package com.cgvsu.render_engine.rendering;

import java.util.EnumMap;
import java.util.Map;

public class RenderFactory {
    private final Map<RenderType, Render> renders;

    public RenderFactory() {
        renders = new EnumMap<>(RenderType.class);
    }

    public Render createRender(RenderType type){
        var render = renders.get(type);
        if (render == null){
            switch (type){
                case NO_MESH -> render = new RenderNoMesh();
                case SELECTION_VERTICES -> render = new RenderSelectionVertices();
                case SELECTION_POLYGONS -> render = new RenderSelectionPolygons();
                default -> {

                }
            }
            if (render != null){
                renders.put(type,render);
            }
        }
        return render;
    }
}
