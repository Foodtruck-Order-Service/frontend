package kr.co.fos.client.review;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;

import kr.co.fos.client.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReviewAdapter extends BaseAdapter {
    Bitmap bitmap;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Review> reviewList = new ArrayList<Review>() ;
    ImageView reviewImage;

    // ListViewAdapter의 생성자
    public ReviewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return reviewList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.review_list_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        TextView gradeTextView = (TextView)convertView.findViewById(R.id.grade);
        TextView idTextView = (TextView)convertView.findViewById(R.id.id);
        TextView contentTextView = (TextView) convertView.findViewById(R.id.content);
        TextView registDateTextView = (TextView)convertView.findViewById(R.id.registDate);
        reviewImage = (ImageView)convertView.findViewById(R.id.reviewImage);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Review review = reviewList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        gradeTextView.setText(review.getGrade());
        idTextView.setText(review.getId());
        contentTextView.setText(review.getContent());
        registDateTextView.setText(review.getRegistDate().toString());
        Glide.with(context).load("http://222.117.135.101:8090/review/photo/" + review.getNo()).into(reviewImage);

        reviewImage.setImageBitmap(bitmap);

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return reviewList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(int no, String id, String grade, String content, String registDate, String physical) {
        Review review = new Review();

        review.setNo(no);
        review.setId(id);
        review.setGrade(grade);
        review.setContent(content);
        review.setRegistDate(registDate);
        review.setPhysical(physical);

        reviewList.add(review);
    }
}
