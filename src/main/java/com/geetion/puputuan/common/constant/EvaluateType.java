package com.geetion.puputuan.common.constant;

/**
 * Created by jian on 2016/3/27.
 */
public class EvaluateType {

    /**
     * 评价列表 pu_evaluate 表 的 type 字段
     */

    /**
     * 未评价
     */
    public static final int NOT_EVALUATE = 0;
    /**
     * 已评价 -- 喜欢
     */
    public static final int HAVE_EVALUATE_LIKE = 1;
    /**
     * 已评价 -- 不喜欢
     */
    public static final int HAVE_EVALUATE_UNLIKE = 2;

    /**
     * 评价列表 pu_evaluate 表 的 is_delete 字段
     */
    /**
     * 未删除
     */
    public static final boolean HAVE_DELETE = true;
    /**
     * 已删除
     */
    public static final boolean NO_DELETE = false;

}
