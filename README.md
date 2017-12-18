# pinyin
汉字转拼音
 ![](https://github.com/Gholls/pinyin/blob/master/img/1.png) 




# 用法

添加依赖

配置gradle：
在Project root的build.gradle中添加：

allprojects {

    repositories {
    
        ...
        maven { url 'http://maven.gholl.com/nexus/content/repositories/android-tools/' }
        
    }
    
}

在app 中添加：

单独使用中文转拼音的jar包

dependencies {

    compile 'com.gholl.tools:GhollPy4Android:1.0.0' 
    
}

使用拼音展示控件（包含jar包）

dependencies {

    compile 'com.gholl.tools:PinyinTextView:1.0.1'
    
}
