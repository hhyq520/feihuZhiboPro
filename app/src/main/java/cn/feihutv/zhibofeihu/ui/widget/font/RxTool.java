package cn.feihutv.zhibofeihu.ui.widget.font;

import android.content.Context;

/**
 * Created by vondear on 2016/1/24.
 * RxTools的常用工具类
 * <p>
 * For the brave souls who get this far: You are the chosen ones,
 * the valiant knights of programming who toil away, without rest,
 * fixing our most awful code. To you, true saviors, kings of men,
 * I say this: never gonna give you up, never gonna let you down,
 * never gonna run around and desert you. Never gonna make you cry,
 * never gonna say goodbye. Never gonna tell a lie and hurt you.
 * <p>
 * 致终于来到这里的勇敢的人：
 * 你是被上帝选中的人，是英勇的、不敌辛苦的、不眠不休的来修改我们这最棘手的代码的编程骑士。
 * 你，我们的救世主，人中之龙，我要对你说：永远不要放弃，永远不要对自己失望，永远不要逃走，辜负了自己，
 * 永远不要哭啼，永远不要说再见，永远不要说谎来伤害自己。
 */
public class RxTool {

    private static Context context;
    private static long lastClickTime;

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        RxTool.context = context.getApplicationContext();
    }

    /**
     * 在某种获取不到 Context 的情况下，即可以使用才方法获取 Context
     * <p>
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("");
    }
}
