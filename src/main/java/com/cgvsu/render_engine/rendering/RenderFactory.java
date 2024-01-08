package com.cgvsu.render_engine.rendering;

import com.cgvsu.render_engine.rasterization.Rasterization;

import java.util.EnumMap;
import java.util.Map;

public class RenderFactory {
    private RenderFactory() {
        renders = new EnumMap<>(RenderType.class);
    }

    private static RenderFactory INSTANCE;

    public static RenderFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RenderFactory();
        }
        return INSTANCE;
    }
    private final Map<RenderType, Render> renders;



    public Render createRender(RenderType type){
        var render = renders.get(type);
        if (render == null){
            switch (type){
                case TEXTURE -> render = new RenderTexture();
                case SELECTION_VERTICES -> render = new RenderSelectionVertices();
                case SELECTION_POLYGONS -> render = new RenderSelectionPolygons();
                case SELF_COLORED-> render = new RenderSelfColored();
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
