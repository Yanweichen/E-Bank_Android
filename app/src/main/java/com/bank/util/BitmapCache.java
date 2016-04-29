package com.bank.util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * @author yanwe
 * 图片缓存
 */
public class BitmapCache implements ImageCache {

//	private static LruCache<String, Bitmap> cache;
//	private int max = 10*1024*1024;
//	
//	public BitmapCache() {
//		cache = new LruCache<String, Bitmap>(max){
//			@Override
//			protected int sizeOf(String key, Bitmap value) {
//				return value.getRowBytes()*value.getHeight();
//			}
//		};
//	}
//
//	@Override
//	public Bitmap getBitmap(String arg0) {
//		return null;
//	}
//
//	@Override
//	public void putBitmap(String arg0, Bitmap arg1) {
//		
//	}
private LruCache<String, Bitmap> mCache;
	  
	  
    public BitmapCache() {
  
       if (mCache == null) {
  
           // 分配10M的缓存空间
  
           int maxSize = 10 * 1024 * 1024;
  
           mCache = new LruCache<String, Bitmap>(maxSize) {
  
              @Override
              protected int sizeOf(String key,  Bitmap value) {
  
                  return value.getRowBytes() *  value.getHeight();
  
              }
  
           };
  
       }
  
    }
  
  
    @Override
  
    public Bitmap getBitmap(String url) {
  
       return mCache.get(url);
  
    }
  
  
    @Override
  
    public void putBitmap(String url,  Bitmap bitmap) {
  
       mCache.put(url, bitmap);
  
      // Log.d(getClass().getSimpleName(),  "cacheSize/maxSize:" + mCache.size() + "/" + mCache.maxSize());
  
    }

}