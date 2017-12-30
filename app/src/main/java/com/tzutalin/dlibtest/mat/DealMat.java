package com.tzutalin.dlibtest.mat;

import java.util.*;

import timber.log.Timber;

/**
 * Created by Wean K on 2017/4/13.
 */
public class DealMat {

    private static final String TAG = "dlib";

    public boolean CalDistance(float[] arr1, float[] arr2)
    {
        assert (arr1.length == arr2.length);
        double res = 0;
        for (int i = 0; i < 128; ++i)
        {
            res += Math.pow((arr1[i]-arr2[i]), 2);
        }
        res = Math.sqrt(res);
        Timber.tag(TAG).d("distance: " + res);
        return (res < 0.45);
    }


    public Map<Integer, List> CalGroups(ArrayList<float[]> featuresMat)
    {
        int itemCount = featuresMat.size();
        Map<Integer, List> linkPairs = new HashMap<>();
        for (int i = 0; i < itemCount; i++) {
            List tempList = new ArrayList<>();
            float[] alpha = featuresMat.get(i);

            for (int j = 0; j < itemCount; j++) {
                if (i == j)
                    continue;
                float[] beta = featuresMat.get(j);
                if ( CalDistance(alpha, beta))
                {
                    tempList.add(j);
                }
            }
            linkPairs.put(i, tempList);
        }
        return linkPairs;
    }

    public void LoopValues(Map<Integer, List> linkPairs, int key, Set group)
    {
        Iterator<Map.Entry<Integer, List>> iter = linkPairs.entrySet().iterator();
        group.add(key);

        List listValue = linkPairs.get(key);
        if (listValue != null) {
            Iterator<Integer> nIter = listValue.iterator();

            while (nIter.hasNext()) {
                Integer newKey = nIter.next();
                if (!group.contains(newKey)) {
                    group.add(newKey);
                    LoopValues(linkPairs, newKey, group);
                }
            }
        }
        linkPairs.remove(key);
    }

    public List<Set> DivGroups(Map<Integer, List> linkPairs)
    {
        //log
//        System.out.println(linkPairs.size());
        //remove empty list
        Iterator<Map.Entry<Integer, List>> iter = linkPairs.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry<Integer, List> entry = iter.next();
            int linkKey = entry.getKey();
            List linkValue = entry.getValue();
            if(linkValue.isEmpty())
            {
//                linkPairs.remove(linkKey);
            }
//            iter.
        }
        //log
//        System.out.println(linkPairs.size());

        List groups = new ArrayList<>();
        Iterator<Map.Entry<Integer, List>> nIter = linkPairs.entrySet().iterator();
        while (nIter.hasNext()) {
            Map.Entry<Integer, List> nEntry = nIter.next();

            //init list of group
            Set group = new HashSet();
            int key = nEntry.getKey();
            group.add(key);
            LoopValues(linkPairs, key, group);
            groups.add(group);
            nIter = linkPairs.entrySet().iterator();
        }
        return groups;
    }

    public void InfoLog(Set<Integer> set)
    {
        Iterator<Integer> iter = set.iterator();
        while (iter.hasNext())
        {
            Integer temp = iter.next();
            System.out.print(temp + ", ");
        }
    }
}

