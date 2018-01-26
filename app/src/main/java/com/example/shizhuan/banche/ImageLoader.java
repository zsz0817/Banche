//package com.example.shizhuan.banche;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.util.LruCache;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileDescriptor;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Created by ShiZhuan on 2018/1/25.
// */
//
//public class ImageLoader {
//    private static final String TAG = "ImageLoader";
//
//    //抽象成单例 begin
//    private static class ImageLoaderHolder {
//        private static final ImageLoader mImageLoader = new ImageLoader();
//    }
//    private static Context mContext;
//    public static ImageLoader getInstance(Context context){
//        mContext = context.getApplicationContext();
//        return ImageLoaderHolder.mImageLoader;
//    }
//    private ImageLoader() {
//        super();
//        Log.i("TAG","ImageLoader");
//        int maxMemory = (int) Runtime.getRuntime().maxMemory();
//        mCache = new LruCache<String, Bitmap>(maxMemory / 20) {
//            @Override
//            protected int sizeOf(String key, Bitmap value) {
//                return value.getByteCount();
//            }
//
//        };
//
//        //硬盘缓存 begin
//        //初始化工作
//        File cacheDir = FileUtils.getFileCache(mContext, "disk_caches");
//        if (!cacheDir.exists()) {
//            cacheDir.mkdirs();
//        }
//        try {
//            mDiskCache = DiskLruCache.open(cacheDir, 1, 1, 10 * 1024 * 1024);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //硬盘缓存 end
//
//        mSets = new HashSet<NewsImageAsyncTask>();
//    }
//    //抽象成单例 end
//
//
//    //图片缓存 start
//    private LruCache<String,Bitmap> mCache;
//
//    //硬盘缓存
//    private DiskLruCache mDiskCache;
//
//
//    public Bitmap getBitmapFromCache(String url){
//        Log.i(TAG, "getBitmapFromCache");
//        return mCache.get(url);
//    }
//    public void putBitmapToCache(String url,Bitmap bitmap){
//        Log.i(TAG, "putBitmapToCache");
//        if(null == getBitmapFromCache(url)){
//            mCache.put(url, bitmap);
//        }
//    }
//    //图片缓存 end
//
//    public void getImageByAsyncTask(ImageView imageView, String url){
//        //图片缓存 start
//        Bitmap bitmap = getBitmapFromCache(url);
//        if(null == bitmap)
//        {
//            new NewsImageAsyncTask(/*imageView,*/url).execute();
//        }
//        else{
//            imageView.setImageBitmap(bitmap);
//        }
//        //图片缓存 end
//    }
//
//    //每次加载一张图片
//    private class NewsImageAsyncTask extends AsyncTask<Void,Void,Bitmap> {
//        //private ImageView mImageView ;
//        private String mUrl;
//
//        public NewsImageAsyncTask(/*ImageView mImageView,*/String url) {
//            super();
//            //this.mImageView = mImageView;
//            mUrl = url;
//        }
//
//        @Override
//        protected Bitmap doInBackground(Void... params) {
//            //硬盘缓存
//            // 用了二级缓存 所以要改写获取图片的流程：
//         /*Bitmap result = getBitmapByUrl(mUrl);
//         Log.i(TAG, "isCancelled:"+isCancelled());
//         //内存缓存 start
//         putBitmapToCache(mUrl, result);
//         //内存缓存end*/
//            return getBitmapAndSaveToCacheByUrl(mUrl);
//        }
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            super.onPostExecute(result);
//            //设置图片 防止错位
//         /*if(mUrl.equals(mImageView.getTag())){
//            mImageView.setImageBitmap(result);
//         }*/
//            //滑动优化
//            ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);
//            if(imageView!=null && null != result)
//            {
//                imageView.setImageBitmap(result);
//            }
//
//            mSets.remove(this);
//            //滑动优化
//
//
//
//        }
//
//
//
//    }
//
//    public Bitmap getBitmapByUrl(String string) {
//        InputStream is1=null/*,is2 = null*/;
//        Bitmap result = null;
//        try {
//            HttpURLConnection conn = (HttpURLConnection) new URL(string).openConnection();
//            is1 = new BufferedInputStream(conn.getInputStream());
//            //优化图片加载 begin
//
//         /*ByteArrayOutputStream out = new ByteArrayOutputStream();
//         ImageUtils.copy(is1,out);
//         is2 = new ByteArrayInputStream(out.toByteArray());*/
//
//            result = BitmapFactory.decodeStream(is1);
//            //result  = ImageUtils.decodeSuitableBitmap(is,60,60);
//            //result = ImageUtils.decodeSuitableBitmap(string,1,1);
//            //Log.i(TAG,"resutl size:"+result.getByteCount());
//
//            //result = ImageUtils.decodeSampledBitmapFromInputStream(is1,is2,1,1);
//            Log.i(TAG,"resutl size:"+(result !=null?result.getByteCount():"null"));
//
//         /*if (result==null){
//            Log.i(TAG,"result==null");
//         }*/
//            //Log.i(TAG,"result="+result);
//            //优化图片加载 end
//
//
//            conn.disconnect();
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }finally{
//            try {
//                if(is1!=null){
//                    is1.close();
//                }
//            /*if(is2!=null){
//               is2.close();
//            }*/
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }
//    //滚动时的优化 begin
//    private static Set<NewsImageAsyncTask> mSets ;
//    private ListView mListView;
//
//    public ListView getmListView() {
//        return mListView;
//    }
//
//    public void setmListView(ListView mListView) {
//        this.mListView = mListView;
//    }
//
//    public void loadImages(int mStart, int mEnd) {
//        Log.i(TAG, "loadImages:" + mStart+"-"+mEnd);
//        for (int i = mStart; i < mEnd; i++) {
//            Log.i(TAG, "loadImages:"+i);
//            String url = MainActivity.urls.get(i);
//            Bitmap bm = getBitmapFromCache(url);
//            ImageView mImageView = (ImageView)mListView.findViewWithTag(url);
//            if(null!=bm && null!= mImageView){
//
//                mImageView.setImageBitmap(bm);
//            }
//            else{//重新下载
//                NewsImageAsyncTask task = new NewsImageAsyncTask(url);
//                mSets.add(task);
//                task.execute();
//            }
//        }
//    }
//
//    public void cancelAllTasks() {
//        if(mSets!=null){
//            for (NewsImageAsyncTask task : mSets) {
//                task.cancel(false);
//            }
//        }
//    }
//
//    //看缓存中是否有 有则设置 没有则默认图片不管
//    public Bitmap setDefaultBitmap(ImageView pic, String newsPicResUrl) {
//        Bitmap bm = getBitmapFromCache(newsPicResUrl);
//        if(null!=bm){
//            pic.setImageBitmap(bm);
//        }
//        return bm;
//    }
//
//    //滚动时的优化 end
//
//
//
//    //硬盘缓存 加载图片方法(不返回bitmap，为了通用，不仅仅保存图片) 并写入硬缓存 begin
//    private static boolean getBitmapUrlToStream(String urlString, OutputStream outputStream) {
//        HttpURLConnection urlConnection = null;
//        BufferedOutputStream out = null;
//        BufferedInputStream in = null;
//        try {
//            final URL url = new URL(urlString);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
//            out = new BufferedOutputStream(outputStream, 8 * 1024);
//            int b;
//            while ((b = in.read()) != -1) {
//                out.write(b);
//            }
//            return true;
//        } catch (final IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//            } catch (final IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//    //硬盘缓存 加载图片方法 end
//
//    //硬盘缓存  下载图片 存入缓存（SD ,内存都存入）方法 begin
//
//    @Nullable
//    private Bitmap getBitmapAndSaveToCacheByUrl(String mUrl) {
//        String key = FileUtils.toMD5String(mUrl);
//        FileDescriptor fileDescriptor = null;
//        FileInputStream fileInputStream = null;
//        DiskLruCache.Snapshot snapShot = null;
//        try {
//            //尝试获取硬缓存里的数据
//            snapShot = mDiskCache.get(key);
//            //如果没有缓存
//            if (snapShot == null) {
//                //尝试读从网络取并写入
//                DiskLruCache.Editor editor = mDiskCache.edit(key);
//                if (editor != null) {
//                    OutputStream outputStream = editor.newOutputStream(0);
//                    if (getBitmapUrlToStream(mUrl, outputStream)) {
//                        editor.commit();
//                    } else {
//                        editor.abort();
//                    }
//                }
//                //再次读取
//                snapShot = mDiskCache.get(key);
//            }
//            if (snapShot != null) {
//                fileInputStream = (FileInputStream) snapShot.getInputStream(0);
//                fileDescriptor = fileInputStream.getFD();
//            }
//            //利用读取的硬盘数据，生成bitmap
//            Bitmap bitmap = null;
//            if (fileDescriptor != null) {
//                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
//            }
//            //利用bitmap 存入内存缓存
//            if (bitmap != null) {
//                putBitmapToCache(mUrl, bitmap);
//            }
//            //返回bitmap
//            return bitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fileDescriptor == null && fileInputStream != null) {
//                try {
//                    fileInputStream.close();
//                } catch (IOException e) {
//                }
//            }
//        }
//        return null;
//    }
//    //end
//
//
//    //加载大图
//
//    public void loadBigBitmap(String url, ImageView imageView, ProgressBar mProgressBar){
//        if(imageView!=null && null!= url){
//            new BigImageAsyncTask(imageView,mProgressBar).execute(url);
//        }
//    }
//
//    private class BigImageAsyncTask extends AsyncTask<String,Void,Bitmap> {
//        private ImageView mImageView;
//        private ProgressBar mProgressBar;
//        public BigImageAsyncTask(ImageView imageView, ProgressBar progressBar) {
//            mImageView = imageView;
//            mProgressBar = progressBar;
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            return getBitmapByUrl(params[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            if(bitmap!=null)
//                mImageView.setImageBitmap(bitmap);
//            if(null!=mProgressBar){
//                mProgressBar.setVisibility(View.GONE);
//            }
//        }
//    }
//}
