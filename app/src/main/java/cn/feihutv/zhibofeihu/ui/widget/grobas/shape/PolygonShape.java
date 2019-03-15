package cn.feihutv.zhibofeihu.ui.widget.grobas.shape;

import android.graphics.Path;

/**
 * <pre>
 *      author : liwen.chen
 *      time   : 2017/10/24 17:01
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public interface PolygonShape {

    /**
     * Return a closed valid Path
     *
     * @param polygonShapeSpec polygonal specs
     * @return a Path
     */
    Path getPolygonPath(PolygonShapeSpec polygonShapeSpec);
}
