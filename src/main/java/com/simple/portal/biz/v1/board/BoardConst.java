package com.simple.portal.biz.v1.board;

public class BoardConst {
    public static final String LIKE = "like";
    public static final String DIS_LIKE = "dislike";
    public static final String ITEM_GB_LIKE = "L";
    public static final String ITEM_GB_DISLIKE = "D";
    public static final String FAIL_ITEM_GB = "아이템 구분값이 올바르지 않습니다.";
    public static final String FAIL_PATH = "올바른 경로를 입력 하시오.";
    public static final String BODY_BLANK = "";
    public static final String FAIL_BOARD_LIST = "게시글이 존재하지 않습니다.";
    public static final String FAIL_REQUIRED_VALUE = "필수 값을 입력 하시오.";

    public static boolean isItemGbDisLike(String itemGb) {
        return BoardConst.ITEM_GB_DISLIKE.equals(itemGb);
    }

    public static boolean isItemGbLike(String itemGb) {
        return BoardConst.ITEM_GB_LIKE.equals(itemGb);
    }

    public static boolean isEventPath(String click) {
        return BoardConst.LIKE.equals(click) || BoardConst.DIS_LIKE.equals(click);
    }


}
