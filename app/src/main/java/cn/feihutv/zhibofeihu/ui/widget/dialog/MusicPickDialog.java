package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.Manifest;
import android.app.DialogFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;

public class MusicPickDialog extends DialogFragment implements View.OnClickListener, DialogInterface, LoaderCallbacks<Cursor>, OnItemClickListener {
    
    private ListView mListView = null;
    private Button mPositiveBtn = null;
    private Button mNegativeBtn = null;
    private View mEmptyView = null;
    private View mLoadingView = null;
    private MusicPickAdapter mAdapter = null;
    
    private OnClickListener mOnClickListener = null;
    
    private ArrayList<MusicItem> mMusicList = new ArrayList<MusicItem>();
    

    public MusicPickDialog() {
        super();
    }

    public List<String> getCheckedMusicList() {
        List<String> list = new ArrayList<String>();
        for(MusicItem item : mMusicList) {
            if(item.checked) {
                list.add(item.musicFilePath);
            }
        }
        return list;
    }
    
    public void setOnclickListener(OnClickListener onclickListener) {
        mOnClickListener = onclickListener;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MusicPickAdapter(getActivity(), mMusicList);
    }
    
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("选择歌曲");
        View rootView = inflater.inflate(R.layout.music_pick_content_view, container, false);
        mListView = (ListView) rootView.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mNegativeBtn = (Button) rootView.findViewById(R.id.button1);
        mNegativeBtn.setOnClickListener(this);
        mPositiveBtn = (Button) rootView.findViewById(R.id.button2);
        mPositiveBtn.setOnClickListener(this);
        mEmptyView = rootView.findViewById(android.R.id.empty);
        mLoadingView = rootView.findViewById(R.id.loading_container);
        int result = PermissionChecker.checkCallingOrSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result != PackageManager.PERMISSION_GRANTED) {
            mLoadingView.setVisibility(View.GONE);
            mMusicList.clear();
            mEmptyView.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "SD卡权限拒绝，读取音乐列表失败！", Toast.LENGTH_SHORT).show();
            return rootView;
        }
        if(mMusicList.isEmpty()) {
            mMusicList.clear();
            File musicFile = new File("/sdcard/StreamPusherMusic");//优先显示测试文件夹内的歌曲
            if(musicFile.exists() && musicFile.isDirectory()){
                File[] musicFiles = musicFile.listFiles();
                for(int i = 0; musicFiles != null && i< musicFiles.length; i++){
                    MusicItem item = new MusicItem();
                    item.checked = false;
                    item.musicFilePath = musicFiles[i].getAbsolutePath();
                    item.musicTitle = musicFiles[i].getName();
                    mMusicList.add(item);
                }                
            }
            if(mMusicList.size() > 0){
                mLoadingView.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
                mListView.setVisibility(View.VISIBLE);
            } else {
                getLoaderManager().initLoader(0, null, this);
            }
        } else {
            mLoadingView.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            mListView.setVisibility(View.VISIBLE);
        }
        return rootView;
    }
    
    @Override
    public void onStart() {
        super.onStart();
    }
    
    @Override
    public void onDestroyView() {
        getLoaderManager().destroyLoader(0);
        super.onDestroyView();
    }
    

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button1:
            if(mOnClickListener != null) {
                mOnClickListener.onClick(this, DialogInterface.BUTTON_NEGATIVE);
            }
            break;
        case R.id.button2:
            if(mOnClickListener != null) {
                mOnClickListener.onClick(this, DialogInterface.BUTTON_POSITIVE);
            }
            break;

        default:
            break;
        }
        this.dismissAllowingStateLoss();
        
    }

    @Override
    public void cancel() {
        
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = new String[]{MediaStore.Audio.AudioColumns.DATA,
                                          MediaStore.Audio.AudioColumns.TITLE
                                          };
        return new CursorLoader(getActivity(),
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                MediaStore.Audio.AudioColumns.IS_MUSIC + "= ? AND (" + MediaStore.Audio.AudioColumns.DATA + " like ? OR " + MediaStore.Audio.AudioColumns.DATA + " like ?)",
                new String[]{"1", "%.mp3", "%.MP3"},
                MediaStore.Audio.Media._ID);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data == null) return;
        mLoadingView.setVisibility(View.GONE);
        mMusicList.clear();
        if(data.getCount() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            return;
        }  

        while (data.moveToNext()) {
            MusicItem item = new MusicItem();
            item.checked = false;
            item.musicFilePath = data.getString(data.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
            item.musicTitle = data.getString(data.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
            Log.e("onLoadFinished", item.musicFilePath );
//            if(item.musicFilePath.toLowerCase().contains(".mp3")) {
            mMusicList.add(item);
//            }
        }
        mAdapter.notifyDataSetChanged();
        mListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
    
    private class MusicItem {
        public String musicTitle;
        public String musicFilePath;
        public boolean checked;
    }
    
    private class MusicPickAdapter extends ArrayAdapter<MusicItem> {
        
        LayoutInflater mInflater = null;
        
        public MusicPickAdapter(Context context, List<MusicItem> items) {
            super(context, R.layout.music_pick_item, R.id.text1, items);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CheckedTextView textView;
            if (convertView == null) {
                textView = (CheckedTextView) mInflater.inflate(R.layout.music_pick_item, null);
            } else {
                textView = (CheckedTextView) convertView;
            }
            if (textView != null) {
                MusicItem item = getItem(position);
                textView.setText(item.musicTitle);
                textView.setChecked(item.checked);
            }
            return textView;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView textView = (CheckedTextView) view;
        if(position > mMusicList.size() -1) return;
        boolean checked = !textView.isChecked();
        textView.setChecked(checked);
        mMusicList.get(position).checked = checked;
        
    }

}
