# Face verify
This is an Android application with face recognition and face clustering. The original plan is to develop a smart album based on face classification, now has been shelved.

## Before using
Before using, I would like to introduce the operating mechanism of the application, which should help you to use or modify the program.

### Algorithm
* Face recognition: Residual Neural Network
* Clustering: Hierarchical Clustering Algorithm

### Accuracy and Performance
* Assuming face detection accuracy is 100%, the correct rate is 0.97, the recall rate is 0.96.
* Performance have a very large relationship with hardware

image size <= 1800x1800

|  |cpu|face detection|face verify|
|--|:--:|:--:|:--:|
|Snapdragon616|1.2GHZ| 2s |6-7s|
|Snapdragon652|1.8GHZ| 0.5s|1-2s|

 ps: For reasons of performance differences, refer to #Others.

## How to use
As I am very busy lately, and not good at android programming (especially UI), so the actual page is not currently visible on the layout, but you can still get feedback from the message log.</br>
First, you need to empty your album, preferably all the pictures in your phone. </br>
Then, import the face image you want to test, note that the image must include the face, and the face can not be too small. </br>
Finally run the program, wait a while, you will see in the Android Studio log similar to the following information:

    I/verification.h:80 load_face_landmark from : /storage/emulated/0/shape_predictor_68_face_landmarks.dat
    I/verification.h:86 load_face_verify from : /storage/emulated/0/dlib_face_recognition_resnet_model_v1.dat
    I/jni_face_ver.cpp:135 jniVerify
    I/verification.h:107 verify image_path/storage/emulated/0/DCIM/Camera/lyj_6.jpg
    I/verification.h:117 image size:1280 960
    I/verification.h:132 time test: detect start.
    I/verification.h:134 Dlib HOG face det size : 1
    I/verification.h:147 Dlib face_descriptors size : 1
    I/verification.h:148 Dlib face_descriptors[0].nr : 128  Dlib face_descriptors[0].nc: 1
    I/jni_face_ver.cpp:93 getVerifyResult in
    I/jni_face_ver.cpp:135 jniVerify
    I/verification.h:107 verify image_path/storage/emulated/0/DCIM/Camera/lyj_5.jpg
    I/verification.h:117 image size:1280 1280
    I/verification.h:132 time test: detect start.
    I/verification.h:134 Dlib HOG face det size : 1
    I/verification.h:147 Dlib face_descriptors size : 1
    ......
    insert image vec tempVec = /storage/emulated/0/DCIM/Camera/lyj_6.jpg
    imageVecText:-0.024751978,0.024874505,-0.013718821,-0.028619522,-0.09627778,-0.0052718264,-0.09067893,-0.04976383,0.06042842,-0.13504581,0.14587808,-0.097083785,-0.17904182,-0.079852544,-0.033664394,0.19204171,-0.15419398,-0.18213901,-0.022971295,-0.046877004,0.015238022,0.008480772,-0.0069706016,0.02609567,-0.15924351,-0.33667544,-0.0842147,-0.09047962,0.0028145332,-0.07761911,-0.05197698,0.12094144,-0.08431834,0.066665806,0.09168729,0.060553893,0.03974204,-0.043633655,0.23516662,0.023121234,-0.24345173,-0.019863492,0.12237895,0.24899954,0.18819396,8.997456E-4,-0.0076567214,-0.09647579,0.182668,-0.1509597,0.0085309595,0.20168272,0.050060004,0.095187746,0.017229209,-0.04954455,0.075121254,0.12472357,-0.10816748,-0.004579893,0.03797207,-0.03161928,-0.008323679,-0.057388052,0.22502124,0.025031103,-0.10274051,-0.18190168,0.14093028,-0.17409733,-0.12056996,0.024919994,-0.16519178,-0.16235322,-0.24118903,-0.015565047,0.33244506,0.14860749,-0.17130354,0.13421851,0.02611238,-0.09063421,0.14841715,0.17515385,-0.06889065,0.017389476,-0.032926377,-0.0070704296,0.29050073,-0.04428906,0.020326203,0.17312077,-0.008399133,0.11601181,0.04945643,-0.01870037,-0.062723845,0.0273058,-0.1307312,-0.030138377,0.029176394,-0.069304466,0.011285176,0.14039525,-0.16073291,0.14106114,-0.0067199203,0.044936907,0.027418818,0.09416451,-0.06430113,-0.084608324,0.115929455,-0.16218446,0.14299646,0.15306793,0.1023902,0.0600553,0.15778205,0.10151707,0.0031617861,-0.009695006,-0.1983394,-0.011435228,0.09858083,-0.04802562,0.021209128,-0.006057208,
    ......
    group: 0
    D/dlib: /storage/emulated/0/DCIM/Camera/lyj_6.jpg
    D/dlib: /storage/emulated/0/DCIM/Camera/lyj_3.jpg
    D/dlib: /storage/emulated/0/DCIM/Camera/lyj_0.jpg
    group: 3
    D/dlib: /storage/emulated/0/DCIM/Camera/lyj_2.jpg
    D/dlib: /storage/emulated/0/DCIM/Camera/lyj_1.jpg
    
## Contributors wanted

*The most emergency*
* Optimize performance
* Complete UI

*And others*
* Non-face pictures are not processed.
* Multi-face images are not processed.
* Redundant code is not cleaned up

## Others
*reference*
1. [dlib](https://github.com/davisking/dlib)
2. [dlib_android](https://github.com/tzutalin/dlib-android)

About jni and os folder I will update soon.

*performance*
* app will run faster in platform supported NEON.
* resize image can reduce face decetion time but no help to face verify.