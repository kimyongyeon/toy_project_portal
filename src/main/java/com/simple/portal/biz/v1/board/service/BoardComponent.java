package com.simple.portal.biz.v1.board.service;

import com.simple.portal.biz.v1.board.BoardConst;

public class BoardComponent {
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
