package com.tzutalin.dlibtest.mat;

/**
 * Created by weank on 4/19/17.
 */

import java.util.ArrayList;

public final class Group extends MatrixCommon{
    public float[] meanVec;
    public ArrayList<Integer> id;


    public void Put(float[] meanVec, ArrayList<Integer> id)
    {
        this.meanVec = meanVec;
        this.id = id;
    }


    public void updateMean(ArrayList<float[]> featureMat)
    {
        int row = id.size();
        int col = featureMat.get(0).length;
        // 初始化sum为0的向量
        float[] sum = new float[col];
        for (int i = 0; i < col; i++) {
            sum[i] = 0;
        }
        // 计算均值向量
        for(int i = 0; i < row; ++i)
        {
            // warning: be careful to index
            sum = mergeVec(sum, featureMat.get(id.get(i)));
        }
        // warning: convert row to float type
        sum = multiplyVec(sum, (1/(float)row));
        // 更新均值向量
        this.meanVec = sum;
    }


    public void updateMember(ArrayList<Integer> n_id, ArrayList<float[]> featureMat)
    {
        if (n_id.isEmpty())
        {
            System.out.println("error: id is empty");
        }
        // 添加新成员到成员列表
        int size = n_id.size();
        for (int i = 0; i < size; ++i) {
            id.add(n_id.get(i));
        }
        // 更新成员后必须更新均值向量
        updateMean(featureMat);
    }

}

