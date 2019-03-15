package cn.feihutv.zhibofeihu.rxbus;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : Rx bus Thread mode
 *     version: 1.0
 * </pre>
 */
public enum ThreadMode {
    /**
     * current thread
     */
    CURRENT_THREAD,

    /**
     * android main thread
     */
    MAIN,


    /**
     * new thread
     */
    NEW_THREAD
}
