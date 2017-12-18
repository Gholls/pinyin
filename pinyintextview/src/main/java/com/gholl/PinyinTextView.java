package com.gholl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.gholl.pinyin.PinyinFormat;
import com.gholl.pinyin.PinyinHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gholl on 2016/5/3.
 * Email:slgogo@foxmail.com
 */
@SuppressLint("AppCompatCustomView")
public class PinyinTextView extends TextView {
    private final String namespace = "http://www.gholl.com";
    private String text;
    private float textSize;
    private float pyTextSize;
    private float paddingLeft;
    private float paddingRight;
    private float marginLeft;
    private float marginRight;
    private int textColor;
    private Paint paint1 = new Paint();
    private Paint paint2 = new Paint();
    private float textShowWidth;
    private boolean showPinyin = false;
    private int pyTextColor;
    private int textSpace=10;
    List<Spirit> spiritList = new ArrayList<>();
    List<String> texts = new ArrayList<>();
    List<String> texts_py = new ArrayList<>();
    Map<Integer,Block> blockMap = new HashMap<>();
    OnBlockClickListener listener;
    List<BlockData> blockDatas = new ArrayList<>();
    int clickBlockId = -1;
    private int width=0;
    public boolean isShowPinyin() {
        return showPinyin;
    }

    public PinyinTextView(Context context) {
        super(context);
    }

    public PinyinTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        text = attrs.getAttributeValue(
                "http://schemas.android.com/apk/res/android", "text");
        textSize = attrs.getAttributeIntValue(namespace, "textSize", 30);
        textColor = attrs.getAttributeIntValue(namespace, "textColor",Color.BLACK);
        paddingLeft = attrs.getAttributeIntValue(namespace, "paddingLeft", 0);
        paddingRight = attrs.getAttributeIntValue(namespace, "paddingRight", 0);
        marginLeft = attrs.getAttributeIntValue(namespace, "marginLeft", 0);
        marginRight = attrs.getAttributeIntValue(namespace, "marginRight", 0);
        showPinyin = attrs.getAttributeBooleanValue(namespace, "showPinyin", false);
        pyTextColor = attrs.getAttributeIntValue(namespace, "pyTextColor", Color.RED);
        pyTextSize = attrs.getAttributeIntValue(namespace, "pyTextSize", 30);
        textSpace = attrs.getAttributeIntValue(namespace, "textSpace", 10);
        paint2.setColor(pyTextColor);
        paint2.setAntiAlias(true);
        paint2.setStrokeWidth(3);
        paint2.setTextAlign(Paint.Align.CENTER);
//        Log.e("gholl","size : " + pyTextSize  + "   "  + textSize);
        paint2.setTextSize(dip2px(context,pyTextSize));
        paint1.setTextSize(dip2px(context,textSize));
        paint1.setColor(textColor);
        paint1.setAntiAlias(true);
        paint1.setStrokeWidth(3);
        paint1.setTextAlign(Paint.Align.CENTER);
        textShowWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth() - paddingLeft - paddingRight - marginLeft - marginRight;
//        Log.e("gholl","textShowWidth : " + textShowWidth);
    }

    public PinyinTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(width==0){
            reBuildData();
            return;
        }
        for(Spirit spirit : spiritList){
            if(spirit != null){
                spirit.draw(canvas);
            }

        }
    }

    private int getID(int i){
        for(BlockData bd : blockDatas){
            for(BlockData.Pos pos :bd.getPoss()){
                if(pos.getStart()<=i&&i<=pos.getEnd()){
                    return bd.id;
                }
            }
        }
        return -1;
    }
    private void drawPinyinText(){
        int lineCount = 0;
        int textWidth = 0;
        int lineHeight = 0;
        int lastRight=textSpace;
        width=getWidth();
        for(int i=0;i<texts.size();i++){
            int id = getID(i);
            String str = texts.get(i);
            String str_py = "";
            if(showPinyin){
                str_py = texts_py.get(i);
            }
            int py_w = getTextWidth(str_py,paint2);
            int cn_w = getTextWidth(str,paint1);
            textWidth = Math.max(py_w,cn_w);
            if(i==0){
                Rect rect = drawPinyin(id,str,str_py,textWidth,0,0);
                lineHeight = rect.bottom - rect.top;
                lastRight = rect.right;
            }
            if(lastRight + textWidth >= width){
                lineCount++;
                lastRight = 0;
            }
            if(i!=0){
                lastRight = drawPinyin(id,str, str_py, Math.max(py_w,cn_w), lastRight, lineHeight * lineCount).right;
//                PinyinSpirit spirit = new PinyinSpirit();
//                spirit.rect = new Rect(x,lineHeight*lineCount,textWidth,(lineCount+1)*lineHeight);
            }

            lastRight += textSpace;

        }
        setHeight((lineCount+1)*lineHeight);
    }


    private Rect getTextRect(int width,int x,int h,Paint paint){
        paint.setStrokeWidth(3);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        Rect targetRect = new Rect(x, h, x+width, h+fontMetrics.bottom - fontMetrics.top);
        return targetRect;
    }

    private int getTextWidth(String s,Paint paint){
        int charWidth = (int)paint.measureText(s);
        return  charWidth;
    }

    private Rect drawPinyin(int id,String str,String str_py,int w,int x,int h){
        Spirit spirit_py = null;
        Spirit spirit = null;
        Rect rect = null;
        if(showPinyin){
            spirit_py = new Spirit(id,"",1,new int[]{},true);
            spirit_py.text = str_py;
            spirit_py.paint = paint2;
            spirit_py.rect = getTextRect(w, x, h, paint2);
            spiritList.add(spirit_py);
            spirit = new Spirit(id,"",1,new int[]{},false);
            spirit.text = str;
            spirit.paint = paint1;
            spirit.rect = getTextRect(w, x, spirit_py.rect.bottom, paint1);
            spiritList.add(spirit);
//        Rect rect2 = new Rect(rect.left,rect.top,rect1.right,rect1.bottom);
//        rects.add(rect2);
            rect =  new Rect(spirit_py.rect.left,spirit_py.rect.top,spirit.rect.right,spirit.rect.bottom);

        }else {
            spirit = new Spirit(id,"",1,new int[]{},false);
            spirit.text = str;
            spirit.paint = paint1;
            spirit.rect = getTextRect(w, x, h, paint1);
            spiritList.add(spirit);
            rect =  spirit.rect;
        }
        if(id>=0){
            if(blockMap.get(id) != null){
                blockMap.get(id).getSpirits().add(spirit);
                blockMap.get(id).getSpirits_py().add(spirit_py);
            }else {
                blockMap.put(id,new Block(id,spirit,spirit_py));
            }

        }

        return  rect;
    }

    public void setText(String text) {
       this.text = text;
        reBuildData();
    }

    public void setShowPinyin(boolean showPinyin) {
        this.showPinyin = showPinyin;
        reBuildData();
    }

    private void reBuildData(){
        blockMap.clear();
        spiritList.clear();
        texts.clear();
        texts_py.clear();
        if(showPinyin){
            String str_py = PinyinHelper.convertToPinyinString(text, "|", PinyinFormat.WITH_TONE_MARK);
            String[] ss_py = str_py.split("\\|");
            for(String s : ss_py){
                texts_py.add(s);
            }

        }

        char[] cs = text.toCharArray();
        for(char c : cs){
            String s = c + "";
            texts.add(s);
        }
        drawPinyinText();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()== MotionEvent.ACTION_DOWN)

            for(Map.Entry<Integer,Block> entry :blockMap.entrySet()){
                for(Spirit spirit :entry.getValue().getSpirits()){
                    if(event.getX()>=spirit.getRect().left&& event.getX() <= spirit.getRect().right && event.getY() <= spirit.getRect().bottom && event.getY()>=spirit.getRect().top ){
//                    Toast.makeText(getContext(),"rect" + b.getSpirit().getText(),Toast.LENGTH_SHORT).show();
                        if(listener != null){
                            listener.onClick(entry.getValue());
                            clickBlockId = entry.getValue().id;
                            reBuildData();
                        }
                        return true;
                    }
                }

                for(Spirit spirit :entry.getValue().getSpirits_py()){
                    if(spirit != null && event.getX()>=spirit.getRect().left&& event.getX() <= spirit.getRect().right && event.getY() <= spirit.getRect().bottom && event.getY()>=spirit.getRect().top ){
                        if(listener != null){
                            listener.onClick(entry.getValue());
                            clickBlockId = entry.getValue().id;
                            reBuildData();
                        }
                        return true;
                    }
                }
            }

        if(event.getAction()== MotionEvent.ACTION_UP){
            clickBlockId = -1;
            reBuildData();
        }
        return super.onTouchEvent(event);
    }


    public void setOnBlockClickListener(OnBlockClickListener listener){
        this.listener = listener;
    }

    public void setBlockData(List<BlockData> blockDatas,String text){
        this.blockDatas.clear();
        this.blockDatas.addAll(blockDatas);
        this.text = text;
        reBuildData();
    }
    public class BlockData{
        private int id;
        private String data;
        private List<Pos> poss = new ArrayList<>();
        public BlockData(int id,String data){
            this.id = id;
            this.data = data;
            String[] ss = data.split("\\|");
            for(String s : ss){
                String[] sss = s.split(",");
                Pos pos = new Pos();
                pos.setStart(Integer.parseInt(sss[0]));
                pos.setEnd(Integer.parseInt(sss[1]));
                poss.add(pos);
            }
        }

        public int getId() {
            return id;
        }

        public String getData() {
            return data;
        }

        public List<Pos> getPoss() {
            return poss;
        }

        class Pos{
            private int start;
            private int end;

            public int getStart() {
                return start;
            }

            public void setStart(int start) {
                this.start = start;
            }

            public int getEnd() {
                return end;
            }

            public void setEnd(int end) {
                this.end = end;
            }
        }
    }
    public class Block{
        private int id;
        private List<Spirit> spirits = new ArrayList<>();
        private List<Spirit> spirits_py = new ArrayList<>();
        Block(int id,Spirit spirit,Spirit spirit_py){
            this.id = id;
            this.spirits.add(spirit);
            this.spirits_py.add(spirit_py);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<Spirit> getSpirits() {
            return spirits;
        }

        public void setSpirits(List<Spirit> spirits) {
            this.spirits = spirits;
        }

        public List<Spirit> getSpirits_py() {
            return spirits_py;
        }

        public void setSpirits_py(List<Spirit> spirits_py) {
            this.spirits_py = spirits_py;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            for(Spirit s : spirits){
                sb.append(s.getText());
            }
            return sb.toString();
        }
    }

    public class Spirit{
        private int id;
        private Rect rect;
        private String text;
        private Paint paint;
        private boolean isPy = false;
        private int[] pos;
        Spirit(int id,String str,int Color,int[] pos,boolean isPy){
            this.id = id;
            this.isPy = isPy;

        }
        public void draw(Canvas canvas){;

//            paint.setColor(Color.CYAN);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
//            canvas.drawRect(rect, paint);
            if(id == clickBlockId && id>0){
                paint.setColor(Color.GREEN);
            } else if(id >0){
                paint.setColor(Color.parseColor("#ab825e"));
            } else {
                if(isPy){
//                    Log.e("gholl","py");
                    paint.setColor(pyTextColor);
                }else {
//                    Log.e("gholl"," not py");
                    paint.setColor(textColor);
                }
            }
            int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
//            Log.e("gholl","rect: (" + rect.left + "," + rect.top + ","+rect.right + "," + rect.bottom +")  baseline:" + baseline );
            // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
            canvas.drawText(text, rect.centerX(), baseline, paint);
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Rect getRect() {
            return rect;
        }

        public void setRect(Rect rect) {
            this.rect = rect;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isPy() {
            return isPy;
        }

        public void setPy(boolean py) {
            isPy = py;
        }
    }

    public interface OnBlockClickListener{
        public void onClick(Block block);
    }

    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
