package com.example.Lion_FilterSnap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Frame;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private ModelRenderable modelRenderable;
    private Texture texture;
    private boolean isAdded=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomArFragment customArFragment=(CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        ModelRenderable.builder().setSource(this,R.raw.fox_face)
                .build()
                .thenAccept(modelRenderable1 -> {
                        modelRenderable=modelRenderable1;
                modelRenderable.setShadowCaster(false);
                modelRenderable.setShadowReceiver(false);
                });

        Texture
                .builder().setSource(this,R.drawable.fox_face_mesh_texture)
                .build()
                .thenAcceptAsync(texture1 -> texture=texture1 );

        customArFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);  //This will ensure that cameraStreamRender first show that the face mesh run corrextly

        customArFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {

            if(modelRenderable==null|| texture==null)
                return;

            Frame frame=customArFragment.getArSceneView().getArFrame();

            Collection<AugmentedFace> augmentedFaces=frame.getUpdatedTrackables(AugmentedFace.class);

            //all the pictured trapped by camera
            for(AugmentedFace augmentedFace: augmentedFaces){
                if(isAdded)
                    return;

                AugmentedFaceNode augmentedFaceNode=new AugmentedFaceNode(augmentedFace);
                augmentedFaceNode.setParent(customArFragment.getArSceneView().getScene());  //adding the node
                augmentedFaceNode.setFaceRegionsRenderable(modelRenderable);
                augmentedFaceNode.setFaceMeshTexture(texture);

                isAdded=true;

            }
        });
    }

}