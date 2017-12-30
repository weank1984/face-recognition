package com.tzutalin.dlibtest.mat;

/**
 * Created by weank on 4/19/17.
 */

import android.nfc.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import timber.log.Timber;

public class DivideGroup {

    private static final String TAG = "dlib";//"MainActivity";

    public ArrayList<Group> groups;
    public ArrayList<float[]> featureMat;
    static float threshold = (float)0.3;


    public void Put(ArrayList<float[]> featureMat)
    {
        this.groups = new ArrayList<>();
        this.featureMat = featureMat;
        int sign = 0;
        for (float[] feature:featureMat) {
            ArrayList<Integer> id = new ArrayList<>();
            id.add(sign++);
            Group mGroup = new Group();
            try {
                mGroup.Put(feature, id);
            }catch (Exception e) {
                System.out.println(e);
            }
            this.groups.add(mGroup);
        }
    }



    public void mergeGroup(Group aGroup, Group bGroup)
    {
        ArrayList<Integer> n_id = bGroup.id;
        // 将后者并入前一个group
        aGroup.updateMember(n_id, featureMat);
        // 移除被合并的group
        groups.remove(bGroup);
    }

    public boolean updateDistance()
    {
        int[] pair = new int[2];
        float minDistance = 10;
        int size = groups.size();
        // group数量为1，停止合并
        if (size <= 2)
        {
            return false;
        }
        // 计算最短欧式距离
        for (int i = 0; i < size; ++i)
        {
            float[] alpha = groups.get(i).meanVec;
            for (int j = i+1; j < size; ++j)
            {
                float[] beta = groups.get(j).meanVec;
                float tempDistance = calDistance(alpha, beta);
                // 保存最近距离和对象
                if (tempDistance < minDistance) {
                    minDistance = tempDistance;
                    pair[0] = i;
                    pair[1] = j;
                }
            }
        }
        // 收紧半径，距离大于半径时停止合并
        if (minDistance > threshold)
        {
            return false;
        }
        // 合并距离最短的两个group
        mergeGroup(groups.get(pair[0]), groups.get(pair[1]));
        return true;
    }


    // 计算欧式距离
    public float calDistance(float[] arr1, float[] arr2)
    {
        assert (arr1.length == arr2.length);
        float res = 0;
        for (int i = 0; i < arr1.length; ++i)
        {
            res += Math.pow((arr1[i] - arr2[i]), 2);
        }
        res = (float)Math.sqrt(res);
//        Timber.tag(TAG).d("distance of two vec: " + res);
        return res;
    }

//    public ArrayList<float[]> readmat (String matFile) throws IOException
//    {
//        MatFileReader read = new MatFileReader(matFile);
//        MLArray mlArray = read.getMLArray("test_img");
//        MLDouble d = (MLDouble)mlArray;
//        float[][] matrix = (d.getArray());
//
//        int size = matrix.length;
//        ArrayList<float[]> featureMat = new ArrayList<>();
//        for (int i = 0; i < 118; i++) {
//            featureMat.add(matrix[i]);
//        }
//        return featureMat;
//    }
}

