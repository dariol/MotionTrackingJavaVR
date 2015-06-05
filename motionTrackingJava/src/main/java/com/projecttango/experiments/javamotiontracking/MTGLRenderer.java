/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.projecttango.experiments.javamotiontracking;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.projecttango.tangoutils.Renderer;
import com.projecttango.tangoutils.renderables.CameraFrustum;
import com.projecttango.tangoutils.renderables.CameraFrustumAndAxis;
import com.projecttango.tangoutils.renderables.Grid;
import com.projecttango.tangoutils.renderables.Trajectory;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * OpenGL rendering class for the Motion Tracking API sample. This class managers the objects
 * visible in the OpenGL view which are the {@link CameraFrustum}, {@link CameraFrustumAndAxis},
 * {@link Trajectory}, and the {@link Grid}. These objects are implemented in the TangoUtils library
 * in the package {@link com.projecttango.tangoutils.renderables}.
 * 
 * This class receives also handles the user-selected camera view, which can be 1st person, 3rd
 * person, or top-down.
 */
public class MTGLRenderer extends Renderer implements CardboardView.StereoRenderer {//GLSurfaceView.Renderer {

    private Trajectory mTrajectory;
    private CameraFrustum mCameraFrustum;
    private CameraFrustumAndAxis mCameraFrustumAndAxis;
    private Grid mFloorGrid;
    private boolean mIsValid = false;
    private static String TAG="MTGLRenderer";


    @Override
    public void onSurfaceCreated(EGLConfig config) {

        // Set background color and enable depth testing
        GLES20.glClearColor(1f, 1f, 1f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // resetModelMatCalculator();
        mCameraFrustum = new CameraFrustum();
        mFloorGrid = new Grid();
        mCameraFrustumAndAxis = new CameraFrustumAndAxis();
        mTrajectory = new Trajectory(3);

        // Construct the initial view matrix
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setLookAtM(mViewMatrix, 0, 5f, 5f, 5f, 0f, 0f, 0f, 0f, 1f, 0f);
        mCameraFrustumAndAxis.setModelMatrix(getModelMatCalculator().getModelMatrix());
        mIsValid = true;

    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.e(TAG,"onSurfaceChanged: "+width+","+height);
        GLES20.glViewport(0, 0, width, height);
        mCameraAspect = (float) width / height;
        Matrix.perspectiveM(mProjectionMatrix, 0, CAMERA_FOV, mCameraAspect, CAMERA_NEAR, CAMERA_FAR);
    }

    public CameraFrustum getCameraFrustum() {
        return mCameraFrustum;
    }

    public CameraFrustumAndAxis getCameraFrustumAndAxis() {
        return mCameraFrustumAndAxis;
    }

    public Trajectory getTrajectory() {
        return mTrajectory;
    }
    
    public boolean isValid(){
        return mIsValid;
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
    }
    @Override
    public void onNewFrame(HeadTransform headTransform) {
        // Build the Model part of the ModelView matrix.
//        Matrix.rotateM(mModelCube, 0, TIME_DELTA, 0.5f, 0.5f, 1.0f);
        // Build the camera matrix and apply it to the ModelView.
//        Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
//        headTransform.getHeadView(mHeadView, 0);
        checkGLError("onReadyToDraw");
    }
//    @Override
//    public void onDrawFrame(GL10 gl) {
//    }
    @Override
    public void onDrawEye(Eye eye) {
        synchronized (MotionTrackingActivity.sharedLock) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            mTrajectory.draw(getViewMatrix(), mProjectionMatrix);
            mFloorGrid.draw(getViewMatrix(), mProjectionMatrix);
            mCameraFrustumAndAxis.draw(getViewMatrix(), mProjectionMatrix);
        }
/*
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        checkGLError("mColorParam");
        // Apply the eye transformation to the camera.
        Matrix.multiplyMM(mView, 0, eye.getEyeView(), 0, mCamera, 0);
        // Set the position of the light
        Matrix.multiplyMV(mLightPosInEyeSpaceVR, 0, mView, 0, LIGHT_POS_IN_WORLD_SPACE, 0);
        // Build the ModelView and ModelViewProjection matrices
        // for calculating cube position and light.
        float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);
        Matrix.multiplyMM(mModelView, 0, mView, 0, mModelCube, 0);
        Matrix.multiplyMM(mModelViewProjection, 0, perspective, 0, mModelView, 0);
*/
    }
    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }
    /*@Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged");

        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);

    }
*/
    private static void checkGLError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, label + ": glError " + error);
            throw new RuntimeException(label + ": glError " + error);
        }
    }
}

