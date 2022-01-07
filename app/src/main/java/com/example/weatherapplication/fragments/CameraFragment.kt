package com.example.weatherapplication.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.weatherapplication.R


class CameraFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        val capture = view.findViewById<Button>(R.id.capture)
        val upload = view.findViewById<Button>(R.id.upload)

        // Capture button
        capture.setOnClickListener {
            if (checkSelfPermission(requireContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            {
                val permissions = arrayOf(Manifest.permission.CAMERA)
                requestPermissions(permissions, PERMISSION_CODE_CAMERA)
            }
            else { takePhoto() }
        }

        //Upload button
        upload.setOnClickListener {

            if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
            {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE_UPLOAD)
            }
            else { uploadImageGallery() }
        }
        return view
    }

    private fun takePhoto()
    {
        val intent = Intent (MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun uploadImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        val imageView = view?.findViewById<ImageView>(R.id.image)


        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = data?.extras?.get("data") as Bitmap
            imageView?.setImageBitmap(takenImage)
        }


        else if (requestCode == IMAGE_PICK_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            imageView?.setImageURI(data?.data)
        }


        else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE_UPLOAD ->
            {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    uploadImageGallery()

                else
                    Toast.makeText(context,"Permission denied for galery", Toast.LENGTH_SHORT).show()
            }

            PERMISSION_CODE_CAMERA ->
            {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    takePhoto()

                else
                    Toast.makeText(context,"Permission denied for camera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE = 123
        private const val IMAGE_PICK_CODE = 100
        private const val PERMISSION_CODE_UPLOAD = 1000
        private const val PERMISSION_CODE_CAMERA = 1123
    }


}