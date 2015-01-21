package com.example.x;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fragmentC extends Fragment{
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle SavedInstances) {
		View view = inflater.inflate(R.layout.list_view, container,
				false);
		return view;
	}
	}