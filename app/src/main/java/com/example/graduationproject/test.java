package com.example.graduationproject;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.graduationproject.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class test {
//    private void uploadImage1(MultipartBody.Part body, List<MultipartBody.Part> resourceBody) {
//        Uri uri = imagesList.get("image" + 1);
//        File file = null;
//        try {
//            file = FileUtil.from(context, uri);
//            RequestBody requestFile =
//                    RequestBody.create(MediaType.parse("multipart/form-data")
//                            , file);
//            body = MultipartBody.Part.createFormData(
//                    "assets[" + 1 + "]", file.getName(), requestFile);
//            resourceBody.add(body);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    private void uploadImage2(MultipartBody.Part body, List<MultipartBody.Part> resourceBody) {
//        Uri uri = imagesList.get("image" + 2);
//        File file = null;
//        try {
//            file = FileUtil.from(context, uri);
//            RequestBody requestFile =
//                    RequestBody.create(MediaType.parse("multipart/form-data")
//                            , file);
//            body = MultipartBody.Part.createFormData(
//                    "assets[" + 2 + "]", file.getName(), requestFile);
//            resourceBody.add(body);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    private void uploadImage3(MultipartBody.Part body, List<MultipartBody.Part> resourceBody) {
//        Uri uri = imagesList.get("image" + 3);
//        File file = null;
//        try {
//            file = FileUtil.from(context, uri);
//            RequestBody requestFile =
//                    RequestBody.create(MediaType.parse("multipart/form-data")
//                            , file);
//            body = MultipartBody.Part.createFormData(
//                    "assets[" + 3 + "]", file.getName(), requestFile);
//            resourceBody.add(body);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    private void uploadImage(MultipartBody.Part body, List<MultipartBody.Part> resourceBody,int init) {
//        Log.e("loream_edit_size",editImages.size()+"");
//        Log.e("loream_images_list",imagesList.get("image" + 1)+"");
//        Log.e("loream_images_list",imagesList.get("image" + 2)+"");
//        Log.e("loream_images_list",imagesList.get("image" + 3)+"");
//        Log.e("loream_initt",init+"");
//        for (int i = 0; i <imagesList.size() ; i++) {
//            init++;
//            Uri uri = imagesList.get("image" + init);
//            File file = null;
//            try {
//                file = FileUtil.from(context, uri);
//                RequestBody requestFile =
//                        RequestBody.create(MediaType.parse("multipart/form-data")
//                                , file);
//                body = MultipartBody.Part.createFormData(
//                        "assets[" + init + "]", file.getName(), requestFile);
//                resourceBody.add(body);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    private void setEditImage() {
//        switch (editImages.size()) {
//            case 3:
//                Glide.with(context).load(editImages.get(0)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image1);
//                Glide.with(context).load(editImages.get(1)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image2);
//                Glide.with(context).load(editImages.get(2)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image3);
//                binding.imagePost2.setVisibility(View.VISIBLE);
//                binding.imagePost3.setVisibility(View.VISIBLE);
//                binding.delete1.setVisibility(View.VISIBLE);
//                binding.delete2.setVisibility(View.VISIBLE);
//                binding.delete3.setVisibility(View.VISIBLE);
//                break;
//            case 2:
//                Glide.with(context).load(editImages.get(0)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image1);
//                Glide.with(context).load(editImages.get(1)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image2);
//                Glide.with(context).load(R.drawable.add).into(binding.image3);
//                binding.imagePost3.setVisibility(View.VISIBLE);
//                binding.imagePost2.setVisibility(View.VISIBLE);
//                binding.delete1.setVisibility(View.VISIBLE);
//                binding.delete2.setVisibility(View.VISIBLE);
//                break;
//            case 1:
//                Glide.with(context).load(editImages.get(0)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image1);
//                binding.imagePost2.setVisibility(View.VISIBLE);
//                binding.delete1.setVisibility(View.VISIBLE);
//                if (imagesList.size() == 0) {
//                    Glide.with(context).load(R.drawable.add).into(binding.image2);
//                    Glide.with(context).load(R.drawable.add).into(binding.image3);
//                    binding.imagePost3.setVisibility(View.GONE);
//                } else if (imagesList.size() == 1) {
//                    Glide.with(context).load(imagesList.get(IMAGE3)).into(binding.image2);
//                    Glide.with(context).load(R.drawable.add).into(binding.image3);
//                    imagesList.put(IMAGE2, imagesList.get(IMAGE3));
//                    imagesList.put(IMAGE3, null);
//                }
//
//                break;
//            case 0:
//                if (imagesList.size() == 1) {
//                    Glide.with(context).load(imagesList.get(IMAGE2)).into(binding.image1);
//                    Glide.with(context).load(R.drawable.add).into(binding.image2);
//                    Glide.with(context).load(R.drawable.add).into(binding.image3);
//                    binding.imagePost3.setVisibility(View.GONE);
//                    imagesList.put(IMAGE1, imagesList.get(IMAGE2));
//                    imagesList.put(IMAGE2, null);
//                } else if (imagesList.size() == 2) {
//                    Glide.with(context).load(imagesList.get(IMAGE2)).into(binding.image1);
//                    Glide.with(context).load(imagesList.get(IMAGE3)).into(binding.image2);
//                    binding.imagePost3.setVisibility(View.VISIBLE);
//                    Glide.with(context).load(R.drawable.add).into(binding.image3);
//                    imagesList.put(IMAGE1, imagesList.get(IMAGE2));
//                    imagesList.put(IMAGE2, imagesList.get(IMAGE3));
//                    imagesList.put(IMAGE3, null);
//
//                } else {
//                    Glide.with(context).load(imagesList.get(IMAGE1)).into(binding.image1);
//                    Glide.with(context).load(imagesList.get(IMAGE2)).into(binding.image2);
//                    Glide.with(context).load(imagesList.get(IMAGE3)).into(binding.image3);
//
//                }
//                break;
//        }
//    }
//
//    private void setEditImagesAction(List<String> imageName) {
//        binding.imagePost1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (editImages.size() == 0) {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    someActivityResultLauncher.launch(intent);
//                    imageNum = 1;
//                } else
//                    createDialogConfirmDelete(0);
//            }
//        });
//        binding.imagePost2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (editImages.size() < 2) {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    someActivityResultLauncher.launch(intent);
//                    imageNum = 2;
//                } else
//                    createDialogConfirmDelete(1);
//            }
//        });
//        binding.imagePost3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (editImages.size() < 3) {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    someActivityResultLauncher.launch(intent);
//                    imageNum = 3;
//                } else
//                    createDialogConfirmDelete(2);
//            }
//        });
//
//    }
//
//    private void createDialogConfirmDelete(int position) {
//
//
//        if (editImages.size() == 1 && imagesList.size() == 0) {
//            pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
//            pDialog.getProgressHelper().setBarColor(Color.parseColor("#E60F5DAB"));
//            pDialog.setTitleText("you cannot do this!");
//            pDialog.setCancelable(true);
//            pDialog.show();
//        } else {
//            pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
//            pDialog.getProgressHelper().setBarColor(Color.parseColor("#E60F5DAB"));
//            pDialog.setTitleText("you will lose this image if you continue ");
//            pDialog.setCancelable(true);
//            pDialog.setConfirmButton("sure", new SweetAlertDialog.OnSweetClickListener() {
//                @Override
//                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                    String finalImageName = editImages.get(position).replace("http://54.209.160.242/storage/", "");
//                    deleteImageRequest(finalImageName, position);
//
//                    setEditImage();
//                }
//            });
//            pDialog.show();
//        }
//
//    }
//
//    int editImagesSize = editImages.size(), addImagesSize = imagesList.size();
//        if (imagesList.size() > 0) {
//        if (editImagesSize == 0) {
//            if (addImagesSize == 1) {
//                uploadImage1(body, resourceBody);
//            } else {
//                uploadImage(body, resourceBody, 0);
//            }
//
//        } else if (editImagesSize == 1) {
//            if (addImagesSize == 1) {
//                uploadImage2(body, resourceBody);
//            } else {
//                uploadImage(body, resourceBody, 1);
//            }
//
//        } else if (editImagesSize == 2) {
//            if (addImagesSize == 1) {
//                uploadImage3(body, resourceBody);
//            } else {
//                uploadImage(body, resourceBody, 2);
//            }
//        }
//    }
}
