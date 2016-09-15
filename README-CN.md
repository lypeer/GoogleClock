#GoogleClock

对于Google I/O2016上那个炫酷的时钟的模拟。
库中几乎所有的动画和图像都是基于SVG实现的，所以这个库也许是一个非常好的学习SVG在Android中的使用的素材。

##show

![GoogleClock](http://ac-cnyv47la.clouddn.com/ac980baf7fcca42e.gif)

##Installation

将下述代码写入你的build.gradle中：
```java
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
}
```
```java
dependencies {
	        compile 'com.github.lypeer:GoogleClock:1.0.0'
}
```

##Usage

###Simple Usage :

直接将 GoogleClock 加入你的 activity 的 xml 文件中 :

```java
<com.lypeer.googleioclock.GoogleClock
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
```

然后时钟就出现了。

###Complex Usage

你也可以在 xml 中对 GoogleClock 做一些配置：

```java
<com.lypeer.googleioclock.GoogleClock
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:lyClockWidth="32dp"
        app:lyHourWidth="32dp"
        app:lyMinWidth="32dp"
        app:lySecWidth="32dp"
        app:lyDividerHorizontal="32dp"
        app:lyDividerVertical="32dp"
        app:lyTheme="@style/LyTheme3"
        />
```
下表是你能进行的配置的列表：

|属性|数据类型|d默认值| 描述|
|---| ---| ---|---|
|lyClockWidth|dimen|0dp|时钟中的数字的尺寸，但是它的优先级比下面三个属性要小。|
|lyHourWidth|dimen|48dp|时钟中小时部分的尺寸。|
|lyMinWidth|dimen|48dp|时钟中分钟部分的尺寸。|
|lySecWidth|dimen|32dp|时钟中秒部分的尺寸。|
|lyDividerHorizontal|dimen|4dp|小时（或者分钟或者秒）部分的两个数字之间的分隔距离。|
|lyDividerVertical|dimen|16dp|行距（小时部分和分钟部分在同一行，秒部分在另一行）。|
|lyTheme|reference|LyTheme|自定义的theme。|

你可以自定义 theme 来改变时钟中更多的属性，像下面这样：

```java
<style name="LyTheme2" parent="LyTheme">
        <item name="lyColor0">@color/colorRed</item>
        <item name="lyColor1">@color/colorYellow</item>
        <item name="lyColor2">@color/colorGreen</item>
        <item name="lyColor3">@color/colorBlue</item>
        <item name="lyColorColon">@color/colorDarkBlue</item>
        <item name="lyThickness">@integer/lyThickness_4</item>
        <item name="lyColonThickness">@integer/lyThickness_1</item>
</style>
```
推荐奖自定义的的 theme 的 parent 设置成 LyTheme ，否则的话你将需要在你的自定义 theme 中写入 LyTheme ，所含的所有属性，而不是只写入你希望修改的属性。LyTheme 中含有如下属性：

|属性|数据类型|默认值|描述|
|---|---|---|---|
|lyColor0|color|@color/colorRed|时钟的第一个颜色。|
|lyColor1|color|@color/colorYellow|时钟的第二个颜色。|
|lyColor2|color|@color/colorGreen|时钟的第三个颜色。|
|lyColor3|color|@color/colorBlue|时钟的第四个颜色。|
|lyColorColon|color|@color/colorDarkBlue|冒号的颜色。|
|lyThickness|integer|4|数字的粗度。|
|lyColonThickness|integer|1|冒号的粗度。|

用法差不多就是这么多了，更多的你可以在demo中看。

##依赖的库

 - org.greenrobot:eventbus:3.0.0 

##开源协议
 ```
  Copyright 2014-2016 lypeer.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```



