package com.tzutalin.dlibtest.mat;

/**
 * Created by weank on 4/19/17.
 */

import java.util.ArrayList;

/**
 * Created by Wean k on 2017/4/19.
 */
public class MatrixCommon {

    // 向量相加
    public float[] mergeVec(float[] vec1, float[] vec2)
    {
        int size = vec1.length;
        if (vec1.length != vec2.length)
            return null;
        float[] res = new float[size];
        for(int i =0; i < size; ++i)
        {
            res[i] = vec1[i] + vec2[i];
        }
        return res;
    }


    // 向量乘以标量
    public float[] multiplyVec(float[] vec, float num)
    {
        int size = vec.length;
        float[] res = new float[size];
        for (int i = 0; i < size; ++i)
        {
            res[i] = vec[i] * num;
        }
        return res;
    }


    // 合并向量矩阵
    public float[] mergeMat(ArrayList<float[]> mat) {
        if (mat.isEmpty()) {
            return null;
        }
        int row = mat.size();
        // warning: don't delete
        int col = mat.get(0).length;
        float[] res = mat.get(0);
        for (int i = 1; i < row; ++i) {
            res = mergeVec(res, mat.get(i));
        }
        return res;
    }
}

