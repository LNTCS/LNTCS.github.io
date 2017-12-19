---
layout: post
title: 'DataBinding 을 쓰자'
date: 2017-12-18
author: LNTCS
tags: 안드소전
---
# 데이터 바인딩?
안드로이드 개발에 있어서 xml을 열심히 작성하고, java 혹은 kotlin 에서 뷰를 불러오고 데이터를 수정하고 하는 작업은 불편함을 야기하게 된다.<br>
데이터 바인딩을 이용하면 뷰를 작성한 뒤 내부적으로 들어가는 데이터들을 xml 내부에서 처리하는 방식으로 코딩을 하다보니 조금 더 직관적인 코드 구성을 이룰 수 있다.

### build.gradle (Module: app)
```gradle
apply plugin: 'kotlin-kapt'

android {
    ....
    dataBinding {
        enabled = true
    }
}

dependencies {
    kapt 'com.android.databinding:compiler:3.0.1' // gradle 버전
	....
}
```
물론 서드파티 라이브러리가 아닌 안드로이드에 있는 공식 라이브러리? 방식? 이다 보니 간단히 추가가 가능하다.<br>
다만 코틀린의 경우 몇몇 꼬이는 경우가 종종 있기에 kapt구문을 추가 해준다.<br><br>

이로써 데이터 바인딩의 사용 준비는 끝이고, 바로 사용에 들어가자면

우선 기존의 레이아웃 파일의 형태는 이러하다.
#### activity_main.xml
```xml
<LinearLayout
	xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">
	<TextView
		android:layout_width="match_parent"
        android:layout_height="wrap_content"
		android:text="name"/>
	<TextView
		android:layout_width="match_parent"
        android:layout_height="wrap_content"
		android:text="email"/>
	<TextView
		android:layout_width="match_parent"
        android:layout_height="wrap_content"
		android:text="count"/>
</LinearLayout>
```

그리고 각 텍스트뷰에 데이터를 넣을 변수를 위해 클래스를 생성해준다
### User.kt
```kotlin
class User(var name: String, var email: String, var count: Int)
```
이후 위의 `activity_main.xml` 파일을 바인딩을 할 수 있는 형태로 수정해준다.<br>
수정법은 `activity_main.xml` 전체를 `<layout>`으로 묶어주고, `<data>` 를 이용하여 변수를 설정한다.

#### activity_main.xml
```xml
<layout
	xmlns:android="http://schemas.android.com/apk/res/android">
	
	<data>
    	<variable
        	name="user"
			type="com.example.User" /> 
			<!-- User 클래스의 패키지 -->
	</data>
	
	<LinearLayout
			android:layout_width="match_parent"
	        android:layout_height="match_parent"
	        android:orientation="vertical">
		<TextView
			android:layout_width="match_parent"
	        android:layout_height="wrap_content"
			android:text="name"/>
		<TextView
			android:layout_width="match_parent"
	        android:layout_height="wrap_content"
			android:text="email"/>
		<TextView
			android:layout_width="match_parent"
	        android:layout_height="wrap_content"
			android:text="count"/>
	</LinearLayout>
</layout>
```
이후 `<variable>` 안의 `name`이 변수명이 되어 사용이 가능하다.

`android:text="@{user.name}"` 처럼 @{} 안에서 코드를 작성하듯이 사용하면 된다.<br><br>
마지막까지 작성을 한다면
#### activity_main.xml
```xml
<layout
	xmlns:android="http://schemas.android.com/apk/res/android">
	
	<data>
    	<variable
        	name="user"
			type="com.example.User" /> 
			<!-- User 클래스의 패키지 -->
	</data>
	
	<LinearLayout
			android:layout_width="match_parent"
	        android:layout_height="match_parent"
	        android:orientation="vertical">
		<TextView
			android:layout_width="match_parent"
	        android:layout_height="wrap_content"
			android:text="@{user.name}"/>
		<TextView
			android:layout_width="match_parent"
	        android:layout_height="wrap_content"
			android:text="@{user.mail}"/>
		<TextView
			android:layout_width="match_parent"
	        android:layout_height="wrap_content"
			android:text="@{user.count + ``}"/>
	</LinearLayout>
</layout>
```
이후 데이터를 `MainActivity.kt` 에서 넣어주면 된다.
#### MainActivity.kt
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
		var binding = 
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
		/*
		ActivityMainBinding 의 경우 데이터 바인딩을 세팅한 xml 파일의 이름 뒤에 Binding을 붙이는 방식이다.
		
		기존의 setContentView() 대신 DataBindingUtil의 setContentView 기능을 이용
		*/
		
		//  binding 에 변수를 넣어준다.
		binding.setVariable(BR.user, User("이창선", "LNTCS.Dev@gmail.com", 0))
		(this, R.layout.activity_main)
		/*
		BR.user 는 BR 이후 <variable> 안에 name 으로 넣어준 변수의 이름을 뜻하고
        BR.user 의 경우 User형으로 변수가 설정되있으므로 User를 새로 만들어 넣어줬다.
		*/
	}
}
```
로 할 경우 따로 xml 에서 텍스트뷰를 불러와서 데이터를 넣지 않아도 자동적으로 바인딩이 되어 `User("이창선", "LNTCS.Dev@gmail.com", 0)` 의 값이 텍스트뷰들에 다 들어가게 된다.

### CustomBindingAdapter
일단 간단히 색상코드를 받아서 배경색을 바꾸는 방식을 구현하자면
#### User.kt
```kotlin
class User(var name: String, var email: String, var count: Int, var color: String){
// 생성자에 color 변수 추가

    object CustomBindingAdapter{
		// 이용할 attribute 이름
        @BindingAdapter("android:background") 
        @JvmStatic
        fun setBackgroundColor(layout: TextView?, colorCode: String){
		// 여기서 TextView는 적용시킬 뷰가 TextView이기 때문이다.
            layout?.setBackgroundColor(Color.parseColor(colorCode))
        }
    }
}
```
이후 xml 에서 재정의한 속성에 알맞은 값을 넣어주면 `setBackgroundColor()` 함수로 들어간다.
```xml
	<TextView
    	android:layout_width="match_parent"
    	android:layout_height="wrap_content"
    	android:background="@{user.color}"
    	android:text="@{user.count + ``}" />
```

