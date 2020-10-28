package com.example.httpdemo;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Client {

	public static void main(String[] args) throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request req = new Request.Builder()
				.get()
				.url("http://localhost:8801")
				.build();
		Response resp = client.newCall(req).execute();
		System.out.println(resp.body().string());
	}
}
