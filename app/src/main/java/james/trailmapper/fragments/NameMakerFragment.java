package james.trailmapper.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import james.trailmapper.R;
import james.trailmapper.data.MapData;
import james.trailmapper.data.PositionData;
import james.trailmapper.utils.ImageUtils;
import james.trailmapper.views.RatioImageView;

public class NameMakerFragment extends MakerFragment {

    private static final int REQUEST_SELECT_IMAGE = 824;
    private static final int REQUEST_TAKE_PICTURE = 543;

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.imageView)
    RatioImageView imageView;

    private boolean isNameComplete;
    private boolean isImageComplete;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_name_maker, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @OnTextChanged(R.id.name)
    void setName() {
        String text = name.getText().toString();
        isNameComplete = false;

        for (MapData map : getTrailMapper().getMaps()) {
            if (text.equals(map.getName())) {
                changeComletion(false);
                name.setError(
                        getString(R.string.msg_same_name),
                        ImageUtils.getVectorDrawable(getContext(), R.drawable.ic_error, Color.WHITE)
                );
                return;
            }
        }

        for (MapData map : getTrailMapper().getOfflineMaps()) {
            if (text.equals(map.getName())) {
                changeComletion(false);
                name.setError(
                        getString(R.string.msg_same_name),
                        ImageUtils.getVectorDrawable(getContext(), R.drawable.ic_error, Color.WHITE)
                );
                return;
            }
        }

        if (getMap() != null) {
            getMap().name = text;
            isNameComplete = true;
            changeComletion(isImageComplete);
        }
    }

    @OnClick(R.id.selectImage)
    void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_SELECT_IMAGE);
    }

    @OnClick(R.id.takePicture)
    void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            File file = new File(Environment.getExternalStorageDirectory(), String.valueOf(System.currentTimeMillis()) + ".jpg");

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, REQUEST_TAKE_PICTURE);

            if (getMap() != null)
                getMap().offlineImage = file.getAbsolutePath();
        }
    }

    @Override
    public void onSelect() {
    }

    @Override
    public void onLocationChanged(PositionData position) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @Override
    public void onMapChanged(MapData map) {
    }

    @Override
    public void onMapsChanged() {
    }

    @Override
    public void onPreferenceChanged() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String path = data.getDataString();
                    try {
                        Cursor cursor = getContext().getContentResolver().query(data.getData(), null, null, null, null);
                        String documentId;
                        if (cursor != null) {
                            cursor.moveToFirst();
                            documentId = cursor.getString(0);
                            documentId = documentId.substring(documentId.lastIndexOf(":") + 1);
                            cursor.close();
                        } else break;

                        cursor = getContext().getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{documentId}, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            cursor.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    File file = new File(path);
                    if (getMap() != null && file.exists()) {
                        getMap().offlineImage = file.getAbsolutePath();
                        isImageComplete = true;
                    }
                }
                break;
            case REQUEST_TAKE_PICTURE:
                isImageComplete = resultCode == Activity.RESULT_OK;
                break;
        }

        if (getMap() != null) {
            getMap().getDrawable(getContext()).into(new SimpleTarget<Bitmap>(imageView.getWidth(), imageView.getHeight()) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    isImageComplete = true;
                    changeComletion(isNameComplete);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    Toast.makeText(getContext(), "load failed", Toast.LENGTH_SHORT).show();
                    isImageComplete = false;
                    changeComletion(false);
                }
            });
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
