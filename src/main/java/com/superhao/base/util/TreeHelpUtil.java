package com.superhao.base.util;

import java.util.UUID;

/**
 * @Auther:
 * @Date: 2019/4/24 22:46
 * @email:
 * @Description:
 */
public class TreeHelpUtil {
    public static String[] DEEP_CODE_SET="A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");

    /**
     *  treeDeep+treeId 生成该层的唯一编码
     * @param treeDeep 深度
     * @param treeId  数id
     * @return
     */
    public static String createDeepCode(Integer treeDeep,Long treeId){
        /**
         * 深度超过25 用uuid替代
         */
        if(treeDeep>DEEP_CODE_SET.length-1){
            return UUID.randomUUID().toString().replace("-","");
        }
        return DEEP_CODE_SET[treeDeep]+treeId.toString();
    }

    public static void main(String[] args) {
        System.out.println(DEEP_CODE_SET.length);
        System.out.println(UUID.randomUUID().toString().replace("-",""));
        System.out.println(UUID.randomUUID().toString().replace("-","").length());
    }
}
