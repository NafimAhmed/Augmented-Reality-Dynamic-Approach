package com.example.augmentedreality2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.Sceneform;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.lang.ref.WeakReference;

public class ARpage extends AppCompatActivity implements FragmentOnAttachListener,
        BaseArFragment.OnTapArPlaneListener,
        BaseArFragment.OnSessionConfigurationListener,
        ArFragment.OnViewCreatedListener  {


    ArFragment arFragment;
    Renderable model;
    ViewRenderable viewRenderable;
    String SamplePath="https://storage.googleapis.com/ar-answers-in-search-models/static/Tiger/model.glb";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arpage);



        getSupportFragmentManager().addFragmentOnAttachListener(this);

        if(savedInstanceState==null)
        {
            if(Sceneform.isSupported(this))
            {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.arFragment, ArFragment.class,null)
                        .commit();
            }
        }

        //SamplePath= getIntent().getStringExtra("imageUri");

        loadModels();






    }


    @Override
    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {

        if (fragment.getId()==R.id.arFragment)
        {
            arFragment = (ArFragment) fragment;
            arFragment.setOnSessionConfigurationListener(this);
            arFragment.setOnViewCreatedListener(this);
            arFragment.setOnTapArPlaneListener(this);
        }

    }


    @Override
    public void onViewCreated(ArSceneView arSceneView)
    {
        arFragment.setOnViewCreatedListener(null);
        arSceneView.setFrameRateFactor(SceneView.FrameRate.FULL);

    }


    @Override
    public void onSessionConfiguration(Session session, Config config) {

        if(session.isDepthModeSupported(Config.DepthMode.AUTOMATIC))
        {
            ////////////////////////////////////////////////////////
            config.setDepthMode(Config.DepthMode.AUTOMATIC);

            ///////////////////////////////////////////////////////
        }

    }



    @Override
    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {

        if(model==null||viewRenderable==null)
        {
            Toast.makeText(this,"Loading........",Toast.LENGTH_SHORT).show();
            return;
        }

        Anchor anchor =hitResult.createAnchor();
        AnchorNode anchorNode=new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        TransformableNode model=new TransformableNode(arFragment.getTransformationSystem());
        /////////////////////////////////
        model.getScaleController().setMaxScale(0.2f);
        model.getScaleController().setMinScale(0.01f);
        //////////////////////////////////
        model.setParent(anchorNode);
        model.setRenderable(this.model).animate(true).start();
        model.select();


        Node titleNode=new Node();
        titleNode.setParent(model);
        titleNode.setEnabled(false);
        titleNode.setLocalPosition(new Vector3(0.0f,1.0f,0.0f));
        titleNode.setRenderable(viewRenderable);
        titleNode.setEnabled(true);


    }




    public void loadModels()
    {
        ///////////////////////////////////////////////////
        WeakReference<ARpage> weakActivity=new WeakReference<>(this);

        ////////////////////////////////////////////////

        ModelRenderable.builder()
                .setSource(this, Uri.parse(SamplePath))
                //.setSource(this,R.raw.bee)
                .setIsFilamentGltf(true)

                .setAsyncLoadEnabled(true)
                .build().thenAccept(model -> {
                    //////////////////////////////////////////////
                    ARpage activity = weakActivity.get();//Change Main Activity to Arpage
                    /////////////////////////////////////////////////////////
                    if (activity != null) {
                        activity.model = model;
                    }
                }) .exceptionally(throwable -> {
                    Toast.makeText(
                            this, "Unable to load model", Toast.LENGTH_LONG).show();
                    return null;
                });

        ViewRenderable.builder().setView(this,R.layout.view_model_title)
                .build()
                .thenAccept(viewRenderable -> {
                    ARpage activity = weakActivity.get();
                    if (activity != null) {
                        activity.viewRenderable = viewRenderable;
                    }
                }).exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load model", Toast.LENGTH_LONG).show();
                    return null;
                });



    }




//    @Override
//    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
//
//    }
//
//    @Override
//    public void onViewCreated(ArSceneView arSceneView) {
//
//    }
//
//    @Override
//    public void onSessionConfiguration(Session session, Config config) {
//
//    }
//
//    @Override
//    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
//
//    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//        super.onPointerCaptureChanged(hasCapture);
//    }
}