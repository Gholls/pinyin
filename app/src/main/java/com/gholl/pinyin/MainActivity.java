 package com.gholl.pinyin;

 import android.app.AlertDialog;
 import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
 import android.view.View;
 import android.widget.AdapterView;
 import android.widget.ListView;
 import android.widget.Toast;

 import com.gholl.PinyinTextView;

 import java.util.ArrayList;
 import java.util.List;

 public class MainActivity extends AppCompatActivity {
     PinyinTextView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_lesson_detail);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("sssssssssssss");
        alertDialog.create();
        alertDialog.show();
        view = (PinyinTextView)findViewById(R.id.view);
//        final EditText editText = (EditText)findViewById(R.id.editText);

        view.setOnBlockClickListener(new PinyinTextView.OnBlockClickListener() {
            @Override
            public void onClick(PinyinTextView.Block block) {
                Toast.makeText(MainActivity.this, "click text:" + block.toString(), Toast.LENGTH_SHORT).show();
            }
        });
//        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                view.setText(editText.getText().toString());
//            }
//        });
//
//        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                view.setShowPinyin(!view.isShowPinyin());
//            }
//        });
//        String str = "{\"success\":true,\"error\":\"\",\"list\":[]}";
//        if(str.contains("\"list\":[]")){
//            str = str.replaceFirst("\\{","{\"emptyList\":true,");
//            Log.e("gholl","包含空list  " + str);
//        }else{
//            Log.e("gholl","不包含空list");
//        }
        ListView listView = (ListView)findViewById(R.id.listView);
        final List<Example> examples = new ArrayList<>();
        for (int i=0;i<10;i++){
            Example example = new Example();
            example.setName("多半儿");
            example.setAttribute("[duobanr] adv.(=大多)，mostly");
            example.setExampleText("来北京旅游的人多半儿会去长城");
            examples.add(example);
        }
        final LessonDeatilAdapter adapter = new LessonDeatilAdapter(this,examples);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                examples.get(position).setShowPy(!examples.get(position).isShowPy());
                adapter.notifyDataSetChanged();
            }
        });

    }

     @Override
     protected void onResume() {
         super.onResume();
         List<PinyinTextView.BlockData> list = new ArrayList<>();
         PinyinTextView.BlockData d = view.new BlockData(1,"0,2|4,5|8,9");
         list.add(d);
         view.setBlockData(list,"在中国话里头有个词儿叫面子英文里没有一个完全相应的词儿");
     }

     String str = "{\"success\":true,\"error\":\"\",\"list\":[]}";
}
